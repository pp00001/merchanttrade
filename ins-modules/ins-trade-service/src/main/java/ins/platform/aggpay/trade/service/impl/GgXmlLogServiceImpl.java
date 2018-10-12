package ins.platform.aggpay.trade.service.impl;

import ins.platform.aggpay.trade.entity.GgXmlLog;
import ins.platform.aggpay.trade.mapper.GgXmlLogMapper;
import ins.platform.aggpay.trade.service.GgXmlLogService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 * 报文往来日志表 服务实现类
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
@Service
public class GgXmlLogServiceImpl extends ServiceImpl<GgXmlLogMapper, GgXmlLog> implements GgXmlLogService {

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean insert(GgXmlLog entity) {
		return super.insert(entity);
	}
}
