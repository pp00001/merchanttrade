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
import ins.platform.aggpay.trade.util.ApiCallUtil;
import ins.platform.aggpay.trade.vo.GgMerchantVo;
import ins.platform.aggpay.trade.vo.RegisterQueryVo;
import ins.platform.aggpay.trade.vo.RespInfoVo;
import ins.platform.aggpay.trade.vo.SmsVo;
import ins.platform.aggpay.trade.vo.UploadPhotoVo;

import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
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
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

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
	 *
	 * @param ggMerchant 实体
	 * @return success/false
	 */
	@PostMapping
	public R<Boolean> add(@RequestBody GgMerchantVo ggMerchantVo) {
		ggMerchantService.insert(ggMerchantVo);
		return new R<>(true);
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
     *
     * @param ggMerchantVo 实体
     * @return success/false
     */
    @PutMapping
    public String edit(@RequestBody GgMerchantVo ggMerchantVo) {
        JSONObject jo = new JSONObject();
        String logPrefix = "【商户信息修改】";
        try {
            ggMerchantVo.setUpdateTime(new Date());
            ggMerchantVo.setOutTradeNo(ApiCallUtil.generateOutTradeNo());
            if (StringUtils.isBlank(ggMerchantVo.getOutMerchantId())) {
                ggMerchantVo.setOutMerchantId(ggMerchantVo.getOutTradeNo());
            }
            GgMerchantVo resultVo = ggMerchantService.update(ggMerchantVo);
            RespInfoVo respInfoVo = resultVo.getRespInfo();
            if (respInfoVo != null) {
                jo.put("resCode", respInfoVo.getResultCode());
                jo.put("resMsg", respInfoVo.getResultMsg());
                jo.put("outTradeNo", resultVo.getOutTradeNo());
            }
        } catch (Exception e) {
            String errorMessage = logPrefix + "异常:" + e.getMessage();
            logger.error(errorMessage, e);
            jo.put("resCode", "1005");
            jo.put("resMsg", errorMessage);
        }
        return jo.toJSONString();

    }

    /**
     * 接收图片流接口
     *
     * @param file
     * @param request
     */
    @PostMapping(value = "/uploadImg")
    public String UploadImage(HttpServletRequest request,
                            @RequestParam(value = "file", required = false) MultipartFile file) {
        //获取文件在服务器的储存位置
        String path = request.getSession().getServletContext().getRealPath("/upload");
        File filePath = new File(path);
        logger.info("文件的保存路径：" + path);
        if (!filePath.exists() && !filePath.isDirectory()) {
            filePath.mkdir();
        }

        //获取原始文件名称(包含格式)
        String originalFileName = file.getOriginalFilename();
        logger.info("原始文件名称：" + originalFileName);

        //获取文件类型，以最后一个`.`为标识
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        logger.info("文件类型：" + type);
        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        //设置文件新名称: 当前时间+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(d);
        String fileName = date + name + "." + type;
        logger.info(fileName);

        //在指定路径下创建一个文件
        File targetFile = new File(path, fileName);

        //将文件保存到服务器指定位置
        try {
            file.transferTo(targetFile);
            logger.info("图片上传成功！");
        } catch (Exception e) {
            logger.error("图片上传异常："+e);
        }

        return "/upload/" + fileName;
    }

	/**
	 * 5.1.1	短信验证码发送接口
	 *
	 * @param smsVo 短信vo
	 * @return
	 */
	@PostMapping("/sendSmsCode")
	public R<Object> sendSmsCode(@RequestBody SmsVo smsVo) {
		return new R<>(ggMerchantService.sendSmsCode(smsVo));
	}

	/**
	 * 5.1.2	图片上传接口
	 *
	 * @param uploadPhotoVo 上传图片信息
	 * @return
	 */
	@PostMapping("/upload/photo")
	public R<Object> uploadPhoto(@RequestBody UploadPhotoVo uploadPhotoVo) {
		return new R<>(ggMerchantService.uploadPhoto(uploadPhotoVo));
	}

	/**
	 * 5.2.2	商户入驻申请接口（不开银行账户）
	 *
	 * @param register
	 * @return
	 */
	@PostMapping("/regist")
	public String regist(@RequestBody GgMerchantVo register) {

		JSONObject jo = new JSONObject();
		String logPrefix = "【商户入驻申请】";
		try {
			register.setOutTradeNo(ApiCallUtil.generateOutTradeNo());
			if (StringUtils.isBlank(register.getOutMerchantId())) {
				register.setOutMerchantId(register.getOutTradeNo());
			}
			GgMerchantVo resultVo = ggMerchantService.regist(register);
			RespInfoVo respInfoVo = resultVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("orderNo", resultVo.getOrderNo());
				jo.put("outTradeNo", resultVo.getOutTradeNo());
			}
		} catch (Exception e) {
			String errorMessage = logPrefix + "异常:" + e.getMessage();
			logger.error(errorMessage, e);
			jo.put("resCode", "1001");
			jo.put("resMsg", errorMessage);
		}
		return jo.toJSONString();
	}

	/**
	 * 5.2.3	商户入驻结果查询
	 *
	 * @param orderNo 订单号
	 * @return
	 */
	@GetMapping("/register/query/{orderNo}")
	public String registerQuery(@PathVariable String orderNo) {
		String logPrefix = "【商户入驻结果查询】";
		JSONObject jo = new JSONObject();
		String errorMessage;

		if (StringUtils.isBlank(orderNo)) {
			errorMessage = "订单号不能为空！";
			logger.info("{}信息：{}", logPrefix, errorMessage);
			jo.put("resCode", "1002");
			jo.put("resMsg", errorMessage);
			return jo.toJSONString();
		}
		try {
			RegisterQueryVo resultVo = ggMerchantService.registerQuery(orderNo);
			RespInfoVo respInfoVo = resultVo.getRespInfo();
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
				jo.put("merchantId", resultVo.getMerchantId());
				jo.put("registerStatus", resultVo.getRegisterStatus());
				jo.put("failReason", resultVo.getFailReason());
				jo.put("wechatChannelList", resultVo.getWechatChannelVoList());
			} else {
				errorMessage = "查询失败！";
				logger.info("{}信息：{}", logPrefix, errorMessage);
				jo.put("resCode", "1003");
				jo.put("resMsg", errorMessage);
			}
		} catch (Exception e) {
			errorMessage = logPrefix + "异常:" + e.getMessage();
			logger.error(errorMessage, e);
			jo.put("resCode", "1003");
			jo.put("resMsg", errorMessage);
		}
		return jo.toJSONString();
	}

	/**
	 * 5.2.5 商户信息修改
	 * 商户入驻成功后，不允许单纯修改商户信息，当且仅当发起商户信息修改成功后，才能修改商户表数据
	 *
	 * @param update 待修改的商户vo
	 * @return
	 */
	@PostMapping("/updateMerchant")
	public R<Object> updateMerchant(@RequestBody GgMerchantVo update) {
		JSONObject jo = new JSONObject();
		String logPrefix = "【商户信息修改】";
		try {
			update.setOutTradeNo(ApiCallUtil.generateOutTradeNo());
			RespInfoVo respInfoVo = ggMerchantService.updateMerchant(update);
			if (respInfoVo != null) {
				jo.put("resCode", respInfoVo.getResultCode());
				jo.put("resMsg", respInfoVo.getResultMsg());
			}
		} catch (Exception e) {
			String errorMessage = logPrefix + "异常:" + e.getMessage();
			logger.error(errorMessage, e);
			jo.put("resCode", "1004");
			jo.put("resMsg", errorMessage);
		}
		return new R<>(jo);
	}

	/**
	 * 5.2.6	商户信息查询
	 */
	@GetMapping("/merchantQuery/{merchantId}")
	public R<Object> merchantQuery(@PathVariable String merchantId) {
		return new R<>(ggMerchantService.merchantQuery(merchantId));
	}

	/**
	 * 5.2.7	商户关闭接口
	 */
	@PutMapping("/freeze")
	public R<Object> merchantFreeze(@RequestBody Map<String,String> params) {
        String merchantId = params.get("merchantId");
        String freezeReason = params.get("freezeReason");
        return new R<>(ggMerchantService.merchantFreeze(merchantId, freezeReason));
    }

	/**
	 * 5.2.8	商户开启接口
	 */
	@PutMapping("/unFreeze")
	public R<Object> merchantUnfreeze(@RequestBody Map<String,String> params) {
	    String merchantId = params.get("merchantId");
	    String unFreezeReason = params.get("unFreezeReason");
		return new R<>(ggMerchantService.merchantUnfreeze(merchantId , unFreezeReason));
	}

}
