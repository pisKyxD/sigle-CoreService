package coreService.controller;

import coreService.model.Usuario;
import coreService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/usuario/{uid}")
    public ResponseEntity<Usuario> obtenerUsuario(
            @PathVariable String uid) {
        return authService.obtenerPorUid(uid)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/usuario/{id}/rol")
    public ResponseEntity<Usuario> cambiarRol(
            @PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        Usuario.Rol nuevoRol = Usuario.Rol.valueOf(body.get("rol"));
        return ResponseEntity.ok(
            authService.cambiarRol(id, nuevoRol));
    }
}