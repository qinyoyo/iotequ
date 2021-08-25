-- ----------------------------
-- Table structure for ck_sign_in
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CK_SIGN_IN','IOTEQU');
CREATE TABLE "IOTEQU"."CK_SIGN_IN" (
  "ID" CHAR(32) NOT NULL PRIMARY KEY,
  "USER_NO" VARCHAR2(15 BYTE) NOT NULL,
  "ORG_CODE" INTEGER NOT NULL,
  "REC_SOURCE" VARCHAR2(45 BYTE) NOT NULL,
  "REC_SOURCE_TYPE" VARCHAR2(45 BYTE) DEFAULT 'U53' NOT NULL,
  "EVENT_TYPE" INTEGER DEFAULT 1 NOT NULL,
  "REC_TIME" DATE NOT NULL
);
CALL REMOVE_OBJECT('INDEX','INDEX_CK_SIGN_IN_USER_NO','IOTEQU');
CREATE INDEX "IOTEQU"."INDEX_CK_SIGN_IN_USER_NO" ON "IOTEQU"."CK_SIGN_IN"("USER_NO");
CALL REMOVE_OBJECT('INDEX','INDEX_CK_SIGN_IN_ORG_CODE','IOTEQU');
CREATE INDEX "IOTEQU"."INDEX_CK_SIGN_IN_ORG_CODE" ON "IOTEQU"."CK_SIGN_IN"("ORG_CODE");
CALL REMOVE_OBJECT('INDEX','INDEX_CK_SIGN_IN_REC_TIME','IOTEQU');
CREATE INDEX "IOTEQU"."INDEX_CK_SIGN_IN_REC_TIME" ON "IOTEQU"."CK_SIGN_IN"("REC_TIME");

-- ----------------------------
-- Table structure for ck_register
-- ----------------------------
CALL REMOVE_OBJECT('TABLE','CK_REGISTER','IOTEQU');
CREATE TABLE "IOTEQU"."CK_REGISTER" (
  "ID" CHAR(32) NOT NULL PRIMARY KEY,
  "USER_NO" VARCHAR2(36 BYTE) NOT NULL,
  "NAME" VARCHAR2(32 BYTE) NOT NULL,
  "SEX" VARCHAR2(1 BYTE) DEFAULT '1' NOT NULL,
  "BIRTH_DATE" DATE NULL,
  "ORG_CODE" INTEGER NOT NULL,
  "ORG_NAME" VARCHAR2(32 BYTE) NOT NULL,
  "IN_DATE" DATE NOT NULL,
  "ON_TIME" DATE NOT NULL,
  "OFF_TIME" DATE NULL
);

ALTER TABLE "IOTEQU"."CK_REGISTER" ADD CONSTRAINT  "UI_CK_REGISTER012" UNIQUE ("USER_NO","ORG_CODE","IN_DATE");