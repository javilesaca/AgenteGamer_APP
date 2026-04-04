package com.miapp.agentegamer.ui.lanzamientos;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.ui.common.BaseNavActivity;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.LanzamientosViewModel;

@AndroidEntryPoint
public class LanzamientosActivity extends BaseNavActivity {

    private LanzamientosViewModel viewModel;
    private LanzamientosAdapter adapter;

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
