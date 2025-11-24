package com.example.LibrosCatalogo.controller;

import com.example.LibrosCatalogo.dto.ApiResponse;
import com.example.LibrosCatalogo.dto.BusquedaRequest;
import com.example.LibrosCatalogo.dto.LibroCreateRequest;
import com.example.LibrosCatalogo.dto.LibroDTO;
import com.example.LibrosCatalogo.dto.MensajeResponse;
import com.example.LibrosCatalogo.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "API para gestión del catálogo de libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @Operation(summary = "Crear un nuevo libro", description = "Registra un nuevo libro en el catálogo")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
                    content = @Content(schema = @Schema(implementation = LibroDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El ISBN ya existe")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<LibroDTO>> crearLibro(
            @Parameter(description = "Datos del libro a crear", required = true)
            @RequestBody LibroCreateRequest request) {
        try {
            LibroDTO libroCreado = libroService.crearLibro(request);
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.CREATED.value(),
                    "Libro creado exitosamente",
                    libroCreado,
                    1L
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al crear libro: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Actualizar un libro", description = "Actualiza la información de un libro existente")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "El nuevo ISBN ya existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> actualizarLibro(
            @Parameter(description = "ID del libro a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del libro", required = true)
            @RequestBody LibroCreateRequest request) {
        try {
            LibroDTO libroActualizado = libroService.actualizarLibro(id, request);
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Libro actualizado exitosamente",
                    libroActualizado,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al actualizar libro: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Eliminar un libro", description = "Elimina un libro del catálogo (eliminación lógica)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Libro eliminado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarLibro(
            @Parameter(description = "ID del libro a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        try {
            libroService.eliminarLibro(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Libro eliminado exitosamente",
                    null,
                    0L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Libro no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al eliminar libro: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Obtener libro por ID", description = "Recupera la información de un libro específico")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> obtenerLibroPorId(
            @Parameter(description = "ID del libro a buscar", required = true, example = "1")
            @PathVariable Long id) {
        try {
            LibroDTO libro = libroService.obtenerLibroPorId(id);
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Libro encontrado",
                    libro,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Libro no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener libro: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Listar todos los libros", description = "Obtiene una lista de todos los libros activos en el catálogo")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente")
    @GetMapping
    public ResponseEntity<ApiResponse<List<LibroDTO>>> obtenerTodosLosLibros() {
        List<LibroDTO> libros = libroService.obtenerTodosLosLibros();

        if (libros.isEmpty()) {
            ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.NO_CONTENT.value(),
                    "No se encontraron libros activos",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Lista de libros obtenida exitosamente",
                libros,
                (long) libros.size()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Búsqueda avanzada de libros", description = "Busca libros aplicando diversos filtros")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resultados de búsqueda obtenidos exitosamente")
    @PostMapping("/buscar")
    public ResponseEntity<ApiResponse<List<LibroDTO>>> buscarLibros(
            @Parameter(description = "Criterios de búsqueda", required = true)
            @RequestBody BusquedaRequest request) {
        List<LibroDTO> libros = libroService.buscarLibros(request);
        ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Resultados de búsqueda obtenidos exitosamente",
                libros,
                (long) libros.size()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener libros disponibles", description = "Lista todos los libros que tienen ejemplares disponibles")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de libros disponibles obtenida exitosamente")
    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse<List<LibroDTO>>> obtenerLibrosDisponibles() {
        List<LibroDTO> libros = libroService.obtenerLibrosDisponibles();

        if (libros.isEmpty()) {
            ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.NO_CONTENT.value(),
                    "No se encontraron libros disponibles",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Lista de libros disponibles obtenida exitosamente",
                libros,
                (long) libros.size()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener libros por categoría", description = "Filtra libros por nombre de categoría")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de libros por categoría"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse<List<LibroDTO>>> obtenerLibrosPorCategoria(
            @Parameter(description = "Nombre de la categoría", required = true, example = "Ficción")
            @PathVariable String categoria) {
        List<LibroDTO> libros = libroService.obtenerLibrosPorCategoria(categoria);

        if (libros.isEmpty()) {
            ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.NO_CONTENT.value(),
                    "No se encontraron libros para la categoría especificada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Lista de libros por categoría obtenida exitosamente",
                libros,
                (long) libros.size()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener libros por autor", description = "Filtra libros por nombre del autor")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de libros del autor")
    @GetMapping("/autor/{autor}")
    public ResponseEntity<ApiResponse<List<LibroDTO>>> obtenerLibrosPorAutor(
            @Parameter(description = "Nombre del autor", required = true, example = "Gabriel García Márquez")
            @PathVariable String autor) {
        List<LibroDTO> libros = libroService.obtenerLibrosPorAutor(autor);

        if (libros.isEmpty()) {
            ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.NO_CONTENT.value(),
                    "No se encontraron libros para el autor especificado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        ApiResponse<List<LibroDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Lista de libros por autor obtenida exitosamente",
                libros,
                (long) libros.size()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar stock", description = "Actualiza la cantidad total de ejemplares de un libro")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<LibroDTO>> actualizarStock(
            @Parameter(description = "ID del libro", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nueva cantidad total", required = true, example = "15")
            @RequestParam Integer cantidad) {
        try {
            LibroDTO libroActualizado = libroService.actualizarStock(id, cantidad);
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Stock actualizado exitosamente",
                    libroActualizado,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Libro no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<LibroDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al actualizar stock: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Verificar ISBN", description = "Verifica si un ISBN ya está registrado en el sistema")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    @GetMapping("/verificar-isbn/{isbn}")
    public ResponseEntity<ApiResponse<MensajeResponse>> verificarIsbn(
            @Parameter(description = "ISBN a verificar", required = true, example = "978-8437604947")
            @PathVariable String isbn) {
        boolean existe = libroService.existeLibroPorIsbn(isbn);
        String mensaje = existe ? "El ISBN ya está registrado" : "El ISBN está disponible";
        MensajeResponse resultado = new MensajeResponse(mensaje, existe);

        ApiResponse<MensajeResponse> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Resultado de la verificación de ISBN",
                resultado,
                1L
        );
        return ResponseEntity.ok(response);
    }
}
