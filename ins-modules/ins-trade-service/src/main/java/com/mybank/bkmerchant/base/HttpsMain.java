package com.mybank.bkmerchant.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsMain {
    /**
     * 是否dev环境
     */
    public static final boolean isDev        = false;
    /**
     * 是否开启验签
     */
    public static final boolean isSign       = true;

    /**
     * 身份证
     */
    public static String        certNo       = "512734195005252263";

    /**
     * 商户ID
     * 1. 自然人-226801000000141472259
     * 2. 企业商户-226801000000142576686
     */
    public static String        merchantId   = "226801000000142576686";

    /**
     * 渠道
     */
    public static final String  channel      = "MYBANK";

    /**
     * 短信验证码
     */
    public static final String  smsCode      = "888888";

    /**
     * 币种
     */
    public static final String  currencyCode = "156";

    public static String        reqUrl       = "https://fcsupergw.dl.alipaydev.com/open/api/common/request2.htm";

    public static String        payUrl       = "https://fcsupergw.dl.alipaydev.com/open/api/common/requestasync.htm";

    public static String        uploadUrl    = "https://fcsupergwlite.dl.alipaydev.com/ant/mybank/merchantprod/merchant/uploadphoto.htm";

    public static String        oauthUrl    = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2018073160766860&scope=auth_base&redirect_uri=https://baidu.com";

    public static String        openApiUrl    = "https://openapi.alipay.com/gateway.do";
    /**
     * partner
     */
    public static String        IsvOrgId     = "202210000000000001316";

    /**
     * 联调阶段使用1.0.0的版本，上线前需要根据情况重新配置
     */
    public static String        version      = "1.0.0";

    public static String        appId        = "2018090700000286";


    /**
     * 
     */
    public static void main(String[] args) throws Exception {

    }

    public static String httpsReq(String reqUrl, String param) throws NoSuchAlgorithmException,
                                                              NoSuchProviderException, IOException,
                                                              KeyManagementException {
        URL url = new URL(reqUrl);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String paramString, SSLSession paramSSLSession) {
                return true;
            }
        });

        //创建SSLContext对象，并使用我们指定的信任管理器初始化
        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                           String paramString) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());

        //从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        //创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
        httpsConn.setSSLSocketFactory(ssf);

        httpsConn.setDoOutput(true);
        httpsConn.setRequestMethod("POST");
        httpsConn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
        httpsConn.setRequestProperty("Content-Length", String.valueOf(param.length()));

        OutputStreamWriter out = new OutputStreamWriter(httpsConn.getOutputStream(), "UTF-8");
        out.write(param);
        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
            httpsConn.getInputStream(), "UTF-8"));
        String tempLine = "";
        StringBuffer resultBuffer = new StringBuffer();
        while ((tempLine = reader.readLine()) != null) {
            resultBuffer.append(tempLine).append(System.getProperty("line.separator"));
        }
        return resultBuffer.toString();
    }

    public static String httpReq(String param) throws NoSuchAlgorithmException,
                                              NoSuchProviderException, IOException,
                                              KeyManagementException {
        URL url = new URL("http://fcsupergw.d1003.mayibank.net/open/api/common/request.htm");
        HttpURLConnection httpsConn = (HttpURLConnection) url.openConnection();

        httpsConn.setDoOutput(true);
        httpsConn.setRequestMethod("POST");
        httpsConn.setRequestProperty("Content-Type", "application/xml;charset=UTF-8");
        httpsConn.setRequestProperty("Content-Length", String.valueOf(param.length()));

        OutputStreamWriter out = new OutputStreamWriter(httpsConn.getOutputStream(), "UTF-8");
        out.write(param);
        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
            httpsConn.getInputStream(), "UTF-8"));
        String tempLine = "";
        StringBuffer resultBuffer = new StringBuffer();
        while ((tempLine = reader.readLine()) != null) {
            resultBuffer.append(tempLine).append(System.getProperty("line.separator"));
        }
        return resultBuffer.toString();
    }

}
