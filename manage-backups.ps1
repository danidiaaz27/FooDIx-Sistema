# ========================================
# Script de Gestión de Backups - FoodIx
# ========================================
# Este script facilita la gestión de backups de la base de datos

param(
    [Parameter(Position=0)]
    [ValidateSet('create', 'list', 'restore', 'clean', 'status', 'help')]
    [string]$Action = 'help'
)

$ErrorActionPreference = "Stop"

# Colores para output
function Write-ColorOutput($ForegroundColor) {
    $fc = $host.UI.RawUI.ForegroundColor
    $host.UI.RawUI.ForegroundColor = $ForegroundColor
    if ($args) {
        Write-Output $args
    }
    $host.UI.RawUI.ForegroundColor = $fc
}

function Show-Help {
    Write-Host ""
    Write-ColorOutput Green "========================================="
    Write-ColorOutput Green "  Script de Gestión de Backups - FoodIx"
    Write-ColorOutput Green "========================================="
    Write-Host ""
    Write-Host "USO: .\manage-backups.ps1 [ACCIÓN]"
    Write-Host ""
    Write-Host "ACCIONES DISPONIBLES:"
    Write-Host ""
    Write-Host "  create  - Crear un backup manual inmediatamente"
    Write-Host "  list    - Listar todos los backups existentes"
    Write-Host "  restore - Restaurar un backup específico"
    Write-Host "  clean   - Limpiar backups antiguos (más de 7 días)"
    Write-Host "  status  - Verificar estado del servicio de backup"
    Write-Host "  help    - Mostrar esta ayuda"
    Write-Host ""
    Write-ColorOutput Cyan "EJEMPLOS:"
    Write-Host "  .\manage-backups.ps1 create"
    Write-Host "  .\manage-backups.ps1 list"
    Write-Host "  .\manage-backups.ps1 status"
    Write-Host ""
}

function Create-Backup {
    Write-ColorOutput Yellow "📦 Creando backup manual..."
    Write-Host ""
    
    try {
        docker exec foodix-sistema-mi-app-1 /usr/local/bin/backup-database.sh
        
        Write-Host ""
        Write-ColorOutput Green "✓ Backup creado exitosamente"
    }
    catch {
        Write-ColorOutput Red "✗ Error al crear backup: $_"
        exit 1
    }
}

function List-Backups {
    Write-ColorOutput Yellow "📋 Listando backups existentes..."
    Write-Host ""
    
    try {
        $backups = docker exec foodix-sistema-mi-app-1 ls -lh /backups/ 2>&1
        
        if ($backups -match "No such file") {
            Write-ColorOutput Yellow "⚠ No hay backups disponibles"
        } else {
            docker exec foodix-sistema-mi-app-1 sh -c "ls -lh /backups/*.sql 2>/dev/null || echo 'No hay archivos de backup'"
            Write-Host ""
            
            # Contar backups
            $count = (docker exec foodix-sistema-mi-app-1 sh -c "ls /backups/*.sql 2>/dev/null | wc -l").Trim()
            Write-ColorOutput Cyan "Total de backups: $count"
        }
    }
    catch {
        Write-ColorOutput Red "✗ Error al listar backups: $_"
        exit 1
    }
}

function Restore-Backup {
    Write-ColorOutput Yellow "🔄 Restaurar backup de base de datos"
    Write-Host ""
    
    # Listar backups disponibles
    Write-Host "Backups disponibles:"
    Write-Host ""
    docker exec foodix-sistema-mi-app-1 sh -c "ls -1 /backups/*.sql 2>/dev/null | nl" 2>&1
    
    Write-Host ""
    $backupFile = Read-Host "Ingrese el nombre del archivo de backup (ej: backup_db_foodix_20251211_000500.sql)"
    
    if ([string]::IsNullOrWhiteSpace($backupFile)) {
        Write-ColorOutput Red "✗ Debe especificar un archivo de backup"
        exit 1
    }
    
    Write-ColorOutput Yellow "⚠ ADVERTENCIA: Esto sobrescribirá la base de datos actual"
    $confirm = Read-Host "¿Está seguro? (escriba 'SI' para confirmar)"
    
    if ($confirm -ne "SI") {
        Write-ColorOutput Yellow "Operación cancelada"
        exit 0
    }
    
    try {
        Write-ColorOutput Yellow "Restaurando backup: $backupFile"
        
        # Copiar backup del contenedor al host temporalmente
        docker cp "foodix-sistema-mi-app-1:/backups/$backupFile" "./temp_backup.sql"
        
        # Restaurar en la base de datos
        Get-Content "./temp_backup.sql" | docker exec -i foodix-sistema-db-1 mysql -uroot -proot
        
        # Limpiar archivo temporal
        Remove-Item "./temp_backup.sql" -Force
        
        Write-Host ""
        Write-ColorOutput Green "✓ Backup restaurado exitosamente"
    }
    catch {
        Write-ColorOutput Red "✗ Error al restaurar backup: $_"
        
        # Limpiar archivo temporal si existe
        if (Test-Path "./temp_backup.sql") {
            Remove-Item "./temp_backup.sql" -Force
        }
        
        exit 1
    }
}

function Clean-OldBackups {
    Write-ColorOutput Yellow "🧹 Limpiando backups antiguos (más de 7 días)..."
    Write-Host ""
    
    try {
        # Ejecutar limpieza
        docker exec foodix-sistema-mi-app-1 sh -c "find /backups -name '*.sql' -type f -mtime +7 -exec rm -v {} \;"
        
        Write-Host ""
        Write-ColorOutput Green "✓ Limpieza completada"
        
        # Mostrar backups restantes
        Write-Host ""
        Write-ColorOutput Cyan "Backups restantes:"
        docker exec foodix-sistema-mi-app-1 sh -c "ls -lh /backups/*.sql 2>/dev/null || echo 'No hay backups'"
    }
    catch {
        Write-ColorOutput Red "✗ Error al limpiar backups: $_"
        exit 1
    }
}

function Check-Status {
    Write-ColorOutput Yellow "📊 Verificando estado del sistema de backups..."
    Write-Host ""
    
    try {
        # Verificar contenedor
        Write-Host "Estado del contenedor:"
        docker ps --filter "name=foodix-sistema-mi-app-1" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
        Write-Host ""
        
        # Verificar directorio de backups
        Write-Host "Directorio de backups:"
        docker exec foodix-sistema-mi-app-1 sh -c "ls -ld /backups && du -sh /backups"
        Write-Host ""
        
        # Contar backups
        $count = (docker exec foodix-sistema-mi-app-1 sh -c "ls /backups/*.sql 2>/dev/null | wc -l").Trim()
        Write-ColorOutput Cyan "Backups totales: $count"
        
        # Último backup
        Write-Host ""
        Write-Host "Último backup creado:"
        docker exec foodix-sistema-mi-app-1 sh -c "ls -lt /backups/*.sql 2>/dev/null | head -1"
        
        # Health check
        Write-Host ""
        Write-Host "Health Check de la aplicación:"
        try {
            $health = Invoke-RestMethod -Uri "http://localhost:8080/actuator/health" -TimeoutSec 5
            Write-ColorOutput Green "✓ Aplicación: $($health.status)"
        }
        catch {
            Write-ColorOutput Red "✗ No se pudo conectar al health check"
        }
        
        Write-Host ""
        Write-ColorOutput Green "✓ Verificación completada"
    }
    catch {
        Write-ColorOutput Red "✗ Error al verificar estado: $_"
        exit 1
    }
}

# ========================================
# MAIN - Ejecutar acción
# ========================================

switch ($Action) {
    'create' { Create-Backup }
    'list' { List-Backups }
    'restore' { Restore-Backup }
    'clean' { Clean-OldBackups }
    'status' { Check-Status }
    'help' { Show-Help }
    default { Show-Help }
}
