# 📊 Guía de Base de Datos - FoodIx

## 🗄️ Estructura de Base de Datos

### Archivo Principal
- **`datos_iniciales.sql`** - Contiene TODA la estructura y datos iniciales

## 📋 Contenido del Script

### 1️⃣ **Estructura de Tablas**
Todas las tablas necesarias para el sistema:
- ✅ `categoria` - Categorías de restaurantes (Pollería, Cevichería, etc.)
- ✅ `departamento`, `provincia`, `distrito` - Ubicaciones geográficas
- ✅ `rol` - Roles del sistema (ADMINISTRADOR, RESTAURANTE, REPARTIDOR, USUARIO)
- ✅ `usuario` - Usuarios registrados
- ✅ `restaurante` - Restaurantes y sus datos
- ✅ `repartidor` - Repartidores de delivery
- ✅ `estado_aprobacion` - Estados de aprobación (Pendiente, Aprobado, Rechazado)
- ✅ `tipo_vehiculo` - Tipos de vehículos para delivery
- ✅ `documento_restaurante` - Documentos de verificación
- ✅ `documento_repartidor` - Documentos de repartidores
- ✅ `imagen_restaurante` - Imágenes del restaurante
- ✅ `categoria_restaurante` - Relación entre categorías y restaurantes

### 2️⃣ **Sistema de Permisos (37 Permisos)**
El script incluye un sistema completo de permisos granulares:

#### Tablas de Permisos:
- ✅ `permiso` - Define cada permiso individual
- ✅ `rol_permiso` - Relaciona roles con permisos

#### Permisos por Sección:

**Usuarios del Sistema (6 permisos)**
- Ver usuarios
- Crear usuarios
- Editar usuarios
- Eliminar usuarios
- Cambiar estado
- Asignar roles

**Clientes (5 permisos)**
- Ver clientes
- Ver detalle
- Editar clientes
- Eliminar clientes
- Cambiar estado

**Restaurantes (7 permisos)**
- Ver restaurantes
- Ver detalle
- Aprobar solicitudes
- Rechazar solicitudes
- Editar restaurantes
- Eliminar restaurantes
- Cambiar estado

**Repartidores/Delivery (7 permisos)**
- Ver repartidores
- Ver detalle
- Aprobar solicitudes
- Rechazar solicitudes
- Editar repartidores
- Eliminar repartidores
- Cambiar estado

**Categorías (5 permisos)**
- Ver categorías
- Crear categorías
- Editar categorías
- Eliminar categorías
- Cambiar estado

**Configuración y Roles (7 permisos)**
- Ver configuración
- Ver roles
- Crear roles
- Editar roles
- Eliminar roles
- Asignar permisos
- Cambiar estado de roles

### 3️⃣ **Datos Iniciales**

#### Categorías (12)
- Pollería, Cevichería, Chaufería, Mariscos, Comida Criolla
- Chifa, Pizzería, Hamburguesas, Postres, Cafetería
- Comida Vegetariana, Sushi

#### Ubicaciones
- **Departamento**: Lambayeque
- **Provincias**: Chiclayo, Lambayeque, Ferreñafe
- **Distritos**: 38 distritos de Lambayeque

#### Estados de Aprobación (3 principales)
- **7** - Pendiente
- **8** - Aprobado
- **9** - Rechazado

#### Tipos de Vehículo
- Bicicleta
- Motocicleta
- Scooter Eléctrico
- Automóvil

#### Usuarios de Prueba

**👨‍💼 Administrador**
```
Email: daniela@FooDix.com.pe
Password: [ya configurada con BCrypt]
Rol: ADMINISTRADOR
Permisos: TODOS (37 permisos)
```

**🏪 Restaurantes de Prueba**
1. SUNAT (Aprobado)
2. El Sabor Norteño (Aprobado)
3. La Casa de las Empanadas (Pendiente)
4. Pollos & Parrillas Express (Pendiente)

## 🚀 Cómo Usar

### Instalación Inicial

#### Opción 1: Docker (Recomendado)
```bash
# La base de datos se crea automáticamente con docker-compose
docker-compose up -d

# Esperar a que MySQL inicie (10-15 segundos)
docker exec -i foodix-sistema-mysql-1 mysql -uroot -proot db_foodix < datos_iniciales.sql
```

#### Opción 2: MySQL Local
```bash
# Crear base de datos
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS db_foodix CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;"

# Importar datos
mysql -u root -p db_foodix < datos_iniciales.sql
```

### Verificación Post-Instalación

#### 1. Verificar Tablas
```sql
USE db_foodix;
SHOW TABLES;
-- Debe mostrar 19 tablas
```

#### 2. Verificar Permisos
```sql
-- Ver total de permisos
SELECT COUNT(*) as total_permisos FROM permiso WHERE estado = b'1';
-- Debe mostrar: 37

-- Ver permisos por sección
SELECT seccion, COUNT(*) as total 
FROM permiso 
WHERE estado = b'1' 
GROUP BY seccion;
```

#### 3. Verificar Rol Administrador
```sql
-- Ver permisos del administrador
SELECT r.nombre AS rol, COUNT(p.codigo) as permisos_asignados
FROM rol r
INNER JOIN rol_permiso rp ON r.codigo = rp.rol_codigo
INNER JOIN permiso p ON rp.permiso_codigo = p.codigo
WHERE r.codigo = 1;
-- Debe mostrar: 37 permisos
```

#### 4. Verificar Datos de Prueba
```sql
-- Categorías
SELECT COUNT(*) FROM categoria WHERE estado = b'1';
-- Debe mostrar: 12

-- Restaurantes
SELECT nombre, codigo_estado_aprobacion FROM restaurante;
-- Debe mostrar: 4 restaurantes (2 aprobados, 2 pendientes)

-- Usuarios
SELECT nombre, correo_electronico, codigo_rol FROM usuario;
-- Debe mostrar: 7 usuarios
```

## 🔄 Reset de Base de Datos

### Opción 1: Docker
```bash
# Eliminar contenedores y volúmenes
docker-compose down -v

# Recrear todo
docker-compose up -d

# Esperar e importar
Start-Sleep -Seconds 15
docker exec -i foodix-sistema-mysql-1 mysql -uroot -proot db_foodix < datos_iniciales.sql
```

### Opción 2: MySQL Local
```bash
mysql -u root -p -e "DROP DATABASE IF EXISTS db_foodix; CREATE DATABASE db_foodix CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;"
mysql -u root -p db_foodix < datos_iniciales.sql
```

## 📦 Respaldo y Restauración

### Crear Respaldo
```bash
# Con Docker
docker exec foodix-sistema-mysql-1 mysqldump -uroot -proot db_foodix > backup_$(date +%Y%m%d_%H%M%S).sql

# MySQL Local
mysqldump -u root -p db_foodix > backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restaurar Respaldo
```bash
# Con Docker
docker exec -i foodix-sistema-mysql-1 mysql -uroot -proot db_foodix < backup_20251117_150000.sql

# MySQL Local
mysql -u root -p db_foodix < backup_20251117_150000.sql
```

## 🛠️ Mantenimiento

### Agregar Nuevos Permisos
```sql
-- Ejemplo: Agregar permiso para reportes
INSERT INTO permiso (nombre, descripcion, seccion, accion, estado) 
VALUES ('REPORTES_VER', 'Ver reportes del sistema', 'reportes', 'Ver', b'1');

-- Asignar al administrador
INSERT INTO rol_permiso (rol_codigo, permiso_codigo) 
VALUES (1, LAST_INSERT_ID());
```

### Crear Nuevo Rol con Permisos Específicos
```sql
-- 1. Crear el rol
INSERT INTO rol (nombre, descripcion, estado) 
VALUES ('SUPERVISOR', 'Supervisor de operaciones', b'1');

-- 2. Asignar permisos específicos
INSERT INTO rol_permiso (rol_codigo, permiso_codigo)
SELECT @rol_id := LAST_INSERT_ID(), codigo 
FROM permiso 
WHERE nombre IN (
    'RESTAURANTES_VER',
    'RESTAURANTES_APROBAR',
    'RESTAURANTES_RECHAZAR',
    'DELIVERY_VER',
    'DELIVERY_APROBAR'
);
```

## 📊 Estructura de Permisos

```
permiso
├── codigo (PK)
├── nombre (UNIQUE) - Ej: "RESTAURANTES_APROBAR"
├── descripcion - Descripción detallada
├── seccion - usuarios|clientes|restaurantes|delivery|categorias|configuracion
├── accion - Ver|Crear|Editar|Eliminar|Aprobar|Rechazar|Gestionar
└── estado - Activo/Inactivo

rol_permiso (Many-to-Many)
├── rol_codigo (FK → rol)
└── permiso_codigo (FK → permiso)
```

## 🔐 Roles del Sistema

| Código | Rol          | Permisos Asignados | Descripción |
|--------|--------------|-------------------|-------------|
| 1      | ADMINISTRADOR| 37 (TODOS)       | Control total del sistema |
| 2      | RESTAURANTE  | 0                | Propietario de restaurante |
| 3      | REPARTIDOR   | 0                | Repartidor de pedidos |
| 4      | USUARIO      | 0                | Cliente del sistema |

## ⚠️ Notas Importantes

1. **Encoding**: Todas las tablas usan `utf8mb4_0900_ai_ci` para soportar caracteres especiales
2. **Permisos del Admin**: El rol ADMINISTRADOR (código 1) tiene TODOS los permisos por defecto
3. **Estados de Aprobación**: 
   - Código 7 = Pendiente (pueden aprobarse/rechazarse)
   - Código 8 = Aprobado (estado final)
   - Código 9 = Rechazado (estado final)
4. **Contraseñas**: Todas las contraseñas están hasheadas con BCrypt
5. **Datos de Prueba**: Los restaurantes y usuarios incluidos son solo para testing

## 🆘 Solución de Problemas

### Error: "Table already exists"
```sql
-- Verificar si las tablas existen
SHOW TABLES;

-- Si necesitas recrear, elimina primero
DROP TABLE IF EXISTS rol_permiso, permiso;
```

### Error: "Duplicate entry"
```sql
-- Limpiar permisos existentes
DELETE FROM rol_permiso;
DELETE FROM permiso;

-- Volver a ejecutar los INSERTs
```

### Error: "Cannot add foreign key constraint"
```sql
-- Verificar que la tabla rol existe
SELECT * FROM rol;

-- Verificar orden de creación de tablas
-- rol debe existir ANTES de crear permiso y rol_permiso
```

## 📞 Soporte

Si encuentras problemas con la base de datos:
1. Revisa los logs de MySQL
2. Verifica la versión de MySQL (debe ser 8.0+)
3. Asegúrate de que el charset sea utf8mb4
4. Consulta la documentación de Spring Boot + MySQL

---

**Última actualización**: 17/11/2025  
**Versión del Schema**: 2.0  
**Compatibilidad**: MySQL 8.0+
