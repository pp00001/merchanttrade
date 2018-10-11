/*
 * Copyright (c) 2018-2020, Ripin Yan. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ins.platform.aggpay.trade.controller;

import ins.platform.aggpay.common.constant.CommonConstant;
import ins.platform.aggpay.common.util.Query;
import ins.platform.aggpay.common.util.R;
import ins.platform.aggpay.common.web.BaseController;
import ins.platform.aggpay.trade.entity.GgMerchant;
import ins.platform.aggpay.trade.service.GgMerchantService;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.mybank.bkmerchant.merchant.UpdateMerchant;
import com.mybank.bkmerchant.merchant.UploadPhoto;
import com.mybank.bkmerchant.trade.SendSmsCode;

/**
 * <p>
 * 商户信息主表 前端控制器
 * </p>
 *
 * @author ripin
 * @since 2018-09-18
 */
@RestController
@RequestMapping("/merchant")
public class GgMerchantController extends BaseController {

	@Autowired
	private GgMerchantService ggMerchantService;

	/**
	 * 通过ID查询
	 *
	 * @param id ID
	 * @return GgMerchant
	 */
	@GetMapping("/{id}")
	public R<GgMerchant> get(@PathVariable Integer id) {
        return new R<>(ggMerchantService.selectById(id));
	}


	/**
	 * 分页查询信息
	 *
	 * @param params 分页对象
	 * @return 分页对象
	 */
	@RequestMapping("/page")
	public Page page(@RequestParam Map<String, Object> params) {
		params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return ggMerchantService.selectPage(new Query<>(params), new EntityWrapper<>());
	}

	/**
	 * 添加
     * @param  ggMerchant  实体
	 * @return success/false
	 */
	@PostMapping
    public R<Boolean> add(@RequestBody GgMerchant ggMerchant) {
        return new R<>(ggMerchantService.insert(ggMerchant));
	}

	/**
	 * 删除
	 *
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        GgMerchant ggMerchant = new GgMerchant();
        ggMerchant.setId(id);
        ggMerchant.setUpdateTime(new Date());
        ggMerchant.setDelFlag(CommonConstant.STATUS_DEL);
        return new R<>(ggMerchantService.updateById(ggMerchant));
	}

	/**
	 * 编辑
     * @param  ggMerchant  实体
	 * @return success/false
	 */
	@PutMapping
    public R<Boolean> edit(@RequestBody GgMerchant ggMerchant) {
        ggMerchant.setUpdateTime(new Date());
        return new R<>(ggMerchantService.updateById(ggMerchant));
	}

	/**
	 * 5.1.1	短信验证码发送接口
	 *
	 * @param sendSmsCode
	 * @return
	 */
	@PostMapping("/sendSmsCode")
	public R<Object> sendsmscode(@RequestBody SendSmsCode sendSmsCode) {
		return new R<>(ggMerchantService.sendsmscode(sendSmsCode));
	}

	/**
	 * 5.1.2	图片上传接口
	 *
	 * @param uploadPhoto
	 * @return
	 */
	@PostMapping("/uploadphoto")
	public R<Object> uploadphoto(@RequestBody UploadPhoto uploadPhoto) {
		return new R<>(ggMerchantService.uploadphoto(uploadPhoto));
	}

	/**
	 * 5.2.2	商户入驻申请接口（不开银行账户）
	 *
	 * @param register
	 * @return
	 */
	@PostMapping("/regist")
	public R<Object> regist(@RequestBody GgMerchantVo register) {
		return new R<>(ggMerchantService.regist(register));
	}

	/**
	 * 5.2.3	商户入驻结果查询
	 *
	 * @param orderNo
	 * @return
	 */
	@GetMapping("registerQuery/{orderNo}")
	public R<RegisterQueryVo> registerQuery(@PathVariable String orderNo) {
		return new R<>(ggMerchantService.registerQuery(orderNo));
	}

	/**
	 * 5.2.5	商户信息修改
	 */
	@PostMapping("/updateMerchant")
	public R<Object> updateMerchant(UpdateMerchant updateMerchant) {
		return new R<>(ggMerchantService.updateMerchant(updateMerchant));
	}

	/**
	 * 5.2.6	商户信息查询
	 */
	@GetMapping("/{isvOrgId}/{merchantId}")
	public R<Object> merchantQuery(@PathVariable String isvOrgId, @PathVariable String merchantId) {
		return new R<>(ggMerchantService.merchantQuery(isvOrgId, merchantId));
	}

	/**
	 * 5.2.7	商户关闭接口
	 */
	@GetMapping("/{isvOrgId}/{merchantId}/{freezeReason}/{outTradeNo}")
	public R<Object> merchantFreeze(@PathVariable String isvOrgId, @PathVariable String merchantId, @PathVariable String freezeReason, @PathVariable
			String outTradeNo) {
		return new R<>(ggMerchantService.merchantFreeze(isvOrgId, merchantId, freezeReason, outTradeNo));
	}

	/**
	 * 5.2.8	商户开启接口
	 */
	@GetMapping("/{isvOrgId}/{merchantId}/{unfreezeReason}/{outTradeNo}")
	public R<Object> merchantUnfreeze(@PathVariable String isvOrgId, @PathVariable String merchantId, @PathVariable String unfreezeReason,
	                                  @PathVariable String outTradeNo) {
		return new R<>(ggMerchantService.merchantUnfreeze(isvOrgId, merchantId, unfreezeReason, outTradeNo));
	}

}
