package com.example.SistemaDePromociones.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Servicio para realizar backups automáticos de la base de datos
 * Ejecuta un backup diario a las 00:05 AM
 */
@Service
public class DatabaseBackupService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseBackupService.class);

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${backup.directory:/backups}")
    private String backupDirectory;

    @Value("${backup.retention.days:7}")
    private int retentionDays;

    /**
     * Ejecuta el backup automático todos los días a las 00:05 AM
     * Cron: segundo minuto hora día mes día-semana
     */
    @Scheduled(cron = "0 5 0 * * *", zone = "America/Lima")
    public void performAutomaticBackup() {
        logger.info("=== Iniciando backup automático de base de datos ===");
        try {
            String backupFileName = createBackup();
            if (backupFileName != null) {
                logger.info("Backup completado exitosamente: {}", backupFileName);
                cleanOldBackups();
            } else {
                logger.error("Error al crear el backup");
            }
        } catch (Exception e) {
            logger.error("Error durante el proceso de backup automático", e);
        }
    }

    /**
     * Crea un backup de la base de datos
     * @return El nombre del archivo de backup creado, o null si falla
     */
    public String createBackup() {
        try {
            // Crear directorio de backups si no existe
            Path backupPath = Paths.get(backupDirectory);
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
                logger.info("Directorio de backups creado: {}", backupDirectory);
            }

            // Extraer información de la URL de la base de datos
            String[] urlParts = extractDatabaseInfo(databaseUrl);
            String host = urlParts[0];
            String port = urlParts[1];
            String database = urlParts[2];

            // Generar nombre del archivo con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupFileName = String.format("backup_%s_%s.sql", database, timestamp);
            String backupFilePath = Paths.get(backupDirectory, backupFileName).toString();

            // Construir comando mysqldump
            ProcessBuilder processBuilder = new ProcessBuilder();
            
            // Determinar si estamos en Windows o Linux
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // Para Windows (desarrollo local)
                processBuilder.command(
                    "mysqldump",
                    "-h", host,
                    "-P", port,
                    "-u", databaseUsername,
                    "-p" + databasePassword,
                    "--databases", database,
                    "--result-file=" + backupFilePath,
                    "--single-transaction",
                    "--routines",
                    "--triggers"
                );
            } else {
                // Para Linux/Docker
                processBuilder.command(
                    "sh", "-c",
                    String.format(
                        "mysqldump -h %s -P %s -u %s -p%s --databases %s --single-transaction --routines --triggers > %s",
                        host, port, databaseUsername, databasePassword, database, backupFilePath
                    )
                );
            }

            logger.info("Ejecutando backup de base de datos: {}", database);
            Process process = processBuilder.start();

            // Leer salida del proceso
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                File backupFile = new File(backupFilePath);
                if (backupFile.exists() && backupFile.length() > 0) {
                    logger.info("Backup creado exitosamente: {} ({} bytes)", 
                               backupFileName, backupFile.length());
                    return backupFileName;
                } else {
                    logger.error("El archivo de backup está vacío o no existe");
                    return null;
                }
            } else {
                logger.error("Error al ejecutar mysqldump. Código de salida: {}. Salida: {}", 
                            exitCode, output.toString());
                return null;
            }

        } catch (IOException | InterruptedException e) {
            logger.error("Excepción durante la creación del backup", e);
            return null;
        }
    }

    /**
     * Limpia backups antiguos según el periodo de retención configurado
     */
    private void cleanOldBackups() {
        try {
            Path backupPath = Paths.get(backupDirectory);
            if (!Files.exists(backupPath)) {
                return;
            }

            long cutoffTime = System.currentTimeMillis() - (retentionDays * 24L * 60 * 60 * 1000);
            
            Files.list(backupPath)
                .filter(path -> path.toString().endsWith(".sql"))
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toMillis() < cutoffTime;
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                        logger.info("Backup antiguo eliminado: {}", path.getFileName());
                    } catch (IOException e) {
                        logger.error("Error al eliminar backup antiguo: {}", path.getFileName(), e);
                    }
                });

        } catch (IOException e) {
            logger.error("Error al limpiar backups antiguos", e);
        }
    }

    /**
     * Extrae host, puerto y nombre de base de datos de la URL JDBC
     * @param jdbcUrl URL JDBC de conexión
     * @return Array con [host, port, database]
     */
    private String[] extractDatabaseInfo(String jdbcUrl) {
        // jdbc:mysql://db:3306/db_foodix?...
        String[] result = new String[3];
        
        try {
            // Remover el prefijo jdbc:mysql://
            String cleanUrl = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2);
            
            // Separar la parte de host:port/database de los parámetros
            String[] parts = cleanUrl.split("\\?");
            String hostPortDb = parts[0];
            
            // Separar database
            String[] hostPortAndDb = hostPortDb.split("/");
            result[2] = hostPortAndDb[1]; // database
            
            // Separar host y port
            String[] hostPort = hostPortAndDb[0].split(":");
            result[0] = hostPort[0]; // host
            result[1] = hostPort.length > 1 ? hostPort[1] : "3306"; // port (default 3306)
            
        } catch (Exception e) {
            logger.error("Error al parsear URL de base de datos", e);
            // Valores por defecto
            result[0] = "localhost";
            result[1] = "3306";
            result[2] = "db_foodix";
        }
        
        return result;
    }
}
