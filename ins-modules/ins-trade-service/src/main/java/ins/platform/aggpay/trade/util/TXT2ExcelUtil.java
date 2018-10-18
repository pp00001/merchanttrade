package ins.platform.aggpay.trade.util;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将对账txt文件转换成商户excel文件
 * Created by ADD on 2018/10/11.
 */
public class TXT2ExcelUtil {

    private static final Logger logger = LoggerFactory.getLogger(TXT2ExcelUtil.class);

    public static void main(String args[]) throws Exception {
        TXT2ExcelUtil txt2ExcelUtil = new TXT2ExcelUtil();
        System.out.println("TXTConversionExcel.main");

    }
    //将txt文件转换为excel文件
    public void conversionExcel(String billPath,String excelPath){
        //获取当前时间(YYYYMMdd)
        DateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
        DateFormat dfMonth = new SimpleDateFormat("yyyyMM");
        Calendar calendar = Calendar.getInstance();
        String date = dfDate.format(calendar.getTime());
        String month = dfMonth.format(calendar.getTime());
        calendar.add(Calendar.DATE,-1);
        String lastDate = dfDate.format(calendar.getTime());

        File file = new File(billPath);//要读取的txt文件
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
                File excelFile;
                if (null != merchantMap && merchantMap.size() > 0) {
                    for (String merchantNo : merchantMap.keySet()) {
                        //excel文件名
                        excelName = merchantNo + "_" + date;
                        //excel文件地址
                        String excelFilePath = excelPath+"/"+merchantNo+"/"+month;

                        //判断是否存在路径，不存在则创建
                        excelFile = new File(excelFilePath);
                        if (!excelFile.exists()){
                            excelFile.mkdirs();
                            System.out.println(excelFile);
                        }
                        file2 = new File(excelFilePath+"/" + excelName + ".xls");// 将生成的excel表格

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
                logger.error("账单转换异常！异常信息：" + e.getMessage(), e);
            } finally {
                try{
                    input.close();
                    read.close();
                }catch (Exception e){
                    logger.error("关闭异常！异常信息："+e.getMessage() , e);
                }
            }
            logger.info("excel转换完成！");
        } else {
            logger.info("对账文件读取异常！");
            System.exit(0);
        }
    }
}
