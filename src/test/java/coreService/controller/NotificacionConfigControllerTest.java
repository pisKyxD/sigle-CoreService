package coreService.controller;

import coreService.model.NotificacionConfig;
import coreService.service.NotificacionConfigService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificacionConfigController.class)
@DisplayName("NotificacionConfigController")
class NotificacionConfigControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean NotificacionConfigService service;

    @Test
    @WithMockUser
    @DisplayName("GET /api/notificaciones/config retorna 200 con lista")
    void listar_returns200() throws Exception {
        NotificacionConfig config = new NotificacionConfig();
        config.setId(1L);
        config.setTipo(NotificacionConfig.Canal.EMAIL);
        config.setActivo(true);

        when(service.listarActivos()).thenReturn(List.of(config));

        mockMvc.perform(get("/api/notificaciones/config"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].tipo").value("EMAIL"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/notificaciones/config/{id} retorna 200")
    void obtener_returns200() throws Exception {
        NotificacionConfig config = new NotificacionConfig();
        config.setId(1L);
        config.setTipo(NotificacionConfig.Canal.SMS);

        when(service.obtenerPorId(1L)).thenReturn(config);

        mockMvc.perform(get("/api/notificaciones/config/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.tipo").value("SMS"));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /api/notificaciones/config/{id}/estado retorna 200")
    void cambiarEstado_returns200() throws Exception {
        NotificacionConfig config = new NotificacionConfig();
        config.setId(1L);
        config.setActivo(false);

        when(service.cambiarEstado(1L, false)).thenReturn(config);

        mockMvc.perform(put("/api/notificaciones/config/1/estado")
            .with(csrf())
            .param("activo", "false"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.activo").value(false));
    }
}