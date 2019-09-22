package com.arnoldmanuel.cookpedia.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.arnoldmanuel.cookpedia.model.Ingrediente;
import com.arnoldmanuel.cookpedia.model.PasoReceta;
import com.arnoldmanuel.cookpedia.model.Receta;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;

import java.util.List;

@Dao
public interface RecetaDao {

    @Transaction
    @Query("SELECT * FROM receta WHERE recetaId LIKE :id")
    RecetaConIngredientesConPasos getReceta(String id);

    @Transaction
    @Query("SELECT * FROM receta WHERE titulo LIKE :search")
    LiveData<List<RecetaConIngredientesConPasos>> getRecetaByTitulo(String search);

    @Insert
    long insertReceta(Receta receta);

    @Update
    void updateReceta(Receta receta);

    @Delete
    void deleteReceta(Receta receta);

    @Insert
    void insertIngredientes(List<Ingrediente> ingredientes);

    @Delete
    void deleteIngredientes(List<Ingrediente> ingredientes);

    @Insert
    void insertPasosReceta(List<PasoReceta> pasoRecetas);

    @Delete
    void deletePasosReceta(List<PasoReceta> pasoRecetas);

    @Transaction
    @Query("SELECT * FROM receta")
    LiveData<List<RecetaConIngredientesConPasos>> getAllRecetas();
}
