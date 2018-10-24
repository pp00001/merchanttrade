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
import ins.platform.aggpay.trade.entity.GgBankCardParam;
import ins.platform.aggpay.trade.entity.GgFeeParam;
import ins.platform.aggpay.trade.entity.GgMerchant;
import ins.platform.aggpay.trade.entity.GgMerchantDetail;
import ins.platform.aggpay.trade.mapper.GgBankCardParamMapper;
import ins.platform.aggpay.trade.mapper.GgFeeParamMapper;
import ins.platform.aggpay.trade.mapper.GgMerchantDetailMapper;
import ins.platform.aggpay.trade.mapper.GgMerchantMapper;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.util.MapUtil;
import ins.platform.aggpay.trade.vo.GgBankCardParamVo;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantDetailVo;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.MerchantResVo;
import ins.platform.aggpay.trade.vo.RegistResVo;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;
import ins.platform.aggpay.trade.vo.UploadPhotoVo;
import sun.misc.BASE64Decoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.constant.BizTypeEnum;
import com.mybank.bkmerchant.merchant.RegisterQuery;
import com.mybank.bkmerchant.merchant.UpdateMerchant;
import com.mybank.bkmerchant.merchant.UploadPhoto;
import com.mybank.bkmerchant.trade.SendSmsCode;
import com.xiaoleilu.hutool.date.DatePattern;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.RandomUtil;

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
	private TradeConfig tradeConfig;


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RegistResVo regist(GgMerchantVo register) {

		try {
			//			register.call();

			//			Register c = new Register(register.getMerchantName(), register.getMerchantType(), register.getDealtype(), register
			// .getSupportPrepayment
			//					(), register.getSettleMode(), register.getMcc(), register.getGgMerchantDetailVo(), register.getTradeTypeList(),
			// register
			//					.getPayChannelList(), register.getDeniedPayToolList(), register.getFeeParamList(), register.getGgBankCardParamVo(),
			// register
			//					.getAuthCode(), register.getOutTradeNo(), register.getSupportStage(), register.getPartnerType(), register
			// .getAlipaySource(),
			//					register.getWechatChannel(), register.getRateVersion());

			ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_REGISTER);
			pc.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", HttpsMain.IsvOrgId);
					put("OutMerchantId", DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + RandomUtil.simpleUUID());
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


			Map<String, Object> call = pc.call(tradeConfig.getReqUrl());

			RegistResVo rs = MapUtil.map2Obj(call, RegistResVo.class);
			//			if(rs.getRespInfo() != null && "S".equals(rs.getRespInfo().getResultStatus())){
			//update merchant 入驻结果
			GgMerchant insert = new GgMerchant();
			BeanUtils.copyProperties(register, insert);
			insert.setOutMerchantId(rs.getOutMerchantId());
			insert.setOrderNo(rs.getOrderNo());

			System.out.println(insert.getOutMerchantId());
			ggMerchantMapper.insert(insert);


			GgMerchantDetail detail = new GgMerchantDetail();
			BeanUtils.copyProperties(register.getGgMerchantDetailVo(), detail);
			ggMerchantDetailMapper.insert(detail);


			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public RegisterQueryVo registerQuery(String orderNo) {
		RegisterQuery rq = new RegisterQuery(orderNo);
		RegisterQueryVo vo = null;
		try {
			Map<String, Object> call = rq.call(tradeConfig.getReqUrl());
			vo = MapUtil.map2Obj(call, RegisterQueryVo.class);
			if (vo.getRespInfo() != null && "S".equals(vo.getRespInfo().getResultStatus())) {
				GgMerchant query = new GgMerchant();
				query.setOrderNo(orderNo);
				GgMerchant update = ggMerchantMapper.selectOne(query);
				//				ggMerchantMapper.selectList(new EntityWrapper<GgMerchant>().isNotNull("order_no"));
				System.out.println();
				update.setMerchantId(vo.getMerchantId());
				update.setRegisterStatus(vo.getRegisterStatus());
				update.setFailReason(vo.getFailReason());
				ggMerchantMapper.updateById(update);
			}
			System.out.println(vo.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vo;
	}

	@Override
	public GgMerchantVo findMerchantById(Long id) {
		GgMerchantVo merchantVo = null;
		try {
			GgMerchant ggMerchant = ggMerchantMapper.selectById(id);
			if (ggMerchant != null) {
				merchantVo = new GgMerchantVo();
				BeanUtils.copyProperties(merchantVo, ggMerchant);


				String outMerchantId = ggMerchant.getOutMerchantId();
				GgMerchantDetail ggMerchantDetail = new GgMerchantDetail().setOutMerchantId(outMerchantId);
				ggMerchantDetail = ggMerchantDetailMapper.selectOne(ggMerchantDetail);
				if (ggMerchantDetail != null) {
					GgMerchantDetailVo ggMerchantDetailVo = new GgMerchantDetailVo();
					BeanUtils.copyProperties(ggMerchantDetailVo, ggMerchantDetail);
					merchantVo.setGgMerchantDetailVo(ggMerchantDetailVo);
				}
				GgBankCardParam ggBankCardParam = new GgBankCardParam().setOutMerchantId(outMerchantId);
				ggBankCardParam = ggBankCardParamMapper.selectOne(ggBankCardParam);
				if (ggBankCardParam != null) {
					GgBankCardParamVo bcpVo = new GgBankCardParamVo();
					BeanUtils.copyProperties(bcpVo, ggBankCardParam);
					merchantVo.setGgBankCardParamVo(bcpVo);
				}

				List<GgFeeParam> fpList = ggFeeParamMapper.selectList(new EntityWrapper<GgFeeParam>().eq("out_merchant_id", outMerchantId));
				if (fpList != null && fpList.size() > 0) {
					List<GgFeeParamVo> fpListVo = new ArrayList<>();
					for (int i = 0; i < fpList.size(); i++) {
						GgFeeParamVo fpVo = new GgFeeParamVo();
						BeanUtils.copyProperties(fpVo, fpList.get(i));
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
	public MerchantResVo sendsmscode(SendSmsCode sendSmsCode) {
		MerchantResVo rs = null;
		try {
			Map<String, Object> result = SendSmsCode.sendSmsCodeReturnMap(sendSmsCode.getMerchantId(), sendSmsCode.getOutTradeNo(), BizTypeEnum
					.getBizTypeByCode(sendSmsCode.getBizType()), sendSmsCode.getMobile());
			rs = MapUtil.map2Obj(result, MerchantResVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public UploadPhotoVo uploadphoto(UploadPhoto uploadPhoto) {
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
	public MerchantResVo updateMerchant(UpdateMerchant updateMerchant) {
		//		UpdateMerchant.updateMerchant(merchantId, outTradeNo);

		return null;
	}

	@Override
	public GgMerchantVo merchantQuery(String merchantId) {
		ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_MERCHANTQUERY);
		pc.setBody(new HashMap<String, String>() {
			{
				put("IsvOrgId", HttpsMain.IsvOrgId);
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
	public MerchantResVo merchantFreeze(String merchantId, String freezeReason, String outTradeNo) {
		ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_FREEZE);
		pc.setBody(new HashMap<String, String>() {
			{
				put("IsvOrgId", HttpsMain.IsvOrgId);
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
		MerchantResVo rs = MapUtil.map2Obj(result, MerchantResVo.class);
		return rs;
	}

	@Override
	public MerchantResVo merchantUnfreeze(String merchantId, String unfreezeReason, String outTradeNo) {

		ApiCallUtil pc = new ApiCallUtil(ApiCallUtil.FUNCTION_UNFREEZE);
		pc.setBody(new HashMap<String, String>() {
			{
				put("IsvOrgId", HttpsMain.IsvOrgId);
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
		MerchantResVo rs = MapUtil.map2Obj(result, MerchantResVo.class);
		return rs;
	}


}
