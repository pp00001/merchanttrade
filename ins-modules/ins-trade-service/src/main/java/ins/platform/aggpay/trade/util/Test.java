package ins.platform.aggpay.trade.util;


import ins.platform.aggpay.trade.config.SftpConfig;


/**
 * Created by ADD on 2018/10/15.
 */

public class Test {

    public static void main(String args[]) throws Exception {
//        System.out.println("host: "+isvConfigSFTPConfig.getHost());
//        System.out.println("port: "+isvConfigSFTPConfig.getPort());
        SftpConfig sftpConfig = new SftpConfig();
        sftpConfig.setBillPath("/home");
        System.out.println("billPath: "+ sftpConfig.getBillPath());

    }


}
