package ins.platform.aggpay.trade.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.jcraft.jsch.ChannelSftp;
import ins.platform.aggpay.common.util.Query;
import ins.platform.aggpay.trade.config.SftpConfig;
import ins.platform.aggpay.trade.config.TradeConfig;
import ins.platform.aggpay.trade.entity.GgBill;
import ins.platform.aggpay.trade.mapper.GgBillMapper;
import ins.platform.aggpay.trade.service.GgBillService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import ins.platform.aggpay.trade.util.SFTP;
import ins.platform.aggpay.trade.util.Txt2ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 账单时间表 服务实现类
 * </p>
 *
 * @author zhangyu
 * @since 2018-10-23
 */
@Service
public class GgBillServiceImpl extends ServiceImpl<GgBillMapper, GgBill> implements GgBillService {
    private static final Logger logger = LoggerFactory.getLogger(GpTradeServiceImpl.class);
    @Autowired
    private SftpConfig sftpConfig;
    @Autowired
    private GgBillService ggBillService;
    @Autowired
    private GgBillMapper ggBillMapper;
    @Autowired
    private TradeConfig tradeConfig;

    /**
     * 根据商户号搜索
     * @param query
     * @param ggBill
     * @return
     */
    @Override
    public Page selectWithMerchanId(Query query, GgBill ggBill) {
        String merchantId = ggBill.getMerchantId();
        query.setRecords(findBillByMerchantId(merchantId));
        return query;
    }

    /**
     * 通过商户号id查询账单
     *
     * @param merchantId 商户号
     * @return 同商户账单s
     */
    @Cacheable(value = "merchant_id", key = "#merchantId")
    public List<GgBill> findBillByMerchantId(String merchantId) {
       return ggBillMapper.selectByMerchantId(merchantId);
    }


    /**
     * 原始账单下载
     * @param billDate  账单日期
     * @return
     */
    public boolean originalBillDown(String billDate) {
        if(billDate != "" || billDate.length() > 1){
            //处理前端日期
            billDate = billDate.replace("GMT", "").replaceAll("\\(.*\\)", "");
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
            Date dateTrans;
            String date;
            String isvOrgId = tradeConfig.getIsvOrgId();

            //连接sftp
            Map<String, Object> resultMap = new SFTP().connect(sftpConfig.getHost(), sftpConfig.getPort(), sftpConfig.getUsername(), sftpConfig.getPassword());

            if (resultMap != null || resultMap.size() > 1) {
                try {
                    //日期转换为yyyy-MM-dd
                    dateTrans = format.parse(billDate);
                    new SimpleDateFormat("yyyy-MM-dd").format(dateTrans).replace("-","/");
                    //日期转换为String类型
                    DateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
                    date = dfDate.format(dateTrans);
                    String month = date.substring(0, 6);
                    logger.info("日期为："+date);
                    //获取连接的sftp
                    ChannelSftp sftp = (ChannelSftp) resultMap.get("sftp");

                    String saveFile =  isvOrgId + "_" + date + ".txt";
                    String savePath = sftpConfig.getBillPath() + "/" + month + "/";

                    //要保存的对账文件
                    String localFile = savePath + saveFile;
                    File fileDir = new File(savePath);
                    //如果文件夹不存在则创建
                    if (!fileDir.exists() && !fileDir.isDirectory()) {
                        fileDir.mkdir();
                    }
                    FileOutputStream fos = new FileOutputStream(new File(localFile));
                    //下载文件地址
                    String ftpBillPath = sftpConfig.getFtpBillPath()+isvOrgId+ "/" + month + "/"+isvOrgId+"_"+billDate+".txt";
                    sftp.get(ftpBillPath, fos);

                    // 4. 调用txt2excel方法
                    logger.info("正在转换excel文件...");
                    Txt2ExcelUtil txt2ExcelUtil = new Txt2ExcelUtil();
                    txt2ExcelUtil.conversionExcel(localFile, sftpConfig.getExcelPath());

                } catch (Exception e) {
                    logger.error("账单下载失败！异常信息："+e.getMessage(),e);
                    return false;
                }
                logger.info("账单下载成功！");
                return true;
            } else {
                logger.info("连接sftp服务器异常！");
                return false;
            }
        }else{
            return false;
        }

    }

}
