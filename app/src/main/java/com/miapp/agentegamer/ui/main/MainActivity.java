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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.model.UsuarioEntity;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;
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
import com.miapp.agentegamer.util.MoneyUtils;
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
    protected TextView tvRecomendacion, tvTotalGastos;
    private View indicadorEstado;
    private PieChart pieChart;

    //Drawer
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    // MVVM
    private GastoViewModel gastoViewModel;

    // Dominio
    private SistemaFinanciero sistemaFinanciero;
    
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
    }

    /**
     * Referencias a vistas del layout.
     */
    private void inicializarVistas() {

        tvRecomendacion = findViewById(R.id.tvRecomendacion);
        tvTotalGastos = findViewById(R.id.tvTotalGastos);
        indicadorEstado = findViewById(R.id.viewIndicador);
        pieChart = findViewById(R.id.pieChart);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            tvEmail.setText(user.getEmail());
            // Fetch latest nombre from Firestore to keep consistency with PerfilActivity
            userRepository.obtenerUsuario(new UserRepository.OnUsuarioCallback() {
                @Override
                public void onSuccess(UsuarioEntity usuario) {
                    tvNombre.setText(usuario.getNombre());
                }

                @Override
                public void onError() {
                    // Fallback to Firebase Auth display name if Firestore fetch fails
                    if (user.getDisplayName() != null) {
                        tvNombre.setText(user.getDisplayName());
                    } else {
                        tvNombre.setText("Usuario desconocido");
                    }
                }
            });
        } else {
            tvNombre.setText("Usuario desconocido");
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
        userRepository.obtenerPresupuesto(new UserRepository.OnPresupuestoCallback() {
            @Override
            public void onSuccess(double presupuesto) {
                sistemaFinanciero = new SistemaFinanciero(presupuesto);
                gastoViewModel.setSistemaFinanciero(sistemaFinanciero);

                // Actualizar el TextView con el presupuesto restante
                gastoRepo.getTotalGastadoMesSync(totalGastado -> {
                    runOnUiThread(() -> {
                        tvTotalGastos.setText(MoneyUtils.format(presupuesto - totalGastado));
                    });
                });
            }

            @Override
            public void onError() {
                // Presupuesto por defecto si falla Firestore
                double presupuestoDefault = 100;
                sistemaFinanciero = new SistemaFinanciero(presupuestoDefault);
                gastoViewModel.setSistemaFinanciero(sistemaFinanciero);

                // Actualizar el TextView con el presupuesto restante
                gastoRepo.getTotalGastadoMesSync(totalGastado -> {
                    runOnUiThread(() -> {
                        tvTotalGastos.setText(MoneyUtils.format(presupuestoDefault - totalGastado));
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
            tvTotalGastos.setText(MoneyUtils.format(valor));
        });

        animator.start();
    }
}