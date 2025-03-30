package com.envios.envios.controller;

import com.envios.envios.model.Envio;
import com.envios.envios.model.EstadoEnvio;
import com.envios.envios.service.EnvioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    public List<Envio> getAllEnvios() {
        return envioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public Envio getEnvioById(@PathVariable String id) {
        return envioService.obtenerPorId(id);
    }

    @GetMapping("/estados")
    public List<EstadoEnvio> getAllEstadosEnvio() {
        return envioService.obtenerTodosEstados();
    }
}