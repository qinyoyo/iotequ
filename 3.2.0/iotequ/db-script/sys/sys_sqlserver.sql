-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_user]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_user]
GO
CREATE TABLE [dbo].[sys_user] (
  [id] varchar(32) NOT NULL PRIMARY KEY,
  [icon] text NULL,
  [name] varchar(32) NOT NULL UNIQUE,
  [real_name] varchar(32) NOT NULL,
  [sex] varchar(1) DEFAULT ('1') NULL,
  [birth_date] datetime NULL,
  [reg_time] datetime NULL,
  [mobile_phone] varchar(32) NULL,
  [email] varchar(50) NULL,
  [wechat_openid] varchar(50) NULL,
  [org_code] int NULL,
  [employee_no] varchar(32) NULL,
  [org_privilege] int NULL,
  [role_list] varchar(200) NULL,
  [locked] bit DEFAULT ((0)) NOT NULL,
  [state] bit DEFAULT ((1)) NOT NULL,
  [id_type] int DEFAULT ((1)) NOT NULL,
  [id_number] varchar(45) NOT NULL,
  [expired_time] datetime NULL,
  [password_expired_time] datetime NULL,
  [password] varchar(32) DEFAULT ('96e79218965eb72c92a549dd5a330112') NOT NULL,
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
			select @ct=count(*) from sys_user where employee_no=@value
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
			select @ct=count(*) from sys_user where employee_no=@value
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
			select @ct=count(*) from sys_user where employee_no=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[sys_user] Add constraint unique_multi_null_sys_user_wechat_openid check ([dbo].[fn_multi_null_sys_user_wechat_openid](wechat_openid)=1);
GO
CREATE INDEX [index_sys_user_org_code] ON [dbo].[sys_user]([org_code]);
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_sys_user_employee_no]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_sys_user_employee_no]
GO
CREATE FUNCTION [dbo].[fn_multi_null_sys_user_employee_no](    @value varchar(32))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from sys_user where employee_no=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[sys_user] Add constraint unique_multi_null_sys_user_employee_no check ([dbo].[fn_multi_null_sys_user_employee_no](employee_no)=1);
GO

ALTER TABLE [dbo].[sys_user] ADD CONSTRAINT  [ui_sys_user01] UNIQUE ([id_type],[id_number])
GO
-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_user]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_user]
GO
CREATE TABLE [dbo].[sys_user] (
  [id] varchar(32) NOT NULL PRIMARY KEY,
  [name] varchar(32) NOT NULL UNIQUE,
  [real_name] varchar(32) NOT NULL,
  [birth_date] datetime NULL,
  [org_code] int NULL,
  [employee_no] varchar(32) NULL,
  [mobile_phone] varchar(32) NULL,
  [sex] varchar(1) DEFAULT ('1') NULL,
  [locked] bit DEFAULT ((0)) NOT NULL,
  [state] bit DEFAULT ((1)) NOT NULL,
  [id_type] int DEFAULT ((1)) NOT NULL,
  [id_number] varchar(45) NOT NULL,
  [email] varchar(50) NULL,
  [reg_time] datetime NULL,
  [role_list] varchar(200) NULL,
  [org_privilege] int NULL,
  [expired_time] datetime NULL,
  [password_expired_time] datetime NULL,
  [icon] text NULL,
  [password] varchar(32) DEFAULT ('96e79218965eb72c92a549dd5a330112') NOT NULL,
  [wechat_openid] varchar(50) NULL,
  [password_error_times] int DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[sys_user] SET (LOCK_ESCALATION = TABLE)
GO
CREATE INDEX [index_sys_user_org_code] ON [dbo].[sys_user]([org_code]);
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_sys_user_employee_no]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_sys_user_employee_no]
GO
CREATE FUNCTION [dbo].[fn_multi_null_sys_user_employee_no](    @value varchar(32))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from sys_user where employee_no=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[sys_user] Add constraint unique_multi_null_sys_user_employee_no check ([dbo].[fn_multi_null_sys_user_employee_no](employee_no)=1);
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
			select @ct=count(*) from sys_user where employee_no=@value
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
			select @ct=count(*) from sys_user where employee_no=@value
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
			select @ct=count(*) from sys_user where employee_no=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[sys_user] Add constraint unique_multi_null_sys_user_wechat_openid check ([dbo].[fn_multi_null_sys_user_wechat_openid](wechat_openid)=1);
GO

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
  [name] varchar(45) NOT NULL,
  [description] varchar(300) NULL,
  [scedule_years] varchar(100) DEFAULT ('*') NOT NULL,
  [schedule_months] varchar(100) DEFAULT ('*') NOT NULL,
  [schedule_days] varchar(100) DEFAULT ('*') NOT NULL,
  [schedule_weeks] varchar(100) DEFAULT ('*') NOT NULL,
  [schedule_hours] varchar(100) DEFAULT ('*') NOT NULL,
  [schedule_minutes] varchar(100) DEFAULT ('*') NOT NULL,
  [class_name] varchar(100) NOT NULL,
  [mothod_name] varchar(500) NOT NULL,
  [is_static] bit DEFAULT ((1)) NOT NULL,
  [parames] varchar(100) NULL,
  [is_running] bit DEFAULT ((1)) NOT NULL
)
GO
ALTER TABLE [dbo].[sys_task] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[sys_role]') AND type IN ('U'))
	DROP TABLE [dbo].[sys_role]
GO
CREATE TABLE [dbo].[sys_role] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [code] varchar(8) NOT NULL UNIQUE,
  [name] varchar(45) NOT NULL UNIQUE,
  [note] varchar(64) NULL
)
GO
ALTER TABLE [dbo].[sys_role] SET (LOCK_ESCALATION = TABLE)
GO

