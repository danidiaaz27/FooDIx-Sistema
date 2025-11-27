-- =====================================================
-- SINCRONIZAR CONTRASEÑAS - Usar la misma que el nuevo usuario
-- Contraseña: Admin123!
-- =====================================================

-- Paso 1: Obtener el hash del último usuario registrado (el que acabas de crear)
SELECT 
    codigo,
    nombre,
    correo_electronico,
    contrasena as 'Hash_Admin123!'
FROM usuario 
WHERE estado = TRUE 
ORDER BY codigo DESC 
LIMIT 1;

-- Paso 2: COPIAR el hash de arriba y pegarlo en el siguiente UPDATE
-- Reemplazar 'PEGAR_HASH_AQUI' con el hash completo que aparece arriba

-- ⚠️ IMPORTANTE: Ejecutar primero el SELECT de arriba, copiar el hash, y pegarlo abajo

-- Actualizar TODOS los usuarios de prueba con el mismo hash
UPDATE usuario 
SET contrasena = (
    SELECT contrasena 
    FROM (SELECT contrasena FROM usuario ORDER BY codigo DESC LIMIT 1) as temp
)
WHERE correo_electronico IN (
    'admin@local.dev',
    'JesusDiaz@gmail.com',
    'Tania@gmail.com',
    'daniela@test.com',
    'dann27@gmail.com',
    'CarlosDiaz@gmail.com',
    'nanisss27@gmail.com',
    'DanielAnteroJunior@gmail.com'
);

-- Verificar que todas las contraseñas son iguales ahora
SELECT 
    codigo,
    nombre,
    correo_electronico,
    codigo_rol,
    CASE codigo_rol
        WHEN 1 THEN 'ADMINISTRADOR'
        WHEN 2 THEN 'RESTAURANTE'
        WHEN 3 THEN 'REPARTIDOR'
        WHEN 4 THEN 'USUARIO'
    END as rol,
    CASE 
        WHEN contrasena = (SELECT contrasena FROM usuario ORDER BY codigo DESC LIMIT 1) 
        THEN '✅ Misma contraseña (Admin123!)'
        ELSE '❌ Contraseña diferente'
    END as estado_password
FROM usuario
WHERE estado = TRUE
ORDER BY codigo_rol, codigo;
