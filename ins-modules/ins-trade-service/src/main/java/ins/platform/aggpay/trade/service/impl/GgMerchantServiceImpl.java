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

import ins.platform.aggpay.trade.common.util.MapUtil;
import ins.platform.aggpay.trade.entity.GgMerchant;
import ins.platform.aggpay.trade.entity.GgMerchantDetail;
import ins.platform.aggpay.trade.mapper.GgMerchantDetailMapper;
import ins.platform.aggpay.trade.mapper.GgMerchantMapper;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.MerchantResVo;
import ins.platform.aggpay.trade.vo.PublicCall;
import ins.platform.aggpay.trade.vo.RegistResVo;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;
import ins.platform.aggpay.trade.vo.UploadPhotoVo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import sun.misc.BASE64Decoder;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.constant.BizTypeEnum;
import com.mybank.bkmerchant.merchant.Register;
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

	@Autowired
	private GgMerchantMapper ggMerchantMapper;

	@Autowired
	private GgMerchantDetailMapper ggMerchantDetailMapper;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public RegistResVo regist(GgMerchantVo register) {

		try {
			//			register.call();

//			Register c = new Register(register.getMerchantName(), register.getMerchantType(), register.getDealtype(), register.getSupportPrepayment
//					(), register.getSettleMode(), register.getMcc(), register.getGgMerchantDetailVo(), register.getTradeTypeList(), register
//					.getPayChannelList(), register.getDeniedPayToolList(), register.getFeeParamList(), register.getGgBankCardParamVo(), register
//					.getAuthCode(), register.getOutTradeNo(), register.getSupportStage(), register.getPartnerType(), register.getAlipaySource(),
//					register.getWechatChannel(), register.getRateVersion());

			PublicCall pc = new PublicCall(PublicCall.FUNCTION_REGISTER);
			pc.setBody(new HashMap<String,String>() {
				{
				    put("IsvOrgId", HttpsMain.IsvOrgId);
				    put("OutMerchantId", DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT) + RandomUtil.simpleUUID());
				    put("MerchantName", register.getMerchantName());
				    put("MerchantType", register.getMerchantType());
				    put("DealType", register.getDealtype());
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
			
			
			Map<String, Object> call = pc.call();
			
			RegistResVo rs = MapUtil.map2Obj(call,RegistResVo.class);
//			if(rs.getRespInfo() != null && "S".equals(rs.getRespInfo().getResultStatus())){
				//update merchant 入驻结果
				GgMerchant insert = new GgMerchant();
				BeanUtils.copyProperties(insert, register);
				insert.setOutMerchantId(rs.getOutMerchantId());
				insert.setOrderNo(rs.getOrderNo());

				System.out.println(insert.getOutMerchantId());
				ggMerchantMapper.insert(insert);

				
				GgMerchantDetail detail = new GgMerchantDetail();
				BeanUtils.copyProperties(detail,register.getGgMerchantDetailVo());
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
			Map<String, Object> call = rq.call();
			vo = MapUtil.map2Obj(call,RegisterQueryVo.class);
			if(vo.getRespInfo()!=null && "S".equals(vo.getRespInfo().getResultStatus())){
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
		PublicCall pc = new PublicCall(PublicCall.FUNCTION_MERCHANTQUERY);
		pc.setBody(new HashMap<String,String>() {
			{
				put("IsvOrgId",HttpsMain.IsvOrgId);
				put("MerchantId",merchantId);
			}
		});
		BASE64Decoder decoder = new BASE64Decoder();
		GgMerchantVo rs = null;
		try {
			Map<String, Object> result = pc.call();
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
	public MerchantResVo merchantFreeze(String merchantId,
			String freezeReason, String outTradeNo) {
		PublicCall pc = new PublicCall(PublicCall.FUNCTION_FREEZE);
		pc.setBody(new HashMap<String,String>() {
			{
				put("IsvOrgId",HttpsMain.IsvOrgId);
				put("MerchantId",merchantId);
				put("FreezeReason",freezeReason);
				put("OutTradeNo",UUID.randomUUID().toString());
			}
		});
		Map<String, Object> result = null;
		try {
			result = pc.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		MerchantResVo rs = MapUtil.map2Obj(result, MerchantResVo.class);
		return rs;
	}

	@Override
	public MerchantResVo merchantUnfreeze(String merchantId,
			String unfreezeReason, String outTradeNo) {

		PublicCall pc = new PublicCall(PublicCall.FUNCTION_UNFREEZE);
		pc.setBody(new HashMap<String,String>() {
			{
				put("IsvOrgId",HttpsMain.IsvOrgId);
				put("MerchantId",merchantId);
				put("UnfreezeReason",unfreezeReason);
				put("OutTradeNo",UUID.randomUUID().toString());
			}
		});
		Map<String, Object> result = null;
		try {
			result = pc.call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		MerchantResVo rs = MapUtil.map2Obj(result, MerchantResVo.class);
		return rs;
	}

}
