package coreService.controller;

import coreService.model.Usuario;
import coreService.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@DisplayName("AuthController")
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean AuthService authService;

    @Test
    @WithMockUser
    @DisplayName("GET /api/auth/usuario/{uid} retorna 200 cuando existe")
    void obtenerUsuario_returns200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setFirebaseUid("uid-123");
        usuario.setEmail("test@sigle.cl");
        usuario.setNombre("Test");
        usuario.setRol(Usuario.Rol.PACIENTE);

        when(authService.obtenerPorUid("uid-123")).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/auth/usuario/uid-123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@sigle.cl"))
            .andExpect(jsonPath("$.rol").value("PACIENTE"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/auth/usuario/{uid} retorna 404 cuando no existe")
    void obtenerUsuario_returns404() throws Exception {
        when(authService.obtenerPorUid("uid-inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/usuario/uid-inexistente"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/auth/usuario/{id}/rol retorna 200 con rol actualizado")
    void cambiarRol_returns200() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setRol(Usuario.Rol.ADMINISTRATIVO);

        when(authService.cambiarRol(anyLong(), any(Usuario.Rol.class))).thenReturn(usuario);

        mockMvc.perform(put("/api/auth/usuario/1/rol")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(Map.of("rol", "ADMINISTRATIVO"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.rol").value("ADMINISTRATIVO"));
    }
}