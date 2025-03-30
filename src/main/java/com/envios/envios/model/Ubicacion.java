package com.envios.envios.model;

public class Ubicacion {
    private String ciudad;
    private String pais;
    private String codigoPostal;
    private String direccion;

    // Constructor
    public Ubicacion(String ciudad, String pais) {
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Getters y Setters
    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return ciudad + ", " + pais;
    }
}