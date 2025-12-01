-- =====================================================
-- BASE DE DATOS FOODIX - ESTRUCTURA CON USUARIOS UNIFICADOS
-- Fecha: 2025-11-23
-- ESTADO = BOOLEAN (TRUE/FALSE) - Compatible 100%
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- TABLA: tipo_documento
-- =====================================================
DROP TABLE IF EXISTS `tipo_documento`;
CREATE TABLE `tipo_documento` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_nombre_tipo_documento` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tipo_documento` VALUES
(1, 'DNI', 'Documento Nacional de Identidad', TRUE),
(2, 'Carnet de Extranjería', 'Documento de identidad para extranjeros', TRUE),
(3, 'Pasaporte', 'Documento de identidad internacional', TRUE);

-- =====================================================
-- TABLA: categoria
-- =====================================================
DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` text,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `icono` varchar(100) DEFAULT NULL,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `categoria` VALUES 
(1, 'Pollo a la brasa y parrillas', TRUE, 'fa-drumstick-bite', 'Pollería'),
(2, 'Ceviches y comida marina', TRUE, 'fa-fish', 'Cevichería'),
(3, 'Comida norteña y chicharrones', TRUE, 'fa-utensils', 'Chaufería'),
(4, 'Restaurantes de mariscos', TRUE, 'fa-shrimp', 'Mariscos'),
(5, 'Platos tradicionales peruanos', TRUE, 'fa-plate-wheat', 'Comida Criolla'),
(6, 'Comida fusión chino-peruana', TRUE, 'fa-bowl-rice', 'Chifa'),
(7, 'Pizzas y comida italiana', TRUE, 'fa-pizza-slice', 'Pizzería'),
(8, 'Hamburguesas y fast food', TRUE, 'fa-burger', 'Hamburguesas'),
(9, 'Heladerías y postres', TRUE, 'fa-ice-cream', 'Postres'),
(10, 'Cafés y bebidas', TRUE, 'fa-mug-hot', 'Cafetería'),
(11, 'Opciones vegetarianas y veganas', TRUE, 'fa-leaf', 'Comida Vegetariana'),
(12, 'Comida japonesa', TRUE, 'fa-fish-fins', 'Sushi');

-- =====================================================
-- TABLA: departamento
-- =====================================================
DROP TABLE IF EXISTS `departamento`;
CREATE TABLE `departamento` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `departamento` VALUES (11, TRUE, 'Lambayeque');

-- =====================================================
-- TABLA: provincia (CON FOREIGN KEY)
-- =====================================================
DROP TABLE IF EXISTS `provincia`;
CREATE TABLE `provincia` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_departamento` bigint NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_provincia_departamento` (`codigo_departamento`),
  CONSTRAINT `FK_provincia_departamento` FOREIGN KEY (`codigo_departamento`) REFERENCES `departamento` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `provincia` VALUES 
(1, 11, TRUE, 'Chiclayo'),
(2, 11, TRUE, 'Lambayeque'),
(3, 11, TRUE, 'Ferreñafe');

-- =====================================================
-- TABLA: distrito
-- =====================================================
DROP TABLE IF EXISTS `distrito`;
CREATE TABLE `distrito` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_provincia` bigint NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_distrito_provincia` (`codigo_provincia`),
  CONSTRAINT `FK_distrito_provincia` FOREIGN KEY (`codigo_provincia`) REFERENCES `provincia` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `distrito` VALUES 
(1, 1, TRUE, 'Chiclayo'),
(2, 1, TRUE, 'Chongoyape'),
(3, 1, TRUE, 'Eten'),
(4, 1, TRUE, 'Eten Puerto'),
(5, 1, TRUE, 'José Leonardo Ortiz'),
(6, 1, TRUE, 'La Victoria'),
(7, 1, TRUE, 'Lagunas'),
(8, 1, TRUE, 'Monsefú'),
(9, 1, TRUE, 'Nueva Arica'),
(10, 1, TRUE, 'Oyotún'),
(11, 1, TRUE, 'Pátapo'),
(12, 1, TRUE, 'Picsi'),
(13, 1, TRUE, 'Pimentel'),
(14, 1, TRUE, 'Pomalca'),
(15, 1, TRUE, 'Pucalá'),
(16, 1, TRUE, 'Reque'),
(17, 1, TRUE, 'Santa Rosa'),
(18, 1, TRUE, 'Saña'),
(19, 1, TRUE, 'Cayaltí'),
(20, 1, TRUE, 'Tumán'),
(21, 2, TRUE, 'Lambayeque'),
(22, 2, TRUE, 'Chóchope'),
(23, 2, TRUE, 'Illimo'),
(24, 2, TRUE, 'Jayanca'),
(25, 2, TRUE, 'Mochumi'),
(26, 2, TRUE, 'Mórrope'),
(27, 2, TRUE, 'Motupe'),
(28, 2, TRUE, 'Olmos'),
(29, 2, TRUE, 'Pacora'),
(30, 2, TRUE, 'Salas'),
(31, 2, TRUE, 'San José'),
(32, 2, TRUE, 'Túcume'),
(33, 3, TRUE, 'Ferreñafe'),
(34, 3, TRUE, 'Cañaris'),
(35, 3, TRUE, 'Incahuasi'),
(36, 3, TRUE, 'Manuel Antonio Mesones Muro'),
(37, 3, TRUE, 'Pítipo'),
(38, 3, TRUE, 'Pueblo Nuevo');

-- =====================================================
-- TABLA: rol
-- ⚠️ IMPORTANTE: Los códigos son 1=ADMIN, 2=RESTAURANTE, 3=REPARTIDOR, 4=USUARIO
-- Estos valores son constantes definidas en Rol.java
-- =====================================================
DROP TABLE IF EXISTS `rol`;
CREATE TABLE `rol` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_nombre_rol` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `rol` VALUES 
(1, 'ADMINISTRADOR', 'Administrador del sistema FooDix', TRUE),
(2, 'RESTAURANTE', 'Propietario de restaurante', TRUE),
(3, 'REPARTIDOR', 'Repartidor de pedidos', TRUE),
(4, 'USUARIO', 'Usuario cliente del sistema', TRUE);

-- =====================================================
-- TABLA: usuario (TABLA UNIFICADA)
-- =====================================================
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `apellido_materno` varchar(50) NOT NULL,
  `apellido_paterno` varchar(50) NOT NULL,
  `codigo_distrito` bigint NOT NULL DEFAULT 1,
  `codigo_rol` bigint NOT NULL,
  `codigo_tipo_documento` bigint NOT NULL DEFAULT 1,
  `contrasena` varchar(255) NOT NULL,
  `correo_electronico` varchar(50) NOT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_creacion` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `fecha_modificacion` datetime(6) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `nombre` varchar(50) NOT NULL,
  `numero_documento` varchar(15) NOT NULL DEFAULT '00000000',
  `telefono` varchar(20) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `genero` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_correo_electronico` (`correo_electronico`),
  UNIQUE KEY `UK_numero_documento` (`numero_documento`),
  KEY `FK_usuario_distrito` (`codigo_distrito`),
  KEY `FK_usuario_rol` (`codigo_rol`),
  KEY `FK_usuario_tipo_documento` (`codigo_tipo_documento`),
  CONSTRAINT `FK_usuario_distrito` FOREIGN KEY (`codigo_distrito`) REFERENCES `distrito` (`codigo`),
  CONSTRAINT `FK_usuario_rol` FOREIGN KEY (`codigo_rol`) REFERENCES `rol` (`codigo`),
  CONSTRAINT `FK_usuario_tipo_documento` FOREIGN KEY (`codigo_tipo_documento`) REFERENCES `tipo_documento` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- INSERTAR USUARIOS DE PRUEBA
-- =====================================================
-- ⚠️ NOTA: Todos los usuarios usan la contraseña: Admin123!
-- Hash BCrypt: $2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge
-- Para cambiar la contraseña, usa: http://localhost:8080/api/test-password/hash?password=tucontraseña
-- =====================================================

-- Administradores (Password: Admin123!)
INSERT INTO `usuario` (
  `apellido_materno`, `apellido_paterno`, `codigo_rol`, `contrasena`, 
  `correo_electronico`, `direccion`, `estado`, `nombre`, `telefono`, 
  `numero_documento`, `fecha_creacion`
) VALUES 
('System', 'Admin', 1, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'admin@local.dev', NULL, TRUE, 'Admin', NULL, '99999999', NOW()),

('Diaz', 'Jesus', 1, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'JesusDiaz@gmail.com', NULL, TRUE, 'Jesus', '975184139', '99999998', NOW()),

('Herrera', 'Tania', 1, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'Tania@gmail.com', NULL, TRUE, 'Tania', '994518225', '99999997', NOW());

-- Usuario restaurante (Daniela) - Password: Admin123!
INSERT INTO `usuario` (
  `apellido_materno`, `apellido_paterno`, `codigo_rol`, `contrasena`, 
  `correo_electronico`, `direccion`, `estado`, `nombre`, `telefono`, 
  `numero_documento`, `fecha_nacimiento`, `genero`, `avatar`, `fecha_creacion`
) VALUES 
('Chaname', 'Díaz', 2, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'daniela@test.com', 'Av. Balta 123, Chiclayo', TRUE, 'Daniela', '+51 987654321', 
 '12345678', '1995-05-15', 'femenino', 'Imagen', NOW());

-- Repartidores - Password: Admin123!
INSERT INTO `usuario` (
  `apellido_materno`, `apellido_paterno`, `codigo_rol`, `contrasena`, 
  `correo_electronico`, `direccion`, `estado`, `nombre`, `telefono`, 
  `numero_documento`, `fecha_nacimiento`, `genero`, `avatar`, `fecha_creacion`
) VALUES 
('', 'Diaz', 3, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'dann27@gmail.com', 'Faustino Sanchez Carrion 258', TRUE, 'Daniela', '975184139', 
 '87654321', '2003-02-27', 'femenino', 'Daniela', NOW()),

('', 'Diaz', 3, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'CarlosDiaz@gmail.com', 'Avenida Saleverry', TRUE, 'Carlos', '975184139', 
 '76543210', NULL, NULL, NULL, NOW());

-- Usuarios clientes - Password: Admin123!
INSERT INTO `usuario` (
  `apellido_materno`, `apellido_paterno`, `codigo_rol`, `contrasena`, 
  `correo_electronico`, `direccion`, `estado`, `nombre`, `telefono`, 
  `numero_documento`, `fecha_nacimiento`, `genero`, `avatar`, `fecha_creacion`
) VALUES 
('Herrera', 'Diaz', 4, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'nanisss27@gmail.com', 'Avenida zarumilla', TRUE, 'Daniela Andreina', '975184139', 
 '11223344', '2003-02-27', 'femenino', 'jecha', NOW()),

('Herrera', 'Diaz', 4, '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge', 
 'DanielAnteroJunior@gmail.com', 'Avenida Salaverry', TRUE, 'Daniel Antero', '975184139', 
 '44332211', '2003-02-27', 'femenino', 'jecha', NOW());

-- =====================================================
-- TABLAS RESTANTES DEL SISTEMA (sin productos)
-- =====================================================

-- =====================================================
-- Tabla: estado_aprobacion
-- ⚠️ IMPORTANTE: Los códigos son 7, 8, 9 (NO 1, 2, 3)
-- Este estándar es usado en TODO el código Java:
--   - Repartidor.java (línea 34 y 74): usa 7L
--   - RestauranteJdbcRepository.java (línea 48): usa 7
--   - AdminController.java: usa 8L y 9L
-- =====================================================
DROP TABLE IF EXISTS `estado_aprobacion`;
CREATE TABLE `estado_aprobacion` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `estado_aprobacion` VALUES 
(7, 'Solicitud en revisión', TRUE, 'Pendiente'),
(8, 'Solicitud aprobada', TRUE, 'Aprobado'),
(9, 'Solicitud rechazada', TRUE, 'Rechazado');

-- Tabla: tipo_vehiculo
DROP TABLE IF EXISTS `tipo_vehiculo`;
CREATE TABLE `tipo_vehiculo` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tipo_vehiculo` VALUES 
(9, TRUE, 'Bicicleta'),
(10, TRUE, 'Motocicleta'),
(11, TRUE, 'Scooter Eléctrico'),
(12, TRUE, 'Automóvil');

-- =====================================================
-- TABLA: restaurante
-- =====================================================
DROP TABLE IF EXISTS `restaurante`;
CREATE TABLE `restaurante` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_aprobador` bigint DEFAULT NULL,
  `codigo_distrito` bigint NOT NULL,
  `codigo_estado_aprobacion` bigint NOT NULL,
  `codigo_usuario` bigint NOT NULL,
  `correo_electronico` varchar(150) DEFAULT NULL,
  `descripcion` text,
  `direccion` varchar(200) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_aprobacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `motivo_rechazo` text,
  `nombre` varchar(100) NOT NULL,
  `razon_social` varchar(150) NOT NULL,
  `ruc` varchar(15) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_ruc` (`ruc`),
  KEY `FK_restaurante_distrito` (`codigo_distrito`),
  KEY `FK_restaurante_estado_aprobacion` (`codigo_estado_aprobacion`),
  KEY `FK_restaurante_usuario` (`codigo_usuario`),
  CONSTRAINT `FK_restaurante_distrito` FOREIGN KEY (`codigo_distrito`) REFERENCES `distrito` (`codigo`),
  CONSTRAINT `FK_restaurante_estado_aprobacion` FOREIGN KEY (`codigo_estado_aprobacion`) REFERENCES `estado_aprobacion` (`codigo`),
  CONSTRAINT `FK_restaurante_usuario` FOREIGN KEY (`codigo_usuario`) REFERENCES `usuario` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: categoria_restaurante
-- =====================================================
DROP TABLE IF EXISTS `categoria_restaurante`;
CREATE TABLE `categoria_restaurante` (
  `codigo_categoria` bigint NOT NULL,
  `codigo_restaurante` bigint NOT NULL,
  PRIMARY KEY (`codigo_categoria`,`codigo_restaurante`),
  KEY `FK_categoria_restaurante_restaurante` (`codigo_restaurante`),
  CONSTRAINT `FK_categoria_restaurante_categoria` FOREIGN KEY (`codigo_categoria`) REFERENCES `categoria` (`codigo`),
  CONSTRAINT `FK_categoria_restaurante_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: repartidor
-- =====================================================
DROP TABLE IF EXISTS `repartidor`;
CREATE TABLE `repartidor` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_aprobador` bigint DEFAULT NULL,
  `codigo_estado_aprobacion` bigint NOT NULL,
  `codigo_tipo_vehiculo` bigint NOT NULL,
  `codigo_usuario` bigint NOT NULL,
  `disponible` BOOLEAN NOT NULL DEFAULT FALSE,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_aprobacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `motivo_rechazo` text,
  `numero_licencia` varchar(15) NOT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_numero_licencia` (`numero_licencia`),
  KEY `FK_repartidor_estado_aprobacion` (`codigo_estado_aprobacion`),
  KEY `FK_repartidor_tipo_vehiculo` (`codigo_tipo_vehiculo`),
  KEY `FK_repartidor_usuario` (`codigo_usuario`),
  CONSTRAINT `FK_repartidor_estado_aprobacion` FOREIGN KEY (`codigo_estado_aprobacion`) REFERENCES `estado_aprobacion` (`codigo`),
  CONSTRAINT `FK_repartidor_tipo_vehiculo` FOREIGN KEY (`codigo_tipo_vehiculo`) REFERENCES `tipo_vehiculo` (`codigo`),
  CONSTRAINT `FK_repartidor_usuario` FOREIGN KEY (`codigo_usuario`) REFERENCES `usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLAS DE DOCUMENTOS E IMÁGENES
-- =====================================================

DROP TABLE IF EXISTS `imagen_restaurante`;
CREATE TABLE `imagen_restaurante` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_restaurante` bigint NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_subida` datetime(6) NOT NULL,
  `orden` int DEFAULT NULL,
  `ruta_imagen` varchar(255) NOT NULL,
  `tipo_imagen` enum('Galeria','Logo','Portada') NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_imagen_restaurante_restaurante` (`codigo_restaurante`),
  CONSTRAINT `FK_imagen_restaurante_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `documento_restaurante`;
CREATE TABLE `documento_restaurante` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_restaurante` bigint NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_subida` datetime(6) NOT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `ruta_archivo` varchar(255) NOT NULL,
  `tipo_documento` enum('CARTA_RESTAURANTE','CarnetSanidad','LicenciaFuncionamiento','RUC','Otros') NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_documento_restaurante_restaurante` (`codigo_restaurante`),
  CONSTRAINT `FK_documento_restaurante_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: plato_menu
-- Almacena los platos del menú de cada restaurante
-- =====================================================
DROP TABLE IF EXISTS `plato_menu`;
CREATE TABLE `plato_menu` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_restaurante` bigint NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_creacion` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `fecha_modificacion` datetime(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`codigo`),
  KEY `FK_plato_menu_restaurante` (`codigo_restaurante`),
  KEY `IDX_plato_estado` (`estado`),
  CONSTRAINT `FK_plato_menu_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: unidad_medida_plato
-- Almacena las unidades de medida de cada plato
-- (Personal, Familiar, 1/4, 1/2, etc.)
-- Cada unidad tiene su propio precio
-- =====================================================
DROP TABLE IF EXISTS `unidad_medida_plato`;
CREATE TABLE `unidad_medida_plato` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_plato` bigint NOT NULL,
  `nombre` varchar(100) NOT NULL COMMENT 'Ej: Personal, Familiar, 1/4, 1/2, Entero',
  `descripcion` varchar(255) DEFAULT NULL COMMENT 'Ej: Porción individual, Para 4-6 personas',
  `precio_original` decimal(10,2) NOT NULL COMMENT 'Precio base de esta unidad',
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_creacion` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `fecha_modificacion` datetime(6) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`codigo`),
  KEY `FK_unidad_medida_plato` (`codigo_plato`),
  KEY `IDX_unidad_estado` (`estado`),
  CONSTRAINT `FK_unidad_medida_plato` FOREIGN KEY (`codigo_plato`) REFERENCES `plato_menu` (`codigo`) ON DELETE CASCADE,
  CONSTRAINT `CHK_precio_positivo` CHECK (`precio_original` > 0)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- DATOS DE EJEMPLO PARA MENÚ (OPCIONAL - Comentado)
-- =====================================================
-- Estos datos de ejemplo se pueden descomentar para pruebas
-- pero en producción se gestionarán desde la interfaz

/*
-- Ejemplo: Restaurante Pollería "El Sabor" (código 1)
INSERT INTO `plato_menu` VALUES 
(1, 1, 'Pollo a la Brasa', 'Pollo tierno con papas y ensalada', TRUE, NOW(), NULL),
(2, 1, 'Alitas Picantes', 'Alitas bañadas en salsa picante', TRUE, NOW(), NULL);

-- Unidades para Pollo a la Brasa
INSERT INTO `unidad_medida_plato` VALUES 
(1, 1, '1/4 de Pollo', 'Cuarto de pollo con papas', 15.00, TRUE, NOW(), NULL),
(2, 1, '1/2 Pollo', 'Medio pollo con papas y ensalada', 28.00, TRUE, NOW(), NULL),
(3, 1, 'Pollo Entero', 'Pollo completo para compartir', 52.00, TRUE, NOW(), NULL);

-- Unidades para Alitas Picantes
INSERT INTO `unidad_medida_plato` VALUES 
(4, 2, 'Personal (6 unidades)', '6 alitas picantes', 12.00, TRUE, NOW(), NULL),
(5, 2, 'Familiar (12 unidades)', '12 alitas picantes', 22.00, TRUE, NOW(), NULL);
*/

-- =====================================================
-- NOTA IMPORTANTE: ENUM de tipo_documento
-- =====================================================
-- El ENUM incluye 'CARTA_RESTAURANTE' como primer valor
-- para que coincida con el código Java en FileStorageService
-- y RestauranteService que usa este valor al guardar documentos.
-- Los valores deben estar en este orden específico:
--   1. CARTA_RESTAURANTE (menú del restaurante)
--   2. CarnetSanidad (certificado sanitario)
--   3. LicenciaFuncionamiento (autorización municipal)
--   4. RUC (documento tributario)
--   5. Otros (documentos adicionales)
-- =====================================================

DROP TABLE IF EXISTS `documento_repartidor`;
CREATE TABLE `documento_repartidor` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_repartidor` bigint NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_subida` datetime(6) NOT NULL,
  `fecha_vencimiento` datetime(6) DEFAULT NULL,
  `ruta_archivo` varchar(255) NOT NULL,
  `tipo_documento` enum('AntecedentesPolicial','Licencia','SOAT','TarjetaPropiedad') NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_documento_repartidor_repartidor` (`codigo_repartidor`),
  CONSTRAINT `FK_documento_repartidor_repartidor` FOREIGN KEY (`codigo_repartidor`) REFERENCES `repartidor` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: promocion
-- =====================================================
DROP TABLE IF EXISTS `promocion`;
CREATE TABLE `promocion` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) NOT NULL,
  `descripcion` text,
  `codigo_restaurante` bigint NOT NULL,
  `codigo_plato` bigint DEFAULT NULL COMMENT 'Plato del menú asociado a la promoción',
  `codigo_unidad_medida` bigint DEFAULT NULL COMMENT 'Unidad específica del plato en promoción',
  `precio_original` decimal(10,2) NOT NULL DEFAULT 0.00,
  `precio_promocional` decimal(10,2) NOT NULL DEFAULT 0.00,
  `tipo_descuento` varchar(50) DEFAULT NULL,
  `valor_descuento` decimal(10,2) DEFAULT 0.00,
  `categoria_promocion` varchar(100) DEFAULT NULL,
  `fecha_inicio` datetime(6) DEFAULT NULL,
  `fecha_fin` datetime(6) DEFAULT NULL,
  `estado` varchar(50) NOT NULL DEFAULT 'borrador',
  `contador_vistas` int NOT NULL DEFAULT 0,
  `contador_pedidos` int NOT NULL DEFAULT 0,
  `ingresos_totales` decimal(10,2) NOT NULL DEFAULT 0.00,
  `fecha_creacion` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `fecha_modificacion` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_promocion_restaurante` (`codigo_restaurante`),
  KEY `FK_promocion_plato` (`codigo_plato`),
  KEY `FK_promocion_unidad_medida` (`codigo_unidad_medida`),
  KEY `IDX_promocion_estado` (`estado`),
  KEY `IDX_promocion_fechas` (`fecha_inicio`, `fecha_fin`),
  CONSTRAINT `FK_promocion_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`),
  CONSTRAINT `FK_promocion_plato` FOREIGN KEY (`codigo_plato`) REFERENCES `plato_menu` (`codigo`) ON DELETE SET NULL,
  CONSTRAINT `FK_promocion_unidad_medida` FOREIGN KEY (`codigo_unidad_medida`) REFERENCES `unidad_medida_plato` (`codigo`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Tabla de promociones con relación a platos y unidades de medida específicas';

-- =====================================================
-- INSERTAR RESTAURANTE PARA DANIELA
-- =====================================================
INSERT INTO `restaurante` (
  `nombre`, `razon_social`, `ruc`, `telefono`, `direccion`, 
  `correo_electronico`, `codigo_distrito`, `codigo_estado_aprobacion`, 
  `codigo_usuario`, `estado`, `fecha_creacion`
) VALUES (
  'Restaurante Daniela', 
  'DANIELA CHANAME E.I.R.L.', 
  '20543210987', 
  '+51 987654321', 
  'Av. Balta 123, Chiclayo', 
  'daniela@test.com', 
  1, 
  8, 
  (SELECT codigo FROM usuario WHERE correo_electronico = 'daniela@test.com' LIMIT 1), 
  TRUE, 
  NOW()
);

-- =====================================================
-- SISTEMA DE PERMISOS (opcional, si lo usas)
-- =====================================================

DROP TABLE IF EXISTS `permiso`;
CREATE TABLE `permiso` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `seccion` varchar(50) NOT NULL,
  `accion` varchar(50) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_nombre_permiso` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insertar permisos predefinidos
INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('DASHBOARD_VIEW', 'Ver dashboard principal', 'dashboard', 'view', TRUE),
('CATEGORIAS_VIEW', 'Ver gestión de categorías', 'categorias', 'view', TRUE),
('CATEGORIAS_CREATE', 'Crear categorías', 'categorias', 'create', TRUE),
('CATEGORIAS_EDIT', 'Editar categorías', 'categorias', 'edit', TRUE),
('CATEGORIAS_DELETE', 'Eliminar categorías', 'categorias', 'delete', TRUE),
('RESTAURANTES_VIEW', 'Ver gestión de restaurantes', 'restaurantes', 'view', TRUE),
('RESTAURANTES_APPROVE', 'Aprobar/rechazar restaurantes', 'restaurantes', 'approve', TRUE),
('RESTAURANTES_EDIT', 'Editar restaurantes', 'restaurantes', 'edit', TRUE),
('RESTAURANTES_DELETE', 'Eliminar restaurantes', 'restaurantes', 'delete', TRUE),
('CLIENTES_VIEW', 'Ver gestión de clientes', 'clientes', 'view', TRUE),
('CLIENTES_EDIT', 'Editar clientes', 'clientes', 'edit', TRUE),
('CLIENTES_DELETE', 'Eliminar clientes', 'clientes', 'delete', TRUE),
('DELIVERY_VIEW', 'Ver gestión de repartidores', 'delivery', 'view', TRUE),
('DELIVERY_APPROVE', 'Aprobar/rechazar repartidores', 'delivery', 'approve', TRUE),
('DELIVERY_EDIT', 'Editar repartidores', 'delivery', 'edit', TRUE),
('DELIVERY_DELETE', 'Eliminar repartidores', 'delivery', 'delete', TRUE),
('ROLES_VIEW', 'Ver gestión de roles', 'roles', 'view', TRUE),
('ROLES_CREATE', 'Crear roles', 'roles', 'create', TRUE),
('ROLES_EDIT', 'Editar roles', 'roles', 'edit', TRUE),
('ROLES_DELETE', 'Eliminar roles', 'roles', 'delete', TRUE),
('CONFIGURACION_VIEW', 'Ver configuración del sistema', 'configuracion', 'view', TRUE),
('CONFIGURACION_EDIT', 'Editar configuración', 'configuracion', 'edit', TRUE),
('REPORTES_VIEW', 'Ver reportes y estadísticas', 'reportes', 'view', TRUE),
('PEDIDOS_VIEW', 'Ver gestión de pedidos', 'pedidos', 'view', TRUE),
('PEDIDOS_MANAGE', 'Gestionar pedidos', 'pedidos', 'manage', TRUE);

DROP TABLE IF EXISTS `rol_permiso`;
CREATE TABLE `rol_permiso` (
  `rol_codigo` bigint NOT NULL,
  `permiso_codigo` bigint NOT NULL,
  PRIMARY KEY (`rol_codigo`,`permiso_codigo`),
  KEY `FK_rol_permiso_permiso` (`permiso_codigo`),
  CONSTRAINT `FK_rol_permiso_permiso` FOREIGN KEY (`permiso_codigo`) REFERENCES `permiso` (`codigo`) ON DELETE CASCADE,
  CONSTRAINT `FK_rol_permiso_rol` FOREIGN KEY (`rol_codigo`) REFERENCES `rol` (`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Asignar TODOS los permisos al rol ADMINISTRADOR (codigo 1)
INSERT INTO `rol_permiso` (`rol_codigo`, `permiso_codigo`) 
SELECT 1, codigo FROM permiso WHERE estado = TRUE;

-- =====================================================
-- TABLA: estado_pedido
-- =====================================================
DROP TABLE IF EXISTS `estado_pedido`;
CREATE TABLE `estado_pedido` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `color` varchar(20) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_nombre_estado_pedido` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `estado_pedido` (`nombre`, `descripcion`, `color`, `estado`) VALUES
('Pendiente', 'Pedido creado, esperando confirmación', '#ffc107', TRUE),
('Confirmado', 'Pedido confirmado por el restaurante', '#17a2b8', TRUE),
('Preparando', 'El restaurante está preparando el pedido', '#fd7e14', TRUE),
('Listo', 'Pedido listo para recoger/entregar', '#6f42c1', TRUE),
('En camino', 'Repartidor en camino a entregar', '#007bff', TRUE),
('Entregado', 'Pedido entregado exitosamente', '#28a745', TRUE),
('Cancelado', 'Pedido cancelado', '#dc3545', TRUE),
('Rechazado', 'Pedido rechazado por el restaurante', '#6c757d', TRUE);

-- =====================================================
-- TABLA: metodo_pago
-- =====================================================
DROP TABLE IF EXISTS `metodo_pago`;
CREATE TABLE `metodo_pago` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `activo` BOOLEAN NOT NULL DEFAULT TRUE,
  `requiere_cambio` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_nombre_metodo_pago` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `metodo_pago` (`nombre`, `descripcion`, `activo`, `requiere_cambio`) VALUES
('Efectivo', 'Pago en efectivo al recibir', TRUE, TRUE),
('Yape', 'Transferencia por Yape', TRUE, FALSE),
('Plin', 'Transferencia por Plin', TRUE, FALSE),
('Tarjeta', 'Pago con tarjeta de crédito/débito', TRUE, FALSE),
('Transferencia', 'Transferencia bancaria', TRUE, FALSE);

-- =====================================================
-- TABLA: pedido
-- =====================================================
DROP TABLE IF EXISTS `pedido`;
CREATE TABLE `pedido` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_usuario` bigint NOT NULL,
  `codigo_restaurante` bigint NOT NULL,
  `codigo_promocion` bigint DEFAULT NULL,
  `codigo_repartidor` bigint DEFAULT NULL,
  `codigo_estado_pedido` bigint NOT NULL DEFAULT 1,
  `codigo_metodo_pago` bigint NOT NULL,
  `direccion_entrega` varchar(200) NOT NULL,
  `referencia_direccion` varchar(200) DEFAULT NULL,
  `latitud` decimal(10,8) DEFAULT NULL,
  `longitud` decimal(11,8) DEFAULT NULL,
  `telefono_contacto` varchar(20) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL DEFAULT 0.00,
  `descuento` decimal(10,2) NOT NULL DEFAULT 0.00,
  `costo_delivery` decimal(10,2) NOT NULL DEFAULT 0.00,
  `total` decimal(10,2) NOT NULL DEFAULT 0.00,
  `monto_efectivo` decimal(10,2) DEFAULT NULL,
  `vuelto` decimal(10,2) DEFAULT NULL,
  `notas` text,
  `fecha_pedido` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `fecha_confirmacion` datetime(6) DEFAULT NULL,
  `fecha_listo` datetime(6) DEFAULT NULL,
  `fecha_asignacion_repartidor` datetime(6) DEFAULT NULL,
  `fecha_en_camino` datetime(6) DEFAULT NULL,
  `fecha_entrega` datetime(6) DEFAULT NULL,
  `fecha_cancelacion` datetime(6) DEFAULT NULL,
  `motivo_cancelacion` text,
  `calificacion_restaurante` int DEFAULT NULL,
  `calificacion_repartidor` int DEFAULT NULL,
  `comentario_calificacion` text,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`),
  KEY `FK_pedido_usuario` (`codigo_usuario`),
  KEY `FK_pedido_restaurante` (`codigo_restaurante`),
  KEY `FK_pedido_promocion` (`codigo_promocion`),
  KEY `FK_pedido_repartidor` (`codigo_repartidor`),
  KEY `FK_pedido_estado_pedido` (`codigo_estado_pedido`),
  KEY `FK_pedido_metodo_pago` (`codigo_metodo_pago`),
  CONSTRAINT `FK_pedido_usuario` FOREIGN KEY (`codigo_usuario`) REFERENCES `usuario` (`codigo`),
  CONSTRAINT `FK_pedido_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`),
  CONSTRAINT `FK_pedido_promocion` FOREIGN KEY (`codigo_promocion`) REFERENCES `promocion` (`codigo`),
  CONSTRAINT `FK_pedido_repartidor` FOREIGN KEY (`codigo_repartidor`) REFERENCES `repartidor` (`codigo`),
  CONSTRAINT `FK_pedido_estado_pedido` FOREIGN KEY (`codigo_estado_pedido`) REFERENCES `estado_pedido` (`codigo`),
  CONSTRAINT `FK_pedido_metodo_pago` FOREIGN KEY (`codigo_metodo_pago`) REFERENCES `metodo_pago` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: detalle_pedido
-- =====================================================
DROP TABLE IF EXISTS `detalle_pedido`;
CREATE TABLE `detalle_pedido` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_pedido` bigint NOT NULL,
  `codigo_promocion` bigint NOT NULL,
  `cantidad` int NOT NULL DEFAULT 1,
  `precio_unitario` decimal(10,2) NOT NULL,
  `precio_total` decimal(10,2) NOT NULL,
  `notas` text,
  PRIMARY KEY (`codigo`),
  KEY `FK_detalle_pedido_pedido` (`codigo_pedido`),
  KEY `FK_detalle_pedido_promocion` (`codigo_promocion`),
  CONSTRAINT `FK_detalle_pedido_pedido` FOREIGN KEY (`codigo_pedido`) REFERENCES `pedido` (`codigo`) ON DELETE CASCADE,
  CONSTRAINT `FK_detalle_pedido_promocion` FOREIGN KEY (`codigo_promocion`) REFERENCES `promocion` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;