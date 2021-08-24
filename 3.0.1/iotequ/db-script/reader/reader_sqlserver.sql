-- ----------------------------
-- Table structure for dev_reader
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_reader]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_reader]
GO
CREATE TABLE [dbo].[dev_reader] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [reader_no] varchar(20) NOT NULL UNIQUE,
  [name] varchar(30) NOT NULL,
  [type] varchar(30) DEFAULT ('D10') NOT NULL,
  [reader_group] int NOT NULL,
  [address] varchar(100) NULL,
  [capacity] int NULL,
  [connect_type] varchar(11) NOT NULL,
  [ip] varchar(20) NOT NULL,
  [sn_no] varchar(36) NULL,
  [dev_mode] varchar(32) NOT NULL,
  [firmware] varchar(40) NULL,
  [is_online] bit DEFAULT ('0') NOT NULL,
  [is_time_sync] bit DEFAULT ('0') NOT NULL,
  [align_method] tinyint DEFAULT ((4)) NOT NULL,
  [blacklight_time] tinyint DEFAULT ((0)) NOT NULL,
  [voiceprompt] bit DEFAULT ('1') NOT NULL,
  [menu_time] tinyint DEFAULT ((0)) NOT NULL,
  [wengenform] tinyint DEFAULT ((2)) NOT NULL,
  [wengen_output] tinyint DEFAULT ((1)) NOT NULL,
  [wengen_out_area] tinyint DEFAULT ((26)) NOT NULL,
  [regfinger_out_time] tinyint DEFAULT ((49)) NOT NULL,
  [authfinger_out_time] tinyint DEFAULT ((49)) NOT NULL,
  [wg_order] tinyint DEFAULT ((0)) NULL,
  [relay_time] tinyint DEFAULT ((5)) NULL,
  [fixed_value] varchar(36) NULL,
  [alarm_enable] tinyint DEFAULT ((1)) NULL,
  [open_enable] tinyint DEFAULT ((0)) NULL,
  [door_state] tinyint DEFAULT ((1)) NULL,
  [relay_enable] tinyint DEFAULT ((1)) NULL,
  [doorbell_enable] tinyint DEFAULT ((1)) NULL,
  [wifi_ssid] varchar(36) NULL,
  [wifi_psw] varchar(36) NULL
)
GO
ALTER TABLE [dbo].[dev_reader] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_auth_config
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_auth_config]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_auth_config]
GO
CREATE TABLE [dbo].[dev_auth_config] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [role_id] int NOT NULL,
  [begin_at] datetime NULL,
  [end_at] datetime NULL,
  [start_time] datetime NULL,
  [end_time] datetime NULL,
  [only_work_day] bit DEFAULT ('0') NOT NULL,
  [auth] int DEFAULT ((4)) NOT NULL
)
GO
ALTER TABLE [dbo].[dev_auth_config] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_reader_people
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_reader_people]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_reader_people]
GO
CREATE TABLE [dbo].[dev_reader_people] (
  [type] int DEFAULT ((0)) NOT NULL,
  [order_num] int NULL,
  [user_no] varchar(36) NOT NULL,
  [status] int DEFAULT ((1)) NULL,
  [reader_no] varchar(20) NULL,
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY
)
GO
ALTER TABLE [dbo].[dev_reader_people] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_reader_group
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_reader_group]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_reader_group]
GO
CREATE TABLE [dbo].[dev_reader_group] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [name] varchar(20) NOT NULL UNIQUE,
  [parent] int NULL,
  [org_code] int NOT NULL,
  [org_auth] varchar(256) NULL,
  [sub_org_auth] varchar(256) NULL,
  [auth_group_list] varchar(256) NULL
)
GO
ALTER TABLE [dbo].[dev_reader_group] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_people_mapping
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_people_mapping]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_people_mapping]
GO
CREATE TABLE [dbo].[dev_people_mapping] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [reader_no] varchar(20) NOT NULL,
  [user_no] varchar(36) NOT NULL,
  [status] bit NOT NULL
)
GO
ALTER TABLE [dbo].[dev_people_mapping] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_people_group
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_people_group]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_people_group]
GO
CREATE TABLE [dbo].[dev_people_group] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [group_id] int NOT NULL,
  [user_no] varchar(45) NOT NULL
)
GO
ALTER TABLE [dbo].[dev_people_group] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[dev_people_group] ADD CONSTRAINT  [ui_dev_people_group01] UNIQUE ([group_id],[user_no])
GO
-- ----------------------------
-- Table structure for dev_people
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_people]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_people]
GO
CREATE TABLE [dbo].[dev_people] (
  [user_no] varchar(15) NOT NULL PRIMARY KEY,
  [real_name] varchar(32) NOT NULL,
  [sex] varchar(1) DEFAULT ('1') NULL,
  [birth_date] datetime NULL,
  [org_code] int DEFAULT ((0)) NOT NULL,
  [duty_rank] varchar(36) NULL,
  [card_no] varchar(45) NULL,
  [id_type] int DEFAULT ((1)) NOT NULL,
  [id_number] varchar(45) NOT NULL,
  [user_type] int DEFAULT ((2)) NOT NULL,
  [mobile_phone] varchar(32) NULL,
  [email] varchar(50) NULL,
  [register_type] int DEFAULT ((1)) NOT NULL,
  [valid_date] datetime NULL,
  [expired_date] datetime NULL,
  [reg_time] datetime NULL,
  [dev_password] varchar(32) DEFAULT ('111111') NULL,
  [reg_fingers] int DEFAULT ((0)) NULL,
  [note] varchar(100) NULL,
  [id_nation] varchar(100) NULL,
  [photo] varchar(200) NULL,
  [home_addr] varchar(200) NULL
)
GO
ALTER TABLE [dbo].[dev_people] SET (LOCK_ESCALATION = TABLE)
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_dev_people_card_no]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_dev_people_card_no]
GO
CREATE FUNCTION [dbo].[fn_multi_null_dev_people_card_no](    @value varchar(45))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from dev_people where card_no=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[dev_people] Add constraint unique_multi_null_dev_people_card_no check ([dbo].[fn_multi_null_dev_people_card_no](card_no)=1);
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_dev_people_mobile_phone]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_dev_people_mobile_phone]
GO
CREATE FUNCTION [dbo].[fn_multi_null_dev_people_mobile_phone](    @value varchar(32))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from dev_people where mobile_phone=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[dev_people] Add constraint unique_multi_null_dev_people_mobile_phone check ([dbo].[fn_multi_null_dev_people_mobile_phone](mobile_phone)=1);
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_dev_people_email]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_dev_people_email]
GO
CREATE FUNCTION [dbo].[fn_multi_null_dev_people_email](    @value varchar(50))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from dev_people where email=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[dev_people] Add constraint unique_multi_null_dev_people_email check ([dbo].[fn_multi_null_dev_people_email](email)=1);
GO

ALTER TABLE [dbo].[dev_people] ADD CONSTRAINT  [ui_dev_people01] UNIQUE ([id_type],[id_number])
GO
-- ----------------------------
-- Table structure for dev_org_group
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_org_group]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_org_group]
GO
CREATE TABLE [dbo].[dev_org_group] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [group_id] int NOT NULL,
  [org_id] int NOT NULL,
  [is_include_sub_org] bit DEFAULT ((1)) NOT NULL
)
GO
ALTER TABLE [dbo].[dev_org_group] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[dev_org_group] ADD CONSTRAINT  [ui_dev_org_group01] UNIQUE ([group_id],[org_id])
GO
-- ----------------------------
-- Table structure for dev_event
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_event]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_event]
GO
CREATE TABLE [dbo].[dev_event] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [dev_type] varchar(45) DEFAULT ('D10') NOT NULL,
  [dev_no] varchar(45) NOT NULL,
  [org_code] int NULL,
  [user_no] varchar(45) NULL,
  [status] int NULL,
  [time] datetime NOT NULL,
  [auditee_auth_type] tinyint NULL,
  [auditor_user_num] varchar(45) NULL,
  [auditor_auth_type] tinyint NULL,
  [auditor_org] int NULL,
  [auth_type] tinyint NULL
)
GO
ALTER TABLE [dbo].[dev_event] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_download_plan
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_download_plan]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_download_plan]
GO
CREATE TABLE [dbo].[dev_download_plan] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [user_no] varchar(36) NOT NULL,
  [reader_no] varchar(20) NOT NULL,
  [type] int NOT NULL,
  [download_num] int DEFAULT ((3)) NOT NULL,
  [time] datetime NOT NULL
)
GO
ALTER TABLE [dbo].[dev_download_plan] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_auth_role
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_auth_role]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_auth_role]
GO
CREATE TABLE [dbo].[dev_auth_role] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [name] varchar(20) NOT NULL
)
GO
ALTER TABLE [dbo].[dev_auth_role] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for dev_auth_group
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[dev_auth_group]') AND type IN ('U'))
	DROP TABLE [dbo].[dev_auth_group]
GO
CREATE TABLE [dbo].[dev_auth_group] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [name] varchar(45) NOT NULL,
  [auth] varchar(256) NOT NULL
)
GO
ALTER TABLE [dbo].[dev_auth_group] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[dev_reader] ADD CONSTRAINT [dbo].[fk_dev_reader_reader_group_dev_reader_group_id] FOREIGN KEY([reader_group]) REFERENCES [dbo].[dev_reader_group]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE [dbo].[dev_auth_config] ADD CONSTRAINT [dbo].[fk_dev_auth_config_role_id_dev_auth_role_id] FOREIGN KEY([role_id]) REFERENCES [dbo].[dev_auth_role]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
