-- ----------------------------
-- Table structure for pay_pos
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pay_pos]') AND type IN ('U'))
	DROP TABLE [dbo].[pay_pos]
GO
CREATE TABLE [dbo].[pay_pos] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [no] varchar(45) NOT NULL UNIQUE,
  [shop_id] int NOT NULL,
  [security_code] varchar(45) NULL,
  [work_code] varchar(45) NULL,
  [login_id] int NULL,
  [ewallet_active] bit DEFAULT ((1)) NOT NULL,
  [count_project_list] varchar(200) NULL,
  [time_project_list] varchar(200) NULL
)
GO
ALTER TABLE [dbo].[pay_pos] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for pay_operator
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pay_operator]') AND type IN ('U'))
	DROP TABLE [dbo].[pay_operator]
GO
CREATE TABLE [dbo].[pay_operator] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [name] varchar(45) NOT NULL UNIQUE,
  [real_name] varchar(45) NOT NULL,
  [password] varchar(45) NOT NULL,
  [shop_id] int NOT NULL,
  [user_no] varchar(45) NULL
)
GO
ALTER TABLE [dbo].[pay_operator] SET (LOCK_ESCALATION = TABLE)
GO
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[fn_multi_null_pay_operator_user_no]') AND type IN ('FN'))
	DROP FUNCTION [dbo].[fn_multi_null_pay_operator_user_no]
GO
CREATE FUNCTION [dbo].[fn_multi_null_pay_operator_user_no](    @value varchar(45))
RETURNS bit
AS
BEGIN
  DECLARE @result bit
	DECLARE @ct  int
	IF @value is null  or @value =''
		SET @result=1
	ELSE
	  BEGIN
			select @ct=count(*) from pay_operator where user_no=@value
			select @result = case when @ct>1 then 0 else 1 end
		END	
  RETURN @result
END
GO
Alter table [dbo].[pay_operator] Add constraint unique_multi_null_pay_operator_user_no check ([dbo].[fn_multi_null_pay_operator_user_no](user_no)=1);
GO

-- ----------------------------
-- Table structure for pay_shop
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pay_shop]') AND type IN ('U'))
	DROP TABLE [dbo].[pay_shop]
GO
CREATE TABLE [dbo].[pay_shop] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [corporation_id] int NOT NULL,
  [name] varchar(45) NOT NULL,
  [linkman] varchar(45) NOT NULL,
  [link_phone] varchar(45) NOT NULL,
  [address] varchar(45) NOT NULL
)
GO
ALTER TABLE [dbo].[pay_shop] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for pay_login
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pay_login]') AND type IN ('U'))
	DROP TABLE [dbo].[pay_login]
GO
CREATE TABLE [dbo].[pay_login] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [pos_id] int NOT NULL,
  [shop_id] int NOT NULL,
  [operator_id] int NOT NULL,
  [batch_no] varchar(45) NOT NULL,
  [login_time] datetime NOT NULL,
  [logout_time] datetime NULL,
  [device_stream] varchar(45) NULL,
  [random_no] varchar(45) NOT NULL,
  [app_version] varchar(45) NOT NULL,
  [trade_count] int DEFAULT ((0)) NOT NULL,
  [failure_count] int DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[pay_login] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for pay_corporation
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[pay_corporation]') AND type IN ('U'))
	DROP TABLE [dbo].[pay_corporation]
GO
CREATE TABLE [dbo].[pay_corporation] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [parent_id] int NULL,
  [name] varchar(45) NOT NULL,
  [address] varchar(45) NOT NULL,
  [linkman] varchar(45) NOT NULL,
  [linkphone] varchar(45) NOT NULL
)
GO
ALTER TABLE [dbo].[pay_corporation] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[pay_pos] ADD CONSTRAINT [dbo].[fk_pay_pos_shop_id_pay_shop_id] FOREIGN KEY([shop_id]) REFERENCES [dbo].[pay_shop]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE [dbo].[pay_operator] ADD CONSTRAINT [dbo].[fk_pay_operator_shop_id_pay_shop_id] FOREIGN KEY([shop_id]) REFERENCES [dbo].[pay_shop]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE [dbo].[pay_shop] ADD CONSTRAINT [dbo].[fk_pay_shop_corporation_id_pay_corporation_id] FOREIGN KEY([corporation_id]) REFERENCES [dbo].[pay_corporation]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;
