package com.example.Gestion.de.prestamos.application.controller;

import com.example.Gestion.de.prestamos.application.dto.ApiResponse;
import com.example.Gestion.de.prestamos.application.dto.PrestamoDTO;
import com.example.Gestion.de.prestamos.domain.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PrestamoDTO>> crear(@RequestBody @Valid PrestamoDTO dto) {
        try {
            PrestamoDTO creado = prestamoService.crearPrestamo(dto);
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.CREATED.value(),
                    "Préstamo creado exitosamente",
                    creado,
                    1L
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.BAD_REQUEST.value(),
                    "Error: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al crear préstamo: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrestamoDTO>> obtener(@PathVariable Long id) {
        try {
            PrestamoDTO prestamo = prestamoService.obtenerPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Préstamo no encontrado con ID: " + id));
            
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Préstamo encontrado",
                    prestamo,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Préstamo no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener préstamo: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<PrestamoDTO>>> listarPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<PrestamoDTO> prestamos = prestamoService.listarPorUsuario(usuarioId);
            
            if (prestamos.isEmpty()) {
                ApiResponse<List<PrestamoDTO>> response = new ApiResponse<>(
                        false,
                        HttpStatus.NO_CONTENT.value(),
                        "No se encontraron préstamos para el usuario",
                        null,
                        0L
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            
            ApiResponse<List<PrestamoDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Préstamos obtenidos correctamente",
                    prestamos,
                    (long) prestamos.size()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<PrestamoDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al listar préstamos: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<PrestamoDTO>>> listarPorEstado(@PathVariable String estado) {
        try {
            List<PrestamoDTO> prestamos = prestamoService.listarPorEstado(estado);
            
            if (prestamos.isEmpty()) {
                ApiResponse<List<PrestamoDTO>> response = new ApiResponse<>(
                        false,
                        HttpStatus.NO_CONTENT.value(),
                        "No se encontraron préstamos con el estado especificado",
                        null,
                        0L
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            
            ApiResponse<List<PrestamoDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Préstamos obtenidos correctamente por estado",
                    prestamos,
                    (long) prestamos.size()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<PrestamoDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.BAD_REQUEST.value(),
                    "Estado inválido: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<List<PrestamoDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al listar préstamos: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{id}/renovar")
    public ResponseEntity<ApiResponse<PrestamoDTO>> renovar(@PathVariable Long id) {
        try {
            PrestamoDTO prestamo = prestamoService.renovar(id);
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Préstamo renovado exitosamente",
                    prestamo,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Préstamo no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalStateException e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.BAD_REQUEST.value(),
                    "Error: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al renovar préstamo: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<ApiResponse<PrestamoDTO>> devolver(@PathVariable Long id) {
        try {
            PrestamoDTO prestamo = prestamoService.devolver(id);
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Préstamo devuelto exitosamente",
                    prestamo,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Préstamo no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<PrestamoDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al devolver préstamo: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
