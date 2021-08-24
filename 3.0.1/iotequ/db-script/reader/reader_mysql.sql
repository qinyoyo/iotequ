-- ----------------------------
-- Table structure for dev_reader
-- ----------------------------
DROP TABLE IF EXISTS `dev_reader`;
CREATE TABLE `dev_reader` (
  `id` char(32) NOT NULL PRIMARY KEY COMMENT '主键',
  `reader_no` varchar(20) NOT NULL UNIQUE COMMENT '设备编号',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `type` varchar(30) DEFAULT 'D10' NOT NULL COMMENT '型号',
  `reader_group` int(11) NOT NULL COMMENT '设备组',
  `address` varchar(100) NULL COMMENT '地点',
  `capacity` int(10) NULL COMMENT '容量',
  `connect_type` varchar(11) NOT NULL COMMENT '连接类型',
  `ip` varchar(20) NOT NULL COMMENT 'IP地址',
  `sn_no` varchar(36) NULL COMMENT '设备序列号',
  `dev_mode` varchar(32) NOT NULL COMMENT '设备模式',
  `firmware` varchar(40) NULL COMMENT '固件版本',
  `is_online` tinyint(1) DEFAULT '0' NOT NULL COMMENT '在线',
  `is_time_sync` tinyint(1) DEFAULT '0' NOT NULL COMMENT '同步',
  `align_method` tinyint(3) DEFAULT 4 NOT NULL COMMENT '验证方式',
  `blacklight_time` tinyint(3) DEFAULT 0 NOT NULL COMMENT '背光时间',
  `voiceprompt` tinyint(1) DEFAULT '1' NOT NULL COMMENT '语言提示',
  `menu_time` tinyint(3) DEFAULT 0 NOT NULL COMMENT '菜单时间',
  `wengenform` tinyint(3) DEFAULT 2 NOT NULL COMMENT '韦根格式',
  `wengen_output` tinyint(3) DEFAULT 1 NOT NULL COMMENT '韦根输出',
  `wengen_out_area` tinyint(3) DEFAULT 26 NOT NULL COMMENT '韦根输出区位码',
  `regfinger_out_time` tinyint(3) DEFAULT 49 NOT NULL COMMENT '指静脉注册超时时长',
  `authfinger_out_time` tinyint(3) DEFAULT 49 NOT NULL COMMENT '验证超时时长',
  `wg_order` tinyint(2) DEFAULT 0 NULL COMMENT '高低位',
  `relay_time` tinyint(11) DEFAULT 5 NULL COMMENT '继电器开启时间',
  `fixed_value` varchar(36) NULL COMMENT '固定码',
  `alarm_enable` tinyint(2) DEFAULT 1 NULL COMMENT '防拆告警使能',
  `open_enable` tinyint(2) DEFAULT 0 NULL COMMENT '开门按钮使能',
  `door_state` tinyint(2) DEFAULT 1 NULL COMMENT '开门状态检测使能',
  `relay_enable` tinyint(2) DEFAULT 1 NULL COMMENT '继电器使能',
  `doorbell_enable` tinyint(2) DEFAULT 1 NULL COMMENT '门铃输出使能',
  `wifi_ssid` varchar(36) NULL COMMENT 'wifi名称',
  `wifi_psw` varchar(36) NULL COMMENT 'wifi密码|Wifi password'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '终端设备表|Device' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_auth_config
-- ----------------------------
DROP TABLE IF EXISTS `dev_auth_config`;
CREATE TABLE `dev_auth_config` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
  `role_id` int(11) NOT NULL COMMENT '配置id',
  `begin_at` date NULL COMMENT '开始日期',
  `end_at` date NULL COMMENT '结束日期',
  `start_time` time(6) NULL COMMENT '开始时间',
  `end_time` time(6) NULL COMMENT '结束时间',
  `only_work_day` tinyint(1) DEFAULT '0' NOT NULL COMMENT '仅工作日有效',
  `auth` int(11) DEFAULT 4 NOT NULL COMMENT '权限'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限配置|Authorization config' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_reader_people
-- ----------------------------
DROP TABLE IF EXISTS `dev_reader_people`;
CREATE TABLE `dev_reader_people` (
  `type` int(36) DEFAULT 0 NOT NULL COMMENT '操作类型',
  `order_num` int(11) NULL COMMENT '排序',
  `user_no` varchar(36) NOT NULL COMMENT '用户编号',
  `status` int(2) DEFAULT 1 NULL COMMENT '状态',
  `reader_no` varchar(20) NULL COMMENT '设备编号1234',
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备人员信息|Device people infomation' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_reader_group
-- ----------------------------
DROP TABLE IF EXISTS `dev_reader_group`;
CREATE TABLE `dev_reader_group` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
  `name` varchar(20) NOT NULL UNIQUE COMMENT '组名称',
  `parent` int(11) NULL COMMENT '父节点ID',
  `org_code` int(11) NOT NULL COMMENT '归属部门',
  `org_auth` varchar(256) NULL COMMENT '部门通行权限',
  `sub_org_auth` varchar(256) NULL COMMENT '子部门通行权限',
  `auth_group_list` varchar(256) NULL COMMENT '人员分组授权列表'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备分组|Device group' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_people_mapping
-- ----------------------------
DROP TABLE IF EXISTS `dev_people_mapping`;
CREATE TABLE `dev_people_mapping` (
  `id` int(10) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'id',
  `reader_no` varchar(20) NOT NULL COMMENT '设备编号',
  `user_no` varchar(36) NOT NULL COMMENT '用户',
  `status` tinyint(1) NOT NULL COMMENT '状态'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '下发用户关系|Map of download users' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_people_group
-- ----------------------------
DROP TABLE IF EXISTS `dev_people_group`;
CREATE TABLE `dev_people_group` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'id',
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `user_no` varchar(45) NOT NULL COMMENT '姓名'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组人员|Pelple group' ROW_FORMAT = Dynamic;

ALTER TABLE `dev_people_group` ADD CONSTRAINT  `ui_dev_people_group01` UNIQUE (`group_id`,`user_no`);
-- ----------------------------
-- Table structure for dev_people
-- ----------------------------
DROP TABLE IF EXISTS `dev_people`;
CREATE TABLE `dev_people` (
  `user_no` varchar(15) NOT NULL PRIMARY KEY COMMENT '用户号',
  `real_name` varchar(32) NOT NULL COMMENT '姓名',
  `sex` varchar(1) DEFAULT '1' NULL COMMENT '性别',
  `birth_date` date NULL COMMENT '生日',
  `org_code` int(11) DEFAULT 0 NOT NULL COMMENT '部门',
  `duty_rank` varchar(36) NULL COMMENT '职务',
  `card_no` varchar(45) NULL UNIQUE COMMENT '卡号',
  `id_type` int(11) DEFAULT 1 NOT NULL COMMENT '证件类型',
  `id_number` varchar(45) NOT NULL COMMENT '证件号码',
  `user_type` int(11) DEFAULT 2 NOT NULL COMMENT '用户类型',
  `mobile_phone` varchar(32) NULL UNIQUE COMMENT '手机号码',
  `email` varchar(50) NULL UNIQUE COMMENT '邮箱',
  `register_type` int(11) DEFAULT 1 NOT NULL COMMENT '创建类型',
  `valid_date` date NULL COMMENT '生效日期',
  `expired_date` date NULL COMMENT '过期日期',
  `reg_time` datetime NULL COMMENT '注册时间',
  `dev_password` varchar(32) DEFAULT '111111' NULL COMMENT '设备密码',
  `reg_fingers` int(11) DEFAULT 0 NULL COMMENT '注册数',
  `note` varchar(100) NULL COMMENT '备注',
  `id_nation` varchar(100) NULL COMMENT '民族',
  `photo` varchar(200) NULL COMMENT '照片',
  `home_addr` varchar(200) NULL COMMENT '住址'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '注册人员|People' ROW_FORMAT = Dynamic;

ALTER TABLE `dev_people` ADD CONSTRAINT  `ui_dev_people01` UNIQUE (`id_type`,`id_number`);
-- ----------------------------
-- Table structure for dev_org_group
-- ----------------------------
DROP TABLE IF EXISTS `dev_org_group`;
CREATE TABLE `dev_org_group` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `group_id` int(11) NOT NULL COMMENT '分组id',
  `org_id` int(11) NOT NULL COMMENT '部门',
  `is_include_sub_org` tinyint(1) DEFAULT 1 NOT NULL COMMENT '包括子部门'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分组部门|Organization of group' ROW_FORMAT = Dynamic;

ALTER TABLE `dev_org_group` ADD CONSTRAINT  `ui_dev_org_group01` UNIQUE (`group_id`,`org_id`);
-- ----------------------------
-- Table structure for dev_event
-- ----------------------------
DROP TABLE IF EXISTS `dev_event`;
CREATE TABLE `dev_event` (
  `id` char(32) NOT NULL PRIMARY KEY,
  `dev_type` varchar(45) DEFAULT 'D10' NOT NULL COMMENT '设备类别',
  `dev_no` varchar(45) NOT NULL COMMENT '设备号',
  `org_code` int(11) NULL COMMENT '部门',
  `user_no` varchar(45) NULL COMMENT '用户',
  `status` int(11) NULL COMMENT '状态',
  `time` datetime NOT NULL COMMENT '时间',
  `auditee_auth_type` tinyint(2) NULL COMMENT '用户验证方式',
  `auditor_user_num` varchar(45) NULL COMMENT '审核人',
  `auditor_auth_type` tinyint(2) NULL COMMENT '审核人验证方式',
  `auditor_org` int(11) NULL COMMENT '审核人部门',
  `auth_type` tinyint(11) NULL COMMENT '验证模式'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '设备事件|Event of device' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_download_plan
-- ----------------------------
DROP TABLE IF EXISTS `dev_download_plan`;
CREATE TABLE `dev_download_plan` (
  `id` int(10) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'id',
  `user_no` varchar(36) NOT NULL COMMENT '用户编号',
  `reader_no` varchar(20) NOT NULL COMMENT '设备编号',
  `type` int(2) NOT NULL COMMENT '下发',
  `download_num` int(2) DEFAULT 3 NOT NULL COMMENT '尝试下发次数',
  `time` datetime NOT NULL COMMENT '创建时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '下发计划|Download plan' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_auth_role
-- ----------------------------
DROP TABLE IF EXISTS `dev_auth_role`;
CREATE TABLE `dev_auth_role` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '配置名称'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限定义|Authorization role' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dev_auth_group
-- ----------------------------
DROP TABLE IF EXISTS `dev_auth_group`;
CREATE TABLE `dev_auth_group` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'id',
  `name` varchar(45) NOT NULL COMMENT '分组名',
  `auth` varchar(256) NOT NULL COMMENT '授权权限'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '人员授权分组|Group of people' ROW_FORMAT = Dynamic;

ALTER TABLE `dev_reader` ADD CONSTRAINT `fk_dev_reader_reader_group_dev_reader_group_id` FOREIGN KEY(`reader_group`) REFERENCES `dev_reader_group`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `dev_auth_config` ADD CONSTRAINT `fk_dev_auth_config_role_id_dev_auth_role_id` FOREIGN KEY(`role_id`) REFERENCES `dev_auth_role`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
