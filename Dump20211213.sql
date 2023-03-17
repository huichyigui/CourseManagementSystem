-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: anavrin
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cocurriculum`
--

DROP TABLE IF EXISTS `cocurriculum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cocurriculum` (
  `courseCode` varchar(8) NOT NULL DEFAULT '0',
  `courseName` varchar(50) NOT NULL,
  `description` varchar(45) NOT NULL,
  `creditHours` int NOT NULL DEFAULT '2',
  `category` varchar(20) NOT NULL,
  `fee` decimal(7,2) NOT NULL DEFAULT '0.00',
  `status` varchar(45) NOT NULL DEFAULT 'AVAILABLE',
  PRIMARY KEY (`courseCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cocurriculum`
--

LOCK TABLES `cocurriculum` WRITE;
/*!40000 ALTER TABLE `cocurriculum` DISABLE KEYS */;
INSERT INTO `cocurriculum` VALUES ('COCU0010','DANCE','MODERN DANCE LEARNING',2,'CULTURAL STUDIES',80.00,'AVAILABLE'),('COCU0011','HANDBALL','GAME PLAYED USING HANDS TO STRIKE THE BALL',2,'GAMES/SPORTS',0.00,'UNAVAILABLE'),('COCU0012','FIRST AID UNIT','PROVIDE A FIRST AID RESPONSE TO A CASUALTY',2,'VOLUNTEERISM',0.00,'UNAVAILABLE'),('COCU0013','COMMUNITY SOCIOLOGY','SHARE LOVE WITH OTHERS AND HELP THOSE IN NEED',2,'COMMUNITY SERVICE',0.00,'UNAVAILABLE');
/*!40000 ALTER TABLE `cocurriculum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cocurriculum_seq`
--

DROP TABLE IF EXISTS `cocurriculum_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cocurriculum_seq` (
  `courseCode` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`courseCode`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cocurriculum_seq`
--

LOCK TABLES `cocurriculum_seq` WRITE;
/*!40000 ALTER TABLE `cocurriculum_seq` DISABLE KEYS */;
INSERT INTO `cocurriculum_seq` VALUES (10),(11),(12),(13);
/*!40000 ALTER TABLE `cocurriculum_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curriculum`
--

DROP TABLE IF EXISTS `curriculum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `curriculum` (
  `courseCode` varchar(8) NOT NULL,
  `courseName` varchar(50) NOT NULL,
  `description` varchar(120) NOT NULL,
  `creditHours` int NOT NULL,
  `fee` decimal(7,2) NOT NULL DEFAULT '0.00',
  `status` varchar(12) NOT NULL DEFAULT 'AVAILABLE',
  PRIMARY KEY (`courseCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curriculum`
--

LOCK TABLES `curriculum` WRITE;
/*!40000 ALTER TABLE `curriculum` DISABLE KEYS */;
INSERT INTO `curriculum` VALUES ('BACS2023','OBJECT-ORIENTED PROGRAMMING','OO IN JAVA',3,777.00,'AVAILABLE'),('BAIT1173','IT FUNDAMENTALS','FOCUSES ON THE BASICS OF COMPUTING',3,777.00,'UNAVAILABLE'),('BAIT2004','FUNDAMENTAL COMPUTER NETWORKS','NETACAD CISCO FOR BEGINNER',4,1036.00,'AVAILABLE'),('BAIT2164','COMPUTER NETWORK','N/A',4,1036.00,'AVAILABLE'),('BAMS1623','DISCRETE MATHEMATICS','IMPROVE PROGRAMMING SOLVING ABILITY',3,777.00,'UNAVAILABLE'),('BMIT2164','COMPUTER NETWORKS','NETACAD CISCO',4,1036.00,'AVAILABLE');
/*!40000 ALTER TABLE `curriculum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fulltimestaff`
--

DROP TABLE IF EXISTS `fulltimestaff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fulltimestaff` (
  `fulltimeStaffID` varchar(5) NOT NULL,
  `password` varchar(15) NOT NULL,
  `name` varchar(30) NOT NULL,
  `IC` varchar(14) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `contactNumber` varchar(15) NOT NULL,
  `emailAddress` varchar(50) NOT NULL,
  `DOB` varchar(10) NOT NULL,
  `accountStatus` varchar(20) NOT NULL,
  `staffType` varchar(20) NOT NULL,
  PRIMARY KEY (`fulltimeStaffID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fulltimestaff`
--

LOCK TABLES `fulltimestaff` WRITE;
/*!40000 ALTER TABLE `fulltimestaff` DISABLE KEYS */;
INSERT INTO `fulltimestaff` VALUES ('FT001','ws012355','WEISHENG','998120-01-3889','MALE','016-832-9901','weisheng@gmail.com','01-12-1998','Activate','FullTime'),('FT002','janny2111','JANNY','002821-11-2814','FEMALE','018-289-1099','janny@gmail.com','21-08-2000','Activate','FullTime'),('FT003','Chuu0110','CHUU','010111-11-1111','Female','011-9999999','testing@ner.com','10/12/2002','Active','FullTime');
/*!40000 ALTER TABLE `fulltimestaff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interstudent`
--

DROP TABLE IF EXISTS `interstudent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `interstudent` (
  `interStudentID` varchar(15) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `ICNumber` varchar(15) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `contactNumber` varchar(15) NOT NULL,
  `emailAddress` varchar(50) NOT NULL,
  `DOB` varchar(10) NOT NULL,
  `accountStatus` varchar(10) NOT NULL,
  `programme` varchar(5) NOT NULL,
  `studyMode` varchar(10) NOT NULL,
  `country` varchar(45) NOT NULL,
  PRIMARY KEY (`interStudentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interstudent`
--

LOCK TABLES `interstudent` WRITE;
/*!40000 ALTER TABLE `interstudent` DISABLE KEYS */;
INSERT INTO `interstudent` VALUES ('20IWBR22222','sYIAYpdrnTLdwunsV462zA==','JOHN WICK','A78945612','Male','018-7894562','fongzhijun@gmail.com','20/07/1998','Active','REI','Full-time','ENGLAND'),('21IWMR00001','pG02rRwfb/8yzmH9y0V0lQ==','CHARLIE DOE','Z12345678','Male','012-7894561','charliedoe@hotmail.com','10/10/1999','Active','RMM','Full-time','UNITED STATES'),('22IWMR12345','L2+pwz8dcg5dAQTl686LXw==','BILLIE EILISH','B12345678','Female','013-78945632','dontknow@protonmail.com','18/12/2001','Inactive','RSD','Full-time','UNITED STATES');
/*!40000 ALTER TABLE `interstudent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `localstudent`
--

DROP TABLE IF EXISTS `localstudent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `localstudent` (
  `localStudentID` varchar(15) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `ICNumber` varchar(15) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `contactNumber` varchar(15) NOT NULL,
  `emailAddress` varchar(50) NOT NULL,
  `DOB` varchar(10) NOT NULL,
  `accountStatus` varchar(10) NOT NULL,
  `programme` varchar(5) NOT NULL,
  `studyMode` varchar(10) NOT NULL,
  PRIMARY KEY (`localStudentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `localstudent`
--

LOCK TABLES `localstudent` WRITE;
/*!40000 ALTER TABLE `localstudent` DISABLE KEYS */;
INSERT INTO `localstudent` VALUES ('19WMR00050','HSAgZhz6RjM92LIrYFC0wA==','XIAO CHENG LEI','980101-78-4512','Male','012-4561237','justtesting@gmail.com','01/01/1998','Inactive','REI','Full-time'),('20WBR77777','wvyS5Z46TjLzAsAKXxozfQ==','ELAINE MAY','991010-78-6541','Female','018-7894562','elaine@hotmail.com','10/10/1999','Active','REI','Part-time'),('20WMR12153','hYb8TFmWGYVZMuZxyLGsIQ==','CHEON CHUU','011223-34-5678','Female','012-9913668','huichyigui@gmail.com','21/03/2001','Active','RSD','Full-time'),('22WMR88888','2B0TJOWhPLzjAZUYkivhcA==','CHEAH CHENG LONG','001010-08-7894','Male','012-1234561','fongzhijun@gmail.com','10/10/2000','Active','RMM','Full-time');
/*!40000 ALTER TABLE `localstudent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parttimestaff`
--

DROP TABLE IF EXISTS `parttimestaff`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parttimestaff` (
  `parttimeStaffID` varchar(6) NOT NULL,
  `password` varchar(15) NOT NULL,
  `name` varchar(45) NOT NULL,
  `IC` varchar(14) NOT NULL,
  `gender` varchar(6) NOT NULL,
  `contactNumber` varchar(15) NOT NULL,
  `emailAddress` varchar(50) NOT NULL,
  `DOB` varchar(10) NOT NULL,
  `accountStatus` varchar(20) NOT NULL,
  `staffType` varchar(10) NOT NULL,
  PRIMARY KEY (`parttimeStaffID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parttimestaff`
--

LOCK TABLES `parttimestaff` WRITE;
/*!40000 ALTER TABLE `parttimestaff` DISABLE KEYS */;
INSERT INTO `parttimestaff` VALUES ('PT0001','zicong99','ZICONG','010502-11-2844','Male','016-291-3092','zicong@gmail.com','2001-05-10','Activate','PartTime'),('PT0002','jun0122','ZIJUN','010113-21-3011','Male','017-432-7833','zijun@gmail.com','2000-03-22','Activate','PartTime'),('PT0003','HuiChyi01','HUICHYI','010221-88-1033','Female','012-718-4912','huichyi@gmail.com','1999-10-02','Activate','PartTime'),('PT0005','jacelyn944','JACELYN','010122-11-3411','Female','018-221-9931','jacelyn@gmail.com','2002-01-09','Activate','PartTime'),('PT9999','PT9999','HARRY JUNE','010402-99-1211','Male','011-8888888','harry@net.com','15/10/2001','Active','PartTime');
/*!40000 ALTER TABLE `parttimestaff` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentrecord`
--

DROP TABLE IF EXISTS `paymentrecord`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paymentrecord` (
  `referenceNo` varchar(45) NOT NULL,
  `studentID` varchar(45) NOT NULL,
  `date` varchar(10) NOT NULL,
  `item` varchar(45) NOT NULL,
  `price` double NOT NULL,
  `paid` double NOT NULL,
  `balance` double NOT NULL,
  PRIMARY KEY (`referenceNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentrecord`
--

LOCK TABLES `paymentrecord` WRITE;
/*!40000 ALTER TABLE `paymentrecord` DISABLE KEYS */;
INSERT INTO `paymentrecord` VALUES ('P     1','20WMR12046','2021-12-13','BMIT2164',1036,2000,964),('P000002','20WMR12046','2021-12-13','BSEX4269',69,90,21),('P000003','20WMR12046','2021-12-13','BAIT1083',777,1000,223),('P000004','20WMR12046','2021-12-13','BACS2023',777,1000,223),('P000005','20WMR12046','2021-12-13','BACS2023',777,2000,1223),('P000006','20WMR12046','2021-12-13','BAIT2004',1036,1100,64),('P000007','20WMR12046','2021-12-13','BAIT1083',777,777,0),('P000008','20WMR12046','2021-12-13','BMIT2164',1036,2000,964),('P000009','20WMR12046','2021-12-13','Cocu0008',0,0,0),('P000010','20WMR12046','2021-12-13','BSEX4269',69,69,0);
/*!40000 ALTER TABLE `paymentrecord` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-13 19:13:44
