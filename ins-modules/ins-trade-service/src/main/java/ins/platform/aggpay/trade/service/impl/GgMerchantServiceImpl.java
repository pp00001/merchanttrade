/*
 * Copyright (c) 2018-2020, Ripin Yan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ins.platform.aggpay.trade.service.impl;

import ins.platform.aggpay.trade.config.TradeConfig;
import ins.platform.aggpay.trade.constant.TradeConstant;
import static ins.platform.aggpay.trade.constant.TradeConstant.RespInfo.RESULT_STATUS_SUCCESS;
import ins.platform.aggpay.trade.entity.GgBankCardParam;
import ins.platform.aggpay.trade.entity.GgFeeParam;
import ins.platform.aggpay.trade.entity.GgMerchant;
import ins.platform.aggpay.trade.entity.GgMerchantDetail;
import ins.platform.aggpay.trade.entity.GgWechatChannel;
import ins.platform.aggpay.trade.mapper.GgBankCardParamMapper;
import ins.platform.aggpay.trade.mapper.GgFeeParamMapper;
import ins.platform.aggpay.trade.mapper.GgMerchantDetailMapper;
import ins.platform.aggpay.trade.mapper.GgMerchantMapper;
import ins.platform.aggpay.trade.mapper.GgWechatChannelMapper;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.util.MapUtil;
import ins.platform.aggpay.trade.vo.GgBankCardParamVo;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantDetailVo;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.GgWechatChannelVo;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;
import ins.platform.aggpay.trade.vo.SmsVo;
import ins.platform.aggpay.trade.vo.UploadPhotoVo;
import sun.misc.BASE64Decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mybank.bkmerchant.merchant.UploadPhoto;

/**
 * <p>
 * 商户信息主表 服务实现类
 * </p>
 *
 * @author ripin
 * @since 2018-09-18
 */
@Service
public class GgMerchantServiceImpl extends ServiceImpl<GgMerchantMapper, GgMerchant> implements GgMerchantService {


    private static final Logger logger = LoggerFactory.getLogger(GgMerchantServiceImpl.class);

    @Autowired
    private GgMerchantMapper ggMerchantMapper;
    @Autowired
    private GgMerchantDetailMapper ggMerchantDetailMapper;
    @Autowired
    private GgBankCardParamMapper ggBankCardParamMapper;
    @Autowired
    private GgFeeParamMapper ggFeeParamMapper;
    @Autowired
    private GgWechatChannelMapper ggWechatChannelMapper;
    @Autowired
    private TradeConfig tradeConfig;

	@Override
	public void insert(GgMerchantVo register) {
		try {
			// 插入商户信息
			GgMerchant insert = new GgMerchant();
			BeanUtils.copyProperties(register, insert);
			ggMerchantMapper.insert(insert);

			String outMerchantId = register.getOutMerchantId();
			// 插入商户详情信息
			GgMerchantDetail detail = new GgMerchantDetail();
			BeanUtils.copyProperties(register.getGgMerchantDetailVo(), detail);
			detail.setOutMerchantId(outMerchantId);
			ggMerchantDetailMapper.insert(detail);

			// 插入手续费列表信息
			List<GgFeeParamVo> feeParamList = register.getFeeParamList();
			for (int i = 0; i < feeParamList.size(); i++) {
				GgFeeParam feeParam = new GgFeeParam();
				BeanUtils.copyProperties(feeParamList.get(i), feeParam);
				feeParam.setOutMerchantId(outMerchantId);
				ggFeeParamMapper.insert(feeParam);
			}

			// 插入清算卡信息
			GgBankCardParam bankCardParam = new GgBankCardParam();
			BeanUtils.copyProperties(register.getGgBankCardParamVo(), bankCardParam);
			bankCardParam.setOutMerchantId(outMerchantId);
			ggBankCardParamMapper.insert(bankCardParam);
		} catch (Exception e) {
			logger.error("商户保存数据库操作异常");
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GgMerchantVo regist(GgMerchantVo register) {

		GgMerchantVo rs;
		try {
			String outMerchantId = register.getOutMerchantId();
			ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_REGISTER);
			pc.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("OutMerchantId", outMerchantId);
					put("MerchantName", register.getMerchantName());
					put("MerchantType", register.getMerchantType());
					put("DealType", register.getDealType());
					put("SupportPrepayment", register.getSupportPrepayment());
					put("SettleMode", register.getSettleMode());
					put("Mcc", register.getMcc());
					put("MerchantDetail", register.getGgMerchantDetailVo().genJsonBase64());
					put("TradeTypeList", register.getTradeTypeList());
					put("PayChannelList", register.getPayChannelList());
					put("DeniedPayToolList", register.getDeniedPayToolList());
					put("FeeParamList", GgFeeParamVo.genJsonBase64(register.getFeeParamList()));
					put("BankCardParam", register.getGgBankCardParamVo().genJsonBase64());
					put("AuthCode", register.getAuthCode());
					put("OutTradeNo", register.getOutTradeNo());
					put("SupportStage", register.getSupportStage());
					put("PartnerType", register.getPartnerType());
					put("AlipaySource", register.getAlipaySource());
					put("WechatChannel", register.getWechatChannel());
					put("RateVersion", register.getRateVersion());
				}
			});

			Map<String, Object> resMap = pc.call(tradeConfig.getReqUrl());

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GgMerchantVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			boolean flag = false;
			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("商户入驻申请已受理!外部交易号：{}，订单号：{}", rs.getOutTradeNo(), rs.getOrderNo());
				register.setOrderNo(rs.getOrderNo());
				register.setRegisterStatus(TradeConstant.RegisterStatusEnum.Audit.getStatusCode());
				flag = true;
			} else {
				logger.info("商户入驻申请失败!外部交易号：{}", rs.getOutTradeNo());
			}
			GgMerchant query = new GgMerchant();
			query.setOutMerchantId(register.getOutMerchantId());
			query = ggMerchantMapper.selectOne(query);
			if (query != null) {
				if (flag) {
					//update merchant 入驻结果
					GgMerchant update = new GgMerchant();
					update.setId(query.getId());
					update.setOrderNo(register.getOrderNo());
					update.setRegisterStatus(register.getRegisterStatus());
					ggMerchantMapper.updateById(update);
				}
			} else {
				// 插入商户表
				insert(register);
			}

		} catch (Exception e) {
			logger.error("商户入驻异常！");
			throw new RuntimeException(e);
		}
		return rs;
	}

	@Override
	public RegisterQueryVo registerQuery(String orderNo) {
		RegisterQueryVo rs = new RegisterQueryVo();
		try {
			ApiCallUtil registerQuery = new ApiCallUtil(ApiCallUtil.FUNCTION_REGISTER_QUERY);
			registerQuery.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("OrderNo", orderNo);
				}
			});

			logger.info("开始调用registerQuery接口, url={}", tradeConfig.getReqUrl());
			Map<String, Object> resMap = registerQuery.call(tradeConfig.getReqUrl());

			// 数据转换
			rs = MapUtil.map2Obj(resMap, RegisterQueryVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("商户入驻结果查询成功!订单号：{}，入驻状态：{}", orderNo, TradeConstant.RegisterStatusEnum.getRegisterStatusByCode(rs.getRegisterStatus())
						.getStatusDesc());
				GgMerchant merchant = new GgMerchant().setOrderNo(orderNo);
				merchant = ggMerchantMapper.selectOne(merchant);
				if (merchant != null) {
					String registerStatus = merchant.getRegisterStatus();
					String queryStatus = rs.getRegisterStatus();
					try {
						if (!queryStatus.equals(registerStatus)) {
							GgMerchant update = new GgMerchant();
							update.setId(merchant.getId());
							update.setRegisterStatus(queryStatus);

							if (TradeConstant.RegisterStatusEnum.Success.getStatusCode().equals(queryStatus)) {
								update.setMerchantId(rs.getMerchantId());
								update.setAccountNo(rs.getAccountNo());
								update.setSmid(rs.getSmid());
								if (StringUtils.isNotBlank(rs.getWechatChannelList())) {
									String wechatChannels = new String(new BASE64Decoder().decodeBuffer(rs.getWechatChannelList()), "UTF-8");
									rs.setWechatChannelList(wechatChannels);
									List<GgWechatChannelVo> wechatChannelList = JSON.parseArray(wechatChannels, GgWechatChannelVo.class);
									rs.setWechatChannelVoList(wechatChannelList);
									if (wechatChannelList != null) {
										for (int i = 0; i < wechatChannelList.size(); i++) {
											GgWechatChannel ggWechatChannel = new GgWechatChannel();
											BeanUtils.copyProperties(wechatChannelList.get(i), ggWechatChannel);
											ggWechatChannel.setOutMerchantId(merchant.getOutMerchantId());
											ggWechatChannelMapper.insert(ggWechatChannel);
										}
									}
								}
							}
							ggMerchantMapper.updateById(update);
						}
					} catch (Exception e) {
						logger.error("更新商户入驻结果信息失败！", e);
					}

				}
			} else {
				logger.info("商户入驻结果查询失败!订单号：{}", orderNo);
			}

		} catch (Exception e) {
			logger.error("订单查询异常！", e);
		}

		return rs;
	}

	@Override
	public GgMerchantVo findMerchantById(Long id) {
		GgMerchantVo merchantVo = null;
		try {
			GgMerchant ggMerchant = ggMerchantMapper.selectById(id);
			if (ggMerchant != null) {
				merchantVo = assembleMerchantVo(ggMerchant);
			}
		} catch (Exception e) {
			logger.error("查询商户信息异常！" + e.getMessage(), e);
		}
		return merchantVo;
	}

	@Override
	public GgMerchantVo findMerchantByMerchantId(String merchantId) {
		GgMerchantVo merchantVo = null;
		try {
			GgMerchant ggMerchant = this.selectOne(new EntityWrapper<GgMerchant>().eq("merchant_id", merchantId));
			if (ggMerchant != null) {
				merchantVo = assembleMerchantVo(ggMerchant);
			}
		} catch (Exception e) {
			logger.error("查询商户信息异常！" + e.getMessage(), e);
		}
		return merchantVo;
	}


	/**
	 * assembleMerchantVo(组装商户信息vo)
	 *
	 * @Title: assembleMerchantVo
	 * @Description:
	 * @param merchant 商户信息
	 * @param outMerchantId 外部商户号
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.GgMerchantVo
	 */
	public GgMerchantVo assembleMerchantVo(GgMerchant merchant) {

		GgMerchantVo merchantVo = new GgMerchantVo();
		BeanUtils.copyProperties(merchant, merchantVo);

		// 商户详情
		String outMerchantId = merchant.getOutMerchantId();
		GgMerchantDetail ggMerchantDetail = new GgMerchantDetail().setOutMerchantId(outMerchantId);
		ggMerchantDetail = ggMerchantDetailMapper.selectOne(ggMerchantDetail);
		if (ggMerchantDetail != null) {
			GgMerchantDetailVo ggMerchantDetailVo = new GgMerchantDetailVo();
			BeanUtils.copyProperties(ggMerchantDetail, ggMerchantDetailVo);
			merchantVo.setGgMerchantDetailVo(ggMerchantDetailVo);
		}
		// 清算卡
		GgBankCardParam ggBankCardParam = new GgBankCardParam().setOutMerchantId(outMerchantId);
		ggBankCardParam = ggBankCardParamMapper.selectOne(ggBankCardParam);
		if (ggBankCardParam != null) {
			GgBankCardParamVo bcpVo = new GgBankCardParamVo();
			BeanUtils.copyProperties(ggBankCardParam, bcpVo);
			merchantVo.setGgBankCardParamVo(bcpVo);
		}
		// 手续费
		List<GgFeeParam> fpList = ggFeeParamMapper.selectList(new EntityWrapper<GgFeeParam>().eq("out_merchant_id", outMerchantId));
		if (fpList != null && fpList.size() > 0) {
			List<GgFeeParamVo> fpListVo = new ArrayList<>();
			for (int i = 0; i < fpList.size(); i++) {
				GgFeeParamVo fpVo = new GgFeeParamVo();
				BeanUtils.copyProperties(fpList.get(i), fpVo);
				fpListVo.add(fpVo);
			}
			merchantVo.setFeeParamList(fpListVo);
		}
		return merchantVo;
	}

	@Override
	public RespInfoVo sendSmsCode(SmsVo smsVo) {
		RespInfoVo rs = null;
		try {
			String outTradeNo = ApiCallUtil.generateOutTradeNo();
			smsVo.setOutTradeNo(outTradeNo);
			String bizType = smsVo.getBizType();
			ApiCallUtil sms = new ApiCallUtil(ApiCallUtil.FUNCTION_SEND_SMS_CODE);
			sms.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("BizType", bizType);
					if ("02".equals(bizType) || "03".equals(bizType) || "05".equals(bizType)) {
						put("MerchantId", smsVo.getMerchantId());
					}
					if ("01".equals(bizType) || "03".equals(bizType) || "04".equals(bizType)) {
						put("Mobile", smsVo.getMobile());
					}
					put("OutTradeNo", outTradeNo);
				}
			});

			logger.info("开始调用sendSmsCode接口, url={}", tradeConfig.getReqUrl());
			Map<String, Object> resMap = sms.call(tradeConfig.getReqUrl());

			// 数据转换
			rs = MapUtil.map2Obj((Map) resMap.get("respInfo"), RespInfoVo.class);
			if (rs != null && RESULT_STATUS_SUCCESS.equals(rs.getResultStatus())) {
				logger.info("发送短信成功!外部订单号：{}", outTradeNo);
			} else {
				logger.info("发送短信失败!外部订单号：{}，失败原因：{}", outTradeNo, rs != null ? rs.getResultMsg() : "");
			}

		} catch (Exception e) {
			logger.error("发送短信异常！", e);
		}

		return rs;
	}

	@Override
	public UploadPhotoVo uploadPhoto(UploadPhotoVo uploadPhotoVo) {
		UploadPhotoVo rs = null;
		try {
			String outTradeNo = ApiCallUtil.generateOutTradeNo();
			uploadPhotoVo.setOutTradeNo(outTradeNo);
			UploadPhoto uploadPhoto = new UploadPhoto(tradeConfig.getIsvOrgId(), tradeConfig.getAppId(), tradeConfig.getVersion(), tradeConfig
					.getAppPrivateKey(), uploadPhotoVo.getPhotoType(), outTradeNo, uploadPhotoVo.getPicture());
			Map<String, Object> result = uploadPhoto.call();
			rs = MapUtil.map2Obj(result, UploadPhotoVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("上传图片成功!外部订单号：{}，图片url：{}", outTradeNo, rs.getPhotoUrl());
			} else {
				logger.info("上传图片失败!外部订单号：{}，失败原因：{}", outTradeNo, respInfoVo != null ? respInfoVo.getResultMsg() : "");
			}
		} catch (Exception e) {
			logger.error("上传图片异常！", e);
		}

		return rs;
	}

	@Override
	public RespInfoVo updateMerchant(GgMerchantVo merchantVo) {
		RespInfoVo rs;
		try {
			ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_MERCHANT_UPDATE);
			pc.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("MerchantId", merchantVo.getMerchantId());
					put("DealType", merchantVo.getDealType());
					put("SupportPrepayment", merchantVo.getSupportPrepayment());
					put("SettleMode", merchantVo.getSettleMode());
					put("Mcc", merchantVo.getMcc());
					if (merchantVo.getGgMerchantDetailVo() != null) {
						put("MerchantDetail", merchantVo.getGgMerchantDetailVo().genJsonBase64());
					}
					put("TradeTypeList", merchantVo.getTradeTypeList());
					put("PayChannelList", merchantVo.getPayChannelList());
					put("DeniedPayToolList", merchantVo.getDeniedPayToolList());
					if (merchantVo.getFeeParamList() != null) {
						put("FeeParamList", GgFeeParamVo.genJsonBase64(merchantVo.getFeeParamList()));
					}
					if (merchantVo.getGgBankCardParamVo() != null) {
						put("BankCardParam", merchantVo.getGgBankCardParamVo().genJsonBase64());
					}
					put("OutTradeNo", merchantVo.getOutTradeNo());
					put("SupportStage", merchantVo.getSupportStage());
					put("AlipaySource", merchantVo.getAlipaySource());
				}
			});

			Map<String, Object> resMap = pc.call(tradeConfig.getReqUrl());
			// 数据转换
			rs = MapUtil.map2Obj((Map) resMap.get("respInfo"), RespInfoVo.class);

			if (rs != null && RESULT_STATUS_SUCCESS.equals(rs.getResultStatus())) {
				logger.info("商户信息修改成功!外部交易号：{}", merchantVo.getOutTradeNo());

				// 更新商户主表
				GgMerchant merchant = new GgMerchant();
				BeanUtils.copyProperties(merchantVo, merchant);
				ggMerchantMapper.updateById(merchant);
				// 更新商户详情
				GgMerchantDetailVo merchantDetailVo = merchantVo.getGgMerchantDetailVo();
				if (merchantDetailVo != null) {
					GgMerchantDetail merchantDetail = new GgMerchantDetail();
					BeanUtils.copyProperties(merchantDetailVo, merchantDetail);
					ggMerchantDetailMapper.updateById(merchantDetail);
				}
				// 更新清算卡信息
				if (merchantVo.getGgBankCardParamVo() != null) {
					GgBankCardParam bankCardParam = new GgBankCardParam();
					BeanUtils.copyProperties(merchantVo.getGgBankCardParamVo(), bankCardParam);
					ggBankCardParamMapper.updateById(bankCardParam);
				}
				// 更新手续费列表
				List<GgFeeParamVo> feeParamList = merchantVo.getFeeParamList();
				if (merchantVo.getFeeParamList() != null && merchantVo.getFeeParamList().size() > 0) {
					for (int i = 0; i < feeParamList.size(); i++) {
						GgFeeParamVo feeParamVo = feeParamList.get(i);
						GgFeeParam feeParam = new GgFeeParam();
						BeanUtils.copyProperties(feeParamVo, feeParam);
						ggFeeParamMapper.updateById(feeParam);
					}
				}

			} else {
				logger.info("商户信息修改失败，不更新db库数据!商户号：{}，失败原因：{}", merchantVo.getMerchantId(), rs != null ? rs.getResultMsg() : "rs is null");
			}

		} catch (Exception e) {
			logger.error("商户信息修改异常！");
			throw new RuntimeException(e);
		}
		return rs;
	}

	@Override
	public GgMerchantVo merchantQuery(String merchantId) {
		ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_MERCHANT_QUERY);
		pc.setBody(new HashMap<String, String>() {
			{
				put("IsvOrgId", tradeConfig.getIsvOrgId());
				put("MerchantId", merchantId);
			}
		});
		GgMerchantVo rs = null;
		try {
			Map<String, Object> result = pc.call(tradeConfig.getReqUrl());
			rs = MapUtil.map2Obj(result, GgMerchantVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();

			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("商户信息查询成功!商户号：{}", merchantId);

				BASE64Decoder decoder = new BASE64Decoder();
				// 商户详情信息
				String merchantDetailJson = new String(decoder.decodeBuffer((String) result.get("merchantDetail")));
				GgMerchantDetailVo ggMerchantDetailVo = JSON.parseObject(merchantDetailJson).toJavaObject(GgMerchantDetailVo.class);
				rs.setGgMerchantDetailVo(ggMerchantDetailVo);
				// 费率列表
				String feeParamListJson = new String(decoder.decodeBuffer((String) result.get("feeParamList")));
				List<GgFeeParamVo> ggFeeParamVos = JSON.parseArray(feeParamListJson, GgFeeParamVo.class);
				rs.setFeeParamList(ggFeeParamVos);
				// 清算卡信息
				String bankCardParamJson = new String(decoder.decodeBuffer((String) result.get("bankCardParam")));
				GgBankCardParamVo ggBankCardParamVo = JSON.parseObject(bankCardParamJson).toJavaObject(GgBankCardParamVo.class);
				rs.setGgBankCardParamVo(ggBankCardParamVo);
				// 微信渠道号进驻信息
				String wechatChannelListJson = new String(decoder.decodeBuffer((String) result.get("wechatChannelList")));
				List<GgWechatChannelVo> wechatChannelList = JSON.parseArray(wechatChannelListJson, GgWechatChannelVo.class);
				rs.setWechatChannelList(wechatChannelList);
				rs.setWechatChannel(wechatChannelListJson);
			} else {
				logger.info("商户信息查询失败!商户号：{}", merchantId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

    @Override
    public RespInfoVo merchantFreeze(String merchantId, String freezeReason) {
        ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_FREEZE);
        RespInfoVo rs = null;
        String outTradeNo = ApiCallUtil.generateOutTradeNo();
        pc.setBody(new HashMap<String, String>() {
            {
                put("IsvOrgId", tradeConfig.getIsvOrgId());
                put("MerchantId", merchantId);
                put("FreezeReason", freezeReason);
                put("OutTradeNo", outTradeNo);
            }
        });

        try {
            Map<String, Object> result = pc.call(tradeConfig.getReqUrl());
            rs = MapUtil.map2Obj((Map) result.get("respInfo"), RespInfoVo.class);
            if (rs != null && RESULT_STATUS_SUCCESS.equals(rs.getResultStatus())) {
                logger.info("商户关闭申请已受理!外部交易号：{}", outTradeNo);
                //查询对应的商户信息
                GgMerchant query = new GgMerchant();
                query.setMerchantId(merchantId);
                query = ggMerchantMapper.selectOne(query);
                if (query != null) {
                    //更新商户表
                    GgMerchant update = new GgMerchant();
                    update.setId(query.getId());
                    update.setFreezeInd("0");
                    update.setFreezeReason(freezeReason);
                    int resultCode = ggMerchantMapper.updateById(update);
                    if (resultCode != 0) {
                        logger.info("数据库更新完毕！");
                    } else {
                        logger.error("数据库更新失败！");
                    }
                } else {
                    logger.error("数据库查询商户失败！");
                }
            } else {
                logger.info("商户关闭申请失败!外部交易号：{}", outTradeNo);
            }
        } catch (Exception e) {
            logger.error("商户关闭异常！异常原因：" + e.getMessage(), e);
        }

        return rs;
    }

    @Override
    public RespInfoVo merchantUnfreeze(String merchantId, String unFreezeReason) {
        ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_UNFREEZE);
        RespInfoVo rs = null;
        String outTradeNo = ApiCallUtil.generateOutTradeNo();
        pc.setBody(new HashMap<String, String>() {
            {
                put("IsvOrgId", tradeConfig.getIsvOrgId());
                put("MerchantId", merchantId);
                put("UnfreezeReason", unFreezeReason);
                put("OutTradeNo", outTradeNo);
            }
        });

        try {
            Map<String, Object> result = pc.call(tradeConfig.getReqUrl());
            rs = MapUtil.map2Obj((Map) result.get("respInfo"), RespInfoVo.class);
            if (rs != null && RESULT_STATUS_SUCCESS.equals(rs.getResultStatus())) {
                logger.info("商户开启申请已受理!外部交易号：{}", outTradeNo);
                //查询对应的商户信息
                GgMerchant query = new GgMerchant();
                query.setMerchantId(merchantId);
                query = ggMerchantMapper.selectOne(query);
                if (query != null) {
                    //更新商户表
                    GgMerchant update = new GgMerchant();
                    update.setId(query.getId());
                    update.setFreezeInd("1");
                    update.setFreezeReason(unFreezeReason);
                    int resultCode = ggMerchantMapper.updateById(update);
                    if (resultCode != 0) {
                        logger.info("数据库更新完毕！");
                    } else {
                        logger.error("数据库更新失败！");
                    }
                } else {
                    logger.error("数据库查询商户失败！");
                }
            } else {
                logger.info("商户开启申请失败!外部交易号：{}", outTradeNo);
            }
        } catch (Exception e) {
            logger.error("商户开启异常！异常原因：" + e.getMessage(), e);
        }

        return rs;
    }


}
