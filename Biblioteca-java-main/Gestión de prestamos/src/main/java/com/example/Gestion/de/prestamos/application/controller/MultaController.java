package com.example.Gestion.de.prestamos.application.controller;

import com.example.Gestion.de.prestamos.application.dto.ApiResponse;
import com.example.Gestion.de.prestamos.application.dto.MultaDTO;
import com.example.Gestion.de.prestamos.domain.service.MultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @GetMapping("/prestamo/{prestamoId}")
    public ResponseEntity<ApiResponse<List<MultaDTO>>> listarPorPrestamo(@PathVariable Long prestamoId) {
        try {
            List<MultaDTO> multas = multaService.listarPorPrestamo(prestamoId);
            
            if (multas.isEmpty()) {
                ApiResponse<List<MultaDTO>> response = new ApiResponse<>(
                        false,
                        HttpStatus.NO_CONTENT.value(),
                        "No se encontraron multas para el préstamo",
                        null,
                        0L
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            
            ApiResponse<List<MultaDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Multas obtenidas correctamente",
                    multas,
                    (long) multas.size()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<MultaDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Préstamo no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<List<MultaDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al listar multas: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{id}/pagar")
    public ResponseEntity<ApiResponse<MultaDTO>> pagar(@PathVariable Long id) {
        try {
            MultaDTO multa = multaService.pagar(id);
            ApiResponse<MultaDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Multa pagada exitosamente",
                    multa,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<MultaDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Multa no encontrada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<MultaDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al pagar multa: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/{id}/eximir")
    public ResponseEntity<ApiResponse<MultaDTO>> eximir(
            @PathVariable Long id,
            @RequestParam(name = "motivo", required = false) String motivo) {
        try {
            MultaDTO multa = multaService.eximir(id, motivo);
            ApiResponse<MultaDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Multa eximida exitosamente",
                    multa,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<MultaDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Multa no encontrada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<MultaDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al eximir multa: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
