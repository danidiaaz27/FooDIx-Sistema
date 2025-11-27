-- Actualizar ENUM de documento_restaurante para incluir CARTA_RESTAURANTE
ALTER TABLE documento_restaurante 
MODIFY tipo_documento ENUM('CARTA_RESTAURANTE', 'CarnetSanidad', 'LicenciaFuncionamiento', 'RUC', 'Otros') NOT NULL;

-- Verificar el cambio
SHOW COLUMNS FROM documento_restaurante LIKE 'tipo_documento';
