-- ----------------------------
-- Table structure for ew_user_time
-- ----------------------------
DROP TABLE IF EXISTS `ew_user_time`;
CREATE TABLE `ew_user_time` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'id',
  `user_no` varchar(45) NOT NULL COMMENT '用户号',
  `time_id` int(11) NOT NULL COMMENT '计时项目编号',
  `amount_time` int(11) DEFAULT 0 NOT NULL COMMENT '计时时长',
  `check_code` varchar(45) NOT NULL COMMENT '检验码',
  `cost_limit` int(11) DEFAULT 0 NOT NULL COMMENT '消费限额',
  `day_limit` int(11) DEFAULT 0 NOT NULL COMMENT '日限额'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '计时钱包' ROW_FORMAT = Dynamic;

ALTER TABLE `ew_user_time` ADD CONSTRAINT  `ui_ew_user_time01` UNIQUE (`user_no`,`time_id`);
-- ----------------------------
-- Table structure for ew_user_count
-- ----------------------------
DROP TABLE IF EXISTS `ew_user_count`;
CREATE TABLE `ew_user_count` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `user_no` varchar(45) NOT NULL COMMENT '账户',
  `count_id` int(11) NOT NULL COMMENT '计次项目',
  `amount_count` int(11) DEFAULT 0 NOT NULL COMMENT '计次总数',
  `check_code` varchar(45) NOT NULL COMMENT '检验码',
  `cost_limit` int(11) DEFAULT 0 NOT NULL COMMENT '消费限额',
  `day_limit` int(11) DEFAULT 0 NOT NULL COMMENT '日限额'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '计次钱包' ROW_FORMAT = Dynamic;

ALTER TABLE `ew_user_count` ADD CONSTRAINT  `ui_ew_user_count01` UNIQUE (`user_no`,`count_id`);
-- ----------------------------
-- Table structure for ew_user
-- ----------------------------
DROP TABLE IF EXISTS `ew_user`;
CREATE TABLE `ew_user` (
  `user_no` varchar(32) NOT NULL PRIMARY KEY COMMENT '账户号',
  `is_active` tinyint(1) DEFAULT 1 NULL COMMENT '激活',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `gender` int(11) NOT NULL COMMENT '性别',
  `id_type` int(11) NOT NULL COMMENT '证件类别',
  `id_no` varchar(45) NOT NULL COMMENT '证件号码',
  `mobile_phone` varchar(45) NULL UNIQUE COMMENT '手机号码',
  `email` varchar(45) NULL UNIQUE COMMENT '邮箱',
  `wechat_openid` varchar(45) NULL UNIQUE COMMENT '微信号',
  `birth_date` date NULL COMMENT '生日',
  `member_group` varchar(45) NULL COMMENT '会员级别',
  `bonus_point` int(11) DEFAULT 0 NOT NULL COMMENT '积分',
  `amount_money` int(11) DEFAULT 0 NOT NULL COMMENT '余额',
  `cost_limit` int(11) DEFAULT 0 NOT NULL COMMENT '消费限额',
  `day_limit` int(11) DEFAULT 0 NOT NULL COMMENT '日限额',
  `active_since` datetime NULL COMMENT '生效时间',
  `expire_at` datetime NULL COMMENT '有效期限',
  `check_code` varchar(45) NOT NULL COMMENT '检验码'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '电子钱包' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ew_time_project
-- ----------------------------
DROP TABLE IF EXISTS `ew_time_project`;
CREATE TABLE `ew_time_project` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '编号',
  `icon` varchar(45) NULL COMMENT '图标',
  `name` varchar(45) NOT NULL COMMENT '消费名称',
  `base_value` int(11) DEFAULT 60 NOT NULL COMMENT '基本消费单元',
  `base_price` int(11) DEFAULT 10 NOT NULL COMMENT '基础价格',
  `bonus_point` double(12,2) NOT NULL COMMENT '基本单元默认积分',
  `start_time` time(6) NULL COMMENT '有效开始时间',
  `end_time` time(6) NULL COMMENT '有效结束时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '计时消费项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ew_device
-- ----------------------------
DROP TABLE IF EXISTS `ew_device`;
CREATE TABLE `ew_device` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `device_no` varchar(45) NOT NULL COMMENT '设备号',
  `shop_id` varchar(45) NOT NULL COMMENT '商家编号',
  `privilege_list` varchar(64) NOT NULL COMMENT '消费权限'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '授信设备' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ew_count_project
-- ----------------------------
DROP TABLE IF EXISTS `ew_count_project`;
CREATE TABLE `ew_count_project` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '编号',
  `icon` varchar(45) NULL COMMENT '图标',
  `name` varchar(45) NOT NULL COMMENT '消费名称',
  `base_price` int(11) DEFAULT 10 NOT NULL COMMENT '基础价格',
  `base_value` int(11) DEFAULT 1 NOT NULL COMMENT '基本消费单元',
  `bonus_point` double(12,2) NOT NULL COMMENT '每单元默认积分数',
  `start_time` time(6) NULL COMMENT '有效开始时间',
  `end_time` time(6) NULL COMMENT '有效结束时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '计次消费项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ew_bill
-- ----------------------------
DROP TABLE IF EXISTS `ew_bill`;
CREATE TABLE `ew_bill` (
  `no` varchar(32) NOT NULL PRIMARY KEY COMMENT '流水号',
  `canceled` tinyint(1) DEFAULT 0 NOT NULL COMMENT '已取消',
  `is_charge` tinyint(1) DEFAULT 0 NOT NULL COMMENT '是否充值',
  `user_no` varchar(45) NOT NULL COMMENT '用户编号',
  `batch_no` varchar(45) NOT NULL COMMENT '批次号',
  `dt` datetime NOT NULL COMMENT '时间',
  `operation_type` int(11) NOT NULL COMMENT '操作代码',
  `cost_type` int(11) DEFAULT 1 NOT NULL COMMENT '交易类别',
  `project_id` int(11) DEFAULT 0 NOT NULL COMMENT '交易项目',
  `project_name` varchar(45) DEFAULT '消费' NOT NULL COMMENT '交易名称',
  `project_price` int(11) NOT NULL COMMENT '产品单价',
  `amount` int(11) NOT NULL COMMENT '交易额',
  `amount_before` int(11) NOT NULL COMMENT '交易前钱包值',
  `bonus` int(11) NOT NULL COMMENT '获得积分',
  `bonus_before` int(11) NOT NULL COMMENT '交易前积分',
  `device_no` varchar(45) NOT NULL COMMENT '设备编号',
  `shop_id` varchar(45) NOT NULL COMMENT '商户编号',
  `device_stream` varchar(45) NOT NULL COMMENT '设备流水',
  `device_dt` datetime NOT NULL COMMENT '设备时间',
  `trade_no` varchar(45) NOT NULL UNIQUE COMMENT '订单号',
  `operator_no` varchar(45) NOT NULL COMMENT '操作员编号',
  `check_code` varchar(45) NOT NULL COMMENT '检验码',
  `link_no` varchar(45) NULL COMMENT '关联流水号',
  `login_id` int(11) NOT NULL COMMENT '签到id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消费明细' ROW_FORMAT = Dynamic;
CREATE INDEX `index_ew_bill_is_charge` ON `ew_bill`(`is_charge`);
CREATE INDEX `index_ew_bill_user_no` ON `ew_bill`(`user_no`);
CREATE INDEX `index_ew_bill_dt` ON `ew_bill`(`dt`);
CREATE INDEX `index_ew_bill_cost_type` ON `ew_bill`(`cost_type`);
CREATE INDEX `index_ew_bill_project_id` ON `ew_bill`(`project_id`);
CREATE INDEX `index_ew_bill_shop_id` ON `ew_bill`(`shop_id`);
CREATE INDEX `index_ew_bill_operator_no` ON `ew_bill`(`operator_no`);
CREATE INDEX `index_ew_bill_login_id` ON `ew_bill`(`login_id`);

ALTER TABLE `ew_user_time` ADD CONSTRAINT `fk_ew_user_time_user_no_ew_user_user_no` FOREIGN KEY(`user_no`) REFERENCES `ew_user`(`user_no`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `ew_user_time` ADD CONSTRAINT `fk_ew_user_time_time_id_ew_time_project_id` FOREIGN KEY(`time_id`) REFERENCES `ew_time_project`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `ew_user_count` ADD CONSTRAINT `fk_ew_user_count_user_no_ew_user_user_no` FOREIGN KEY(`user_no`) REFERENCES `ew_user`(`user_no`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `ew_user_count` ADD CONSTRAINT `fk_ew_user_count_count_id_ew_count_project_id` FOREIGN KEY(`count_id`) REFERENCES `ew_count_project`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
