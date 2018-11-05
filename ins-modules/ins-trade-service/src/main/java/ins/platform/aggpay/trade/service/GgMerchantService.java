/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ins.platform.aggpay.trade.service;


import ins.platform.aggpay.trade.entity.GgMerchant;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;
import ins.platform.aggpay.trade.vo.SmsVo;
import ins.platform.aggpay.trade.vo.UploadPhotoVo;

import com.baomidou.mybatisplus.service.IService;



/**
 * @author RipinYan
 * @ClassName: GgMerchantService
 * @Description: 商户入驻相关接口
 * @date 2018/9/18 下午4:11
 */
public interface GgMerchantService extends IService<GgMerchant> {

	/**
	 * insert(商户数据保存)
	 *
	 * @Title: insert
	 * @Description:
	 * @param ggMerchantVo 商户关联数据信息
	 * @throws
	 * @author Ripin Yan
	 * @return void
	 */
	void insert(GgMerchantVo ggMerchantVo);

	/**
	 * findMerchantById(通过id查询商户信息)
	 *
	 * @Title: findMerchantById
	 * @Description: 
	 * @param id 主键
	 * @throws 
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.GgMerchantVo
	 */
	GgMerchantVo findMerchantById(Long id);

	/**
	 * findMerchantByMerchantId(根据商户号查询商户信息)
	 *
	 * @Title: findMerchantByMerchantId
	 * @Description:
	 * @param merchantId 商户号
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.GgMerchantVo
	 */
	GgMerchantVo findMerchantByMerchantId(String merchantId);

	/**
	 * sendSmsCode(发送短信验证码)
	 *
	 * @Title: sendSmsCode
	 * @Description:
	 * @param smsVo 短信vo
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.RespInfoVo
	 */
	RespInfoVo sendSmsCode(SmsVo smsVo);
	
	/**
	 * uploadPhoto(上传图片)
	 *
	 * @Title: uploadPhoto
	 * @Description:
	 * @param uploadPhotoVo 图片vo
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.UploadPhotoVo
	 */
	UploadPhotoVo uploadPhoto(UploadPhotoVo uploadPhotoVo);

	/**
	 * regist(申请商户入驻)
	 *
	 * @Title: regist
	 * @Description:
	 * @param register 商户信息vo
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.GgMerchantVo
	 */
	GgMerchantVo regist(GgMerchantVo register);
	
	/**
	 * registerQuery(查询商户入驻结果)
	 *
	 * @Title: registerQuery
	 * @Description:
	 * @param orderNo 订单号
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.RegisterQueryVo
	 */
	RegisterQueryVo registerQuery(String orderNo);

	/**
	 * updateMerchant(修改商户信息接口)
	 *
	 * @Title: updateMerchant
	 * @Description:
	 * @param updateMerchant 修改的商户内容
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.RespInfoVo
	 */
	RespInfoVo updateMerchant(GgMerchantVo updateMerchant);

	/**
	 * merchantQuery(查询商户信息)
	 *
	 * @Title: merchantQuery
	 * @Description:
	 * @param orderNo 订单号
	 * @throws
	 * @author Ripin Yan
	 * @return ins.platform.aggpay.trade.vo.GgMerchantVo
	 */
	GgMerchantVo merchantQuery(String orderNo);

    /**
     * merchantFreeze(关闭商户)
     *
     * @Title: merchantFreeze
     * @Description:
     * @param merchantId 商户号
     * @param freezeReason 理由
     * @throws
     * @author Ripin Yan
     * @return ins.platform.aggpay.trade.vo.RespInfoVo
     */
	RespInfoVo merchantFreeze(String merchantId , String freezeReason);
    /**
     * merchantUnfreeze(开启商户)
     *
     * @Title: merchantUnfreeze
     * @Description:
     * @param merchantId 商户号
     * @param unfreezeReason 理由
     * @throws
     * @author Ripin Yan
     * @return ins.platform.aggpay.trade.vo.RespInfoVo
     */
	RespInfoVo merchantUnfreeze(String merchantId , String unFreezeReason);

}