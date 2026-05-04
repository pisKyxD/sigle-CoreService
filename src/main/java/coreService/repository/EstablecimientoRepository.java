package coreService.repository;

import coreService.model.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstablecimientoRepository
        extends JpaRepository<Establecimiento, Long> {

    List<Establecimiento> findByActivoTrue();
}