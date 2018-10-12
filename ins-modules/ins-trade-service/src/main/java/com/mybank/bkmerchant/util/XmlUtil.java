/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.mybank.bkmerchant.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.text.WordUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dom.DOMElement;
import org.slf4j.LoggerFactory;

/**
 * @author simon.xxm
 * @version $Id: XmlUtil.java, v 0.1 2016年1月25日 下午1:54:52 simon.xxm Exp $
 */
public class XmlUtil {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	public static void main(String[] args) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("key", "value");
		map.put("key1", "value1");
		String xmlText = XmlUtil.createXml("data", map, "GBK");
		System.out.println(xmlText);
		//      System.out.println(XmlUtil.readXml(xmlText).get("key"));
		//      try {
		//          File file = new File("D:/warn.txt");
		//          InputStream in = new FileInputStream(file);
		//          byte[] b = new byte[in.available()];
		//          in.read(b);
		//          String text = new String(b,"GBK");
		//          Map<String, String> map = XmlUtil.readXml(text);
		//          System.out.println(map.get("hmac"));
		//          System.out.println(map.get("order_Id"));
		//          in.close();
		//      } catch (Exception e) {
		//          // TODO Auto-generated catch block
		//          e.printStackTrace();
		//      }
	}

	/**
	 * 描  叙: 生成xml报文
	 * <p>
	 * 作  者: 徐新明<br>
	 * 时  间: 2015年4月17日 下午6:39:18
	 */
	public static String createXml(String element, Map<String, String> map, String encoding) {
		Document document = DocumentHelper.createDocument();
		if (encoding != null) {
			document.setXMLEncoding(encoding);
		}
		Element newspaperElement = document.addElement(element);

		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			newspaperElement.addElement(key).setText(map.get(key));
		}

		return document.asXML();
	}

	/**
	 * 描  叙: 解析xml报文
	 * <p>
	 * 作  者: 徐新明<br>
	 * 时  间: 2015年4月17日 下午6:39:00
	 */
	public static Map<String, String> readXml(String xmlText) {
		Map<String, String> map = new HashMap<String, String>();
		Document document;
		try {
			document = DocumentHelper.parseText(xmlText);
			Element root = document.getRootElement();
			List<Element> list = root.elements();
			for (Element e : list) {
				parseXmlElement(map, e);
			}
		} catch (DocumentException e) {
			logger.error("解析xml报文出错" + xmlText, e);
		}
		return map;
	}

	public static void parseXmlElement(Map<String, String> map, Element e) {
		if (e.elementIterator().hasNext()) {
			Iterator<Element> it = e.elementIterator();
			while (it.hasNext()) {
				e = it.next();
				parseXmlElement(map, e);
			}
		} else {
			map.put(e.getName(), e.getText());
		}
	}

	//读取报文格式
	public String sendStyle(String function) {
		InputStream is = getClass().getResourceAsStream("../../../../xml/" + function + ".xml");
		return getStyle(is);
	}

	//读取报文格式
	public String receiveStyle(String function) {
		InputStream is = getClass().getResourceAsStream("../../../../xml/" + function + ".xml");
		return getStyle(is);
	}

	public String getStyle(InputStream is) {
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(is));
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	//封装报文
	public String format(Map<String, String> form, String function) throws DocumentException {
		//报文头
		Element headElement = formatHead(form);
		//报文体
		Element bodyElement = formatBody(form, function);

		Document docRes = DocumentHelper.createDocument();
		Element rootElement = docRes.addElement("document");
		Element requestElement = rootElement.addElement("request").addAttribute("id", "request");
		requestElement.add(headElement);
		requestElement.add(bodyElement);

		String out = docRes.asXML();
		return out;
	}

	private void sign(Document doc) {
		String xml = doc.getRootElement().element("response").asXML();
		//return RSA.sign(xml, privateKey);
	}

	//封装报文头
	public Element formatHead(Map<String, String> form) throws DocumentException {
		Document docStyle = DocumentHelper.parseText(sendStyle("head"));
		Element headStyle = docStyle.getRootElement();
		List<Element> list = headStyle.elements();
		Element headElement = new DOMElement("head");
		for (int i = 0; i < list.size(); i++) {
			Element e = list.get(i);
			String tagName = e.attributeValue("tagName");
			String value = form.get(tagName);
			if (value == null) {
				String defaultValue = e.attributeValue("defaultValue");
				if (defaultValue != null) {
					value = defaultValue;
				}
			}
			if (value != null) {
				headElement.addElement(tagName).setText(value);
			}
		}
		return headElement;
	}

	//封装报文体
	public Element formatBody(Map<String, String> form, String function) throws DocumentException {
		Document docStyle = DocumentHelper.parseText(sendStyle(function));
		Element bodyStyle = docStyle.getRootElement();
		List<Element> list = bodyStyle.elements();
		Element bodyElement = new DOMElement("body");
		for (int i = 0; i < list.size(); i++) {
			Element e = list.get(i);
			String tagName = e.attributeValue("tagName");
			String value = form.get(tagName);
			if (value == null) {
				String defaultValue = e.attributeValue("defaultValue");
				if (defaultValue != null) {
					value = defaultValue;
				}
			}
			if (value != null) {
				bodyElement.addElement(tagName).setText(value);
			}
		}
		return bodyElement;
	}

	//封装签名数据
	public Element formatSign(Map<String, String> form) {
		Namespace dsNamespace = new Namespace("ds", "http://www.w3.org/2000/09/xmldsig#");
		Element signature = new DOMElement(new QName("Signature", dsNamespace));
		Element signatureValue = signature.addElement(new QName("SignatureValue", dsNamespace));
		signatureValue.setText("签名数据");
		return signature;
	}

	//解析报文
	public Map<String, Object> parse(String response, String function) throws DocumentException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Document docStyle = DocumentHelper.parseText(response);
		List<Element> headList = docStyle.getRootElement().element("response").element("head").elements();
		Map<String, Object> headMap = parseHead(headList);

		List<Element> bodyList = docStyle.getRootElement().element("response").element("body").elements();
		Map<String, Object> bodyMap = parseBody(bodyList);


		returnMap.putAll(headMap);
		returnMap.putAll(bodyMap);
		return returnMap;
	}

	//解析报文
	public Map<String, Object> parseReceive(String request, String function) throws DocumentException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Document docStyle = DocumentHelper.parseText(request);
		List<Element> headList = docStyle.getRootElement().element("request").element("head").elements();
		Map<String, Object> headMap = parseHead(headList);

		List<Element> bodyList = docStyle.getRootElement().element("request").element("body").elements();
		Map<String, Object> bodyMap = parseBody(bodyList);


		returnMap.putAll(headMap);
		returnMap.putAll(bodyMap);
		return returnMap;
	}

	//解析报文头
	private Map<String, Object> parseHead(List<Element> list) throws DocumentException {
		Map<String, Object> headMap = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			Element e = list.get(i);
			if (e.getText() != null && !"".equals(e.getText())) headMap.put(e.getName(), e.getText());
		}
		return headMap;
	}

	//解析报文体
	private Map<String, Object> parseBody(List<Element> list) throws DocumentException {
		Map<String, Object> bodyMap = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			Element e = list.get(i);
			//包含子元素
			if (e.elements().size() != 0) {
				//重复标签合并成list
				if (bodyMap.containsKey(e.getName())) {
					Object o = bodyMap.get(e.getName());
					if (o instanceof ArrayList) {
						ArrayList<Object> oList = (ArrayList<Object>) o;
						oList.add(parseBody(e.elements()));
					} else {
						ArrayList<Object> oList = new ArrayList<Object>();
						oList.add(o);
						oList.add(parseBody(e.elements()));
						bodyMap.put(WordUtils.uncapitalize(e.getName()), oList);
					}
				} else {
					bodyMap.put(WordUtils.uncapitalize(e.getName()), parseBody(e.elements()));
				}
				//无子元素且非空
			} else if (e.getText() != null && !"".equals(e.getText())) {
				//重复标签合并成list
				if (bodyMap.containsKey(e.getName())) {
					Object o = bodyMap.get(e.getName());
					if (o instanceof ArrayList) {
						ArrayList<Object> oList = (ArrayList<Object>) o;
						oList.add(e.getText());
					} else {
						ArrayList<Object> oList = new ArrayList<Object>();
						oList.add(o);
						oList.add(e.getText());
						bodyMap.put(WordUtils.uncapitalize(e.getName()), oList);
					}
				} else {
					bodyMap.put(WordUtils.uncapitalize(e.getName()), e.getText());
				}
			}
		}
		return bodyMap;
	}

	public void addDs() throws IOException, DocumentException {
		InputStream is = XmlUtil.class.getResourceAsStream("originMessage.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		String s = "";
		while ((s = br.readLine()) != null) {
			sb.append(s);
		}
		s = sb.toString();
		Document document = DocumentHelper.parseText(s);
		String out = document.asXML();
		System.out.println(out);

		Element root = document.getRootElement();

		Namespace dsNamespace = new Namespace("ds", "http://www.w3.org/2000/09/xmldsig#");
		Element signature = root.addElement(new QName("Signature", dsNamespace));
		Element signedInfo = signature.addElement(new QName("SignedInfo", dsNamespace));
		signedInfo.addElement(new QName("CanonicalizationMethod", dsNamespace)).addAttribute("Algorithm", "http://www" + ".w3.org/TR/2001/REC-xml-c14n-20010315");
		signedInfo.addElement(new QName("SignatureMethod", dsNamespace)).addAttribute("Algorithm", "http://www" +
                ".w3.org/2001/04/xmldsig-more#rsa-sha256");
		Element reference = signedInfo.addElement(new QName("Reference", dsNamespace));
		reference.addAttribute("URI", "");
		reference.addElement(new QName("Transforms", dsNamespace)).addElement(new QName("Transform", dsNamespace)).addAttribute("Algorithm",
                "http://www.w3.org/2000/09/xmldsig#enveloped-signature");
		reference.addElement(new QName("DigestMethod", dsNamespace)).addAttribute("Algorithm", "http://www.w3.org/2000/09/xmldsig#sha1");
		reference.addElement(new QName("DigestValue", dsNamespace)).setText("MD5值");

		Element signatureValue = signature.addElement(new QName("SignatureValue", dsNamespace));
		signatureValue.setText("签名数据");

		out = document.asXML();
		System.out.println(out);
	}

}
