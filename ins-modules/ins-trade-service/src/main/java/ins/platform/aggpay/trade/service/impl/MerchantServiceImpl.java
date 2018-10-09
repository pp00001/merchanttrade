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

package ins.platform.aggpay.trade.service.impl;

import ins.platform.aggpay.trade.common.util.MapUtil;
import ins.platform.aggpay.trade.mapper.MerchantMapper;
import ins.platform.aggpay.trade.model.entity.Merchant;
import ins.platform.aggpay.trade.model.vo.RegistResVo;
import ins.platform.aggpay.trade.model.vo.RegisterQueryVo;
import ins.platform.aggpay.trade.service.MerchantService;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.mybank.bkmerchant.merchant.Register;
import com.mybank.bkmerchant.merchant.RegisterQuery;

/**
 * <p>
 * 商户信息主表 服务实现类
 * </p>
 *
 * @author ripin
 * @since 2018-09-18
 */
@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

	@Override
	public RegistResVo regist(Register register) {

		try {
			Map<String, Object> call = register.call();
			Object resultStatus = call.get("resultStatus");
			if(resultStatus.equals("S")){

				//update merchant 入驻结果

			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return null;
	}

	@Override
	public RegisterQueryVo registerQuery(String isvOrgId, String orderNo) {
		RegisterQuery rq = new RegisterQuery(orderNo);
		RegisterQueryVo vo = null;
		try {
			Map<String, Object> call = rq.call();
			vo = MapUtil.map2Obj(call,RegisterQueryVo.class);
			System.out.println(vo.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return vo;
	}

}
