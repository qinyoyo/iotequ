-- ----------------------------
-- Table structure for ck_sign_in
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ck_sign_in]') AND type IN ('U'))
	DROP TABLE [dbo].[ck_sign_in]
GO
CREATE TABLE [dbo].[ck_sign_in] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [user_no] varchar(15) NOT NULL,
  [org_code] int NOT NULL,
  [rec_source] varchar(45) NOT NULL,
  [rec_source_type] varchar(45) DEFAULT ('U53') NOT NULL,
  [event_type] int DEFAULT ((1)) NOT NULL,
  [rec_time] datetime NOT NULL
)
GO
ALTER TABLE [dbo].[ck_sign_in] SET (LOCK_ESCALATION = TABLE)
GO
CREATE INDEX [index_ck_sign_in_user_no] ON [dbo].[ck_sign_in]([user_no]);
CREATE INDEX [index_ck_sign_in_org_code] ON [dbo].[ck_sign_in]([org_code]);
CREATE INDEX [index_ck_sign_in_rec_time] ON [dbo].[ck_sign_in]([rec_time]);

-- ----------------------------
-- Table structure for ck_register
-- ----------------------------
IF EXISTS (SELECT * FROM sys.all_objects WHERE object_id = OBJECT_ID('[dbo].[ck_register]') AND type IN ('U'))
	DROP TABLE [dbo].[ck_register]
GO
CREATE TABLE [dbo].[ck_register] (
  [id] char(32) NOT NULL PRIMARY KEY,
  [user_no] varchar(36) NOT NULL,
  [name] varchar(32) NOT NULL,
  [sex] varchar(1) DEFAULT ('1') NOT NULL,
  [birth_date] datetime NULL,
  [org_code] int NOT NULL,
  [org_name] varchar(32) NOT NULL,
  [in_date] datetime NOT NULL,
  [on_time] datetime NOT NULL,
  [off_time] datetime NULL
)
GO
ALTER TABLE [dbo].[ck_register] SET (LOCK_ESCALATION = TABLE)
GO

ALTER TABLE [dbo].[ck_register] ADD CONSTRAINT  [ui_ck_register012] UNIQUE ([user_no],[org_code],[in_date])
GO