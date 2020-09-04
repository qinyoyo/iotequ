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

