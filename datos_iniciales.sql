-- MySQL dump 10.13  Distrib 8.0.44, for Linux (x86_64)
--
-- Host: localhost    Database: db_foodix
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categoria`
--

DROP TABLE IF EXISTS `categoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` text,
  `estado` bit(1) NOT NULL,
  `icono` varchar(100) DEFAULT NULL,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria`
--

LOCK TABLES `categoria` WRITE;
/*!40000 ALTER TABLE `categoria` DISABLE KEYS */;
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (1,'Pollo a la brasa y parrillas',_binary '','fa-drumstick-bite','Poller????a');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (2,'Ceviches y comida marina',_binary '','fa-fish','Cevicher????a');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (3,'Comida norte????a y chicharrones',_binary '','fa-utensils','Chaufer????a');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (4,'Restaurantes de mariscos',_binary '','fa-shrimp','Mariscos');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (5,'Platos tradicionales peruanos',_binary '','fa-plate-wheat','Comida Criolla');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (6,'Comida fusi????n chino-peruana',_binary '','fa-bowl-rice','Chifa');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (7,'Pizzas y comida italiana',_binary '','fa-pizza-slice','Pizzer????a');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (8,'Hamburguesas y fast food',_binary '','fa-burger','Hamburguesas');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (9,'Helader????as y postres',_binary '','fa-ice-cream','Postres');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (10,'Caf????s y bebidas',_binary '','fa-mug-hot','Cafeter????a');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (11,'Opciones vegetarianas y veganas',_binary '','fa-leaf','Comida Vegetariana');
INSERT INTO `categoria` (`codigo`, `descripcion`, `estado`, `icono`, `nombre`) VALUES (12,'Comida japonesa',_binary '','fa-fish-fins','Sushi');
/*!40000 ALTER TABLE `categoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria_restaurante`
--

DROP TABLE IF EXISTS `categoria_restaurante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categoria_restaurante` (
  `codigo_categoria` bigint NOT NULL,
  `codigo_restaurante` bigint NOT NULL,
  PRIMARY KEY (`codigo_categoria`,`codigo_restaurante`),
  KEY `FKr9s55ujsgwiwydufi04b4acuu` (`codigo_restaurante`),
  CONSTRAINT `FKjh1ymbdnw2gwwt73sg2vjm57y` FOREIGN KEY (`codigo_categoria`) REFERENCES `categoria` (`codigo`),
  CONSTRAINT `FKr9s55ujsgwiwydufi04b4acuu` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria_restaurante`
--

LOCK TABLES `categoria_restaurante` WRITE;
/*!40000 ALTER TABLE `categoria_restaurante` DISABLE KEYS */;
/*!40000 ALTER TABLE `categoria_restaurante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamento` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `estado` bit(1) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamento`
--

LOCK TABLES `departamento` WRITE;
/*!40000 ALTER TABLE `departamento` DISABLE KEYS */;
INSERT INTO `departamento` (`codigo`, `estado`, `nombre`) VALUES (11,_binary '','Lambayeque');
/*!40000 ALTER TABLE `departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `distrito`
--

DROP TABLE IF EXISTS `distrito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `distrito` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_provincia` bigint NOT NULL,
  `estado` bit(1) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `distrito`
--

LOCK TABLES `distrito` WRITE;
/*!40000 ALTER TABLE `distrito` DISABLE KEYS */;
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (1,1,_binary '','Chiclayo');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (2,1,_binary '','Chongoyape');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (3,1,_binary '','Eten');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (4,1,_binary '','Eten Puerto');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (5,1,_binary '','Jos?? Leonardo Ortiz');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (6,1,_binary '','La Victoria');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (7,1,_binary '','Lagunas');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (8,1,_binary '','Monsef??');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (9,1,_binary '','Nueva Arica');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (10,1,_binary '','Oyot??n');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (11,1,_binary '','P??tapo');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (12,1,_binary '','Picsi');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (13,1,_binary '','Pimentel');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (14,1,_binary '','Pomalca');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (15,1,_binary '','Pucal??');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (16,1,_binary '','Reque');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (17,1,_binary '','Santa Rosa');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (18,1,_binary '','Sa??a');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (19,1,_binary '','Cayalt??');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (20,1,_binary '','Tum??n');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (21,2,_binary '','Lambayeque');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (22,2,_binary '','Ch??chope');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (23,2,_binary '','Illimo');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (24,2,_binary '','Jayanca');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (25,2,_binary '','Mochumi');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (26,2,_binary '','M??rrope');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (27,2,_binary '','Motupe');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (28,2,_binary '','Olmos');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (29,2,_binary '','Pacora');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (30,2,_binary '','Salas');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (31,2,_binary '','San Jos??');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (32,2,_binary '','T??cume');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (33,3,_binary '','Ferre??afe');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (34,3,_binary '','Ca??aris');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (35,3,_binary '','Incahuasi');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (36,3,_binary '','Manuel Antonio Mesones Muro');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (37,3,_binary '','P??tipo');
INSERT INTO `distrito` (`codigo`, `codigo_provincia`, `estado`, `nombre`) VALUES (38,3,_binary '','Pueblo Nuevo');
/*!40000 ALTER TABLE `distrito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documento_repartidor`
--

DROP TABLE IF EXISTS `documento_repartidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documento_repartidor` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_repartidor` bigint NOT NULL,
  `estado` bit(1) NOT NULL,
  `fecha_subida` datetime(6) NOT NULL,
  `fecha_vencimiento` datetime(6) DEFAULT NULL,
  `ruta_archivo` varchar(255) NOT NULL,
  `tipo_documento` enum('AntecedentesPolicial','Licencia','SOAT','TarjetaPropiedad') NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FKkbg2rqt2skqweq0j05qknqtbx` (`codigo_repartidor`),
  CONSTRAINT `FKkbg2rqt2skqweq0j05qknqtbx` FOREIGN KEY (`codigo_repartidor`) REFERENCES `repartidor` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento_repartidor`
--

LOCK TABLES `documento_repartidor` WRITE;
/*!40000 ALTER TABLE `documento_repartidor` DISABLE KEYS */;
/*!40000 ALTER TABLE `documento_repartidor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documento_restaurante`
--

DROP TABLE IF EXISTS `documento_restaurante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documento_restaurante` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_restaurante` bigint NOT NULL,
  `estado` bit(1) NOT NULL,
  `fecha_subida` datetime(6) NOT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `ruta_archivo` varchar(255) NOT NULL,
  `tipo_documento` enum('CarnetSanidad','LicenciaFuncionamiento','Otros','RUC') NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FKh50ugf4l2r7wcrixkbvu1ha8n` (`codigo_restaurante`),
  CONSTRAINT `FKh50ugf4l2r7wcrixkbvu1ha8n` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento_restaurante`
--

LOCK TABLES `documento_restaurante` WRITE;
/*!40000 ALTER TABLE `documento_restaurante` DISABLE KEYS */;
/*!40000 ALTER TABLE `documento_restaurante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_aprobacion`
--

DROP TABLE IF EXISTS `estado_aprobacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estado_aprobacion` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) DEFAULT NULL,
  `estado` bit(1) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_aprobacion`
--

LOCK TABLES `estado_aprobacion` WRITE;
/*!40000 ALTER TABLE `estado_aprobacion` DISABLE KEYS */;
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (7,'Solicitud en revisi??n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (8,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (9,'Solicitud rechazada',_binary '','Rechazado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (10,'Solicitud en revisi????n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (11,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (12,'Solicitud rechazada',_binary '','Rechazado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (13,'Solicitud en revisi????n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (14,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (15,'Solicitud rechazada',_binary '','Rechazado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (16,'Solicitud en revisi????n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (17,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (18,'Solicitud rechazada',_binary '','Rechazado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (19,'Solicitud en revisi????n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (20,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (21,'Solicitud rechazada',_binary '','Rechazado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (22,'Solicitud en revisi????n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (23,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (24,'Solicitud rechazada',_binary '','Rechazado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (25,'Solicitud en revisi??n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (26,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (27,'Solicitud rechazada',_binary '','Rechazado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (28,'Solicitud en revisi??n',_binary '','Pendiente');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (29,'Solicitud aprobada',_binary '','Aprobado');
INSERT INTO `estado_aprobacion` (`codigo`, `descripcion`, `estado`, `nombre`) VALUES (30,'Solicitud rechazada',_binary '','Rechazado');
/*!40000 ALTER TABLE `estado_aprobacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `imagen_restaurante`
--

DROP TABLE IF EXISTS `imagen_restaurante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `imagen_restaurante` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_restaurante` bigint NOT NULL,
  `estado` bit(1) NOT NULL,
  `fecha_subida` datetime(6) NOT NULL,
  `orden` int DEFAULT NULL,
  `ruta_imagen` varchar(255) NOT NULL,
  `tipo_imagen` enum('Galeria','Logo','Portada') NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FKerryencl6dqeh2rvkn7k28tf7` (`codigo_restaurante`),
  CONSTRAINT `FKerryencl6dqeh2rvkn7k28tf7` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `imagen_restaurante`
--

LOCK TABLES `imagen_restaurante` WRITE;
/*!40000 ALTER TABLE `imagen_restaurante` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagen_restaurante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provincia`
--

DROP TABLE IF EXISTS `provincia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provincia` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_departamento` bigint NOT NULL,
  `estado` bit(1) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provincia`
--

LOCK TABLES `provincia` WRITE;
/*!40000 ALTER TABLE `provincia` DISABLE KEYS */;
INSERT INTO `provincia` (`codigo`, `codigo_departamento`, `estado`, `nombre`) VALUES (1,11,_binary '','Chiclayo');
INSERT INTO `provincia` (`codigo`, `codigo_departamento`, `estado`, `nombre`) VALUES (2,11,_binary '','Lambayeque');
INSERT INTO `provincia` (`codigo`, `codigo_departamento`, `estado`, `nombre`) VALUES (3,11,_binary '','Ferre??afe');
/*!40000 ALTER TABLE `provincia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repartidor`
--

DROP TABLE IF EXISTS `repartidor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `repartidor` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_aprobador` bigint DEFAULT NULL,
  `codigo_estado_aprobacion` bigint NOT NULL,
  `codigo_tipo_vehiculo` bigint NOT NULL,
  `codigo_usuario` bigint NOT NULL,
  `disponible` bit(1) NOT NULL,
  `estado` bit(1) NOT NULL,
  `fecha_aprobacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `motivo_rechazo` text,
  `numero_licencia` varchar(15) NOT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UKkkmdh2oy77utd3jektr1y9ibf` (`numero_licencia`),
  KEY `FKjpcuxnrgek7psma601j3aby0k` (`codigo_estado_aprobacion`),
  KEY `FKpb4ivf19k1ca5eenmll5b5ju9` (`codigo_tipo_vehiculo`),
  KEY `FKhyrxfcjeffef656xqqccby1m3` (`codigo_usuario`),
  CONSTRAINT `FKhyrxfcjeffef656xqqccby1m3` FOREIGN KEY (`codigo_usuario`) REFERENCES `usuario` (`codigo`),
  CONSTRAINT `FKjpcuxnrgek7psma601j3aby0k` FOREIGN KEY (`codigo_estado_aprobacion`) REFERENCES `estado_aprobacion` (`codigo`),
  CONSTRAINT `FKpb4ivf19k1ca5eenmll5b5ju9` FOREIGN KEY (`codigo_tipo_vehiculo`) REFERENCES `tipo_vehiculo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repartidor`
--

LOCK TABLES `repartidor` WRITE;
/*!40000 ALTER TABLE `repartidor` DISABLE KEYS */;
/*!40000 ALTER TABLE `repartidor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurante`
--

DROP TABLE IF EXISTS `restaurante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurante` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_aprobador` bigint DEFAULT NULL,
  `codigo_distrito` bigint NOT NULL,
  `codigo_estado_aprobacion` bigint NOT NULL,
  `codigo_usuario` bigint NOT NULL,
  `correo_electronico` varchar(150) DEFAULT NULL,
  `descripcion` text,
  `direccion` varchar(200) DEFAULT NULL,
  `estado` bit(1) NOT NULL,
  `fecha_aprobacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `motivo_rechazo` text,
  `nombre` varchar(100) NOT NULL,
  `razon_social` varchar(150) NOT NULL,
  `ruc` varchar(15) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UKdo0m21fnl5yubgihvdsfu0loq` (`ruc`),
  KEY `FKc11x0rvljomamy1gj34blwfc4` (`codigo_distrito`),
  KEY `FKfik6icyogt8l5areb193cp9v0` (`codigo_estado_aprobacion`),
  KEY `FKbedox8d8y87q26n7m93dd8u03` (`codigo_usuario`),
  CONSTRAINT `FKbedox8d8y87q26n7m93dd8u03` FOREIGN KEY (`codigo_usuario`) REFERENCES `usuario` (`codigo`),
  CONSTRAINT `FKc11x0rvljomamy1gj34blwfc4` FOREIGN KEY (`codigo_distrito`) REFERENCES `distrito` (`codigo`),
  CONSTRAINT `FKfik6icyogt8l5areb193cp9v0` FOREIGN KEY (`codigo_estado_aprobacion`) REFERENCES `estado_aprobacion` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurante`
--

LOCK TABLES `restaurante` WRITE;
/*!40000 ALTER TABLE `restaurante` DISABLE KEYS */;
INSERT INTO `restaurante` (`codigo`, `codigo_aprobador`, `codigo_distrito`, `codigo_estado_aprobacion`, `codigo_usuario`, `correo_electronico`, `descripcion`, `direccion`, `estado`, `fecha_aprobacion`, `fecha_creacion`, `motivo_rechazo`, `nombre`, `razon_social`, `ruc`, `telefono`) VALUES (2,1,1,8,3,'danieladaniela@gmail.com','Lo mejor en comida','AV. GARCILASO DE LA VEGA NRO. 1472, LIMA, LIMA, LIMA',_binary '','2025-11-10 18:37:49.840752','2025-11-10 06:16:02.000000',NULL,'SUPERINTENDENCIA NACIONAL DE ADUANAS Y DE ADMINISTRACION TRIBUTARIA - SUNAT','SUPERINTENDENCIA NACIONAL DE ADUANAS Y DE ADMINISTRACION TRIBUTARIA - SUNAT','20131312955','975184139');
INSERT INTO `restaurante` (`codigo`, `codigo_aprobador`, `codigo_distrito`, `codigo_estado_aprobacion`, `codigo_usuario`, `correo_electronico`, `descripcion`, `direccion`, `estado`, `fecha_aprobacion`, `fecha_creacion`, `motivo_rechazo`, `nombre`, `razon_social`, `ruc`, `telefono`) VALUES (3,1,33,8,7,'contacto@sabornorteno.com','Especialidad en comida norte??a tradicional: seco de cabrito, arroz con pato, ceviche de conchas negras','Av. Balta 350, Chiclayo',_binary '','2025-11-13 03:41:11.212224','2025-11-13 03:02:23.000000',NULL,'El Sabor Norte??o','EL SABOR NORTE??O S.A.C.','20567891234','974123456');
INSERT INTO `restaurante` (`codigo`, `codigo_aprobador`, `codigo_distrito`, `codigo_estado_aprobacion`, `codigo_usuario`, `correo_electronico`, `descripcion`, `direccion`, `estado`, `fecha_aprobacion`, `fecha_creacion`, `motivo_rechazo`, `nombre`, `razon_social`, `ruc`, `telefono`) VALUES (4,NULL,1,7,6,'ventas@casaempanadas.com','Empanadas artesanales rellenas de carne, pollo, queso, verduras. Tambi??n ofrecemos salte??os y pastelitos','Jr. Ucayali 456, Lima Cercado',_binary '',NULL,'2025-11-13 03:02:23.000000',NULL,'La Casa de las Empanadas','EMPANADAS Y M??S S.R.L.','20678912345','965234567');
INSERT INTO `restaurante` (`codigo`, `codigo_aprobador`, `codigo_distrito`, `codigo_estado_aprobacion`, `codigo_usuario`, `correo_electronico`, `descripcion`, `direccion`, `estado`, `fecha_aprobacion`, `fecha_creacion`, `motivo_rechazo`, `nombre`, `razon_social`, `ruc`, `telefono`) VALUES (5,NULL,24,7,5,'pedidos@pollosparrillas.com','Pollos a la brasa, parrillas mixtas, anticuchos, chorizos. Delivery r??pido y atenci??n 24/7','Av. Salaverry 789, Trujillo',_binary '',NULL,'2025-11-13 03:02:23.000000',NULL,'Pollos & Parrillas Express','POLLOS Y PARRILLAS EXPRESS E.I.R.L.','20789123456','987345678');
/*!40000 ALTER TABLE `restaurante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `estado` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_nombre_rol` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` (`codigo`, `nombre`, `descripcion`, `estado`) VALUES (1,'ADMINISTRADOR','Administrador del sistema FooDix',_binary '');
INSERT INTO `rol` (`codigo`, `nombre`, `descripcion`, `estado`) VALUES (2,'RESTAURANTE','Propietario de restaurante',_binary '');
INSERT INTO `rol` (`codigo`, `nombre`, `descripcion`, `estado`) VALUES (3,'REPARTIDOR','Repartidor de pedidos',_binary '');
INSERT INTO `rol` (`codigo`, `nombre`, `descripcion`, `estado`) VALUES (4,'USUARIO','Usuario cliente del sistema',_binary '');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_vehiculo`
--

DROP TABLE IF EXISTS `tipo_vehiculo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tipo_vehiculo` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `estado` bit(1) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_vehiculo`
--

LOCK TABLES `tipo_vehiculo` WRITE;
/*!40000 ALTER TABLE `tipo_vehiculo` DISABLE KEYS */;
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (9,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (10,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (11,_binary '','Scooter El??ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (12,_binary '','Autom??vil');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (13,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (14,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (15,_binary '','Scooter El????ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (16,_binary '','Autom????vil');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (17,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (18,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (19,_binary '','Scooter El????ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (20,_binary '','Autom????vil');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (21,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (22,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (23,_binary '','Scooter El????ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (24,_binary '','Autom????vil');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (25,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (26,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (27,_binary '','Scooter El????ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (28,_binary '','Autom????vil');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (29,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (30,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (31,_binary '','Scooter El????ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (32,_binary '','Autom????vil');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (33,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (34,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (35,_binary '','Scooter El??ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (36,_binary '','Autom??vil');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (37,_binary '','Bicicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (38,_binary '','Motocicleta');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (39,_binary '','Scooter El??ctrico');
INSERT INTO `tipo_vehiculo` (`codigo`, `estado`, `nombre`) VALUES (40,_binary '','Autom??vil');
/*!40000 ALTER TABLE `tipo_vehiculo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `apellido_materno` varchar(50) NOT NULL,
  `apellido_paterno` varchar(50) NOT NULL,
  `codigo_distrito` bigint NOT NULL,
  `codigo_rol` bigint NOT NULL,
  `codigo_tipo_documento` bigint NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `correo_electronico` varchar(50) NOT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `estado` bit(1) NOT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `fecha_modificacion` datetime(6) DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `numero_documento` varchar(15) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UKf7w2jekriedf7k6a4kaclt9t7` (`correo_electronico`),
  UNIQUE KEY `UKcyd6xjxln9p38cm60dkrox5no` (`numero_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` (`codigo`, `apellido_materno`, `apellido_paterno`, `codigo_distrito`, `codigo_rol`, `codigo_tipo_documento`, `contrasena`, `correo_electronico`, `direccion`, `estado`, `fecha_creacion`, `fecha_modificacion`, `fecha_nacimiento`, `nombre`, `numero_documento`, `telefono`) VALUES (1,'FooDix','Administrador',1,1,1,'$2a$10$ktinS55BjqW/wCvkPar.Au5VjwqTd2ZsvPO6Ze/A49ylKS9xArPJ.','daniela@FooDix.com.pe','Admin Address',_binary '','2025-11-09 19:04:49.000000',NULL,'1990-01-01','Daniela','99999999','999999999');
INSERT INTO `usuario` (`codigo`, `apellido_materno`, `apellido_paterno`, `codigo_distrito`, `codigo_rol`, `codigo_tipo_documento`, `contrasena`, `correo_electronico`, `direccion`, `estado`, `fecha_creacion`, `fecha_modificacion`, `fecha_nacimiento`, `nombre`, `numero_documento`, `telefono`) VALUES (3,'HERRERA','DIAZ',21,4,1,'Andrekaileth2!','danndiazherrera@gmail.com','No',_binary '','2025-11-10 06:16:02.000000','2025-11-10 06:16:02.000000','2003-02-27','DANIELA ANDREINA','90000422','975184139');
INSERT INTO `usuario` (`codigo`, `apellido_materno`, `apellido_paterno`, `codigo_distrito`, `codigo_rol`, `codigo_tipo_documento`, `contrasena`, `correo_electronico`, `direccion`, `estado`, `fecha_creacion`, `fecha_modificacion`, `fecha_nacimiento`, `nombre`, `numero_documento`, `telefono`) VALUES (4,'OLAZABAL','DIAZ',24,4,1,'$2a$10$qN038Ytc3d0SNZX/AjdalOo5lE1bm0LTYEnxrzknMjRSH68uvKKly','U22302426@utp.edu.pe','Faustino Sanchez Carrion 258',_binary '','2025-11-10 16:24:41.000000','2025-11-10 16:24:41.000000','1973-01-11','CARLOS ALBERTO','17446043','994518225');
INSERT INTO `usuario` (`codigo`, `apellido_materno`, `apellido_paterno`, `codigo_distrito`, `codigo_rol`, `codigo_tipo_documento`, `contrasena`, `correo_electronico`, `direccion`, `estado`, `fecha_creacion`, `fecha_modificacion`, `fecha_nacimiento`, `nombre`, `numero_documento`, `telefono`) VALUES (5,'GARCIA','PEREZ',33,2,1,'$2a$10$ktinS55BjqW/wCvkPar.Au5VjwqTd2ZsvPO6Ze/A49ylKS9xArPJ.','juan.perez@sabornorteno.com','Av. Balta 350, Chiclayo',_binary '','2025-11-13 03:02:23.000000','2025-11-13 03:02:23.000000','1985-03-15','JUAN','45678912','974123456');
INSERT INTO `usuario` (`codigo`, `apellido_materno`, `apellido_paterno`, `codigo_distrito`, `codigo_rol`, `codigo_tipo_documento`, `contrasena`, `correo_electronico`, `direccion`, `estado`, `fecha_creacion`, `fecha_modificacion`, `fecha_nacimiento`, `nombre`, `numero_documento`, `telefono`) VALUES (6,'LOPEZ','RODRIGUEZ',1,2,1,'$2a$10$ktinS55BjqW/wCvkPar.Au5VjwqTd2ZsvPO6Ze/A49ylKS9xArPJ.','maria.rodriguez@casaempanadas.com','Jr. Ucayali 456, Lima',_binary '','2025-11-13 03:02:23.000000','2025-11-13 03:02:23.000000','1990-07-22','MARIA','56789123','965234567');
INSERT INTO `usuario` (`codigo`, `apellido_materno`, `apellido_paterno`, `codigo_distrito`, `codigo_rol`, `codigo_tipo_documento`, `contrasena`, `correo_electronico`, `direccion`, `estado`, `fecha_creacion`, `fecha_modificacion`, `fecha_nacimiento`, `nombre`, `numero_documento`, `telefono`) VALUES (7,'SANCHEZ','TORRES',24,2,1,'$2a$10$ktinS55BjqW/wCvkPar.Au5VjwqTd2ZsvPO6Ze/A49ylKS9xArPJ.','carlos.torres@pollosparrillas.com','Av. Salaverry 789, Trujillo',_binary '','2025-11-13 03:02:23.000000','2025-11-13 03:02:23.000000','1988-11-10','CARLOS','67891234','987345678');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-13  3:47:22

-- =====================================================
-- SISTEMA DE PERMISOS GRANULARES
-- Permisos específicos para cada acción en cada sección
-- Fecha: 2025-11-17
-- =====================================================

-- Crear tabla de permisos
CREATE TABLE IF NOT EXISTS `permiso` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `seccion` varchar(50) NOT NULL COMMENT 'Sección del menú: usuarios, clientes, restaurantes, delivery, categorias, configuracion',
  `accion` varchar(50) DEFAULT NULL COMMENT 'Acción: Ver, Crear, Editar, Eliminar, Aprobar, Rechazar',
  `estado` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_nombre_permiso` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Crear tabla intermedia para relación ManyToMany entre Rol y Permiso
CREATE TABLE IF NOT EXISTS `rol_permiso` (
  `rol_codigo` bigint NOT NULL,
  `permiso_codigo` bigint NOT NULL,
  PRIMARY KEY (`rol_codigo`,`permiso_codigo`),
  KEY `FK_rol_permiso_permiso` (`permiso_codigo`),
  CONSTRAINT `FK_rol_permiso_permiso` FOREIGN KEY (`permiso_codigo`) REFERENCES `permiso` (`codigo`) ON DELETE CASCADE,
  CONSTRAINT `FK_rol_permiso_rol` FOREIGN KEY (`rol_codigo`) REFERENCES `rol` (`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Limpiar permisos existentes (si los hay)
DELETE FROM rol_permiso;
DELETE FROM permiso;

-- =====================================================
-- PERMISOS PARA USUARIOS DEL SISTEMA
-- =====================================================
INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('USUARIOS_VER', 'Ver lista de usuarios del sistema', 'usuarios', 'Ver', b'1'),
('USUARIOS_CREAR', 'Crear nuevos usuarios del sistema', 'usuarios', 'Crear', b'1'),
('USUARIOS_EDITAR', 'Editar información de usuarios existentes', 'usuarios', 'Editar', b'1'),
('USUARIOS_ELIMINAR', 'Eliminar usuarios del sistema', 'usuarios', 'Eliminar', b'1'),
('USUARIOS_CAMBIAR_ESTADO', 'Activar/desactivar usuarios', 'usuarios', 'Gestionar', b'1'),
('USUARIOS_ASIGNAR_ROL', 'Cambiar el rol de un usuario', 'usuarios', 'Gestionar', b'1');

-- =====================================================
-- PERMISOS PARA CLIENTES
-- =====================================================
INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('CLIENTES_VER', 'Ver lista de clientes registrados', 'clientes', 'Ver', b'1'),
('CLIENTES_VER_DETALLE', 'Ver información detallada de un cliente', 'clientes', 'Ver', b'1'),
('CLIENTES_EDITAR', 'Editar información de clientes', 'clientes', 'Editar', b'1'),
('CLIENTES_ELIMINAR', 'Eliminar clientes del sistema', 'clientes', 'Eliminar', b'1'),
('CLIENTES_CAMBIAR_ESTADO', 'Activar/desactivar clientes', 'clientes', 'Gestionar', b'1');

-- =====================================================
-- PERMISOS PARA RESTAURANTES
-- =====================================================
INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('RESTAURANTES_VER', 'Ver lista de restaurantes registrados', 'restaurantes', 'Ver', b'1'),
('RESTAURANTES_VER_DETALLE', 'Ver información detallada de un restaurante', 'restaurantes', 'Ver', b'1'),
('RESTAURANTES_APROBAR', 'Aprobar solicitudes de registro de restaurantes', 'restaurantes', 'Aprobar', b'1'),
('RESTAURANTES_RECHAZAR', 'Rechazar solicitudes de registro de restaurantes', 'restaurantes', 'Rechazar', b'1'),
('RESTAURANTES_EDITAR', 'Editar información de restaurantes', 'restaurantes', 'Editar', b'1'),
('RESTAURANTES_ELIMINAR', 'Eliminar restaurantes del sistema', 'restaurantes', 'Eliminar', b'1'),
('RESTAURANTES_CAMBIAR_ESTADO', 'Activar/desactivar restaurantes', 'restaurantes', 'Gestionar', b'1');

-- =====================================================
-- PERMISOS PARA REPARTIDORES (DELIVERY)
-- =====================================================
INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('DELIVERY_VER', 'Ver lista de repartidores registrados', 'delivery', 'Ver', b'1'),
('DELIVERY_VER_DETALLE', 'Ver información detallada de un repartidor', 'delivery', 'Ver', b'1'),
('DELIVERY_APROBAR', 'Aprobar solicitudes de registro de repartidores', 'delivery', 'Aprobar', b'1'),
('DELIVERY_RECHAZAR', 'Rechazar solicitudes de registro de repartidores', 'delivery', 'Rechazar', b'1'),
('DELIVERY_EDITAR', 'Editar información de repartidores', 'delivery', 'Editar', b'1'),
('DELIVERY_ELIMINAR', 'Eliminar repartidores del sistema', 'delivery', 'Eliminar', b'1'),
('DELIVERY_CAMBIAR_ESTADO', 'Activar/desactivar repartidores', 'delivery', 'Gestionar', b'1');

-- =====================================================
-- PERMISOS PARA CATEGORÍAS
-- =====================================================
INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('CATEGORIAS_VER', 'Ver lista de categorías de productos', 'categorias', 'Ver', b'1'),
('CATEGORIAS_CREAR', 'Crear nuevas categorías', 'categorias', 'Crear', b'1'),
('CATEGORIAS_EDITAR', 'Editar categorías existentes', 'categorias', 'Editar', b'1'),
('CATEGORIAS_ELIMINAR', 'Eliminar categorías', 'categorias', 'Eliminar', b'1'),
('CATEGORIAS_CAMBIAR_ESTADO', 'Activar/desactivar categorías', 'categorias', 'Gestionar', b'1');

-- =====================================================
-- PERMISOS PARA CONFIGURACIÓN Y ROLES
-- =====================================================
INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('CONFIGURACION_VER', 'Ver sección de configuración del sistema', 'configuracion', 'Ver', b'1'),
('ROLES_VER', 'Ver lista de roles del sistema', 'configuracion', 'Ver', b'1'),
('ROLES_CREAR', 'Crear nuevos roles', 'configuracion', 'Crear', b'1'),
('ROLES_EDITAR', 'Editar roles existentes', 'configuracion', 'Editar', b'1'),
('ROLES_ELIMINAR', 'Eliminar roles', 'configuracion', 'Eliminar', b'1'),
('ROLES_ASIGNAR_PERMISOS', 'Asignar/modificar permisos de roles', 'configuracion', 'Gestionar', b'1'),
('ROLES_CAMBIAR_ESTADO', 'Activar/desactivar roles', 'configuracion', 'Gestionar', b'1');

-- =====================================================
-- ASIGNAR TODOS LOS PERMISOS AL ROL ADMINISTRADOR
-- =====================================================
INSERT INTO `rol_permiso` (`rol_codigo`, `permiso_codigo`)
SELECT 1, codigo FROM permiso WHERE estado = b'1';

-- =====================================================
-- VERIFICACIÓN DE PERMISOS
-- =====================================================
-- Ejecutar estas queries para verificar la correcta instalación:

-- Ver todos los permisos creados por sección
-- SELECT seccion, COUNT(*) as total_permisos FROM permiso WHERE estado = b'1' GROUP BY seccion;

-- Ver permisos del administrador
-- SELECT r.nombre AS rol, COUNT(p.codigo) as total_permisos 
-- FROM rol r 
-- INNER JOIN rol_permiso rp ON r.codigo = rp.rol_codigo 
-- INNER JOIN permiso p ON rp.permiso_codigo = p.codigo 
-- WHERE r.codigo = 1;

-- =====================================================
-- FIN DEL SCRIPT DE DATOS INICIALES
-- =====================================================
