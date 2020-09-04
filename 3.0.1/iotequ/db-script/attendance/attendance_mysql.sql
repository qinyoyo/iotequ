-- ----------------------------
-- Table structure for ad_special_shift_time
-- ----------------------------
DROP TABLE IF EXISTS `ad_special_shift_time`;
CREATE TABLE `ad_special_shift_time` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `special_shift_id` int(11) NOT NULL COMMENT '特殊排班编号',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `start_time` time(6) NOT NULL COMMENT '开始时间',
  `end_time` time(6) NOT NULL COMMENT '结束时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '特殊排班时间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_shift_time
-- ----------------------------
DROP TABLE IF EXISTS `ad_shift_time`;
CREATE TABLE `ad_shift_time` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `shift_id` int(11) NOT NULL COMMENT '排班编号',
  `name` varchar(100) NOT NULL COMMENT '详细描述',
  `week_days` varchar(45) DEFAULT '1,2,3,4,5' NOT NULL COMMENT '工作日序列',
  `start_work_time` time(6) NOT NULL COMMENT '上班时间',
  `end_work_time` time(6) NOT NULL COMMENT '下班时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '排班时间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_org
-- ----------------------------
DROP TABLE IF EXISTS `ad_org`;
CREATE TABLE `ad_org` (
  `org_code` int(11) NOT NULL PRIMARY KEY COMMENT '机构代码',
  `shift_id` int(11) NULL COMMENT '部门排班',
  `hr` varchar(36) NULL COMMENT '人事',
  `manager` varchar(36) NULL COMMENT '考勤审核人',
  `manage_limit` int(11) NULL COMMENT '审核权限',
  `deviation` int(11) NULL COMMENT '允许误差',
  `float_limit` int(11) NULL COMMENT '浮动范围',
  `absent_limit` int(11) NULL COMMENT '旷工底限',
  `free_work_limit` int(11) NULL COMMENT '自由加班起限'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤部门信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_exception
-- ----------------------------
DROP TABLE IF EXISTS `ad_exception`;
CREATE TABLE `ad_exception` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `shift_id` int(11) NOT NULL COMMENT '排班编号',
  `end_date` date NOT NULL COMMENT '结束日期(含)',
  `start_date` date NOT NULL COMMENT '开始日期',
  `week_day` int(11) NOT NULL COMMENT '工作属性',
  `description` varchar(100) NULL COMMENT '详细描述'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '节假日调休安排' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_employee
-- ----------------------------
DROP TABLE IF EXISTS `ad_employee`;
CREATE TABLE `ad_employee` (
  `id` varchar(36) NOT NULL PRIMARY KEY COMMENT '用户',
  `employee_no` varchar(32) NOT NULL UNIQUE COMMENT '工号',
  `em_level` int(11) DEFAULT 1 NOT NULL COMMENT '职务级别',
  `is_attendance` tinyint(1) DEFAULT 1 NOT NULL COMMENT '是否考勤',
  `enter_date` date NULL COMMENT '入职日期',
  `leave_date` date NULL COMMENT '离职日期',
  `overtime_minutes` int(11) DEFAULT 0 NOT NULL COMMENT '可调休时间',
  `shift_id` int(11) NULL COMMENT '考勤排班'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤职员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_approve_list
-- ----------------------------
DROP TABLE IF EXISTS `ad_approve_list`;
CREATE TABLE `ad_approve_list` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `approver` varchar(36) NULL COMMENT '审批人',
  `approve_time` datetime NOT NULL COMMENT '审批时间',
  `state1` int(11) NULL COMMENT '审批后状态',
  `state0` int(11) NULL COMMENT '初始状态',
  `approve_note` varchar(200) NULL COMMENT '审批意见',
  `adjust_id` varchar(32) NOT NULL COMMENT '考勤调整单编号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审核信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_special_shift
-- ----------------------------
DROP TABLE IF EXISTS `ad_special_shift`;
CREATE TABLE `ad_special_shift` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `shift_mode` int(11) NOT NULL COMMENT '排班属性',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `mode_property` int(11) NULL COMMENT '考勤模式',
  `start_date` date NOT NULL COMMENT '启用时间',
  `end_date` date NOT NULL COMMENT '结束时间(含)',
  `org_codes` varchar(300) NOT NULL COMMENT '部门',
  `sex_property` int(11) NULL COMMENT '性别',
  `age_property0` int(11) NULL COMMENT '年龄0',
  `age_property1` int(11) NULL COMMENT '年龄1',
  `level_property0` int(11) NULL COMMENT '职级0',
  `level_property1` int(11) NULL COMMENT '职级1',
  `description` varchar(45) NULL COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '特殊排班表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_shift
-- ----------------------------
DROP TABLE IF EXISTS `ad_shift`;
CREATE TABLE `ad_shift` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `name` varchar(45) NOT NULL COMMENT '排班名称',
  `ad_mode` int(11) DEFAULT 1 NOT NULL COMMENT '考勤模式',
  `start_date` date NOT NULL COMMENT '启用日期',
  `end_date` date NOT NULL COMMENT '终止时间(含)',
  `minutes_per_day` int(11) DEFAULT 480 NOT NULL COMMENT '工作时长(分)',
  `description` varchar(200) NULL COMMENT '描述'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤排班表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_day_result
-- ----------------------------
DROP TABLE IF EXISTS `ad_day_result`;
CREATE TABLE `ad_day_result` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `org_code` int(11) NOT NULL COMMENT '部门',
  `org_name` varchar(100) NOT NULL COMMENT '部门名',
  `employee` varchar(45) NOT NULL COMMENT '员工',
  `employee_no` varchar(45) NOT NULL COMMENT '工号',
  `real_name` varchar(45) NOT NULL COMMENT '姓名',
  `ad_date` date NOT NULL COMMENT '日期',
  `shift_name` varchar(45) NOT NULL COMMENT '班次',
  `state` int(11) NOT NULL COMMENT '考勤',
  `state_name` varchar(45) NOT NULL COMMENT '考勤描述',
  `times` int(11) DEFAULT 1 NOT NULL COMMENT '次数',
  `minutes` int(11) DEFAULT 0 NOT NULL COMMENT '时长',
  `work_minutes` int(11) DEFAULT 0 NOT NULL COMMENT '工作时长',
  `shift_id` int(11) DEFAULT 0 NOT NULL COMMENT '班次排序'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '日考勤结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ad_data
-- ----------------------------
DROP TABLE IF EXISTS `ad_data`;
CREATE TABLE `ad_data` (
  `id` char(32) NOT NULL PRIMARY KEY,
  `employee_no` varchar(45) NOT NULL COMMENT '工号',
  `rec_source` varchar(45) NULL COMMENT '来源',
  `rec_type` int(11) DEFAULT 3 NOT NULL COMMENT '类别',
  `rec_time` datetime NOT NULL COMMENT '时间',
  `is_used` tinyint(1) DEFAULT 0 NOT NULL COMMENT '被使用'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤数据' ROW_FORMAT = Dynamic;
CREATE INDEX `index_ad_data_employee_no` ON `ad_data`(`employee_no`);

-- ----------------------------
-- Table structure for ad_adjust
-- ----------------------------
DROP TABLE IF EXISTS `ad_adjust`;
CREATE TABLE `ad_adjust` (
  `id` char(32) NOT NULL PRIMARY KEY COMMENT 'id',
  `employee` varchar(36) NOT NULL COMMENT '职员',
  `org_code` int(11) NOT NULL COMMENT '部门',
  `hr` varchar(45) NULL COMMENT '人事',
  `adjust_type` int(11) DEFAULT 10 NULL COMMENT '调整类别',
  `state` int(11) NOT NULL COMMENT '审批状态',
  `register_time` datetime NOT NULL COMMENT '登记时间',
  `start_time` datetime NULL COMMENT '开始时间',
  `end_time` datetime NULL COMMENT '结束时间',
  `description` varchar(500) NOT NULL COMMENT '详情描述',
  `approver` varchar(36) NULL COMMENT '后续审批人',
  `approve_org` int(11) NULL COMMENT '审批部门',
  `add_file` varchar(200) NULL COMMENT '附件'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '考勤调整' ROW_FORMAT = Dynamic;

ALTER TABLE `ad_special_shift_time` ADD CONSTRAINT `fk_ad_special_shift_time_special_shift_id_ad_special_shift_id` FOREIGN KEY(`special_shift_id`) REFERENCES `ad_special_shift`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `ad_shift_time` ADD CONSTRAINT `fk_ad_shift_time_shift_id_ad_shift_id` FOREIGN KEY(`shift_id`) REFERENCES `ad_shift`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `ad_org` ADD CONSTRAINT `fk_ad_org_org_code_sys_org_org_code` FOREIGN KEY(`org_code`) REFERENCES `sys_org`(`org_code`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `ad_exception` ADD CONSTRAINT `fk_ad_exception_shift_id_ad_shift_id` FOREIGN KEY(`shift_id`) REFERENCES `ad_shift`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `ad_employee` ADD CONSTRAINT `fk_ad_employee_id_sys_user_id` FOREIGN KEY(`id`) REFERENCES `sys_user`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `ad_approve_list` ADD CONSTRAINT `fk_ad_approve_list_adjust_id_ad_adjust_id` FOREIGN KEY(`adjust_id`) REFERENCES `ad_adjust`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
