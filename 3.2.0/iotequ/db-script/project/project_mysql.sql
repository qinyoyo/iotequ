-- ----------------------------
-- Table structure for pm_people
-- ----------------------------
DROP TABLE IF EXISTS `pm_people`;
CREATE TABLE `pm_people` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '编号',
  `group_id` int(11) NOT NULL COMMENT '职能分组',
  `user_id` varchar(45) NOT NULL COMMENT '职员'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目人员' ROW_FORMAT = Dynamic;

ALTER TABLE `pm_people` ADD CONSTRAINT  `ui_pm_people01` UNIQUE (`group_id`,`user_id`);
-- ----------------------------
-- Table structure for pm_version_application
-- ----------------------------
DROP TABLE IF EXISTS `pm_version_application`;
CREATE TABLE `pm_version_application` (
  `id` char(32) NOT NULL PRIMARY KEY COMMENT 'id',
  `flow_state` int(11) DEFAULT 1 NOT NULL COMMENT '状态',
  `flow_register_time` datetime NOT NULL COMMENT '申请时间',
  `flow_register_by` varchar(45) NOT NULL COMMENT '申请人',
  `project` varchar(36) NOT NULL COMMENT '项目',
  `application_type` int(11) NOT NULL COMMENT '申请类别',
  `customer` varchar(20) NOT NULL COMMENT '使用单位',
  `licence` varchar(100) NOT NULL COMMENT '授权说明',
  `contract_no` varchar(45) NULL COMMENT '合同号',
  `description` varchar(200) NOT NULL COMMENT '详情描述',
  `version_info` varchar(200) NULL COMMENT '发放版本号详情',
  `flow_note` varchar(1000) NULL COMMENT '已处理意见',
  `add_file` varchar(200) NULL COMMENT '附件',
  `flow_next_operator` varchar(45) NULL COMMENT '即将处理人',
  `flow_copy_to_list` varchar(100) NULL COMMENT '抄送列表'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '版本申请' ROW_FORMAT = Dynamic;
CREATE INDEX `index_pm_version_application_project` ON `pm_version_application`(`project`);

-- ----------------------------
-- Table structure for pm_project
-- ----------------------------
DROP TABLE IF EXISTS `pm_project`;
CREATE TABLE `pm_project` (
  `id` char(32) NOT NULL PRIMARY KEY COMMENT 'id',
  `flow_state` int(11) DEFAULT 1 NOT NULL COMMENT '状态',
  `flow_register_time` datetime NOT NULL COMMENT '登记时间',
  `flow_register_by` varchar(45) NOT NULL COMMENT '登记人',
  `name` varchar(45) NOT NULL UNIQUE COMMENT '名称',
  `type` int(11) DEFAULT 1 NOT NULL COMMENT '类别',
  `customer` varchar(200) NULL COMMENT '客户信息',
  `market_size` int(11) NULL COMMENT '市场规模(万)',
  `human_cost` int(11) NULL COMMENT '人力投入(人月)',
  `material_cost` int(11) NULL COMMENT '物料成本(万)',
  `code` varchar(45) NULL UNIQUE COMMENT '版本编码',
  `description` varchar(500) NULL COMMENT '详细描述',
  `flow_note` varchar(1000) NULL COMMENT '已处理意见',
  `add_file` varchar(45) NULL COMMENT '附件',
  `flow_next_operator` varchar(45) NULL COMMENT '下一步处理人',
  `flow_copy_to_list` varchar(100) NULL COMMENT '抄送列表'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目及产品列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pm_people_group
-- ----------------------------
DROP TABLE IF EXISTS `pm_people_group`;
CREATE TABLE `pm_people_group` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT '编号',
  `parent` int(8) NULL COMMENT '上级',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `group_type` varchar(45) NOT NULL UNIQUE COMMENT '类别',
  `enabled` tinyint(1) DEFAULT 1 NOT NULL COMMENT '激活',
  `description` varchar(200) NULL COMMENT '职能描述'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职能人员分组' ROW_FORMAT = Dynamic;

ALTER TABLE `pm_people` ADD CONSTRAINT `fk_pm_people_group_id_pm_people_group_id` FOREIGN KEY(`group_id`) REFERENCES `pm_people_group`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
