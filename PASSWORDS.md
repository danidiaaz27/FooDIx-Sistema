# 🔐 Gestión de Contraseñas - FooDix

## 📌 Contraseña Unificada

**Todos los usuarios de prueba usan la misma contraseña:**

```
Contraseña: Admin123!
```

---

## 👥 Usuarios de Prueba Disponibles

### 🔧 Administradores (Rol: ADMINISTRADOR)
- **Email:** `admin@local.dev` | **Password:** `Admin123!`
- **Email:** `JesusDiaz@gmail.com` | **Password:** `Admin123!`
- **Email:** `Tania@gmail.com` | **Password:** `Admin123!`

### 🍽️ Restaurantes (Rol: RESTAURANTE)
- **Email:** `daniela@test.com` | **Password:** `Admin123!`

### 🚴 Repartidores (Rol: REPARTIDOR)
- **Email:** `dann27@gmail.com` | **Password:** `Admin123!`
- **Email:** `CarlosDiaz@gmail.com` | **Password:** `Admin123!`

### 👤 Clientes (Rol: USUARIO)
- **Email:** `nanisss27@gmail.com` | **Password:** `Admin123!`
- **Email:** `DanielAnteroJunior@gmail.com` | **Password:** `Admin123!`

---

## 🔄 Sincronizar Contraseñas en BD Existente

Si ya tienes una base de datos con usuarios y quieres que todos usen `Admin123!`:

### Opción 1: Desde SQL (Recomendado)

```sql
-- 1. Obtener el hash del último usuario registrado
SELECT contrasena FROM usuario ORDER BY codigo DESC LIMIT 1;

-- 2. Actualizar todos los usuarios con ese hash
UPDATE usuario 
SET contrasena = (
    SELECT contrasena 
    FROM (SELECT contrasena FROM usuario ORDER BY codigo DESC LIMIT 1) as temp
)
WHERE estado = TRUE;
```

O ejecutar el archivo completo:
```bash
mysql -u root -p foodix < sync_passwords.sql
```

### Opción 2: Desde la Aplicación

1. Iniciar aplicación:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

2. Abrir en navegador:
   ```
   http://localhost:8080/api/test-password/hash?password=Admin123!
   ```

3. Copiar el hash generado

4. Ejecutar en la BD:
   ```sql
   UPDATE usuario SET contrasena = 'HASH_COPIADO_AQUI' WHERE estado = TRUE;
   ```

---

## 🆕 Crear Nuevos Usuarios

### Desde el Formulario de Registro
Los nuevos usuarios registrados desde el formulario web automáticamente tienen sus contraseñas encriptadas con BCrypt.

**Importante:** Recuerda la contraseña que ingresas, no hay forma de recuperarla sin el proceso de "Olvidé mi contraseña".

### Desde SQL con Contraseña Personalizada

1. Generar hash para tu contraseña:
   ```
   http://localhost:8080/api/test-password/hash?password=MiContraseña123
   ```

2. Insertar usuario con el hash:
   ```sql
   INSERT INTO usuario (
       nombre, apellido_paterno, apellido_materno,
       correo_electronico, contrasena, codigo_rol,
       numero_documento, estado, fecha_creacion
   ) VALUES (
       'Juan', 'Pérez', 'García',
       'juan@example.com', '$2a$12$HASH_GENERADO_AQUI', 2,
       '11111111', TRUE, NOW()
   );
   ```

---

## 🧪 Herramientas de Desarrollo

### Endpoints de Prueba (Solo desarrollo)

```bash
# Generar hash para una contraseña
GET http://localhost:8080/api/test-password/hash?password=tucontraseña

# Verificar si una contraseña coincide con un hash
GET http://localhost:8080/api/test-password/verify?password=tucontraseña&hash=elhash

# Probar contraseñas comunes contra el hash de la BD
GET http://localhost:8080/api/test-password/test-default

# Generar múltiples hashes comunes
GET http://localhost:8080/api/test-password/generate-common
```

⚠️ **IMPORTANTE:** Eliminar `PasswordTestController.java` antes de producción.

---

## 🐛 Problemas Comunes

### No puedo iniciar sesión
1. Verificar que usas `Admin123!` (con mayúsculas y signo de exclamación)
2. Verificar que el email es correcto (sin espacios)
3. Ejecutar `sync_passwords.sql` para sincronizar todas las contraseñas

### Los hashes no coinciden
- BCrypt genera hashes diferentes cada vez, pero todos funcionan
- No compares hashes directamente, usa el endpoint `/verify`

### Usuarios nuevos no pueden entrar
- La aplicación encripta automáticamente al registrar
- Usa la contraseña que ingresaste en el formulario (sin encriptar)
- Verifica que el usuario se creó: `SELECT * FROM usuario ORDER BY codigo DESC LIMIT 1;`

---

## 📝 Archivos Relacionados

- `datos_iniciales.sql` - Script de inicialización con usuarios de prueba
- `sync_passwords.sql` - Script para sincronizar contraseñas
- `update_passwords.sql` - Script alternativo para actualizar contraseñas
- `PasswordTestController.java` - Herramientas de desarrollo (eliminar en producción)

---

## 🔒 Seguridad en Producción

Antes de desplegar:

1. ✅ Eliminar todos los usuarios de prueba
2. ✅ Cambiar contraseña del administrador
3. ✅ Eliminar `PasswordTestController.java`
4. ✅ Eliminar archivos `.sql` de prueba
5. ✅ Habilitar HTTPS
6. ✅ Configurar políticas de contraseña fuerte
7. ✅ Implementar límite de intentos de login

---

**Última actualización:** 27 de noviembre de 2025
