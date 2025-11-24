# Configuración de Puertos - Microservicios Biblioteca

## ✅ Puertos Configurados

| Microservicio | Puerto | URL Base | Estado | Frontend Espera |
|--------------|--------|----------|--------|----------------|
| **Libros** | 8082 | `http://localhost:8082/api/libros` | ✅ Correcto | 8082 ✅ |
| **Usuarios** | 8081 | `http://localhost:8081/api/usuarios` | ✅ Correcto | 8081 ✅ |
| **Préstamos** | 8083 | `http://localhost:8083/api/v1/prestamos` | ✅ **CORREGIDO** | 8083 ✅ |
| **Informes** | 8085 | `http://localhost:8085/api/informes` | ✅ Correcto | 8085 ✅ |
| **Notificaciones** | 8084 | `http://localhost:8084` | ✅ **CORREGIDO** | N/A (no usado por frontend) |

## 🔧 Cambios Realizados

### 1. Préstamos (Puerto 8083)
**Archivo**: `Gestión de prestamos/src/main/resources/application.properties`

**Cambio realizado**:
```properties
# Antes
server.port=8082

# Después
server.port=8083
```

**Razón**: El frontend (`httpClient.ts`) espera que el microservicio de Préstamos esté en el puerto 8083.

### 2. Notificaciones (Puerto 8084)
**Archivo**: `Notificaciones/Notificaciones/src/main/resources/application.properties`

**Cambio realizado**:
```properties
# Antes
server.port=8083

# Después
server.port=8084
```

**Razón**: Evitar conflicto con el microservicio de Préstamos que ahora usa el puerto 8083.

## 📋 Configuración del Frontend

El frontend está configurado en `src/api/httpClient.ts` con los siguientes puertos:

```typescript
const BOOKS_API_URL = 'http://localhost:8082/api/libros';
const USERS_API_URL = 'http://localhost:8081/api/usuarios';
const LOANS_API_URL = 'http://localhost:8083/api/v1/prestamos';
const REPORTS_API_URL = 'http://localhost:8085/api/informes';
```

**Todos los puertos están ahora correctamente alineados** ✅

## 🚀 Orden de Inicio Recomendado

1. **Base de datos MySQL**: Asegúrate de tener las bases de datos creadas:
   - `biblioteca_catalogo` (para Libros)
   - `biblioteca_usuarios` (para Usuarios)
   - `biblioteca_prestamos` (para Préstamos)
   - `biblioteca_notificaciones` (para Notificaciones)

2. **Iniciar Microservicios** (en orden):
   ```bash
   # Terminal 1: Libros (Puerto 8082)
   cd LibrosCatalogo
   mvn spring-boot:run

   # Terminal 2: Usuarios (Puerto 8081)
   cd "Gestión de Usuarios"
   mvn spring-boot:run

   # Terminal 3: Préstamos (Puerto 8083)
   cd "Gestión de prestamos"
   mvn spring-boot:run

   # Terminal 4: Informes (Puerto 8085)
   cd GestionDeInformes/GestionDeInformes
   mvn spring-boot:run

   # Terminal 5: Notificaciones (Puerto 8084) - Opcional
   cd Notificaciones/Notificaciones
   mvn spring-boot:run
   ```

3. **Iniciar Frontend**:
   ```bash
   cd library-up
   npm run dev
   ```

## ⚠️ Notas Importantes

1. **Recompilación**: Después de cambiar los puertos en `application.properties`, necesitas recompilar los microservicios:
   ```bash
   mvn clean package
   ```

2. **Puertos en conflicto**: Si algún puerto está en uso, verifica que no haya otro proceso usando ese puerto:
   ```bash
   # Windows
   netstat -ano | findstr :8083

   # Linux/Mac
   lsof -i :8083
   ```

3. **Variables de entorno**: El frontend puede sobrescribir estas URLs usando variables de entorno en `.env`:
   ```env
   VITE_BOOKS_API_URL=http://localhost:8082/api/libros
   VITE_USERS_API_URL=http://localhost:8081/api/usuarios
   VITE_LOANS_API_URL=http://localhost:8083/api/v1/prestamos
   VITE_REPORTS_API_URL=http://localhost:8085/api/informes
   ```

## ✅ Checklist de Verificación

- [x] Préstamos configurado en puerto 8083
- [x] Notificaciones configurado en puerto 8084 (sin conflictos)
- [x] Libros configurado en puerto 8082
- [x] Usuarios configurado en puerto 8081
- [x] Informes configurado en puerto 8085
- [x] Frontend alineado con los puertos del backend
- [ ] Microservicios recompilados (ejecutar `mvn clean package`)
- [ ] Microservicios iniciados y funcionando
- [ ] Frontend conectado correctamente a los microservicios

---

**Última actualización**: 2024-01-15
**Estado**: ✅ Puertos corregidos y alineados

