package coreService.service;

import coreService.model.Usuario;
import coreService.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock private UsuarioRepository usuarioRepository;
    @InjectMocks private AuthService authService;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setFirebaseUid("uid-123");
        usuarioEjemplo.setEmail("test@sigle.cl");
        usuarioEjemplo.setNombre("Test User");
        usuarioEjemplo.setRol(Usuario.Rol.PACIENTE);
    }

    @Test
    @DisplayName("obtenerOCrearUsuario retorna usuario existente si ya existe")
    void obtenerOCrear_retornaExistente() {
        when(usuarioRepository.findByFirebaseUid("uid-123"))
            .thenReturn(Optional.of(usuarioEjemplo));

        Usuario resultado = authService.obtenerOCrearUsuario("uid-123", "test@sigle.cl", "Test User");

        assertThat(resultado.getFirebaseUid()).isEqualTo("uid-123");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerOCrearUsuario crea nuevo usuario si no existe")
    void obtenerOCrear_creaUsuarioNuevo() {
        when(usuarioRepository.findByFirebaseUid("uid-nuevo"))
            .thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class)))
            .thenAnswer(i -> i.getArgument(0));

        Usuario resultado = authService.obtenerOCrearUsuario("uid-nuevo", "nuevo@sigle.cl", "Nuevo User");

        assertThat(resultado.getRol()).isEqualTo(Usuario.Rol.PACIENTE);
        assertThat(resultado.getEmail()).isEqualTo("nuevo@sigle.cl");
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("obtenerPorUid retorna usuario cuando existe")
    void obtenerPorUid_retornaUsuario() {
        when(usuarioRepository.findByFirebaseUid("uid-123"))
            .thenReturn(Optional.of(usuarioEjemplo));

        Optional<Usuario> resultado = authService.obtenerPorUid("uid-123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("test@sigle.cl");
    }

    @Test
    @DisplayName("obtenerPorUid retorna vacío cuando no existe")
    void obtenerPorUid_retornaVacio() {
        when(usuarioRepository.findByFirebaseUid("uid-inexistente"))
            .thenReturn(Optional.empty());

        Optional<Usuario> resultado = authService.obtenerPorUid("uid-inexistente");

        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("cambiarRol actualiza el rol correctamente")
    void cambiarRol_actualizaRol() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario resultado = authService.cambiarRol(1L, Usuario.Rol.ADMINISTRATIVO);

        assertThat(resultado.getRol()).isEqualTo(Usuario.Rol.ADMINISTRATIVO);
        verify(usuarioRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("cambiarRol lanza excepción si usuario no existe")
    void cambiarRol_lanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.cambiarRol(99L, Usuario.Rol.DIRECCION))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Usuario no encontrado");
    }
}