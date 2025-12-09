# Script para detener Docker y reiniciar MySQL local
# Ejecutar como ADMINISTRADOR

Write-Host "=== FooDIx - Detener Docker y restaurar MySQL ===" -ForegroundColor Cyan
Write-Host ""

# 1. Detener contenedores Docker
Write-Host "1. Deteniendo contenedores Docker..." -ForegroundColor Yellow
docker-compose down
Write-Host "   ✓ Contenedores detenidos" -ForegroundColor Green

# 2. Reiniciar MySQL local
Write-Host ""
Write-Host "2. Iniciando MySQL local..." -ForegroundColor Yellow
$mysqlService = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
if ($mysqlService) {
    Start-Service -Name "MySQL80"
    Write-Host "   ✓ MySQL local iniciado" -ForegroundColor Green
} else {
    Write-Host "   - Servicio MySQL80 no encontrado" -ForegroundColor Gray
}

Write-Host ""
Write-Host "=== Listo ===" -ForegroundColor Green
Write-Host ""
