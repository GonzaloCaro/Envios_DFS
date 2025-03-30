package com.envios.envios.service;

import com.envios.envios.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnvioService {

    private final List<Envio> envios = new ArrayList<>();

    public EnvioService() {
        // Datos en memoria con IDs de estado
        envios.add(new Envio("1", "Cliente1", "México", "España",
                new EstadoEnvio("1", "En tránsito", LocalDateTime.now()),
                new Ubicacion("Ciudad de México", "México")));

        envios.add(new Envio("2", "Cliente2", "Colombia", "Chile",
                new EstadoEnvio("2", "En aduanas", LocalDateTime.now().minusDays(1)),
                new Ubicacion("Bogotá", "Colombia")));

        envios.add(new Envio("3", "Cliente1", "Argentina", "Brasil",
                new EstadoEnvio("3", "Entregado", LocalDateTime.now().minusDays(3)),
                new Ubicacion("São Paulo", "Brasil")));
    }

    public List<Envio> obtenerTodos() {
        return envios;
    }

    public Envio obtenerPorId(String id) {
        return envios.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<EstadoEnvio> obtenerTodosEstados() {
        return envios.stream()
                .map(Envio::getEstado)
                .distinct() // Elimina duplicados
                .collect(Collectors.toList());
    }
}