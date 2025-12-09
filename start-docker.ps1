# Script para iniciar el proyecto FooDIx con Docker
# Ejecutar como ADMINISTRADOR

Write-Host "=== FooDIx - Iniciar con Docker ===" -ForegroundColor Cyan
Write-Host ""

# 1. Detener MySQL local si está corriendo
Write-Host "1. Deteniendo MySQL local..." -ForegroundColor Yellow
$mysqlService = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
if ($mysqlService -and $mysqlService.Status -eq "Running") {
    Stop-Service -Name "MySQL80" -Force
    Write-Host "   ✓ MySQL local detenido" -ForegroundColor Green
} else {
    Write-Host "   - MySQL local no está corriendo" -ForegroundColor Gray
}

# Esperar un momento para que se libere el puerto
Start-Sleep -Seconds 2

# 2. Construir el JAR
Write-Host ""
Write-Host "2. Construyendo el archivo JAR..." -ForegroundColor Yellow
& .\mvnw.cmd clean package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "   ✗ Error al construir el JAR" -ForegroundColor Red
    exit 1
}
Write-Host "   ✓ JAR construido exitosamente" -ForegroundColor Green

# 3. Detener contenedores anteriores
Write-Host ""
Write-Host "3. Limpiando contenedores anteriores..." -ForegroundColor Yellow
docker-compose down 2>$null
Write-Host "   ✓ Contenedores anteriores removidos" -ForegroundColor Green

# 4. Construir y levantar Docker
Write-Host ""
Write-Host "4. Construyendo y levantando contenedores Docker..." -ForegroundColor Yellow
docker-compose up --build -d
if ($LASTEXITCODE -ne 0) {
    Write-Host "   ✗ Error al levantar Docker" -ForegroundColor Red
    exit 1
}

# 5. Mostrar logs
Write-Host ""
Write-Host "=== Contenedores iniciados ===" -ForegroundColor Green
Write-Host ""
Write-Host "Ver logs en tiempo real:" -ForegroundColor Cyan
Write-Host "  docker-compose logs -f" -ForegroundColor White
Write-Host ""
Write-Host "Acceder a la aplicación:" -ForegroundColor Cyan
Write-Host "  http://localhost:8080" -ForegroundColor White
Write-Host ""
Write-Host "Detener los contenedores:" -ForegroundColor Cyan
Write-Host "  docker-compose down" -ForegroundColor White
Write-Host ""

# Preguntar si quiere ver los logs
$response = Read-Host "¿Deseas ver los logs ahora? (s/n)"
if ($response -eq 's' -or $response -eq 'S') {
    docker-compose logs -f
}
