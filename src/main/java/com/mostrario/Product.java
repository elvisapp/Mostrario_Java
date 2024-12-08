package com.mostrario;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int id;

    @SerializedName("Nombre")
    private String nombre;

    @SerializedName("Descripcion")
    private String descripcion;

    @SerializedName("Precio")
    private double precio;

    @SerializedName("Foto")
    private String foto; // Cambiado a String para datos en Base64

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
