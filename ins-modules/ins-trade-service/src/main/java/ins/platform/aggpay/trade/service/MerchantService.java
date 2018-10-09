/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ins.platform.aggpay.trade.service;


import ins.platform.aggpay.trade.entity.Merchant;
import ins.platform.aggpay.trade.vo.RegistResVo;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;

import com.baomidou.mybatisplus.service.IService;
import com.mybank.bkmerchant.merchant.Register;

/**
 * @author RipinYan
 * @ClassName: MerchantService
 * @Description: 商户入驻相关接口
 * @date 2018/9/18 下午4:11
 */
public interface MerchantService extends IService<Merchant> {

	RegistResVo regist(Register register);

	RegisterQueryVo registerQuery(String isvOrgId,String orderNo);

}
