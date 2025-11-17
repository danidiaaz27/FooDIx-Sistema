#!/bin/bash
set -e

echo "Esperando a que MySQL esté listo..."
until mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" -e "SELECT 1" &> /dev/null; do
  sleep 1
done

echo "Cargando datos iniciales..."
mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" db_foodix < /docker-entrypoint-initdb.d/datos_iniciales.sql

echo "¡Datos iniciales cargados exitosamente!"
