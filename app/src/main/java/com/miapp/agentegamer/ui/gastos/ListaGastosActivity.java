package com.miapp.agentegamer.ui.gastos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.viewmodel.GastoViewModel;

@AndroidEntryPoint
public class ListaGastosActivity extends AppCompatActivity {

    private GastoViewModel gastoViewModel;
    private RecyclerView recyclerView;
    private GastoAdapter adapter;
    private FloatingActionButton fabAgregar;
    private LinearLayout layoutEmpty;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos);

        gastoViewModel = new ViewModelProvider(this).get(GastoViewModel.class);

        recyclerView = findViewById(R.id.recyclerGastos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutEmpty = findViewById(R.id.layoutEmpty);
        fabAgregar = findViewById(R.id.fabAgregar);
        tvTotal = findViewById(R.id.tvTotal);

        adapter = new GastoAdapter();
        recyclerView.setAdapter(adapter);

        // FAB click listener
        fabAgregar.setOnClickListener(v -> {
            GastoEntity gastoPrueba = new GastoEntity("Compra test", 19.99, System.currentTimeMillis());
            gastoViewModel.insertar(gastoPrueba);
        });

        // Observe lista de gastos y mostrar/ocultar empty state
        gastoViewModel.getListaGastos().observe(this, gastos -> {
            adapter.setLista(gastos);
            if (gastos == null || gastos.isEmpty()) {
                layoutEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                tvTotal.setText("Total: $0.00");
            } else {
                layoutEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                double total = 0;
                for (GastoEntity gasto : gastos) {
                    total += gasto.getMonto();
                }
                tvTotal.setText(String.format("Total: $%.2f", total));
            }
        });
    }


}
