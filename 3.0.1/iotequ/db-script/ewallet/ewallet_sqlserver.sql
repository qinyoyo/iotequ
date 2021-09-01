-- ----------------------------
-- Table structure for ew_user_time
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ew_user_time]') AND type IN ('U'))
	DROP TABLE [dbo].[ew_user_time]
GO
CREATE TABLE [dbo].[ew_user_time] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [user_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [time_id] int NOT NULL,
  [amount_time] int DEFAULT ((0)) NOT NULL,
  [check_code] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [cost_limit] int DEFAULT ((0)) NOT NULL,
  [day_limit] int DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[ew_user_time] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[ew_user_time] ADD CONSTRAINT  [ui_ew_user_time01] UNIQUE ([user_no],[time_id])
GO
-- ----------------------------
-- Table structure for ew_user_count
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ew_user_count]') AND type IN ('U'))
	DROP TABLE [dbo].[ew_user_count]
GO
CREATE TABLE [dbo].[ew_user_count] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [user_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [count_id] int NOT NULL,
  [amount_count] int DEFAULT ((0)) NOT NULL,
  [check_code] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [cost_limit] int DEFAULT ((0)) NOT NULL,
  [day_limit] int DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[ew_user_count] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[ew_user_count] ADD CONSTRAINT  [ui_ew_user_count01] UNIQUE ([user_no],[count_id])
GO
-- ----------------------------
-- Table structure for ew_user
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ew_user]') AND type IN ('U'))
	DROP TABLE [dbo].[ew_user]
GO
CREATE TABLE [dbo].[ew_user] (
  [user_no] varchar(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [is_active] bit DEFAULT ((1)) NULL,
  [name] varchar(32) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [gender] int NOT NULL,
  [id_type] int NOT NULL,
  [id_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [mobile_phone] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [email] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [wechat_openid] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [birth_date] datetime NULL,
  [member_group] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [bonus_point] int DEFAULT ((0)) NOT NULL,
  [amount_money] int DEFAULT ((0)) NOT NULL,
  [cost_limit] int DEFAULT ((0)) NOT NULL,
  [day_limit] int DEFAULT ((0)) NOT NULL,
  [active_since] datetime NULL,
  [expire_at] datetime NULL,
  [check_code] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[ew_user] SET (LOCK_ESCALATION = TABLE)
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_ew_user_mobile_phone]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_ew_user_mobile_phone]
GO
CREATE FUNCTION [dbo].[fn_multi_null_ew_user_mobile_phone](    @value varchar(45))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from ew_user where mobile_phone=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[ew_user] Add constraint unique_multi_null_ew_user_mobile_phone check ([dbo].[fn_multi_null_ew_user_mobile_phone](mobile_phone)=1);
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_ew_user_email]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_ew_user_email]
GO
CREATE FUNCTION [dbo].[fn_multi_null_ew_user_email](    @value varchar(45))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from ew_user where email=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[ew_user] Add constraint unique_multi_null_ew_user_email check ([dbo].[fn_multi_null_ew_user_email](email)=1);
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_ew_user_wechat_openid]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_ew_user_wechat_openid]
GO
CREATE FUNCTION [dbo].[fn_multi_null_ew_user_wechat_openid](    @value varchar(45))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from ew_user where wechat_openid=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[ew_user] Add constraint unique_multi_null_ew_user_wechat_openid check ([dbo].[fn_multi_null_ew_user_wechat_openid](wechat_openid)=1);
GO

-- ----------------------------
-- Table structure for ew_time_project
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ew_time_project]') AND type IN ('U'))
	DROP TABLE [dbo].[ew_time_project]
GO
CREATE TABLE [dbo].[ew_time_project] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [icon] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [name] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [base_value] int DEFAULT ((60)) NOT NULL,
  [base_price] int DEFAULT ((10)) NOT NULL,
  [bonus_point] double(12:2) NOT NULL,
  [start_time] datetime NULL,
  [end_time] datetime NULL
)
GO
ALTER TABLE [dbo].[ew_time_project] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ew_device
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ew_device]') AND type IN ('U'))
	DROP TABLE [dbo].[ew_device]
GO
CREATE TABLE [dbo].[ew_device] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [device_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [shop_id] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [privilege_list] varchar(64) NOT NULL COLLATE Chinese_PRC_CI_AS
)
GO
ALTER TABLE [dbo].[ew_device] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ew_count_project
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ew_count_project]') AND type IN ('U'))
	DROP TABLE [dbo].[ew_count_project]
GO
CREATE TABLE [dbo].[ew_count_project] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [icon] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [name] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [base_price] int DEFAULT ((10)) NOT NULL,
  [base_value] int DEFAULT ((1)) NOT NULL,
  [bonus_point] double(12:2) NOT NULL,
  [start_time] datetime NULL,
  [end_time] datetime NULL
)
GO
ALTER TABLE [dbo].[ew_count_project] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ew_bill
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ew_bill]') AND type IN ('U'))
	DROP TABLE [dbo].[ew_bill]
GO
CREATE TABLE [dbo].[ew_bill] (
  [no] varchar(32) NOT NULL PRIMARY KEY COLLATE Chinese_PRC_CI_AS,
  [canceled] bit DEFAULT ((0)) NOT NULL,
  [is_charge] bit DEFAULT ((0)) NOT NULL,
  [user_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [batch_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [dt] datetime NOT NULL,
  [operation_type] int NOT NULL,
  [cost_type] int DEFAULT ((1)) NOT NULL,
  [project_id] int DEFAULT ((0)) NOT NULL,
  [project_name] varchar(45) DEFAULT ('消费') NOT NULL COLLATE Chinese_PRC_CI_AS,
  [project_price] int NOT NULL,
  [amount] int NOT NULL,
  [amount_before] int NOT NULL,
  [bonus] int NOT NULL,
  [bonus_before] int NOT NULL,
  [device_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [shop_id] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [device_stream] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [device_dt] datetime NOT NULL,
  [trade_no] varchar(45) NOT NULL UNIQUE COLLATE Chinese_PRC_CI_AS,
  [operator_no] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [check_code] varchar(45) NOT NULL COLLATE Chinese_PRC_CI_AS,
  [link_no] varchar(45) NULL COLLATE Chinese_PRC_CI_AS,
  [login_id] int NOT NULL
)
GO
ALTER TABLE [dbo].[ew_bill] SET (LOCK_ESCALATION = TABLE)
GO
CREATE INDEX [index_ew_bill_is_charge] ON [dbo].[ew_bill]([is_charge]);
CREATE INDEX [index_ew_bill_user_no] ON [dbo].[ew_bill]([user_no]);
CREATE INDEX [index_ew_bill_dt] ON [dbo].[ew_bill]([dt]);
CREATE INDEX [index_ew_bill_cost_type] ON [dbo].[ew_bill]([cost_type]);
CREATE INDEX [index_ew_bill_project_id] ON [dbo].[ew_bill]([project_id]);
CREATE INDEX [index_ew_bill_shop_id] ON [dbo].[ew_bill]([shop_id]);
CREATE INDEX [index_ew_bill_operator_no] ON [dbo].[ew_bill]([operator_no]);
CREATE INDEX [index_ew_bill_login_id] ON [dbo].[ew_bill]([login_id]);

ALTER TABLE [dbo].[ew_user_time] ADD CONSTRAINT [dbo].[fk_ew_user_time_user_no_ew_user_user_no] FOREIGN KEY([user_no]) REFERENCES [dbo].[ew_user]([user_no]) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE [dbo].[ew_user_time] ADD CONSTRAINT [dbo].[fk_ew_user_time_time_id_ew_time_project_id] FOREIGN KEY([time_id]) REFERENCES [dbo].[ew_time_project]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE [dbo].[ew_user_count] ADD CONSTRAINT [dbo].[fk_ew_user_count_user_no_ew_user_user_no] FOREIGN KEY([user_no]) REFERENCES [dbo].[ew_user]([user_no]) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE [dbo].[ew_user_count] ADD CONSTRAINT [dbo].[fk_ew_user_count_count_id_ew_count_project_id] FOREIGN KEY([count_id]) REFERENCES [dbo].[ew_count_project]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;
