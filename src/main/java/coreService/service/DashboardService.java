package coreService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RestTemplate restTemplate;

    public Map<String, Object> obtenerMetricas() {
        Map<String, Object> metricas = new HashMap<>();

        try {
            Object metricasListas = restTemplate.getForObject(
                "http://listas-service/api/listas/metricas",
                Object.class);
            metricas.put("listas", metricasListas);
        } catch (Exception e) {
            metricas.put("listas", "no disponible");
        }

        try {
            Object metricasCitas = restTemplate.getForObject(
                "http://citas-service/api/citas/metricas",
                Object.class);
            metricas.put("citas", metricasCitas);
        } catch (Exception e) {
            metricas.put("citas", "no disponible");
        }

        return metricas;
    }
}