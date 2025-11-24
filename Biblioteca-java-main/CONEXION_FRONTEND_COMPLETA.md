# Conexión Completa: Microservicios Biblioteca con Frontend React

## ✅ Configuración Completada

Se ha implementado la conexión completa entre todos los microservicios de Biblioteca y el frontend React (`library-up`).

### Microservicios Conectados

1. ✅ **Libros** (puerto 8082) - `/api/libros`
2. ✅ **Usuarios** (puerto 8081) - `/api/usuarios` y `/api/auth`
3. ✅ **Préstamos** (puerto 8083) - `/api/v1/prestamos`
4. ✅ **Informes** (puerto 8085) - `/api/informes`

### Archivos Creados/Modificados

#### Backend (Microservicios)
- ✅ `LibrosCatalogo/src/main/java/com/example/LibrosCatalogo/config/CorsConfig.java`
- ✅ `Gestión de prestamos/src/main/java/com/example/Gestion/de/prestamos/config/CorsConfig.java`
- ✅ `Gestión de Usuarios/src/main/java/com/example/Gestion/de/Usuarios/config/CorsConfig.java`
- ✅ `GestionDeInformes/GestionDeInformes/src/main/java/com/example/GestionDeInformes/config/CorsConfig.java`

#### Frontend (React)
- ✅ `src/api/httpClient.ts` - Actualizado para manejar formato `ok` y `success`
- ✅ `src/api/booksApi.ts` - Actualizado para usar endpoints reales
- ✅ `src/api/usersApi.ts` - Actualizado para usar endpoints reales
- ✅ `src/api/loansApi.ts` - Actualizado para usar endpoints reales
- ✅ `src/api/reportsApi.ts` - Nuevo, para microservicio de Informes
- ✅ `.env.example` - Actualizado con todas las URLs

## 🔧 Configuración de Puertos

**IMPORTANTE**: Hay un conflicto de puertos detectado:
- Libros y Préstamos ambos están configurados en puerto 8082

**Solución**: Cambiar el puerto de Préstamos a 8083 en `Gestión de prestamos/src/main/resources/application.properties`:

```properties
server.port=8083
```

O cambiar Libros a otro puerto si prefieres mantener Préstamos en 8082.

## 📋 Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto React (`library-up/.env`) con:

```env
# Microservicio de Libros
VITE_BOOKS_API_URL=http://localhost:8082/api/libros

# Microservicio de Usuarios
VITE_USERS_API_URL=http://localhost:8081/api/usuarios

# Microservicio de Préstamos (verificar puerto)
VITE_LOANS_API_URL=http://localhost:8083/api/v1/prestamos

# Microservicio de Informes
VITE_REPORTS_API_URL=http://localhost:8085/api/informes

# Microservicios de Cursos y Estudiantes (opcional)
VITE_API_BASE_URL=http://localhost:8080
VITE_COURSES_API_URL=http://localhost:8080/api/v1/course
VITE_STUDENTS_API_URL=http://localhost:8080/api/v1/student
```

## 🚀 Orden de Inicio

1. **Base de datos MySQL**: Asegúrate de tener las bases de datos creadas:
   - `biblioteca_catalogo` (para Libros)
   - `biblioteca_usuarios` (para Usuarios)
   - `biblioteca_prestamos` (para Préstamos)

2. **Iniciar Microservicios** (en orden):
   ```bash
   # Terminal 1: Libros
   cd LibrosCatalogo
   mvn spring-boot:run

   # Terminal 2: Usuarios
   cd "Gestión de Usuarios"
   mvn spring-boot:run

   # Terminal 3: Préstamos
   cd "Gestión de prestamos"
   mvn spring-boot:run

   # Terminal 4: Informes
   cd GestionDeInformes/GestionDeInformes
   mvn spring-boot:run
   ```

3. **Iniciar Frontend**:
   ```bash
   cd library-up
   npm install  # Solo la primera vez
   npm run dev
   ```

## 📡 Endpoints Disponibles

### Libros (`/api/libros`)
- `GET /api/libros` - Listar todos
- `GET /api/libros/{id}` - Obtener por ID
- `POST /api/libros` - Crear libro
- `PUT /api/libros/{id}` - Actualizar libro
- `DELETE /api/libros/{id}` - Eliminar libro
- `POST /api/libros/buscar` - Búsqueda avanzada
- `GET /api/libros/disponibles` - Libros disponibles
- `GET /api/libros/categoria/{categoria}` - Por categoría
- `GET /api/libros/autor/{autor}` - Por autor
- `PATCH /api/libros/{id}/stock` - Actualizar stock
- `GET /api/libros/verificar-isbn/{isbn}` - Verificar ISBN

### Usuarios (`/api/usuarios` y `/api/auth`)
- `POST /api/auth/login` - Iniciar sesión
- `GET /api/usuarios` - Listar todos (admin)
- `GET /api/usuarios/{id}` - Obtener por ID
- `POST /api/usuarios` - Crear usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario (admin)
- `PATCH /api/usuarios/{id}/activar` - Activar usuario (admin)
- `PATCH /api/usuarios/{id}/desactivar` - Desactivar usuario (admin)

### Préstamos (`/api/v1/prestamos`)
- `GET /api/v1/prestamos/{id}` - Obtener por ID
- `GET /api/v1/prestamos/usuario/{usuarioId}` - Por usuario
- `GET /api/v1/prestamos/estado/{estado}` - Por estado
- `POST /api/v1/prestamos` - Crear préstamo
- `POST /api/v1/prestamos/{id}/renovar` - Renovar préstamo
- `POST /api/v1/prestamos/{id}/devolver` - Devolver préstamo

### Informes (`/api/informes`)
- `GET /api/informes/prestamos/resumen` - Resumen de préstamos
- `GET /api/informes/usuarios/{usuarioId}/resumen` - Resumen de usuario
- `GET /api/informes/multas/resumen` - Resumen de multas

## 💻 Uso en React

### Ejemplo: Obtener libros

```typescript
import { booksApi } from './api/booksApi';

const [libros, setLibros] = useState([]);

useEffect(() => {
  const loadLibros = async () => {
    try {
      const data = await booksApi.getAll();
      setLibros(data);
    } catch (error) {
      console.error('Error:', error);
    }
  };
  loadLibros();
}, []);
```

### Ejemplo: Login

```typescript
import { usersApi } from './api/usersApi';

const handleLogin = async (email: string, password: string) => {
  try {
    const authResponse = await usersApi.login({ email, password });
    // Guardar token en localStorage
    localStorage.setItem('session', JSON.stringify({
      token: authResponse.token,
      user: {
        id: authResponse.id,
        email: authResponse.email,
        nombre: authResponse.nombre,
        roles: authResponse.roles
      }
    }));
  } catch (error) {
    console.error('Error de login:', error);
  }
};
```

### Ejemplo: Crear préstamo

```typescript
import { loansApi } from './api/loansApi';

const crearPrestamo = async (usuarioId: number, ejemplarId: number) => {
  try {
    const prestamo = await loansApi.create({
      usuarioId,
      ejemplarId
    });
    console.log('Préstamo creado:', prestamo);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

### Ejemplo: Obtener resumen de informes

```typescript
import { reportsApi } from './api/reportsApi';

const loadResumen = async () => {
  try {
    const resumen = await reportsApi.getPrestamosResumen();
    console.log('Total préstamos:', resumen.totalPrestamos);
    console.log('Activos:', resumen.activos);
    console.log('En atraso:', resumen.atraso);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

## 🔍 Formato de Respuestas

Los microservicios de Biblioteca usan formato `ApiResponse` con campo `ok`:

```json
{
  "ok": true,
  "statusCode": 200,
  "message": "Operación exitosa",
  "data": { ... },
  "count": 1
}
```

El `httpClient` maneja automáticamente este formato y extrae el campo `data`.

## ⚠️ Notas Importantes

1. **Autenticación**: El endpoint de login (`/api/auth/login`) retorna `AuthResponse` directamente, no en formato `ApiResponse`.

2. **Puertos**: Verifica que no haya conflictos de puertos. Si Libros y Préstamos están en el mismo puerto, cambia uno de ellos.

3. **CORS**: Todos los microservicios tienen configuración CORS que permite conexiones desde `http://localhost:5173`.

4. **Token JWT**: Después del login, el token debe incluirse en el header `Authorization: Bearer {token}`. El `httpClient` lo hace automáticamente si existe en `localStorage.getItem('session')`.

5. **Variables de Entorno**: Después de crear o modificar `.env`, **reinicia el servidor de desarrollo** de React.

## ✅ Checklist

- [ ] Bases de datos MySQL creadas
- [ ] Puertos configurados correctamente (sin conflictos)
- [ ] Microservicios iniciados y corriendo
- [ ] Archivo `.env` creado en `library-up/`
- [ ] Frontend React corriendo
- [ ] CORS configurado en todos los microservicios
- [ ] APIs importadas en los componentes React

---

**Última actualización**: 2024-01-15
**Versión**: 1.0

