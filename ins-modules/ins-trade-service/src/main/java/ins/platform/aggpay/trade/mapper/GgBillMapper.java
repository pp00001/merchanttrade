package ins.platform.aggpay.trade.mapper;

import ins.platform.aggpay.common.util.Query;
import ins.platform.aggpay.trade.entity.GgBill;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 账单时间表 Mapper 接口
 * </p>
 *
 * @author zhangyu
 * @since 2018-10-23
 */
public interface GgBillMapper extends BaseMapper<GgBill> {

    /**
     * 根据商户号id
     * @param merchantId
     * @return
     */
    List<GgBill> selectByMerchantId(@Param("merchantId") String merchantId);
}
