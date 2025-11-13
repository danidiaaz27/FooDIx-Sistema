# 🔐 Flujo de Seguridad y Roles - Sistema FooDix

## ✅ Tu SecurityConfig está PERFECTAMENTE configurado

### 📊 Mapeo de Roles: Base de Datos → Spring Security

| BD (codigo) | Nombre BD | Spring Security | Menú Asignado |
|-------------|-----------|-----------------|---------------|
| **1** | ADMINISTRADOR | `ROLE_ADMIN` | `/menuAdministrador` |
| **2** | RESTAURANTE | `ROLE_RESTAURANT` | `/menuRestaurante` |
| **3** | REPARTIDOR | `ROLE_DELIVERY` | `/menuDelivery` |
| **4** | USUARIO | `ROLE_CUSTOMER` | `/menuUsuario` |

---

## 🔄 Flujo Completo de Autenticación

### 1️⃣ **Usuario hace LOGIN**
```
Usuario ingresa: email + password
      ↓
CustomUserDetailsService.loadUserByUsername()
      ↓
Busca en BD: SELECT * FROM usuario WHERE correo_electronico = ?
      ↓
Encuentra usuario y carga su ROL (codigo_rol)
```

### 2️⃣ **CustomUserDetails mapea el ROL**
```java
// En CustomUserDetails.java
private String getRoleFromCodigo(Long codigoRol) {
    if (Rol.ADMINISTRADOR.equals(codigoRol))  // 1
        return "ADMIN";
    else if (Rol.RESTAURANTE.equals(codigoRol))  // 2
        return "RESTAURANT";
    else if (Rol.REPARTIDOR.equals(codigoRol))  // 3
        return "DELIVERY";
    else if (Rol.USUARIO.equals(codigoRol))  // 4
        return "CUSTOMER";
}

// Crea la autoridad: "ROLE_ADMIN", "ROLE_RESTAURANT", etc.
```

### 3️⃣ **Spring Security valida las credenciales**
```
BCryptPasswordEncoder compara:
  - Password ingresado
  - Hash almacenado en BD
      ↓
¿Coinciden? → ✅ Autenticación EXITOSA
```

### 4️⃣ **RoleBasedAuthenticationSuccessHandler redirige**
```java
// Según el rol, redirige a:
ROLE_ADMIN      → /menuAdministrador
ROLE_RESTAURANT → /menuRestaurante
ROLE_DELIVERY   → /menuDelivery
ROLE_CUSTOMER   → /menuUsuario
```

### 5️⃣ **SecurityConfig valida el acceso**
```java
// Cada vez que accedes a una URL, Spring Security verifica:
.requestMatchers("/menuAdministrador/**").hasRole("ADMIN")
                                         ↑
                        ¿El usuario tiene ROLE_ADMIN?
                                  ↓
                            SÍ → ✅ Acceso permitido
                            NO → ❌ 403 Forbidden
```

---

## 🎯 Ejemplos Prácticos

### Ejemplo 1: Usuario Administrador
```
1. daniela@FooDix.com.pe hace login
2. BD retorna: codigo_rol = 1 (ADMINISTRADOR)
3. CustomUserDetails convierte: 1 → ROLE_ADMIN
4. RoleBasedAuthenticationSuccessHandler redirige a: /menuAdministrador
5. SecurityConfig permite acceso porque tiene hasRole("ADMIN")
```

### Ejemplo 2: Usuario Restaurante
```
1. juan.perez@sabornorteno.com hace login
2. BD retorna: codigo_rol = 2 (RESTAURANTE)
3. CustomUserDetails convierte: 2 → ROLE_RESTAURANT
4. RoleBasedAuthenticationSuccessHandler redirige a: /menuRestaurante
5. SecurityConfig permite acceso porque tiene hasRole("RESTAURANT")
```

### Ejemplo 3: Intento de acceso no autorizado
```
1. Usuario con ROLE_CUSTOMER (codigo_rol = 4)
2. Intenta acceder a: /menuAdministrador
3. SecurityConfig verifica: .hasRole("ADMIN")
4. Usuario NO tiene ROLE_ADMIN
5. Spring Security retorna: 403 Forbidden
```

---

## 🛡️ Rutas y Permisos

### ✅ Rutas PÚBLICAS (sin login)
```
/                           - Página principal
/login                      - Login
/registro                   - Registro clientes
/registro-restaurante       - Registro restaurantes
/registro-repartidor        - Registro repartidores
/verificacion              - Verificación email
/auth/**                   - APIs de autenticación
/api/roles                 - Consultar roles (NUEVO ✨)
/api/roles/**              - Detalles de roles (NUEVO ✨)
/api/provincias/**         - Consultar provincias
/api/distritos/**          - Consultar distritos
/contacto                  - Contacto
/tutorial                  - Tutorial
/css/**, /js/**, /img/**   - Recursos estáticos
```

### 🔒 Rutas PROTEGIDAS

#### ROLE_ADMIN (codigo_rol = 1)
```
/menuAdministrador/**
  - Ver todos los restaurantes
  - Aprobar/rechazar restaurantes
  - Ver todos los repartidores
  - Ver todos los usuarios
  - Gestionar categorías
  - Estadísticas globales
```

#### ROLE_RESTAURANT (codigo_rol = 2)
```
/menuRestaurante/**
  - Ver perfil del restaurante
  - Gestionar menú
  - Ver pedidos
  - Actualizar información
```

#### ROLE_DELIVERY (codigo_rol = 3)
```
/menuDelivery/**
  - Ver pedidos asignados
  - Actualizar estado de entrega
  - Ver historial
```

#### ROLE_CUSTOMER (codigo_rol = 4)
```
/menuUsuario/**
  - Ver restaurantes disponibles
  - Hacer pedidos
  - Ver historial de pedidos
  - Actualizar perfil
```

---

## 🧪 Cómo Probar

### 1. Probar Login con diferentes roles

```sql
-- En DBeaver, ver usuarios de prueba:
SELECT 
    u.codigo,
    u.correo_electronico,
    -- La contraseña está encriptada en BD
    r.codigo as codigo_rol,
    r.nombre as nombre_rol
FROM usuario u
INNER JOIN rol r ON u.codigo_rol = r.codigo
ORDER BY r.codigo;
```

### 2. Probar redirecciones

**Admin:**
```
1. Login: daniela@FooDix.com.pe
2. Debe redirigir → http://localhost:8080/menuAdministrador
```

**Restaurante:**
```
1. Login: juan.perez@sabornorteno.com
2. Debe redirigir → http://localhost:8080/menuRestaurante
```

**Cliente:**
```
1. Login: danndiazherrera@gmail.com
2. Debe redirigir → http://localhost:8080/menuUsuario
```

### 3. Probar accesos no autorizados

```
1. Login como CUSTOMER (codigo_rol = 4)
2. Intentar acceder: http://localhost:8080/menuAdministrador
3. Resultado esperado: ❌ 403 Forbidden o redirección
```

### 4. Probar API de roles (nueva)

```bash
# Obtener todos los roles (público)
curl http://localhost:8080/api/roles

# Obtener un rol específico
curl http://localhost:8080/api/roles/1

# Obtener roles para registro (excluye admin)
curl http://localhost:8080/api/roles/registro
```

---

## 🔍 Debugging

### Ver logs de autenticación
```
Cuando haces login, verás en la consola:
🔐 [AUTH] Buscando usuario: juan.perez@sabornorteno.com
✅ [AUTH] Usuario encontrado: JUAN PEREZ
   - Email: juan.perez@sabornorteno.com
   - Código Rol: 2
   - Nombre Rol: RESTAURANTE  ← Viene del JOIN con tabla rol
   - Estado: true
🔑 [AUTH] Asignando autoridad: ROLE_RESTAURANT para usuario: juan.perez@sabornorteno.com
```

### Verificar sesión actual (en controlador)
```java
@GetMapping("/debug/session")
public String debugSession(Authentication auth) {
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    Usuario usuario = userDetails.getUsuario();
    
    System.out.println("Usuario actual: " + usuario.getNombreCompleto());
    System.out.println("Rol: " + usuario.getNombreRol());
    System.out.println("Es admin? " + usuario.esAdministrador());
    
    return "redirect:/";
}
```

---

## ✨ Ventajas de tu Configuración Actual

1. ✅ **Roles centralizados en BD** - Fácil de gestionar
2. ✅ **Mapeo claro y explícito** - Sin números mágicos
3. ✅ **Type-safe con constantes** - Menos errores
4. ✅ **Logging completo** - Fácil debugging
5. ✅ **API REST de roles** - Frontend puede consultarlos
6. ✅ **Relaciones JPA** - Carga automática de rol
7. ✅ **SecurityConfig documentado** - Fácil de entender

---

## 🚨 Solución de Problemas

### Problema: "403 Forbidden" al acceder a un menú

**Causa posible 1:** Usuario no tiene el rol correcto
```sql
-- Verificar rol del usuario:
SELECT u.correo_electronico, r.nombre 
FROM usuario u 
INNER JOIN rol r ON u.codigo_rol = r.codigo 
WHERE u.correo_electronico = 'tu-email@example.com';
```

**Causa posible 2:** Mapeo de rol incorrecto en CustomUserDetails
```
Verificar logs: 🔑 [AUTH] Asignando autoridad: ROLE_XXX
```

### Problema: Redirige al menú incorrecto

**Solución:** Verificar `RoleBasedAuthenticationSuccessHandler`
```java
// Debe mapear correctamente:
ROLE_ADMIN      → /menuAdministrador
ROLE_RESTAURANT → /menuRestaurante
ROLE_DELIVERY   → /menuDelivery
ROLE_CUSTOMER   → /menuUsuario
```

### Problema: No puede acceder a /api/roles

**Solución:** Ya está agregado a las rutas públicas ✅
```java
.requestMatchers(
    "/api/roles",
    "/api/roles/**"
).permitAll()
```

---

## 🎉 Resumen

Tu `SecurityConfig` está **perfectamente configurado** y funciona correctamente con:

- ✅ Tabla `rol` en la base de datos
- ✅ Modelo `Rol` con constantes
- ✅ Relación Usuario → Rol (ManyToOne)
- ✅ CustomUserDetails con mapeo correcto
- ✅ RoleBasedAuthenticationSuccessHandler
- ✅ API REST de roles disponible

**¡Todo funciona en armonía! 🎵**
