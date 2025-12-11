#!/bin/bash

# Script de backup manual para base de datos MySQL
# Este script puede ser ejecutado manualmente dentro del contenedor

# Variables de configuración
DB_HOST="db"
DB_PORT="3306"
DB_NAME="db_foodix"
DB_USER="root"
DB_PASSWORD="root"
BACKUP_DIR="/backups"

# Crear directorio de backups si no existe
mkdir -p $BACKUP_DIR

# Generar timestamp para el nombre del archivo
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="$BACKUP_DIR/backup_${DB_NAME}_${TIMESTAMP}.sql"

echo "========================================="
echo "Iniciando backup de base de datos"
echo "========================================="
echo "Fecha: $(date)"
echo "Base de datos: $DB_NAME"
echo "Host: $DB_HOST:$DB_PORT"
echo "Archivo de destino: $BACKUP_FILE"
echo "========================================="

# Ejecutar mysqldump
mysqldump -h $DB_HOST \
          -P $DB_PORT \
          -u $DB_USER \
          -p$DB_PASSWORD \
          --databases $DB_NAME \
          --single-transaction \
          --routines \
          --triggers \
          --events \
          > $BACKUP_FILE

# Verificar si el backup fue exitoso
if [ $? -eq 0 ]; then
    BACKUP_SIZE=$(du -h "$BACKUP_FILE" | cut -f1)
    echo "========================================="
    echo "✓ Backup completado exitosamente"
    echo "Tamaño del archivo: $BACKUP_SIZE"
    echo "Ubicación: $BACKUP_FILE"
    echo "========================================="
    
    # Listar backups existentes
    echo ""
    echo "Backups existentes:"
    ls -lh $BACKUP_DIR/*.sql 2>/dev/null || echo "No hay backups previos"
    
    exit 0
else
    echo "========================================="
    echo "✗ Error al crear el backup"
    echo "========================================="
    exit 1
fi
