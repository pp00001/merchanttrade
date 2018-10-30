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
import ins.platform.aggpay.trade.service.GgXmlLogService;
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
import java.util.UUID;
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
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.merchant.UpdateMerchant;
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
	private GgXmlLogService ggXmlLogService;
	@Autowired
	private TradeConfig tradeConfig;


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public GgMerchantVo regist(GgMerchantVo register) {

		GgMerchantVo rs = new GgMerchantVo();
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
				// 插入商户信息
				GgMerchant insert = new GgMerchant();
				BeanUtils.copyProperties(register, insert);
				ggMerchantMapper.insert(insert);

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
				merchantVo = new GgMerchantVo();
				BeanUtils.copyProperties(ggMerchant, merchantVo);

				String outMerchantId = ggMerchant.getOutMerchantId();
				GgMerchantDetail ggMerchantDetail = new GgMerchantDetail().setOutMerchantId(outMerchantId);
				ggMerchantDetail = ggMerchantDetailMapper.selectOne(ggMerchantDetail);
				if (ggMerchantDetail != null) {
					GgMerchantDetailVo ggMerchantDetailVo = new GgMerchantDetailVo();
					BeanUtils.copyProperties(ggMerchantDetail, ggMerchantDetailVo);
					merchantVo.setGgMerchantDetailVo(ggMerchantDetailVo);
				}
				GgBankCardParam ggBankCardParam = new GgBankCardParam().setOutMerchantId(outMerchantId);
				ggBankCardParam = ggBankCardParamMapper.selectOne(ggBankCardParam);
				if (ggBankCardParam != null) {
					GgBankCardParamVo bcpVo = new GgBankCardParamVo();
					BeanUtils.copyProperties(ggBankCardParam, bcpVo);
					merchantVo.setGgBankCardParamVo(bcpVo);
				}

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
			}
		} catch (Exception e) {
			logger.error("查询商户信息异常！" + e.getMessage(), e);
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
	public UploadPhotoVo uploadPhoto(UploadPhoto uploadPhoto) {
		UploadPhotoVo rs = null;
		try {
			Map<String, Object> result = uploadPhoto.call();
			rs = MapUtil.map2Obj(result, UploadPhotoVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rs;
	}

	@Override
	public RespInfoVo updateMerchant(UpdateMerchant updateMerchant) {
		//		UpdateMerchant.updateMerchant(merchantId, outTradeNo);

		return null;
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
		BASE64Decoder decoder = new BASE64Decoder();
		GgMerchantVo rs = null;
		try {
			Map<String, Object> result = pc.call(tradeConfig.getReqUrl());
			rs = MapUtil.map2Obj(result, GgMerchantVo.class);
			result.put("merchantDetail", new String(decoder.decodeBuffer((String) result.get("merchantDetail")), "UTF-8"));
			result.put("feeParamList", new String(decoder.decodeBuffer((String) result.get("feeParamList"))));
			result.put("bankCardParam", new String(decoder.decodeBuffer((String) result.get("bankCardParam"))));
			result.put("wechatChannelList", new String(decoder.decodeBuffer((String) result.get("wechatChannelList"))));
			System.out.println((String) result.get("merchantDetail"));
			System.out.println((String) result.get("feeParamList"));
			System.out.println((String) result.get("bankCardParam"));
			System.out.println((String) result.get("wechatChannelList"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public RespInfoVo merchantFreeze(String merchantId, String freezeReason, String outTradeNo) {
		ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_FREEZE);
		pc.setBody(new HashMap<String, String>() {
			{
				put("IsvOrgId", tradeConfig.getIsvOrgId());
				put("MerchantId", merchantId);
				put("FreezeReason", freezeReason);
				put("OutTradeNo", UUID.randomUUID().toString());
			}
		});
		Map<String, Object> result = null;
		try {
			result = pc.call(tradeConfig.getReqUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		RespInfoVo rs = MapUtil.map2Obj(result, RespInfoVo.class);
		return rs;
	}

	@Override
	public RespInfoVo merchantUnfreeze(String merchantId, String unfreezeReason, String outTradeNo) {

		ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_UNFREEZE);
		pc.setBody(new HashMap<String, String>() {
			{
				put("IsvOrgId", tradeConfig.getIsvOrgId());
				put("MerchantId", merchantId);
				put("UnfreezeReason", unfreezeReason);
				put("OutTradeNo", UUID.randomUUID().toString());
			}
		});
		Map<String, Object> result = null;
		try {
			result = pc.call(tradeConfig.getReqUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		RespInfoVo rs = MapUtil.map2Obj(result, RespInfoVo.class);
		return rs;
	}


}
