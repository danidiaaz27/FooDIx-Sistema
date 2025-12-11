# 🔧 Tareas de Mantenimiento - FoodIx Sistema

Este documento describe las tareas de mantenimiento críticas implementadas en el sistema FoodIx.

## 📋 Índice

1. [Backup Automático de Base de Datos](#backup-automático-de-base-de-datos)
2. [Monitoreo de Salud (Spring Actuator)](#monitoreo-de-salud-spring-actuator)
3. [Endpoints API](#endpoints-api)

---

## 💾 Backup Automático de Base de Datos

### Descripción

Se ha implementado un sistema de backup automático que realiza copias de seguridad de la base de datos MySQL todos los días a las **00:05 AM** (hora de Lima).

### Características

- ✅ **Programación automática**: Backup diario a las 00:05 AM
- ✅ **Retención configurable**: Por defecto, mantiene backups de los últimos 7 días
- ✅ **Backup manual**: Posibilidad de ejecutar backups manuales a través de API
- ✅ **Almacenamiento persistente**: Los backups se guardan en un volumen Docker dedicado
- ✅ **Limpieza automática**: Elimina backups antiguos según el periodo de retención

### Configuración

Las siguientes variables de entorno están configuradas en `docker-compose.yml`:

```yaml
environment:
  - BACKUP_DIRECTORY=/backups        # Directorio donde se guardan los backups
  - BACKUP_RETENTION_DAYS=7          # Días de retención de backups
```

### Ubicación de los Backups

Los backups se almacenan en:
- **Contenedor Docker**: `/backups/`
- **Volumen Docker**: `db-backups`

Formato de nombre: `backup_db_foodix_YYYYMMDD_HHMMSS.sql`

Ejemplo: `backup_db_foodix_20251211_000500.sql`

### Ejecución Manual de Backup

#### Opción 1: A través de la API (Requiere rol ADMINISTRADOR)

```bash
# Ejecutar backup manual
curl -X POST http://localhost:8080/api/backup/execute \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Verificar estado del servicio de backup
curl -X GET http://localhost:8080/api/backup/status \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Opción 2: Dentro del contenedor Docker

```bash
# Entrar al contenedor de la aplicación
docker exec -it foodix-sistema-mi-app-1 bash

# Ejecutar el script de backup
/usr/local/bin/backup-database.sh
```

#### Opción 3: Script desde PowerShell

```powershell
# Ejecutar backup desde el host
docker exec foodix-sistema-mi-app-1 /usr/local/bin/backup-database.sh
```

### Ver Backups Existentes

```bash
# Listar backups en el volumen
docker exec foodix-sistema-mi-app-1 ls -lh /backups/

# Ver detalles de un backup específico
docker exec foodix-sistema-mi-app-1 ls -lh /backups/backup_db_foodix_20251211_000500.sql
```

### Restaurar un Backup

```bash
# Copiar backup del contenedor al host
docker cp foodix-sistema-mi-app-1:/backups/backup_db_foodix_20251211_000500.sql ./

# Restaurar en la base de datos (desde el contenedor de la BD)
docker exec -i foodix-sistema-db-1 mysql -uroot -proot db_foodix < backup_db_foodix_20251211_000500.sql
```

### Configuración Personalizada

Para cambiar la hora o frecuencia del backup, edita el archivo:
`src/main/java/com/example/SistemaDePromociones/service/DatabaseBackupService.java`

```java
// Cambiar la expresión cron (actualmente: 0 5 0 * * * = 00:05 AM)
@Scheduled(cron = "0 5 0 * * *", zone = "America/Lima")
public void performAutomaticBackup() {
    // ...
}
```

**Ejemplos de expresiones cron:**
- `0 0 2 * * *` - Todos los días a las 2:00 AM
- `0 30 1 * * *` - Todos los días a las 1:30 AM
- `0 0 0 * * SUN` - Todos los domingos a medianoche
- `0 0 */6 * * *` - Cada 6 horas

---

## 📊 Monitoreo de Salud (Spring Actuator)

### Descripción

Se ha implementado **Spring Boot Actuator** para proporcionar endpoints de monitoreo y diagnóstico de la aplicación en tiempo real.

### Endpoints Disponibles

Todos los endpoints están bajo la ruta base: `http://localhost:8080/actuator/`

#### 1. Health Check (Salud del Sistema)
```
GET http://localhost:8080/actuator/health
```

**Respuesta:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 107374182400,
        "free": 53687091200,
        "threshold": 10485760
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

#### 2. Información de la Aplicación
```
GET http://localhost:8080/actuator/info
```

**Respuesta:**
```json
{
  "app": {
    "name": "FoodIx Sistema de Promociones",
    "description": "Sistema de gestión de promociones para restaurantes",
    "version": "0.0.1-SNAPSHOT",
    "encoding": "UTF-8",
    "java": {
      "version": "21"
    }
  }
}
```

#### 3. Métricas del Sistema
```
GET http://localhost:8080/actuator/metrics
```

Lista todas las métricas disponibles:
- `jvm.memory.used` - Memoria JVM utilizada
- `jvm.threads.live` - Threads activos
- `system.cpu.usage` - Uso de CPU
- `http.server.requests` - Estadísticas de requests HTTP
- `jdbc.connections.active` - Conexiones JDBC activas

Para ver una métrica específica:
```
GET http://localhost:8080/actuator/metrics/jvm.memory.used
GET http://localhost:8080/actuator/metrics/system.cpu.usage
```

#### 4. Variables de Entorno
```
GET http://localhost:8080/actuator/env
```

Muestra todas las propiedades de configuración y variables de entorno.

#### 5. Loggers
```
GET http://localhost:8080/actuator/loggers
```

Muestra y permite modificar el nivel de logging en tiempo de ejecución.

#### 6. Thread Dump
```
GET http://localhost:8080/actuator/threaddump
```

Información sobre todos los threads de la JVM.

#### 7. Heap Dump
```
GET http://localhost:8080/actuator/heapdump
```

Descarga un heap dump de la JVM para análisis de memoria.

### Verificación de Endpoints

Puedes verificar todos los endpoints disponibles con:

```bash
# Desde PowerShell
curl http://localhost:8080/actuator

# O desde el navegador
http://localhost:8080/actuator
```

### Endpoints Configurados

En `application.properties` se han configurado los siguientes endpoints:

```properties
# Endpoints expuestos
management.endpoints.web.exposure.include=health,info,metrics,env,loggers,threaddump,heapdump

# Mostrar detalles completos del health check
management.endpoint.health.show-details=always
management.endpoint.health.show-components=always

# Ruta base
management.endpoints.web.base-path=/actuator
```

### Monitoreo en Producción

Para producción, se recomienda:

1. **Restringir acceso a Actuator** mediante autenticación
2. **Exponer solo endpoints necesarios**
3. **Usar un puerto diferente** para Actuator
4. **Integrar con herramientas de monitoreo** como:
   - Prometheus
   - Grafana
   - Spring Boot Admin
   - New Relic
   - Datadog

---

## 🔌 Endpoints API

### Backup Manual (Solo Administradores)

#### Ejecutar Backup
```http
POST /api/backup/execute
Authorization: Bearer {JWT_TOKEN}
```

**Respuesta exitosa:**
```json
{
  "success": true,
  "message": "Backup creado exitosamente",
  "backupFile": "backup_db_foodix_20251211_143022.sql",
  "timestamp": 1702309822000
}
```

#### Estado del Servicio de Backup
```http
GET /api/backup/status
Authorization: Bearer {JWT_TOKEN}
```

**Respuesta:**
```json
{
  "backupServiceActive": true,
  "scheduledBackupTime": "00:05 AM (America/Lima)",
  "nextBackupInfo": "El próximo backup automático se ejecutará a las 00:05 AM"
}
```

---

## 🚀 Despliegue y Uso

### 1. Reconstruir la Aplicación

```powershell
# Detener contenedores
docker-compose down

# Reconstruir
docker-compose build

# Iniciar
docker-compose up -d
```

### 2. Verificar que el Servicio de Backup está Activo

```bash
# Ver logs de la aplicación
docker logs foodix-sistema-mi-app-1 --tail=50

# Buscar mensaje de inicio del scheduler
docker logs foodix-sistema-mi-app-1 | grep -i "scheduling"
```

### 3. Verificar Health Check

```bash
# Desde PowerShell
curl http://localhost:8080/actuator/health

# Desde el navegador
http://localhost:8080/actuator/health
```

### 4. Probar Backup Manual (Como Administrador)

1. Iniciar sesión como administrador
2. Obtener el JWT token
3. Ejecutar:

```bash
curl -X POST http://localhost:8080/api/backup/execute \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 📝 Logs y Monitoreo

### Ver Logs de Backup

```bash
# Logs en tiempo real
docker logs -f foodix-sistema-mi-app-1 | grep -i backup

# Logs específicos de backup
docker logs foodix-sistema-mi-app-1 2>&1 | grep "DatabaseBackupService"
```

### Verificar Salud del Sistema

```bash
# Health check completo
curl http://localhost:8080/actuator/health | jq

# Estado de la base de datos
curl http://localhost:8080/actuator/health | jq '.components.db'

# Espacio en disco
curl http://localhost:8080/actuator/health | jq '.components.diskSpace'
```

---

## 🔒 Seguridad

- ✅ Los endpoints de backup requieren **autenticación JWT** y rol **ADMINISTRADOR**
- ✅ Los endpoints de Actuator están **públicos** (cambiar en producción)
- ✅ Los backups se almacenan en un **volumen protegido**
- ✅ Las credenciales de la BD están en **variables de entorno**

### Recomendaciones de Seguridad para Producción

1. **Proteger Actuator:**
   ```properties
   # Solo exponer health e info públicamente
   management.endpoints.web.exposure.include=health,info
   
   # Requiere autenticación para otros endpoints
   management.endpoint.health.show-details=when-authorized
   ```

2. **Encriptar Backups:**
   - Implementar encriptación GPG para backups sensibles
   - Usar contraseñas seguras en variables de entorno

3. **Backup Remoto:**
   - Configurar sincronización con almacenamiento en la nube (AWS S3, Azure Blob, etc.)
   - Implementar backup off-site

---

## 📞 Soporte

Para problemas o consultas sobre el sistema de mantenimiento:

- Revisar logs: `docker logs foodix-sistema-mi-app-1`
- Verificar health: `http://localhost:8080/actuator/health`
- Contactar al equipo de desarrollo

---

## 📅 Changelog

### v1.0.0 (2025-12-11)
- ✅ Implementado backup automático diario a las 00:05 AM
- ✅ Integrado Spring Boot Actuator
- ✅ Creado API REST para gestión de backups
- ✅ Configurado volumen Docker para almacenamiento de backups
- ✅ Implementada limpieza automática de backups antiguos
