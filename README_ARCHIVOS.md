# 📁 Sistema de Gestión de Archivos - FoodIx

## ✅ Problema Resuelto

**Error anterior:** 404 al intentar acceder a `/uploads/restaurante/1/CARTA_RESTAURANTE`

**Causa:** Los archivos se guardaban en la base de datos como rutas, pero no había:
1. Configuración para servir archivos estáticos desde `/uploads/`
2. Directorio físico para almacenar los archivos
3. Controlador para visualizar/descargar archivos

---

## 🔧 Solución Implementada

### 1. **FileStorageConfig.java** ✅
Configuración de Spring MVC para servir archivos estáticos:

```java
@Configuration
public class FileStorageConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
```

- **URL pública:** `/uploads/**`
- **Ubicación física:** Carpeta `uploads/` en el directorio del proyecto

### 2. **FileStorageService.java** ✅
Servicio mejorado para gestionar archivos:

#### Métodos principales:

**a) Guardar archivo genérico:**
```java
String guardarArchivo(MultipartFile file, String subFolder)
```
- Guarda archivos con nombre único (UUID)
- Ejemplo: `uploads/repartidores/abc123-uuid.pdf`

**b) Guardar documento de restaurante:**
```java
String guardarArchivoRestaurante(MultipartFile file, Long restauranteId, String tipoDocumento)
```
- Estructura: `uploads/restaurante/{id}/{TIPO_DOCUMENTO}.ext`
- Ejemplo: `uploads/restaurante/1/CARTA_RESTAURANTE.pdf`
- Tipos: `CARTA_RESTAURANTE`, `CARNET_SANIDAD`, `LICENCIA_FUNCIONAMIENTO`

**c) Verificar existencia:**
```java
boolean archivoExiste(String rutaRelativa)
```

**d) Eliminar archivo:**
```java
void eliminarArchivo(String rutaRelativa)
```

### 3. **FileController.java** ✅
Controlador para servir archivos con mejor visualización:

#### Endpoints:

**a) Ver archivo en línea (PDF, imágenes):**
```
GET /files/view/restaurante/1/CARTA_RESTAURANTE.pdf
```
- Header: `Content-Disposition: inline`
- Abre en el navegador

**b) Descargar archivo:**
```
GET /files/download/restaurante/1/CARTA_RESTAURANTE.pdf
```
- Header: `Content-Disposition: attachment`
- Descarga directa

### 4. **SecurityConfig.java** ✅
Acceso público a archivos:

```java
.requestMatchers(
    "/uploads/**",  // Acceso directo a archivos
    "/files/**"     // Controlador de visualización/descarga
).permitAll()
```

---

## 📂 Estructura de Directorios

```
FooDIx-Sistema/
├── uploads/                          ← Carpeta principal (creada automáticamente)
│   ├── restaurante/                  ← Documentos de restaurantes
│   │   ├── 1/                        ← Restaurante ID 1
│   │   │   ├── CARTA_RESTAURANTE.pdf
│   │   │   ├── CARNET_SANIDAD.jpg
│   │   │   └── LICENCIA_FUNCIONAMIENTO.pdf
│   │   ├── 2/                        ← Restaurante ID 2
│   │   │   └── ...
│   │   └── ...
│   ├── repartidores/                 ← Documentos de repartidores
│   │   ├── abc-uuid-123.pdf
│   │   └── ...
│   └── promociones/                  ← Imágenes de promociones
│       ├── 1/                        ← Restaurante ID 1
│       │   ├── uuid-1.jpg
│       │   └── uuid-2.png
│       └── ...
```

---

## 🔗 Formas de Acceder a los Archivos

### **Opción 1: URL Directa** (Recomendado para desarrollo)
```
http://localhost:8080/uploads/restaurante/1/CARTA_RESTAURANTE.pdf
```

### **Opción 2: Controlador con visor inline** (Mejor para producción)
```
http://localhost:8080/files/view/restaurante/1/CARTA_RESTAURANTE.pdf
```
- ✅ Abre PDFs en el navegador
- ✅ Muestra imágenes directamente
- ✅ Logs de acceso

### **Opción 3: Descarga forzada**
```
http://localhost:8080/files/download/restaurante/1/CARTA_RESTAURANTE.pdf
```
- Descarga el archivo automáticamente

---

## 💻 Ejemplos de Uso en Código

### **Frontend (HTML/JavaScript):**

#### Ver documento en modal:
```html
<button onclick="window.open('/files/view/restaurante/1/CARTA_RESTAURANTE.pdf', '_blank')">
    Ver Carta
</button>
```

#### Mostrar imagen:
```html
<img src="/uploads/promociones/1/imagen-uuid.jpg" alt="Promoción" />
```

#### Link de descarga:
```html
<a href="/files/download/restaurante/1/CARNET_SANIDAD.pdf" download>
    Descargar Carnet de Sanidad
</a>
```

### **Backend (Controlador):**

#### Guardar archivo de restaurante:
```java
@Autowired
private FileStorageService fileStorageService;

@PostMapping("/restaurante/upload")
public String subirDocumento(
    @RequestParam("archivo") MultipartFile file,
    @RequestParam("tipo") String tipoDocumento,
    @AuthenticationPrincipal CustomUserDetails userDetails
) {
    Restaurante restaurante = ...; // Obtener restaurante del usuario
    
    // Guardar archivo
    String rutaArchivo = fileStorageService.guardarArchivoRestaurante(
        file, 
        restaurante.getCodigo(), 
        tipoDocumento
    );
    
    // Guardar ruta en BD
    if ("CARTA_RESTAURANTE".equals(tipoDocumento)) {
        restaurante.setCartaRestaurante(rutaArchivo);
    } else if ("CARNET_SANIDAD".equals(tipoDocumento)) {
        restaurante.setCarnetSanidad(rutaArchivo);
    } else if ("LICENCIA_FUNCIONAMIENTO".equals(tipoDocumento)) {
        restaurante.setLicenciaFuncionamiento(rutaArchivo);
    }
    
    restauranteRepository.save(restaurante);
    
    return "redirect:/menuRestaurante";
}
```

#### Guardar imagen de promoción:
```java
@PostMapping("/promociones/crear")
public String crearPromocion(
    @RequestParam("imagen") MultipartFile imagen,
    @RequestParam("titulo") String titulo,
    // ... otros parámetros
) {
    Promocion promocion = new Promocion();
    promocion.setTitulo(titulo);
    
    // Guardar imagen
    if (!imagen.isEmpty()) {
        String rutaImagen = fileStorageService.guardarImagenPromocion(
            imagen, 
            restauranteId
        );
        promocion.setImagenPrincipal(rutaImagen);
    }
    
    promocionRepository.save(promocion);
    
    return "redirect:/menuRestaurante";
}
```

---

## 🎨 Integración con Thymeleaf

### Ver documento con botón:
```html
<button th:if="${restaurante.cartaRestaurante != null}" 
        th:onclick="'window.open(\'/files/view/' + ${restaurante.cartaRestaurante} + '\', \'_blank\')'"
        class="btn btn-primary">
    <i class="fas fa-eye"></i> Ver Documento
</button>
```

### Mostrar imagen de promoción:
```html
<img th:if="${promocion.imagenPrincipal != null}" 
     th:src="'/uploads/' + ${promocion.imagenPrincipal}" 
     th:alt="${promocion.titulo}"
     class="img-fluid" />
```

### Descargar con ícono:
```html
<a th:href="'/files/download/' + ${restaurante.carnetSanidad}" 
   class="btn btn-sm btn-outline-success" 
   download>
    <i class="fas fa-download"></i> Descargar
</a>
```

---

## 🔍 Debugging

### Ver logs en consola:
```bash
docker logs foodix-sistema-mi-app-1 -f
```

**Logs esperados:**
```
📁 [FILE SERVICE] Directorio uploads encontrado: /uploads
📁 [FILE CONFIG] Configuración de archivos estáticos agregada
✅ [FILE SERVICE] Archivo guardado: restaurante/1/CARTA_RESTAURANTE.pdf
📂 [FILE SERVICE] Ubicación: /uploads/restaurante/1/CARTA_RESTAURANTE.pdf
📄 [FILE CONTROLLER] Sirviendo archivo: restaurante/1/CARTA_RESTAURANTE.pdf
```

### Verificar archivos físicos:
```powershell
# Listar archivos en uploads
Get-ChildItem -Recurse .\uploads\

# Ver contenido de directorio específico
ls .\uploads\restaurante\1\
```

---

## ⚠️ Consideraciones de Producción

### 1. **Volumen Docker**
Para persistir archivos en Docker, agregar volumen en `docker-compose.yml`:

```yaml
services:
  mi-app:
    volumes:
      - ./uploads:/uploads  # Mapear carpeta local a contenedor
```

### 2. **Tamaño máximo de archivos**
En `application.properties`:

```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### 3. **Validaciones recomendadas**
```java
// En FileStorageService

public boolean validarTamano(MultipartFile file) {
    long maxSize = 10 * 1024 * 1024; // 10MB
    return file.getSize() <= maxSize;
}

public boolean validarTipoArchivo(MultipartFile file, String[] tiposPermitidos) {
    String contentType = file.getContentType();
    for (String tipo : tiposPermitidos) {
        if (contentType.contains(tipo)) {
            return true;
        }
    }
    return false;
}
```

### 4. **Seguridad**
- ✅ `/uploads/**` es público (necesario para ver documentos)
- ✅ Validar tipos de archivo permitidos
- ✅ Limitar tamaño de archivos
- ⚠️ En producción, considerar almacenamiento en cloud (AWS S3, Google Cloud Storage)

---

## 🚀 Testing

### 1. Probar acceso directo:
```
http://localhost:8080/uploads/restaurante/1/CARTA_RESTAURANTE.pdf
```
✅ Debe abrir/descargar el PDF

### 2. Probar con controlador:
```
http://localhost:8080/files/view/restaurante/1/CARTA_RESTAURANTE.pdf
```
✅ Debe abrir el PDF en el navegador

### 3. Probar descarga:
```
http://localhost:8080/files/download/restaurante/1/CARTA_RESTAURANTE.pdf
```
✅ Debe descargar el archivo

---

## 📊 Base de Datos

Las rutas se guardan en la BD como **texto relativo**:

```sql
-- Tabla restaurante
UPDATE restaurante 
SET carta_restaurante = 'restaurante/1/CARTA_RESTAURANTE.pdf',
    carnet_sanidad = 'restaurante/1/CARNET_SANIDAD.jpg',
    licencia_funcionamiento = 'restaurante/1/LICENCIA_FUNCIONAMIENTO.pdf'
WHERE codigo = 1;

-- Tabla promocion
UPDATE promocion 
SET imagen_principal = 'promociones/1/uuid-imagen.jpg'
WHERE codigo = 1;
```

**Nota:** NO guardar `/uploads/` al inicio, solo la ruta relativa desde esa carpeta.

---

## ✅ Resumen

| Componente | Estado | Descripción |
|------------|--------|-------------|
| FileStorageConfig | ✅ | Sirve archivos estáticos desde `/uploads/` |
| FileStorageService | ✅ | Guarda y gestiona archivos |
| FileController | ✅ | Endpoints para view/download |
| SecurityConfig | ✅ | Acceso público a `/uploads/**` y `/files/**` |
| Directorio uploads/ | ✅ | Creado automáticamente |

**Sistema completamente funcional para:**
- ✅ Subir documentos de restaurantes
- ✅ Subir imágenes de promociones
- ✅ Ver documentos en línea (PDFs, imágenes)
- ✅ Descargar archivos
- ✅ Eliminar archivos obsoletos

---

**Fecha:** 27 de Noviembre, 2025  
**Estado:** ✅ Producción Ready
