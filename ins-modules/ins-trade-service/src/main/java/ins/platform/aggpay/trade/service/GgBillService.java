package ins.platform.aggpay.trade.service;

import com.baomidou.mybatisplus.plugins.Page;
import ins.platform.aggpay.common.util.Query;
import ins.platform.aggpay.trade.entity.GgBill;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 账单时间表 服务类
 * </p>
 *
 * @author zhangyu
 * @since 2018-10-23
 */
public interface GgBillService extends IService<GgBill> {

    /**
     * 根据商户号查询
     * @param Query
     * @param ggBill
     * @return
     */
    Page selectWithMerchanId(Query Query, GgBill ggBill);


}
