package com.miapp.agentegamer.ui.gastos;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.GastoViewModel;

@AndroidEntryPoint
public class ListaGastosActivity extends AppCompatActivity {

    private GastoViewModel gastoViewModel;
    private RecyclerView recyclerView;
    private GastoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        gastoViewModel = new ViewModelProvider(this).get(GastoViewModel.class);

        recyclerView = findViewById(R.id.recyclerGastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GastoAdapter();
        recyclerView.setAdapter(adapter);

        // Use the ViewModel from ViewModelProvider
        gastoViewModel.getListaGastos().observe(this, gastos -> {
            adapter.setLista(gastos);
        });

        // Botón de prueba
        Button btnAgregarPrueba = findViewById(R.id.btnAgregarPrueba);
        btnAgregarPrueba.setOnClickListener(v -> {
            GastoEntity gastoPrueba = new GastoEntity("Compra test", 19.99, System.currentTimeMillis());
            gastoViewModel.insertar(gastoPrueba);
        });
    }


}
