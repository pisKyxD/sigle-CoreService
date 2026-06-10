package coreService.controller;

import coreService.model.Establecimiento;
import coreService.service.EstablecimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstablecimientoController.class)
@DisplayName("EstablecimientoController")
class EstablecimientoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean EstablecimientoService service;

    @Test
    @WithMockUser
    @DisplayName("GET /api/establecimientos retorna 200 con lista")
    void listar_returns200() throws Exception {
        Establecimiento e = new Establecimiento();
        e.setId(1L);
        e.setNombre("Hospital Central");
        e.setTipo(Establecimiento.Tipo.HOSPITAL_ALTA);
        e.setRegion("RM");
        e.setActivo(true);

        when(service.listarActivos()).thenReturn(List.of(e));

        mockMvc.perform(get("/api/establecimientos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].nombre").value("Hospital Central"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/establecimientos/{id} retorna 200 cuando existe")
    void obtener_returns200() throws Exception {
        Establecimiento e = new Establecimiento();
        e.setId(1L);
        e.setNombre("Hospital Central");
        e.setRegion("RM");

        when(service.obtenerPorId(1L)).thenReturn(e);

        mockMvc.perform(get("/api/establecimientos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Hospital Central"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /api/establecimientos retorna 200 con establecimiento creado")
    void crear_returns200() throws Exception {
        Establecimiento e = new Establecimiento();
        e.setNombre("CAP Sur");
        e.setTipo(Establecimiento.Tipo.CAP);
        e.setRegion("RM");

        Establecimiento guardado = new Establecimiento();
        guardado.setId(2L);
        guardado.setNombre("CAP Sur");
        guardado.setTipo(Establecimiento.Tipo.CAP);
        guardado.setRegion("RM");

        when(service.guardar(any(Establecimiento.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/establecimientos")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(e)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.nombre").value("CAP Sur"));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/establecimientos/{id} retorna 200")
    void eliminar_returns200() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/establecimientos/1")
            .with(csrf()))
            .andExpect(status().isOk());
    }
}