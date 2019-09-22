package com.arnoldmanuel.cookpedia.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.arnoldmanuel.cookpedia.CrearRecetaActivity;
import com.arnoldmanuel.cookpedia.R;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;
import com.arnoldmanuel.cookpedia.viewmodels.RecetaViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearRecetaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearRecetaFragment extends Fragment {

    private RecetaViewModel recetaViewModel;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button buttonCrearReceta;
    static public int CREAR_RECETA = 301;


    public CrearRecetaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearRecetaFragment newInstance(String param1, String param2) {
        CrearRecetaFragment fragment = new CrearRecetaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_crear, container, false);
        buttonCrearReceta = rootView.findViewById(R.id.button_crear_receta);
        recetaViewModel = new ViewModelProvider(this).get(RecetaViewModel.class);

        buttonCrearReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearReceta();
            }
        });
        return rootView;

    }

    public void crearReceta() {
        Intent intent = new Intent(getContext(), CrearRecetaActivity.class);
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
                        RecetaConIngredientesConPasos recetaConCosas = (RecetaConIngredientesConPasos) data.getSerializableExtra("recetaConCosas");
                        long id = recetaViewModel.insertReceta(recetaConCosas.receta);

                        for (int i = 0; i < recetaConCosas.ingredientes.size(); i++) {
                            recetaConCosas.ingredientes.get(i).setRecetaIngredienteId((int) id);
                        }

                        recetaViewModel.insertIngredientes(recetaConCosas.ingredientes);

                        for (int i = 0; i < recetaConCosas.pasosReceta.size(); i++) {
                            recetaConCosas.pasosReceta.get(i).setRecetaPasoRecetaId((int) id);
                        }

                        recetaViewModel.insertPasosReceta(recetaConCosas.pasosReceta);
                    }
                }
            });


}