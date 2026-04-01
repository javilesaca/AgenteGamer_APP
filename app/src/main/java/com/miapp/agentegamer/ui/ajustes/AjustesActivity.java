package com.miapp.agentegamer.ui.ajustes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.domain.model.SistemaFinanciero;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.domain.usecase.UpdateSettingsUseCase;
import com.miapp.agentegamer.ui.wishlist.WishlistItemUI;
import com.miapp.agentegamer.ui.viewmodel.WishlistViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AjustesActivity extends AppCompatActivity {

    private EditText etPresupuesto;
    private Button btnGuardar;
    private Button btnCancelar;
    private RadioGroup rgMoneda;

    @Inject
    UserRepository userRepo;

    @Inject
    UpdateSettingsUseCase updateSettingsUseCase;

    private WishlistViewModel wishlistViewModel;
    private List<WishlistItemUI> wishlistActual;
    private TextView tvImpactoRecomendado;
    private TextView tvImpactoNoRecomendado;
    private TextView tvImpactoAjustado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        etPresupuesto = findViewById(R.id.etPresupuesto);
        tvImpactoRecomendado = findViewById(R.id.tvImpactoRecomendado);
        tvImpactoAjustado = findViewById(R.id.tvImpactoAjustado);
        tvImpactoNoRecomendado = findViewById(R.id.tvImpactoNoRecomendado);

        etPresupuesto.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                recalcularImpacto();
            }
        });

        wishlistViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);

        wishlistViewModel.getWishList().observe(this, lista -> {
            wishlistActual = lista;
        });
        btnGuardar = findViewById(R.id.btnGuardarPresupuesto);
        btnCancelar = findViewById(R.id.btnCancelarPresupuesto);
        rgMoneda = findViewById(R.id.rgMoneda);

        cargarPresupuestoActual();

        btnGuardar.setOnClickListener(v -> guardarPresupuesto());
        btnCancelar.setOnClickListener(v -> finish());

        // Restaurar selección de moneda guardada (ejemplo)
        // TODO: implementar persistencia de preferencias
        rgMoneda.setOnCheckedChangeListener((group, checkedId) -> {
            // Guardar preferencia de moneda
            // Por ahora solo muestra un toast
            RadioButton selected = findViewById(checkedId);
            if (selected != null) {
                Toast.makeText(AjustesActivity.this, 
                    "Moneda seleccionada: " + selected.getText(), 
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarPresupuestoActual() {
        userRepo.obtenerPresupuesto(new UserRepository.OnPresupuestoCallback() {
            @Override
            public void onSuccess(double presupuesto) {
                etPresupuesto.setText(String.valueOf(presupuesto));
            }

            @Override
            public void onError() {
                Toast.makeText(AjustesActivity.this, R.string.error_cargar_presupuesto, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void guardarPresupuesto() {
        try {
            double nuevoPresupuesto = Double.parseDouble(etPresupuesto.getText().toString());

            updateSettingsUseCase.updatePresupuesto(nuevoPresupuesto, new UserRepository.OnPresupuestoCallback() {
                @Override
                public void onSuccess(double presupuesto) {
                    Toast.makeText(AjustesActivity.this, R.string.presupuesto_actualizado, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError() {
                    Toast.makeText(AjustesActivity.this, R.string.error_actualizar_presupuesto, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, R.string.presupuesto_invalido, Toast.LENGTH_SHORT).show();
        }
    }

    private void recalcularImpacto() {

        if (wishlistActual == null) return;

        try {

            double presupuesto =
                    Double.parseDouble(etPresupuesto.getText().toString());

            SistemaFinanciero sistema = new SistemaFinanciero(presupuesto);

            int recomendados = 0;
            int ajustados = 0;
            int noRecomendados = 0;

            double gastoSimulado = 0;

            for (WishlistItemUI item : wishlistActual) {

                double precio = item.getJuego().getPrecioEstimado();

                String evaluacion =
                        sistema.evaluarCompra(precio, gastoSimulado);

                if (evaluacion.equals("RECOMENDADO")) {
                    recomendados++;
                }
                else if (evaluacion.equals("AJUSTADO")) {
                    ajustados++;
                }
                else {
                    noRecomendados++;
                }

                gastoSimulado += precio;
            }

            tvImpactoRecomendado.setText(getString(R.string.impacto_recomendado, recomendados));
            tvImpactoAjustado.setText(getString(R.string.impacto_ajustado, ajustados));
            tvImpactoNoRecomendado.setText(getString(R.string.impacto_no_recomendado, noRecomendados));

        } catch (Exception ignored) {}
    }

}