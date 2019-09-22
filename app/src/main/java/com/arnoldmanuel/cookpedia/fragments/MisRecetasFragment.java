package com.arnoldmanuel.cookpedia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arnoldmanuel.cookpedia.R;
import com.arnoldmanuel.cookpedia.VerRecetaActivity;
import com.arnoldmanuel.cookpedia.adapters.RecetasAdapter;
import com.arnoldmanuel.cookpedia.model.RecetaConIngredientesConPasos;
import com.arnoldmanuel.cookpedia.viewmodels.RecetaViewModel;

import java.io.File;
import java.util.List;

import static com.arnoldmanuel.cookpedia.VerRecetaActivity.BORRAR_RECETA_RESULT;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisRecetasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisRecetasFragment extends Fragment
        implements RecetasAdapter.OnRecetaListener, SearchView.OnQueryTextListener {

    private RecetaViewModel recetaViewModel;
    private RecyclerView recyclerRecetas;
    private RecetasAdapter adapter;
    ConstraintLayout misrecetasMensaje;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MisRecetasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MisRecetasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MisRecetasFragment newInstance(String param1, String param2) {
        MisRecetasFragment fragment = new MisRecetasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mis_recetas, container, false);
        recyclerRecetas = rootView.findViewById(R.id.recycler_recetas);
        recyclerRecetas.setLayoutManager(new GridLayoutManager(rootView.getContext(),2));
        adapter = new RecetasAdapter(rootView.getContext(),this);
        recyclerRecetas.setAdapter(adapter);

        misrecetasMensaje = rootView.findViewById(R.id.misrecetas_mensaje);
        misrecetasMensaje.setVisibility(View.GONE);

        recetaViewModel = new ViewModelProvider(this).get(RecetaViewModel.class);
        recetaViewModel.getAllRecetas().observe(getViewLifecycleOwner(),
                new Observer<List<RecetaConIngredientesConPasos>>() {
                    @Override
                    public void onChanged(List<RecetaConIngredientesConPasos> recetasOnChanged) {
                        adapter.setRecetas(recetasOnChanged);
                        if (recetasOnChanged.isEmpty()) {
                            misrecetasMensaje.setVisibility(View.VISIBLE);
                        }
                    }
                });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_mis_recetas_search, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onRecetaClick(int position) {
        Intent intent = new Intent(requireActivity(), VerRecetaActivity.class);
        RecetaConIngredientesConPasos recetaConCosas = adapter.getRecetas().get(position);
        intent.putExtra("receta", recetaConCosas);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == BORRAR_RECETA_RESULT) {
                        // There are no request codes
                        Intent data = result.getData();
                        RecetaConIngredientesConPasos recetaConCosas = (RecetaConIngredientesConPasos) data.getSerializableExtra("receta");
                        recetaViewModel.deleteReceta(recetaConCosas.receta);
                        if (recetaConCosas.receta.getImagen() != null) {
                            new File(recetaConCosas.receta.getImagen()).delete();
                        }
                        recetaViewModel.deleteIngredientes(recetaConCosas.ingredientes);
                        for (int i = 0; i < recetaConCosas.pasosReceta.size(); i++) {
                            if (recetaConCosas.pasosReceta.get(i).getImage1() != null) {
                                new File(recetaConCosas.pasosReceta.get(i).getImage1()).delete();
                            }
                            if (recetaConCosas.pasosReceta.get(i).getImage2() != null) {
                                new File(recetaConCosas.pasosReceta.get(i).getImage2()).delete();
                            }
                            if (recetaConCosas.pasosReceta.get(i).getImage3() != null) {
                                new File(recetaConCosas.pasosReceta.get(i).getImage3()).delete();
                            }
                        }
                        recetaViewModel.deletePasosReceta(recetaConCosas.pasosReceta);
                        Toast.makeText(requireActivity(), "Se ha borrado la receta", Toast.LENGTH_LONG).show();
                    }
                }
            });

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query != null) {
            getRecetaByTitulo(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText != null) {
            getRecetaByTitulo(newText);
        }
        return true;
    }

    private void getRecetaByTitulo(String search) {
        String searchQuery = "%"+search+"%";
        recetaViewModel.getRecetaByTitulo(searchQuery).observe(this,
                new Observer<List<RecetaConIngredientesConPasos>>() {
                    @Override
                    public void onChanged(List<RecetaConIngredientesConPasos> receatas) {
                        adapter.setRecetas(receatas);
                    }
                });
    }
}