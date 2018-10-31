package com.mybank.bkmerchant.merchant;

import ins.platform.aggpay.trade.entity.GgXmlLog;
import ins.platform.aggpay.trade.service.impl.GgXmlLogServiceImpl;
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.util.BeanManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mybank.bkmerchant.base.HttpsMain;
import com.mybank.bkmerchant.util.HttpsUtil;
import com.mybank.bkmerchant.util.RSA;
import com.mybank.bkmerchant.util.XmlSignUtil;
import com.mybank.bkmerchant.util.XmlUtil;
import com.xiaoleilu.hutool.date.DatePattern;

/**
 * Created by jingzhu.zr on 2017/7/27.
 */
public class UploadPhoto {

	private static final Logger logger = LoggerFactory.getLogger(UploadPhoto.class);

	private static final String CHARSET = "UTF-8";
	private static final String SIGN_ALGORITHM = "SHA256withRSA";
	private String function = ApiCallUtil.FUNCTION_UPLOAD_PHOTO;
	private String isvOrgId;
	private String appId;
	private String version;
	/**
	 * 图片类型。可选值：
	 * 01 身份证正面
	 * 02 身份证反面
	 * 03 营业执照
	 * 04 组织机构代码证
	 * 05 开户许可证
	 * 06 门头照
	 * 07 其他
	 */
	private String photoType;
	private String outTradeNo;
	/**
	 * 图片地址
	 */
	private String picture;
	private String reqTime;
	private String signature;

	public UploadPhoto(String isvOrgId, String appId, String version, String privateKey, String photoType, String outTradeNo, String picture) {
		this.isvOrgId = isvOrgId;
		this.appId = appId;
		this.version = version;
		this.photoType = photoType;
		this.outTradeNo = outTradeNo;
		this.picture = picture;
		this.reqTime = new DateTime(System.currentTimeMillis()).toString("yyyyMMddHHmmss");
		try {
			this.signature = sign(privateKey);
		} catch (Exception e) {
			logger.error("签名生成失败", e);
		}

	}

	public String sign(String privateKey) throws Exception {
		Map<String, String> params = new TreeMap<String, String>();
		params.put("AppId", appId);
		params.put("Function", function);
		params.put("IsvOrgId", isvOrgId);
		params.put("OutTradeNo", outTradeNo);
		params.put("PhotoType", photoType);
		params.put("ReqTime", reqTime);
		params.put("Version", version);

		StringBuilder sb = new StringBuilder();
		PrivateKey key = RSA.getPrivateKey(privateKey);

		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		String source = URLEncoder.encode(sb.toString().substring(0, sb.toString().length() - 1), CHARSET);
		logger.info("参与加密的明文：{}", source);
		final Signature signatureChecker = Signature.getInstance(SIGN_ALGORITHM);
		signatureChecker.initSign(key);
		signatureChecker.update(source.getBytes(CHARSET));
		String signature = Base64.encodeBase64String(signatureChecker.sign());
		logger.info("生成的最终签名：{}", signature);
		return signature;
	}

	private HttpEntity buildReqEntity() throws UnsupportedEncodingException, FileNotFoundException {
		HttpEntity reqEntity = MultipartEntityBuilder.create()
				.addPart("IsvOrgId", new StringBody(isvOrgId, ContentType.create("text/plain", Consts.ASCII)))
				.addPart("PhotoType", new StringBody(photoType, ContentType.create("text/plain", Consts.ASCII)))
				.addPart("OutTradeNo", new StringBody(outTradeNo, ContentType.create("text/plain", Consts.ASCII)))
				.addBinaryBody("Picture", new File(picture), ContentType.DEFAULT_BINARY, picture)
				.addPart("Function", new StringBody(function, ContentType.create("text/plain", Consts.ASCII)))
				.addPart("Version", new StringBody(version, ContentType.create("text/plain", Consts.ASCII)))
				.addPart("AppId", new StringBody(appId, ContentType.create("text/plain", Consts.ASCII)))
				.addPart("ReqTime", new StringBody(reqTime, ContentType.create("text/plain", Consts.ASCII)))
				.addPart("Signature", new StringBody(signature, ContentType.create("text/plain", Consts.ASCII)))
				.build();

		return reqEntity;
	}

	public Map<String, Object> call() throws Exception {
		HttpEntity reqEntity = buildReqEntity();
		//发送请求
		String response = HttpsUtil.httpPost(HttpsMain.uploadUrl, reqEntity);

		System.out.println("-------------------------");
		System.out.println("---------RESPONSE--------");
		System.out.println("-------------------------");
		System.out.println(response);
		if (HttpsMain.isSign) {//生产环境需进行rsa验签
			if (!XmlSignUtil.verify(response)) {
				logger.error("延签失败！");
				throw new RuntimeException("验签失败");
			}
		}
		//解析报文
		XmlUtil xmlUtil = new XmlUtil();
		GgXmlLog xmlLog = new GgXmlLog();
		Map<String, Object> resMap = xmlUtil.parse(response, function);
		xmlLog.setFunction(function);
		xmlLog.setReqTime(new Date());
		xmlLog.setRequestXml((String) resMap.get("requestXml"));
		xmlLog.setResponseXml(response.replaceAll("\n", ""));
		xmlLog.setRespTime(DateUtils.parseDate((String) resMap.get("RespTime"), DatePattern.PURE_DATETIME_PATTERN));
		Object obj = resMap.get("respInfo");
		if (obj != null) {
			Map<String, String> respInfo = (Map<String, String>) obj;
			xmlLog.setResultStatus(respInfo.get("resultStatus"));
			xmlLog.setResultCode(respInfo.get("resultCode"));
			xmlLog.setResultMsg(respInfo.get("resultMsg"));
		}
		GgXmlLogServiceImpl ggXmlLogService = (GgXmlLogServiceImpl) BeanManager.getBean("ggXmlLogServiceImpl");
		ggXmlLogService.insert(xmlLog);
		return resMap;
	}

	public static void main(String[] args) throws Exception {
		//		UploadPhoto uploadPhoto = new UploadPhoto(
		//				/**
		//				 * 图片类型。可选值：
		//				 01 身份证正面
		//				 02 身份证反面
		//				 03 营业执照
		//				 04 组织机构代码证
		//				 05 开户许可证
		//				 06 门头照
		//				 07 其他
		//				 */
		//				PhotoType.PRG_PHOTO,
		//				/**
		//				 * 外部交易号
		//				 */
		//				UUID.randomUUID().toString(),
		//				/**
		//				 * 本地图片地址
		//				 */
		//				"/Users/yanshuiping/Downloads/a.png");
		//
		//		Map<String, Object> call = uploadPhoto.call();
		//		System.out.println("#####" + call.toString());
	}
}
