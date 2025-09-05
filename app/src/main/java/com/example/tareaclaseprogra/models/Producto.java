package com.example.tareaclaseprogra.models;

public class Producto {
    private int id;
    private String nombre;
    private String estado;
    private String fecha;

    public Producto(int id, String nombre, String estado, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.fecha = fecha;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

