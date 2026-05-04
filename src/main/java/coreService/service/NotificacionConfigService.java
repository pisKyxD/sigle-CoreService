package coreService.service;

import coreService.model.NotificacionConfig;
import coreService.repository.NotificacionConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionConfigService {

    private final NotificacionConfigRepository repo;

    public List<NotificacionConfig> listarActivos() {
        return repo.findByActivoTrue();
    }

    public NotificacionConfig obtenerPorId(Long id) {
        return repo.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Configuración no encontrada"));
    }

    public NotificacionConfig guardar(NotificacionConfig config) {
        return repo.save(config);
    }

    public NotificacionConfig cambiarEstado(Long id, Boolean activo) {
        NotificacionConfig config = obtenerPorId(id);
        config.setActivo(activo);
        return repo.save(config);
    }
}