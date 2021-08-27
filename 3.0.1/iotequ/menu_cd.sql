CREATE DATABASE  IF NOT EXISTS `iotequ3` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iotequ3`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: iotequ3
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sort_num` int(11) NOT NULL DEFAULT '10' COMMENT '排列顺序',
  `name` varchar(45) NOT NULL COMMENT '名称',
  `parent` int(11) DEFAULT NULL COMMENT '上级菜单',
  `is_divider` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分割线',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `action` varchar(100) DEFAULT NULL COMMENT '功能地址',
  `class_name` varchar(45) DEFAULT NULL COMMENT '附加类名',
  `data_action` varchar(200) DEFAULT NULL COMMENT '附加参数',
  `bigIcon` varchar(50) DEFAULT NULL COMMENT '大图标',
  `mobile_hidden` tinyint(1) NOT NULL DEFAULT '0' COMMENT '手机隐藏',
  `js_cmd` varchar(45) DEFAULT NULL COMMENT '操作函数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_sys_menu012` (`name`,`parent`,`action`)
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统菜单|Menu';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,10,'menu.systemManage',NULL,0,'fa fa-gear',NULL,NULL,NULL,NULL,0,NULL),(2,140,'sysMenu.title.code',1,0,'fa fa-bars','/framework/sysMenu/list',NULL,NULL,'',0,NULL),(3,80,'sysRole.title.code',1,1,'fa fa-chrome','/framework/sysRole/list',NULL,NULL,NULL,0,NULL),(4,120,'sysAction.title.code',1,0,'fa fa-qrcode','/framework/sysAction/list',NULL,NULL,'',1,NULL),(7,100,'sysOrg.title.code',1,0,'fa fa-sitemap','/framework/sysOrg/list',NULL,NULL,'',0,NULL),(8,110,'sysUser.title.code',1,0,'fa fa-user-plus','/framework/sysUser/list',NULL,NULL,'',0,NULL),(9,90,'sysDataDict.title.code',1,0,'fa fa-book','/framework/sysDataDict/list','','','',1,''),(10,150,'sysLog.title.code',1,0,'fa fa-file-text-o','/framework/sysLog/list','','','',0,''),(11,160,'sysTask.title.code',1,0,'fa fa-clock-o','/framework/sysTask/list','','','',1,''),(20,30,'menu.attendanceManage',NULL,0,'fa fa-address-card-o','','','','',0,''),(21,180,'menu.personnelManage',20,0,'fa fa-user-plus','','','','',0,''),(22,190,'menu.personnelAdjust',20,0,'fa fa-pencil-square-o','','','','',0,''),(23,200,'adData.title.code',20,0,'el-icon-s-data','/attendance/data/adData/list','','','',0,''),(24,210,'adDayResult.title.code',20,0,'fa fa-bar-chart','/attendance/dayresult/adDayResult/list','','','',0,''),(30,240,'adShift.title.code',21,0,'','/attendance/shift/adShift/list','','','',0,''),(31,260,'adOrg.title.code',21,0,'','/attendance/org/adOrg/list','','','',0,''),(32,250,'adSpecialShift.title.code',21,0,'','/attendance/specialshift/adSpecialShift/list','','','',0,''),(33,280,'adAdjust.title.code',22,0,'fa fa-file-text','/attendance/adjust/adAdjust/list','','','',0,''),(40,270,'menu.personnelBill',22,1,'fa fa-plane','/attendance/adjust/adAdjust/record','','','',0,''),(102,340,'menu.dev.device',103,0,'fa fa-tv','/reader/devReaderGroup/list','','','',0,''),(103,50,'menu.deviceUser',NULL,0,'fa fa-user-circle','','','','',0,''),(104,310,'menu.dev.user',103,0,'fa fa-address-book-o','/reader/devPeople/list','','','',0,''),(105,350,'menu.dev.event',103,0,'fa fa-calendar-check-o','/reader/devEvent/list','','','',0,''),(106,320,'menu.dev.group',103,0,'fa fa-user-plus','/reader/devAuthGroup/list','','','',0,''),(108,40,'menu.projectManage',NULL,0,'fa fa-group','','','','',0,''),(109,370,'pmProject.title.code',108,0,'fa fa-magic','/project/product/pmProject/list','','','',0,''),(110,360,'pmPeopleGroup.title.code',108,0,'fa fa-user-circle','/project/people/pmPeopleGroup/list','','','',0,''),(113,380,'pmVersionApplication.title.code',108,0,'fa fa-download','/project/version/pmVersionApplication/list','','','',0,''),(114,60,'menu.pay',NULL,0,'el-icon-document','','','','',0,''),(115,410,'sysUser.title.code',119,0,'fa fa-shopping-bag',NULL,'','','',0,''),(116,420,'账单查询',119,0,'fa fa-bitcoin','/ewBill/list','','','',0,''),(117,430,'计次项目维护',119,0,'fa fa-stack-overflow','/ewCountProject/list','','','',0,''),(118,440,'计时项目维护',119,0,'fa fa-spinner','/ewTimeProject/list','','','',0,''),(119,390,'电子钱包',114,0,'fa fa-behance-square','','','','',0,''),(120,400,'支付运营管理',114,0,'fa fa-university','','','','',0,''),(121,450,'运营公司管理',120,0,'fa fa-tty','/payCorporation/list','','','',0,''),(122,460,'商店管理',120,0,'fa fa-cart-plus','/payShop/list','','','',0,''),(123,470,'pos机具管理',120,0,'fa fa-desktop','/payPos/list','','','',0,''),(124,480,'收银员管理',120,0,'fa fa-user-circle','/payOperator/list','','','',0,''),(125,490,'终端签到浏览',120,0,'fa fa-wpforms','/payLogin/list','','','',0,''),(126,130,'sysOauthClientDetails.title.code',1,0,'fa fa-user-circle-o','/framework/sysOauthClientDetails/list','','','',1,''),(130,70,'menu.codeGenerator',NULL,0,'fa fa-pencil','','hidden-xs-down','','',0,''),(131,500,'menu.cg.project',130,0,'fa fa-plus-circle','/codegenerator/cgProject/list','','','',0,''),(132,510,'menu.cg.design',130,0,'fa fa-edit','/codegenerator/cgTable/list','','','',0,''),(200,530,'Icons',130,0,'el-icon-magic-stick','/icon/index',NULL,NULL,NULL,1,NULL),(203,520,'menu.cg.download',130,0,'fa fa-cloud-download fa-fw','/codegenerator/cgProject/action/down',NULL,'{request: {timeout: 20000,responseType: \"blob\"}, fileName: \"cg.zip\"}',NULL,0,'request'),(204,540,'menu.cg.reg',130,1,'el-icon-circle-check','/icon',NULL,NULL,NULL,0,'doRegTest'),(205,300,'menu.dev.data',103,0,'svg-guide','/reader/devData/record',NULL,NULL,NULL,1,NULL),(206,220,'adStat.title.statByOrg',20,0,'svg-chart/chart-rose','/attendance/dayresult/adDayResult/statByOrg',NULL,NULL,NULL,0,NULL),(207,230,'adStat.title.statRecTime',20,0,'svg-chart/chart-scatter','/attendance/dayresult/adDayResult/statRecTime',NULL,NULL,NULL,0,NULL),(208,330,'menu.dev.permission',103,0,'el-icon-setting','/reader/devAuthRole/list',NULL,NULL,NULL,0,NULL),(209,20,'医保验证',NULL,0,'fa fa-ambulance',NULL,NULL,NULL,NULL,0,NULL),(211,550,'签到',209,0,NULL,'/check-in/ckRegister/record',NULL,NULL,NULL,0,NULL),(212,560,'上机记录',209,0,NULL,'/check-in/ckRegister/list',NULL,NULL,NULL,0,NULL),(213,290,'新设备浏览',103,0,NULL,'/reader/devNewDevice/list',NULL,NULL,NULL,0,NULL),(214,620,'ckStat.title.amountByDay',209,0,NULL,'/check-in/ckRegister/amountByDay',NULL,NULL,NULL,0,NULL),(215,630,'ckStat.title.amountByMonth',209,0,NULL,'/check-in/ckRegister/amountByMonth',NULL,NULL,NULL,0,NULL),(216,600,'ckStat.title.amountByAge',209,0,NULL,'/check-in/ckRegister/amountByAge',NULL,NULL,NULL,0,NULL),(217,610,'ckStat.title.amountByAgeMonth',209,0,NULL,'/check-in/ckRegister/amountByAgeMonth',NULL,NULL,NULL,0,NULL),(218,590,'ckStat.title.distributionByAge',209,0,NULL,'/check-in/ckRegister/distributionByAge',NULL,NULL,NULL,0,NULL),(219,570,'ckStat.title.timeByDay',209,0,NULL,'/check-in/ckRegister/timeByDay',NULL,NULL,NULL,0,NULL),(220,580,'ckStat.title.amountByArea',209,0,NULL,'/check-in/ckRegister/amountByArea',NULL,NULL,NULL,0,NULL),(221,170,'About',1,0,'fa fa-question','/about',NULL,NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'iotequ3'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-08-27 12:04:11
