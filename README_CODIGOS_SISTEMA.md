# 📋 CÓDIGOS DEL SISTEMA FOODIX - REFERENCIA RÁPIDA

## ⚠️ IMPORTANTE: CÓDIGOS ESTANDARIZADOS

Este documento contiene los códigos oficiales que **SIEMPRE** debes usar en el sistema.

---

## 🔐 CÓDIGOS DE ROLES (tabla: `rol`)

| Código | Nombre          | Descripción                        | Constante Java       |
|--------|-----------------|-----------------------------------|---------------------|
| **1**  | ADMINISTRADOR   | Administrador del sistema FooDix  | `Rol.ADMINISTRADOR` |
| **2**  | RESTAURANTE     | Propietario de restaurante        | `Rol.RESTAURANTE`   |
| **3**  | REPARTIDOR      | Repartidor de pedidos             | `Rol.REPARTIDOR`    |
| **4**  | USUARIO         | Usuario cliente del sistema       | `Rol.USUARIO`       |

### 📍 Uso en código Java:
```java
// Definido en: src/main/java/com/example/SistemaDePromociones/model/Rol.java
public static final Long ADMINISTRADOR = 1L;
public static final Long RESTAURANTE = 2L;
public static final Long REPARTIDOR = 3L;
public static final Long USUARIO = 4L;
```

### ✅ Ejemplo de uso correcto:
```java
usuario.setCodigoRol(Rol.RESTAURANTE); // Correcto: usa 2L
usuario.setCodigoRol(2L);              // Correcto: usa 2L directamente
```

### ❌ Errores comunes:
```java
usuario.setCodigoRol(1L); // ❌ ERROR: esto es ADMINISTRADOR, no RESTAURANTE
usuario.setCodigoRol(5L); // ❌ ERROR: este rol no existe
```

---

## ✅ CÓDIGOS DE ESTADO DE APROBACIÓN (tabla: `estado_aprobacion`)

| Código | Nombre    | Descripción               | Color      | Uso                           |
|--------|-----------|---------------------------|-----------|-------------------------------|
| **7**  | Pendiente | Solicitud en revisión     | `#ffc107` | Restaurantes y repartidores nuevos |
| **8**  | Aprobado  | Solicitud aprobada        | `#28a745` | Después de aprobación admin   |
| **9**  | Rechazado | Solicitud rechazada       | `#dc3545` | Cuando admin rechaza          |

### 📍 Uso en código Java:
```java
// Archivo: RestauranteJdbcRepository.java (línea 48)
"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 7, 1, NOW())";  // 7 = Pendiente

// Archivo: Repartidor.java (línea 34)
private Long codigoEstadoAprobacion = 7L; // 7 = Pendiente

// Archivo: AdminController.java (líneas 318, 371, 736, 797)
restaurante.setCodigoEstadoAprobacion(8L); // Aprobar
restaurante.setCodigoEstadoAprobacion(9L); // Rechazar
```

### ✅ Ejemplo de uso correcto:
```java
// Al crear un nuevo restaurante o repartidor:
entity.setCodigoEstadoAprobacion(7L); // Pendiente

// Al aprobar:
entity.setCodigoEstadoAprobacion(8L); // Aprobado

// Al rechazar:
entity.setCodigoEstadoAprobacion(9L); // Rechazado
```

### ❌ Errores comunes:
```java
// ❌ ERROR: Usar 1, 2, 3 en lugar de 7, 8, 9
repartidor.setCodigoEstadoAprobacion(1L); // ❌ INCORRECTO
restaurante.setCodigoEstadoAprobacion(2L); // ❌ INCORRECTO

// ✅ CORRECTO: Usar 7, 8, 9
repartidor.setCodigoEstadoAprobacion(7L);  // ✅ CORRECTO
restaurante.setCodigoEstadoAprobacion(8L); // ✅ CORRECTO
```

---

## 🚦 CÓDIGOS DE ESTADO DE PEDIDO (tabla: `estado_pedido`)

| Código | Nombre      | Descripción                              | Color      |
|--------|-------------|------------------------------------------|-----------|
| 1      | Pendiente   | Pedido creado, esperando confirmación    | `#ffc107` |
| 2      | Confirmado  | Pedido confirmado por el restaurante     | `#17a2b8` |
| 3      | Preparando  | El restaurante está preparando el pedido | `#fd7e14` |
| 4      | Listo       | Pedido listo para recoger/entregar       | `#6f42c1` |
| 5      | En camino   | Repartidor en camino a entregar          | `#007bff` |
| 6      | Entregado   | Pedido entregado exitosamente            | `#28a745` |
| 7      | Cancelado   | Pedido cancelado                         | `#dc3545` |
| 8      | Rechazado   | Pedido rechazado por el restaurante      | `#6c757d` |

---

## 🚗 CÓDIGOS DE TIPO DE VEHÍCULO (tabla: `tipo_vehiculo`)

| Código | Nombre             |
|--------|--------------------|
| 9      | Bicicleta          |
| 10     | Motocicleta        |
| 11     | Scooter Eléctrico  |
| 12     | Automóvil          |

---

## 💳 CÓDIGOS DE MÉTODO DE PAGO (tabla: `metodo_pago`)

| Código | Nombre        | Requiere Cambio |
|--------|---------------|----------------|
| 1      | Efectivo      | ✅ Sí          |
| 2      | Yape          | ❌ No          |
| 3      | Plin          | ❌ No          |
| 4      | Tarjeta       | ❌ No          |
| 5      | Transferencia | ❌ No          |

---

## 📄 TIPOS DE DOCUMENTOS

### Para Restaurantes (ENUM en `documento_restaurante`)
- `CARTA_RESTAURANTE` - Menú del restaurante
- `CarnetSanidad` - Certificado de sanidad
- `LicenciaFuncionamiento` - Licencia municipal
- `RUC` - Registro Único de Contribuyentes
- `Otros` - Otros documentos

### Para Repartidores (ENUM en `documento_repartidor`)
- `Licencia` - Licencia de conducir
- `SOAT` - Seguro Obligatorio de Accidentes de Tránsito
- `TarjetaPropiedad` - Tarjeta de propiedad del vehículo
- `AntecedentesPolicial` - Certificado de antecedentes policiales

---

## 🐛 DEBUGGING: ¿Cómo saber si hay un error de código?

### Síntomas comunes:
1. ✅ El registro parece exitoso PERO no aparece en el listado
2. 🔍 Los logs dicen "guardado exitosamente"
3. ❌ En la base de datos aparece con `codigo_estado_aprobacion = NULL` o un número incorrecto
4. 🚫 Al intentar aprobar/rechazar, no encuentra el registro

### Solución:
1. Verifica en MySQL:
   ```sql
   SELECT codigo, nombre, codigo_estado_aprobacion FROM restaurante;
   SELECT codigo, numero_licencia, codigo_estado_aprobacion FROM repartidor;
   ```

2. Compara con esta tabla:
   - Si ves `1, 2, 3` → ❌ ERROR: Debes usar `7, 8, 9`
   - Si ves `7, 8, 9` → ✅ CORRECTO

3. Si hay error, busca en el código:
   ```bash
   grep -rn "setCodigoEstadoAprobacion(1" src/
   grep -rn "setCodigoEstadoAprobacion(2" src/
   grep -rn "setCodigoEstadoAprobacion(3" src/
   ```

---

## 📦 ARCHIVOS MODIFICADOS EN ESTA CORRECCIÓN

### ✅ Archivos corregidos:
1. **RepartidorService.java** (línea 54)
   - ❌ Antes: `setCodigoEstadoAprobacion(1L)`
   - ✅ Ahora: `setCodigoEstadoAprobacion(7L)`

2. **datos_iniciales.sql**
   - ✅ Agregados comentarios aclaratorios sobre códigos 7, 8, 9

### ✅ Archivos verificados (ya estaban correctos):
- ✅ `Repartidor.java` - Usa `7L` correctamente
- ✅ `RestauranteJdbcRepository.java` - Usa `7` correctamente
- ✅ `AdminController.java` - Usa `8L` y `9L` correctamente
- ✅ `Rol.java` - Constantes definidas correctamente (1, 2, 3, 4)

---

## 🎯 CHECKLIST DE VERIFICACIÓN ANTES DE HACER COMMIT

Antes de hacer commit o deploy, verifica:

- [ ] ✅ Todos los roles usan códigos 1, 2, 3, 4
- [ ] ✅ Todos los estados de aprobación usan códigos 7, 8, 9
- [ ] ✅ No hay código que use `setCodigoEstadoAprobacion(1L)` o `(2L)` o `(3L)`
- [ ] ✅ No hay INSERT INTO con `codigo_estado_aprobacion = 1` o `= 2` o `= 3`
- [ ] ✅ Los ENUMs de documentos coinciden entre Java y SQL

---

## 📞 ¿Necesitas agregar un nuevo estado o rol?

### Para agregar un nuevo rol:
1. Inserta en la BD: `INSERT INTO rol (nombre, descripcion, estado) VALUES ('NUEVO_ROL', 'Descripción', TRUE);`
2. Anota el código generado (ej: 5)
3. Agrega constante en `Rol.java`: `public static final Long NUEVO_ROL = 5L;`

### Para agregar un nuevo estado de aprobación:
1. Inserta en la BD: `INSERT INTO estado_aprobacion (codigo, nombre, descripcion, estado) VALUES (10, 'NuevoEstado', 'Desc', TRUE);`
2. Usa el código 10 en tu código Java

---

## 🔍 COMANDOS ÚTILES DE VERIFICACIÓN

```bash
# Ver todos los restaurantes con su estado
docker exec -it foodix-sistema-db-1 mysql -u root -proot db_foodix -e "
  SELECT r.codigo, r.nombre, r.codigo_estado_aprobacion, ea.nombre as estado 
  FROM restaurante r 
  LEFT JOIN estado_aprobacion ea ON r.codigo_estado_aprobacion = ea.codigo;"

# Ver todos los repartidores con su estado
docker exec -it foodix-sistema-db-1 mysql -u root -proot db_foodix -e "
  SELECT rep.codigo, u.nombre, rep.codigo_estado_aprobacion, ea.nombre as estado 
  FROM repartidor rep 
  JOIN usuario u ON rep.codigo_usuario = u.codigo 
  LEFT JOIN estado_aprobacion ea ON rep.codigo_estado_aprobacion = ea.codigo;"

# Ver usuarios con sus roles
docker exec -it foodix-sistema-db-1 mysql -u root -proot db_foodix -e "
  SELECT u.codigo, u.nombre, u.correo_electronico, u.codigo_rol, r.nombre as rol 
  FROM usuario u 
  LEFT JOIN rol r ON u.codigo_rol = r.codigo;"
```

---

**Última actualización:** 2025-11-27  
**Versión:** 1.0  
**Estado:** ✅ Verificado y corregido
