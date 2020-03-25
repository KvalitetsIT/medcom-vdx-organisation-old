-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: 172.20.0.41    Database: vdxapi
-- ------------------------------------------------------
-- Server version	5.7.29-0ubuntu0.16.04.1

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
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(250) DEFAULT NULL,
  `group_type` int(1) NOT NULL DEFAULT '1',
  `parent_id` bigint(20) DEFAULT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(250) NOT NULL DEFAULT 'system',
  `updated_time` datetime NOT NULL DEFAULT '0001-01-01 00:00:00',
  `updated_by` varchar(250) DEFAULT NULL,
  `deleted_time` datetime NOT NULL DEFAULT '0001-01-01 00:00:00',
  `deleted_by` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_id` (`group_id`),
  KEY `idx_deleted_time` (`deleted_time`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
INSERT INTO `groups` VALUES (1,'VDX',2,NULL,'2019-04-07 23:44:09','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(2,'Regioner',1,1,'2019-04-07 23:50:50','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(3,'Kommuner',1,1,'2019-04-07 23:50:50','system','2019-04-19 15:20:21','jsk-test@medcom.dk','0001-01-01 00:00:00',NULL),(4,'Offentlige organisationer',1,1,'2019-04-07 23:51:05','system','2019-04-19 15:20:14','jsk-test@medcom.dk','0001-01-01 00:00:00',NULL),(5,'Private organisationer',1,1,'2019-04-07 23:51:05','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(6,'MedCom',2,4,'2019-04-07 23:57:30','system','2019-06-14 12:33:43','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(7,'Region Syddanmark',2,2,'2019-04-07 23:58:35','system','2019-04-30 15:27:14','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(8,NULL,2,2,'2019-04-08 00:03:02','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(9,NULL,2,2,'2019-04-08 00:03:02','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(10,NULL,2,2,'2019-04-08 00:03:22','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(11,NULL,2,7,'2019-04-08 00:04:16','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(12,NULL,2,5,'2019-04-08 00:04:40','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(13,NULL,2,5,'2019-04-08 00:04:53','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(14,'Medware',2,5,'2019-04-08 00:05:03','system','2020-01-20 12:03:57','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(15,'Test Org for MedCom Med et meget langt navn.',2,6,'2019-05-07 11:22:00','system','2019-07-08 11:21:49','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(16,NULL,2,4,'2019-04-08 00:05:48','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(17,NULL,2,5,'2019-04-08 00:06:07','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(18,NULL,2,6,'2019-04-08 00:06:24','system','0001-01-01 00:00:00',NULL,'2019-04-21 01:00:37','jsk-test@medcom.dk'),(19,'Praksis',1,7,'2019-04-08 00:06:47','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(20,'Almen medicinsk l&#230;geklinik',3,33,'2019-04-08 00:06:57','system','2019-04-30 14:56:02','jsk-test@medcom.dk','0001-01-01 00:00:00',NULL),(21,NULL,2,3,'2019-04-08 00:07:23','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(22,NULL,2,3,'2019-04-08 00:07:23','system','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(33,'Fyn',1,19,'2019-04-22 20:47:16','claus.terkelsen@rsyd.dk','2019-04-30 15:47:03','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(43,'Test 12',1,6,'2019-05-07 12:28:43','jsk@medcom.dk','2019-05-07 12:29:38','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(44,'Test Klinikken',3,47,'2019-05-07 12:29:29','jsk@medcom.dk','2019-05-22 18:18:37','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(45,'Test 3',1,6,'2019-05-07 12:31:34','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'2019-06-14 13:51:52','jsk@medcom.dk'),(46,'Test 4',2,6,'2019-05-07 12:31:43','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'2020-03-24 15:55:44','jsk@medcom.dk'),(47,'Klinikker med MinL&#230;ge',1,6,'2019-05-22 17:55:19','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(48,'Lakeside',3,6,'2019-05-07 11:22:00','system','2020-03-24 23:18:42','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(49,'Test praksis',3,6,'2019-09-09 09:05:56','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(50,'Test praksis 2',3,6,'2019-09-09 09:06:57','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'2020-03-24 23:15:31','jsk@medcom.dk'),(51,'Test Org 1',2,6,'2019-09-20 12:03:59','jsk@medcom.dk','2020-03-24 15:47:24','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(52,'F&#230;lles Udbud af Telemedicin (FUT)',2,4,'2019-09-26 10:43:39','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(53,'Sundhedsplatformen RH',2,9,'2019-10-21 19:03:24','jsk@medcom.dk','2019-10-21 19:12:33','jsk@medcom.dk','0001-01-01 00:00:00',NULL),(54,'CapGemini',2,5,'2019-11-05 09:52:43','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(55,'PLSP - MinL&#230;ge',2,5,'2020-01-23 12:56:29','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(56,'Region Nordjylland',2,2,'2020-01-26 18:41:42','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(57,'Test praksis 2',2,6,'2020-03-24 23:26:03','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'0001-01-01 00:00:00',NULL),(58,'Test praksis 2',3,6,'2020-03-24 23:28:26','jsk@medcom.dk','0001-01-01 00:00:00',NULL,'2020-03-24 23:28:47','jsk@medcom.dk');
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_unicode_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `organisation_update_deleted` AFTER UPDATE ON `groups` FOR EACH ROW UPDATE organisation SET organisation.deleted_time = NEW.deleted_time WHERE organisation.group_id=NEW.group_id */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `groups_authorization`
--

DROP TABLE IF EXISTS `groups_authorization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups_authorization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(200) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role` varchar(250) NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(250) NOT NULL DEFAULT 'system',
  `updated_time` datetime DEFAULT NULL,
  `updated_by` varchar(250) DEFAULT NULL,
  `deleted_time` timestamp NULL DEFAULT NULL,
  `deleted_by` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups_authorization`
--

LOCK TABLES `groups_authorization` WRITE;
/*!40000 ALTER TABLE `groups_authorization` DISABLE KEYS */;
INSERT INTO `groups_authorization` VALUES (4,'jsk@medcom.dk',6,'management-localusers,meeting-admin','2019-04-28 19:05:39','jsk@medcom.dk','2019-04-28 22:25:53','jsk@medcom.dk',NULL,NULL),(5,'jsk@medcom.dk',7,'vdx-admin','2019-04-30 15:18:00','jsk@medcom.dk',NULL,NULL,NULL,NULL),(6,'jsk@medcom.dk',3,'vdx-admin','2019-06-13 09:27:46','jsk@medcom.dk',NULL,NULL,'2019-06-13 08:43:00','jsk@medcom.dk'),(7,'jsk@medcom.dk',19,'meeting-admin,management-groups,management-localusers','2019-06-13 10:43:49','jsk@medcom.dk','2019-06-13 11:08:31','jsk@medcom.dk',NULL,NULL),(8,'jsk-test3@medcom.dk',9,'management-admin','2020-03-12 10:19:31','jsk@medcom.dk',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `groups_authorization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups_bookingdefaults`
--

DROP TABLE IF EXISTS `groups_bookingdefaults`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups_bookingdefaults` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `jsondata` longtext,
  `defaults_type` varchar(200) NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(250) NOT NULL DEFAULT 'system',
  `updated_time` datetime DEFAULT NULL,
  `updated_by` varchar(250) DEFAULT NULL,
  `deleted_time` datetime DEFAULT NULL,
  `deleted_by` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups_bookingdefaults`
--

LOCK TABLES `groups_bookingdefaults` WRITE;
/*!40000 ALTER TABLE `groups_bookingdefaults` DISABLE KEYS */;
INSERT INTO `groups_bookingdefaults` VALUES (22,6,'{\"inherit\":true,\"group_type\":2,\"description\":\"Grundl&aelig;ggende skabelon\",\"messagetemplate\":\"K&aelig;re [Inds&aelig;t navn]&lt;br&gt;&lt;br&gt;Du har et videom&oslash;de med #Fullname# d. #StartDate.Long# kl. #StartTime.Short#&lt;br&gt;&lt;br&gt;M&oslash;dets beskrivelse:&lt;br&gt;#Description#&lt;br&gt;&lt;br&gt;Klik p&aring; linket herunder for at starte m&oslash;det:&lt;br&gt;#MeetingLink.Long#\"}','messagetemplate','2019-05-25 16:15:31','jsk@medcom.dk','2019-05-25 17:10:30','jsk@medcom.dk','2019-12-13 11:04:34','jsk@medcom.dk'),(23,6,'{\"inherit\":false,\"name\":\"sdfsf\",\"description\":\"fghfghfgfhfgh\"}','projectcode','2019-05-25 16:55:10','jsk@medcom.dk','2019-05-25 17:09:17','jsk@medcom.dk',NULL,NULL),(24,6,'{\"inherit\":false,\"name\":\"234\",\"description\":\"serwrew\"}','projectcode','2019-05-25 17:09:16','jsk@medcom.dk',NULL,NULL,NULL,NULL),(25,6,'{\"group_type\":\"2\",\"inherit\":true,\"description\":\"Test 1\",\"messagetemplate\":\"qweqe1\"}','messagetemplate','2020-01-14 07:54:16','jsk@medcom.dk','2020-01-23 17:10:25','jsk@medcom.dk',NULL,NULL),(26,6,'{\"group_type\":\"2\",\"inherit\":false,\"description\":\"Test 2\",\"messagetemplate\":\"asdasdasdasd\"}','messagetemplate','2020-01-14 08:21:16','jsk@medcom.dk','2020-01-23 16:10:41','jsk@medcom.dk',NULL,NULL),(27,47,'{\"group_id\":\"47\",\"group_type\":\"3\",\"inherit\":true,\"description\":\"Test almen\",\"messagetemplate\":\"asdasdasd\"}','messagetemplate','2020-01-23 17:10:14','jsk@medcom.dk',NULL,NULL,NULL,NULL),(28,1,'{\"group_type\":\"2\",\"inherit\":true,\"description\":\"Test 111\",\"messagetemplate\":\"asdasd asd asdasd\"}','messagetemplate','2020-01-23 22:38:34','jsk@medcom.dk','2020-01-27 22:12:59','jsk@medcom.dk','2020-01-27 22:25:00','jsk@medcom.dk'),(29,1,'{\"group_type\":\"2\",\"inherit\":true,\"description\":\"Test 222\",\"messagetemplate\":\"qw\"}','messagetemplate','2020-01-23 22:38:56','jsk@medcom.dk','2020-01-27 22:12:55','jsk@medcom.dk','2020-01-27 22:25:04','jsk@medcom.dk'),(30,6,'{\"group_type\":\"2\",\"inherit\":false,\"description\":\"Test 3\",\"messagetemplate\":\"&lt;span&gt;&lt;p&gt;#MeetingLink.Long#&lt;/p&gt;&lt;p&gt;K&#230;re [Inds&#230;t navn]&lt;br&gt;&lt;br&gt;Som vi aftalte, har jeg booket en videokonsultation med dig den #StartDate.Long# kl. #StartTime.Short#&lt;br&gt;&lt;br&gt;I stedet for at komme op i klinikken, skal du via Min L&#230;ge app’en trykke p&#229; nedenst&#229;ende knap med &#39;KAMERA-IKONET&#39;. &lt;br&gt;&lt;br&gt;Har du ikke installeret Min L&#230;ge app’en, kan du g&#248;re det fra App-store eller Google Play og logge ind med NEMID. &lt;br&gt;&lt;br&gt;Du kan logge p&#229; ca. 15 min f&#248;r den aftalte tid. HUSK ogs&#229; her kan der forekomme ventetid.&lt;br&gt;&lt;br&gt;Vi skal snakke om: &lt;br&gt;#Description#&lt;/p&gt;&lt;p&gt;#MeetingLink.Long#&lt;br&gt;&lt;br&gt;&lt;/p&gt;&lt;p&gt;&lt;strong&gt;Evaluering&lt;/strong&gt;:&lt;br&gt;Efter din videokonsultation med l&#230;gen vil vi meget gerne h&#248;re om din oplevelse hermed. Tryk venligst p&#229; &lt;a href=&quot;https://www.survey-xact.dk/LinkCollector?key=G4SLXGZUC2C2&quot;&gt;dette link&lt;/a&gt;, hvis du vil deltage i en sp&#248;rgeskemaunders&#248;gelse. Det tager ca. 5 min. at deltage og er anonymt”.&lt;br&gt;&lt;br&gt;Med venlig hilsen&lt;br&gt;#Fullname#&lt;/p&gt;&lt;/span&gt;\"}','messagetemplate','2020-01-27 21:49:54','jsk@medcom.dk','2020-01-27 21:59:26','jsk@medcom.dk',NULL,NULL),(31,1,'{\"group_id\":\"1\",\"group_type\":\"2\",\"inherit\":false,\"description\":\"Test 1\",\"messagetemplate\":\"asdas a das da d\"}','messagetemplate','2020-01-28 10:43:50','jsk@medcom.dk',NULL,NULL,'2020-03-12 09:43:28','jsk@medcom.dk'),(32,1,'{\"group_id\":\"1\",\"group_type\":\"2\",\"inherit\":false,\"description\":\"Test 2\",\"messagetemplate\":\"asdasdasdasdasdas a asd&#160;\"}','messagetemplate','2020-01-28 10:43:58','jsk@medcom.dk',NULL,NULL,'2020-03-12 09:43:25','jsk@medcom.dk');
/*!40000 ALTER TABLE `groups_bookingdefaults` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups_domains`
--

DROP TABLE IF EXISTS `groups_domains`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups_domains` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `domain` varchar(250) NOT NULL,
  `inherit` bit(1) NOT NULL DEFAULT b'1',
  `is_history_domain` bit(1) NOT NULL DEFAULT b'0',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(250) NOT NULL DEFAULT 'system',
  `updated_time` datetime DEFAULT NULL,
  `updated_by` varchar(250) DEFAULT NULL,
  `deleted_time` datetime DEFAULT NULL,
  `deleted_by` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups_domains`
--

LOCK TABLES `groups_domains` WRITE;
/*!40000 ALTER TABLE `groups_domains` DISABLE KEYS */;
','2020-02-07 08:51:41','system',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `groups_domains` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups_settings`
--

DROP TABLE IF EXISTS `groups_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `setting_name` varchar(250) NOT NULL,
  `setting_value` longtext NOT NULL,
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `created_by` varchar(250) NOT NULL DEFAULT 'system',
  `updated_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00',
  `updated_by` varchar(250) DEFAULT NULL,
  `deleted_time` datetime NOT NULL DEFAULT '1000-01-01 00:00:00',
  `deleted_by` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups_settings`
--

LOCK TABLES `groups_settings` WRITE;
/*!40000 ALTER TABLE `groups_settings` DISABLE KEYS */;
INSERT INTO `groups_settings` VALUES (1,47,'noinherit_booking_defaults','True','2019-05-09 18:42:19','system','2020-01-27 12:39:35','jsk@medcom.dk','1000-01-01 00:00:00',NULL),(2,44,'conferencing_system_id','0','2020-01-23 13:44:05','system','1000-01-01 00:00:00',NULL,'1000-01-01 00:00:00',NULL),(3,6,'noinherit_booking_defaults','True','2020-01-23 22:12:22','jsk@medcom.dk','2020-01-27 22:00:07','jsk@medcom.dk','1000-01-01 00:00:00',NULL),(4,1,'pexip_conferencing_systems_location_id','2','2020-01-25 21:38:07','system','1000-01-01 00:00:00',NULL,'1000-01-01 00:00:00',NULL),(6,6,'vmr_number_series','{\"start\":1000,\"end\":1099}','2020-02-06 07:21:28','system','1000-01-01 00:00:00',NULL,'1000-01-01 00:00:00',NULL),(7,6,'smtp_server_settings','{\"smtp_server\":\"smtp.office365.com\",\"smtp_port\":587,\"smtp_usessl\":true,\"smtp_user\":\"service@vconf.dk\",\"smtp_password\":\"ww0ncUhtq4ucSARRNBho+w==\",\"sender_name\":\"MedCom Video Service\",\"sender_email\":\"service@vconf.dk\"}','2020-02-28 13:35:22','system','1000-01-01 00:00:00',NULL,'1000-01-01 00:00:00',NULL);
/*!40000 ALTER TABLE `groups_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organisation`
--

DROP TABLE IF EXISTS `organisation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organisation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) DEFAULT NULL,
  `organisation_id` varchar(250) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `deleted_time` datetime NOT NULL DEFAULT '0001-01-01 00:00:00',
  `pool_size` int(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `organisation_id` (`organisation_id`),
  KEY `group_id` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organisation`
--

LOCK TABLES `organisation` WRITE;
/*!40000 ALTER TABLE `organisation` DISABLE KEYS */;
INSERT INTO `organisation` VALUES (1,6,'medcom','MedCom','0001-01-01 00:00:00',3),(2,17,'kit','KvalitetsIT','0001-01-01 00:00:00',NULL),(3,7,'region-syd','Region Syddanmark','0001-01-01 00:00:00',NULL),(4,8,'region-midt','Region Midt','0001-01-01 00:00:00',NULL),(5,16,'plo','PLO','0001-01-01 00:00:00',NULL),(6,18,'vdx-medcom','vdx-medcom test','2019-04-21 01:00:37',NULL),(7,9,'region-h','Region Hovedstaden','0001-01-01 00:00:00',5),(9,10,'region-sj','Region Sjælland','0001-01-01 00:00:00',5),(10,11,'sharedcare','IBM Shared Care','0001-01-01 00:00:00',NULL),(11,12,'telemed','Dansk Telemedicin A/S','0001-01-01 00:00:00',NULL),(12,13,'minlaege','Trifork - MinLæge','0001-01-01 00:00:00',NULL),(13,14,'32146910','Medware','0001-01-01 00:00:00',NULL),(14,15,'medcom-test','Test Org for MedCom Med et meget langt navn.','0001-01-01 00:00:00',NULL),(15,20,'region-syd-praksis-042552','Almen medicinsk lægeklinik','0001-01-01 00:00:00',NULL),(16,1,'vdx','VDX','0001-01-01 00:00:00',0),(17,21,'fc3478ef-38c2-43d6-83ef-33a8db912e45','Københavns Kommune','0001-01-01 00:00:00',NULL),(18,22,'6cedc378-235e-41c9-9663-927c29c87365','Århus Kommune','0001-01-01 00:00:00',NULL),(19,48,'658309','Lakeside','0001-01-01 00:00:00',NULL),(23,44,'testklinikken','Test Klinikken','0001-01-01 00:00:00',NULL),(24,46,'22910e83-ceef-483b-bb44-7dd385432ee0','Test 4','2020-03-24 15:55:44',NULL),(25,50,'34b047e3-d470-4d58-8a4e-615858c5c094','Test praksis 2','2020-03-24 23:15:31',NULL),(26,51,'2300f3bd-ce68-49b2-b00d-68d7ea07bc9a','Test Org 1','0001-01-01 00:00:00',NULL),(27,52,'futservice','Fælles Udbud af Telemedicin (FUT)','0001-01-01 00:00:00',NULL),(28,53,'region-h-sp2','Sundhedsplatformen RH','0001-01-01 00:00:00',NULL),(29,54,'capgemini','CapGemini','0001-01-01 00:00:00',10),(30,55,'plsp_minlaege','PLSP - MinLæge','0001-01-01 00:00:00',100),(31,56,'region_nord','Region Nordjylland','0001-01-01 00:00:00',NULL),(33,58,'29e30576-e31a-4870-a2dd-b4e4c84b79ca','Test praksis 2','2020-03-24 23:28:47',NULL);
/*!40000 ALTER TABLE `organisation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

',_binary '',_binary '\0','2020-01-26 18:43:02','system',NULL,NULL,NULL,NULL),(9,7,'vrsyd.dk',_binary '',_binary '\0','2020-01-26 18:42:29','system',NULL,NULL,NULL,NULL),(8,10,'video.regsj.dk',_binary '',_binary '\0','2020-01-26 18:41:53','system',NULL,NULL,NULL,NULL),(7,9,'meet.regionh.dk',_binary '',_binary '\0','2020-01-26 18:40:45','system',NULL,NULL,NULL,NULL),(6,56,'praksisvideo.rn.dk',_binary '',_binary '\0','2019-05-05 15:55:33','system',NULL,NULL,NULL,NULL),(5,8,'rooms.rm.dk',_binary '',_binary '\0','2019-05-05 15:10:28','system',NULL,NULL,NULL,NULL),(3,7,'praksis.rsyd.dk',_binary '',_binary '\0','2019-04-12 08:27:15','system',NULL,NULL,NULL,NULL),(2,6,'rooms.medcom.dk',_binary 'INSERT INTO `groups_domains` VALUES (1,1,'rooms.vconf.dk',_binary '
