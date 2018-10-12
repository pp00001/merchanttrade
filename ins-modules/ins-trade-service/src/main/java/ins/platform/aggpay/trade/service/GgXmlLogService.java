package ins.platform.aggpay.trade.service;

import ins.platform.aggpay.trade.entity.GgXmlLog;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 报文往来日志表 服务类
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
public interface GgXmlLogService extends IService<GgXmlLog> {


	@Override
	boolean insert(GgXmlLog ggXmlLog);
}
