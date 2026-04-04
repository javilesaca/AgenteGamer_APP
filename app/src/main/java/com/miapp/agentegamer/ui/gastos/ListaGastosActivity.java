package com.miapp.agentegamer.ui.gastos;

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
import com.miapp.agentegamer.data.model.UsuarioEntity;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.ui.common.BaseNavActivity;
import com.miapp.agentegamer.ui.viewmodel.GastoViewModel;
import com.miapp.agentegamer.util.MoneyUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * ListaGastosActivity
 * -------------------
 * Pantalla que muestra el historial de gastos del usuario.
 * Lista todos los gastos de juegos en un RecyclerView con posibilidad
 * de agregar nuevos gastos mediante un FloatingActionButton.
 * 
 * Características:
 * - Muestra lista de gastos con imagen, nombre, precio y fecha
 * - Calcula y muestra el total de gastos del período
 * - Permite agregar gastos de prueba (solo visible para admin)
 * - Navegación inferior para acceder a otras secciones
 * 
 * @see GastoAdapter
 * @see BaseNavActivity
 */
@AndroidEntryPoint
public class ListaGastosActivity extends BaseNavActivity {

    // ViewModel para gestionar datos de gastos
    private GastoViewModel gastoViewModel;
    // RecyclerView para mostrar la lista de gastos
    private RecyclerView recyclerView;
    // Adapter para el RecyclerView
    private GastoAdapter adapter;
    // FloatingActionButton para agregar gastos (solo admin)
    private FloatingActionButton fabAgregar;
    // Layout mostrado cuando no hay gastos
    private LinearLayout layoutEmpty;
    // TextView para mostrar el total de gastos
    private TextView tvTotal;
    // Moneda actual del usuario
    private String userCurrency = "EUR";

    @Inject
    UserRepository userRepository;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa el ViewModel, configura el RecyclerView con su adapter,
     * establece la navegación inferior, carga la moneda del usuario
     * y configura los observers para actualizar la UI con los datos de gastos.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
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

        setupBottomNavigation(R.id.nav_gastos);

        // Solo mostrar botón de gasto de prueba para administradores
        configurarVisibilidadBotonPrueba();

        // Cargar moneda del usuario para formatear totales
        cargarMonedaUsuario();

        // FAB click listener — gasto de prueba (solo visible para admin)
        fabAgregar.setOnClickListener(v -> {
            GastoEntity gastoPrueba = new GastoEntity(null, "Compra test", 19.99, System.currentTimeMillis(), null);
            gastoViewModel.insertar(gastoPrueba);
        });

        // Observe lista de gastos y mostrar/ocultar empty state
        gastoViewModel.getListaGastos().observe(this, gastos -> {
            adapter.setLista(gastos, userCurrency);
            if (gastos == null || gastos.isEmpty()) {
                layoutEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                tvTotal.setText("Total: " + MoneyUtils.format(0, userCurrency));
            } else {
                layoutEmpty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                double total = 0;
                for (GastoEntity gasto : gastos) {
                    total += gasto.getPrecio();
                }
                tvTotal.setText("Total: " + MoneyUtils.format(total, userCurrency));
            }
        });
    }

    /**
     * Carga la moneda configurada por el usuario desde Firestore.
     * Se usa para formatear el total de gastos con el símbolo correcto.
     */
    private void cargarMonedaUsuario() {
        userRepository.obtenerUsuario(new UserRepository.OnUsuarioCallback() {
            @Override
            public void onSuccess(UsuarioEntity usuario) {
                String moneda = usuario.getMoneda();
                if (moneda != null && !moneda.isEmpty()) {
                    userCurrency = moneda;
                }
            }

            @Override
            public void onError() {
                // Mantener EUR por defecto
            }
        });
    }

    /**
     * Oculta el botón de gasto de prueba para usuarios no-admin.
     * Solo los usuarios con rol "ADMIN" pueden ver el FAB de prueba.
     */
    private void configurarVisibilidadBotonPrueba() {
        // Ocultar por defecto hasta confirmar el rol
        fabAgregar.setVisibility(View.GONE);

        userRepository.obtenerUsuario(new UserRepository.OnUsuarioCallback() {
            @Override
            public void onSuccess(UsuarioEntity usuario) {
                if (isFinishing()) return;
                String rol = usuario.getRol();
                if ("ADMIN".equalsIgnoreCase(rol)) {
                    fabAgregar.setVisibility(View.VISIBLE);
                }
                // Si no es admin, queda GONE (ya establecido)
            }

            @Override
            public void onError() {
                // En caso de error, mantener oculto por seguridad
            }
        });
    }
}
