package com.arnoldmanuel.cookpedia;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.arnoldmanuel.cookpedia.adapters.IngredientesVerAdapter;
import com.arnoldmanuel.cookpedia.adapters.PasosRecetaVerAdapter;
import com.arnoldmanuel.cookpedia.fragments.MisRecetasFragment;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;
import com.arnoldmanuel.cookpedia.viewmodels.RecetaViewModel;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class VerRecetaActivity extends AppCompatActivity {

    private RecetaViewModel recetaViewModel;
    ImageView recetaImageView;
    TextView tituloTextView;
    TextView descripcionTextView;
    TextView tiempoTextView;
    TextView comensalesTextView;
    RecyclerView recyclerPasos;
    PasosRecetaVerAdapter pasoRecetaVerAdapter;
    RecyclerView recyclerIngredientes;
    IngredientesVerAdapter ingredientesVerAdapter;
    RecetaConIngredientesConPasos recetaConCosas;

    Button editarButton;
    Button borrarButton;

    public static int BORRAR_RECETA_RESULT = 401;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_receta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        recetaViewModel = new ViewModelProvider(this).get(RecetaViewModel.class);

        recetaConCosas = (RecetaConIngredientesConPasos) getIntent().getSerializableExtra("receta");
        recetaImageView = findViewById(R.id.imagen_receta);
        recetaImageView.setImageBitmap(BitmapFactory.decodeFile(recetaConCosas.receta.getImagen()));

        tituloTextView = findViewById(R.id.titulo_texto_view);
        tituloTextView.setText(recetaConCosas.receta.getTitulo());

        descripcionTextView = findViewById(R.id.descricpion_text_view);
        descripcionTextView.setText(recetaConCosas.receta.getDescripcion());

        tiempoTextView = findViewById(R.id.tiempo_text_view);
        tiempoTextView.setText(recetaConCosas.receta.getTiempo());

        comensalesTextView = findViewById(R.id.comensales_text_view);
        comensalesTextView.setText(recetaConCosas.receta.getComensales()+" personas");

        recyclerIngredientes = findViewById(R.id.ingredientes_list_recycler_view);

        ArrayList<String> textosIngredientes = new ArrayList<>();
        for (int i = 0; i < recetaConCosas.ingredientes.size();i++) {
            textosIngredientes.add(recetaConCosas.ingredientes.get(i).getTexto());
        }
        ingredientesVerAdapter = new IngredientesVerAdapter(this, textosIngredientes);//adapter
        recyclerIngredientes.setAdapter(ingredientesVerAdapter);
        recyclerIngredientes.setLayoutManager(new LinearLayoutManager(this));//ponemos layoutmanager

        ArrayList<String> textosPasos= new ArrayList<>();
        ArrayList<String[]> imagenesPasos = new ArrayList<>();
        for (int i = 0; i < recetaConCosas.pasosReceta.size(); i++) {
            textosPasos.add(recetaConCosas.pasosReceta.get(i).getTexto());
            imagenesPasos.add(new String[]{
                    recetaConCosas.pasosReceta.get(i).getImage1(),
                    recetaConCosas.pasosReceta.get(i).getImage2(),
                    recetaConCosas.pasosReceta.get(i).getImage3()
            });
        }
        recyclerPasos = findViewById(R.id.pasos_list_recycler_view);//recycler
        pasoRecetaVerAdapter = new PasosRecetaVerAdapter(this, textosPasos, imagenesPasos);//adapter
        recyclerPasos.setAdapter(pasoRecetaVerAdapter);//ponemos adapter
        recyclerPasos.setLayoutManager(new LinearLayoutManager(this));//ponemos layoutmanager

        editarButton = findViewById(R.id.editar_button);
        editarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarReceta();
            }
        });

        borrarButton = findViewById(R.id.borrar_button);
        borrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarReceta();
            }
        });

    }

    public void borrarReceta() {
        new MaterialAlertDialogBuilder(this)
                .setMessage("¿Estas seguro de que quieres borrar esta Receta?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.putExtra("receta", recetaConCosas);
                        setResult(BORRAR_RECETA_RESULT, intent);
                        finish();
                    }
                }).show();
    }

    public void editarReceta() {
        Intent intent = new Intent(this, CrearRecetaActivity.class);
        intent.putExtra("receta", recetaConCosas);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Bundle bundle = new Bundle();

                        RecetaConIngredientesConPasos recetaConCosas = (RecetaConIngredientesConPasos) data.getSerializableExtra("recetaConCosas");
                        RecetaConIngredientesConPasos recetaNuevo = (RecetaConIngredientesConPasos) data.getSerializableExtra("recetaNuevo");
                        recetaViewModel.updateReceta(recetaConCosas.receta);

                        recetaViewModel.deleteIngredientes(recetaConCosas.ingredientes);
                        recetaViewModel.insertIngredientes(recetaNuevo.ingredientes);

                        recetaViewModel.deletePasosReceta(recetaConCosas.pasosReceta);
                        recetaViewModel.insertPasosReceta(recetaNuevo.pasosReceta);
                        Intent intent = getIntent();
                        recetaNuevo.receta = recetaConCosas.receta;
                        intent.putExtra("receta", recetaNuevo);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(intent);
                        finish();

                    }
                }
            });

}