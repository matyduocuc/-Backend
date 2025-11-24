# Configuración de Bases de Datos en XAMPP/MySQL

## ⚠️ IMPORTANTE

**Todos los microservicios están configurados con `ddl-auto=none`**

Esto significa que **NO se crearán las tablas automáticamente**. Debes crearlas manualmente en XAMPP/MySQL.

---

## 📋 Bases de Datos Necesarias

Crea estas 5 bases de datos en XAMPP:

1. **`biblioteca_catalogo`** - Para el microservicio de Libros (puerto 8082)
2. **`biblioteca_usuarios`** - Para el microservicio de Usuarios (puerto 8081)
3. **`biblioteca_prestamos`** - Para el microservicio de Préstamos (puerto 8083)
4. **`biblioteca_notificaciones`** - Para el microservicio de Notificaciones (puerto 8084)
5. **`biblioteca_usuarios`** y **`biblioteca_prestamos`** - Para el microservicio de Informes (puerto 8085) - usa las mismas bases de datos

---

## 🗄️ Cómo Crear las Bases de Datos en XAMPP

### Paso 1: Abrir phpMyAdmin

1. Inicia XAMPP
2. Inicia el servicio **MySQL**
3. Abre tu navegador y ve a: `http://localhost/phpmyadmin`
4. Usuario: `root` (sin contraseña por defecto)

### Paso 2: Crear las Bases de Datos

Ejecuta estos comandos SQL en phpMyAdmin (pestaña "SQL"):

```sql
-- 1. Base de datos para Libros
CREATE DATABASE IF NOT EXISTS biblioteca_catalogo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. Base de datos para Usuarios
CREATE DATABASE IF NOT EXISTS biblioteca_usuarios CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. Base de datos para Préstamos
CREATE DATABASE IF NOT EXISTS biblioteca_prestamos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 4. Base de datos para Notificaciones
CREATE DATABASE IF NOT EXISTS biblioteca_notificaciones CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## 📊 Crear las Tablas

### Opción 1: Dejar que Hibernate las cree (TEMPORAL)

Si quieres que se creen automáticamente **solo la primera vez**, puedes cambiar temporalmente:

```properties
spring.jpa.hibernate.ddl-auto=update
```

**Después de crear las tablas, vuelve a cambiar a:**
```properties
spring.jpa.hibernate.ddl-auto=none
```

### Opción 2: Crear las Tablas Manualmente (RECOMENDADO)

Crea las tablas usando los scripts SQL que Hibernate genera. Puedes:

1. **Temporalmente cambiar a `ddl-auto=update`**
2. **Iniciar el microservicio** (se crearán las tablas)
3. **Exportar las tablas desde phpMyAdmin** (Exportar → SQL)
4. **Cambiar de vuelta a `ddl-auto=none`**
5. **Eliminar las tablas y recrearlas usando el script exportado**

---

## 🔧 Configuración Actual de Cada Microservicio

### ✅ Libros (Puerto 8082)
- **Base de datos:** `biblioteca_catalogo`
- **ddl-auto:** `none` ✅ (NO crea tablas automáticamente)

### ✅ Usuarios (Puerto 8081)
- **Base de datos:** `biblioteca_usuarios`
- **ddl-auto:** `none` ✅ (NO crea tablas automáticamente)

### ✅ Préstamos (Puerto 8083)
- **Base de datos:** `biblioteca_prestamos`
- **ddl-auto:** `none` ✅ (NO crea tablas automáticamente)

### ✅ Informes (Puerto 8085)
- **Bases de datos:** `biblioteca_usuarios` y `biblioteca_prestamos` (solo lectura)
- **ddl-auto:** `none` ✅ (NO crea tablas automáticamente)

### ✅ Notificaciones (Puerto 8084)
- **Base de datos:** `biblioteca_notificaciones`
- **ddl-auto:** `none` ✅ (NO crea tablas automáticamente)

---

## 🚀 Pasos Recomendados

### Para Desarrollo (Crear Tablas Automáticamente)

1. **Cambia temporalmente** en cada `application.properties`:
   ```properties
   spring.jpa.hibernate.ddl-auto=update
   ```

2. **Inicia cada microservicio** (se crearán las tablas)

3. **Exporta las tablas** desde phpMyAdmin

4. **Vuelve a cambiar** a:
   ```properties
   spring.jpa.hibernate.ddl-auto=none
   ```

### Para Producción (Tablas Manuales)

1. **Crea las bases de datos** usando los comandos SQL de arriba

2. **Importa los scripts SQL** de las tablas en cada base de datos

3. **Mantén `ddl-auto=none`** en todos los microservicios

---

## ⚠️ Notas Importantes

- **XAMPP por defecto** no tiene contraseña para `root`
- Si cambias la contraseña, actualiza `spring.datasource.password` en cada `application.properties`
- El puerto de MySQL en XAMPP es **3306** (por defecto)
- Asegúrate de que el servicio **MySQL** esté iniciado en XAMPP

---

## 🔍 Verificar que las Tablas Existen

En phpMyAdmin:

1. Selecciona la base de datos (ej: `biblioteca_catalogo`)
2. Ve a la pestaña "Estructura"
3. Deberías ver las tablas creadas

Si no ves tablas, significa que:
- Las bases de datos están vacías
- Necesitas crear las tablas manualmente o cambiar temporalmente a `ddl-auto=update`

---

## 📝 Resumen

✅ **Configuración actual:** `ddl-auto=none` en todos los microservicios  
✅ **No se crearán tablas automáticamente**  
✅ **Debes crear las bases de datos y tablas manualmente**  
✅ **Informes ya estaba configurado correctamente**

