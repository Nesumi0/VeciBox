package com.example.vecibox.model;

public class Negocio {
    String Nombre, Descripcion, Horario, Operaciones, url_imagen_negocio;
    public Negocio(){}


    public Negocio(String nombre, String descripcion, String horario, String operaciones, String url_imagen_negocio) {
        Nombre = nombre;
        Descripcion = descripcion;
        Horario = horario;
        Operaciones = operaciones;
        this.url_imagen_negocio = url_imagen_negocio;

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getHorario() {
        return Horario;
    }

    public void setHorario(String horario) {
        Horario = horario;
    }

    public String getOperaciones() {
        return Operaciones;
    }

    public void setOperaciones(String operaciones) {
        Operaciones = operaciones;
    }

    public String getUrl_imagen_negocio() {
        return url_imagen_negocio;
    }

    public void setUrl_imagen_negocio(String url_imagen_negocio) {
        this.url_imagen_negocio = url_imagen_negocio;
    }
}
