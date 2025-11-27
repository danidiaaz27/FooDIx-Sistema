-- =====================================================
-- SCRIPT PARA ACTUALIZAR CONTRASEÑAS DE USUARIOS DE PRUEBA
-- Ejecutar SOLO en entorno de desarrollo
-- =====================================================

-- Contraseña por defecto para todos: "password123"
-- Hash BCrypt: $2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge

-- =====================================================
-- OPCIÓN 1: Actualizar TODAS las contraseñas a "password123"
-- =====================================================
UPDATE usuario 
SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge'
WHERE estado = TRUE;

-- =====================================================
-- OPCIÓN 2: Actualizar contraseñas individuales
-- =====================================================

-- Administradores
UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'admin@local.dev';

UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'JesusDiaz@gmail.com';

UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'Tania@gmail.com';

-- Restaurante
UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'daniela@test.com';

-- Repartidores
UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'dann27@gmail.com';

UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'CarlosDiaz@gmail.com';

-- Clientes
UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'nanisss27@gmail.com';

UPDATE usuario SET contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
WHERE correo_electronico = 'DanielAnteroJunior@gmail.com';

-- =====================================================
-- VERIFICAR CONTRASEÑAS ACTUALIZADAS
-- =====================================================
SELECT 
    codigo,
    nombre,
    correo_electronico,
    codigo_rol,
    estado,
    LEFT(contrasena, 20) as hash_preview,
    CASE 
        WHEN contrasena = '$2a$12$Nc5S.bEvkzigCcsXx8FRpeZc5jUi4JzXyOQLmjc2Wj51bZt1zj8Ge' 
        THEN 'password123' 
        ELSE 'otra contraseña' 
    END as password_hint
FROM usuario
ORDER BY codigo_rol, codigo;

-- =====================================================
-- GENERAR HASHES PERSONALIZADOS (usa el endpoint /api/test-password/hash)
-- =====================================================
-- Puedes usar el endpoint REST para generar hashes personalizados:
-- GET http://localhost:8080/api/test-password/hash?password=tucontraseña
