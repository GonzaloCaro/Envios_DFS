package com.envios.envios.DTO;

import jakarta.validation.constraints.*;

public class EnvioDTO {

    private Long id;

    @NotNull(message = "El ID del aeropuerto origen es obligatorio")
    private Long origenId;

    @NotNull(message = "El ID del aeropuerto destino es obligatorio")
    private Long destinoId;

    private String descripcion;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrigenId() {
        return origenId;
    }

    public void setOrigenId(Long origenId) {
        this.origenId = origenId;
    }

    public Long getDestinoId() {
        return destinoId;
    }

    public void setDestinoId(Long destinoId) {
        this.destinoId = destinoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}