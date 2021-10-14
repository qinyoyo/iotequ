CREATE DATABASE  IF NOT EXISTS `svas` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `svas`;
-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: iotequ3
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `dev_user_no`
--

DROP TABLE IF EXISTS `dev_user_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dev_user_no` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_no` varchar(20) NOT NULL COMMENT '用户号',
  `name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `sex` varchar(1) DEFAULT '1' COMMENT '性别',
  `birth_date` date DEFAULT NULL COMMENT '生日',
  `mobile_phone` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `wechat_openid` varchar(50) DEFAULT NULL COMMENT '微信openId',
  `id_type` int(11) NOT NULL COMMENT '证件类型',
  `id_no` varchar(45) NOT NULL COMMENT '证件号',
  `id_nation` varchar(100) DEFAULT NULL COMMENT '民族',
  `id_organization` varchar(200) DEFAULT NULL COMMENT '发证机构',
  `home_addr` varchar(200) DEFAULT NULL COMMENT '住址',
  `valid_date` date DEFAULT NULL COMMENT '生效日期',
  `expired_date` date DEFAULT NULL COMMENT '过期日期',
  `reg_time` datetime DEFAULT NULL COMMENT '注册时间',
  `photo` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_no` (`user_no`) USING HASH,
  UNIQUE KEY `id_type_no` (`id_type`,`id_no`)
) ENGINE=InnoDB AUTO_INCREMENT=261 DEFAULT CHARSET=utf8 COMMENT='用户号列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dev_vein_info`
--

DROP TABLE IF EXISTS `dev_vein_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dev_vein_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_no` varchar(45) NOT NULL COMMENT '用户号',
  `finger_no` tinyint(8) NOT NULL COMMENT '手指编号',
  `templates` varchar(2000) NOT NULL COMMENT '指静脉信息',
  `warning` int(11) NOT NULL DEFAULT '0' COMMENT '胁迫',
  `finger_type` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_no` (`user_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13518 DEFAULT CHARSET=utf8 COMMENT='指静脉信息';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-28 17:40:20
