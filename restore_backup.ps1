# ============================================
# SCRIPT DE RESTAURACION DE BACKUP
# ============================================
# Este script restaura el backup completo de la base de datos
# ============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RESTAURACION DE BACKUP - FooDix" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que Docker este corriendo
Write-Host "[1/5] Verificando contenedor de base de datos..." -ForegroundColor Yellow
$containers = docker ps --format "{{.Names}}"
$containerName = $containers | Where-Object { $_ -match "db" } | Select-Object -First 1

if (-not $containerName) {
    Write-Host "ERROR: El contenedor de base de datos no esta corriendo." -ForegroundColor Red
    Write-Host "Ejecuta primero: docker-compose up -d" -ForegroundColor Yellow
    exit 1
}
Write-Host "   Contenedor encontrado: $containerName" -ForegroundColor Green
Write-Host ""

# Verificar que existe el archivo de backup
Write-Host "[2/5] Verificando archivo de backup..." -ForegroundColor Yellow
$backupFile = "backup_db_foodix_2025-11-10_15-03-36.sql"
if (-not (Test-Path $backupFile)) {
    Write-Host "ERROR: No se encuentra el archivo $backupFile" -ForegroundColor Red
    exit 1
}
Write-Host "   Archivo encontrado" -ForegroundColor Green
Write-Host ""

# Esperar a que MySQL este completamente listo
Write-Host "[3/5] Esperando a que MySQL este listo..." -ForegroundColor Yellow
$maxRetries = 30
$retryCount = 0
$mysqlReady = $false

while (($retryCount -lt $maxRetries) -and (-not $mysqlReady)) {
    docker exec $containerName mysqladmin ping -h localhost -u root -proot 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        $mysqlReady = $true
        Write-Host "   MySQL esta listo" -ForegroundColor Green
    } else {
        $retryCount++
        Write-Host "   Esperando... (intento $retryCount de $maxRetries)" -ForegroundColor Gray
        Start-Sleep -Seconds 2
    }
}

if (-not $mysqlReady) {
    Write-Host "ERROR: MySQL no respondio despues de esperar." -ForegroundColor Red
    exit 1
}
Write-Host ""

# Restaurar el backup
Write-Host "[4/5] Restaurando backup..." -ForegroundColor Yellow
Write-Host "   Esto puede tomar unos momentos..." -ForegroundColor Gray

# Usar Get-Content y pipe directo
Get-Content $backupFile | docker exec -i $containerName mysql -u root -proot db_foodix 2>&1 | Out-Null

if ($LASTEXITCODE -eq 0) {
    Write-Host "   Backup restaurado correctamente" -ForegroundColor Green
} else {
    Write-Host "   Error al restaurar el backup" -ForegroundColor Red
    Write-Host "   Codigo de error: $LASTEXITCODE" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Verificar resultados
Write-Host "[5/5] Verificando datos restaurados..." -ForegroundColor Yellow

$queryCateg = "SELECT COUNT(*) FROM categoria;"
$queryDist = "SELECT COUNT(*) FROM distrito;"
$queryUser = "SELECT COUNT(*) FROM usuario;"
$queryRest = "SELECT COUNT(*) FROM restaurante;"

$categorias = docker exec $containerName mysql -u root -proot db_foodix -e $queryCateg 2>$null | Select-Object -Last 1
$distritos = docker exec $containerName mysql -u root -proot db_foodix -e $queryDist 2>$null | Select-Object -Last 1
$usuarios = docker exec $containerName mysql -u root -proot db_foodix -e $queryUser 2>$null | Select-Object -Last 1
$restaurantes = docker exec $containerName mysql -u root -proot db_foodix -e $queryRest 2>$null | Select-Object -Last 1

if ($categorias) {
    Write-Host "   Categorias: $($categorias.Trim())" -ForegroundColor Cyan
}
if ($distritos) {
    Write-Host "   Distritos: $($distritos.Trim())" -ForegroundColor Cyan
}
if ($usuarios) {
    Write-Host "   Usuarios: $($usuarios.Trim())" -ForegroundColor Cyan
}
if ($restaurantes) {
    Write-Host "   Restaurantes: $($restaurantes.Trim())" -ForegroundColor Cyan
}
Write-Host ""

Write-Host "========================================" -ForegroundColor Green
Write-Host "  RESTAURACION COMPLETADA" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Tu base de datos ha sido restaurada desde el backup." -ForegroundColor White
Write-Host "La aplicacion ya esta corriendo en: http://localhost:8080" -ForegroundColor Yellow
Write-Host ""
