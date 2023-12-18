package com.example.vecibox.model;

public class Comentario {
    String Comentario;
    String Tipo;
    public Comentario(){}

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public Comentario(String comentario, String tipo, String usuario) {
        Comentario = comentario;
        Tipo = tipo;
        Usuario = usuario;
    }

    String Usuario;
}
