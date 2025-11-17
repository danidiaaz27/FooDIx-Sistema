# 🍕 FoodIx - Sistema de Gestión de Restaurantes

Sistema de delivery de comida con gestión de restaurantes, repartidores y clientes.

## 🚀 Inicio Rápido

### Levantar el Proyecto con Docker

```bash
# 1. Iniciar contenedores
docker-compose up -d

# 2. Esperar 15 segundos para que MySQL inicie
Start-Sleep -Seconds 15

# 3. Importar base de datos
docker exec -i foodix-sistema-mi-app-1 mysql -uroot -proot db_foodix < datos_iniciales.sql

# 4. Acceder a la aplicación
http://localhost:8080
```

### Credenciales de Prueba

**Administrador**
- Email: `daniela@FooDix.com.pe`
- Password: `admin123` (o la configurada)

## 📁 Estructura del Proyecto

```
FooDIx-Sistema/
├── src/
│   ├── main/
│   │   ├── java/com/example/SistemaDePromociones/
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── model/           # Entidades JPA
│   │   │   ├── repository/      # Repositorios
│   │   │   ├── service/         # Lógica de negocio
│   │   │   └── security/        # Configuración de seguridad
│   │   └── resources/
│   │       ├── templates/       # Vistas Thymeleaf
│   │       ├── static/          # CSS, JS, imágenes
│   │       └── application.properties
│   └── test/                    # Tests unitarios
├── docker-compose.yml           # Configuración Docker
├── datos_iniciales.sql          # Base de datos inicial
└── README_DATABASE.md           # Documentación de BD
```

## 🔧 Tecnologías

- **Backend**: Spring Boot 3.5.7
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript
- **Base de Datos**: MySQL 8.0
- **Seguridad**: Spring Security
- **Reportes**: Apache POI (Excel)
- **Email**: Spring Mail

## 🎯 Funcionalidades Principales

### Para Administradores
- ✅ Gestión de clientes
- ✅ Aprobación/rechazo de restaurantes
- ✅ Aprobación/rechazo de repartidores
- ✅ Sistema de permisos granulares (37 permisos)
- ✅ Gestión de roles y usuarios
- ✅ Exportación a Excel
- ✅ Notificaciones por email

### Para Restaurantes
- ✅ Registro con validación de documentos
- ✅ Gestión de perfil
- ✅ Panel de control

### Para Repartidores
- ✅ Registro con documentación
- ✅ Gestión de entregas

### Para Clientes
- ✅ Búsqueda de restaurantes
- ✅ Realización de pedidos
- ✅ Seguimiento de entregas

## 🗄️ Base de Datos

Ver documentación detallada en: [README_DATABASE.md](README_DATABASE.md)

**Importar datos iniciales:**
```bash
docker exec -i foodix-sistema-mysql-1 mysql -uroot -proot db_foodix < datos_iniciales.sql
```

**Reset completo:**
```bash
docker-compose down -v
docker-compose up -d
Start-Sleep -Seconds 15
docker exec -i foodix-sistema-mysql-1 mysql -uroot -proot db_foodix < datos_iniciales.sql
```

## 📦 Scripts Útiles

**Reiniciar base de datos (PowerShell):**
```powershell
.\reset_db.ps1
```

**Restaurar backup:**
```powershell
.\restore_backup.ps1
```

## 🔒 Sistema de Permisos

El sistema cuenta con 37 permisos granulares organizados en 6 secciones:
- Usuarios del Sistema (6 permisos)
- Clientes (5 permisos)
- Restaurantes (7 permisos)
- Repartidores/Delivery (7 permisos)
- Categorías (5 permisos)
- Configuración y Roles (7 permisos)

## 📧 Configuración de Email

Edita `application.properties`:
```properties
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-contraseña-de-aplicacion
```

## 🐛 Solución de Problemas

### El proyecto no inicia
```bash
# Verificar logs
docker logs foodix-sistema-mi-app-1

# Reiniciar contenedores
docker-compose restart
```

### Error de base de datos
```bash
# Verificar que MySQL esté corriendo
docker ps

# Verificar logs de MySQL
docker logs foodix-sistema-mysql-1

# Reimportar base de datos
docker exec -i foodix-sistema-mysql-1 mysql -uroot -proot db_foodix < datos_iniciales.sql
```

### Puerto 8080 ocupado
```bash
# Ver qué proceso usa el puerto
netstat -ano | findstr :8080

# Cambiar puerto en application.properties
server.port=8081
```

## 📝 Desarrollo

### Compilar
```bash
./mvnw clean package
```

### Ejecutar tests
```bash
./mvnw test
```

### Ejecutar sin Docker
```bash
./mvnw spring-boot:run
```

## 📄 Licencia

Este proyecto es parte de un trabajo académico - Universidad Tecnológica del Perú

---

**Última actualización**: Noviembre 2025  
**Versión**: 2.0  
**Autor**: Daniela Díaz
