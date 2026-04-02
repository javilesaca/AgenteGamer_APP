package com.miapp.agentegamer.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.model.UsuarioEntity;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;
import com.miapp.agentegamer.domain.model.MonthlyExpense;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.domain.repository.GastoRepository;
import com.miapp.agentegamer.ui.ajustes.AjustesActivity;
import com.miapp.agentegamer.ui.auth.LoginActivity;
import com.miapp.agentegamer.ui.games.ListaJuegosActivity;
import com.miapp.agentegamer.ui.gastos.ListaGastosActivity;
import com.miapp.agentegamer.ui.lanzamientos.LanzamientosActivity;
import com.miapp.agentegamer.ui.perfil.PerfilActivity;
import com.miapp.agentegamer.ui.wishlist.ListaWishlistActivity;
import com.miapp.agentegamer.ui.adapter.UltimosGastosAdapter;
import com.miapp.agentegamer.util.MoneyUtils;
import com.miapp.agentegamer.util.FinancialTrendHelper;
import com.miapp.agentegamer.ui.viewmodel.GastoViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * MainActivity
 * --------------
 * Pantalla principal de la aplicación.
 * Muestra el estado financiero del usuario, gastos acumulados
 * y permite navegar al resto de funcionalidades.
 */
@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    // UI
    protected TextView tvRecomendacion, tvTotalGastos, tvPresupuesto, tvRestante;
    private View indicadorEstado;
    private PieChart pieChart;
    
    // Nuevos componentes Dashboard
    private LineChart lineChartTendencia;
    private TextView tvRecomendacionAgente;
    private TextView tvIndicadorTendencia;
    private RecyclerView recyclerUltimosGastos;
    private UltimosGastosAdapter ultimosGastosAdapter;

    //Drawer
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    // Quick Actions
    private MaterialCardView btnAddGasto, btnWishlist, btnJuegos, btnAjustes;

    // MVVM
    private GastoViewModel gastoViewModel;

    // Dominio
    private SistemaFinanciero sistemaFinanciero;
    private String moneda = "EUR";

    @Inject
    UserRepository userRepository;
    
    @Inject
    GastoRepository gastoRepo;

    /**
     * Se ejecuta cada vez que la Activity pasa a primer plano.
     * Verificamos que el usuario esté autenticado.
     */
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    /**
     * Inicialización de la Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View root = findViewById(android.R.id.content);
        root.setAlpha(0f);
        root.setTranslationY(40f);

        root.animate()
                .alpha(1f)
                        .translationY(0f)
                                .setDuration(400)
                                        .setInterpolator(new android.view.animation.DecelerateInterpolator())
                                                .start();

        inicializarVistas();
        configurarDrawer();
        configurarHeaderDrawer();
        inicializarViewModel();
        cargarPresupuestoUsuario();
        configurarObservers();
        observarMoneda();
        configurarMenu();

        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                            drawerLayout.closeDrawer(GravityCompat.START);
                        } else {
                            setEnabled(false);
                            getOnBackPressedDispatcher().onBackPressed();
                        }
                    }
                }
        );
    }

    /**
     * En caso de actualizar nombre/email dede la Activity de Perfil
     */
    @Override
    public void onResume(){
        super.onResume();
        configurarHeaderDrawer();
        
        // Actualizar presupuesto restante cada vez que se muestra la pantalla
        actualizarPresupuestoRestante();
    }
    
    private void actualizarPresupuestoRestante() {
        // Calcular restante usando Room directamente (fuente de verdad única)
        gastoRepo.getTotalGastadoMesSync(totalGastado -> {
            runOnUiThread(() -> {
                double presupuesto = sistemaFinanciero != null 
                    ? sistemaFinanciero.getPresupuestoMensual() : 0;
                double restante = presupuesto - totalGastado;
                tvTotalGastos.setText(MoneyUtils.format(totalGastado, moneda));
                tvRestante.setText(MoneyUtils.format(restante, moneda));
            });
        });
    }

    /**
     * Referencias a vistas del layout.
     */
    private void inicializarVistas() {

        tvRecomendacion = findViewById(R.id.tvRecomendacion);
        tvTotalGastos = findViewById(R.id.tvTotalGastos);
        tvPresupuesto = findViewById(R.id.tvPresupuesto);
        tvRestante = findViewById(R.id.tvRestante);
        indicadorEstado = findViewById(R.id.viewIndicador);
        pieChart = findViewById(R.id.pieChart);
        
        // Nuevos componentes Dashboard
        lineChartTendencia = findViewById(R.id.lineChartTendencia);
        tvRecomendacionAgente = findViewById(R.id.tvRecomendacionAgente);
        tvIndicadorTendencia = findViewById(R.id.tvIndicadorTendencia);
        recyclerUltimosGastos = findViewById(R.id.recyclerUltimosGastos);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        // Quick Actions
        btnAddGasto = findViewById(R.id.btnAddGasto);
        btnWishlist = findViewById(R.id.btnWishlist);
        btnJuegos = findViewById(R.id.btnJuegos);
        btnAjustes = findViewById(R.id.btnAjustes);

        configurarQuickActions();
        configurarDashboardComponents();
    }

    private void configurarQuickActions() {
        btnAddGasto.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListaGastosActivity.class));
        });

        btnWishlist.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListaWishlistActivity.class));
        });

        btnJuegos.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListaJuegosActivity.class));
        });

        btnAjustes.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AjustesActivity.class));
        });
    }
    
    /**
     * Configura los nuevos componentes del Dashboard.
     */
    private void configurarDashboardComponents() {
        // Configurar LineChart para tendencias
        configurarLineChart();
        
        // Configurar RecyclerView de últimos gastos
        configurarRecyclerUltimosGastos();
    }
    
    /**
     * Configura el LineChart para mostrar tendencias de gastos.
     */
    private void configurarLineChart() {
        lineChartTendencia.getDescription().setEnabled(false);
        lineChartTendencia.getLegend().setEnabled(false);
        lineChartTendencia.setTouchEnabled(true);
        lineChartTendencia.setDragEnabled(false);
        lineChartTendencia.setScaleEnabled(false);
        lineChartTendencia.setPinchZoom(false);
        
        // X-Axis
        XAxis xAxis = lineChartTendencia.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(getColor(R.color.text_hint));
        xAxis.setTextSize(10f);
        
        // Y-Axis (oculto)
        lineChartTendencia.getAxisLeft().setEnabled(false);
        lineChartTendencia.getAxisRight().setEnabled(false);
        
        // Sin grid
        lineChartTendencia.getAxisLeft().setDrawGridLines(false);
        lineChartTendencia.getAxisRight().setDrawGridLines(false);
        
        // Margen
        lineChartTendencia.setExtraOffsets(8f, 8f, 8f, 8f);
    }
    
    /**
     * Configura el RecyclerView horizontal de últimos gastos.
     */
    private void configurarRecyclerUltimosGastos() {
        ultimosGastosAdapter = new UltimosGastosAdapter();
        recyclerUltimosGastos.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerUltimosGastos.setAdapter(ultimosGastosAdapter);
        
        // Click listener para navegar a ListaGastos
        ultimosGastosAdapter.setOnGastoClickListener(gasto -> {
            startActivity(new Intent(MainActivity.this, ListaGastosActivity.class));
        });
    }

    /**
     * Configura el Drawer
     */
    private void configurarDrawer() {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Configura el HeaderDrawer
     */
    private void configurarHeaderDrawer(){

        View header = navigationView.getHeaderView(0);

        TextView tvNombre = header.findViewById(R.id.tvNombreUsuario);
        TextView tvEmail = header.findViewById(R.id.tvEmailUsuario);
        TextView tvFecha = header.findViewById(R.id.tvFechaCreacion);
        TextView tvRol = header.findViewById(R.id.tvRol);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            tvEmail.setText(user.getEmail());
            // Fetch latest nombre from Firestore to keep consistency with PerfilActivity
            userRepository.obtenerUsuario(new UserRepository.OnUsuarioCallback() {
                @Override
                public void onSuccess(UsuarioEntity usuario) {
                    tvNombre.setText(usuario.getNombre());
                    tvRol.setText(usuario.getRol() != null ? usuario.getRol() : "Usuario");
                    // Format fechaCreacion
                    if (usuario.getFechaCreacion() != null) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault());
                        String fechaStr = sdf.format(new java.util.Date(usuario.getFechaCreacion().getSeconds() * 1000));
                        tvFecha.setText(fechaStr);
                    }
                }

                @Override
                public void onError() {
                    // Fallback to Firebase Auth display name if Firestore fetch fails
                    if (user.getDisplayName() != null) {
                        tvNombre.setText(user.getDisplayName());
                    } else {
                        tvNombre.setText("Usuario desconocido");
                    }
                    tvRol.setText("Usuario");
                }
            });
        } else {
            tvNombre.setText("Usuario desconocido");
            tvRol.setText("Usuario");
        }
    }

    /**
     * Inicializa el ViewModel.
     */
    private void inicializarViewModel() {
        gastoViewModel = new ViewModelProvider(this).get(GastoViewModel.class);
    }

    /**
     * Obtiene el presupuesto del usuario desde Firestore
     * y lo inyecta en el ViewModel.
     */
    private void cargarPresupuestoUsuario() {
        // Usar LiveData para que se actualice automáticamente cuando cambie el presupuesto
        userRepository.getPresupuestoLiveData().observe(this, presupuesto -> {
            if (presupuesto != null) {
                sistemaFinanciero = new SistemaFinanciero(presupuesto);
                gastoViewModel.setSistemaFinanciero(sistemaFinanciero);

                // Actualizar el TextView con el presupuesto
                tvPresupuesto.setText(MoneyUtils.format(presupuesto, moneda));

                // También actualizar restante cuando cambie el presupuesto
                gastoRepo.getTotalGastadoMesSync(totalGastado -> {
                    runOnUiThread(() -> {
                        double restante = presupuesto - totalGastado;
                        tvTotalGastos.setText(MoneyUtils.format(totalGastado, moneda));
                        tvRestante.setText(MoneyUtils.format(restante, moneda));
                    });
                });
            }
        });
    }


    /**
     * Observers del ViewModel.
     * La UI reacciona automáticamente a los cambios de datos.
     */
    private void configurarObservers() {

        // Estado financiero + recomendación
        gastoViewModel.getEstadoUI().observe(this, ui -> {

            tvRecomendacion.setText(ui.getMensaje());
            
            // Actualizar recomendación del agente
            actualizarRecomendacionAgente();

            indicadorEstado.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(150)
                    .withEndAction(() -> {
                        indicadorEstado.setBackgroundColor(getColor(ui.getColorRes()));

                        indicadorEstado.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(150)
                                .start();
                    })
                    .start();
        });

        // Lista de gastos → total + gráfico
        gastoViewModel.getListaGastos().observe(this, gastos -> {

            double total = 0;
            List<PieEntry> entradas = new ArrayList<>();

            for (GastoEntity gasto : gastos) {
                total += gasto.getPrecio();
                entradas.add(
                        new PieEntry(
                                (float) gasto.getPrecio(),
                                gasto.getId()
                        )
                );
            }

            // Calcular restante directamente: presupuesto - total gastado
            double presupuesto = sistemaFinanciero != null ? sistemaFinanciero.getPresupuestoMensual() : 0;
            double restante = presupuesto - total;
            
            tvTotalGastos.setText(MoneyUtils.format(total, moneda));
            tvPresupuesto.setText(MoneyUtils.format(presupuesto, moneda));
            tvRestante.setText(MoneyUtils.format(restante, moneda));

            animarTotal(total);

            PieDataSet dataSet = new PieDataSet(entradas, "");
            dataSet.setColors(
                    getColor(R.color.piechart_midnightblue),
                    getColor(R.color.piechart_slategray),
                    getColor(R.color.piechart_verde),
                    getColor(R.color.piechart_amarillo),
                    getColor(R.color.piechart_rojo),
                    getColor(R.color.piechart_lightblue)
            );
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(8f);
            dataSet.setValueTextSize(12f);
            dataSet.setValueTextColor(Color.WHITE);

            pieChart.setData(new PieData(dataSet));
            pieChart.setUsePercentValues(false);
            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleRadius(65f);
            pieChart.setTransparentCircleRadius(70f);
            pieChart.setCenterText("Gastos");
            pieChart.setCenterTextSize(16f);
            pieChart.setEntryLabelColor(Color.WHITE);
            pieChart.getLegend().setEnabled(false);
            pieChart.getDescription().setEnabled(false);
            pieChart.setDrawEntryLabels(false);
            pieChart.animateY(800);
            pieChart.invalidate();
        });
        
        // ========== NUEVOS OBSERVERS PARA DASHBOARD ==========
        
        // Observer para gastos mensuales (gráfico de tendencia)
        gastoViewModel.getMonthlyExpenses().observe(this, monthlyExpenses -> {
            if (monthlyExpenses != null && !monthlyExpenses.isEmpty()) {
                actualizarLineChart(monthlyExpenses);
            }
        });
        
        // Observer para resultado de tendencia
        gastoViewModel.getTrendResult().observe(this, trendResult -> {
            if (trendResult != null) {
                actualizarIndicadorTendencia(trendResult);
            }
        });
        
        // Observer para últimos gastos
        gastoViewModel.getRecentGastos().observe(this, recentGastos -> {
            if (ultimosGastosAdapter != null) {
                ultimosGastosAdapter.setMoneda(moneda);
                ultimosGastosAdapter.setGastos(recentGastos);
            }
        });
    }
    
    /**
     * Actualiza el LineChart con los datos de gastos mensuales.
     */
    private void actualizarLineChart(List<MonthlyExpense> expenses) {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        
        for (int i = 0; i < expenses.size(); i++) {
            MonthlyExpense expense = expenses.get(i);
            entries.add(new Entry(i, (float) expense.getTotal()));
            labels.add(expense.getMes());
        }
        
        LineDataSet dataSet = new LineDataSet(entries, "Gastos");
        dataSet.setColor(getColor(R.color.accent_green));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(6f);
        dataSet.setCircleColor(getColor(R.color.accent_green));
        dataSet.setFillColor(getColor(R.color.accent_green));
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(30);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setValueTextSize(10f);
        dataSet.setValueTextColor(getColor(R.color.text_primary));
        dataSet.setDrawValues(true);
        
        // Configurar eje X con labels de meses
        lineChartTendencia.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < labels.size()) {
                    return labels.get(index);
                }
                return "";
            }
        });
        
        lineChartTendencia.setData(new LineData(dataSet));
        lineChartTendencia.animateX(800);
        lineChartTendencia.invalidate();
    }
    
    /**
     * Actualiza el indicador de tendencia en el Card Total.
     */
    private void actualizarIndicadorTendencia(FinancialTrendHelper.TrendResult trend) {
        String text = trend.symbol + " " + String.format("%.1f%%", trend.percentage);
        tvIndicadorTendencia.setText(text);
        tvIndicadorTendencia.setTextColor(getColor(trend.colorResId));
        tvIndicadorTendencia.setVisibility(View.VISIBLE);
        
        // Animación de entrada
        tvIndicadorTendencia.setAlpha(0f);
        tvIndicadorTendencia.animate()
                .alpha(1f)
                .setDuration(300)
                .start();
    }
    
    /**
     * Actualiza la recomendación del agente.
     */
    private void actualizarRecomendacionAgente() {
        String recomendacion = gastoViewModel.getRecomendacionDashboard();
        tvRecomendacionAgente.setText(recomendacion);
    }

    /**
     * Observa la moneda seleccionada por el usuario.
     */
    private void observarMoneda() {
        userRepository.getMonedaLiveData().observe(this, currency -> {
            moneda = currency != null ? currency : "EUR";
            refreshAdaptersWithCurrency();
        });
    }

    /**
     * Refresca adapters y TextViews con la moneda actual.
     */
    private void refreshAdaptersWithCurrency() {
        if (ultimosGastosAdapter != null) {
            ultimosGastosAdapter.setMoneda(moneda);
        }
        // Re-renderizar los textos formateados
        actualizarPresupuestoRestante();
    }

    /**
     * Listeners de menú.
     */
    private void configurarMenu() {

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menu_perfil) {
                navegar(PerfilActivity.class, id);
            } else if (id == R.id.menu_juegos) {
                navegar(ListaJuegosActivity.class, id);
            } else if (id == R.id.menu_wishlist) {
                navegar(ListaWishlistActivity.class, id);
            } else if (id == R.id.menu_gastos) {
                navegar(ListaGastosActivity.class, id);
            } else if (id == R.id.menu_lanzamientos) {
                navegar(LanzamientosActivity.class, id);
            } else if (id == R.id.menu_ajustes) {
                navegar(AjustesActivity.class, id);
            } else if (id == R.id.menu_logout) {
                cerrarSesion();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Cierra la sesión del usuario y vuelve al Login
     * Forma recomendada: logout explícito por acción del usuario
     */
    private void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Navega entre Activities con animación
     */
    private void navegar(Class<?> destino, int menuId){
        navigationView.setCheckedItem(menuId);

        Intent intent = new Intent(this, destino);
        startActivity(intent);
        overridePendingTransition(
                R.anim.slide_in,
                R.anim.slide_out
        );
    }

    /**
     *  Animación para el texto de gastos Total
     */
    private void animarTotal(double valorFinal) {

        android.animation.ValueAnimator animator =
                android.animation.ValueAnimator.ofFloat(0f, (float) valorFinal);

        animator.setDuration(900);
        animator.setInterpolator(new android.view.animation.DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            float valor = (float) animation.getAnimatedValue();
            tvTotalGastos.setText(MoneyUtils.format(valor, moneda));
        });

        animator.start();
    }
}