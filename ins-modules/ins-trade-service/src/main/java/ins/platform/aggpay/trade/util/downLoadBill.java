package ins.platform.aggpay.trade.util;

import com.jcraft.jsch.ChannelSftp;
import ins.platform.aggpay.trade.config.SftpConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by ADD on 2018/10/17.
 */
public class downLoadBill {
    @Autowired
    private static SftpConfig sftpConfig;

    public String downLoad(String billPath,String isvOrgId,String billDate){

        DateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
        DateFormat dfMonth = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        String date = dfDate.format(calendar.getTime());
        String month = dfMonth.format(calendar.getTime());
        calendar.add(Calendar.DATE,-1);
        String lastDate = dfDate.format(calendar.getTime());

//		SFTP sftp = new SFTP();
        try {
            Map<String,Object> resultMap = new SFTP().connect(sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getUsername(), sftpConfig.getPassword());
            // resultMap-code
            if(resultMap == null || resultMap.size() == 0){
                return "FAIL!";
            }
            ChannelSftp sftp = (ChannelSftp)resultMap.get("sftp");//获取连接的sftp

            String saveFile = isvOrgId +"_"+billDate + ".txt";
            String savePath = sftpConfig.getBillPath()+"/"+month+"/" ;
            //要保存的对账文件
            String saveBillFile = savePath+saveFile;
            File fileDir = new File(savePath);
            //如果文件夹不存在则创建
            if (!fileDir.exists() && !fileDir.isDirectory()) {
                fileDir.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(new File(saveBillFile));
            //下载文件地址
            String remoteBillPath = "/home/sinosoft/soft_root/isv/226801000001181738033/20181015/交易对账文件.txt";
            sftp.cd(remoteBillPath);
            sftp.get(saveBillFile, fos);

            // 4. 调用txt2excel方法
        }catch (Exception e){
            e.printStackTrace();
        }

        return "over!";
    }
}
