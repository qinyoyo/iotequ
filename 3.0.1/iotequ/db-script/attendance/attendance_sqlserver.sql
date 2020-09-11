-- ----------------------------
-- Table structure for ad_special_shift_time
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_special_shift_time]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_special_shift_time]
GO
CREATE TABLE [dbo].[ad_special_shift_time] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [special_shift_id] int NOT NULL,
  [name] varchar(45) NOT NULL,
  [start_time] datetime NOT NULL,
  [end_time] datetime NOT NULL
)
GO
ALTER TABLE [dbo].[ad_special_shift_time] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_shift_time
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_shift_time]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_shift_time]
GO
CREATE TABLE [dbo].[ad_shift_time] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [shift_id] int NOT NULL,
  [name] varchar(100) NOT NULL,
  [week_days] varchar(45) DEFAULT ('1,2,3,4,5') NOT NULL,
  [start_work_time] datetime NOT NULL,
  [end_work_time] datetime NOT NULL
)
GO
ALTER TABLE [dbo].[ad_shift_time] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_org
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_org]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_org]
GO
CREATE TABLE [dbo].[ad_org] (
  [org_code] int NOT NULL PRIMARY KEY,
  [shift_id] int NULL,
  [hr] varchar(36) NULL,
  [manager] varchar(36) NULL,
  [manage_limit] int NULL,
  [deviation] int NULL,
  [float_limit] int NULL,
  [absent_limit] int NULL,
  [free_work_limit] int NULL
)
GO
ALTER TABLE [dbo].[ad_org] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_exception
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_exception]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_exception]
GO
CREATE TABLE [dbo].[ad_exception] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [shift_id] int NOT NULL,
  [end_date] datetime NOT NULL,
  [start_date] datetime NOT NULL,
  [week_day] int NOT NULL,
  [description] varchar(100) NULL
)
GO
ALTER TABLE [dbo].[ad_exception] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_employee
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_employee]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_employee]
GO
CREATE TABLE [dbo].[ad_employee] (
  [id] varchar(36) NOT NULL PRIMARY KEY,
  [employee_no] varchar(32) NOT NULL UNIQUE,
  [em_level] int NULL,
  [is_attendance] bit DEFAULT ((1)) NOT NULL,
  [enter_date] datetime NULL,
  [leave_date] datetime NULL,
  [overtime_minutes] int DEFAULT ((0)) NOT NULL,
  [shift_id] int NULL
)
GO
ALTER TABLE [dbo].[ad_employee] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_approve_list
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_approve_list]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_approve_list]
GO
CREATE TABLE [dbo].[ad_approve_list] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [approver] varchar(36) NULL,
  [approve_time] datetime NOT NULL,
  [state1] int NULL,
  [state0] int NULL,
  [approve_note] varchar(200) NULL,
  [adjust_id] varchar(32) NOT NULL
)
GO
ALTER TABLE [dbo].[ad_approve_list] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_special_shift
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_special_shift]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_special_shift]
GO
CREATE TABLE [dbo].[ad_special_shift] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [shift_mode] int NOT NULL,
  [name] varchar(45) NOT NULL,
  [mode_property] int NULL,
  [start_date] datetime NOT NULL,
  [end_date] datetime NOT NULL,
  [org_codes] varchar(300) NOT NULL,
  [sex_property] int NULL,
  [age_property0] int NULL,
  [age_property1] int NULL,
  [level_property0] int NULL,
  [level_property1] int NULL,
  [description] varchar(45) NULL
)
GO
ALTER TABLE [dbo].[ad_special_shift] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_shift
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_shift]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_shift]
GO
CREATE TABLE [dbo].[ad_shift] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [name] varchar(45) NOT NULL,
  [ad_mode] int DEFAULT ((1)) NOT NULL,
  [start_date] datetime NOT NULL,
  [end_date] datetime NOT NULL,
  [minutes_per_day] int DEFAULT ((480)) NOT NULL,
  [description] varchar(200) NULL
)
GO
ALTER TABLE [dbo].[ad_shift] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_day_result
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_day_result]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_day_result]
GO
CREATE TABLE [dbo].[ad_day_result] (
  [id] int IDENTITY(1,1) NOT NULL PRIMARY KEY,
  [org_code] int NOT NULL,
  [org_name] varchar(100) NOT NULL,
  [employee] varchar(45) NOT NULL,
  [employee_no] varchar(45) NOT NULL,
  [real_name] varchar(45) NOT NULL,
  [ad_date] datetime NOT NULL,
  [shift_name] varchar(45) NOT NULL,
  [state] int NOT NULL,
  [state_name] varchar(45) NOT NULL,
  [times] int DEFAULT ((1)) NOT NULL,
  [minutes] int DEFAULT ((0)) NOT NULL,
  [work_minutes] int DEFAULT ((0)) NOT NULL,
  [shift_id] int DEFAULT ((0)) NOT NULL
)
GO
ALTER TABLE [dbo].[ad_day_result] SET (LOCK_ESCALATION = TABLE)
GO

-- ----------------------------
-- Table structure for ad_data
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_data]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_data]
GO
CREATE TABLE [dbo].[ad_data] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [employee_no] varchar(45) NOT NULL,
  [rec_source_type] varchar(45) DEFAULT ('D10') NOT NULL,
  [rec_source] varchar(45) NULL,
  [rec_type] int DEFAULT ((3)) NOT NULL,
  [rec_time] datetime NOT NULL,
  [is_used] bit DEFAULT ('0') NOT NULL
)
GO
ALTER TABLE [dbo].[ad_data] SET (LOCK_ESCALATION = TABLE)
GO
CREATE INDEX [index_ad_data_employee_no] ON [dbo].[ad_data]([employee_no]);

-- ----------------------------
-- Table structure for ad_adjust
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ad_adjust]') AND type IN ('U'))
	DROP TABLE [dbo].[ad_adjust]
GO
CREATE TABLE [dbo].[ad_adjust] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [employee] varchar(36) NOT NULL,
  [org_code] int NOT NULL,
  [hr] varchar(45) NULL,
  [adjust_type] int DEFAULT ((10)) NULL,
  [state] int NOT NULL,
  [register_time] datetime NOT NULL,
  [start_time] datetime NULL,
  [end_time] datetime NULL,
  [description] varchar(500) NOT NULL,
  [approver] varchar(36) NULL,
  [approve_org] int NULL,
  [add_file] varchar(200) NULL
)
GO
ALTER TABLE [dbo].[ad_adjust] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[ad_special_shift_time] ADD CONSTRAINT [dbo].[fk_ad_special_shift_time_special_shift_id_ad_special_shift_id] FOREIGN KEY([special_shift_id]) REFERENCES [dbo].[ad_special_shift]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[ad_shift_time] ADD CONSTRAINT [dbo].[fk_ad_shift_time_shift_id_ad_shift_id] FOREIGN KEY([shift_id]) REFERENCES [dbo].[ad_shift]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[ad_org] ADD CONSTRAINT [dbo].[fk_ad_org_org_code_sys_org_org_code] FOREIGN KEY([org_code]) REFERENCES [dbo].[sys_org]([org_code]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[ad_exception] ADD CONSTRAINT [dbo].[fk_ad_exception_shift_id_ad_shift_id] FOREIGN KEY([shift_id]) REFERENCES [dbo].[ad_shift]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[ad_employee] ADD CONSTRAINT [dbo].[fk_ad_employee_id_sys_user_id] FOREIGN KEY([id]) REFERENCES [dbo].[sys_user]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE [dbo].[ad_approve_list] ADD CONSTRAINT [dbo].[fk_ad_approve_list_adjust_id_ad_adjust_id] FOREIGN KEY([adjust_id]) REFERENCES [dbo].[ad_adjust]([id]) ON DELETE CASCADE ON UPDATE CASCADE;
