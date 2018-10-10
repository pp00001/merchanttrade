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

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

BEGIN;

-- ----------------------------
--  Records of sys_dept
-- ----------------------------
INSERT INTO sys_dept VALUES ('1', '山东农信', NULL, '2018-01-22 19:00:23', '2018-01-23 12:40:46', '0',
                             '0'), ('2', '沙县国际', NULL, '2018-01-22 19:00:38', '2018-01-23 12:42:04', '0', '0'),
  ('3', '潍坊农信', NULL, '2018-01-22 19:00:44', '2018-01-23 12:40:56', '0', '1'),
  ('4', '高新农信', NULL, '2018-01-22 19:00:52', '2018-01-23 12:41:11', '0', '3'),
  ('5', '院校农信', NULL, '2018-01-22 19:00:57', '2018-01-23 12:41:22', '0', '4'),
  ('6', '潍坊学院农信', '115', '2018-01-22 19:01:06', '2018-03-07 16:22:04', '0', '5'),
  ('7', '山东沙县', NULL, '2018-01-22 19:01:57', '2018-01-23 12:42:15', '0', '2'),
  ('8', '潍坊沙县', NULL, '2018-01-22 19:02:03', '2018-01-23 12:42:23', '0', '7'),
  ('9', '高新沙县', NULL, '2018-01-22 19:02:14', '2018-03-07 16:28:14', '0', '8');


-- ----------------------------
--  Records of sys_dept_relation
-- ----------------------------
INSERT INTO sys_dept_relation VALUES ('1',
                                      '1'), ('1', '3'), ('1', '4'), ('1', '5'), ('1', '6'), ('2', '2'), ('2', '7'), ('2', '8'), ('2', '9'),
  ('3', '3'), ('3', '4'), ('3', '5'), ('3', '6'), ('4', '4'), ('4', '5'), ('4', '6'), ('5', '5'), ('5', '6'), ('6', '6'), ('7', '7'), ('7', '8'),
  ('7', '9'), ('8', '8'), ('8', '9'), ('9', '9');


-- ----------------------------
--  Records of sys_dict
-- ----------------------------
INSERT INTO sys_dict VALUES ('2', '9', '异常', 'log_type', '日志异常', '1', '2017-12-28 13:06:39', '2018-01-06 10:54:41', NULL,
                             '0'), ('3', '0', '正常', 'log_type', '正常', '1', '2018-05-11 23:52:57', '2018-05-11 23:52:57', '123', '0');


-- ----------------------------
--  Records of sys_menu
-- ----------------------------
INSERT INTO sys_menu VALUES
  ('-1', '1', '系统管理', NULL, '/admin', NULL, NULL, 'icon-xitongguanli', 'Layout', '1', '0', '2017-11-07 20:56:00', '2018-05-14 21:53:22', '0'),
  ('1', '2', '用户管理', NULL, 'user', '', NULL, 'icon-yonghuguanli', 'views/admin/user/index', '2', '0', '2017-11-02 22:24:37', '2018-05-14 22:11:35', '0'),
  ('1', '3', '菜单管理', NULL, 'menu', '', NULL, 'icon-caidanguanli', 'views/admin/menu/index', '3', '0', '2017-11-08 09:57:27', '2018-05-14 22:11:21', '0'),
  ('1', '4', '角色管理', NULL, 'role', NULL, NULL, 'icon-jiaoseguanli', 'views/admin/role/index', '4', '0', '2017-11-08 10:13:37', '2018-05-14 22:11:19', '0'),
  ('1', '5', '日志管理', NULL, 'log', NULL, NULL, 'icon-rizhiguanli', 'views/admin/log/index', '5', '0', '2017-11-20 14:06:22', '2018-05-14 22:11:18', '0'),
  ('1', '6', '字典管理', NULL, 'dict', NULL, NULL, 'icon-zygl', 'views/admin/dict/index', '6', '0', '2017-11-29 11:30:52', '2018-05-14 22:12:48', '0'),
  ('1', '7', '部门管理', NULL, 'dept', NULL, NULL, 'icon-iconbmgl', 'views/admin/dept/index', '7', '0', '2018-01-20 13:17:19', '2018-05-14 22:11:16', '0'),
  ('-1', '8', '系统监控', NULL, '', NULL, NULL, 'icon-iconbmgl', NULL, '8', '0', '2018-01-22 12:30:41', '2018-05-14 20:41:16', '0'),
  ('8', '9', '服务监控', NULL, 'http://139.224.200.249:5001', NULL, NULL, 'icon-jiankong', NULL, '9', '0', '2018-01-23 10:53:33', '2018-04-21 03:51:56', '0'),
  ('8', '10', 'zipkin监控', NULL, 'http://139.224.200.249:5002', NULL, NULL, 'icon-jiankong', NULL, '11', '0', '2018-01-23 10:55:18', '2018-04-22 07:02:34', '0'),
  ('8', '11', 'pinpoint监控', NULL, 'https://pinpoint.pig4cloud.com', NULL, NULL, 'icon-xiazaihuancun', NULL, '10', '0', '2018-01-25 11:08:52', '2018-04-22 07:02:38', '0'),
  ('8', '12', '缓存状态', NULL, 'http://139.224.200.249:8585', NULL, NULL, 'icon-ecs-status', NULL, '12', '0', '2018-01-23 10:56:11', '2018-04-21 03:51:47', '0'),
  ('8', '13', 'ELK状态', NULL, 'http://139.224.200.249:5601', NULL, NULL, 'icon-ecs-status', NULL, '13', '0', '2018-01-23 10:55:47', '2018-04-21 03:51:40', '0'),
  ('8', '14', '接口文档', NULL, 'http://139.224.200.249:9999/swagger-ui.html', NULL, NULL, 'icon-wendangdocument72', NULL, '14', '0', '2018-01-23 10:56:43', '2018-04-21 03:50:47', '0'),
  ('8', '15', '任务监控', NULL, 'http://139.224.200.249:8899', NULL, NULL, 'icon-jiankong', NULL, '15', '0', '2018-01-23 10:55:18', '2018-04-21 03:51:34', '0'),
  ('2', '21', '用户查看', '', NULL, '/admin/user/**', 'GET', NULL, NULL, NULL, '1', '2017-11-07 20:58:05', '2018-02-04 14:28:49', '0'),
  ('2', '22', '用户新增', 'sys_user_add', NULL, '/admin/user/*', 'POST', NULL, NULL, NULL, '1', '2017-11-08 09:52:09', '2017-12-04 16:31:10', '0'),
  ('2', '23', '用户修改', 'sys_user_upd', NULL, '/admin/user/**', 'PUT', NULL, NULL, NULL, '1', '2017-11-08 09:52:48', '2018-01-17 17:40:01', '0'),
  ('2', '24', '用户删除', 'sys_user_del', NULL, '/admin/user/*', 'DELETE', NULL, NULL, NULL, '1', '2017-11-08 09:54:01', '2017-12-04 16:31:18', '0'),
  ('3', '31', '菜单查看', NULL, NULL, '/admin/menu/**', 'GET', NULL, NULL, NULL, '1', '2017-11-08 09:57:56', '2017-11-14 17:29:17', '0'),
  ('3', '32', '菜单新增', 'sys_menu_add', NULL, '/admin/menu/*', 'POST', NULL, NULL, NULL, '1', '2017-11-08 10:15:53', '2018-01-20 14:37:50', '0'),
  ('3', '33', '菜单修改', 'sys_menu_edit', NULL, '/admin/menu/*', 'PUT', NULL, NULL, NULL, '1', '2017-11-08 10:16:23', '2018-01-20 14:37:56', '0'),
  ('3', '34', '菜单删除', 'sys_menu_del', NULL, '/admin/menu/*', 'DELETE', NULL, NULL, NULL, '1', '2017-11-08 10:16:43', '2018-01-20 14:38:03', '0'),
  ('4', '41', '角色查看', NULL, NULL, '/admin/role/**', 'GET', NULL, NULL, NULL, '1', '2017-11-08 10:14:01', '2018-02-04 13:55:06', '0'),
  ('4', '42', '角色新增', 'sys_role_add', NULL, '/admin/role/*', 'POST', NULL, NULL, NULL, '1', '2017-11-08 10:14:18', '2018-04-20 07:21:38', '0'),
  ('4', '43', '角色修改', 'sys_role_edit', NULL, '/admin/role/*', 'PUT', NULL, NULL, NULL, '1', '2017-11-08 10:14:41', '2018-04-20 07:21:50', '0'),
  ('4', '44', '角色删除', 'sys_role_del', NULL, '/admin/role/*', 'DELETE', NULL, NULL, NULL, '1', '2017-11-08 10:14:59', '2018-04-20 07:22:00', '0'),
  ('4', '45', '分配权限', 'sys_role_perm', NULL, '/admin/role/*', 'PUT', NULL, NULL, NULL, '1', '2018-04-20 07:22:55', '2018-04-20 07:24:40', '0'),
  ('5', '51', '日志查看', NULL, NULL, '/admin/log/**', 'GET', NULL, NULL, NULL, '1', '2017-11-20 14:07:25', '2018-02-04 14:28:53', '0'),
  ('5', '52', '日志删除', 'sys_log_del', NULL, '/admin/log/*', 'DELETE', NULL, NULL, NULL, '1', '2017-11-20 20:37:37', '2017-11-28 17:36:52', '0'),
  ('6', '61', '字典查看', NULL, NULL, '/admin/dict/**', 'GET', NULL, NULL, NULL, '1', '2017-11-19 22:04:24', '2017-11-29 11:31:24', '0'),
  ('6', '62', '字典删除', 'sys_dict_del', NULL, '/admin/dict/**', 'DELETE', NULL, NULL, NULL, '1', '2017-11-29 11:30:11', '2018-01-07 15:40:51', '0'),
  ('6', '63', '字典新增', 'sys_dict_add', NULL, '/admin/dict/**', 'POST', NULL, NULL, NULL, '1', '2018-05-11 22:34:55', NULL, '0'),
  ('6', '64', '字典修改', 'sys_dict_upd', NULL, '/admin/dict/**', 'PUT', NULL, NULL, NULL, '1', '2018-05-11 22:36:03', NULL, '0'),
  ('7', '71', '部门查看', '', NULL, '/admin/dept/**', 'GET', NULL, '', NULL, '1', '2018-01-20 13:17:19', '2018-01-20 14:55:24', '0'),
  ('7', '72', '部门新增', 'sys_dept_add', NULL, '/admin/dept/**', 'POST', NULL, NULL, NULL, '1', '2018-01-20 14:56:16', '2018-01-20 21:17:57', '0'),
  ('7', '73', '部门修改', 'sys_dept_edit', NULL, '/admin/dept/**', 'PUT', NULL, NULL, NULL, '1', '2018-01-20 14:56:59', '2018-01-20 21:17:59', '0'),
  ('7', '74', '部门删除', 'sys_dept_del', NULL, '/admin/dept/**', 'DELETE', NULL, NULL, NULL, '1', '2018-01-20 14:57:28', '2018-01-20 21:18:05', '0'),
  ('1', '100', '客户端管理', '', 'client', '', '', 'icon-bangzhushouji', 'views/admin/client/index', '9', '0', '2018-01-20 13:17:19', '2018-05-15 21:28:10', '0'),
  ('100', '101', '客户端新增', 'sys_client_add', NULL, '/admin/client/**', 'POST', '1', NULL, NULL, '1', '2018-05-15 21:35:18', '2018-05-16 10:35:26', '0'),
  ('100', '102', '客户端修改', 'sys_client_upd', NULL, '/admin/client/**', 'PUT', NULL, NULL, NULL, '1', '2018-05-15 21:37:06', '2018-05-15 21:52:30', '0'),
  ('100', '103', '客户端删除', 'sys_client_del', NULL, '/admin/client/**', 'DELETE', NULL, NULL, NULL, '1', '2018-05-15 21:39:16', '2018-05-15 21:52:34', '0'),
  ('100', '104', '客户端查看', NULL, NULL, '/admin/client/**', 'GET', NULL, NULL, NULL, '1', '2018-05-15 21:39:57', '2018-05-15 21:52:40', '0'),
  ('1', '110', '路由管理', NULL, 'route', NULL, NULL, 'icon-luyou', 'views/admin/route/index', '8', '0', '2018-05-15 21:44:51', '2018-05-16 06:58:20', '0'),
  ('110', '111', '路由查看', NULL, NULL, '/admin/route/**', 'GET', NULL, NULL, NULL, '1', '2018-05-15 21:45:59', '2018-05-16 07:23:04', '0'),
  ('110', '112', '路由新增', 'sys_route_add', NULL, '/admin/route/**', 'POST', NULL, NULL, NULL, '1', '2018-05-15 21:52:22', '2018-05-15 21:53:46', '0'),
  ('110', '113', '路由修改', 'sys_route_upd', NULL, '/admin/route/**', 'PUT', NULL, NULL, NULL, '1', '2018-05-15 21:55:38', NULL, '0'),
  ('110', '114', '路由删除', 'sys_route_del', NULL, '/admin/route/**', 'DELETE', NULL, NULL, NULL, '1', '2018-05-15 21:56:45', NULL, '0');

-- ----------------------------
--  Records of sys_oauth_client_details
-- ----------------------------
INSERT INTO sys_oauth_client_details VALUES ('app', NULL, 'app', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL,
                                                    'true'),
  ('pig', NULL, 'pig', 'server', 'password,refresh_token,authorization_code', NULL, NULL, NULL, NULL, NULL, 'false');


-- ----------------------------
--  Records of sys_role
-- ----------------------------
INSERT INTO sys_role VALUES ('1', 'admin', 'ROLE_ADMIN', '超级管理员', '2017-10-29 15:45:51', '2018-04-22 11:40:29',
                             '0'), ('14', 'demo', 'demo', 'demo用户', '2018-04-20 07:14:32', '2018-04-21 23:35:22', '0');

-- ----------------------------
--  Records of sys_role_dept
-- ----------------------------
INSERT INTO sys_role_dept VALUES ('11', '1', '1'), ('14', '14', '1');


-- ----------------------------
--  Records of sys_role_menu
-- ----------------------------
INSERT INTO sys_role_menu VALUES ('1',
                                  '1'), ('1', '2'), ('1', '3'), ('1', '4'), ('1', '5'), ('1', '6'), ('1', '7'), ('1', '8'), ('1', '9'), ('1', '10'),
  ('1', '11'), ('1', '12'), ('1', '13'), ('1', '14'), ('1', '15'), ('1', '21'), ('1', '22'), ('1', '23'), ('1', '24'), ('1', '31'), ('1', '32'),
  ('1', '33'), ('1', '34'), ('1', '41'), ('1', '42'), ('1', '43'), ('1', '44'), ('1', '45'), ('1', '51'), ('1', '52'), ('1', '61'), ('1', '62'),
  ('1', '63'), ('1', '64'), ('1', '71'), ('1', '72'), ('1', '73'), ('1', '74'), ('1', '100'), ('1', '101'), ('1', '102'), ('1', '103'), ('1', '104'),
  ('1', '110'), ('1', '111'), ('1', '112'), ('1', '113'), ('1', '114'), ('14', '1'), ('14', '2'), ('14', '3'), ('14', '4'), ('14', '5'), ('14', '6'),
  ('14', '7'), ('14', '8'), ('14', '9'), ('14', '10'), ('14', '11'), ('14', '12'), ('14', '13'), ('14', '14'), ('14', '15'), ('14', '21'),
  ('14', '31'), ('14', '41'), ('14', '51'), ('14', '61'), ('14', '71');

-- ----------------------------
--  Records of sys_user
-- ----------------------------
INSERT INTO sys_user VALUES
  ('1', 'admin', '$2a$10$vg5QNHhCknAqevx9vM2s5esllJEzF/pa8VZXtFYHhhOhUcCw/GWyS', NULL, '17034642111', NULL, '1', '2018-04-20 07:15:18',
   '2018-05-11 17:12:00',
   '0'), ('4', 'pig', '$2a$10$vg5QNHhCknAqevx9vM2s5esllJEzF/pa8VZXtFYHhhOhUcCw/GWyS', NULL, '17034642118', NULL, '1', '2018-04-22 11:39:07',
          '2018-05-10 18:01:11', '0');


-- ----------------------------
--  Records of sys_user_role
-- ----------------------------
INSERT INTO sys_user_role VALUES ('1', '1'), ('4', '14');


-- ----------------------------
--  Records of sys_zuul_route
-- ----------------------------
INSERT INTO sys_zuul_route VALUES ('1', 'test', 'test', 'tsest', '1', '1', '1', '0', '2018-05-16 07:28:43', '2018-05-16 07:35:08',
                                        '1'), ('2', 'sdfg', 'we', 'jjj', '1', '1', '1', 'jj', '2018-05-16 07:35:43', '2018-05-17 13:56:14', '1'),
  ('3', '/demo/**', 'pig-demo-service', '', '1', '1', '1', '', '2018-05-17 14:09:06', '2018-05-17 14:32:36', '0'),
  ('4', '/admin/**', 'pig-upms-service', '', '1', '1', '1', '', '2018-05-21 11:40:38', NULL, '0'),
  ('5', '/auth/**', 'pig-auth', '', '1', '1', '1', '', '2018-05-21 11:41:08', NULL, '0');

COMMIT;
SET FOREIGN_KEY_CHECKS = 1;