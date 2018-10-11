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
import ins.platform.aggpay.trade.entity.GpTradeOrder;
import ins.platform.aggpay.trade.service.GpTradeOrderService;

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

/**
 * <p>
 * 交易订单表 前端控制器
 * </p>
 *
 * @author ripin
 * @since 2018-10-11
 */
@RestController
@RequestMapping("/gpTradeOrder")
public class GpTradeOrderController extends BaseController {
	@Autowired
	private GpTradeOrderService gpTradeOrderService;

	/**
	 * 通过ID查询
	 *
	 * @param id ID
	 * @return GpTradeOrder
	 */
	@GetMapping("/{id}")
	public R<GpTradeOrder> get(@PathVariable Integer id) {
		return new R<>(gpTradeOrderService.selectById(id));
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
		return gpTradeOrderService.selectPage(new Query<>(params), new EntityWrapper<>());
	}

	/**
	 * 添加
	 *
	 * @param gpTradeOrder 实体
	 * @return success/false
	 */
	@PostMapping
	public R<Boolean> add(@RequestBody GpTradeOrder gpTradeOrder) {
		return new R<>(gpTradeOrderService.insert(gpTradeOrder));
	}

	/**
	 * 删除
	 *
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	public R<Boolean> delete(@PathVariable Long id) {
		GpTradeOrder gpTradeOrder = new GpTradeOrder();
		gpTradeOrder.setId(id);
		gpTradeOrder.setUpdateTime(new Date());
		gpTradeOrder.setDelFlag(CommonConstant.STATUS_DEL);
		return new R<>(gpTradeOrderService.updateById(gpTradeOrder));
	}

	/**
	 * 编辑
	 *
	 * @param gpTradeOrder 实体
	 * @return success/false
	 */
	@PutMapping
	public R<Boolean> edit(@RequestBody GpTradeOrder gpTradeOrder) {
		gpTradeOrder.setUpdateTime(new Date());
		return new R<>(gpTradeOrderService.updateById(gpTradeOrder));
	}
}
