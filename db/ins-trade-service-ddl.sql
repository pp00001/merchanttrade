/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云-mysql
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : 47.106.91.233:3306
 Source Schema         : sinosoft

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 18/09/2018 17:23:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS merchant;
DROP TABLE IF EXISTS merchant_detail;
DROP TABLE IF EXISTS fee_param;
DROP TABLE IF EXISTS bank_card_param;

/*==============================================================*/
/* Table: merchant                                              */
/*==============================================================*/
CREATE TABLE merchant (
  id                   BIGINT (20) NOT NULL auto_increment COMMENT '主键',
  out_merchant_id      VARCHAR(64) COMMENT '外部商户号',
  order_no             VARCHAR(64) COMMENT '申请单号',
  merchant_id          VARCHAR(64) COMMENT '商户号',
  merchant_name        VARCHAR(50)  DEFAULT NULL COMMENT '商户名称',
  merchant_type        VARCHAR(8)   DEFAULT NULL COMMENT '商户类型 - 01:自然人,02:个体工商户,03:企业商户',
  deal_type            VARCHAR(8)   DEFAULT NULL COMMENT '商户经营类型 - 01:实体特约商户, 02:网络特约商户, 03:实体兼网络特约商户',
  support_prepayment   VARCHAR(8)   DEFAULT NULL COMMENT '商户清算资金是否支持T+0到账 - Y：支持, N：不支持',
  settle_mode          VARCHAR(8)   DEFAULT NULL COMMENT '结算方式 - 01：结算到他行卡, 02：结算到余利宝, 03：结算到活期户（暂不开放）, 04：自提打款（暂不开放）',
  mcc                  VARCHAR(16)  DEFAULT NULL COMMENT '经营类目',
  trade_type_list      VARCHAR(32)  DEFAULT NULL COMMENT '支持交易类型列表 - 01：正扫交易, 02：反扫交易, 06：退款交易, 08: 动态订单扫码',
  pay_channel_list     VARCHAR(32)  DEFAULT NULL COMMENT '支持支付渠道列表 - 01：支付宝, 02：微信支付, 03：手机QQ（暂未开放）',
  denied_pay_tool_list VARCHAR(32)  DEFAULT NULL COMMENT '禁用支付方式 - 02：信用卡, 03：花呗（仅支付宝）',
  auth_code            VARCHAR(32)  DEFAULT NULL COMMENT '手机验证码',
  out_trade_no         VARCHAR(64)  DEFAULT NULL COMMENT '外部交易号',
  support_stage        VARCHAR(1)   DEFAULT 'N' COMMENT '花呗分期目前仅支持用户承担手续费模式 - Y：支持, N：不支持(默认值)',
  partner_type         VARCHAR(2)   DEFAULT NULL COMMENT '商户在进行微信支付H5支付时所使用的公众号相关信息的类型 - 01：商户自有公众号,  03：ISV公众号',
  alipay_source        VARCHAR(32)  DEFAULT NULL COMMENT '记录商户推荐方PID',
  wechat_channel       VARCHAR(10)  DEFAULT NULL COMMENT '指定微信渠道号入驻',
  rate_version         VARCHAR(16)  DEFAULT NULL COMMENT '支付宝费率版本 - RS：标准费率, R0：0费率, R1：千一费率, RBLUE：蓝海行动餐饮0费率',
  register_status      VARCHAR(1)   DEFAULT NULL COMMENT '入驻状态 - 0：审核中，1：成功，2：失败',
  fail_reason          VARCHAR(256) DEFAULT NULL COMMENT '入驻失败原因',
  smid                 VARCHAR(128) DEFAULT NULL COMMENT 'Smid',
  channel_id           VARCHAR(64)  DEFAULT NULL COMMENT '微信支付渠道号',
  wechat_merch_id      VARCHAR(64)  DEFAULT NULL COMMENT '微信商户号',
  wechat_status        VARCHAR(1)   DEFAULT NULL COMMENT '微信入驻状态 - 0：处理中，1：成功，2：失败',
  wechat_fail_reason   VARCHAR(256) DEFAULT NULL COMMENT '微信入驻失败原因',
  valid_ind            VARCHAR(1)   DEFAULT '1' COMMENT '是否有效 - 1：有效，0：无效',
  del_flag             CHAR(1)      DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  creator_code         VARCHAR(32)  DEFAULT NULL COMMENT '创建人代码',
  create_time          TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater_code         VARCHAR(32)  DEFAULT NULL COMMENT '更新人代码',
  update_time          TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE =utf8mb4_bin COMMENT = '商户信息主表';

/*==============================================================*/
/* Table: Merchant_Detail                                       */
/*==============================================================*/
CREATE TABLE merchant_detail
(
  id                     BIGINT (20) NOT NULL auto_increment COMMENT '主键',
  out_merchant_id        VARCHAR(64) NOT NULL COMMENT '外部商户号',
  alias                  VARCHAR(20)  DEFAULT NULL COMMENT '商户简称',
  contact_mobile         VARCHAR(20)  DEFAULT NULL COMMENT '联系人手机号',
  contact_name           VARCHAR(256) DEFAULT NULL COMMENT '联系人姓名',
  province               VARCHAR(16)  DEFAULT NULL COMMENT '省份',
  city                   VARCHAR(16)  DEFAULT NULL COMMENT '城市',
  district               VARCHAR(16)  DEFAULT NULL COMMENT '区',
  address                VARCHAR(128) DEFAULT NULL COMMENT '地址',
  service_phone_no       VARCHAR(32)  DEFAULT NULL COMMENT '商户客服电话，注：不支持-',
  email                  VARCHAR(64)  DEFAULT NULL COMMENT '邮箱，注：不支持-',
  legal_person           VARCHAR(256) DEFAULT NULL COMMENT '企业法人名称',
  principal_mobile       VARCHAR(32)  DEFAULT NULL COMMENT '负责人手机号,自然人或个体工商户上送，企业可不上送',
  principal_cert_type    VARCHAR(2)   DEFAULT '01' COMMENT '负责人证件类型 - 01：身份证',
  principal_cert_no      VARCHAR(32)  DEFAULT NULL COMMENT '负责人证件号码',
  principal_person       VARCHAR(256) DEFAULT NULL COMMENT '负责人名称或企业法人代表姓名',
  buss_auth_num          VARCHAR(32)  DEFAULT NULL COMMENT '负责人名称或企业法人代表姓名',
  cert_org_code          VARCHAR(32)  DEFAULT NULL COMMENT '组织机构代码证号',
  cert_photo_a           VARCHAR(64)  DEFAULT NULL COMMENT '负责人或企业法人代表的身份证图片正面',
  cert_photo_b           VARCHAR(64)  DEFAULT NULL COMMENT '负责人或企业法人代表的身份证图片反面',
  license_photo          VARCHAR(64)  DEFAULT NULL COMMENT '营业执照图片',
  prg_photo              VARCHAR(64)  DEFAULT NULL COMMENT '组织机构代码证图片',
  industry_license_photo VARCHAR(64)  DEFAULT NULL COMMENT '开户许可证照片',
  shop_photo             VARCHAR(64)  DEFAULT NULL COMMENT '门头照',
  other_photo            VARCHAR(64)  DEFAULT NULL COMMENT '其他图片',
  subscribe_appi_d       VARCHAR(256) DEFAULT NULL COMMENT '需关注的公众号appid',
  valid_ind              VARCHAR(1)   DEFAULT '1' COMMENT '是否有效 - 1：有效，0：无效',
  del_flag               CHAR(1)      DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  creator_code           VARCHAR(32)  DEFAULT NULL COMMENT '创建人代码',
  create_time            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater_code           VARCHAR(32)  DEFAULT NULL COMMENT '更新人代码',
  update_time            TIMESTAMP   NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE =utf8mb4_bin COMMENT = '商户详情表';

/*==============================================================*/
/* Table: fee_param                                             */
/*==============================================================*/
CREATE TABLE fee_param
(
  id              BIGINT (20) NOT NULL auto_increment COMMENT '主键',
  out_merchant_id VARCHAR(64) NOT NULL COMMENT '外部商户号',
  channeltype     VARCHAR(8)  DEFAULT NULL COMMENT '渠道类型 - 01：支付宝，02：微信支付',
  feetype         VARCHAR(8)  DEFAULT NULL COMMENT '费用类型 - 01：t0收单手续费，02：t1收单手续费',
  feevalue        VARCHAR(15) DEFAULT NULL COMMENT '费率最多支持小数点后5位',
  valid_ind       VARCHAR(1)  DEFAULT '1' COMMENT '是否有效 - 1：有效，0：无效',
  del_flag        CHAR(1)     DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  creator_code    VARCHAR(32) DEFAULT NULL COMMENT '创建人代码',
  create_time     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater_code    VARCHAR(32) DEFAULT NULL COMMENT '更新人代码',
  update_time     TIMESTAMP   NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE =utf8mb4_bin COMMENT = '手续费信息表';


/*==============================================================*/
/* Table: bank_card_param                                       */
/*==============================================================*/
CREATE TABLE bank_card_param
(
  id                  BIGINT (20) NOT NULL auto_increment COMMENT '主键',
  out_merchant_id     VARCHAR(64) NOT NULL COMMENT '外部商户号',
  bank_card_no        VARCHAR(32)  DEFAULT NULL COMMENT '银行卡号',
  bank_cert_name      VARCHAR(256) DEFAULT NULL COMMENT '银行账户户名',
  account_type        VARCHAR(8)   DEFAULT NULL COMMENT '账户类型 - 01：对私账户（自然人、个体工商户），02：对公账户（企业类型）',
  contact_line        VARCHAR(128) DEFAULT NULL COMMENT '联行号',
  branch_name         VARCHAR(64)  DEFAULT NULL COMMENT '开户支行名称',
  branch_province     VARCHAR(16)  DEFAULT NULL COMMENT '开户支行所在省',
  branch_city         VARCHAR(16)  DEFAULT NULL COMMENT '开户支行所在市',
  cert_type           VARCHAR(2)   DEFAULT NULL COMMENT '持卡人证件类型 - 01：身份证（对公账户不需要填写）',
  cert_no             VARCHAR(256) DEFAULT NULL COMMENT '持卡人证件号码（对公账户不需要填写）',
  card_holder_address VARCHAR(128) DEFAULT NULL COMMENT '持卡人地址（对公账户不需要填写）',
  valid_ind           VARCHAR(1)   DEFAULT '1' COMMENT '是否有效 - 1：有效，0：无效',
  del_flag            CHAR(1)      DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  creator_code        VARCHAR(32)  DEFAULT NULL COMMENT '创建人代码',
  create_time         TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updater_code        VARCHAR(32)  DEFAULT NULL COMMENT '更新人代码',
  update_time         TIMESTAMP   NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE =utf8mb4_bin COMMENT = '清算卡信息表';


SET FOREIGN_KEY_CHECKS = 1;
