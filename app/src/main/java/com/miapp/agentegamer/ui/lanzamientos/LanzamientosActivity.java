package com.miapp.agentegamer.ui.lanzamientos;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.ui.common.BaseNavActivity;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.LanzamientosViewModel;

/**
 * LanzamientosActivity
 * --------------------
 * Pantalla que muestra los próximos lanzamientos de videojuegos.
 * Lista juegos que se lanzarán en los próximos días con información
 * de fecha, plataformas y rating.
 * 
 * Características:
 * - Muestra lista de lanzamientos próximos (próximos 15 días)
 * - Muestra días restantes hasta el lanzamiento
 * - Muestra rating del juego si está disponible
 * - Muestra plataformas disponibles
 * - Precarga datos desde la API al abrir la pantalla
 * - Navegación inferior para acceder a otras secciones
 * 
 * @see LanzamientosAdapter
 * @see LanzamientosViewModel
 */
@AndroidEntryPoint
public class LanzamientosActivity extends BaseNavActivity {

    // ViewModel para gestionar datos de lanzamientos
    private LanzamientosViewModel viewModel;
    // Adapter para el RecyclerView
    private LanzamientosAdapter adapter;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa el ViewModel, configura el RecyclerView con su adapter,
     * establece la navegación inferior y dispara la precarga de lanzamientos.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanzamientos);

        viewModel = new ViewModelProvider(this).get(LanzamientosViewModel.class);

        RecyclerView rv = findViewById(R.id.recyclerLanzamientos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LanzamientosAdapter();
        rv.setAdapter(adapter);

        setupBottomNavigation(R.id.nav_lanzamientos);

        // ViewModel from ViewModelProvider
        viewModel.getLanzamientos().observe(this, adapter::setLista);

        viewModel.precargaProximos15Dias();
    }
}
