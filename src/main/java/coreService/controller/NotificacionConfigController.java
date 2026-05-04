package coreService.controller;

import coreService.model.NotificacionConfig;
import coreService.service.NotificacionConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones/config")
@RequiredArgsConstructor
public class NotificacionConfigController {

    private final NotificacionConfigService service;

    @GetMapping
    public ResponseEntity<List<NotificacionConfig>> listar() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionConfig> obtener(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<NotificacionConfig> cambiarEstado(
            @PathVariable Long id,
            @RequestParam Boolean activo) {
        return ResponseEntity.ok(
            service.cambiarEstado(id, activo));
    }
}