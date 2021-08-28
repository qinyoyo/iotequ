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
