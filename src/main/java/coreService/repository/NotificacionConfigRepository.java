package coreService.repository;

import coreService.model.NotificacionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionConfigRepository
        extends JpaRepository<NotificacionConfig, Long> {

    List<NotificacionConfig> findByActivoTrue();
}