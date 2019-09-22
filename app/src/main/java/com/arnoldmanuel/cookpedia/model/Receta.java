package com.arnoldmanuel.cookpedia.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Receta implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int recetaId;

    private String imagen;

    private String titulo;

    private String descripcion;

    private String comensales;

    private String tiempo;

    public Receta(String imagen, String titulo, String descripcion, String comensales, String tiempo) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.comensales = comensales;
        this.tiempo = tiempo;
    }

    public int getRecetaId() {
        return recetaId;
    }

    public void setRecetaId(int recetaId) {
        this.recetaId = recetaId;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComensales() {
        return comensales;
    }

    public void setComensales(String comensales) {
        this.comensales = comensales;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
}
