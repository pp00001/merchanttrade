package ins.platform.aggpay.trade.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import ins.platform.aggpay.common.constant.CommonConstant;
import ins.platform.aggpay.common.util.Query;
import ins.platform.aggpay.common.util.R;
import ins.platform.aggpay.trade.config.SftpConfig;
import ins.platform.aggpay.trade.service.impl.GgBillServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ins.platform.aggpay.trade.entity.GgBill;
import ins.platform.aggpay.trade.service.GgBillService;
import ins.platform.aggpay.common.web.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * <p>
 * 账单时间表 前端控制器
 * </p>
 *
 * @author zhangyu
 * @since 2018-10-23
 */
@RestController
@RequestMapping("/bill")
public class GgBillController extends BaseController {
    @Autowired
    private GgBillService ggBillService;
    @Autowired
    private SftpConfig sftpConfig;
    @Autowired
    private GgBillServiceImpl ggBillServiceImpl;

    /**
     * 分页查询信息和根据商户号搜索
     * @param params 分页对象
     * @return 分页对象
     */
    @RequestMapping("/billPage")
    public Page page(@RequestParam Map<String, Object> params, GgBill ggBill) {
        if (ggBill.getMerchantId() == null || ggBill.getMerchantId() == "" || ggBill == null) {
            params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
            return ggBillService.selectPage(new Query<>(params), new EntityWrapper<>());
        } else {
            return ggBillService.selectWithMerchanId(new Query(params), ggBill);
        }
    }


    /**
     * 商户账单下载
     *
     * @param merchantId 商户号
     * @param billDate   账单日期
     * @return 下载成功/失败
     */
    @GetMapping(value = "/down/{merchantId}/{billDate}")
    public String downLoadBill(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String merchantId,
                               @PathVariable String billDate) {

        //处理yyyy-MM-dd格式时间
        billDate = billDate.replace("-", "");
        String month = billDate.substring(0, 6);
        //文件名
        String fileName = merchantId + "_" + billDate;
        //测试使用的excel文件路径
        String path = "E:/sftp_root/merchant/" + merchantId + "/" + month + "/" + merchantId + "_" + billDate + ".xls";
//        String path = sftpConfig.getExcelPath()+merchantId+"/"+month+"/"+fileName + ".xls";
        //判断文件是否存在
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            try {
                // 以流的形式下载文件。
                InputStream fis = new BufferedInputStream(new FileInputStream(path));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/vdn.ms-excel");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();


            } catch (Exception e) {
                logger.error("bill download fail！" + e.getMessage(), e);
            }
            logger.info("账单文件下载成功！");
            return null;
        } else {
            logger.info("file is exists！");
        }
        return null;
    }

    /**
     * 原始账单下载
     *
     * @param billDate 账单日期
     * @return
     */
    @RequestMapping(value = "/downLoadBill/{billDate}")
    @ResponseBody
    public R<Boolean> downLoadBill(@PathVariable String billDate) {
        return new R<>(ggBillServiceImpl.originalBillDown(billDate));
    }

}
