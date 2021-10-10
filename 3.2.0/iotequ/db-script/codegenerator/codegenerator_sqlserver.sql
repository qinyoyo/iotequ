-- ----------------------------
-- Table structure for cg_list_field
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_list_field]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_list_field]
GO
CREATE TABLE [dbo].[cg_list_field] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [list_id] varchar(64) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [order_num] int NOT NULL,
  [entity_field] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [query_mode] int DEFAULT ((0)) NOT NULL,
  [fix] bit DEFAULT ((0)) NOT NULL,
  [expand] bit DEFAULT ((0)) NOT NULL,
  [overflow_tooltip] bit DEFAULT ((1)) NOT NULL,
  [align] varchar(45) DEFAULT ('left') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [header_align] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [width] int DEFAULT ((128)) NOT NULL,
  [column_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [hidden] bit DEFAULT ((0)) NOT NULL,
  [edit_inline] bit DEFAULT ((0)) NOT NULL,
  [default_query_value] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [cell_display_slot] varchar(500) NULL COLLATE Chinese_PRC_CI_AS,
  [show_type] varchar(45) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[cg_list_field] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_list_field] ADD CONSTRAINT  [ui_cg_list_field01] UNIQUE ([list_id],[entity_field])
GO
-- ----------------------------
-- Table structure for cg_list
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_list]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_list]
GO
CREATE TABLE [dbo].[cg_list] (
  [id] varchar(64) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [name] varchar(50) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [path] varchar(45) DEFAULT ('list') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [table_id] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [icon] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [head_title] varchar(64) NULL COLLATE Chinese_PRC_CI_AS,
  [tag_title] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [edit_inline] bit NOT NULL,
  [detail_inline] bit NOT NULL,
  [sons] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [son_fields] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [son_align] varchar(1) DEFAULT ('v') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [generator_type] int NOT NULL,
  [title_field] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [parent_entity] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [tree_show_entity] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [show_search] bit NOT NULL,
  [toolbar_mode] int DEFAULT ((2)) NOT NULL,
  [table_height] int DEFAULT ((0)) NOT NULL,
  [pagination] bit NOT NULL,
  [order_by] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [sort_field] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [stripe] bit DEFAULT ((1)) NOT NULL,
  [border] bit DEFAULT ((0)) NOT NULL,
  [state_entity] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [max_height] int DEFAULT ((0)) NULL,
  [highlight_current_row] bit DEFAULT ((1)) NOT NULL,
  [multiple] bit DEFAULT ((0)) NOT NULL,
  [show_summary] bit DEFAULT ((0)) NULL,
  [span_entities] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [show_header] bit DEFAULT ((1)) NOT NULL,
  [images] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [view_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [table_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [sons_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [action_list] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [flow_data_url] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [local_export] bit DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[cg_list] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_list] ADD CONSTRAINT  [ui_cg_list01] UNIQUE ([path],[table_id])
GO
-- ----------------------------
-- Table structure for cg_form_field
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_form_field]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_form_field]
GO
CREATE TABLE [dbo].[cg_form_field] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [form_id] varchar(64) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [order_num] int NOT NULL,
  [entity_field] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [width] int DEFAULT ((24)) NOT NULL,
  [group_title] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [item_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [form_item_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [readonly] bit DEFAULT ((0)) NOT NULL,
  [must_input] bit DEFAULT ((0)) NOT NULL,
  [icon] varchar(64) NULL COLLATE Chinese_PRC_CI_AS,
  [href] varchar(64) NULL COLLATE Chinese_PRC_CI_AS,
  [hidden] bit DEFAULT ((0)) NOT NULL,
  [validate_as_title] bit DEFAULT ((0)) NOT NULL,
  [slot_templates] text NULL COLLATE Chinese_PRC_CI_AS,
  [show_type] varchar(45) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[cg_form_field] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_form_field] ADD CONSTRAINT  [ui_cg_form_field01] UNIQUE ([form_id],[entity_field])
GO
-- ----------------------------
-- Table structure for cg_form
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_form]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_form]
GO
CREATE TABLE [dbo].[cg_form] (
  [id] varchar(64) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [name] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [path] varchar(200) DEFAULT ('record') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [table_id] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [head_title] varchar(400) NULL COLLATE Chinese_PRC_CI_AS,
  [is_flow] bit DEFAULT ((0)) NOT NULL,
  [icon] varchar(300) NULL COLLATE Chinese_PRC_CI_AS,
  [tag_title] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [label_position] varchar(45) DEFAULT ('top') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [is_dialog] bit DEFAULT ((0)) NOT NULL,
  [continue_add] bit DEFAULT ((1)) NOT NULL,
  [images] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [view_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [form_properties] text NULL COLLATE Chinese_PRC_CI_AS,
  [slot_templates] text NULL COLLATE Chinese_PRC_CI_AS,
  [action_list] varchar(200) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[cg_form] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_form] ADD CONSTRAINT  [ui_cg_form01] UNIQUE ([path],[table_id])
GO
-- ----------------------------
-- Table structure for cg_field
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_field]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_field]
GO
CREATE TABLE [dbo].[cg_field] (
  [id] char(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [table_id] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [order_num] int NOT NULL,
  [entity_name] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [title] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [name] varchar(32) NULL COLLATE Chinese_PRC_CI_AS,
  [key_type] varchar(2) DEFAULT ('0') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [default_value] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [show_type] varchar(30) DEFAULT ('text') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [formatter] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [is_null] varchar(5) DEFAULT ('0') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [valid_expression] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [valid_title] varchar(1000) NULL COLLATE Chinese_PRC_CI_AS,
  [dict_table] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [dict_field] varchar(1000) NULL COLLATE Chinese_PRC_CI_AS,
  [dict_multiple] bit DEFAULT ((0)) NOT NULL,
  [dict_text] varchar(500) NULL COLLATE Chinese_PRC_CI_AS,
  [dyna_condition] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [dict_full_name] bit DEFAULT ((0)) NOT NULL,
  [dict_parent] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [dict_parent_field] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [type] varchar(32) DEFAULT ('varchar') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [length] int DEFAULT ((36)) NULL,
  [numeric_precision] int NULL,
  [numeric_scale] int NULL,
  [fk_table] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [fk_column] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [fk_on_delete] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [fk_on_update] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [default_query_value] varchar(45) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[cg_field] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_field] ADD CONSTRAINT  [ui_cg_field01] UNIQUE ([table_id],[entity_name])
GO
-- ----------------------------
-- Table structure for cg_button
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_button]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_button]
GO
CREATE TABLE [dbo].[cg_button] (
  [id] char(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [table_id] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [order_num] int NOT NULL,
  [action] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [title] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [icon] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [append_class] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [action_property] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [row_property] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [display_properties] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [confirm_text] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [is_refresh_list] bit NOT NULL
)
GO
ALTER TABLE [dbo].[cg_button] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_button] ADD CONSTRAINT  [ui_cg_button01] UNIQUE ([table_id],[action])
GO
-- ----------------------------
-- Table structure for cg_table
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_table]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_table]
GO
CREATE TABLE [dbo].[cg_table] (
  [id] varchar(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [project_id] varchar(36) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [code] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [name] varchar(32) NULL COLLATE Chinese_PRC_CI_AS,
  [title] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [sub_package] varchar(30) NULL COLLATE Chinese_PRC_CI_AS,
  [entity] varchar(32) NULL COLLATE Chinese_PRC_CI_AS,
  [template] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [trial_licence] int NULL,
  [trial_days] int NULL,
  [action_list] varchar(200) DEFAULT ('add,view,edit,delete,batdel,impo') NULL COLLATE Chinese_PRC_CI_AS,
  [imports] varchar(500) NULL COLLATE Chinese_PRC_CI_AS,
  [controller_extends] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [pojo_imports] varchar(500) NULL COLLATE Chinese_PRC_CI_AS,
  [pojo_extends] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [pojo_java_code] text NULL COLLATE Chinese_PRC_CI_AS,
  [creator] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [flow_dyna_fields_op] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [flow_dyna_fields_cp] varchar(100) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[cg_table] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_table] ADD CONSTRAINT  [ui_cg_table01] UNIQUE ([code],[project_id])
GO
-- ----------------------------
-- Table structure for cg_project
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_project]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_project]
GO
CREATE TABLE [dbo].[cg_project] (
  [id] varchar(36) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [code] varchar(10) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [creator] varchar(36) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [group_id] varchar(45) DEFAULT ('top.iotequ') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [name] varchar(36) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [version] varchar(36) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [note] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [modules] varchar(200) DEFAULT ('framework') NULL COLLATE Chinese_PRC_CI_AS,
  [spring_module] bit DEFAULT ((0)) NOT NULL,
  [maven_module] bit DEFAULT ((1)) NOT NULL,
  [maven_server] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [addtional_dependencies] text NULL COLLATE Chinese_PRC_CI_AS,
  [test] bit DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[cg_project] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[cg_project] ADD CONSTRAINT  [ui_cg_project01] UNIQUE ([code],[creator])
GO
ALTER TABLE [dbo].[cg_list_field] ADD CONSTRAINT [dbo].[fk_cg_list_field_list_id_cg_list_id] FOREIGN KEY([list_id]) REFERENCES [dbo].[cg_list]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[cg_list] ADD CONSTRAINT [dbo].[fk_cg_list_table_id_cg_table_id] FOREIGN KEY([table_id]) REFERENCES [dbo].[cg_table]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[cg_form_field] ADD CONSTRAINT [dbo].[fk_cg_form_field_form_id_cg_form_id] FOREIGN KEY([form_id]) REFERENCES [dbo].[cg_form]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[cg_form] ADD CONSTRAINT [dbo].[fk_cg_form_table_id_cg_table_id] FOREIGN KEY([table_id]) REFERENCES [dbo].[cg_table]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[cg_field] ADD CONSTRAINT [dbo].[fk_cg_field_table_id_cg_table_id] FOREIGN KEY([table_id]) REFERENCES [dbo].[cg_table]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[cg_button] ADD CONSTRAINT [dbo].[fk_cg_button_table_id_cg_table_id] FOREIGN KEY([table_id]) REFERENCES [dbo].[cg_table]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[cg_table] ADD CONSTRAINT [dbo].[fk_cg_table_project_id_cg_project_id] FOREIGN KEY([project_id]) REFERENCES [dbo].[cg_project]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
