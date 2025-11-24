-- =====================================================
-- BASE DE DATOS FOODIX - VERSIÓN QUE SÍ FUNCIONA
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
-- TABLA: estado_aprobacion
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

-- =====================================================
-- TABLA: rol
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
-- TABLA: tipo_vehiculo
-- =====================================================
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
-- TABLA: usuario
-- =====================================================
DROP TABLE IF EXISTS `usuario`;
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
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_creacion` datetime(6) NOT NULL,
  `fecha_modificacion` datetime(6) DEFAULT NULL,
  `fecha_nacimiento` date NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `numero_documento` varchar(15) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_correo_electronico` (`correo_electronico`),
  UNIQUE KEY `UK_numero_documento` (`numero_documento`),
  KEY `FK_usuario_distrito` (`codigo_distrito`),
  KEY `FK_usuario_rol` (`codigo_rol`),
  KEY `FK_usuario_tipo_documento` (`codigo_tipo_documento`),
  CONSTRAINT `FK_usuario_distrito` FOREIGN KEY (`codigo_distrito`) REFERENCES `distrito` (`codigo`),
  CONSTRAINT `FK_usuario_rol` FOREIGN KEY (`codigo_rol`) REFERENCES `rol` (`codigo`),
  CONSTRAINT `FK_usuario_tipo_documento` FOREIGN KEY (`codigo_tipo_documento`) REFERENCES `tipo_documento` (`codigo`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `usuario` VALUES 
(1, 'FooDix', 'Administrador', 1, 1, 1, '$2a$10$ktinS55BjqW/wCvkPar.Au5VjwqTd2ZsvPO6Ze/A49ylKS9xArPJ.', 'daniela@FooDix.com.pe', 'Admin Address', TRUE, '2025-11-09 19:04:49.000000', NULL, '1990-01-01', 'Daniela', '99999999', '999999999');

-- =====================================================
-- TABLA: restaurante (VACÍA)
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
-- TABLA: imagen_restaurante
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

-- =====================================================
-- TABLA: documento_restaurante
-- =====================================================
DROP TABLE IF EXISTS `documento_restaurante`;
CREATE TABLE `documento_restaurante` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_restaurante` bigint NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  `fecha_subida` datetime(6) NOT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `ruta_archivo` varchar(255) NOT NULL,
  `tipo_documento` enum('CarnetSanidad','LicenciaFuncionamiento','Otros','RUC') NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_documento_restaurante_restaurante` (`codigo_restaurante`),
  CONSTRAINT `FK_documento_restaurante_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: documento_repartidor
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
-- SISTEMA DE PERMISOS
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

DROP TABLE IF EXISTS `rol_permiso`;
CREATE TABLE `rol_permiso` (
  `rol_codigo` bigint NOT NULL,
  `permiso_codigo` bigint NOT NULL,
  PRIMARY KEY (`rol_codigo`,`permiso_codigo`),
  KEY `FK_rol_permiso_permiso` (`permiso_codigo`),
  CONSTRAINT `FK_rol_permiso_permiso` FOREIGN KEY (`permiso_codigo`) REFERENCES `permiso` (`codigo`) ON DELETE CASCADE,
  CONSTRAINT `FK_rol_permiso_rol` FOREIGN KEY (`rol_codigo`) REFERENCES `rol` (`codigo`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('USUARIOS_VER', 'Ver lista de usuarios del sistema', 'usuarios', 'Ver', TRUE),
('USUARIOS_CREAR', 'Crear nuevos usuarios del sistema', 'usuarios', 'Crear', TRUE),
('USUARIOS_EDITAR', 'Editar información de usuarios existentes', 'usuarios', 'Editar', TRUE),
('USUARIOS_ELIMINAR', 'Eliminar usuarios del sistema', 'usuarios', 'Eliminar', TRUE),
('USUARIOS_CAMBIAR_ESTADO', 'Activar/desactivar usuarios', 'usuarios', 'Gestionar', TRUE),
('USUARIOS_ASIGNAR_ROL', 'Cambiar el rol de un usuario', 'usuarios', 'Gestionar', TRUE),
('CLIENTES_VER', 'Ver lista de clientes registrados', 'clientes', 'Ver', TRUE),
('CLIENTES_VER_DETALLE', 'Ver información detallada de un cliente', 'clientes', 'Ver', TRUE),
('CLIENTES_EDITAR', 'Editar información de clientes', 'clientes', 'Editar', TRUE),
('CLIENTES_ELIMINAR', 'Eliminar clientes del sistema', 'clientes', 'Eliminar', TRUE),
('CLIENTES_CAMBIAR_ESTADO', 'Activar/desactivar clientes', 'clientes', 'Gestionar', TRUE),
('RESTAURANTES_VER', 'Ver lista de restaurantes registrados', 'restaurantes', 'Ver', TRUE),
('RESTAURANTES_VER_DETALLE', 'Ver información detallada de un restaurante', 'restaurantes', 'Ver', TRUE),
('RESTAURANTES_APROBAR', 'Aprobar solicitudes de registro de restaurantes', 'restaurantes', 'Aprobar', TRUE),
('RESTAURANTES_RECHAZAR', 'Rechazar solicitudes de registro de restaurantes', 'restaurantes', 'Rechazar', TRUE),
('RESTAURANTES_EDITAR', 'Editar información de restaurantes', 'restaurantes', 'Editar', TRUE),
('RESTAURANTES_ELIMINAR', 'Eliminar restaurantes del sistema', 'restaurantes', 'Eliminar', TRUE),
('RESTAURANTES_CAMBIAR_ESTADO', 'Activar/desactivar restaurantes', 'restaurantes', 'Gestionar', TRUE),
('DELIVERY_VER', 'Ver lista de repartidores registrados', 'delivery', 'Ver', TRUE),
('DELIVERY_VER_DETALLE', 'Ver información detallada de un repartidor', 'delivery', 'Ver', TRUE),
('DELIVERY_APROBAR', 'Aprobar solicitudes de registro de repartidores', 'delivery', 'Aprobar', TRUE),
('DELIVERY_RECHAZAR', 'Rechazar solicitudes de registro de repartidores', 'delivery', 'Rechazar', TRUE),
('DELIVERY_EDITAR', 'Editar información de repartidores', 'delivery', 'Editar', TRUE),
('DELIVERY_ELIMINAR', 'Eliminar repartidores del sistema', 'delivery', 'Eliminar', TRUE),
('DELIVERY_CAMBIAR_ESTADO', 'Activar/desactivar repartidores', 'delivery', 'Gestionar', TRUE),
('CATEGORIAS_VER', 'Ver lista de categorías de productos', 'categorias', 'Ver', TRUE),
('CATEGORIAS_CREAR', 'Crear nuevas categorías', 'categorias', 'Crear', TRUE),
('CATEGORIAS_EDITAR', 'Editar categorías existentes', 'categorias', 'Editar', TRUE),
('CATEGORIAS_ELIMINAR', 'Eliminar categorías', 'categorias', 'Eliminar', TRUE),
('CATEGORIAS_CAMBIAR_ESTADO', 'Activar/desactivar categorías', 'categorias', 'Gestionar', TRUE),
('CONFIGURACION_VER', 'Ver sección de configuración del sistema', 'configuracion', 'Ver', TRUE),
('ROLES_VER', 'Ver lista de roles del sistema', 'configuracion', 'Ver', TRUE),
('ROLES_CREAR', 'Crear nuevos roles', 'configuracion', 'Crear', TRUE),
('ROLES_EDITAR', 'Editar roles existentes', 'configuracion', 'Editar', TRUE),
('ROLES_ELIMINAR', 'Eliminar roles', 'configuracion', 'Eliminar', TRUE),
('ROLES_ASIGNAR_PERMISOS', 'Asignar/modificar permisos de roles', 'configuracion', 'Gestionar', TRUE),
('ROLES_CAMBIAR_ESTADO', 'Activar/desactivar roles', 'configuracion', 'Gestionar', TRUE);

INSERT INTO `rol_permiso` (`rol_codigo`, `permiso_codigo`)
SELECT 1, codigo FROM permiso WHERE estado = TRUE;

-- =====================================================
-- TABLAS NUEVAS PARA EL SISTEMA DE PROMOCIONES Y PEDIDOS
-- =====================================================

-- =====================================================
-- TABLA: promocion
-- =====================================================
DROP TABLE IF EXISTS `promocion`;
CREATE TABLE `promocion` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_restaurante` bigint NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `descripcion` text,
  `precio_original` decimal(10,2) NOT NULL,
  `precio_promocional` decimal(10,2) NOT NULL,
  `tipo_descuento` enum('porcentaje','monto_fijo') NOT NULL,
  `valor_descuento` decimal(5,2) DEFAULT NULL,
  `categoria_promocion` varchar(50) DEFAULT NULL,
  `fecha_inicio` datetime NOT NULL,
  `fecha_fin` datetime NOT NULL,
  `estado` enum('borrador','activa','inactiva','expirada') NOT NULL DEFAULT 'borrador',
  `contador_vistas` int DEFAULT 0,
  `contador_pedidos` int DEFAULT 0,
  `ingresos_totales` decimal(10,2) DEFAULT 0,
  `fecha_creacion` datetime(6) NOT NULL,
  `fecha_modificacion` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_promocion_restaurante` (`codigo_restaurante`),
  CONSTRAINT `FK_promocion_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: imagen_promocion
-- =====================================================
DROP TABLE IF EXISTS `imagen_promocion`;
CREATE TABLE `imagen_promocion` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_promocion` bigint NOT NULL,
  `ruta_imagen` varchar(255) NOT NULL,
  `orden` int DEFAULT 1,
  `tipo_imagen` enum('principal','galeria') NOT NULL DEFAULT 'principal',
  `fecha_subida` datetime(6) NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`),
  KEY `FK_imagen_promocion_promocion` (`codigo_promocion`),
  CONSTRAINT `FK_imagen_promocion_promocion` FOREIGN KEY (`codigo_promocion`) REFERENCES `promocion` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: estado_pedido
-- =====================================================
DROP TABLE IF EXISTS `estado_pedido`;
CREATE TABLE `estado_pedido` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `orden` int NOT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `estado_pedido` VALUES 
(1, 'PENDIENTE', 'Pedido recibido, esperando confirmación', 1, TRUE),
(2, 'CONFIRMADO', 'Restaurante confirmó el pedido', 2, TRUE),
(3, 'EN_PREPARACION', 'Restaurante preparando el pedido', 3, TRUE),
(4, 'LISTO', 'Pedido listo para recoger/delivery', 4, TRUE),
(5, 'EN_CAMINO', 'Repartidor en camino con el pedido', 5, TRUE),
(6, 'ENTREGADO', 'Pedido entregado al cliente', 6, TRUE),
(7, 'CANCELADO', 'Pedido cancelado', 7, TRUE),
(8, 'RECHAZADO', 'Pedido rechazado por el restaurante', 8, TRUE);

-- =====================================================
-- TABLA: tipo_entrega
-- =====================================================
DROP TABLE IF EXISTS `tipo_entrega`;
CREATE TABLE `tipo_entrega` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `estado` BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tipo_entrega` VALUES 
(1, 'DELIVERY', 'Entrega a domicilio', TRUE),
(2, 'RECOGO_TIENDA', 'Recogo en el local', TRUE);

-- =====================================================
-- TABLA: pedido
-- =====================================================
DROP TABLE IF EXISTS `pedido`;
CREATE TABLE `pedido` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_usuario` bigint NOT NULL,
  `codigo_restaurante` bigint NOT NULL,
  `codigo_repartidor` bigint DEFAULT NULL,
  `codigo_estado_pedido` bigint NOT NULL,
  `codigo_tipo_entrega` bigint NOT NULL,
  `numero_pedido` varchar(20) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `costo_delivery` decimal(10,2) DEFAULT 0,
  `descuento` decimal(10,2) DEFAULT 0,
  `total` decimal(10,2) NOT NULL,
  `metodo_pago` enum('efectivo','tarjeta','yape','plin') NOT NULL,
  `direccion_entrega` varchar(255) DEFAULT NULL,
  `referencia_entrega` text,
  `telefono_contacto` varchar(20) DEFAULT NULL,
  `notas_especiales` text,
  `fecha_pedido` datetime(6) NOT NULL,
  `fecha_confirmacion` datetime(6) DEFAULT NULL,
  `fecha_entrega_estimada` datetime(6) DEFAULT NULL,
  `fecha_entrega_real` datetime(6) DEFAULT NULL,
  `calificacion_restaurante` int DEFAULT NULL,
  `calificacion_repartidor` int DEFAULT NULL,
  `comentario_cliente` text,
  `ganancia_repartidor` decimal(10,2) DEFAULT 0,
  PRIMARY KEY (`codigo`),
  UNIQUE KEY `UK_numero_pedido` (`numero_pedido`),
  KEY `FK_pedido_usuario` (`codigo_usuario`),
  KEY `FK_pedido_restaurante` (`codigo_restaurante`),
  KEY `FK_pedido_repartidor` (`codigo_repartidor`),
  KEY `FK_pedido_estado_pedido` (`codigo_estado_pedido`),
  KEY `FK_pedido_tipo_entrega` (`codigo_tipo_entrega`),
  CONSTRAINT `FK_pedido_usuario` FOREIGN KEY (`codigo_usuario`) REFERENCES `usuario` (`codigo`),
  CONSTRAINT `FK_pedido_restaurante` FOREIGN KEY (`codigo_restaurante`) REFERENCES `restaurante` (`codigo`),
  CONSTRAINT `FK_pedido_repartidor` FOREIGN KEY (`codigo_repartidor`) REFERENCES `repartidor` (`codigo`),
  CONSTRAINT `FK_pedido_estado_pedido` FOREIGN KEY (`codigo_estado_pedido`) REFERENCES `estado_pedido` (`codigo`),
  CONSTRAINT `FK_pedido_tipo_entrega` FOREIGN KEY (`codigo_tipo_entrega`) REFERENCES `tipo_entrega` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: detalle_pedido
-- =====================================================
DROP TABLE IF EXISTS `detalle_pedido`;
CREATE TABLE `detalle_pedido` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_pedido` bigint NOT NULL,
  `codigo_promocion` bigint NOT NULL,
  `cantidad` int NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_detalle_pedido_pedido` (`codigo_pedido`),
  KEY `FK_detalle_pedido_promocion` (`codigo_promocion`),
  CONSTRAINT `FK_detalle_pedido_pedido` FOREIGN KEY (`codigo_pedido`) REFERENCES `pedido` (`codigo`),
  CONSTRAINT `FK_detalle_pedido_promocion` FOREIGN KEY (`codigo_promocion`) REFERENCES `promocion` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: historial_estado_pedido
-- =====================================================
DROP TABLE IF EXISTS `historial_estado_pedido`;
CREATE TABLE `historial_estado_pedido` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_pedido` bigint NOT NULL,
  `codigo_estado_pedido` bigint NOT NULL,
  `observaciones` text,
  `fecha_cambio` datetime(6) NOT NULL,
  `codigo_usuario` bigint DEFAULT NULL,
  PRIMARY KEY (`codigo`),
  KEY `FK_historial_pedido` (`codigo_pedido`),
  KEY `FK_historial_estado_pedido` (`codigo_estado_pedido`),
  KEY `FK_historial_usuario` (`codigo_usuario`),
  CONSTRAINT `FK_historial_pedido` FOREIGN KEY (`codigo_pedido`) REFERENCES `pedido` (`codigo`),
  CONSTRAINT `FK_historial_estado_pedido` FOREIGN KEY (`codigo_estado_pedido`) REFERENCES `estado_pedido` (`codigo`),
  CONSTRAINT `FK_historial_usuario` FOREIGN KEY (`codigo_usuario`) REFERENCES `usuario` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- TABLA: pedido_disponible_repartidor
-- =====================================================
DROP TABLE IF EXISTS `pedido_disponible_repartidor`;
CREATE TABLE `pedido_disponible_repartidor` (
  `codigo` bigint NOT NULL AUTO_INCREMENT,
  `codigo_pedido` bigint NOT NULL,
  `codigo_repartidor` bigint NOT NULL,
  `fecha_asignacion` datetime(6) NOT NULL,
  `fecha_respuesta` datetime(6) DEFAULT NULL,
  `estado` enum('pendiente','aceptado','rechazado','expirado') NOT NULL DEFAULT 'pendiente',
  `motivo_rechazo` text,
  PRIMARY KEY (`codigo`),
  KEY `FK_pedido_disponible_pedido` (`codigo_pedido`),
  KEY `FK_pedido_disponible_repartidor` (`codigo_repartidor`),
  CONSTRAINT `FK_pedido_disponible_pedido` FOREIGN KEY (`codigo_pedido`) REFERENCES `pedido` (`codigo`),
  CONSTRAINT `FK_pedido_disponible_repartidor` FOREIGN KEY (`codigo_repartidor`) REFERENCES `repartidor` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- =====================================================
-- ACTUALIZACIÓN DE PERMISOS PARA LAS NUEVAS FUNCIONALIDADES
-- =====================================================

INSERT INTO `permiso` (`nombre`, `descripcion`, `seccion`, `accion`, `estado`) VALUES
('PROMOCIONES_VER', 'Ver promociones del restaurante', 'promociones', 'Ver', TRUE),
('PROMOCIONES_CREAR', 'Crear nuevas promociones', 'promociones', 'Crear', TRUE),
('PROMOCIONES_EDITAR', 'Editar promociones existentes', 'promociones', 'Editar', TRUE),
('PROMOCIONES_ELIMINAR', 'Eliminar promociones', 'promociones', 'Eliminar', TRUE),
('PROMOCIONES_PUBLICAR', 'Publicar/ocultar promociones', 'promociones', 'Gestionar', TRUE),
('PEDIDOS_VER', 'Ver pedidos del restaurante', 'pedidos', 'Ver', TRUE),
('PEDIDOS_GESTIONAR', 'Gestionar estado de pedidos', 'pedidos', 'Gestionar', TRUE),
('ESTADISTICAS_VER', 'Ver estadísticas del restaurante', 'estadisticas', 'Ver', TRUE),
('DELIVERY_PEDIDOS_VER', 'Ver pedidos disponibles para delivery', 'delivery', 'Ver', TRUE),
('DELIVERY_PEDIDOS_ACEPTAR', 'Aceptar pedidos de delivery', 'delivery', 'Aceptar', TRUE),
('DELIVERY_PEDIDOS_RECHAZAR', 'Rechazar pedidos de delivery', 'delivery', 'Rechazar', TRUE),
('DELIVERY_ESTADO_ACTUALIZAR', 'Actualizar estado de entrega', 'delivery', 'Gestionar', TRUE),
('DELIVERY_ESTADISTICAS_VER', 'Ver estadísticas de delivery', 'delivery', 'Ver', TRUE);

-- Asignar permisos nuevos al rol ADMINISTRADOR
INSERT INTO `rol_permiso` (`rol_codigo`, `permiso_codigo`)
SELECT 1, codigo FROM permiso WHERE nombre IN (
    'PROMOCIONES_VER', 'PROMOCIONES_CREAR', 'PROMOCIONES_EDITAR', 'PROMOCIONES_ELIMINAR', 'PROMOCIONES_PUBLICAR',
    'PEDIDOS_VER', 'PEDIDOS_GESTIONAR', 'ESTADISTICAS_VER',
    'DELIVERY_PEDIDOS_VER', 'DELIVERY_PEDIDOS_ACEPTAR', 'DELIVERY_PEDIDOS_RECHAZAR', 
    'DELIVERY_ESTADO_ACTUALIZAR', 'DELIVERY_ESTADISTICAS_VER'
);

-- Asignar permisos al rol RESTAURANTE
INSERT INTO `rol_permiso` (`rol_codigo`, `permiso_codigo`)
SELECT 2, codigo FROM permiso WHERE nombre IN (
    'PROMOCIONES_VER', 'PROMOCIONES_CREAR', 'PROMOCIONES_EDITAR', 'PROMOCIONES_ELIMINAR', 'PROMOCIONES_PUBLICAR',
    'PEDIDOS_VER', 'PEDIDOS_GESTIONAR', 'ESTADISTICAS_VER'
);

-- Asignar permisos al rol REPARTIDOR
INSERT INTO `rol_permiso` (`rol_codigo`, `permiso_codigo`)
SELECT 3, codigo FROM permiso WHERE nombre IN (
    'DELIVERY_PEDIDOS_VER', 'DELIVERY_PEDIDOS_ACEPTAR', 'DELIVERY_PEDIDOS_RECHAZAR', 
    'DELIVERY_ESTADO_ACTUALIZAR', 'DELIVERY_ESTADISTICAS_VER'
);

SET FOREIGN_KEY_CHECKS = 1;