package com.envios.envios.model;

public class Envio {
    private String id;
    private String cliente;
    private String origen;
    private String destino;
    private EstadoEnvio estado;
    private Ubicacion ubicacion;

    public Envio(String id, String cliente, String origen, String destino, EstadoEnvio estado, Ubicacion ubicacion) {
        this.id = id;
        this.cliente = cliente;
        this.origen = origen;
        this.destino = destino;
        this.estado = estado;
        this.ubicacion = ubicacion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
