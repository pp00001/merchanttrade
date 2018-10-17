package ins.platform.aggpay.trade.util;

import java.io.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import ins.platform.aggpay.trade.config.IsvConfig;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 将对账txt文件转换成商户excel文件
 * Created by ADD on 2018/10/11.
 */
public class TXTConversionExcel {
    public static void main(String args[]) throws Exception {
        TXTConversionExcel txtConversionExcel = new TXTConversionExcel();
        txtConversionExcel.conversionExcel();
        System.out.println("TXTConversionExcel.main");

//        txtConversionExcel.readProperties();
//        String txt = MessageFormat.format(txtPath, "1000000", "20181016");
//        String excel = MessageFormat.format(excelPath, "1000000", "20181016");
//        System.out.println(txt);
//        System.out.println(excel);
//
//        ChannelSftp login = txtConversionExcel.login(host,port,username,password);
//        System.out.println(login);
//        login.getSession().disconnect();
//        login.quit();
    }

    private static String txtPath;
    private static String excelPath;
    private static String host;
    private static int port;
    private static String username;
    private static String password;
    private static ChannelSftp sftp = null;

    //读取properties配置文件
    public void readProperties(){
        Properties prop=new Properties();
        InputStream in = TXTConversionExcel.class.getClassLoader().getResourceAsStream("ConversionExcel.properties");
        try{
            prop.load(in);//加载文件
            txtPath = prop.getProperty("txtPath");
            excelPath = prop.getProperty("excelPath");
            host = prop.getProperty("host");
            port = Integer.parseInt(prop.getProperty("port"));
            username = prop.getProperty("username");
            password = prop.getProperty("password");

            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //将txt文件转换为excel文件
    public void conversionExcel(){
        readProperties(); //读取配置文件
//        try {
//            login(host,port,username,password); //连接sftp服务器
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //获取当前时间(YYYYMMdd)
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        String dateName = df.format(calendar.getTime());
        int dateNum = Integer.parseInt(dateName);

        IsvConfig isvConfig = new IsvConfig();
        //获取合作方机构号
        String isvOrgId = isvConfig.getIsvOrgId();

        //txt文件地址
        String txt = MessageFormat.format(txtPath,isvOrgId, dateNum - 1 + "");
        File  file = new File("E:\\"+txt+"交易对账文件.txt");//要读取的txt文件
        System.out.println(file);
        Map<String, List> map = new LinkedHashMap<>();
        Map<String, String> merchantMap = new LinkedHashMap<>();

        List<String> lineList = null;

        List<String> list = new ArrayList<>();

        if (file.exists() && file.isFile()) {

            InputStreamReader read = null;
            String line = "";
            BufferedReader input = null;
            WritableWorkbook wbook = null;
            WritableSheet sheet = null;

            try {
                read = new InputStreamReader(new FileInputStream(file), "utf-8");//读取txt文件
                input = new BufferedReader(read);
                Label t;

                while ((line = input.readLine()) != null) {
                    //save all content
                    list.add(line);
                    String[] sentence = line.split(",");   //分割line
                    //匹配商户号
                    if (!"".equals(sentence[0]) && sentence[0].matches("^[1-9]\\d*$")) {
                        if (map != null && map.containsKey(sentence[0])) {
                            lineList = map.get(sentence[0]);
                            lineList.add(line);
                            map.put(sentence[0], lineList);
                        } else {
                            lineList = new ArrayList<>();
                            lineList.add(line);
                            map.put(sentence[0], lineList);
                        }
                        merchantMap.put(sentence[0], sentence[0]);
                    }
                }

                String excelName;
                File file2;
                File excelFilePath;
                if (null != merchantMap && merchantMap.size() > 0) {
                    for (String merchantNo : merchantMap.keySet()) {
                        excelName = merchantNo + "_" + dateName;
                        //excel文件地址
                        String excel = MessageFormat.format(excelPath,merchantNo,dateName);
                        //判断是否存在路径，不存在则创建
                        excelFilePath = new File("E:\\"+excel);
                        if (!excelFilePath.exists()){
                            excelFilePath.mkdirs();
                            System.out.println(excelFilePath);
                        }
                        file2 = new File("E:\\"+excel + excelName + ".xls");// 将生成的excel表格

                        wbook = Workbook.createWorkbook(file2); // 根据路径生成excel文件
                        sheet = wbook.createSheet("first", 0);  // 新标签页
                        int n = 0;// excel列数
                        int m = 0;//excel行数

                        for (int index = 0; index < list.size(); index++){
                            String[] sentence = list.get(index).split(",");   //分割line
                            if (!sentence[0].matches("^[1-9]\\d*$") || sentence[0].equals(merchantNo)) {
                                for (int i = 0; i < sentence.length; i++) {
                                    if(i ==14){
                                        continue;
                                    }else{
                                        t = new Label(n,m, sentence[i]);  //参数为：列，行，数据
                                        sheet.addCell(t);
                                        n++;
                                    }
                                }
                                n=0;
                                m++;
                            }
                        }
                        wbook.write();
                        wbook.close();

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try{
                    //关闭sftp服务器
//                    sftp.getSession().disconnect();
//                    sftp.quit();
                    input.close();
                    read.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println("over!");
            System.exit(0);
        } else {
            System.out.println("file is not exists or not a file");
            System.exit(0);
        }
    }


    /**
     * 登陆SFTP服务器
     * @param host  IP地址
     * @param port  端口
     * @param username  登陆用户名
     * @param password  登陆密码
     * @return ChannelSftp
     * @throws IOException
     */
    public ChannelSftp login(String host,int port,String username,String password) throws IOException{
        try {
            JSch jsch = new JSch();

            //获取sshSession  账号-ip-端口
            Session sshSession =jsch.getSession(username, host,port);
            //添加密码
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            //严格主机密钥检查
            sshConfig.put("StrictHostKeyChecking", "no");

            sshSession.setConfig(sshConfig);
            //开启sshSession链接
            sshSession.connect();
            //获取sftp通道
            Channel channel = sshSession.openChannel("sftp");
            //开启
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sftp;
    }
}
