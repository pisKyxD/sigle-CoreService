package coreService.service;

import coreService.model.NotificacionConfig;
import coreService.repository.NotificacionConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificacionConfigService")
class NotificacionConfigServiceTest {

    @Mock private NotificacionConfigRepository repo;
    @InjectMocks private NotificacionConfigService service;

    private NotificacionConfig configEjemplo;

    @BeforeEach
    void setUp() {
        configEjemplo = new NotificacionConfig();
        configEjemplo.setId(1L);
        configEjemplo.setTipo(NotificacionConfig.Canal.EMAIL);
        configEjemplo.setActivo(true);
    }

    @Test
    @DisplayName("listarActivos retorna configs activas")
    void listarActivos_retornaActivos() {
        when(repo.findByActivoTrue()).thenReturn(List.of(configEjemplo));

        List<NotificacionConfig> resultado = service.listarActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo(NotificacionConfig.Canal.EMAIL);
    }

    @Test
    @DisplayName("obtenerPorId retorna config cuando existe")
    void obtenerPorId_retornaConfig() {
        when(repo.findById(1L)).thenReturn(Optional.of(configEjemplo));

        NotificacionConfig resultado = service.obtenerPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción cuando no existe")
    void obtenerPorId_lanzaExcepcion() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Configuración no encontrada");
    }

    @Test
    @DisplayName("cambiarEstado actualiza activo correctamente")
    void cambiarEstado_actualizaActivo() {
        when(repo.findById(1L)).thenReturn(Optional.of(configEjemplo));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        NotificacionConfig resultado = service.cambiarEstado(1L, false);

        assertThat(resultado.getActivo()).isFalse();
        verify(repo, times(1)).save(any());
    }
}