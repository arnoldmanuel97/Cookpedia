package com.arnoldmanuel.cookpedia.model;

import android.net.Uri;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class PasoReceta implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int pasoRecetaId;

    private int recetaPasoRecetaId;
    private String texto;
    private String image1;
    private String image2;
    private String image3;

    @Ignore
    public PasoReceta(String texto, String image1, String image2, String image3, int id) {
        this.texto = texto;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.recetaPasoRecetaId = id;
    }

    public PasoReceta() {

    }

    public PasoReceta(String texto, String image1, String image2, String image3) {
        this.texto = texto;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public int getPasoRecetaId() {
        return pasoRecetaId;
    }

    public void setPasoRecetaId(int pasoRecetaId) {
        this.pasoRecetaId = pasoRecetaId;
    }

    public int getRecetaPasoRecetaId() {
        return recetaPasoRecetaId;
    }

    public void setRecetaPasoRecetaId(int recetaPasoRecetaId) {
        this.recetaPasoRecetaId = recetaPasoRecetaId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
