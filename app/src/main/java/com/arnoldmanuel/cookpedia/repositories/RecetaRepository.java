package com.arnoldmanuel.cookpedia.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.arnoldmanuel.cookpedia.dao.RecetaDao;
import com.arnoldmanuel.cookpedia.database.RecetaDatabase;
import com.arnoldmanuel.cookpedia.model.Ingrediente;
import com.arnoldmanuel.cookpedia.model.PasoReceta;
import com.arnoldmanuel.cookpedia.model.Receta;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecetaRepository {

    private RecetaDao recetaDao;
    private LiveData<List<RecetaConIngredientesConPasos>> allRecetas;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RecetaRepository(Application application) {
        RecetaDatabase recetaDatabase = RecetaDatabase.getInstance(application);
        recetaDao = recetaDatabase.recetaDao();
        allRecetas = recetaDao.getAllRecetas();
    }

    public long insertReceta(final Receta receta) {
        Callable<Long> insertCallable = new Callable<Long>() {
            @Override
            public Long call() {
                return recetaDao.insertReceta(receta);
            }
        };
        long rowId = 0;
        Future<Long> future = executorService.submit(insertCallable);
        try {
            rowId = future.get();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return rowId;
    }

    public void updateReceta(final Receta receta) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recetaDao.updateReceta(receta);
            }
        });
    }

    public void deleteReceta(final Receta receta) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recetaDao.deleteReceta(receta);
            }
        });
    }

    public void insertIngredientes(final List<Ingrediente> ingredientes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recetaDao.insertIngredientes(ingredientes);
            }
        });
    }


    public void deleteIngredientes(final List<Ingrediente> ingredientes) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recetaDao.deleteIngredientes(ingredientes);
            }
        });
    }

    public void insertPasosReceta(final List<PasoReceta> pasoRecetas) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recetaDao.insertPasosReceta(pasoRecetas);
            }
        });
    }

    public void deletePasosReceta(final List<PasoReceta> pasoRecetas) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                recetaDao.deletePasosReceta(pasoRecetas);
            }
        });
    }

    public LiveData<List<RecetaConIngredientesConPasos>> getAllRecetas() {
        return allRecetas;
    }

    public LiveData<List<RecetaConIngredientesConPasos>> getRecetaByTitulo(final String search) {
        Callable<LiveData<List<RecetaConIngredientesConPasos>>> insertCallable =
                new Callable<LiveData<List<RecetaConIngredientesConPasos>>>() {
            @Override
            public LiveData<List<RecetaConIngredientesConPasos>> call() {
                return recetaDao.getRecetaByTitulo(search);
            }
        };
        LiveData<List<RecetaConIngredientesConPasos>> recetasBySearch = null;
        Future<LiveData<List<RecetaConIngredientesConPasos>>> future = executorService.submit(insertCallable);
        try {
            recetasBySearch = future.get();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return recetasBySearch;
    }

}
