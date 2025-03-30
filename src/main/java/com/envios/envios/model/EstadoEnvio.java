package com.envios.envios.model;

import java.time.LocalDateTime;

public class EstadoEnvio {
    private String id;
    private String nombre;
    private LocalDateTime fechaActualizacion;

    public EstadoEnvio(String id, String nombre, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.fechaActualizacion = fechaActualizacion;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }
}