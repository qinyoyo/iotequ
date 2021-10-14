-- ----------------------------
-- Table structure for dev_reader
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_READER','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_READER" (
  "ID" CHAR(32) NOT NULL PRIMARY KEY,
  "READER_NO" VARCHAR2(20 BYTE) NOT NULL UNIQUE,
  "NAME" VARCHAR2(30 BYTE) NOT NULL,
  "TYPE" VARCHAR2(30 BYTE) DEFAULT 'D10' NOT NULL,
  "READER_GROUP" INTEGER NOT NULL,
  "ADDRESS" VARCHAR2(100 BYTE) NULL,
  "CAPACITY" INTEGER NULL,
  "CONNECT_TYPE" VARCHAR2(11 BYTE) NOT NULL,
  "IP" VARCHAR2(20 BYTE) NOT NULL,
  "SN_NO" VARCHAR2(36 BYTE) NULL,
  "DEV_MODE" VARCHAR2(32 BYTE) NOT NULL,
  "FIRMWARE" VARCHAR2(40 BYTE) NULL,
  "IS_ONLINE" NUMBER(1) DEFAULT '0' NOT NULL,
  "IS_TIME_SYNC" NUMBER(1) DEFAULT '0' NOT NULL,
  "ALIGN_METHOD" NUMBER(3) DEFAULT 4 NOT NULL,
  "BLACKLIGHT_TIME" NUMBER(3) DEFAULT 0 NOT NULL,
  "VOICEPROMPT" NUMBER(1) DEFAULT '1' NOT NULL,
  "MENU_TIME" NUMBER(3) DEFAULT 0 NOT NULL,
  "WENGENFORM" NUMBER(3) DEFAULT 2 NOT NULL,
  "WENGEN_OUTPUT" NUMBER(3) DEFAULT 1 NOT NULL,
  "WENGEN_OUT_AREA" NUMBER(3) DEFAULT 26 NOT NULL,
  "REGFINGER_OUT_TIME" NUMBER(3) DEFAULT 49 NOT NULL,
  "AUTHFINGER_OUT_TIME" NUMBER(3) DEFAULT 49 NOT NULL,
  "WG_ORDER" NUMBER(3) DEFAULT 0 NULL,
  "RELAY_TIME" NUMBER(3) DEFAULT 5 NULL,
  "FIXED_VALUE" VARCHAR2(36 BYTE) NULL,
  "ALARM_ENABLE" NUMBER(3) DEFAULT 1 NULL,
  "OPEN_ENABLE" NUMBER(3) DEFAULT 0 NULL,
  "DOOR_STATE" NUMBER(3) DEFAULT 1 NULL,
  "RELAY_ENABLE" NUMBER(3) DEFAULT 1 NULL,
  "DOORBELL_ENABLE" NUMBER(3) DEFAULT 1 NULL,
  "WIFI_SSID" VARCHAR2(36 BYTE) NULL,
  "WIFI_PSW" VARCHAR2(36 BYTE) NULL
);

-- ----------------------------
-- Table structure for dev_auth_config
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_AUTH_CONFIG','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_AUTH_CONFIG" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "ROLE_ID" INTEGER NOT NULL,
  "BEGIN_AT" DATE NULL,
  "END_AT" DATE NULL,
  "START_TIME" DATE NULL,
  "END_TIME" DATE NULL,
  "ONLY_WORK_DAY" NUMBER(1) DEFAULT '0' NOT NULL,
  "AUTH" INTEGER DEFAULT 4 NOT NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_AUTH_CONFIG','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_AUTH_CONFIG;

-- ----------------------------
-- Table structure for dev_reader_people
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_READER_PEOPLE','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_READER_PEOPLE" (
  "TYPE" INTEGER DEFAULT 0 NOT NULL,
  "ORDER_NUM" INTEGER NULL,
  "USER_NO" VARCHAR2(36 BYTE) NOT NULL,
  "STATUS" INTEGER DEFAULT 1 NULL,
  "READER_NO" VARCHAR2(20 BYTE) NULL,
  "ID" INTEGER NOT NULL PRIMARY KEY
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_READER_PEOPLE','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_READER_PEOPLE;

-- ----------------------------
-- Table structure for dev_reader_group
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_READER_GROUP','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_READER_GROUP" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "NAME" VARCHAR2(20 BYTE) NOT NULL UNIQUE,
  "PARENT" INTEGER NULL,
  "ORG_CODE" INTEGER NOT NULL,
  "ORG_AUTH" VARCHAR2(256 BYTE) NULL,
  "SUB_ORG_AUTH" VARCHAR2(256 BYTE) NULL,
  "AUTH_GROUP_LIST" VARCHAR2(256 BYTE) NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_READER_GROUP','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_READER_GROUP;

-- ----------------------------
-- Table structure for dev_people_mapping
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_PEOPLE_MAPPING','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_PEOPLE_MAPPING" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "READER_NO" VARCHAR2(20 BYTE) NOT NULL,
  "USER_NO" VARCHAR2(36 BYTE) NOT NULL,
  "STATUS" NUMBER(1) NOT NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_PEOPLE_MAPPING','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_PEOPLE_MAPPING;

-- ----------------------------
-- Table structure for dev_people_group
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_PEOPLE_GROUP','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_PEOPLE_GROUP" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "GROUP_ID" INTEGER NOT NULL,
  "USER_NO" VARCHAR2(45 BYTE) NOT NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_PEOPLE_GROUP','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_PEOPLE_GROUP;

ALTER TABLE "IOTEQU"."DEV_PEOPLE_GROUP" ADD CONSTRAINT  "UI_DEV_PEOPLE_GROUP01" UNIQUE ("GROUP_ID","USER_NO");
-- ----------------------------
-- Table structure for dev_people
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_PEOPLE','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_PEOPLE" (
  "USER_NO" VARCHAR2(15 BYTE) NOT NULL PRIMARY KEY,
  "REAL_NAME" VARCHAR2(32 BYTE) NOT NULL,
  "SEX" VARCHAR2(1 BYTE) DEFAULT '1' NULL,
  "BIRTH_DATE" DATE NULL,
  "ORG_CODE" INTEGER DEFAULT 0 NOT NULL,
  "DUTY_RANK" VARCHAR2(36 BYTE) NULL,
  "CARD_NO" VARCHAR2(45 BYTE) NULL UNIQUE,
  "ID_TYPE" INTEGER DEFAULT 1 NOT NULL,
  "ID_NUMBER" VARCHAR2(45 BYTE) NOT NULL,
  "USER_TYPE" INTEGER DEFAULT 2 NOT NULL,
  "MOBILE_PHONE" VARCHAR2(32 BYTE) NULL UNIQUE,
  "EMAIL" VARCHAR2(50 BYTE) NULL UNIQUE,
  "REGISTER_TYPE" INTEGER DEFAULT 1 NOT NULL,
  "VALID_DATE" DATE NULL,
  "EXPIRED_DATE" DATE NULL,
  "REG_TIME" DATE NULL,
  "DEV_PASSWORD" VARCHAR2(32 BYTE) DEFAULT '111111' NULL,
  "REG_FINGERS" INTEGER DEFAULT 0 NULL,
  "NOTE" VARCHAR2(100 BYTE) NULL,
  "ID_NATION" VARCHAR2(100 BYTE) NULL,
  "PHOTO" VARCHAR2(200 BYTE) NULL,
  "HOME_ADDR" VARCHAR2(200 BYTE) NULL
);

ALTER TABLE "IOTEQU"."DEV_PEOPLE" ADD CONSTRAINT  "UI_DEV_PEOPLE01" UNIQUE ("ID_TYPE","ID_NUMBER");
-- ----------------------------
-- Table structure for dev_org_group
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_ORG_GROUP','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_ORG_GROUP" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "GROUP_ID" INTEGER NOT NULL,
  "ORG_ID" INTEGER NOT NULL,
  "IS_INCLUDE_SUB_ORG" NUMBER(1) DEFAULT 1 NOT NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_ORG_GROUP','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_ORG_GROUP;

ALTER TABLE "IOTEQU"."DEV_ORG_GROUP" ADD CONSTRAINT  "UI_DEV_ORG_GROUP01" UNIQUE ("GROUP_ID","ORG_ID");
-- ----------------------------
-- Table structure for dev_new_device
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_NEW_DEVICE','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_NEW_DEVICE" (
  "READER_NO" VARCHAR2(20 BYTE) NOT NULL PRIMARY KEY,
  "SN_NO" VARCHAR2(36 BYTE) NULL,
  "TYPE" VARCHAR2(30 BYTE) NOT NULL,
  "IP" VARCHAR2(20 BYTE) NULL
);

-- ----------------------------
-- Table structure for dev_event
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_EVENT','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_EVENT" (
  "IMAGE" CLOB NULL,
  "ID" CHAR(32) NOT NULL PRIMARY KEY,
  "DEV_TYPE" VARCHAR2(45 BYTE) DEFAULT 'D10' NOT NULL,
  "DEV_NO" VARCHAR2(45 BYTE) NOT NULL,
  "ORG_CODE" INTEGER NULL,
  "USER_NO" VARCHAR2(45 BYTE) NULL,
  "STATUS" INTEGER NULL,
  "TIME" DATE NOT NULL,
  "AUDITEE_AUTH_TYPE" NUMBER(3) NULL,
  "AUDITOR_USER_NUM" VARCHAR2(45 BYTE) NULL,
  "AUDITOR_AUTH_TYPE" NUMBER(3) NULL,
  "AUDITOR_ORG" INTEGER NULL,
  "AUTH_TYPE" NUMBER(3) NULL,
  "TEMPLATE" CLOB NULL
);

-- ----------------------------
-- Table structure for dev_download_plan
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_DOWNLOAD_PLAN','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_DOWNLOAD_PLAN" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "USER_NO" VARCHAR2(36 BYTE) NOT NULL,
  "READER_NO" VARCHAR2(20 BYTE) NOT NULL,
  "TYPE" INTEGER NOT NULL,
  "DOWNLOAD_NUM" INTEGER DEFAULT 3 NOT NULL,
  "TIME" DATE NOT NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_DOWNLOAD_PLAN','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_DOWNLOAD_PLAN;

-- ----------------------------
-- Table structure for dev_auth_role
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_AUTH_ROLE','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_AUTH_ROLE" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "NAME" VARCHAR2(20 BYTE) NOT NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_AUTH_ROLE','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_AUTH_ROLE;

-- ----------------------------
-- Table structure for dev_auth_group
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','DEV_AUTH_GROUP','IOTEQU');
CREATE TABLE "IOTEQU"."DEV_AUTH_GROUP" (
  "ID" INTEGER NOT NULL PRIMARY KEY,
  "NAME" VARCHAR2(45 BYTE) NOT NULL,
  "AUTH" VARCHAR2(256 BYTE) NOT NULL
);
CALL REMOVE_OBJECT('SEQUENCE','SEQUENCE_DEV_AUTH_GROUP','IOTEQU');
CREATE SEQUENCE SEQUENCE_DEV_AUTH_GROUP;

ALTER TABLE "IOTEQU"."DEV_READER" ADD CONSTRAINT "IOTEQU"."FK_DEV_READER_READER_GROUP_DEV_READER_GROUP_ID" FOREIGN KEY("READER_GROUP") REFERENCES "IOTEQU"."DEV_READER_GROUP"("ID") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "IOTEQU"."DEV_AUTH_CONFIG" ADD CONSTRAINT "IOTEQU"."FK_DEV_AUTH_CONFIG_ROLE_ID_DEV_AUTH_ROLE_ID" FOREIGN KEY("ROLE_ID") REFERENCES "IOTEQU"."DEV_AUTH_ROLE"("ID") ON DELETE CASCADE ON UPDATE CASCADE;
