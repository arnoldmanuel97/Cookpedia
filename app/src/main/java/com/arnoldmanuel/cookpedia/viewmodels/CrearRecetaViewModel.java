package com.arnoldmanuel.cookpedia.viewmodels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.ViewModel;

import com.arnoldmanuel.cookpedia.model.Ingrediente;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;

import java.util.ArrayList;

public class CrearRecetaViewModel extends ViewModel {


    public RecetaConIngredientesConPasos recetaConCosas = null;
    public Bitmap photo;
    public Bitmap oldPhoto;
    public String oldPathPhoto;
    public String titulo;
    public String descripcion;
    public String comensales;
    public String tiempo;
    public ArrayList<String> textosIngredientes = new ArrayList<>();
    public ArrayList<String> textosPasos =  new ArrayList<>();
    public ArrayList<String[]> oldPathPasos = new ArrayList<>();;
    public ArrayList<Bitmap[]> bitmapsPasos = new ArrayList<>();
    public ArrayList<Bitmap[]> oldBitmapsPasos = new ArrayList<>();

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setTextosIngredientes(ArrayList<String> textosIngredientes) {
        this.textosIngredientes = textosIngredientes;
    }

    public void setTextosPasos(ArrayList<String> textosPasos) {
        this.textosPasos = textosPasos;
    }

    public void setBitmapsPasos(ArrayList<Bitmap[]> bitmapsPasos) {
        this.bitmapsPasos = bitmapsPasos;
    }

    public void setState(RecetaConIngredientesConPasos recetaConCosas) {
        this.recetaConCosas = recetaConCosas;
        this.titulo = recetaConCosas.receta.getTitulo();
        this.descripcion = recetaConCosas.receta.getDescripcion();
        this.comensales = recetaConCosas.receta.getComensales();
        this.tiempo = recetaConCosas.receta.getTiempo();

        this.oldPathPhoto = recetaConCosas.receta.getImagen();
        this.photo = BitmapFactory.decodeFile(this.oldPathPhoto);
        this.oldPhoto = BitmapFactory.decodeFile(this.oldPathPhoto);

        for (Ingrediente ingrediente : recetaConCosas.ingredientes) {
            this.textosIngredientes.add(ingrediente.getTexto());
        }
        for (int i = 0; i < recetaConCosas.pasosReceta.size(); i++) {
            textosPasos.add(recetaConCosas.pasosReceta.get(i).getTexto());
            oldPathPasos.add(new String[]{
                    recetaConCosas.pasosReceta.get(i).getImage1(),
                    recetaConCosas.pasosReceta.get(i).getImage2(),
                    recetaConCosas.pasosReceta.get(i).getImage3()
            });
            bitmapsPasos.add(new Bitmap[]{
                    BitmapFactory.decodeFile(recetaConCosas.pasosReceta.get(i).getImage1()),
                    BitmapFactory.decodeFile(recetaConCosas.pasosReceta.get(i).getImage2()),
                    BitmapFactory.decodeFile(recetaConCosas.pasosReceta.get(i).getImage3())
            });
            oldBitmapsPasos.add(new Bitmap[]{
                    BitmapFactory.decodeFile(recetaConCosas.pasosReceta.get(i).getImage1()),
                    BitmapFactory.decodeFile(recetaConCosas.pasosReceta.get(i).getImage2()),
                    BitmapFactory.decodeFile(recetaConCosas.pasosReceta.get(i).getImage3())
            });

        }


    }
}
