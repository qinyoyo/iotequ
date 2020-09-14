-- ----------------------------
-- Table structure for cg_list_field
-- ----------------------------
DROP TABLE IF EXISTS `cg_list_field`;
CREATE TABLE `cg_list_field` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'ID',
  `list_id` varchar(64) NOT NULL COMMENT 'listID',
  `order_num` int(11) NOT NULL COMMENT '排序',
  `entity_field` varchar(45) NOT NULL COMMENT '表entity字段',
  `query_mode` int(11) DEFAULT 0 NOT NULL COMMENT '查询模式',
  `fix` tinyint(1) DEFAULT 0 NOT NULL COMMENT '固定列',
  `expand` tinyint(1) DEFAULT 0 NOT NULL COMMENT '展开列',
  `overflow_tooltip` tinyint(1) DEFAULT 1 NOT NULL COMMENT '提示隐藏内容',
  `align` varchar(45) DEFAULT 'left' NOT NULL COMMENT '对齐',
  `header_align` varchar(45) NULL COMMENT '表头对齐',
  `width` int(11) DEFAULT 128 NOT NULL COMMENT '默认宽度',
  `column_properties` text NULL COMMENT '列属性',
  `hidden` tinyint(1) DEFAULT 0 NOT NULL COMMENT '隐藏字段',
  `edit_inline` tinyint(1) DEFAULT 0 NOT NULL COMMENT '行内编辑',
  `default_query_value` varchar(200) NULL COMMENT '缺省查询条件',
  `cell_display_slot` varchar(500) NULL COMMENT '自定义字段显示',
  `show_type` varchar(45) NULL COMMENT '修改控件类型为'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '列表视图字段定义' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_list_field` ADD CONSTRAINT  `ui_cg_list_field01` UNIQUE (`list_id`,`entity_field`);
-- ----------------------------
-- Table structure for cg_list
-- ----------------------------
DROP TABLE IF EXISTS `cg_list`;
CREATE TABLE `cg_list` (
  `id` varchar(64) NOT NULL PRIMARY KEY COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `path` varchar(45) DEFAULT 'list' NOT NULL COMMENT '路径最后一级',
  `table_id` varchar(32) NOT NULL COMMENT '对应表单',
  `icon` varchar(45) NULL COMMENT '图标',
  `head_title` varchar(64) NOT NULL COMMENT '列表标题',
  `tag_title` varchar(45) NULL COMMENT 'tag标题',
  `edit_inline` tinyint(1) NOT NULL COMMENT '行内编辑',
  `detail_inline` tinyint(1) NOT NULL COMMENT '行内详情',
  `sons` varchar(100) NULL COMMENT '子表',
  `son_fields` varchar(100) NULL COMMENT '子表外键字段列表',
  `son_align` varchar(1) DEFAULT 'v' NOT NULL COMMENT '主从表排列',
  `generator_type` int(11) NOT NULL COMMENT '主表宽度',
  `title_field` varchar(45) NULL COMMENT '显示在子表标题里的主表字段',
  `parent_entity` varchar(45) NULL COMMENT 'tree的父级Entity',
  `tree_show_entity` varchar(45) NULL COMMENT '树显示Entity',
  `show_search` tinyint(1) NOT NULL COMMENT '显示模糊查询',
  `toolbar_mode` int(11) DEFAULT 2 NOT NULL COMMENT '工具条显示模式',
  `table_height` int(11) DEFAULT 0 NOT NULL COMMENT '表高',
  `pagination` tinyint(1) NOT NULL COMMENT '是否分页',
  `order_by` varchar(100) NULL COMMENT '默认排序',
  `sort_field` varchar(45) NULL COMMENT '拖拽排序字段',
  `stripe` tinyint(1) DEFAULT 1 NOT NULL COMMENT '斑马纹风格',
  `border` tinyint(1) DEFAULT 0 NOT NULL COMMENT '边框',
  `state_entity` varchar(45) NULL COMMENT '状态字段或函数',
  `max_height` int(11) DEFAULT 0 NULL COMMENT '最大高度',
  `highlight_current_row` tinyint(1) DEFAULT 1 NOT NULL COMMENT '单选高亮',
  `multiple` tinyint(1) DEFAULT 0 NOT NULL COMMENT '多选',
  `show_summary` tinyint(1) DEFAULT 0 NULL COMMENT '统计栏',
  `span_entities` varchar(200) NULL COMMENT '合并字段',
  `show_header` tinyint(1) DEFAULT 1 NOT NULL COMMENT '显示表头',
  `images` varchar(200) NULL COMMENT '顶部轮播图像',
  `view_properties` text NULL COMMENT '视图属性',
  `table_properties` text NULL COMMENT '列表属性',
  `sons_properties` text NULL COMMENT '子组件属性',
  `action_list` varchar(200) NULL COMMENT '功能清单',
  `flow_data_url` varchar(200) NULL COMMENT '流程状态数据url',
  `local_export` tinyint(1) DEFAULT 0 NOT NULL COMMENT '本地导出'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '列表视图定义' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_list` ADD CONSTRAINT  `ui_cg_list01` UNIQUE (`path`,`table_id`);
-- ----------------------------
-- Table structure for cg_form_field
-- ----------------------------
DROP TABLE IF EXISTS `cg_form_field`;
CREATE TABLE `cg_form_field` (
  `id` int(11) AUTO_INCREMENT NOT NULL PRIMARY KEY COMMENT 'id',
  `form_id` varchar(64) NOT NULL COMMENT 'formId',
  `order_num` int(11) NOT NULL COMMENT '排序',
  `entity_field` varchar(45) NOT NULL COMMENT '表字段',
  `width` int(11) DEFAULT 24 NOT NULL COMMENT '宽度',
  `group_title` varchar(45) NULL COMMENT '分组标签',
  `item_properties` text NULL COMMENT '输入控件属性',
  `form_item_properties` text NULL COMMENT 'form_item属性',
  `readonly` tinyint(1) DEFAULT 0 NOT NULL COMMENT '只读',
  `must_input` tinyint(1) DEFAULT 0 NOT NULL COMMENT '必填',
  `icon` varchar(64) NULL COMMENT '图标',
  `href` varchar(64) NULL COMMENT '超链接',
  `hidden` tinyint(1) DEFAULT 0 NOT NULL COMMENT '隐藏',
  `validate_as_title` tinyint(1) DEFAULT 0 NOT NULL COMMENT '显示title提示',
  `slot_templates` text NULL COMMENT 'slot模板',
  `show_type` varchar(45) NULL COMMENT '更改控件类型为'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表单字段定义' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_form_field` ADD CONSTRAINT  `ui_cg_form_field01` UNIQUE (`form_id`,`entity_field`);
-- ----------------------------
-- Table structure for cg_form
-- ----------------------------
DROP TABLE IF EXISTS `cg_form`;
CREATE TABLE `cg_form` (
  `id` varchar(64) NOT NULL PRIMARY KEY COMMENT 'ID',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `path` varchar(200) DEFAULT 'record' NOT NULL COMMENT '路径',
  `table_id` varchar(32) NOT NULL COMMENT '对应表定义',
  `head_title` varchar(400) NOT NULL COMMENT 'form标题',
  `is_flow` tinyint(1) DEFAULT 0 NOT NULL COMMENT '是否流程定义',
  `icon` varchar(300) NULL COMMENT '图标',
  `tag_title` varchar(45) NOT NULL COMMENT 'tag标题',
  `label_position` varchar(45) DEFAULT 'top' NOT NULL COMMENT '字段标签位置',
  `is_dialog` tinyint(1) DEFAULT 0 NOT NULL COMMENT '对话框模式',
  `continue_add` tinyint(1) DEFAULT 1 NOT NULL COMMENT '连续编辑',
  `images` varchar(200) NULL COMMENT '顶部轮播图像',
  `view_properties` text NULL COMMENT '视图属性',
  `form_properties` text NULL COMMENT '表单属性',
  `slot_templates` text NULL COMMENT 'slot模板',
  `action_list` varchar(200) NULL COMMENT '功能清单'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表单定义表' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_form` ADD CONSTRAINT  `ui_cg_form01` UNIQUE (`path`,`table_id`);
-- ----------------------------
-- Table structure for cg_field
-- ----------------------------
DROP TABLE IF EXISTS `cg_field`;
CREATE TABLE `cg_field` (
  `id` char(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
  `table_id` varchar(32) NOT NULL COMMENT '表ID',
  `order_num` int(11) NOT NULL COMMENT '排序',
  `entity_name` varchar(45) NOT NULL COMMENT 'Entity名',
  `title` varchar(32) NOT NULL COMMENT '标题',
  `name` varchar(32) NULL COMMENT '字段名',
  `key_type` varchar(2) DEFAULT '0' NOT NULL COMMENT '索引',
  `default_value` varchar(200) NULL COMMENT '默认值',
  `show_type` varchar(30) DEFAULT 'text' NOT NULL COMMENT '表单控件类型',
  `formatter` varchar(100) NULL COMMENT '显示格式',
  `is_null` varchar(5) DEFAULT '0' NOT NULL COMMENT '可空',
  `valid_expression` varchar(200) NULL COMMENT '校验正则表达式',
  `valid_title` varchar(1000) NULL COMMENT '校验提示',
  `dict_table` varchar(200) NULL COMMENT '字典表或SQL语句',
  `dict_field` varchar(1000) NULL COMMENT '字典code',
  `dict_multiple` tinyint(1) DEFAULT 0 NOT NULL COMMENT '多选',
  `dict_text` varchar(1000) NULL COMMENT '字典Text',
  `dyna_condition` varchar(200) NULL COMMENT '动态条件',
  `dict_full_name` tinyint(1) DEFAULT 0 NOT NULL COMMENT '显示全名',
  `dict_parent` varchar(100) NULL COMMENT '父亲字段名',
  `dict_parent_field` varchar(100) NULL COMMENT '树键值字段',
  `type` varchar(32) DEFAULT 'varchar' NOT NULL COMMENT '字段类型',
  `length` int(11) DEFAULT 36 NULL COMMENT '字段长',
  `numeric_precision` int(11) NULL COMMENT '小数位长',
  `numeric_scale` int(11) NULL COMMENT '小数精度',
  `fk_table` varchar(45) NULL COMMENT '关联主表',
  `fk_column` varchar(45) NULL COMMENT '关联主表字段',
  `fk_on_delete` varchar(45) NULL COMMENT 'On Delete',
  `fk_on_update` varchar(45) NULL COMMENT 'On Update',
  `default_query_value` varchar(45) NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字段定义表' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_field` ADD CONSTRAINT  `ui_cg_field01` UNIQUE (`table_id`,`entity_name`);
-- ----------------------------
-- Table structure for cg_button
-- ----------------------------
DROP TABLE IF EXISTS `cg_button`;
CREATE TABLE `cg_button` (
  `id` char(32) NOT NULL PRIMARY KEY COMMENT 'id',
  `table_id` varchar(32) NOT NULL COMMENT '所属表单',
  `order_num` int(11) NOT NULL COMMENT '排序',
  `action` varchar(45) NOT NULL COMMENT '操作代码',
  `title` varchar(45) NOT NULL COMMENT '标题',
  `icon` varchar(45) NOT NULL COMMENT '图标',
  `append_class` varchar(200) NULL COMMENT '执行函数或参数',
  `action_property` varchar(45) NOT NULL COMMENT '行为属性',
  `row_property` varchar(45) NOT NULL COMMENT '行属性',
  `display_properties` varchar(45) NULL COMMENT '显示属性',
  `confirm_text` varchar(100) NULL COMMENT '操作前提示',
  `is_refresh_list` tinyint(1) NOT NULL COMMENT '操作后刷新列表'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '按钮定义' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_button` ADD CONSTRAINT  `ui_cg_button01` UNIQUE (`table_id`,`action`);
-- ----------------------------
-- Table structure for cg_table
-- ----------------------------
DROP TABLE IF EXISTS `cg_table`;
CREATE TABLE `cg_table` (
  `id` varchar(32) NOT NULL PRIMARY KEY COMMENT '主键ID',
  `project_id` varchar(36) NOT NULL COMMENT '项目id',
  `code` varchar(45) NOT NULL COMMENT 'cg代码',
  `name` varchar(32) NULL COMMENT '数据库表',
  `title` varchar(32) NOT NULL COMMENT '标题',
  `sub_package` varchar(30) NULL COMMENT '子包',
  `entity` varchar(32) NULL COMMENT 'Entity类名',
  `template` varchar(32) NOT NULL COMMENT '模板名',
  `trial_licence` int(11) NULL COMMENT '试用licence',
  `trial_days` int(11) NULL COMMENT '试用天数',
  `action_list` varchar(200) DEFAULT 'add,view,edit,delete,batdel,impo' NULL COMMENT '功能列表',
  `imports` varchar(500) NULL COMMENT 'import列表',
  `controller_extends` varchar(200) NULL COMMENT 'controller基类以及实现接口',
  `pojo_imports` varchar(500) NULL COMMENT 'pojo基类列表',
  `pojo_extends` varchar(200) NULL COMMENT 'pojo实现接口和继承类',
  `pojo_java_code` text NULL COMMENT 'pojo自定义代码',
  `creator` varchar(45) NOT NULL COMMENT '创建人',
  `flow_dyna_fields_op` varchar(100) NULL COMMENT '处理人关联字段',
  `flow_dyna_fields_cp` varchar(100) NULL COMMENT '抄送人关联字段'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '表单定义' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_table` ADD CONSTRAINT  `ui_cg_table01` UNIQUE (`code`,`project_id`);
-- ----------------------------
-- Table structure for cg_project
-- ----------------------------
DROP TABLE IF EXISTS `cg_project`;
CREATE TABLE `cg_project` (
  `id` varchar(36) NOT NULL PRIMARY KEY COMMENT 'id',
  `code` varchar(10) NOT NULL COMMENT '代码',
  `creator` varchar(36) NOT NULL COMMENT '创建人',
  `group_id` varchar(45) DEFAULT 'top.iotequ' NOT NULL COMMENT '组织机构',
  `name` varchar(36) NOT NULL COMMENT '项目名称',
  `version` varchar(36) NOT NULL COMMENT '版本',
  `note` varchar(100) NULL COMMENT '项目描述',
  `modules` varchar(200) DEFAULT 'framework' NULL COMMENT '包含的iotequ模块',
  `spring_module` tinyint(1) DEFAULT 1 NOT NULL COMMENT 'spring模块',
  `maven_module` tinyint(1) DEFAULT 1 NOT NULL COMMENT 'maven模块',
  `maven_server` varchar(100) NULL COMMENT '内部maven库地址',
  `addtional_dependencies` text NULL COMMENT '附件依赖',
  `test` tinyint(1) DEFAULT 0 NOT NULL COMMENT 'test依赖模块'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目' ROW_FORMAT = Dynamic;

ALTER TABLE `cg_project` ADD CONSTRAINT  `ui_cg_project01` UNIQUE (`code`,`creator`);
ALTER TABLE `cg_list_field` ADD CONSTRAINT `fk_cg_list_field_list_id_cg_list_id` FOREIGN KEY(`list_id`) REFERENCES `cg_list`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `cg_list` ADD CONSTRAINT `fk_cg_list_table_id_cg_table_id` FOREIGN KEY(`table_id`) REFERENCES `cg_table`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `cg_form_field` ADD CONSTRAINT `fk_cg_form_field_form_id_cg_form_id` FOREIGN KEY(`form_id`) REFERENCES `cg_form`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `cg_form` ADD CONSTRAINT `fk_cg_form_table_id_cg_table_id` FOREIGN KEY(`table_id`) REFERENCES `cg_table`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `cg_field` ADD CONSTRAINT `fk_cg_field_table_id_cg_table_id` FOREIGN KEY(`table_id`) REFERENCES `cg_table`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `cg_button` ADD CONSTRAINT `fk_cg_button_table_id_cg_table_id` FOREIGN KEY(`table_id`) REFERENCES `cg_table`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `cg_table` ADD CONSTRAINT `fk_cg_table_project_id_cg_project_id` FOREIGN KEY(`project_id`) REFERENCES `cg_project`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
