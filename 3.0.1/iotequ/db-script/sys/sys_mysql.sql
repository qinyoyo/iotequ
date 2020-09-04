-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL PRIMARY KEY COMMENT 'uuid主键',
  `icon` text NULL COMMENT '头像',
  `name` varchar(32) NOT NULL UNIQUE COMMENT '用户名',
  `real_name` varchar(32) NOT NULL COMMENT '真实名',
  `sex` varchar(1) DEFAULT '1' NULL COMMENT '性别',
  `birth_date` date NULL COMMENT '生日',
  `reg_time` datetime NULL COMMENT '注册时间',
  `mobile_phone` varchar(32) NULL UNIQUE COMMENT '手机号码',
  `email` varchar(50) NULL UNIQUE COMMENT '邮箱',
  `wechat_openid` varchar(50) NULL UNIQUE COMMENT '微信',
  `org_code` int(11) NULL COMMENT '部门',
  `employee_no` varchar(32) NULL UNIQUE COMMENT '工号',
  `org_privilege` int(11) NULL COMMENT '数据权限部门',
  `role_list` varchar(200) NULL COMMENT '角色序列',
  `locked` tinyint(1) DEFAULT 0 NOT NULL COMMENT '被锁定',
  `state` tinyint(1) DEFAULT 1 NOT NULL COMMENT '激活',
  `id_type` int(11) DEFAULT 1 NOT NULL COMMENT '证件类型',
  `id_number` varchar(45) NOT NULL COMMENT '证件号码',
  `expired_time` date NULL COMMENT '账号过期时间',
  `password_expired_time` date NULL COMMENT '密码过期时间',
  `password` varchar(32) DEFAULT '96e79218965eb72c92a549dd5a330112' NOT NULL COMMENT '密码',
  `password_error_times` int(11) DEFAULT 0 NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;
CREATE INDEX `index_sys_user_org_code` ON `sys_user`(`org_code`);

ALTER TABLE `sys_user` ADD CONSTRAINT  `ui_sys_user01` UNIQUE (`id_type`,`id_number`);
-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(32) NOT NULL PRIMARY KEY COMMENT 'uuid主键',
  `name` varchar(32) NOT NULL UNIQUE COMMENT '用户名',
  `real_name` varchar(32) NOT NULL COMMENT '真实名',
  `birth_date` date NULL COMMENT '生日',
  `org_code` int(11) NULL COMMENT '部门',
  `employee_no` varchar(32) NULL UNIQUE COMMENT '工号',
  `mobile_phone` varchar(32) NULL UNIQUE COMMENT '手机号码',
  `sex` varchar(1) DEFAULT '1' NULL COMMENT '性别',
  `locked` tinyint(1) DEFAULT 0 NOT NULL COMMENT '被锁定',
  `state` tinyint(1) DEFAULT 1 NOT NULL COMMENT '激活',
  `id_type` int(11) DEFAULT 1 NOT NULL COMMENT '证件类型',
  `id_number` varchar(45) NOT NULL COMMENT '证件号码',
  `email` varchar(50) NULL UNIQUE COMMENT '邮箱',
  `reg_time` datetime NULL COMMENT '注册时间',
  `role_list` varchar(200) NULL COMMENT '角色序列',
  `org_privilege` int(11) NULL COMMENT '数据权限部门',
  `expired_time` date NULL COMMENT '账号过期时间',
  `password_expired_time` date NULL COMMENT '密码过期时间',
  `icon` text NULL COMMENT '头像',
  `password` varchar(32) DEFAULT '96e79218965eb72c92a549dd5a330112' NOT NULL COMMENT '密码',
  `wechat_openid` varchar(50) NULL UNIQUE COMMENT '微信openId',
  `password_error_times` int(11) DEFAULT 0 NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;
CREATE INDEX `index_sys_user_org_code` ON `sys_user`(`org_code`);

ALTER TABLE `sys_user` ADD CONSTRAINT  `ui_sys_user01` UNIQUE (`id_type`,`id_number`);
-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `name` varchar(45) NOT NULL COMMENT '任务名',
  `description` varchar(300) NULL COMMENT '详细说明',
  `scedule_years` varchar(100) DEFAULT '*' NOT NULL COMMENT '调度年',
  `schedule_months` varchar(100) DEFAULT '*' NOT NULL COMMENT '调度月',
  `schedule_days` varchar(100) DEFAULT '*' NOT NULL COMMENT '调度日',
  `schedule_weeks` varchar(100) DEFAULT '*' NOT NULL COMMENT '调度星期',
  `schedule_hours` varchar(100) DEFAULT '*' NOT NULL COMMENT '调度时',
  `schedule_minutes` varchar(100) DEFAULT '*' NOT NULL COMMENT '调度分',
  `class_name` varchar(100) NOT NULL COMMENT '类名',
  `mothod_name` varchar(500) NOT NULL COMMENT '方法',
  `is_static` tinyint(1) DEFAULT 1 NOT NULL COMMENT '静态方法',
  `parames` varchar(100) NULL COMMENT '参数',
  `is_running` tinyint(1) DEFAULT 1 NOT NULL COMMENT '运行中'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '调度任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `code` varchar(8) NOT NULL UNIQUE COMMENT '代码',
  `name` varchar(45) NOT NULL UNIQUE COMMENT '名称',
  `note` varchar(64) NULL COMMENT '说明'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

