package com.arnoldmanuel.cookpedia.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;


public class RecetaConIngredientesConPasos implements Serializable {

    @Embedded
    public Receta receta;
    @Relation(entity = Ingrediente.class, parentColumn = "recetaId", entityColumn = "recetaIngredienteId")
    public List<Ingrediente> ingredientes;
    @Relation(entity = PasoReceta.class, parentColumn = "recetaId", entityColumn = "recetaPasoRecetaId")
    public List<PasoReceta> pasosReceta;

    public RecetaConIngredientesConPasos(Receta receta,
                                         List<Ingrediente> ingredientes,
                                         List<PasoReceta> pasosReceta) {
        this.receta = receta;
        this.ingredientes = ingredientes;
        this.pasosReceta = pasosReceta;
    }

    @Ignore
    public RecetaConIngredientesConPasos() {}

    @Override
    public String toString() {
        return "RecetaConIngredientesConPasos{" +
                "receta=" + receta +
                ", ingredientes=" + ingredientes.size() +
                ", pasosReceta=" + pasosReceta.size() +
                '}';
    }
}
