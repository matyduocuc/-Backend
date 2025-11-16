package com.example.LibrosCatalogo.controller;

import com.example.LibrosCatalogo.dto.*;
import com.example.LibrosCatalogo.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
                    content = @Content(schema = @Schema(implementation = LibroDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "El ISBN ya existe")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<LibroDTO>> crearLibro(
            @Parameter(description = "Datos del libro a crear", required = true)
            @RequestBody LibroCreateRequest request) {
        LibroDTO libroCreado = libroService.crearLibro(request);
        ApiResponse<LibroDTO> response = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Libro creado exitosamente",
                libroCreado,
                1L
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar un libro", description = "Actualiza la información de un libro existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "409", description = "El nuevo ISBN ya existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> actualizarLibro(
            @Parameter(description = "ID del libro a actualizar", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del libro", required = true)
            @RequestBody LibroCreateRequest request) {
        LibroDTO libroActualizado = libroService.actualizarLibro(id, request);
        ApiResponse<LibroDTO> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Libro actualizado exitosamente",
                libroActualizado,
                1L
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un libro", description = "Elimina un libro del catálogo (eliminación lógica)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarLibro(
            @Parameter(description = "ID del libro a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        libroService.eliminarLibro(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Libro eliminado exitosamente",
                null,
                0L
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener libro por ID", description = "Recupera la información de un libro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LibroDTO>> obtenerLibroPorId(
            @Parameter(description = "ID del libro a buscar", required = true, example = "1")
            @PathVariable Long id) {
        LibroDTO libro = libroService.obtenerLibroPorId(id);
        ApiResponse<LibroDTO> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Libro encontrado",
                libro,
                1L
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar todos los libros", description = "Obtiene una lista de todos los libros activos en el catálogo")
    @ApiResponse(responseCode = "200", description = "Lista de libros obtenida exitosamente")
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
    @ApiResponse(responseCode = "200", description = "Resultados de búsqueda obtenidos exitosamente")
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
    @ApiResponse(responseCode = "200", description = "Lista de libros disponibles obtenida exitosamente")
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
        @ApiResponse(responseCode = "200", description = "Lista de libros por categoría"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
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
    @ApiResponse(responseCode = "200", description = "Lista de libros del autor")
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
        @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<LibroDTO>> actualizarStock(
            @Parameter(description = "ID del libro", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nueva cantidad total", required = true, example = "15")
            @RequestParam Integer cantidad) {
        LibroDTO libroActualizado = libroService.actualizarStock(id, cantidad);
        ApiResponse<LibroDTO> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Stock actualizado exitosamente",
                libroActualizado,
                1L
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verificar ISBN", description = "Verifica si un ISBN ya está registrado en el sistema")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
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
