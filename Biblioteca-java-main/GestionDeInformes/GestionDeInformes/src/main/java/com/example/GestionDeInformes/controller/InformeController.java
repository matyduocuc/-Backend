package com.example.GestionDeInformes.controller;

import com.example.GestionDeInformes.application.dto.ApiResponse;
import com.example.GestionDeInformes.application.dto.MultasResumenDTO;
import com.example.GestionDeInformes.application.dto.PrestamosResumenDTO;
import com.example.GestionDeInformes.application.dto.UsuarioResumenDTO;
import com.example.GestionDeInformes.service.InformeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/informes")
@Tag(name = "Informes", description = "API para consulta de informes y resúmenes")
public class InformeController {

    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    @GetMapping("/prestamos/resumen")
    @Operation(summary = "Obtener resumen de préstamos", description = "Obtiene un resumen general de todos los préstamos")
    public ResponseEntity<ApiResponse<PrestamosResumenDTO>> obtenerResumenPrestamos() {
        try {
            PrestamosResumenDTO resumen = informeService.obtenerResumenPrestamos();
            ApiResponse<PrestamosResumenDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Resumen de préstamos obtenido correctamente",
                    resumen,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<PrestamosResumenDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener resumen de préstamos: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/usuarios/{usuarioId}/resumen")
    @Operation(summary = "Obtener resumen de usuario", description = "Obtiene un resumen de préstamos y multas de un usuario específico")
    public ResponseEntity<ApiResponse<UsuarioResumenDTO>> obtenerResumenUsuario(@PathVariable Long usuarioId) {
        try {
            UsuarioResumenDTO resumen = informeService.obtenerResumenUsuario(usuarioId);
            ApiResponse<UsuarioResumenDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Resumen de usuario obtenido correctamente",
                    resumen,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<UsuarioResumenDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Usuario no encontrado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<UsuarioResumenDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener resumen de usuario: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/multas/resumen")
    @Operation(summary = "Obtener resumen de multas", description = "Obtiene un resumen general de todas las multas")
    public ResponseEntity<ApiResponse<MultasResumenDTO>> obtenerResumenMultas() {
        try {
            MultasResumenDTO resumen = informeService.obtenerResumenMultas();
            ApiResponse<MultasResumenDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Resumen de multas obtenido correctamente",
                    resumen,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<MultasResumenDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener resumen de multas: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

