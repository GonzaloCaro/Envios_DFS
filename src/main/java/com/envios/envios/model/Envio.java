package com.envios.envios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Envio")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El aeropuerto de origen es obligatorio")
    @ManyToOne
    @JoinColumn(name = "origen", nullable = false)
    private Aeropuerto origen;

    @NotNull(message = "El aeropuerto de destino es obligatorio")
    @ManyToOne
    @JoinColumn(name = "destino", nullable = false)
    private Aeropuerto destino;

    @Column(length = 500)
    private String descripcion;

    // Constructores
    public Envio() {
    }

    public Envio(Aeropuerto origen, Aeropuerto destino, String descripcion) {
        this.origen = origen;
        this.destino = destino;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Aeropuerto getOrigen() {
        return origen;
    }

    public void setOrigen(Aeropuerto origen) {
        this.origen = origen;
    }

    public Aeropuerto getDestino() {
        return destino;
    }

    public void setDestino(Aeropuerto destino) {
        this.destino = destino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // toString
    @Override
    public String toString() {
        return "Envio{" +
                "id=" + id +
                ", origen=" + origen.getId() +
                ", destino=" + destino.getId() +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}