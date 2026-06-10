package coreService.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("DashboardService")
class DashboardServiceTest {

    @Mock private RestTemplate restTemplate;
    @InjectMocks private DashboardService dashboardService;

    @Test
    @DisplayName("obtenerMetricas retorna métricas cuando servicios responden")
    void obtenerMetricas_retornaMetricas() {
        when(restTemplate.getForObject(
            eq("http://listas-service/api/listas/metricas"), eq(Object.class)))
            .thenReturn(Map.of("total", 5));
        when(restTemplate.getForObject(
            eq("http://citas-service/api/citas/metricas"), eq(Object.class)))
            .thenReturn(Map.of("total", 10));

        Map<String, Object> resultado = dashboardService.obtenerMetricas();

        assertThat(resultado).containsKey("listas");
        assertThat(resultado).containsKey("citas");
    }

    @Test
    @DisplayName("obtenerMetricas retorna 'no disponible' cuando listas falla")
    void obtenerMetricas_listasFalla_retornaNoDisponible() {
        when(restTemplate.getForObject(
            eq("http://listas-service/api/listas/metricas"), eq(Object.class)))
            .thenThrow(new RestClientException("timeout"));
        when(restTemplate.getForObject(
            eq("http://citas-service/api/citas/metricas"), eq(Object.class)))
            .thenReturn(Map.of("total", 10));

        Map<String, Object> resultado = dashboardService.obtenerMetricas();

        assertThat(resultado.get("listas")).isEqualTo("no disponible");
        assertThat(resultado).containsKey("citas");
    }

    @Test
    @DisplayName("obtenerMetricas retorna 'no disponible' cuando ambos servicios fallan")
    void obtenerMetricas_ambosFallan_retornaNoDisponible() {
        when(restTemplate.getForObject(anyString(), eq(Object.class)))
            .thenThrow(new RestClientException("timeout"));

        Map<String, Object> resultado = dashboardService.obtenerMetricas();

        assertThat(resultado.get("listas")).isEqualTo("no disponible");
        assertThat(resultado.get("citas")).isEqualTo("no disponible");
    }
}