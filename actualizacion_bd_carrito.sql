-- =====================================================
-- ACTUALIZACIÓN DE BASE DE DATOS FOODIX
-- Agregar campos para sistema de carrito y pedidos
-- =====================================================

USE db_foodix;

-- Agregar campos de stock a la tabla promocion
ALTER TABLE promocion 
ADD COLUMN cantidad_disponible INT DEFAULT NULL COMMENT 'Stock disponible de la promoción',
ADD COLUMN cantidad_vendida INT DEFAULT 0 COMMENT 'Cantidad vendida de la promoción';

-- Agregar campos de verificación a la tabla pedido
ALTER TABLE pedido 
ADD COLUMN codigo_verificacion VARCHAR(4) DEFAULT NULL COMMENT 'Código de 4 dígitos para verificar entrega',
ADD COLUMN verificado BOOLEAN DEFAULT FALSE COMMENT 'Indica si el pedido fue verificado con el código';

-- Actualizar promociones existentes con stock inicial (ejemplo: 100 unidades)
UPDATE promocion 
SET cantidad_disponible = 100, 
    cantidad_vendida = 0 
WHERE cantidad_disponible IS NULL;

-- Crear índice para búsqueda rápida por código de verificación
CREATE INDEX idx_pedido_codigo_verificacion ON pedido(codigo_verificacion);

-- Mostrar resultado
SELECT '✅ Base de datos actualizada correctamente' AS status;
