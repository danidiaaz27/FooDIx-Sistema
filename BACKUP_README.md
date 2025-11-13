# 💾 Guía de Manejo de Backups - FooDix

## 📁 Archivos de Backup Disponibles

### `backup_db_foodix_2025-11-10_15-03-36.sql`
Backup completo de la base de datos que incluye:
- ✅ Todas las tablas con estructura completa
- ✅ Todos los datos (categorías, distritos, usuarios, restaurantes, etc.)
- ✅ Configuración de codificación UTF-8
- ✅ Foreign keys y constraints

---

## 🔄 Cómo Restaurar el Backup

### Método 1: Script Automatizado (RECOMENDADO) ⭐

El método más fácil y seguro es usar el script PowerShell incluido:

```powershell
.\restore_backup.ps1
```

**Este script hace todo automáticamente:**
1. Verifica que Docker esté corriendo
2. Verifica que existe el archivo de backup
3. Espera a que MySQL esté completamente listo
4. Restaura el backup completo
5. Verifica que los datos se restauraron correctamente

**Salida esperada:**
```
========================================
  RESTAURACIÓN DE BACKUP - FooDix
========================================

[1/5] Verificando contenedor de base de datos...
   ✓ Contenedor encontrado

[2/5] Verificando archivo de backup...
   ✓ Archivo encontrado

[3/5] Esperando a que MySQL esté listo...
   ✓ MySQL está listo

[4/5] Restaurando backup...
   ✓ Backup restaurado correctamente

[5/5] Verificando datos restaurados...
   • Categorías: 12
   • Distritos: 43
   • Usuarios: X
   • Restaurantes: Y

========================================
  ✓ RESTAURACIÓN COMPLETADA
========================================
```

---

### Método 2: Manual con Docker

Si prefieres hacerlo manualmente:

```powershell
# Paso 1: Verificar que el contenedor está corriendo
docker ps

# Paso 2: Copiar el backup al contenedor
docker cp backup_db_foodix_2025-11-10_15-03-36.sql sistemafoodix-db-1:/tmp/backup.sql

# Paso 3: Restaurar desde dentro del contenedor
docker exec sistemafoodix-db-1 mysql -u root -proot db_foodix -e "source /tmp/backup.sql"

# Paso 4: Limpiar archivo temporal
docker exec sistemafoodix-db-1 rm /tmp/backup.sql

# Paso 5: Verificar
docker exec sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT COUNT(*) as total_usuarios FROM usuario;"
```

---

### Método 3: Con Pipe (Alternativo)

```powershell
Get-Content backup_db_foodix_2025-11-10_15-03-36.sql | docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix
```

⚠️ **Nota**: Este método puede ser más lento para archivos grandes.

---

## 🆕 Cómo Crear un Nuevo Backup

### Backup Completo

```powershell
# Crear backup con timestamp
$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
docker exec sistemafoodix-db-1 mysqldump -u root -proot db_foodix > "backup_db_foodix_$timestamp.sql"
```

### Backup Solo de Datos (sin estructura)

```powershell
$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
docker exec sistemafoodix-db-1 mysqldump -u root -proot --no-create-info db_foodix > "backup_data_only_$timestamp.sql"
```

### Backup Solo de Estructura (sin datos)

```powershell
$timestamp = Get-Date -Format "yyyy-MM-dd_HH-mm-ss"
docker exec sistemafoodix-db-1 mysqldump -u root -proot --no-data db_foodix > "backup_structure_only_$timestamp.sql"
```

---

## 🔍 Verificar el Contenido del Backup

### Ver las primeras líneas del backup

```powershell
Get-Content backup_db_foodix_2025-11-10_15-03-36.sql -First 50
```

### Ver qué tablas incluye

```powershell
Select-String -Path "backup_db_foodix_2025-11-10_15-03-36.sql" -Pattern "DROP TABLE IF EXISTS"
```

### Ver cantidad de registros INSERT

```powershell
Select-String -Path "backup_db_foodix_2025-11-10_15-03-36.sql" -Pattern "^INSERT INTO" | Measure-Object
```

---

## 🚨 Solución de Problemas

### Problema: "ERROR: El contenedor de base de datos no está corriendo"

**Solución**: Inicia los contenedores primero:
```powershell
docker-compose up -d
# Espera 10-15 segundos
.\restore_backup.ps1
```

### Problema: "Error al restaurar el backup"

**Soluciones**:

1. **Verifica que MySQL esté listo**:
   ```powershell
   docker exec sistemafoodix-db-1 mysqladmin ping -h localhost -u root -proot
   ```
   Debe responder: `mysqld is alive`

2. **Revisa los logs de MySQL**:
   ```powershell
   docker logs sistemafoodix-db-1 --tail 50
   ```

3. **Intenta limpiar la base de datos primero**:
   ```powershell
   .\reset_db.ps1
   # Luego restaura el backup
   .\restore_backup.ps1
   ```

### Problema: Caracteres especiales se ven mal (├â, ┬, etc.)

Esto es normal en la consola de PowerShell. La base de datos está correctamente en UTF-8. Para verificar:

```powershell
docker exec sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT nombre FROM categoria LIMIT 3;"
```

Si ves caracteres raros en PowerShell pero la aplicación web muestra bien, todo está correcto.

---

## 📊 Comparar Backups

### Ver diferencias entre dos archivos

```powershell
Compare-Object -ReferenceObject (Get-Content backup1.sql) -DifferenceObject (Get-Content backup2.sql)
```

### Ver tamaño de los backups

```powershell
Get-ChildItem backup_*.sql | Select-Object Name, @{Name="Size(MB)";Expression={[math]::Round($_.Length/1MB,2)}}
```

---

## 🔐 Buenas Prácticas

### ✅ DO (Hacer)

- ✅ Crear backups antes de cambios importantes
- ✅ Nombrar backups con fecha/hora (ya incluido en el nombre)
- ✅ Guardar backups en un lugar seguro (Git, Drive, etc.)
- ✅ Probar que el backup se puede restaurar correctamente
- ✅ Mantener al menos 3 backups históricos

### ❌ DON'T (No Hacer)

- ❌ Subir backups con datos sensibles a repositorios públicos
- ❌ Sobrescribir backups antiguos sin verificar
- ❌ Restaurar backups en producción sin probarlos antes
- ❌ Olvidar verificar después de restaurar

---

## 🗓️ Plan de Backups Recomendado

### Para Desarrollo
- **Diario**: Antes de hacer cambios grandes
- **Semanal**: Backup completo automático

### Para Producción
- **Diario**: Backup automático a las 2 AM
- **Antes de deploy**: Backup manual obligatorio
- **Después de deploy exitoso**: Backup de verificación

---

## 📝 Scripts Relacionados

| Script | Propósito |
|--------|-----------|
| `restore_backup.ps1` | Restaura el backup completo (RECOMENDADO) |
| `reset_db.ps1` | Limpia y carga datos iniciales básicos |
| `limpiar_db.sql` | Script SQL para limpiar todas las tablas |
| `datos_iniciales.sql` | Script SQL con datos básicos |
| `verificar_restaurantes.sql` | Script para verificar datos de restaurantes |

---

## 💡 Consejos Útiles

### Backup Antes de Pruebas

```powershell
# Crear backup rápido antes de pruebas
$backup = "backup_antes_pruebas_$(Get-Date -Format 'yyyyMMdd_HHmmss').sql"
docker exec sistemafoodix-db-1 mysqldump -u root -proot db_foodix > $backup
Write-Host "Backup creado: $backup" -ForegroundColor Green
```

### Restaurar el Último Backup

```powershell
# Encuentra y restaura el backup más reciente
$latestBackup = Get-ChildItem backup_*.sql | Sort-Object LastWriteTime -Descending | Select-Object -First 1
Write-Host "Restaurando: $($latestBackup.Name)" -ForegroundColor Yellow
Get-Content $latestBackup.FullName | docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix
```

---

**¿Preguntas?** Revisa `INSTRUCCIONES_SETUP.md` para más información sobre el proyecto.
