package coreService.service;

import coreService.model.Establecimiento;
import coreService.repository.EstablecimientoRepository;
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
@DisplayName("EstablecimientoService")
class EstablecimientoServiceTest {

    @Mock private EstablecimientoRepository repo;
    @InjectMocks private EstablecimientoService service;

    private Establecimiento establecimientoEjemplo;

    @BeforeEach
    void setUp() {
        establecimientoEjemplo = new Establecimiento();
        establecimientoEjemplo.setId(1L);
        establecimientoEjemplo.setNombre("Hospital Central");
        establecimientoEjemplo.setTipo(Establecimiento.Tipo.HOSPITAL_ALTA);
        establecimientoEjemplo.setRegion("RM");
        establecimientoEjemplo.setActivo(true);
    }

    @Test
    @DisplayName("listarActivos retorna establecimientos activos")
    void listarActivos_retornaActivos() {
        when(repo.findByActivoTrue()).thenReturn(List.of(establecimientoEjemplo));

        List<Establecimiento> resultado = service.listarActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Hospital Central");
    }

    @Test
    @DisplayName("obtenerPorId retorna establecimiento cuando existe")
    void obtenerPorId_retornaEstablecimiento() {
        when(repo.findById(1L)).thenReturn(Optional.of(establecimientoEjemplo));

        Establecimiento resultado = service.obtenerPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getRegion()).isEqualTo("RM");
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción cuando no existe")
    void obtenerPorId_lanzaExcepcion() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.obtenerPorId(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Establecimiento no encontrado");
    }

    @Test
    @DisplayName("guardar persiste el establecimiento")
    void guardar_persisteEstablecimiento() {
        when(repo.save(any(Establecimiento.class))).thenAnswer(i -> i.getArgument(0));

        Establecimiento resultado = service.guardar(establecimientoEjemplo);

        assertThat(resultado.getNombre()).isEqualTo("Hospital Central");
        verify(repo, times(1)).save(any());
    }

    @Test
    @DisplayName("eliminar hace soft delete poniendo activo en false")
    void eliminar_softDelete() {
        when(repo.findById(1L)).thenReturn(Optional.of(establecimientoEjemplo));
        when(repo.save(any(Establecimiento.class))).thenAnswer(i -> i.getArgument(0));

        service.eliminar(1L);

        assertThat(establecimientoEjemplo.getActivo()).isFalse();
        verify(repo, times(1)).save(establecimientoEjemplo);
    }
}