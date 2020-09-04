-- ----------------------------
-- Table structure for pm_people
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pm_people]') AND type IN ('U'))
	DROP TABLE [dbo].[pm_people]
GO
CREATE TABLE [dbo].[pm_people] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [group_id] int NOT NULL,
  [user_id] varchar(45) NOT NULL
)
GO
ALTER TABLE [dbo].[pm_people] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[pm_people] ADD CONSTRAINT  [ui_pm_people01] UNIQUE ([group_id],[user_id])
GO
-- ----------------------------
-- Table structure for pm_version_application
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pm_version_application]') AND type IN ('U'))
	DROP TABLE [dbo].[pm_version_application]
GO
CREATE TABLE [dbo].[pm_version_application] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [flow_state] int DEFAULT ((1)) NOT NULL,
  [flow_register_time] datetime NOT NULL,
  [flow_register_by] varchar(45) NOT NULL,
  [project] varchar(36) NOT NULL,
  [application_type] int NOT NULL,
  [customer] varchar(20) NOT NULL,
  [licence] varchar(100) NOT NULL,
  [contract_no] varchar(45) NULL,
  [description] varchar(200) NOT NULL,
  [version_info] varchar(200) NULL,
  [flow_note] varchar(1000) NULL,
  [add_file] varchar(200) NULL,
  [flow_next_operator] varchar(45) NULL,
  [flow_copy_to_list] varchar(100) NULL
)
GO
ALTER TABLE [dbo].[pm_version_application] SET (LOCK_ESCALATION = TABLE)
GO
CREATE INDEX [index_pm_version_application_project] ON [dbo].[pm_version_application]([project]);

-- ----------------------------
-- Table structure for pm_project
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pm_project]') AND type IN ('U'))
	DROP TABLE [dbo].[pm_project]
GO
CREATE TABLE [dbo].[pm_project] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [flow_state] int DEFAULT ((1)) NOT NULL,
  [flow_register_time] datetime NOT NULL,
  [flow_register_by] varchar(45) NOT NULL,
  [name] varchar(45) NOT NULL UNIQUE,
  [type] int DEFAULT ((1)) NOT NULL,
  [customer] varchar(200) NULL,
  [market_size] int NULL,
  [human_cost] int NULL,
  [material_cost] int NULL,
  [code] varchar(45) NULL,
  [description] varchar(500) NULL,
  [flow_note] varchar(1000) NULL,
  [add_file] varchar(45) NULL,
  [flow_next_operator] varchar(45) NULL,
  [flow_copy_to_list] varchar(100) NULL
)
GO
ALTER TABLE [dbo].[pm_project] SET (LOCK_ESCALATION = TABLE)
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_pm_project_code]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_pm_project_code]
GO
CREATE FUNCTION [dbo].[fn_multi_null_pm_project_code](    @value varchar(45))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from pm_project where code=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[pm_project] Add constraint unique_multi_null_pm_project_code check ([dbo].[fn_multi_null_pm_project_code](code)=1);
GO

-- ----------------------------
-- Table structure for pm_people_group
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pm_people_group]') AND type IN ('U'))
	DROP TABLE [dbo].[pm_people_group]
GO
CREATE TABLE [dbo].[pm_people_group] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [parent] int NULL,
  [name] varchar(45) NOT NULL,
  [group_type] varchar(45) NOT NULL UNIQUE,
  [enabled] bit DEFAULT ((1)) NOT NULL,
  [description] varchar(200) NULL
)
GO
ALTER TABLE [dbo].[pm_people_group] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[pm_people] ADD CONSTRAINT [dbo].[fk_pm_people_group_id_pm_people_group_id] FOREIGN KEY([group_id]) REFERENCES [dbo].[pm_people_group]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;
