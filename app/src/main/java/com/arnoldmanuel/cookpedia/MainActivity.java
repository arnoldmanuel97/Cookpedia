package com.arnoldmanuel.cookpedia;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.arnoldmanuel.cookpedia.fragments.CrearRecetaFragment;
import com.arnoldmanuel.cookpedia.fragments.MisRecetasFragment;

public class MainActivity extends AppCompatActivity {

    private static final int CREAR_RECETA_REQUEST = 1;

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(MisRecetasFragment.newInstance("", ""));

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment instanceof CrearRecetaFragment) {
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        }
        if (fragment instanceof MisRecetasFragment) {
            transaction.setCustomAnimations(R.anim.slide_out_from_right, R.anim.slide_in_from_left);
        }
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.navigation_mis_recetas) {
                        openFragment(MisRecetasFragment.newInstance("", ""));
                        return true;
                    } else if (item.getItemId() == R.id.navigation_crear) {
                        openFragment(CrearRecetaFragment.newInstance("", ""));
                        return true;
                    }
                    return false;
                }
            };

}