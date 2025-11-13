# ============================================
# SCRIPT DE RESET DE BASE DE DATOS
# ============================================
# Este script limpia y recarga los datos iniciales
# ============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  RESET DE BASE DE DATOS - FooDix" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que Docker esté corriendo
Write-Host "[1/4] Verificando contenedor de base de datos..." -ForegroundColor Yellow
$container = docker ps --filter "name=sistemafoodix-db-1" --format "{{.Names}}"
if ($container -ne "sistemafoodix-db-1") {
    Write-Host "ERROR: El contenedor de base de datos no está corriendo." -ForegroundColor Red
    Write-Host "Ejecuta primero: docker-compose up -d" -ForegroundColor Yellow
    exit 1
}
Write-Host "   ✓ Contenedor encontrado" -ForegroundColor Green
Write-Host ""

# Paso 1: Limpiar la base de datos
Write-Host "[2/4] Limpiando base de datos..." -ForegroundColor Yellow
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix < limpiar_db.sql 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✓ Base de datos limpiada" -ForegroundColor Green
} else {
    Write-Host "   ✗ Error al limpiar" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Paso 2: Cargar datos iniciales
Write-Host "[3/4] Cargando datos iniciales..." -ForegroundColor Yellow
docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix < datos_iniciales.sql 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✓ Datos cargados correctamente" -ForegroundColor Green
} else {
    Write-Host "   ✗ Error al cargar datos" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Paso 3: Verificar
Write-Host "[4/4] Verificando resultados..." -ForegroundColor Yellow
$categorias = docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT COUNT(*) FROM categoria;" 2>$null | Select-Object -Last 1
$distritos = docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT COUNT(*) FROM distrito;" 2>$null | Select-Object -Last 1
$usuarios = docker exec -i sistemafoodix-db-1 mysql -u root -proot db_foodix -e "SELECT COUNT(*) FROM usuario;" 2>$null | Select-Object -Last 1

Write-Host "   • Categorías: $categorias" -ForegroundColor Cyan
Write-Host "   • Distritos: $distritos" -ForegroundColor Cyan
Write-Host "   • Usuarios: $usuarios" -ForegroundColor Cyan
Write-Host ""

Write-Host "========================================" -ForegroundColor Green
Write-Host "  ✓ RESET COMPLETADO EXITOSAMENTE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Credenciales de admin:" -ForegroundColor Yellow
Write-Host "   Email: daniela@FooDix.com.pe" -ForegroundColor White
Write-Host "   Password: 525224Da!" -ForegroundColor White
Write-Host ""
