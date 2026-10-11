/*
 Navicat Premium Data Transfer

 Source Server         : 华为云项目
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : 113.46.193.206:3306
 Source Schema         : 2025_cskaoyan_words

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 16/07/2025 17:08:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(0) NOT NULL COMMENT '权限id',
  `permission_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限码，比如100,10001等',
  `parent_permission` bigint(0) NULL DEFAULT NULL COMMENT '父权限id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名称',
  `permission_alias` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限别名',
  `permission_type` int(0) NULL DEFAULT NULL COMMENT '权限类型: 1. 页面权限 2. 访问权限',
  `access_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径访问方式: GET POST等',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '访问路径(针对访问权限)',
  `desc` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `is_deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除表示: 0未删除 1已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_permssion_code`(`permission_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1856542445393313794, '100', -1, '物料管理', NULL, 1, NULL, NULL, '背单词app物料管理', '2024-11-13 11:39:46', '2024-11-13 11:40:04', 0);
INSERT INTO `permission` VALUES (1856543218059608065, '10001', 1856542445393313794, '书籍类目管理', NULL, 1, NULL, NULL, '背单词app英文书籍管理权限', '2024-11-13 11:42:50', '2024-11-13 11:42:50', 0);
INSERT INTO `permission` VALUES (1856543218084773889, '10002', 1856542445393313794, '书籍管理', NULL, 1, NULL, NULL, '背单词app英文书籍管理', '2024-11-13 11:42:50', '2025-04-25 09:33:51', 0);
INSERT INTO `permission` VALUES (1856544832979894274, '10003', 1856542445393313794, '章节管理', NULL, 1, NULL, NULL, 'app英文书籍的章节管理权限', '2024-11-13 11:49:15', '2024-11-13 11:49:15', 0);
INSERT INTO `permission` VALUES (1856546022606143489, '10004', 1856542445393313794, '词汇管理', NULL, 1, NULL, NULL, '背单词app所有词汇管理权限', '2024-11-13 11:53:59', '2024-11-13 11:53:59', 0);
INSERT INTO `permission` VALUES (1856546877954752513, '200', -1, '账号权限管理', NULL, 1, NULL, NULL, '背单词app后台账号权限管理', '2024-11-13 11:57:23', '2024-11-13 12:13:18', 0);
INSERT INTO `permission` VALUES (1856615921783689218, '20001', 1856546877954752513, '员工管理', NULL, 1, NULL, NULL, 'app后台员工管理', '2024-11-13 16:31:44', '2024-11-13 16:31:44', 0);
INSERT INTO `permission` VALUES (1856627063285370882, '20002', 1856546877954752513, '角色管理', NULL, 1, NULL, NULL, 'app后台角色管理', '2024-11-13 17:16:01', '2024-11-13 17:16:01', 0);
INSERT INTO `permission` VALUES (1856634008433782785, '20003', 1856546877954752513, '权限管理', NULL, 1, NULL, NULL, 'app后台权限管理', '2024-11-13 17:43:36', '2024-11-13 17:48:04', 0);
INSERT INTO `permission` VALUES (1856635817952997378, '10001001', 1856543218059608065, '添加', 'admin:category:add', 2, 'POST', '/admin/voc/category', '添加书籍类目', '2024-11-13 17:50:48', '2024-11-22 11:00:38', 0);
INSERT INTO `permission` VALUES (1856654232092594177, '10001002', 1856543218059608065, '类目修改', 'admin:category:update', 2, 'PUT', '/admin/voc/category', '修改书籍', '2024-11-13 19:03:58', '2024-11-22 11:01:13', 0);
INSERT INTO `permission` VALUES (1856655040444026881, '10001003', 1856543218059608065, '单个删除', 'admin:category:delete', 2, 'DELETE', '/admin/voc/category/{categoryId}', '删除单个书籍类目', '2024-11-13 19:07:11', '2024-11-22 11:04:10', 0);
INSERT INTO `permission` VALUES (1856684531581112321, '10001004', 1856543218059608065, '批量删除', 'admin:category:batchdelete', 2, 'DELETE', '/admin/voc/categories', '批量删除书籍目录', '2024-11-13 21:04:22', '2024-11-22 11:06:11', 0);
INSERT INTO `permission` VALUES (1856692035761393666, '10002001', 1856543218084773889, '添加书籍', 'admin:book:add', 2, 'POST', '/admin/voc/book', '添加书籍', '2024-11-13 21:34:11', '2024-11-22 11:04:43', 0);
INSERT INTO `permission` VALUES (1856692773354917889, '10002002', 1856543218084773889, '修改书籍', 'admin:book:update', 2, 'PUT', '/admin/voc/book', '修改书籍信息', '2024-11-13 21:37:07', '2024-11-22 11:05:04', 0);
INSERT INTO `permission` VALUES (1856693553105391618, '10002003', 1856543218084773889, '单个删除', 'admin:book:delete', 2, 'DELETE', '/admin/voc/book/{bookId}', '删除单本书籍', '2024-11-13 21:40:13', '2024-11-22 11:05:44', 0);
INSERT INTO `permission` VALUES (1856695341959270401, '10002004', 1856543218084773889, '批量删除', 'admin:book:batchdelete', 2, 'DELETE', '/admin/voc/books', '批量删除书籍', '2024-11-13 21:47:19', '2024-11-22 11:09:01', 0);
INSERT INTO `permission` VALUES (1856699495062622210, '10003001', 1856544832979894274, '添加章节', 'admin:chapter:add', 2, 'POST', '/admin/voc/chapter', '添加书籍章节', '2024-11-13 22:03:50', '2024-11-22 11:09:50', 0);
INSERT INTO `permission` VALUES (1856700160560254978, '10003002', 1856544832979894274, '修改章节', 'admin:chapter:update', 2, 'PUT', '/admin/voc/chapter', '修改章节', '2024-11-13 22:06:28', '2024-11-22 11:10:03', 0);
INSERT INTO `permission` VALUES (1856700734148104193, '10003003', 1856544832979894274, '单个删除', 'admin:chapter:delete', 2, 'DELETE', '/admin/voc/chapter/{chapterId}', '删除单个章节', '2024-11-13 22:08:45', '2024-11-22 11:10:16', 0);
INSERT INTO `permission` VALUES (1856701496790011905, '10003004', 1856544832979894274, '批量删除', 'admin:chapter:batchdelete', 2, 'DELETE', '/admin/voc/categories', '批量删除章节', '2024-11-13 22:11:47', '2024-11-22 11:12:06', 0);
INSERT INTO `permission` VALUES (1856703322834784258, '10004001', 1856546022606143489, '添加词汇', 'admin:voc:add', 2, 'POST', '/admin/voc', '添加英文', '2024-11-13 22:19:02', '2024-11-22 11:12:27', 0);
INSERT INTO `permission` VALUES (1856704526608420866, '10004002', 1856546022606143489, '修改词汇', 'admin:voc:update', 2, 'PUT', '/admin/voc', '修改词汇', '2024-11-13 22:23:49', '2024-11-22 11:16:13', 0);
INSERT INTO `permission` VALUES (1856704796558020609, '10004003', 1856546022606143489, '单个删除', 'admin:voc:delete', 2, 'DELETE', '/admin/voc/{vocId}', '删除单个单词', '2024-11-13 22:24:54', '2024-11-22 11:16:25', 0);
INSERT INTO `permission` VALUES (1856705938906075138, '10004004', 1856546022606143489, '批量删除', 'admin:voc:batchdelete', 2, 'DELETE', '/admin/voc', '批量删除词汇', '2024-11-13 22:29:26', '2024-11-22 11:16:36', 0);
INSERT INTO `permission` VALUES (1856706416666660865, '20003001', 1856634008433782785, '添加权限', 'admin:permission:add', 2, 'POST', '/admin/permission', '添加后台管理权限', '2024-11-13 22:31:20', '2024-11-22 11:18:13', 0);
INSERT INTO `permission` VALUES (1856706898332143617, '20003002', 1856634008433782785, '修改权限', 'admin:permission:update', 2, 'PUT', '/admin/permission', '修改权限', '2024-11-13 22:33:15', '2024-11-22 11:19:13', 0);
INSERT INTO `permission` VALUES (1856707594011987970, '20003003', 1856634008433782785, '删除权限', 'admin:permission:delete', 2, 'DELETE', '/admin/permission/{permissionId}', '删除某个权限及其子权限', '2024-11-13 22:36:01', '2024-11-22 11:19:54', 0);
INSERT INTO `permission` VALUES (1856708295974899713, '20003004', 1856634008433782785, '查看权限', 'admin:permission:search', 2, 'GET', '/admin/permission/list', '查看权限树', '2024-11-13 22:38:48', '2024-11-22 11:20:04', 0);
INSERT INTO `permission` VALUES (1856709069714935810, '10004005', 1856546022606143489, 'excel添加', 'admin:voc:excel:upload', 2, 'POST', '/admin/voc/excel', 'excel批量上传单词', '2024-11-13 22:41:52', '2024-11-22 11:17:12', 0);
INSERT INTO `permission` VALUES (1858685582668636161, '10001005', 1856543218059608065, '分页查询', 'admin:category:search', 2, 'GET', '/admin/voc/category/list', '根据关键字分页查看', '2024-11-19 09:35:50', '2024-11-22 11:04:25', 0);
INSERT INTO `permission` VALUES (1858687606885883906, '10002005', 1856543218084773889, '分页查询', 'admin:book:search', 2, 'GET', '/admin/voc/book/list', '分页查询所有书籍', '2024-11-19 09:43:52', '2024-11-22 11:08:52', 0);
INSERT INTO `permission` VALUES (1858691382719979522, '10003005', 1856544832979894274, '分页查询章节', 'admin:chapter:search', 2, 'GET', '/admin/voc/chapter/list', '分页查询所有章节', '2024-11-19 09:58:53', '2024-11-22 11:47:30', 0);
INSERT INTO `permission` VALUES (1858692953897869313, '10004006', 1856546022606143489, '分页查询', 'admin:voc:search', 2, 'GET', '/admin/voc/list', '分页查询所有单词', '2024-11-19 10:05:07', '2024-11-22 11:17:25', 0);
INSERT INTO `permission` VALUES (1859067850411167746, '20002001', 1856627063285370882, '添加角色', 'admin:role:add', 2, 'POST', '/admin./role', '添加新的角色', '2024-11-20 10:54:50', '2024-11-22 11:17:41', 0);
INSERT INTO `permission` VALUES (1859068878334414849, '20002002', 1856627063285370882, '修改角色', 'admin:role:update', 2, 'PUT', '/admin/role', '修改角色内容', '2024-11-20 10:58:55', '2024-11-20 10:59:14', 0);
INSERT INTO `permission` VALUES (1859069211689308162, '20002003', 1856627063285370882, '单个删除', 'admin:role:delete', 2, 'DELETE', '/admin/role/{roleId}', '删除单个角色', '2024-11-20 11:00:14', '2024-11-20 11:05:05', 0);
INSERT INTO `permission` VALUES (1859070849187553282, '20002006', 1856627063285370882, '角色授权', 'admin:role:authorize', 2, '', '', '给角色关联权限', '2024-11-20 11:06:44', '2024-11-20 11:08:06', 1);
INSERT INTO `permission` VALUES (1859071474767994882, '20002004', 1856627063285370882, '分页查询', 'admin:role:search', 2, 'GET', '/admin/role/list', '分页查询角色', '2024-11-20 11:09:14', '2024-11-20 11:09:43', 0);
INSERT INTO `permission` VALUES (1859072365378117634, '20002005', 1856627063285370882, '角色授权', 'admin:role:authorize', 2, 'PUT', '/admin/role/permission', '给角色授权', '2024-11-20 11:12:46', '2024-11-20 11:13:46', 0);
INSERT INTO `permission` VALUES (1859072991155691522, '20001001', 1856615921783689218, '添加员工', 'admin:employee:add', 2, 'GET', '/admin/employee/list', '添加员工', '2024-11-20 11:15:15', '2024-11-20 11:15:32', 0);
INSERT INTO `permission` VALUES (1859073318269460482, '20001002', 1856615921783689218, '修改员工', 'admin:employee:update', 2, 'PUT', '/admin/employee', '修改员工信息', '2024-11-20 11:16:33', '2024-11-20 11:16:56', 0);
INSERT INTO `permission` VALUES (1859073554882732034, '20001003', 1856615921783689218, '删除员工', 'admin:employee:delete', 2, 'DELETE', '/admin/employee/{employeeId}', '删除员工', '2024-11-20 11:17:30', '2024-11-20 11:17:56', 0);
INSERT INTO `permission` VALUES (1859073846466551810, '20001004', 1856615921783689218, '查询员工', 'admin:employee:search', 2, 'GET', '/admin/employee/list', '查询员工', '2024-11-20 11:18:39', '2024-11-20 11:21:30', 0);
INSERT INTO `permission` VALUES (1859075853092888577, '20003005', 1856634008433782785, '添加子权限', 'admin:permission:add:sub', 2, 'POST', '/admin/permission', '给已权限添加子权限', '2024-11-20 11:26:38', '2024-11-20 11:27:04', 0);
INSERT INTO `permission` VALUES (1861605941734744065, '10005', 1856542445393313794, '题目管理', 'admin:topic', 1, '', '', '背单词所有题目管理', '2024-11-27 11:00:18', '2025-07-06 17:47:21', 1);
INSERT INTO `permission` VALUES (1861606236392988674, '10005001', 1861605941734744065, '添加题目', 'admin:topic:add', 2, 'POST', '/admin/topic', '添加背单词题目', '2024-11-27 11:01:28', '2025-07-06 17:47:21', 1);
INSERT INTO `permission` VALUES (1861606912976166913, '10005002', 1861605941734744065, '修改题目', 'admin:topic:update', 2, 'PUT', '/admin/topic', '修改背单词题目', '2024-11-27 11:04:09', '2025-07-06 17:47:21', 1);
INSERT INTO `permission` VALUES (1861607314958262274, '10005003', 1861605941734744065, '删除题目', 'admin:topic:delete', 2, 'DELETE', '/admin/topic/{topicId}?topicType=?', '删除背单词题目', '2024-11-27 11:05:45', '2025-07-06 17:47:21', 1);
INSERT INTO `permission` VALUES (1861607781557805057, '10005004', 1861605941734744065, '查询题目', 'admin:topic:search', 2, 'GET', '/admin/topic/list', '查询背单词题目', '2024-11-27 11:07:36', '2025-07-06 17:47:21', 1);
INSERT INTO `permission` VALUES (1914236010600480769, '300', -1, '商品管理权限', '', 1, '', '', '管理移动端用户商品', '2025-04-21 16:33:24', '2025-04-21 16:33:24', 0);
INSERT INTO `permission` VALUES (1914236857455624194, '30001', 1914236010600480769, '付费商品', '', 1, '', '', '移动端用户付费商品信息管理', '2025-04-21 16:36:46', '2025-04-21 16:36:46', 0);
INSERT INTO `permission` VALUES (1914237097063628802, '30001001', 1914236857455624194, '添加商品', 'admin:goods:add', 2, 'POST', '/admin/goods', '添加付费商品', '2025-04-21 16:37:43', '2025-04-21 16:40:51', 0);
INSERT INTO `permission` VALUES (1914237363129303041, '30001002', 1914236857455624194, '修改商品', 'admin:goods:update', 2, 'PUT', '/admin/goods', '修改付费商品信息', '2025-04-21 16:38:46', '2025-04-21 16:41:14', 0);
INSERT INTO `permission` VALUES (1914238393875316737, '30001003', 1914236857455624194, '单个删除', 'admin:goods:delete', 2, 'DELETE', '/admin/goods/id', '删除单个商品', '2025-04-21 16:42:52', '2025-04-29 08:46:06', 0);
INSERT INTO `permission` VALUES (1914238844209348610, '30001004', 1914236857455624194, '批量删除', 'admin:goods:batchdelete', 2, 'DELETE', '/admin/goods/selected', '批量删除商品', '2025-04-21 16:44:39', '2025-04-21 16:45:41', 0);
INSERT INTO `permission` VALUES (1914239692083707906, '30001005', 1914236857455624194, '分页查询', 'admin:goods:search', 2, 'GET', '/admin/goods/list', '分页查询付费商品', '2025-04-21 16:48:02', '2025-04-21 17:38:51', 0);
INSERT INTO `permission` VALUES (1914244125123293186, '400', -1, '等级权限管理', '', 1, '', '', '管理等级以及等级特权信息', '2025-04-21 17:05:39', '2025-04-21 17:05:39', 0);
INSERT INTO `permission` VALUES (1914244296255090689, '40001', 1914244125123293186, '等级管理', '', 1, '', '', '管理用户等级信息', '2025-04-21 17:06:19', '2025-04-21 17:06:19', 0);
INSERT INTO `permission` VALUES (1914244579014094849, '40001001', 1914244296255090689, '添加等级', 'admin:level:add', 2, 'POST', '/admin/level', '添加用户等级', '2025-04-21 17:07:27', '2025-04-21 17:08:52', 0);
INSERT INTO `permission` VALUES (1914246638853566465, '40001002', 1914244296255090689, '修改等级', 'admin:level:update', 2, 'PUT', '/admin/level', '修改用户等级信息', '2025-04-21 17:15:38', '2025-04-21 17:15:51', 0);
INSERT INTO `permission` VALUES (1914247095680380930, '40001003', 1914244296255090689, '删除等级', 'admin:leveldelete', 2, 'DELETE', '/admin/level/id', '删除用户等级', '2025-04-21 17:17:27', '2025-04-21 17:17:46', 0);
INSERT INTO `permission` VALUES (1914248076174438402, '40001004', 1914244296255090689, '批量删除', 'admin:level:batchdelete', 2, '', '', '批量删除用户等级', '2025-04-21 17:21:21', '2025-04-21 17:21:21', 0);
INSERT INTO `permission` VALUES (1914248595282472961, '40001005', 1914244296255090689, '分页查询', 'admin:level:search', 2, 'GET', '/admin/level/list', '查询等级信息', '2025-04-21 17:23:24', '2025-04-21 17:23:56', 0);
INSERT INTO `permission` VALUES (1914249325032648706, '40002', 1914244125123293186, '等级特权管理', '', 1, '', '', '等级对应的用户等级特权管理', '2025-04-21 17:26:18', '2025-04-21 17:26:18', 0);
INSERT INTO `permission` VALUES (1914249772342587393, '40002001', 1914249325032648706, '添加等级特权', 'admin:privilege:add', 2, 'POST', '/admin/privilege/add', '添加用户对应的等级特权', '2025-04-21 17:28:05', '2025-04-21 17:28:23', 0);
INSERT INTO `permission` VALUES (1914250499999805441, '40002002', 1914249325032648706, '修改等级特权', 'admin:privilege:update', 2, 'UPDATE', '/admin/privilege', '修改等级对应的等级特权', '2025-04-21 17:30:58', '2025-04-21 17:31:17', 0);
INSERT INTO `permission` VALUES (1914250972559454209, '40002003', 1914249325032648706, '单个删除', 'admin:privilege:delete', 2, 'DELETE', '/admin/privilege/id', '删除单个等级特权', '2025-04-21 17:32:51', '2025-04-21 17:33:20', 0);
INSERT INTO `permission` VALUES (1914251594994167809, '40002004', 1914249325032648706, '批量删除', 'admin:privilege:batchdelete', 2, 'DELETE', '/admin/privilege', '批量删除等级特权', '2025-04-21 17:35:19', '2025-04-21 17:36:20', 0);
INSERT INTO `permission` VALUES (1914251769754038274, '40002005', 1914249325032648706, '分页查询', 'admin:privilege:search', 2, 'GET', '/admin/privilege/list', '分页查询等级特权', '2025-04-21 17:36:01', '2025-04-21 17:37:25', 0);
INSERT INTO `permission` VALUES (1914252647139520514, '500', 1914249325032648706, '系统管理', '', 1, '', '', '系统管理', '2025-04-21 17:39:30', '2025-04-21 17:39:49', 1);
INSERT INTO `permission` VALUES (1914252935636332545, '500', 1914249325032648706, '系统管理', '', 1, '', '', '系统信息管理', '2025-04-21 17:40:39', '2025-04-21 17:40:47', 1);
INSERT INTO `permission` VALUES (1914253065160634369, '500', 1914249325032648706, '系统管理', '', 1, '', '', '系统信息管理', '2025-04-21 17:41:10', '2025-04-21 17:41:14', 1);
INSERT INTO `permission` VALUES (1914253723255320577, '500', 1914249325032648706, '系统管理', '', 1, '', '', '系统信息管理', '2025-04-21 17:43:47', '2025-04-21 17:43:56', 1);
INSERT INTO `permission` VALUES (1914253834190467073, '500', -1, '系统管理', '', 1, '', '', '系统信息管理', '2025-04-21 17:44:13', '2025-04-21 17:44:44', 0);
INSERT INTO `permission` VALUES (1914254545473122305, '50001', 1914253834190467073, '操作日志管理', '', 1, '', '', '查看系统操作日志', '2025-04-21 17:47:03', '2025-04-21 17:47:03', 0);
INSERT INTO `permission` VALUES (1914254804618194945, '50001001', 1914254545473122305, '分页查询', 'admin:log:search', 2, 'GET', '/admin/log/operation/list', '分页查询系统操作日志', '2025-04-21 17:48:05', '2025-04-21 17:48:23', 0);
INSERT INTO `permission` VALUES (1915371722041401346, '40003', 1914244125123293186, 'AI阅读', '', 1, '', '', 'AI阅读信息管理', '2025-04-24 19:46:19', '2025-04-24 19:46:19', 0);
INSERT INTO `permission` VALUES (1915375645133316097, '40003001', 1915371722041401346, '添加模式', 'admin:ai-read:situation:add', 2, 'POST', '/admin/voc/ai-read/situation', '添加AI阅读模式', '2025-04-24 20:01:54', '2025-04-24 20:01:54', 0);
INSERT INTO `permission` VALUES (1915376437286346753, '40003002', 1915371722041401346, '修改模式', 'admin:ai-read:situation:update', 2, 'PUT', '/admin/voc/ai-read/situation', '修改AI阅读模式', '2025-04-24 20:05:03', '2025-04-24 20:05:48', 0);
INSERT INTO `permission` VALUES (1915377109629083649, '40003003', 1915371722041401346, '单个删除模式', 'admin:ai-read:situation:delete', 2, 'DELETE', '/admin/voc/ai-read/situation/id', '单个删除AI阅读模式', '2025-04-24 20:07:43', '2025-04-24 20:07:43', 0);
INSERT INTO `permission` VALUES (1915378221199335425, '40003004', 1915371722041401346, '批量删除模式', 'admin:ai-read:situation:batchdelete', 2, 'DELETE', '/admin/voc/ai-read/situation', '批量删除AI阅读模式', '2025-04-24 20:12:08', '2025-04-24 20:12:08', 0);
INSERT INTO `permission` VALUES (1915379045120024578, '40003005', 1915371722041401346, '分页查询模式', 'admin:ai-read:situation:search', 2, 'GET', '/admin/voc/ai-read/list', '查询AI阅读模式', '2025-04-24 20:15:25', '2025-04-24 20:15:25', 0);
INSERT INTO `permission` VALUES (1927030813854343169, '1', 1856634008433782785, '111', 'sdfsdfdsfsdfs', 2, '', '', '1111', '2025-05-26 23:55:23', '2025-07-06 18:02:45', 1);
INSERT INTO `permission` VALUES (1927031083501953026, '311', 1856634008433782785, 'dsasd', '', 1, '', '', '打发打发', '2025-05-26 23:56:27', '2025-07-06 18:02:47', 1);
INSERT INTO `permission` VALUES (1927166919753093121, '111111', 1856546877954752513, 'qweqw', 'dfsf', 2, '3213123', '121', '11', '2025-05-27 08:56:13', '2025-05-27 14:48:41', 1);
INSERT INTO `permission` VALUES (1927650109815042050, '', -1, '', '', 1, '', '', '', '2025-05-28 16:56:14', '2025-07-01 16:37:06', 1);
INSERT INTO `permission` VALUES (1927696697656631298, '600', -1, '测试权限', '', 1, '', '', '测试删除', '2025-05-28 20:01:22', '2025-07-06 17:47:01', 1);
INSERT INTO `permission` VALUES (1927696752157417473, '60001', 1927696697656631298, '子节点', '', 1, '', '', '', '2025-05-28 20:01:35', '2025-07-06 17:47:01', 1);
INSERT INTO `permission` VALUES (1927696852892016641, '60002', 1927696752157417473, '子节点2', '', 1, '', '', '', '2025-05-28 20:01:59', '2025-07-06 17:47:01', 1);
INSERT INTO `permission` VALUES (1927934998527397889, '999', -1, '测试添加', '', 1, '', '', '测试用到', '2025-05-29 11:48:17', '2025-07-06 17:47:05', 1);
INSERT INTO `permission` VALUES (1927935401587429377, '900', 1927934998527397889, '添加一级子节点', '', 1, '', '', '测试添加一级子节点', '2025-05-29 11:49:53', '2025-07-06 17:47:05', 1);
INSERT INTO `permission` VALUES (1927991187822886913, '001', -1, 'test', '', 1, '', '', '测试用', '2025-05-29 15:31:34', '2025-07-06 17:47:07', 1);
INSERT INTO `permission` VALUES (1927994053753061377, '002', 1927991187822886913, '子节点1', '', 1, '', '', 'test', '2025-05-29 15:42:57', '2025-07-06 17:47:07', 1);
INSERT INTO `permission` VALUES (1927994637084278786, '01', 1927994053753061377, '子节点2', '111', 2, 'Get', 'abc', '测试', '2025-05-29 15:45:16', '2025-05-29 15:51:13', 1);
INSERT INTO `permission` VALUES (1927996239790751745, '111', 1927991187822886913, '子节点2', '', 1, '', '', '测试', '2025-05-29 15:51:38', '2025-07-06 17:47:07', 1);
INSERT INTO `permission` VALUES (1928070301825212418, '001', 1927994053753061377, 'test', '', 1, '', '', '测试', '2025-05-29 20:45:56', '2025-05-29 21:00:44', 1);
INSERT INTO `permission` VALUES (1928073509305950209, '', 1927994053753061377, 'test01', '', 1, '', '', '', '2025-05-29 20:58:41', '2025-05-29 21:00:32', 1);
INSERT INTO `permission` VALUES (1928073616155844610, '', -1, 'test01', '', 1, '', '', '', '2025-05-29 20:59:06', '2025-05-29 20:59:41', 1);
INSERT INTO `permission` VALUES (1928073658220519425, '', 1928073616155844610, '01', '', 1, '', '', '', '2025-05-29 20:59:16', '2025-05-29 20:59:41', 1);
INSERT INTO `permission` VALUES (1928073706853474306, '', 1928073658220519425, '02', '', 1, '', '', '', '2025-05-29 20:59:28', '2025-05-29 20:59:41', 1);
INSERT INTO `permission` VALUES (1928440500449452034, '2223', -1, '测试权限222', '别名2', 2, '', '', '测试用权限', '2025-05-30 21:16:58', '2025-07-06 17:47:09', 1);
INSERT INTO `permission` VALUES (1928446221740154882, '1998', 1928440500449452034, '测试子节点222', '', 1, '', '', '测试用子节点权限', '2025-05-30 21:39:42', '2025-07-06 17:47:09', 1);
INSERT INTO `permission` VALUES (1928447643517054978, '4533', 1928440500449452034, '测试子权限2', '', 1, '', '', '测试子权限描述2', '2025-05-30 21:45:21', '2025-06-01 00:56:48', 1);
INSERT INTO `permission` VALUES (1945061693084491777, '5656565656', -1, '测试权限', '', 1, '', '', '测试权限', '2025-07-15 18:03:39', '2025-07-15 18:03:39', 0);
INSERT INTO `permission` VALUES (1945072168958517250, '30001006', 1914236857455624194, '上下架', 'admin:goods:status', 2, 'PUT', '/goods/status/{goodsId}', '商品上下架', '2025-07-15 18:45:17', '2025-07-15 18:45:17', 0);

SET FOREIGN_KEY_CHECKS = 1;
