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
import static ins.platform.aggpay.trade.constant.TradeConstant.TradeStatus.TRADE_STATUS_PAYING;
import static ins.platform.aggpay.trade.constant.TradeConstant.TradeStatus.TRADE_STATUS_REFUNDING;
import static ins.platform.aggpay.trade.constant.TradeConstant.TradeStatus.TRADE_STATUS_SUCC;
import ins.platform.aggpay.trade.entity.GgXmlLog;
import com.jcraft.jsch.ChannelSftp;
import ins.platform.aggpay.trade.config.SftpConfig;
import ins.platform.aggpay.trade.entity.GpRefundOrder;
import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.mapper.GpRefundOrderMapper;
import ins.platform.aggpay.trade.mapper.GpTradeOrderMapper;
import ins.platform.aggpay.trade.service.GgXmlLogService;
import ins.platform.aggpay.trade.service.GpTradeService;
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.util.MapUtil;
import ins.platform.aggpay.trade.util.SFTP;
import ins.platform.aggpay.trade.util.TXT2ExcelUtil;
import ins.platform.aggpay.trade.vo.GpRefundOrderVo;
import ins.platform.aggpay.trade.vo.GpTradeOrderVo;
import ins.platform.aggpay.trade.vo.RefundVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;
import com.xiaoleilu.hutool.date.DatePattern;

/**
 * <p>
 * 交易服务实现类
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
@Service
public class GpTradeServiceImpl implements GpTradeService {

	private static final Logger logger = LoggerFactory.getLogger(GpTradeServiceImpl.class);

	@Autowired
	private GpTradeOrderMapper gpTradeOrderMapper;
	@Autowired
	private GpRefundOrderMapper gpRefundOrderMapper;
	@Autowired
	private TradeConfig tradeConfig;
	@Autowired
	private GgXmlLogService ggXmlLogService;
	@Autowired
	private SftpConfig sftpConfig;

	@Override
	public GpTradeOrderVo scanPay(GpTradeOrderVo tradeOrderVo) {
		//账户余额查询
		String function = "ant.mybank.bkmerchanttrade.pay";

		XmlUtil xmlUtil = new XmlUtil();
		Map<String, String> form = new HashMap<String, String>();
		form.put("Function", function);
		form.put("ReqTime", new Timestamp(System.currentTimeMillis()).toString());
		//reqMsgId每次报文必须都不一样
		form.put("ReqMsgId", UUID.randomUUID().toString());
		// 成功authcode
		// 28763443825664394
		// 微信ok-134621141753364349-134723186961316101
		form.put("AuthCode", "134621141753364349");
		form.put("AuthCode", "286664990659481080");
		form.put("OutTradeNo", UUID.randomUUID().toString().replace("-", ""));
		form.put("Body", "反扫测试-碧螺春9");
		form.put("GoodsTag", "test");
		form.put("GoodsDetail", "test");
		form.put("TotalAmount", "3");
		form.put("Currency", "CNY");
		form.put("MerchantId", HttpsMain.merchantId);
		form.put("IsvOrgId", HttpsMain.IsvOrgId);
		form.put("ChannelType", "WX");
		form.put("ChannelType", "ALI");
		//        form.put("OperatorId","test");
		//        form.put("StoreId","test");
		//        form.put("DeviceId","test");
		form.put("DeviceCreateIp", "112.97.59.21");
		form.put("ExpireExpress", "120");
		form.put("SettleType", "T1");
		form.put("Attach", "附加信息");
		form.put("PayLimit", "pcredit");
		form.put("DiscountableAmount", "1");
		form.put("UndiscountableAmount", "2");
		//form.put("AlipayStoreId","支付宝的店铺编号");
		form.put("SysServiceProviderId", "2018090700000286");
		//form.put("CheckLaterNm","");
		form.put("SubAppId", "wx62a55dbdd041bb1d");
		//        form.put("SpecifySubMerchId","N");
		//        form.put("ChannelId","240824008");
		//        form.put("SubMerchId","242972555");

		//封装报文
		try {
			String param = xmlUtil.format(form, function);
			if (HttpsMain.isSign) {//生产环境需进行rsa签名
				param = XmlSignUtil.sign(param);
			}
			System.out.println("-------------------------");
			System.out.println("---------REQUEST---------");
			System.out.println("-------------------------");
			System.out.println(param);

			//发送请求
			String response = HttpsMain.httpsReq(HttpsMain.payUrl, param);

			System.out.println("-------------------------");
			System.out.println("---------RESPONSE--------");
			System.out.println("-------------------------");
			System.out.println(response);
			if (HttpsMain.isSign) {//生产环境需进行rsa验签
				if (!XmlSignUtil.verify(response)) {
					throw new Exception("验签失败");
				}
			}
			//解析报文
			Map<String, Object> resMap = xmlUtil.parse(response, function);
			//结果消息
			String b = (String) resMap.get("OutTradeNo");
			System.out.println(b);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public GpTradeOrderVo prePay(GpTradeOrderVo tradeOrderVo) {

		GgXmlLog xmlLog = new GgXmlLog();
		GpTradeOrderVo rs = new GpTradeOrderVo();
		try {
			String function = ApiCallUtil.FUNCTION_PRE_PAY;
			xmlLog.setFunction(function);
			xmlLog.setReqTime(new Date());
			tradeOrderVo.setOutTradeNo(ApiCallUtil.generateOutTradeNo());

			ApiCallUtil prePay = new ApiCallUtil(function);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("OutTradeNo", tradeOrderVo.getOutTradeNo());
					put("Body", tradeOrderVo.getBody());
					put("GoodsTag", tradeOrderVo.getGoodsTag());
					put("GoodsDetail", tradeOrderVo.getGoodsDetail());
					put("TotalAmount", String.valueOf(tradeOrderVo.getTotalAmount()));
					put("Currency", "CNY");
					put("MerchantId", tradeOrderVo.getMerchantId());
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("ChannelType", tradeOrderVo.getChannelType());
					put("OpenId", tradeOrderVo.getOpenId());
					put("OperatorId", tradeOrderVo.getOperatorId());
					put("StoreId", tradeOrderVo.getStoreId());
					put("DeviceId", tradeOrderVo.getDeviceId());
					put("DeviceCreateIp", tradeOrderVo.getDeviceCreateIp());
					if (tradeOrderVo.getExpireExpress() != null && tradeOrderVo.getExpireExpress() > 0) {
						put("ExpireExpress", String.valueOf(tradeOrderVo.getExpireExpress()));
					} else {
						put("ExpireExpress", tradeConfig.getExpireExpress());
					}
					put("SettleType", tradeOrderVo.getSettleType());
					put("Attach", tradeOrderVo.getAttach());
					put("NotifyUrl", tradeOrderVo.getNotifyUrl());
					put("PayLimit", tradeOrderVo.getPayLimit());
					if (TradeConstant.CHANNEL_TYPE_WX.equals(tradeOrderVo.getChannelType())) {
						put("SubAppId", tradeOrderVo.getSubAppId());
						put("SpecifySubMerchId", tradeOrderVo.getSpecifySubMerchId());
						put("ChannelId", tradeOrderVo.getChannelId());
						put("SubMerchId", tradeOrderVo.getSubMerchId());
					} else {
						put("DiscountableAmount", tradeOrderVo.getDiscountableAmount() == null ? null : String.valueOf(tradeOrderVo
								.getDiscountableAmount()));
						put("UndiscountableAmount", tradeOrderVo.getUndiscountableAmount() == null ? null : String.valueOf(tradeOrderVo
								.getUndiscountableAmount()));
						put("AlipayStoreId", tradeOrderVo.getAlipayStoreId());
						put("SysServiceProviderId", tradeOrderVo.getSysServiceProviderId());
						put("CheckLaterNm", tradeOrderVo.getCheckLaterNm());

					}
				}
			});

			logger.info("开始调用{}接口, url={}", function, tradeConfig.getPayUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getPayUrl());

			// 插入报文日志表

			xmlLog.setRequestXml((String) resMap.get("requestXml"));
			xmlLog.setResponseXml((String) resMap.get("responseXml"));
			xmlLog.setRespTime(DateUtils.parseDate((String) resMap.get("RespTime"), DatePattern.PURE_DATETIME_PATTERN));

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpTradeOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();
			xmlLog.setResultStatus(respInfoVo.getResultStatus());
			xmlLog.setResultCode(respInfoVo.getResultCode());
			xmlLog.setResultMsg(respInfoVo.getResultMsg());

			tradeOrderVo.setRespInfo(respInfoVo);
			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("订单创建成功!外部交易号：{}，订单号：{}", rs.getOutTradeNo(), rs.getOrderNo());
				tradeOrderVo.setOrderNo(rs.getOrderNo());
				tradeOrderVo.setPrePayId(rs.getPrePayId());
				tradeOrderVo.setPayInfo(rs.getPayInfo());
				tradeOrderVo.setTradeStatus(TRADE_STATUS_PAYING);
			} else {
				logger.info("订单创建失败!外部交易号：{}", rs.getOutTradeNo());
				tradeOrderVo.setValidInd("0");
			}

			GpTradeOrder gpTradeOrder = new GpTradeOrder();
			BeanUtils.copyProperties(tradeOrderVo, gpTradeOrder);
			gpTradeOrderMapper.insert(gpTradeOrder);

		} catch (Exception e) {
			xmlLog.setFlag("0");
			logger.error("主扫支付异常！" + e.getMessage()+"##", e);
			throw new RuntimeException("主扫支付异常" + e.getMessage());
		} finally {
			ggXmlLogService.insert(xmlLog);
		}

		return rs;
	}


	public String downLoadBill(String billDate) {

		//获取当前时间(YYYYMMdd)
		DateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
		DateFormat dfMonth = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		String date = dfDate.format(calendar.getTime());
		String month = dfMonth.format(calendar.getTime());

//		SFTP sftp = new SFTP();
		Map<String, Object> resultMap = new SFTP().connect(sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getUsername(), sftpConfig.getPassword());

		// resultMap-code
		if (resultMap != null || resultMap.size() > 1) {
			try {
				ChannelSftp sftp = (ChannelSftp) resultMap.get("sftp");//获取连接的sftp

				String saveFile = tradeConfig.getIsvOrgId() + "_" + billDate + ".txt";
				String savePath = "E:\\" + sftpConfig.getBillPath() + "/" + month + "/";
				//要保存的对账文件
				String saveBillFile = savePath + saveFile;
				File fileDir = new File(savePath);
				//如果文件夹不存在则创建
				if (!fileDir.exists() && !fileDir.isDirectory()) {
					fileDir.mkdir();
				}
				FileOutputStream fos = new FileOutputStream(new File(saveBillFile));
				//下载文件地址
				String remoteBillPath = "/home/sinosoft/sftp_root/isv/226801000001181738033/20181015/226801000001181738033_20181017.txt";
				sftp.get(remoteBillPath, fos);
				logger.info("账单文件下载完成！");

				// 4. 调用txt2excel方法
				logger.info("正在转换excel文件...");
				TXT2ExcelUtil txt2ExcelUtil = new TXT2ExcelUtil();
				txt2ExcelUtil.conversionExcel(saveBillFile, "E:\\" + sftpConfig.getExcelPath());


			} catch (Exception e) {
				logger.error("账单下载失败！异常信息：" + e.getMessage(), e);
			}
			logger.info("账单下载成功！");
			return "账单下载成功!";
		}else{
			logger.info("连接sftp服务器异常！");
			return "连接sftp服务器异常!";
		}

	}

	@Override
	public GpRefundOrderVo refund(GpRefundOrderVo refundVo) {
		GgXmlLog xmlLog = new GgXmlLog();
		GpRefundOrderVo rs = new GpRefundOrderVo();
		try {
			String function = ApiCallUtil.FUNCTION_REFUND;
			xmlLog.setFunction(function);
			xmlLog.setReqTime(new Date());
			refundVo.setOutRefundNo(ApiCallUtil.generateOutTradeNo());

			ApiCallUtil prePay = new ApiCallUtil(function);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("OutTradeNo", refundVo.getOutTradeNo());
					put("MerchantId", refundVo.getMerchantId());
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("OutRefundNo", refundVo.getOutRefundNo());
					put("RefundAmount", String.valueOf(refundVo.getRefundAmount()));
					put("RefundReason",refundVo.getRefundReason());
					put("OperatorId", refundVo.getOperatorId());
					put("DeviceId", refundVo.getDeviceId());
					put("DeviceCreateIp", refundVo.getDeviceCreateIp());
				}
			});

			logger.info("开始调用{}接口, url={}", function, tradeConfig.getPayUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getPayUrl());

			// 插入报文日志表

			xmlLog.setRequestXml((String) resMap.get("requestXml"));
			xmlLog.setResponseXml((String) resMap.get("responseXml"));
			xmlLog.setRespTime(DateUtils.parseDate((String) resMap.get("RespTime"), DatePattern.PURE_DATETIME_PATTERN));

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpRefundOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();
			xmlLog.setResultStatus(respInfoVo.getResultStatus());
			xmlLog.setResultCode(respInfoVo.getResultCode());
			xmlLog.setResultMsg(respInfoVo.getResultMsg());

			refundVo.setRespInfo(respInfoVo);
			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("退款成功!退款外部交易号：{}，退款订单号：{}", rs.getOutRefundNo(), rs.getRefundOrderNo());
				refundVo.setRefundOrderNo(rs.getRefundOrderNo());
				refundVo.setRefundAmount(rs.getRefundAmount());
				refundVo.setTradeStatus(TRADE_STATUS_REFUNDING);
			} else {
				logger.info("退款失败!退款外部交易号：{}", rs.getOutRefundNo());
				refundVo.setValidInd("0");
			}

			GpRefundOrder gpRefundOrder = new GpRefundOrder();
			BeanUtils.copyProperties(refundVo, gpRefundOrder);
			gpRefundOrderMapper.insert(gpRefundOrder);

		} catch (Exception e) {
			xmlLog.setFlag("0");
			logger.error("退款发起异常！");
			throw new RuntimeException(e);
		} finally {
			ggXmlLogService.insert(xmlLog);
		}
		return rs;
	}

	@Override
	public GpTradeOrderVo payQuery(String merchantId, String outTradeNo) {

		GgXmlLog xmlLog = new GgXmlLog();
		GpTradeOrderVo rs = new GpTradeOrderVo();
		try {
			String function = ApiCallUtil.FUNCTION_PAY_QUERY;
			xmlLog.setFunction(function);
			xmlLog.setReqTime(new Date());

			ApiCallUtil prePay = new ApiCallUtil(function);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("MerchantId", merchantId);
					put("OutTradeNo", outTradeNo);
				}
			});

			logger.info("开始调用{}接口, url={}", function, tradeConfig.getReqUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getReqUrl());

			// 插入报文日志表
			xmlLog.setRequestXml((String) resMap.get("requestXml"));
			xmlLog.setResponseXml((String) resMap.get("responseXml"));
			xmlLog.setRespTime(DateUtils.parseDate((String) resMap.get("RespTime"), DatePattern.PURE_DATETIME_PATTERN));

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpTradeOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();
			xmlLog.setResultStatus(respInfoVo.getResultStatus());
			xmlLog.setResultCode(respInfoVo.getResultCode());
			xmlLog.setResultMsg(respInfoVo.getResultMsg());

			rs.setRespInfo(respInfoVo);
			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("订单查询成功!外部交易号：{}，订单号：{}", outTradeNo, rs.getOrderNo());
				GpTradeOrder tradeOrderVo = new GpTradeOrder().setOutTradeNo(outTradeNo).setMerchantId(merchantId);
				tradeOrderVo = gpTradeOrderMapper.selectOne(tradeOrderVo);
				if(tradeOrderVo != null){
					String tradeStatus = tradeOrderVo.getTradeStatus();
					String queryStatus = rs.getTradeStatus();
					if (!queryStatus.equals(tradeStatus)) {
						tradeStatus = queryStatus;
						GpTradeOrder update = new GpTradeOrder();
						update.setId(tradeOrderVo.getId());
						update.setTradeStatus(queryStatus);
						if (TRADE_STATUS_SUCC.equals(queryStatus)) {
							update.setGmtPayment(rs.getGmtPayment());
							update.setBankType(rs.getBankType());
							update.setIsSubscribe(rs.getIsSubscribe());
							update.setPayChannelOrderNo(rs.getPayChannelOrderNo());
							update.setMerchantOrderNo(rs.getMerchantOrderNo());
							update.setSubAppId(rs.getSubAppId());
							update.setCouponFee(rs.getCouponFee());
							update.setBuyerLogonId(rs.getBuyerLogonId());
							update.setBuyerUserId(rs.getBuyerUserId());
							update.setCredit(rs.getCredit());
							update.setReceiptAmount(rs.getReceiptAmount());
							update.setBuyerPayAmount(rs.getBuyerPayAmount());
							update.setInvoiceAmount(rs.getInvoiceAmount());
						}
						try {
							gpTradeOrderMapper.updateById(update);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				}
			} else {
				logger.info("订单查询失败!外部交易号：{}", outTradeNo);
			}

		} catch (Exception e) {
			xmlLog.setFlag("0");
			logger.error("订单查询异常！");
			throw new RuntimeException(e);
		} finally {
			ggXmlLogService.insert(xmlLog);
		}

		return rs;
	}

	@Override
	public GpRefundOrderVo refundQuery(String merchantId, String outRefundNo) {
		GgXmlLog xmlLog = new GgXmlLog();
		GpRefundOrderVo rs = new GpRefundOrderVo();
		try {
			String function = ApiCallUtil.FUNCTION_REFUND_QUERY;
			xmlLog.setFunction(function);
			xmlLog.setReqTime(new Date());

			ApiCallUtil prePay = new ApiCallUtil(function);
			prePay.setBody(new HashMap<String, String>() {
				{
					put("IsvOrgId", tradeConfig.getIsvOrgId());
					put("MerchantId", merchantId);
					put("OutRefundNo", outRefundNo);
				}
			});

			logger.info("开始调用{}接口, url={}", function, tradeConfig.getPayUrl());
			Map<String, Object> resMap = prePay.call(tradeConfig.getPayUrl());

			// 插入报文日志表

			xmlLog.setRequestXml((String) resMap.get("requestXml"));
			xmlLog.setResponseXml((String) resMap.get("responseXml"));
			xmlLog.setRespTime(DateUtils.parseDate((String) resMap.get("RespTime"), DatePattern.PURE_DATETIME_PATTERN));

			// 数据转换
			rs = MapUtil.map2Obj(resMap, GpRefundOrderVo.class);
			RespInfoVo respInfoVo = rs.getRespInfo();
			xmlLog.setResultStatus(respInfoVo.getResultStatus());
			xmlLog.setResultCode(respInfoVo.getResultCode());
			xmlLog.setResultMsg(respInfoVo.getResultMsg());

			rs.setRespInfo(respInfoVo);
			if (respInfoVo != null && RESULT_STATUS_SUCCESS.equals(respInfoVo.getResultStatus())) {
				logger.info("退款订单-查询成功!退款外部交易号：{}，退款订单号：{}，退款状态：{}", outRefundNo, rs.getRefundOrderNo(), rs.getTradeStatus());
				GpRefundOrder gpRefundOrder = new GpRefundOrder().setOutRefundNo(outRefundNo).setMerchantId(merchantId);
				gpRefundOrder = gpRefundOrderMapper.selectOne(gpRefundOrder);
				if(gpRefundOrder != null){
					String tradeStatus = gpRefundOrder.getTradeStatus();
					String queryTradeStatus = rs.getTradeStatus();
					if(!tradeStatus.equals(queryTradeStatus)){
						GpRefundOrder update = new GpRefundOrder();
						update.setId(gpRefundOrder.getId());
						update.setTradeStatus(queryTradeStatus);
						if(TRADE_STATUS_SUCC.equals(queryTradeStatus)){
							update.setGmtRefundment(rs.getGmtRefundment());
						}
						try {
							gpRefundOrderMapper.updateById(update);
						} catch (Exception e) {
							logger.error("退款订单-更新失败！退款外部交易号：{}", outRefundNo, e);
						}
					}
				}
			} else {
				logger.info("退款订单-查询失败!退款外部交易号：{}", outRefundNo);
			}

		} catch (Exception e) {
			xmlLog.setFlag("0");
			logger.error("退款订单查询异常！");
			throw new RuntimeException(e);
		} finally {
			ggXmlLogService.insert(xmlLog);
		}

		return rs;
	}


}
