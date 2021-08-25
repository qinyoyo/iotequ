-- ----------------------------
-- Table structure for ck_sign_in
-- ----------------------------
DROP TABLE IF EXISTS `ck_sign_in`;
CREATE TABLE `ck_sign_in` (
  `id` char(32) NOT NULL PRIMARY KEY,
  `user_no` varchar(15) NOT NULL COMMENT '用户号',
  `org_code` int(11) NOT NULL COMMENT '医院科室|Depart',
  `rec_source` varchar(45) NOT NULL COMMENT '来源|Source',
  `rec_source_type` varchar(45) DEFAULT 'U53' NOT NULL COMMENT '来源类别|Device type',
  `event_type` int(11) DEFAULT 1 NOT NULL COMMENT '事件类别',
  `rec_time` datetime NOT NULL COMMENT '时间|Sign time'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '认证记录' ROW_FORMAT = Dynamic;
CREATE INDEX `index_ck_sign_in_user_no` ON `ck_sign_in`(`user_no`);
CREATE INDEX `index_ck_sign_in_org_code` ON `ck_sign_in`(`org_code`);
CREATE INDEX `index_ck_sign_in_rec_time` ON `ck_sign_in`(`rec_time`);

-- ----------------------------
-- Table structure for ck_register
-- ----------------------------
DROP TABLE IF EXISTS `ck_register`;
CREATE TABLE `ck_register` (
  `id` char(32) NOT NULL PRIMARY KEY COMMENT 'ID',
  `user_no` varchar(36) NOT NULL COMMENT '用户号',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `sex` varchar(1) DEFAULT '1' NOT NULL COMMENT '性别',
  `birth_date` date NULL COMMENT '出生日',
  `org_code` int(36) NOT NULL COMMENT '科室编号',
  `org_name` varchar(32) NOT NULL COMMENT '医院科室',
  `in_date` date NOT NULL COMMENT '上机日',
  `on_time` time(6) NOT NULL COMMENT '上机时间',
  `off_time` time(6) NULL COMMENT '下机时间'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '上机记录' ROW_FORMAT = Dynamic;

ALTER TABLE `ck_register` ADD CONSTRAINT  `ui_ck_register012` UNIQUE (`user_no`,`org_code`,`in_date`);
