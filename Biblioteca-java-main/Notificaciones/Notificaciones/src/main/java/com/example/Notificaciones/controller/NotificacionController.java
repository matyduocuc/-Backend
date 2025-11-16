package com.example.Notificaciones.controller;

import com.example.Notificaciones.dto.ApiResponse;
import com.example.Notificaciones.dto.NotificacionDTO;
import com.example.Notificaciones.dto.NotificacionRequest;
import com.example.Notificaciones.service.NotificacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificacionDTO>> crearNotificacion(@RequestBody NotificacionRequest request) {
        try {
            NotificacionDTO notificacionCreada = notificacionService.crearNotificacion(request);
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.CREATED.value(),
                    "Notificación creada exitosamente",
                    notificacionCreada,
                    1L
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al crear notificación: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ApiResponse<List<NotificacionDTO>>> obtenerNotificacionesPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(usuarioId);
            
            if (notificaciones.isEmpty()) {
                ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                        false,
                        HttpStatus.NO_CONTENT.value(),
                        "No se encontraron notificaciones para el usuario",
                        null,
                        0L
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificaciones obtenidas correctamente",
                    notificaciones,
                    (long) notificaciones.size()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener notificaciones: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<ApiResponse<List<NotificacionDTO>>> obtenerNotificacionesPendientes() {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.obtenerNotificacionesPendientes();
            
            if (notificaciones.isEmpty()) {
                ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                        false,
                        HttpStatus.NO_CONTENT.value(),
                        "No se encontraron notificaciones pendientes",
                        null,
                        0L
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificaciones pendientes obtenidas correctamente",
                    notificaciones,
                    (long) notificaciones.size()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener notificaciones pendientes: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/enviar")
    public ResponseEntity<ApiResponse<NotificacionDTO>> marcarComoEnviada(@PathVariable Long id) {
        try {
            NotificacionDTO notificacion = notificacionService.marcarComoEnviada(id);
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificación marcada como enviada exitosamente",
                    notificacion,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Notificación no encontrada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al marcar notificación como enviada: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<ApiResponse<NotificacionDTO>> marcarComoLeida(@PathVariable Long id) {
        try {
            NotificacionDTO notificacion = notificacionService.marcarComoLeida(id);
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificación marcada como leída exitosamente",
                    notificacion,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Notificación no encontrada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al marcar notificación como leída: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}/fallar")
    public ResponseEntity<ApiResponse<NotificacionDTO>> marcarComoFallida(
            @PathVariable Long id,
            @RequestParam String error) {
        try {
            NotificacionDTO notificacion = notificacionService.marcarComoFallida(id, error);
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificación marcada como fallida exitosamente",
                    notificacion,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Notificación no encontrada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al marcar notificación como fallida: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/procesar-pendientes")
    public ResponseEntity<ApiResponse<List<NotificacionDTO>>> procesarNotificacionesPendientes() {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.procesarNotificacionesPendientes();
            
            if (notificaciones.isEmpty()) {
                ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                        false,
                        HttpStatus.NO_CONTENT.value(),
                        "No se encontraron notificaciones pendientes para procesar",
                        null,
                        0L
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificaciones procesadas exitosamente",
                    notificaciones,
                    (long) notificaciones.size()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al procesar notificaciones: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarNotificacion(@PathVariable Long id) {
        try {
            notificacionService.eliminarNotificacion(id);
            ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificación eliminada exitosamente",
                    null,
                    0L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Notificación no encontrada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al eliminar notificación: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificacionDTO>> obtenerNotificacionPorId(@PathVariable Long id) {
        try {
            NotificacionDTO notificacion = notificacionService.obtenerNotificacionPorId(id);
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificación encontrada",
                    notificacion,
                    1L
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    "Notificación no encontrada",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ApiResponse<NotificacionDTO> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener notificación: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificacionDTO>>> obtenerTodasLasNotificaciones() {
        try {
            List<NotificacionDTO> notificaciones = notificacionService.obtenerTodasLasNotificaciones();
            
            if (notificaciones.isEmpty()) {
                ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                        false,
                        HttpStatus.NO_CONTENT.value(),
                        "No se encontraron notificaciones",
                        null,
                        0L
                );
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
            }
            
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Notificaciones obtenidas correctamente",
                    notificaciones,
                    (long) notificaciones.size()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<NotificacionDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al obtener notificaciones: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/usuario/{usuarioId}/pendientes/count")
    public ResponseEntity<ApiResponse<Long>> contarNotificacionesPendientesUsuario(@PathVariable Long usuarioId) {
        try {
            Long count = notificacionService.contarNotificacionesPendientesUsuario(usuarioId);
            ApiResponse<Long> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Conteo de notificaciones pendientes obtenido correctamente",
                    count,
                    count
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Long> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al contar notificaciones: " + e.getMessage(),
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}