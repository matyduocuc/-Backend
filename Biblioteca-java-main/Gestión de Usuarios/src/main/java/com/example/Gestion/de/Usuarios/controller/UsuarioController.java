package com.example.Gestion.de.Usuarios.controller;

import com.example.Gestion.de.Usuarios.dto.ApiResponse;
import com.example.Gestion.de.Usuarios.dto.UsuarioCreateRequest;
import com.example.Gestion.de.Usuarios.dto.UsuarioDTO;
import com.example.Gestion.de.Usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    public ResponseEntity<ApiResponse<UsuarioDTO>> crearUsuario(@Valid @RequestBody UsuarioCreateRequest request) {
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(request);
        ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                true,
                HttpStatus.CREATED.value(),
                "Usuario creado correctamente",
                usuarioCreado,
                1L
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USUARIO') and @usuarioServiceImpl.getUsuarioEntity(#id).email == authentication.principal.username)")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario")
    public ResponseEntity<ApiResponse<UsuarioDTO>> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioCreateRequest request) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, request);
        ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuario actualizado correctamente",
                usuarioActualizado,
                1L
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario (desactiva)")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuario eliminado (desactivado) correctamente",
                null,
                0L
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USUARIO') and @usuarioServiceImpl.getUsuarioEntity(#id).email == authentication.principal.username)")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene los datos de un usuario por su ID")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerUsuarioPorId(@PathVariable Long id) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuario obtenido correctamente",
                usuario,
                1L
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios", description = "Obtiene la lista de todos los usuarios activos")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();

        if (usuarios.isEmpty()) {
            ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.NO_CONTENT.value(),
                    "No se encontraron usuarios activos",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuarios obtenidos correctamente",
                usuarios,
                (long) usuarios.size()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuario por email", description = "Obtiene un usuario por su email")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerUsuarioPorEmail(@PathVariable String email) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorEmail(email);
        ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuario obtenido correctamente por email",
                usuario,
                1L
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rut/{rut}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuario por RUT", description = "Obtiene un usuario por su RUT")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerUsuarioPorRut(@PathVariable String rut) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorRut(rut);
        ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuario obtenido correctamente por RUT",
                usuario,
                1L
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rol/{rol}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Obtener usuarios por rol", description = "Obtiene usuarios por rol específico")
    public ResponseEntity<ApiResponse<List<UsuarioDTO>>> obtenerUsuariosPorRol(@PathVariable String rol) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerUsuariosPorRol(rol);

        if (usuarios.isEmpty()) {
            ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                    false,
                    HttpStatus.NO_CONTENT.value(),
                    "No se encontraron usuarios para el rol especificado",
                    null,
                    0L
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }

        ApiResponse<List<UsuarioDTO>> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuarios obtenidos correctamente por rol",
                usuarios,
                (long) usuarios.size()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activar usuario", description = "Activa un usuario previamente desactivado")
    public ResponseEntity<ApiResponse<Void>> activarUsuario(@PathVariable Long id) {
        usuarioService.activarUsuario(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuario activado correctamente",
                null,
                0L
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario")
    public ResponseEntity<ApiResponse<Void>> desactivarUsuario(@PathVariable Long id) {
        usuarioService.desactivarUsuario(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Usuario desactivado correctamente",
                null,
                0L
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Obtener mi perfil", description = "Obtiene los datos del usuario autenticado")
    public ResponseEntity<ApiResponse<UsuarioDTO>> obtenerMiPerfil() {
        // Este endpoint sigue pendiente de implementación
        ApiResponse<UsuarioDTO> response = new ApiResponse<>(
                false,
                HttpStatus.NOT_IMPLEMENTED.value(),
                "Endpoint en desarrollo",
                null,
                0L
        );
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }
}
