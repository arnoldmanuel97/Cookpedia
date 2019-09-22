package com.arnoldmanuel.cookpedia;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arnoldmanuel.cookpedia.adapters.IngredientesAdapter;
import com.arnoldmanuel.cookpedia.adapters.PasoRecetaAdapter;
import com.arnoldmanuel.cookpedia.model.Ingrediente;
import com.arnoldmanuel.cookpedia.model.PasoReceta;
import com.arnoldmanuel.cookpedia.model.Receta;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;
import com.arnoldmanuel.cookpedia.viewmodels.CrearRecetaViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class CrearRecetaActivity extends AppCompatActivity {

    RelativeLayout previewImage;
    LinearLayout imagenLayoutBottom;
    ImageView imagenReceta;
    EditText tituloEditText;
    EditText descripcionEditText;
    EditText comensalesEditText;
    EditText tiempoEditText;
    Button addButton;

    RecyclerView recyclerIngredientes;
    IngredientesAdapter ingredientesAdapter;
    ArrayList<String> textosIngredientes = new ArrayList<>();
    Button ingredientesButton;

    static public int SELECCIONA_IMAGEN_PASO = 201;

    RecyclerView recyclerPasoReceta;
    PasoRecetaAdapter pasoRecetaAdapter;
    ArrayList<String> textosPasos = new ArrayList<>();
    ArrayList<Bitmap[]> bitmapsPasos = new ArrayList<>();
    Button pasoButton;
    CrearRecetaViewModel viewModel;
    RecetaConIngredientesConPasos recetaConCosas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewModel = new ViewModelProvider(this).get(CrearRecetaViewModel.class);
        recetaConCosas = (RecetaConIngredientesConPasos) getIntent().getSerializableExtra("receta");
        if (recetaConCosas != null && viewModel.recetaConCosas == null) {
            viewModel.setState(recetaConCosas);
        }

        imagenReceta = findViewById(R.id.imagen_receta);//inicializamos imagen
        imagenLayoutBottom = findViewById(R.id.imagen_layout_bottom);
        if(viewModel.photo != null) {
            imagenReceta.setImageBitmap(viewModel.photo);
            imagenLayoutBottom.setVisibility(View.GONE);
        }

        //Ponemos listener al boton de imagen de la receta para abrir galeria
        previewImage = findViewById(R.id.preview_image);
        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        tituloEditText = findViewById(R.id.titulo_edit_text);
        descripcionEditText = findViewById(R.id.descripcion_edit_text);
        comensalesEditText = findViewById(R.id.comensales_edit_text);
        tiempoEditText = findViewById(R.id.tiempo_edit_text);
        if(viewModel != null) {
            tituloEditText.setText(viewModel.titulo);
            descripcionEditText.setText(viewModel.descripcion);
            comensalesEditText.setText(viewModel.comensales);
            tiempoEditText.setText(viewModel.tiempo);
        }
        addButton = findViewById(R.id.toolbarbtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (recetaConCosas != null) {
                        updateReceta();
                    } else {
                        saveReceta();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //RecyclerView de ingredientes
        textosIngredientes.add("");//Añadimos dos elementos vacios
        textosIngredientes.add("");
        if (!viewModel.textosIngredientes.isEmpty()){
            textosIngredientes.clear();
            textosIngredientes = viewModel.textosIngredientes;
        }
        recyclerIngredientes = findViewById(R.id.recycler_ingredientes);//recycler
        ingredientesAdapter = new IngredientesAdapter(this, textosIngredientes);//adapter
        recyclerIngredientes.setAdapter(ingredientesAdapter);//ponemos adapter
        recyclerIngredientes.setLayoutManager(new LinearLayoutManager(this));//ponemos layoutmanager

        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(simpleCallbackIngredientes);//itemtouchhelper para ingredientes
        itemTouchHelper1.attachToRecyclerView(recyclerIngredientes);//ponemos el itemtouchhelper a ingredientes

        //RecyclerView de ingredientes
        textosPasos.add("");
        textosPasos.add("");
        bitmapsPasos.add(new Bitmap[]{null, null, null});
        bitmapsPasos.add(new Bitmap[]{null, null, null});
        if (!viewModel.textosPasos.isEmpty()){
            textosPasos.clear();
            textosPasos = viewModel.textosPasos;
        }
        if (!viewModel.bitmapsPasos.isEmpty()){
            bitmapsPasos.clear();
            bitmapsPasos = viewModel.bitmapsPasos;
        }
        recyclerPasoReceta = findViewById(R.id.recycler_pasos);//recycler
        pasoRecetaAdapter = new PasoRecetaAdapter(this, textosPasos, bitmapsPasos);//adapter
        recyclerPasoReceta.setAdapter(pasoRecetaAdapter);//ponemos adapter
        recyclerPasoReceta.setLayoutManager(new LinearLayoutManager(this));//ponemos layoutmanager

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleCallbackPasos);//itemtouchhelper para ingredientes
        itemTouchHelper2.attachToRecyclerView(recyclerPasoReceta);//ponemos el itemtouchhelper a ingredientes

        //Ponemos listener al boton de ingrediente para añadir un item
        ingredientesButton = findViewById(R.id.ingredientes_button);
        ingredientesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textosIngredientes.add("");
                ingredientesAdapter.notifyDataSetChanged();
            }
        });

        pasoButton = findViewById(R.id.paso_button);
        pasoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textosPasos.add("");
                bitmapsPasos.add(new Bitmap[]{null, null, null});
                pasoRecetaAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Both navigation bar back press and title bar back press will trigger this method
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    public void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(Intent.createChooser(i, "Selecciona la imagen"));
    }

    private void saveReceta() throws Exception {
        String imagen = null;
        String titulo = tituloEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String comensales = comensalesEditText.getText().toString().trim();
        String tiempo = tiempoEditText.getText().toString().trim();
        if (titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor introduce un título y una descripción",Toast.LENGTH_LONG).show();
            return;
        }
        if (imagenReceta.getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable draw = (BitmapDrawable) imagenReceta.getDrawable();
            Bitmap bitmap = draw.getBitmap();
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File directory = cw.getDir("CookpediaImages", Context.MODE_PRIVATE);
            File outFile = new File(directory, fileName);
            FileOutputStream outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            imagen = outFile.getAbsolutePath();
        }
        FileOutputStream outStream1 = null;
        ArrayList<String[]> imagenesPasos = new ArrayList<>();
        for (int i = 0; i < textosPasos.size(); i++) {
            imagenesPasos.add(new String[]{null, null, null});
            for (int j = 0; j < bitmapsPasos.get(i).length; j++) {
                Bitmap bitmap1;
                if ((bitmap1 = bitmapsPasos.get(i)[j]) != null) {
                    ContextWrapper cw1 = new ContextWrapper(getApplicationContext());
                    String fileName1 = String.format("%d.jpg", System.currentTimeMillis());
                    File directory1 = cw1.getDir("CookpediaImages", Context.MODE_PRIVATE);
                    File outFile1 = new File(directory1, fileName1);
                    outStream1 = new FileOutputStream(outFile1);
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outStream1);
                    outStream1.flush();
                    imagenesPasos.get(i)[j] = outFile1.getAbsolutePath();
                }
            }
        }
        if (outStream1!=null) {
            outStream1.close();
        }
        Intent data = new Intent();
        Receta receta = new Receta(imagen, titulo, descripcion, comensales, tiempo);
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();
        ArrayList<PasoReceta> pasos = new ArrayList<>();
        for (String textoIngrediente: textosIngredientes) {
            ingredientes.add(new Ingrediente(textoIngrediente));
        }

        for (int i = 0 ;i < textosPasos.size(); i++) {
            pasos.add(new PasoReceta(textosPasos.get(i),
                    imagenesPasos.get(i)[0],
                    imagenesPasos.get(i)[1],
                    imagenesPasos.get(i)[2]));
        }

        RecetaConIngredientesConPasos recetaConCosas = new RecetaConIngredientesConPasos(receta, ingredientes, pasos);
        data.putExtra("recetaConCosas", recetaConCosas);
        setResult(RESULT_OK, data);
        finish();
    }

    private void updateReceta() throws Exception {
        String imagen = viewModel.oldPathPhoto;
        String titulo = tituloEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String comensales = comensalesEditText.getText().toString().trim();
        String tiempo = tiempoEditText.getText().toString().trim();
        if (titulo.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor introduce un título y una descripción",Toast.LENGTH_LONG).show();
            return;
        }
        if (viewModel.photo!= null) {
            BitmapDrawable draw = (BitmapDrawable) imagenReceta.getDrawable();
            Bitmap bitmap = draw.getBitmap();
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File directory = cw.getDir("CookpediaImages", Context.MODE_PRIVATE);
            File outFile = new File(directory, fileName);
            FileOutputStream outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            imagen = outFile.getAbsolutePath();
            if (viewModel.oldPhoto != null) {
                new File(viewModel.oldPathPhoto).delete();
            }
        }

        FileOutputStream outStream1 = null;

        for (int i = 0; i < viewModel.oldBitmapsPasos.size(); i++) {
            for (int j = 0; j < viewModel.oldBitmapsPasos.get(i).length; j++) {
                if (viewModel.oldBitmapsPasos.get(i)[j]!= null) {
                    new File(viewModel.oldPathPasos.get(i)[j]).delete();
                }
            }
        }
        ArrayList<String[]> imagenesPasos = new ArrayList<>();
        for (int i = 0; i < textosPasos.size(); i++) {
            imagenesPasos.add(new String[]{null, null, null});
            for (int j = 0; j < bitmapsPasos.get(i).length; j++) {
                Bitmap bitmap1 = bitmapsPasos.get(i)[j];
                if (bitmap1 != null) {
                    ContextWrapper cw1 = new ContextWrapper(getApplicationContext());
                    String fileName1 = String.format("%d.jpg", System.currentTimeMillis());
                    File directory1 = cw1.getDir("CookpediaImages", Context.MODE_PRIVATE);
                    File outFile1 = new File(directory1, fileName1);
                    outStream1 = new FileOutputStream(outFile1);
                    bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, outStream1);
                    outStream1.flush();
                    imagenesPasos.get(i)[j] = outFile1.getAbsolutePath();
                }
            }
        }
        if (outStream1!=null) {
            outStream1.close();
        }
        Intent data = new Intent();
        int id = recetaConCosas.receta.getRecetaId();
        this.recetaConCosas.receta.setImagen(imagen);
        this.recetaConCosas.receta.setTitulo(titulo);
        this.recetaConCosas.receta.setDescripcion(descripcion);
        this.recetaConCosas.receta.setComensales(comensales);
        this.recetaConCosas.receta.setTiempo(tiempo);

        ArrayList<Ingrediente> ingredientesNuevo = new ArrayList<>();
        for (int i = 0; i < textosIngredientes.size(); i++) {
            Ingrediente ingrediente = new Ingrediente(textosIngredientes.get(i), id);
            ingredientesNuevo.add(ingrediente);
        }

        ArrayList<PasoReceta> pasoRecetasNuevo = new ArrayList<>();

        for (int i = 0 ; i < textosPasos.size(); i++) {
            PasoReceta pasoReceta = new PasoReceta(textosPasos.get(i),
                    imagenesPasos.get(i)[0],
                    imagenesPasos.get(i)[1],
                    imagenesPasos.get(i)[2], id);
            pasoRecetasNuevo.add(pasoReceta);
        }
        RecetaConIngredientesConPasos recetaNuevo = new RecetaConIngredientesConPasos();
        recetaNuevo.ingredientes = ingredientesNuevo;
        recetaNuevo.pasosReceta = pasoRecetasNuevo;

        data.putExtra("recetaConCosas", recetaConCosas);
        data.putExtra("recetaNuevo", recetaNuevo);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        viewModel.setTextosIngredientes(textosIngredientes);
        viewModel.setTextosPasos(textosPasos);
        viewModel.setBitmapsPasos(bitmapsPasos);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECCIONA_IMAGEN_PASO) {
                InputStream in = null;
                try {
                    in = getContentResolver().openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap photo = BitmapFactory.decodeStream(in);
                int adapterPosition = pasoRecetaAdapter.lastAdapterPosition;
                int imagePosition = pasoRecetaAdapter.lastImagePosition;
                if (in != null) {
                    bitmapsPasos.get(adapterPosition)[imagePosition] = photo;
                    pasoRecetaAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        try {
                            InputStream in = getContentResolver().openInputStream(data.getData());
                            Bitmap imagen = BitmapFactory.decodeStream(in);
                            if (null != imagen) {
                                viewModel.setPhoto(imagen);
                                imagenLayoutBottom.setVisibility(View.GONE);
                                imagenReceta.setImageBitmap(imagen);
                                imagenReceta.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    ItemTouchHelper.SimpleCallback simpleCallbackIngredientes =  new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(textosIngredientes, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    ItemTouchHelper.SimpleCallback simpleCallbackPasos =  new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(textosPasos, fromPosition, toPosition);
            Collections.swap(bitmapsPasos, fromPosition, toPosition);
            ((PasoRecetaAdapter.PasoRecetaViewHolder)viewHolder).pasoTextNumber.setText(String.valueOf(toPosition+1));
            ((PasoRecetaAdapter.PasoRecetaViewHolder)target).pasoTextNumber.setText(String.valueOf(fromPosition+1));
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}