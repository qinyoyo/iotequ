-- ----------------------------
-- Table structure for pay_pos
-- ----------------------------
DROP TABLE IF EXISTS `pay_pos`;
CREATE TABLE `pay_pos` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `no` varchar(45) NOT NULL UNIQUE COMMENT '终端编号',
  `shop_id` int(11) NOT NULL COMMENT '归属商店',
  `security_code` varchar(45) NULL COMMENT '安全码',
  `work_code` varchar(45) NULL COMMENT '工作密钥',
  `login_id` int(11) NULL COMMENT '签到ID',
  `ewallet_active` tinyint(1) DEFAULT 1 NOT NULL COMMENT '钱包账户可用',
  `count_project_list` varchar(200) NULL COMMENT '可用计次项目列表',
  `time_project_list` varchar(200) NULL COMMENT '可用计时项目列表'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交易终端' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_operator
-- ----------------------------
DROP TABLE IF EXISTS `pay_operator`;
CREATE TABLE `pay_operator` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `name` varchar(45) NOT NULL UNIQUE COMMENT '登录名',
  `real_name` varchar(45) NOT NULL COMMENT '真实名',
  `password` varchar(45) NOT NULL COMMENT '密码',
  `shop_id` int(11) NOT NULL COMMENT '所属店铺',
  `user_no` varchar(45) NULL UNIQUE COMMENT '用户编号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_shop
-- ----------------------------
DROP TABLE IF EXISTS `pay_shop`;
CREATE TABLE `pay_shop` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'ID',
  `corporation_id` int(11) NOT NULL COMMENT '归属公司',
  `name` varchar(45) NOT NULL COMMENT '店名',
  `linkman` varchar(45) NOT NULL COMMENT '联系人',
  `link_phone` varchar(45) NOT NULL COMMENT '联系电话',
  `address` varchar(45) NOT NULL COMMENT '地址'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商店' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_login
-- ----------------------------
DROP TABLE IF EXISTS `pay_login`;
CREATE TABLE `pay_login` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'ID',
  `pos_id` int(11) NOT NULL COMMENT 'pos终端',
  `shop_id` int(11) NOT NULL COMMENT '商店',
  `operator_id` int(11) NOT NULL COMMENT '操作人员',
  `batch_no` varchar(45) NOT NULL COMMENT '批次号',
  `login_time` datetime NOT NULL COMMENT '签到时间',
  `logout_time` datetime NULL COMMENT '签退时间',
  `device_stream` varchar(45) NULL COMMENT '设备流水号',
  `random_no` varchar(45) NOT NULL COMMENT '随机密钥',
  `app_version` varchar(45) NOT NULL COMMENT '应用版本号',
  `trade_count` int(11) DEFAULT 0 NOT NULL COMMENT '交易次数',
  `failure_count` int(11) DEFAULT 0 NOT NULL COMMENT '失败次数'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '签到日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_corporation
-- ----------------------------
DROP TABLE IF EXISTS `pay_corporation`;
CREATE TABLE `pay_corporation` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `parent_id` int(11) NULL COMMENT '上级公司',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `address` varchar(45) NOT NULL COMMENT '地址',
  `linkman` varchar(45) NOT NULL COMMENT '联系人',
  `linkphone` varchar(45) NOT NULL COMMENT '联系电话'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '运营公司' ROW_FORMAT = Dynamic;

ALTER TABLE `pay_pos` ADD CONSTRAINT `fk_pay_pos_shop_id_pay_shop_id` FOREIGN KEY(`shop_id`) REFERENCES `pay_shop`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `pay_operator` ADD CONSTRAINT `fk_pay_operator_shop_id_pay_shop_id` FOREIGN KEY(`shop_id`) REFERENCES `pay_shop`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `pay_shop` ADD CONSTRAINT `fk_pay_shop_corporation_id_pay_corporation_id` FOREIGN KEY(`corporation_id`) REFERENCES `pay_corporation`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
