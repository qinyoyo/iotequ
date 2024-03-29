-- ----------------------------
-- Table structure for cg_list_field
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_LIST_FIELD','IOTEQU');
CREATE TABLE "IOTEQU"."CG_LIST_FIELD" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "LIST_ID" VARCHAR2(64 BYTE) NOT NULL,
  "ORDER_NUM" INTEGER NOT NULL,
  "ENTITY_FIELD" VARCHAR2(45 BYTE) NOT NULL,
  "QUERY_MODE" INTEGER DEFAULT 0 NOT NULL,
  "FIX" NUMBER(1) DEFAULT 0 NOT NULL,
  "EXPAND" NUMBER(1) DEFAULT 0 NOT NULL,
  "OVERFLOW_TOOLTIP" NUMBER(1) DEFAULT 1 NOT NULL,
  "ALIGN" VARCHAR2(45 BYTE) DEFAULT 'left' NOT NULL,
  "HEADER_ALIGN" VARCHAR2(45 BYTE) NULL,
  "WIDTH" INTEGER DEFAULT 128 NOT NULL,
  "COLUMN_PROPERTIES" CLOB NULL,
  "HIDDEN" NUMBER(1) DEFAULT 0 NOT NULL,
  "EDIT_INLINE" NUMBER(1) DEFAULT 0 NOT NULL,
  "DEFAULT_QUERY_VALUE" VARCHAR2(200 BYTE) NULL,
  "CELL_DISPLAY_SLOT" VARCHAR2(500 BYTE) NULL,
  "SHOW_TYPE" VARCHAR2(45 BYTE) NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_CG_LIST_FIELD','IOTEQU');
CREATE SEQUENCE SEQUENCE_CG_LIST_FIELD;

ALTER TABLE "IOTEQU"."CG_LIST_FIELD" ADD CONSTRAINT  "UI_CG_LIST_FIELD01" UNIQUE ("LIST_ID","ENTITY_FIELD");
-- ----------------------------
-- Table structure for cg_list
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_LIST','IOTEQU');
CREATE TABLE "IOTEQU"."CG_LIST" (
  "ID" VARCHAR2(64 BYTE) NOT NULL PRIMARY KEY,
  "NAME" VARCHAR2(50 BYTE) NOT NULL,
  "PATH" VARCHAR2(45 BYTE) DEFAULT 'list' NOT NULL,
  "TABLE_ID" VARCHAR2(32 BYTE) NOT NULL,
  "ICON" VARCHAR2(45 BYTE) NULL,
  "HEAD_TITLE" VARCHAR2(64 BYTE) NULL,
  "TAG_TITLE" VARCHAR2(45 BYTE) NULL,
  "EDIT_INLINE" NUMBER(1) NOT NULL,
  "DETAIL_INLINE" NUMBER(1) NOT NULL,
  "SONS" VARCHAR2(100 BYTE) NULL,
  "SON_FIELDS" VARCHAR2(100 BYTE) NULL,
  "SON_ALIGN" VARCHAR2(1 BYTE) DEFAULT 'v' NOT NULL,
  "GENERATOR_TYPE" INTEGER NOT NULL,
  "TITLE_FIELD" VARCHAR2(45 BYTE) NULL,
  "PARENT_ENTITY" VARCHAR2(45 BYTE) NULL,
  "TREE_SHOW_ENTITY" VARCHAR2(45 BYTE) NULL,
  "SHOW_SEARCH" NUMBER(1) NOT NULL,
  "TOOLBAR_MODE" INTEGER DEFAULT 2 NOT NULL,
  "TABLE_HEIGHT" INTEGER DEFAULT 0 NOT NULL,
  "PAGINATION" NUMBER(1) NOT NULL,
  "ORDER_BY" VARCHAR2(100 BYTE) NULL,
  "SORT_FIELD" VARCHAR2(45 BYTE) NULL,
  "STRIPE" NUMBER(1) DEFAULT 1 NOT NULL,
  "BORDER" NUMBER(1) DEFAULT 0 NOT NULL,
  "STATE_ENTITY" VARCHAR2(45 BYTE) NULL,
  "MAX_HEIGHT" INTEGER DEFAULT 0 NULL,
  "HIGHLIGHT_CURRENT_ROW" NUMBER(1) DEFAULT 1 NOT NULL,
  "MULTIPLE" NUMBER(1) DEFAULT 0 NOT NULL,
  "SHOW_SUMMARY" NUMBER(1) DEFAULT 0 NULL,
  "SPAN_ENTITIES" VARCHAR2(200 BYTE) NULL,
  "SHOW_HEADER" NUMBER(1) DEFAULT 1 NOT NULL,
  "IMAGES" VARCHAR2(200 BYTE) NULL,
  "VIEW_PROPERTIES" CLOB NULL,
  "TABLE_PROPERTIES" CLOB NULL,
  "SONS_PROPERTIES" CLOB NULL,
  "ACTION_LIST" VARCHAR2(200 BYTE) NULL,
  "FLOW_DATA_URL" VARCHAR2(200 BYTE) NULL,
  "LOCAL_EXPORT" NUMBER(1) DEFAULT 0 NOT NULL
);

ALTER TABLE "IOTEQU"."CG_LIST" ADD CONSTRAINT  "UI_CG_LIST01" UNIQUE ("PATH","TABLE_ID");
-- ----------------------------
-- Table structure for cg_form_field
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_FORM_FIELD','IOTEQU');
CREATE TABLE "IOTEQU"."CG_FORM_FIELD" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "FORM_ID" VARCHAR2(64 BYTE) NOT NULL,
  "ORDER_NUM" INTEGER NOT NULL,
  "ENTITY_FIELD" VARCHAR2(45 BYTE) NOT NULL,
  "WIDTH" INTEGER DEFAULT 24 NOT NULL,
  "GROUP_TITLE" VARCHAR2(45 BYTE) NULL,
  "ITEM_PROPERTIES" CLOB NULL,
  "FORM_ITEM_PROPERTIES" CLOB NULL,
  "READONLY" NUMBER(1) DEFAULT 0 NOT NULL,
  "MUST_INPUT" NUMBER(1) DEFAULT 0 NOT NULL,
  "ICON" VARCHAR2(64 BYTE) NULL,
  "HREF" VARCHAR2(64 BYTE) NULL,
  "HIDDEN" NUMBER(1) DEFAULT 0 NOT NULL,
  "VALIDATE_AS_TITLE" NUMBER(1) DEFAULT 0 NOT NULL,
  "SLOT_TEMPLATES" CLOB NULL,
  "SHOW_TYPE" VARCHAR2(45 BYTE) NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_CG_FORM_FIELD','IOTEQU');
CREATE SEQUENCE SEQUENCE_CG_FORM_FIELD;

ALTER TABLE "IOTEQU"."CG_FORM_FIELD" ADD CONSTRAINT  "UI_CG_FORM_FIELD01" UNIQUE ("FORM_ID","ENTITY_FIELD");
-- ----------------------------
-- Table structure for cg_form
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_FORM','IOTEQU');
CREATE TABLE "IOTEQU"."CG_FORM" (
  "ID" VARCHAR2(64 BYTE) NOT NULL PRIMARY KEY,
  "NAME" VARCHAR2(45 BYTE) NOT NULL,
  "PATH" VARCHAR2(200 BYTE) DEFAULT 'record' NOT NULL,
  "TABLE_ID" VARCHAR2(32 BYTE) NOT NULL,
  "HEAD_TITLE" VARCHAR2(400 BYTE) NULL,
  "IS_FLOW" NUMBER(1) DEFAULT 0 NOT NULL,
  "ICON" VARCHAR2(300 BYTE) NULL,
  "TAG_TITLE" VARCHAR2(45 BYTE) NOT NULL,
  "LABEL_POSITION" VARCHAR2(45 BYTE) DEFAULT 'top' NOT NULL,
  "IS_DIALOG" NUMBER(1) DEFAULT 0 NOT NULL,
  "CONTINUE_ADD" NUMBER(1) DEFAULT 1 NOT NULL,
  "IMAGES" VARCHAR2(200 BYTE) NULL,
  "VIEW_PROPERTIES" CLOB NULL,
  "FORM_PROPERTIES" CLOB NULL,
  "SLOT_TEMPLATES" CLOB NULL,
  "ACTION_LIST" VARCHAR2(200 BYTE) NULL
);

ALTER TABLE "IOTEQU"."CG_FORM" ADD CONSTRAINT  "UI_CG_FORM01" UNIQUE ("PATH","TABLE_ID");
-- ----------------------------
-- Table structure for cg_field
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_FIELD','IOTEQU');
CREATE TABLE "IOTEQU"."CG_FIELD" (
  "ID" CHAR(32) NOT NULL PRIMARY KEY,
  "TABLE_ID" VARCHAR2(32 BYTE) NOT NULL,
  "ORDER_NUM" INTEGER NOT NULL,
  "ENTITY_NAME" VARCHAR2(45 BYTE) NOT NULL,
  "TITLE" VARCHAR2(32 BYTE) NOT NULL,
  "NAME" VARCHAR2(32 BYTE) NULL,
  "KEY_TYPE" VARCHAR2(2 BYTE) DEFAULT '0' NOT NULL,
  "DEFAULT_VALUE" VARCHAR2(200 BYTE) NULL,
  "SHOW_TYPE" VARCHAR2(30 BYTE) DEFAULT 'text' NOT NULL,
  "FORMATTER" VARCHAR2(100 BYTE) NULL,
  "IS_NULL" VARCHAR2(5 BYTE) DEFAULT '0' NOT NULL,
  "VALID_EXPRESSION" VARCHAR2(200 BYTE) NULL,
  "VALID_TITLE" VARCHAR2(1000 BYTE) NULL,
  "DICT_TABLE" VARCHAR2(200 BYTE) NULL,
  "DICT_FIELD" VARCHAR2(1000 BYTE) NULL,
  "DICT_MULTIPLE" NUMBER(1) DEFAULT 0 NOT NULL,
  "DICT_TEXT" VARCHAR2(500 BYTE) NULL,
  "DYNA_CONDITION" VARCHAR2(200 BYTE) NULL,
  "DICT_FULL_NAME" NUMBER(1) DEFAULT 0 NOT NULL,
  "DICT_PARENT" VARCHAR2(100 BYTE) NULL,
  "DICT_PARENT_FIELD" VARCHAR2(100 BYTE) NULL,
  "TYPE" VARCHAR2(32 BYTE) DEFAULT 'varchar' NOT NULL,
  "LENGTH" INTEGER DEFAULT 36 NULL,
  "NUMERIC_PRECISION" INTEGER NULL,
  "NUMERIC_SCALE" INTEGER NULL,
  "FK_TABLE" VARCHAR2(45 BYTE) NULL,
  "FK_COLUMN" VARCHAR2(45 BYTE) NULL,
  "FK_ON_DELETE" VARCHAR2(45 BYTE) NULL,
  "FK_ON_UPDATE" VARCHAR2(45 BYTE) NULL,
  "DEFAULT_QUERY_VALUE" VARCHAR2(45 BYTE) NULL
);

ALTER TABLE "IOTEQU"."CG_FIELD" ADD CONSTRAINT  "UI_CG_FIELD01" UNIQUE ("TABLE_ID","ENTITY_NAME");
-- ----------------------------
-- Table structure for cg_button
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_BUTTON','IOTEQU');
CREATE TABLE "IOTEQU"."CG_BUTTON" (
  "ID" CHAR(32) NOT NULL PRIMARY KEY,
  "TABLE_ID" VARCHAR2(32 BYTE) NOT NULL,
  "ORDER_NUM" INTEGER NOT NULL,
  "ACTION" VARCHAR2(45 BYTE) NOT NULL,
  "TITLE" VARCHAR2(45 BYTE) NOT NULL,
  "ICON" VARCHAR2(45 BYTE) NOT NULL,
  "APPEND_CLASS" VARCHAR2(200 BYTE) NULL,
  "ACTION_PROPERTY" VARCHAR2(45 BYTE) NOT NULL,
  "ROW_PROPERTY" VARCHAR2(45 BYTE) NOT NULL,
  "DISPLAY_PROPERTIES" VARCHAR2(45 BYTE) NULL,
  "CONFIRM_TEXT" VARCHAR2(100 BYTE) NULL,
  "IS_REFRESH_LIST" NUMBER(1) NOT NULL
);

ALTER TABLE "IOTEQU"."CG_BUTTON" ADD CONSTRAINT  "UI_CG_BUTTON01" UNIQUE ("TABLE_ID","ACTION");
-- ----------------------------
-- Table structure for cg_table
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_TABLE','IOTEQU');
CREATE TABLE "IOTEQU"."CG_TABLE" (
  "ID" VARCHAR2(32 BYTE) NOT NULL PRIMARY KEY,
  "PROJECT_ID" VARCHAR2(36 BYTE) NOT NULL,
  "CODE" VARCHAR2(45 BYTE) NOT NULL,
  "NAME" VARCHAR2(32 BYTE) NULL,
  "TITLE" VARCHAR2(32 BYTE) NOT NULL,
  "SUB_PACKAGE" VARCHAR2(30 BYTE) NULL,
  "ENTITY" VARCHAR2(32 BYTE) NULL,
  "TEMPLATE" VARCHAR2(32 BYTE) NOT NULL,
  "TRIAL_LICENCE" INTEGER NULL,
  "TRIAL_DAYS" INTEGER NULL,
  "ACTION_LIST" VARCHAR2(200 BYTE) DEFAULT 'add,view,edit,delete,batdel,impo' NULL,
  "IMPORTS" VARCHAR2(500 BYTE) NULL,
  "CONTROLLER_EXTENDS" VARCHAR2(200 BYTE) NULL,
  "POJO_IMPORTS" VARCHAR2(500 BYTE) NULL,
  "POJO_EXTENDS" VARCHAR2(200 BYTE) NULL,
  "POJO_JAVA_CODE" CLOB NULL,
  "CREATOR" VARCHAR2(45 BYTE) NOT NULL,
  "FLOW_DYNA_FIELDS_OP" VARCHAR2(100 BYTE) NULL,
  "FLOW_DYNA_FIELDS_CP" VARCHAR2(100 BYTE) NULL
);

ALTER TABLE "IOTEQU"."CG_TABLE" ADD CONSTRAINT  "UI_CG_TABLE01" UNIQUE ("CODE","PROJECT_ID");
-- ----------------------------
-- Table structure for cg_project
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CG_PROJECT','IOTEQU');
CREATE TABLE "IOTEQU"."CG_PROJECT" (
  "ID" VARCHAR2(36 BYTE) NOT NULL PRIMARY KEY,
  "CODE" VARCHAR2(10 BYTE) NOT NULL,
  "CREATOR" VARCHAR2(36 BYTE) NOT NULL,
  "GROUP_ID" VARCHAR2(45 BYTE) DEFAULT 'top.iotequ' NOT NULL,
  "NAME" VARCHAR2(36 BYTE) NOT NULL,
  "VERSION" VARCHAR2(36 BYTE) NOT NULL,
  "NOTE" VARCHAR2(100 BYTE) NULL,
  "MODULES" VARCHAR2(200 BYTE) DEFAULT 'framework' NULL,
  "SPRING_MODULE" NUMBER(1) DEFAULT 0 NOT NULL,
  "MAVEN_MODULE" NUMBER(1) DEFAULT 1 NOT NULL,
  "MAVEN_SERVER" VARCHAR2(100 BYTE) NULL,
  "ADDTIONAL_DEPENDENCIES" CLOB NULL,
  "TEST" NUMBER(1) DEFAULT 0 NOT NULL
);

ALTER TABLE "IOTEQU"."CG_PROJECT" ADD CONSTRAINT  "UI_CG_PROJECT01" UNIQUE ("CODE","CREATOR");
ALTER TABLE "IOTEQU"."CG_LIST_FIELD" ADD CONSTRAINT "IOTEQU"."FK_CG_LIST_FIELD_LIST_ID_CG_LIST_ID" FOREIGN KEY("LIST_ID") REFERENCES "IOTEQU"."CG_LIST"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "IOTEQU"."CG_LIST" ADD CONSTRAINT "IOTEQU"."FK_CG_LIST_TABLE_ID_CG_TABLE_ID" FOREIGN KEY("TABLE_ID") REFERENCES "IOTEQU"."CG_TABLE"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "IOTEQU"."CG_FORM_FIELD" ADD CONSTRAINT "IOTEQU"."FK_CG_FORM_FIELD_FORM_ID_CG_FORM_ID" FOREIGN KEY("FORM_ID") REFERENCES "IOTEQU"."CG_FORM"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "IOTEQU"."CG_FORM" ADD CONSTRAINT "IOTEQU"."FK_CG_FORM_TABLE_ID_CG_TABLE_ID" FOREIGN KEY("TABLE_ID") REFERENCES "IOTEQU"."CG_TABLE"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "IOTEQU"."CG_FIELD" ADD CONSTRAINT "IOTEQU"."FK_CG_FIELD_TABLE_ID_CG_TABLE_ID" FOREIGN KEY("TABLE_ID") REFERENCES "IOTEQU"."CG_TABLE"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "IOTEQU"."CG_BUTTON" ADD CONSTRAINT "IOTEQU"."FK_CG_BUTTON_TABLE_ID_CG_TABLE_ID" FOREIGN KEY("TABLE_ID") REFERENCES "IOTEQU"."CG_TABLE"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "IOTEQU"."CG_TABLE" ADD CONSTRAINT "IOTEQU"."FK_CG_TABLE_PROJECT_ID_CG_PROJECT_ID" FOREIGN KEY("PROJECT_ID") REFERENCES "IOTEQU"."CG_PROJECT"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
