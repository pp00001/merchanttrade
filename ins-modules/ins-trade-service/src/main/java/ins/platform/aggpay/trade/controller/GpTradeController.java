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

package ins.platform.aggpay.trade.controller;

import ins.platform.aggpay.common.web.BaseController;
import ins.platform.aggpay.trade.common.config.IsvConfig;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.service.GpTradeOrderService;
import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.vo.GgFeeParamVo;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mybank.bkmerchant.constant.DeniedPayTool;

/**
 * @author RipinYan
 * @ClassName: GpTradeController
 * @Description: 交易类API
 * @date 2018/10/12 下午2:57
 */

@RestController
@RequestMapping("/trade")
public class GpTradeController extends BaseController {

	@Autowired
	private GpTradeOrderService gpTradeOrderService;
	@Autowired
	private GgMerchantService ggMerchantService;
	@Autowired
	private GpTradeService gpTradeService;
	@Autowired
	private IsvConfig isvConfig;

	/**
	 * @Title: pay
	 * @Description: 移动刷卡支付（被扫）接口
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/pay/scanPay", method = RequestMethod.POST)
	public String scanPay(@RequestBody GpTradeOrderVo tradeOrderVo) {
		String logPrefix = "【移动刷卡支付-被扫】";

		String result;
		try {
			GgMerchantVo merchantVo = ggMerchantService.findMerchantByMerchentId(tradeOrderVo.getMerchantId());
			String[] deniedPayTool = merchantVo.getDeniedPayToolList().split(",");
			String[] payList = new String[2];
			for (int i = 0; i < deniedPayTool.length; i++) {
				if ("credit".equals(deniedPayTool[i])) {
				}
				switch (deniedPayTool[i]) {
					case DeniedPayTool.CREDIT_CARD:
						payList[i] = "credit";
						break;
					case DeniedPayTool.HUABEI:
						payList[i] = "pcredit";
						break;
				}
			}
			String channelType = tradeOrderVo.getChannelType();
			switch (channelType) {
				case "ALI":
					channelType = "01";
					break;
				case "WX":
					channelType = "02";
					break;
				case "QQ":
					channelType = "03";
					break;
				case "JD":
					channelType = "04";
					break;

			}
			// 默认T+1清算
			String feeType = "T1";
			List<GgFeeParamVo> feeParamList = merchantVo.getFeeParamList();
			for (int i = 0; i < feeParamList.size(); i++) {
				if (feeParamList.get(i).getChanneltype().equals(channelType)) {
					feeType = feeParamList.get(i).getFeetype().equals("01") ? "T0" : "T1";
				}
			}
			tradeOrderVo.setPayLimit(StringUtils.join(payList, ','));
			tradeOrderVo.setSettleType(feeType);
			GpTradeOrderVo resultVo = gpTradeService.scanPay(tradeOrderVo);
			result = JSON.toJSONString(resultVo);

		} catch (Exception e) {
			result = "移动刷卡支付（被扫）支付异常!";
			logger.error(result + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * @Title: notifyPayResult
	 * @Description: H5支付（主扫）
	 * @param requestXml 请求报文
	 * @throws
	 * @author Ripin Yan
	 * @return java.lang.String 响应报文
	 */
	@RequestMapping(value = "/pay/prePay", method = RequestMethod.POST)
	public String prePay(@RequestBody GpTradeOrderVo tradeOrderVo, HttpServletRequest request) {
		String logPrefix = "【H5支付-主扫】";
		JSONObject jo = new JSONObject();
		String userAgent = request.getHeader("User-Agent");
		String client = "alipay";
		String channelId = "ALIPAY_WAP";
        if(StringUtils.isBlank(userAgent)) {
            String errorMessage = "User-Agent为空！";
            logger.info("{}信息：{}", logPrefix, errorMessage);
	        jo.put("result", "failed");
	        jo.put("resMsg", errorMessage);
            return jo.toJSONString();
        }else {
            if(userAgent.contains("Alipay")) {
                client = "alipay";
                channelId = "ALI";
            }else if(userAgent.contains("MicroMessenger")) {
                client = "wx";
                channelId = "WX";
            }
        }

		if(client == null) {
			String errorMessage = "请使用微信或支付宝扫码";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("result", "failed");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}

		String result;
		try {
			GgMerchantVo merchantVo = ggMerchantService.findMerchantByMerchentId(tradeOrderVo.getMerchantId());
			String[] deniedPayTool = merchantVo.getDeniedPayToolList().split(",");
			String[] payList = new String[2];
			for (int i = 0; i < deniedPayTool.length; i++) {
				if ("credit".equals(deniedPayTool[i])) {
				}
				switch (deniedPayTool[i]) {
					case DeniedPayTool.CREDIT_CARD:
						payList[i] = "credit";
						break;
					case DeniedPayTool.HUABEI:
						payList[i] = "pcredit";
						break;
				}
			}
			String channelType = tradeOrderVo.getChannelType();
			switch (channelType) {
				case "ALI":
					channelType = "01";
					break;
				case "WX":
					channelType = "02";
					break;
				case "QQ":
					channelType = "03";
					break;
				case "JD":
					channelType = "04";
					break;

			}
			// 默认T+1清算
			String feeType = "T1";
			List<GgFeeParamVo> feeParamList = merchantVo.getFeeParamList();
			for (int i = 0; i < feeParamList.size(); i++) {
				if (feeParamList.get(i).getChanneltype().equals(channelType)) {
					feeType = feeParamList.get(i).getFeetype().equals("01") ? "T0" : "T1";
				}
			}
			tradeOrderVo.setPayLimit(StringUtils.join(payList, ','));
			tradeOrderVo.setSettleType(feeType);
			GpTradeOrderVo resultVo = gpTradeService.prePay(tradeOrderVo);
			result = JSON.toJSONString(resultVo);

		} catch (Exception e) {
			result = "移动刷卡支付（被扫）支付异常!";
			logger.error(result + e.getMessage(), e);
		}

		return result;
	}

}
