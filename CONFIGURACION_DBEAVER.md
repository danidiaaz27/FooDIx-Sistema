# 📊 Configuración de DBeaver para FooDix

## ✅ Estado Actual
Tu aplicación está funcionando correctamente:
- ✅ Docker contenedores activos
- ✅ MySQL corriendo en puerto 3306
- ✅ Base de datos `db_foodix` creada
- ✅ 14 tablas disponibles
- ✅ Aplicación Spring Boot conectada

## 🔧 Configuración de DBeaver

### 1. Nueva Conexión MySQL

1. **Abrir DBeaver**
2. Click en **Database** → **New Database Connection**
3. Seleccionar **MySQL**
4. Click en **Next**

### 2. Datos de Conexión

```
Host: localhost
Puerto: 3306
Database: db_foodix
Usuario: root
Contraseña: root
```

### 3. Configuración Detallada

**Pestaña Main:**
```
Server Host: localhost
Port: 3306
Database: db_foodix
Username: root
Password: root
```

**Pestaña Driver Properties (Agregar si hay problemas de conexión):**
```
allowPublicKeyRetrieval: true
useSSL: false
serverTimezone: UTC
```

### 4. Test de Conexión

1. Click en **Test Connection**
2. Si es la primera vez, DBeaver descargará el driver MySQL automáticamente
3. Debería mostrar **"Connected"**

### 5. Troubleshooting

#### ❌ Error: "Public Key Retrieval is not allowed"
**Solución:** En Driver Properties, agregar:
```
allowPublicKeyRetrieval=true
```

#### ❌ Error: "Access denied"
**Solución:** Verificar que Docker esté corriendo:
```powershell
docker ps
```
Deberías ver `sistemafoodix-db-1` con estado `Up`

#### ❌ Error: "Communications link failure"
**Solución:** Verificar el puerto:
```powershell
docker ps
```
Buscar la línea con MySQL y verificar: `0.0.0.0:3306->3306/tcp`

### 6. Comandos Útiles de Verificación

```powershell
# Ver estado de contenedores
docker ps

# Ver logs de MySQL
docker logs sistemafoodix-db-1 --tail 50

# Conectarse directamente al MySQL en Docker
docker exec -it sistemafoodix-db-1 mysql -uroot -proot db_foodix

# Ver tablas desde terminal
docker exec sistemafoodix-db-1 mysql -uroot -proot -e "USE db_foodix; SHOW TABLES;"
```

### 7. Tablas Disponibles en db_foodix

```
✅ categoria
✅ categoria_restaurante
✅ departamento
✅ distrito
✅ documento_repartidor
✅ documento_restaurante
✅ estado_aprobacion
✅ imagen_restaurante
✅ provincia
✅ repartidor
✅ restaurante
✅ rol
✅ tipo_vehiculo
✅ usuario
```

## 🎯 Ejemplo de Query para Probar

Una vez conectado en DBeaver, prueba:

```sql
-- Ver todos los usuarios
SELECT * FROM usuario;

-- Ver restaurantes
SELECT * FROM restaurante;

-- Ver categorías
SELECT * FROM categoria;

-- Ver estructura de una tabla
DESCRIBE usuario;
```

## 🚀 Acceso Web a la Aplicación

Tu aplicación está disponible en:
```
http://localhost:8080
```

## 📝 Notas Importantes

1. **Credenciales de prueba:**
   - Admin: Ver datos en tabla `usuario` con `codigo_rol = 1`
   
2. **Puerto 3306:** Asegúrate que no haya otro MySQL corriendo en tu máquina que use el mismo puerto

3. **Persistencia de datos:** Los datos se guardan en el volumen `db-data` de Docker, por lo que persisten aunque detengas los contenedores

4. **Reiniciar contenedores si es necesario:**
   ```powershell
   docker-compose down
   docker-compose up -d
   ```
