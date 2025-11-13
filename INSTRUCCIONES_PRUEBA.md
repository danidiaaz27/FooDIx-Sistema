# 🚀 INSTRUCCIONES PARA PROBAR LA APLICACIÓN

## ✅ LO QUE YA ESTÁ HECHO:

### **Backend:**
- ✅ 14 Entidades JPA (modelos)
- ✅ 8 Repositories (acceso a base de datos)
- ✅ 4 Controllers:
  - `HomeController` → /, /login, /registro, /recuperar-password
  - `RepartidorController` → /registro-repartidor
  - `RestauranteController` → /registro-restaurante
  - `UbicacionRestController` → /api/provincias/{id}, /api/distritos/{id}

### **Frontend:**
- ✅ HTML completo: index, login, registro, registro-Repartidor, registro-Restaurante
- ✅ CSS profesional con validaciones
- ✅ JavaScript con validación en tiempo real

### **Base de Datos:**
- ✅ Configuración de MySQL (db_foodix)
- ✅ Tablas creadas automáticamente por Hibernate
- ⚠️ **FALTA: INSERTAR DATOS INICIALES**

---

## 📋 PASO 1: INSERTAR DATOS EN LA BASE DE DATOS

### **Opción A: Usando MySQL Workbench (RECOMENDADO)**

1. Abre **MySQL Workbench**
2. Conecta a tu servidor MySQL (localhost, root/root)
3. Abre el archivo: `datos_iniciales.sql`
4. Ejecuta todo el script (botón ⚡ Execute)
5. Verifica que se insertaron los datos:
   ```sql
   SELECT COUNT(*) FROM departamento;  -- Debe mostrar 5
   SELECT COUNT(*) FROM provincia;     -- Debe mostrar 11
   SELECT COUNT(*) FROM distrito;      -- Debe mostrar 23
   SELECT COUNT(*) FROM categoria;     -- Debe mostrar 10
   SELECT COUNT(*) FROM tipo_vehiculo; -- Debe mostrar 4
   ```

### **Opción B: Usando línea de comandos**

```bash
mysql -u root -p db_foodix < datos_iniciales.sql
```

---

## 🚀 PASO 2: INICIAR LA APLICACIÓN

Ejecuta el siguiente comando:

```powershell
cd c:\Users\DANIELA\OneDrive\Documentos\SistemaDePromociones\SistemaDePromociones
.\mvnw.cmd spring-boot:run
```

Espera a ver el mensaje:
```
Started SistemaDePromocionesApplication in X.XXX seconds
```

---

## 🌐 PASO 3: PROBAR EN EL NAVEGADOR

### **Rutas disponibles:**

1. **Página Principal:**
   ```
   http://localhost:8080/
   ```

2. **Login:**
   ```
   http://localhost:8080/login
   ```

3. **Selección de tipo de registro:**
   ```
   http://localhost:8080/registro
   ```

4. **Registro de Repartidor:**
   ```
   http://localhost:8080/registro-repartidor
   ```
   - Debe cargar los departamentos en el select
   - Al seleccionar departamento, debe cargar provincias (API REST)
   - Al seleccionar provincia, debe cargar distritos (API REST)
   - Debe cargar tipos de vehículo

5. **Registro de Restaurante:**
   ```
   http://localhost:8080/registro-restaurante
   ```
   - Debe cargar departamentos en ambos selects (personal y negocio)
   - Debe cargar las 10 categorías como checkboxes
   - Al seleccionar departamento, debe cargar provincias
   - Al seleccionar provincia, debe cargar distritos

---

## 🔧 PASO 4: VERIFICAR QUE TODO FUNCIONA

### **Test 1: Navegación básica**
- ✅ Navega desde `/` a `/registro`
- ✅ Desde `/registro` a `/registro-repartidor`
- ✅ Desde `/registro` a `/registro-restaurante`

### **Test 2: Carga de datos dinámicos**
- ✅ En `/registro-repartidor`:
  - Verifica que el select "Departamento" tiene opciones (Lima, Arequipa, etc.)
  - Selecciona "Lima", debe cargar provincias en el siguiente select
  - Selecciona una provincia, debe cargar distritos
  - Verifica que "Tipo de Vehículo" tiene opciones (Bicicleta, Motocicleta, etc.)

- ✅ En `/registro-restaurante`:
  - Verifica que aparecen 10 categorías como checkboxes
  - Verifica que ambos selects de departamento tienen opciones
  - Prueba la cascada: Departamento → Provincia → Distrito

### **Test 3: Validaciones JavaScript**
- ✅ Intenta enviar formulario vacío → debe mostrar errores
- ✅ Escribe un correo inválido → debe validar formato
- ✅ Escribe contraseña corta → debe mostrar requisitos
- ✅ Sube un archivo mayor a 5MB → debe rechazar

---

## ⚠️ LO QUE TODAVÍA FALTA IMPLEMENTAR:

### **Para que funcione completamente necesitas:**

1. **Métodos POST en Controllers** para procesar los formularios:
   - `RepartidorController.registrarRepartidor()`
   - `RestauranteController.registrarRestaurante()`

2. **Services (lógica de negocio):**
   - `UsuarioService` → Crear usuarios, encriptar contraseñas (BCrypt)
   - `RepartidorService` → Guardar repartidor + documentos
   - `RestauranteService` → Guardar restaurante + documentos/imágenes
   - `FileStorageService` → Guardar archivos subidos

3. **DTOs (Data Transfer Objects):**
   - `RepartidorRegistroDTO`
   - `RestauranteRegistroDTO`

4. **Manejo de archivos multipart:**
   - Guardar documentos en carpeta `uploads/`
   - Validar tipos de archivo permitidos
   - Validar tamaños máximos

5. **Sistema de autenticación:**
   - Spring Security (opcional)
   - Login funcional
   - Sesiones de usuario

---

## 📊 VERIFICAR DATOS EN MYSQL

Después de insertar los datos iniciales, puedes verificar:

```sql
-- Ver departamentos y sus provincias
SELECT d.nombre AS Departamento, p.nombre AS Provincia
FROM departamento d
LEFT JOIN provincia p ON p.codigo_departamento = d.codigo
ORDER BY d.nombre, p.nombre;

-- Ver distritos de Lima
SELECT d.nombre AS Distrito
FROM distrito d
JOIN provincia p ON d.codigo_provincia = p.codigo
WHERE p.nombre = 'Lima'
ORDER BY d.nombre;

-- Ver categorías disponibles
SELECT * FROM categoria WHERE estado = TRUE;

-- Ver tipos de vehículo
SELECT * FROM tipo_vehiculo WHERE estado = TRUE;
```

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### **Error: "Whitelabel Error Page"**
- ✅ Verifica que la aplicación esté corriendo
- ✅ Verifica que los HTMLs estén en `src/main/resources/templates/`

### **Error: Selects vacíos (sin opciones)**
- ❌ No ejecutaste el script `datos_iniciales.sql`
- ❌ La base de datos no tiene departamentos/provincias/distritos

### **Error: API REST no responde**
- ✅ Abre la consola del navegador (F12)
- ✅ Ve a la pestaña "Network"
- ✅ Verifica que las llamadas a `/api/provincias/1` retornen JSON

### **Error: Hibernate warnings sobre FK incompatibles**
- ✅ Ya está solucionado (todas las claves son `Long` ahora)
- ✅ Si persiste, ejecuta: `DROP DATABASE db_foodix; CREATE DATABASE db_foodix;`

---

## 📝 PRÓXIMOS PASOS SUGERIDOS

1. **Insertar datos** → `datos_iniciales.sql`
2. **Iniciar app** → `mvnw.cmd spring-boot:run`
3. **Probar navegación** → http://localhost:8080
4. **Verificar carga de datos** → Selects deben tener opciones
5. **Implementar POST** → Para guardar registros en BD
6. **Agregar validaciones backend** → Duplicados, formatos, etc.
7. **Implementar login** → Spring Security + BCrypt

---

## ✅ CHECKLIST DE VERIFICACIÓN

- [ ] Script SQL ejecutado exitosamente
- [ ] Aplicación Spring Boot corriendo
- [ ] Página principal carga (http://localhost:8080/)
- [ ] Botones de navegación funcionan
- [ ] Select "Departamento" tiene opciones
- [ ] API REST carga provincias (al cambiar departamento)
- [ ] API REST carga distritos (al cambiar provincia)
- [ ] Categorías aparecen en registro-restaurante
- [ ] Validaciones JavaScript funcionan
- [ ] Archivos se pueden seleccionar (max 5MB)

---

**¡LISTO PARA PROBAR!** 🎉

**Primero ejecuta el SQL, luego inicia la app y navega a las URLs.**
