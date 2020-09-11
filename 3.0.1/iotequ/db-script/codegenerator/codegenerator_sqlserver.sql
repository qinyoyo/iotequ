-- ----------------------------
-- Table structure for cg_list_field
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[cg_list_field]') AND type IN ('U'))
	DROP TABLE [dbo].[cg_list_field]
GO
CREATE TABLE [dbo].[cg_list_field] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [list_id] varchar(64) NOT NULL,
  [order_num] int NOT NULL,
  [entity_field] varchar(45) NOT NULL,
  [query_mode] int DEFAULT ((0)) NOT NULL,
  [fix] bit DEFAULT ((0)) NOT NULL,
  [expand] bit DEFAULT ((0)) NOT NULL,
  [overflow_tooltip] bit DEFAULT ((1)) NOT NULL,
  [align] varchar(45) DEFAULT ('left') NOT NULL,
  [header_align] varchar(45) NULL,
  [width] int DEFAULT ((128)) NOT NULL,
  [column_properties] text NULL,
  [hidden] bit DEFAULT ((0)) NOT NULL,
  [edit_inline] bit DEFAULT ((0)) NOT NULL,
  [default_query_value] varchar(200) NULL,
  [cell_display_slot] varchar(500) NULL,
  [show_type] varchar(45) NULL
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
  [id] varchar(64) NOT NULL PRIMARY KEY,
  [name] varchar(50) NOT NULL,
  [path] varchar(45) DEFAULT ('list') NOT NULL,
  [table_id] varchar(32) NOT NULL,
  [icon] varchar(45) NULL,
  [head_title] varchar(64) NOT NULL,
  [tag_title] varchar(45) NULL,
  [edit_inline] bit NOT NULL,
  [detail_inline] bit NOT NULL,
  [sons] varchar(100) NULL,
  [son_fields] varchar(100) NULL,
  [son_align] varchar(1) DEFAULT ('v') NOT NULL,
  [generator_type] int NOT NULL,
  [title_field] varchar(45) NULL,
  [parent_entity] varchar(45) NULL,
  [tree_show_entity] varchar(45) NULL,
  [show_search] bit NOT NULL,
  [toolbar_mode] int DEFAULT ((2)) NOT NULL,
  [table_height] int DEFAULT ((0)) NOT NULL,
  [pagination] bit NOT NULL,
  [order_by] varchar(100) NULL,
  [sort_field] varchar(45) NULL,
  [stripe] bit DEFAULT ((1)) NOT NULL,
  [border] bit DEFAULT ((0)) NOT NULL,
  [state_entity] varchar(45) NULL,
  [max_height] int DEFAULT ((0)) NULL,
  [highlight_current_row] bit DEFAULT ((1)) NOT NULL,
  [multiple] bit DEFAULT ((0)) NOT NULL,
  [show_summary] bit DEFAULT ((0)) NULL,
  [span_entities] varchar(200) NULL,
  [show_header] bit DEFAULT ((1)) NOT NULL,
  [images] varchar(200) NULL,
  [view_properties] text NULL,
  [table_properties] text NULL,
  [sons_properties] text NULL,
  [action_list] varchar(200) NULL,
  [flow_data_url] varchar(200) NULL,
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
  [form_id] varchar(64) NOT NULL,
  [order_num] int NOT NULL,
  [entity_field] varchar(45) NOT NULL,
  [width] int DEFAULT ((24)) NOT NULL,
  [group_title] varchar(45) NULL,
  [item_properties] text NULL,
  [form_item_properties] text NULL,
  [readonly] bit DEFAULT ((0)) NOT NULL,
  [must_input] bit DEFAULT ((0)) NOT NULL,
  [icon] varchar(64) NULL,
  [href] varchar(64) NULL,
  [hidden] bit DEFAULT ((0)) NOT NULL,
  [validate_as_title] bit DEFAULT ((0)) NOT NULL,
  [slot_templates] text NULL,
  [show_type] varchar(45) NULL
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
  [id] varchar(64) NOT NULL PRIMARY KEY,
  [name] varchar(45) NOT NULL,
  [path] varchar(200) DEFAULT ('record') NOT NULL,
  [table_id] varchar(32) NOT NULL,
  [head_title] varchar(400) NOT NULL,
  [is_flow] bit DEFAULT ((0)) NOT NULL,
  [icon] varchar(300) NULL,
  [tag_title] varchar(45) NOT NULL,
  [label_position] varchar(45) DEFAULT ('top') NOT NULL,
  [is_dialog] bit DEFAULT ((0)) NOT NULL,
  [continue_add] bit DEFAULT ((1)) NOT NULL,
  [images] varchar(200) NULL,
  [view_properties] text NULL,
  [form_properties] text NULL,
  [slot_templates] text NULL,
  [action_list] varchar(200) NULL
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
  [id] char(32) NOT NULL PRIMARY KEY,
  [table_id] varchar(32) NOT NULL,
  [order_num] int NOT NULL,
  [entity_name] varchar(45) NOT NULL,
  [title] varchar(32) NOT NULL,
  [name] varchar(32) NULL,
  [key_type] varchar(2) DEFAULT ('0') NOT NULL,
  [default_value] varchar(200) NULL,
  [show_type] varchar(30) DEFAULT ('text') NOT NULL,
  [formatter] varchar(100) NULL,
  [is_null] varchar(5) DEFAULT ('0') NOT NULL,
  [valid_expression] varchar(200) NULL,
  [valid_title] varchar(1000) NULL,
  [dict_table] varchar(200) NULL,
  [dict_field] varchar(1000) NULL,
  [dict_multiple] bit DEFAULT ((0)) NOT NULL,
  [dict_text] varchar(1000) NULL,
  [dyna_condition] varchar(200) NULL,
  [dict_full_name] bit DEFAULT ((0)) NOT NULL,
  [dict_parent] varchar(100) NULL,
  [dict_parent_field] varchar(100) NULL,
  [type] varchar(32) DEFAULT ('varchar') NOT NULL,
  [length] int DEFAULT ((36)) NULL,
  [numeric_precision] int NULL,
  [numeric_scale] int NULL,
  [fk_table] varchar(45) NULL,
  [fk_column] varchar(45) NULL,
  [fk_on_delete] varchar(45) NULL,
  [fk_on_update] varchar(45) NULL,
  [default_query_value] varchar(45) NULL
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
  [id] char(32) NOT NULL PRIMARY KEY,
  [table_id] varchar(32) NOT NULL,
  [order_num] int NOT NULL,
  [action] varchar(45) NOT NULL,
  [title] varchar(45) NOT NULL,
  [icon] varchar(45) NOT NULL,
  [append_class] varchar(200) NULL,
  [action_property] varchar(45) NOT NULL,
  [row_property] varchar(45) NOT NULL,
  [display_properties] varchar(45) NULL,
  [confirm_text] varchar(100) NULL,
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
  [id] varchar(32) NOT NULL PRIMARY KEY,
  [project_id] varchar(36) NOT NULL,
  [code] varchar(45) NOT NULL,
  [name] varchar(32) NULL,
  [title] varchar(32) NOT NULL,
  [sub_package] varchar(30) NULL,
  [entity] varchar(32) NULL,
  [template] varchar(32) NOT NULL,
  [trial_licence] int NULL,
  [trial_days] int NULL,
  [action_list] varchar(200) DEFAULT ('add,view,edit,delete,batdel,impo') NULL,
  [imports] varchar(500) NULL,
  [controller_extends] varchar(200) NULL,
  [pojo_imports] varchar(500) NULL,
  [pojo_extends] varchar(200) NULL,
  [pojo_java_code] text NULL,
  [creator] varchar(45) NOT NULL,
  [flow_dyna_fields_op] varchar(100) NULL,
  [flow_dyna_fields_cp] varchar(100) NULL
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
  [id] varchar(36) NOT NULL PRIMARY KEY,
  [code] varchar(10) NOT NULL,
  [creator] varchar(36) NOT NULL,
  [group_id] varchar(45) DEFAULT ('top.iotequ') NOT NULL,
  [name] varchar(36) NOT NULL,
  [version] varchar(36) DEFAULT ('3.0.1-SNAPSHOT') NOT NULL,
  [note] varchar(100) NULL,
  [modules] varchar(200) DEFAULT ('framework') NULL,
  [spring_module] bit DEFAULT ((1)) NOT NULL,
  [maven_module] bit DEFAULT ((1)) NOT NULL,
  [maven_server] varchar(100) NULL,
  [addtional_dependencies] text NULL,
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
