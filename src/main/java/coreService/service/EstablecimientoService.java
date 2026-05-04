package coreService.service;

import coreService.model.Establecimiento;
import coreService.repository.EstablecimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstablecimientoService {

    private final EstablecimientoRepository repo;

    public List<Establecimiento> listarActivos() {
        return repo.findByActivoTrue();
    }

    public Establecimiento obtenerPorId(Long id) {
        return repo.findById(id)
            .orElseThrow(() ->
                new RuntimeException("Establecimiento no encontrado"));
    }

    public Establecimiento guardar(Establecimiento e) {
        return repo.save(e);
    }

    public void eliminar(Long id) {
        Establecimiento e = obtenerPorId(id);
        e.setActivo(false);
        repo.save(e);
    }
}