package com.arnoldmanuel.cookpedia.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Ingrediente implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int ingredienteId;

    private int recetaIngredienteId;

    private String texto;

    public Ingrediente(String texto, int recetaIngredienteId) {
        this.texto = texto;
        this.recetaIngredienteId = recetaIngredienteId;
    }

    @Ignore
    public Ingrediente(String texto) {
        this.texto = texto;
    }

    public int getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(int ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public int getRecetaIngredienteId() {
        return recetaIngredienteId;
    }

    public void setRecetaIngredienteId(int recetaIngredienteId) {
        this.recetaIngredienteId = recetaIngredienteId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
