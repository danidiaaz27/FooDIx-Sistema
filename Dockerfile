# 1. Usamos la imagen base de Java 21 que necesitas
FROM eclipse-temurin:21-jdk

# 2. Argumento para el .jar que se creará en la carpeta 'target'
ARG JAR_FILE=target/*.jar

# 3. Copiamos ese .jar dentro de la imagen Docker
COPY ${JAR_FILE} app.jar

# 4. Exponemos el puerto 8080 (que usa tu server.port)
EXPOSE 8080

# 5. El comando para arrancar tu aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]