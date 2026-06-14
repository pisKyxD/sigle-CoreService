package coreService.controller;

import coreService.service.DashboardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
@DisplayName("DashboardController")
class DashboardControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean DashboardService dashboardService;

    @Test
    @WithMockUser
    @DisplayName("GET /api/dashboard/metricas retorna 200 con métricas")
    void metricas_returns200() throws Exception {
        when(dashboardService.obtenerMetricas())
            .thenReturn(Map.of("listas", 5, "citas", 10));

        mockMvc.perform(get("/api/dashboard/metricas"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.listas").value(5))
            .andExpect(jsonPath("$.citas").value(10));
    }
}