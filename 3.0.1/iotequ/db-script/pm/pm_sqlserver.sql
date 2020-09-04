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

