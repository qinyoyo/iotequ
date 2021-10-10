-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_user]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_user]
GO
CREATE TABLE [dbo].[sys_user] (
  [id] varchar(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [icon] text NULL COLLATE Chinese_PRC_CI_AS,
  [name] varchar(32) NOT NULL UNIQUE COLLATE Chinese_PRC_CI_AS,
  [real_name] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [sex] varchar(1) DEFAULT ('1') NULL COLLATE Chinese_PRC_CI_AS,
  [birth_date] datetime NULL,
  [reg_time] datetime NULL,
  [mobile_phone] varchar(32) NULL COLLATE Chinese_PRC_CI_AS,
  [email] varchar(50) NULL COLLATE Chinese_PRC_CI_AS,
  [wechat_openid] varchar(50) NULL COLLATE Chinese_PRC_CI_AS,
  [org_code] int NOT NULL,
  [org_privilege] int NULL,
  [role_list] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [locked] bit DEFAULT ((0)) NOT NULL,
  [state] bit DEFAULT ((1)) NOT NULL,
  [id_type] int DEFAULT ((1)) NOT NULL,
  [id_number] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [expired_time] datetime NULL,
  [password_expired_time] datetime NULL,
  [password] varchar(32) DEFAULT ('123456') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [password_error_times] int DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[sys_user] SET (LOCK_ESCALATION = TABLE)
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_sys_user_mobile_phone]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_sys_user_mobile_phone]
GO
CREATE FUNCTION [dbo].[fn_multi_null_sys_user_mobile_phone](    @value varchar(32))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from sys_user where mobile_phone=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[sys_user] Add constraint unique_multi_null_sys_user_mobile_phone check ([dbo].[fn_multi_null_sys_user_mobile_phone](mobile_phone)=1);
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_sys_user_email]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_sys_user_email]
GO
CREATE FUNCTION [dbo].[fn_multi_null_sys_user_email](    @value varchar(50))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from sys_user where email=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[sys_user] Add constraint unique_multi_null_sys_user_email check ([dbo].[fn_multi_null_sys_user_email](email)=1);
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_sys_user_wechat_openid]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_sys_user_wechat_openid]
GO
CREATE FUNCTION [dbo].[fn_multi_null_sys_user_wechat_openid](    @value varchar(50))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from sys_user where wechat_openid=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[sys_user] Add constraint unique_multi_null_sys_user_wechat_openid check ([dbo].[fn_multi_null_sys_user_wechat_openid](wechat_openid)=1);
GO
CREATE INDEX [index_sys_user_org_code] ON [dbo].[sys_user]([org_code]);

ALTER TABLE [dbo].[sys_user] ADD CONSTRAINT  [ui_sys_user01] UNIQUE ([id_type],[id_number])
GO
-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_task]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_task]
GO
CREATE TABLE [dbo].[sys_task] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [name] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [description] varchar(300) NULL COLLATE Chinese_PRC_CI_AS,
  [scedule_years] varchar(100) DEFAULT ('*') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [schedule_months] varchar(100) DEFAULT ('*') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [schedule_days] varchar(100) DEFAULT ('*') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [schedule_weeks] varchar(100) DEFAULT ('*') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [schedule_hours] varchar(100) DEFAULT ('*') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [schedule_minutes] varchar(100) DEFAULT ('*') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [class_name] varchar(100) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [mothod_name] varchar(500) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [is_static] bit DEFAULT ((1)) NOT NULL,
  [parames] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [is_running] bit DEFAULT ((1)) NOT NULL
)
GO
ALTER TABLE [dbo].[sys_task] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for sys_route
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_route]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_route]
GO
CREATE TABLE [dbo].[sys_route] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [path] varchar(200) NOT NULL UNIQUE COLLATE Chinese_PRC_CI_AS,
  [name] varchar(100) NOT NULL UNIQUE COLLATE Chinese_PRC_CI_AS,
  [component] varchar(200) DEFAULT ('Layout') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [title] varchar(100) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [breadcrumb_show] bit DEFAULT ((1)) NOT NULL,
  [need_cache] bit DEFAULT ((1)) NOT NULL,
  [tag_view] bit DEFAULT ((1)) NOT NULL
)
GO
ALTER TABLE [dbo].[sys_route] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_role]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_role]
GO
CREATE TABLE [dbo].[sys_role] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [code] varchar(8) NOT NULL UNIQUE COLLATE Chinese_PRC_CI_AS,
  [name] varchar(45) NOT NULL UNIQUE COLLATE Chinese_PRC_CI_AS,
  [note] varchar(64) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[sys_role] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_permission]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_permission]
GO
CREATE TABLE [dbo].[sys_permission] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [role] int NOT NULL,
  [action] int NOT NULL
)
GO
ALTER TABLE [dbo].[sys_permission] SET (LOCK_ESCALATION = TABLE)
GO
CREATE INDEX [index_sys_permission_role] ON [dbo].[sys_permission]([role]);
CREATE INDEX [index_sys_permission_action] ON [dbo].[sys_permission]([action]);

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_org]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_org]
GO
CREATE TABLE [dbo].[sys_org] (
  [org_code] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [name] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [parent] int NULL,
  [phone] varchar(32) NULL COLLATE Chinese_PRC_CI_AS,
  [fax] varchar(32) NULL COLLATE Chinese_PRC_CI_AS,
  [role_list] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [address] varchar(100) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[sys_org] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[sys_org] ADD CONSTRAINT  [ui_sys_org01] UNIQUE ([name],[parent])
GO
-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_message]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_message]
GO
CREATE TABLE [dbo].[sys_message] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [read_time] datetime NULL,
  [create_time] datetime NOT NULL,
  [receiver_name] varchar(50) NULL COLLATE Chinese_PRC_CI_AS,
  [sender_name] varchar(50) NULL COLLATE Chinese_PRC_CI_AS,
  [title] varchar(100) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [content] text NOT NULL COLLATE Chinese_PRC_CI_AS,
  [url] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [receiver] varchar(50) NULL COLLATE Chinese_PRC_CI_AS,
  [sender] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [event_id] varchar(45) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[sys_message] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_menu]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_menu]
GO
CREATE TABLE [dbo].[sys_menu] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [sort_num] int DEFAULT ((10)) NOT NULL,
  [name] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [parent] int NULL,
  [is_divider] bit DEFAULT ((0)) NOT NULL,
  [icon] varchar(50) NULL COLLATE Chinese_PRC_CI_AS,
  [action] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [class_name] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [data_action] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [bigIcon] varchar(50) NULL COLLATE Chinese_PRC_CI_AS,
  [mobile_hidden] bit DEFAULT ((0)) NOT NULL,
  [js_cmd] varchar(45) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[sys_menu] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[sys_menu] ADD CONSTRAINT  [ui_sys_menu012] UNIQUE ([name],[parent],[action])
GO
-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_log]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_log]
GO
CREATE TABLE [dbo].[sys_log] (
  [id] char(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [time] datetime NOT NULL,
  [keyword] varchar(100) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [user_type] varchar(64) NULL COLLATE Chinese_PRC_CI_AS,
  [user_info] varchar(64) NULL COLLATE Chinese_PRC_CI_AS,
  [note] varchar(1000) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[sys_log] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for sys_flow_process
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_flow_process]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_flow_process]
GO
CREATE TABLE [dbo].[sys_flow_process] (
  [id] char(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [flow_id] varchar(36) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [operation] varchar(36) NULL COLLATE Chinese_PRC_CI_AS,
  [selection] varchar(36) NULL COLLATE Chinese_PRC_CI_AS,
  [state_name0] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [state_name1] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [operator] varchar(36) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [time] datetime NOT NULL,
  [note] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [next_operator] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [state0] int NULL,
  [state1] int NOT NULL
)
GO
ALTER TABLE [dbo].[sys_flow_process] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for sys_data_dict
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_data_dict]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_data_dict]
GO
CREATE TABLE [dbo].[sys_data_dict] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [dict] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [code] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [text] varchar(100) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [order_num] int NULL
)
GO
ALTER TABLE [dbo].[sys_data_dict] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[sys_data_dict] ADD CONSTRAINT  [ui_sys_data_dict01] UNIQUE ([dict],[code])
GO
-- ----------------------------
-- Table structure for sys_action
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_action]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_action]
GO
CREATE TABLE [dbo].[sys_action] (
  [note] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [value] varchar(100) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [params] varchar(100) NULL COLLATE Chinese_PRC_CI_AS,
  [method] varchar(45) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[sys_action] SET (LOCK_ESCALATION = TABLE)
GO
CREATE INDEX [index_sys_action_value] ON [dbo].[sys_action]([value]);

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[oauth_client_details]') AND type IN ('U'))
	DROP TABLE [dbo].[oauth_client_details]
GO
CREATE TABLE [dbo].[oauth_client_details] (
  [client_id] varchar(255) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [client_secret] varchar(255) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [scope] varchar(255) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [authorized_grant_types] varchar(255) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [web_server_redirect_uri] varchar(255) NULL COLLATE Chinese_PRC_CI_AS,
  [authorities] varchar(255) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [access_token_validity] int DEFAULT ((86400)) NOT NULL,
  [refresh_token_validity] int DEFAULT ((0)) NOT NULL,
  [autoapprove] varchar(20) DEFAULT ('1') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [expired_date] datetime NULL,
  [locked] bit DEFAULT ((0)) NOT NULL,
  [enabled] bit DEFAULT ((1)) NOT NULL,
  [decription] varchar(200) NULL COLLATE Chinese_PRC_CI_AS,
  [additional_information] text NULL COLLATE Chinese_PRC_CI_AS,
  [resource_ids] varchar(255) NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[oauth_client_details] SET (LOCK_ESCALATION = TABLE)
GO

