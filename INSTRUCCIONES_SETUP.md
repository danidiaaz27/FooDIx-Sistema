# 🚀 Instrucciones de Setup - Sistema FooDix

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- ✅ **Docker Desktop** (versión 4.0 o superior)
- ✅ **Java 21** (JDK)
- ✅ **Maven** (incluido en el proyecto con mvnw)
- ✅ **Git**

---

## 🔧 Instalación Paso a Paso

### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/JechaPush/SistemaFooDix.git
cd SistemaFooDix
```

### 2️⃣ Verificar Docker

Asegúrate de que Docker Desktop esté ejecutándose:

```bash
docker --version
docker-compose --version
```

### 3️⃣ Compilar el Proyecto

**En Windows (PowerShell):**
```powershell
./mvnw.cmd clean package -DskipTests
```

**En Linux/Mac:**
```bash
./mvnw clean package -DskipTests
```

### 4️⃣ Levantar los Contenedores

```bash
docker-compose up --build -d
```

Esto creará dos contenedores:
- **sistemafoodix-db-1**: MySQL 8.0 (puerto 3307)
- **sistemafoodix-mi-app-1**: Aplicación Spring Boot (puerto 8080)

### 5️⃣ Cargar Datos Iniciales

Tienes 3 opciones para cargar datos:

#### **Opción A - Restaurar Backup Completo (RECOMENDADO)** 🎯

Si tienes el archivo de backup completo, usa este script en PowerShell:

```powershell
.\restore_backup.ps1
```

Este script:
- Verifica que Docker esté corriendo
- Espera a que MySQL esté listo
- Restaura el backup completo con todos los datos

#### **Opción B - Datos Iniciales Básicos**

Si prefieres empezar con datos básicos:

```powershell
.\reset_db.ps1
```

#### **Opción C - Manual desde línea de comandos**

```bash
docker exec -i sistemafoodix-db-1 mysql -u root -p'root' db_foodix < datos_iniciales.sql
```

#### **Opción D - Desde un cliente MySQL (DBeaver, MySQL Workbench, etc.)**

1. Conectarse a MySQL:
   - Host: `localhost`
   - Port: `3306`
   - User: `root`
   - Password: `root`
   - Database: `db_foodix`

2. Ejecutar el script que prefieras

### 6️⃣ Verificar que la Aplicación Esté Corriendo

Espera 15-20 segundos después de levantar los contenedores, luego:

- Abre tu navegador en: http://localhost:8080
- Deberías ver la página de inicio de FooDix

---

## 👤 Usuarios de Prueba

### Administrador
- **Email**: daniela@FooDix.com.pe
- **Password**: 525224Da!
- **Acceso**: http://localhost:8080/menuAdministrador

### Cliente (después de registrarte)
- Regístrate en: http://localhost:8080/registro
- Selecciona **Rol: Cliente** (código 4)
- Luego inicia sesión y accede a: http://localhost:8080/menuUsuario

### Restaurante (después de registrarte)
- Regístrate en: http://localhost:8080/registro-restaurante
- Espera aprobación del admin
- Luego inicia sesión y accede a: http://localhost:8080/menuRestaurante

### Repartidor (después de registrarte)
- Regístrate en: http://localhost:8080/registro-repartidor
- Espera aprobación del admin
- Luego inicia sesión y accede a: http://localhost:8080/menuDelivery

---

## 🔍 Comandos Útiles

### Ver logs de la aplicación
```bash
docker logs sistemafoodix-mi-app-1 -f
```

### Ver logs de MySQL
```bash
docker logs sistemafoodix-db-1 -f
```

### Reiniciar solo la aplicación
```bash
docker-compose restart mi-app
```

### Detener todos los contenedores
```bash
docker-compose down
```

### Reiniciar todo desde cero (¡CUIDADO! Borra la base de datos)
```bash
docker-compose down -v
docker-compose up --build -d
# Luego volver a cargar datos_iniciales.sql
```

### Acceder a la consola MySQL
```bash
docker exec -it sistemafoodix-db-1 mysql -u root -p'root' db_foodix
```

---

## 🐛 Solución de Problemas

### Problema: "Port 8080 is already in use"
**Solución**: Cambia el puerto en `docker-compose.yml`:
```yaml
ports:
  - "8081:8080"  # Usa 8081 en lugar de 8080
```

### Problema: "Port 3307 is already in use"
**Solución**: Cambia el puerto de MySQL en `docker-compose.yml`:
```yaml
ports:
  - "3308:3306"  # Usa 3308 en lugar de 3307
```

### Problema: Los cambios en el código no se reflejan
**Solución**: Recompila y reconstruye:
```bash
./mvnw.cmd clean package -DskipTests
docker-compose up --build -d
```

### Problema: Error de conexión a la base de datos
**Solución**: Verifica que el contenedor de MySQL esté corriendo:
```bash
docker ps
# Debe aparecer sistemafoodix-db-1 como "healthy"
```

### Problema: "Template parsing error" en menuUsuario
**Solución**: Asegúrate de tener la última versión compilada:
```bash
./mvnw.cmd clean package -DskipTests
docker-compose down
docker-compose up --build -d
```

---

## 📦 Estructura del Proyecto

```
SistemaFooDix/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/SistemaDePromociones/
│   │   │       ├── controller/        # Controllers (AdminController, UsuarioController, etc.)
│   │   │       ├── model/             # Entidades JPA
│   │   │       ├── repository/        # Repositorios JPA y JDBC
│   │   │       ├── service/           # Lógica de negocio
│   │   │       ├── security/          # Configuración de seguridad
│   │   │       └── config/            # Configuraciones
│   │   └── resources/
│   │       ├── application.properties # Configuración de Spring Boot
│   │       ├── templates/             # Vistas Thymeleaf (HTML)
│   │       └── static/                # CSS, JS, imágenes
│   └── test/
├── docker-compose.yml                 # Configuración de Docker
├── Dockerfile                         # Imagen Docker de la app
├── datos_iniciales.sql               # Script de datos iniciales
└── pom.xml                           # Dependencias Maven
```

---

## 🤝 Contribuir

1. Crea una nueva rama para tu feature:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```

2. Haz tus cambios y commitea:
   ```bash
   git add .
   git commit -m "Descripción del cambio"
   ```

3. Push a tu rama:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```

4. Crea un Pull Request en GitHub

---

## 📞 Contacto

Si tienes problemas con la instalación, contacta al equipo de desarrollo.

---

## ✅ Checklist de Verificación

Antes de hacer push, verifica:

- [ ] El proyecto compila sin errores: `./mvnw.cmd clean package -DskipTests`
- [ ] Los contenedores Docker levantan correctamente: `docker-compose up --build -d`
- [ ] La aplicación está accesible en http://localhost:8080
- [ ] Los datos iniciales se cargan correctamente
- [ ] El login de administrador funciona
- [ ] El registro de usuarios funciona
- [ ] Los tests pasan (si hay): `./mvnw.cmd test`

---

**¡Listo! 🎉 Tu entorno de desarrollo está configurado.**
