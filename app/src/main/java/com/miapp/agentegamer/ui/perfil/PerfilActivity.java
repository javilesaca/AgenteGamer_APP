package com.miapp.agentegamer.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.model.UsuarioEntity;
import com.miapp.agentegamer.domain.repository.UserRepository;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@AndroidEntryPoint
public class PerfilActivity extends AppCompatActivity {

    private TextView tvEmail, tvPresupuesto, tvNombre, tvFechaCreacion, tvRol;
    private ImageButton btnEditNombre;
    private ProgressBar progressBarNombre;
    
    @Inject
    UserRepository userRepo;

    private final ActivityResultLauncher<Intent> editProfileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String nuevoNombre = data.getStringExtra("nombre");
                            if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                                actualizarNombre(nuevoNombre);
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tvEmail = findViewById(R.id.tvEmail);
        tvPresupuesto = findViewById(R.id.tvPresupuesto);
        tvNombre = findViewById(R.id.tvNombre);
        tvFechaCreacion = findViewById(R.id.tvFechaCreacion);
        tvRol = findViewById(R.id.tvRol);
        btnEditNombre = findViewById(R.id.btnEditNombre);
        progressBarNombre = findViewById(R.id.progressBarNombre);

        btnEditNombre.setOnClickListener(v -> {
            String currentNombre = tvNombre.getText().toString();
            Intent intent = new Intent(PerfilActivity.this, EditProfileActivity.class);
            intent.putExtra("nombre", currentNombre);
            editProfileLauncher.launch(intent);
        });

        cargarPerfil();
    }

    private void cargarPerfil() {
        userRepo.obtenerUsuario(new UserRepository.OnUsuarioCallback() {
            @Override
            public void onSuccess(UsuarioEntity usuario) {
                tvEmail.setText(usuario.getEmail());
                tvNombre.setText(usuario.getNombre());
                tvPresupuesto.setText(String.valueOf(usuario.getPresupuestoMensual()));
                
                // Format fechaCreacion
                if (usuario.getFechaCreacion() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String fechaStr = sdf.format(new Date(usuario.getFechaCreacion().getSeconds() * 1000));
                    tvFechaCreacion.setText(fechaStr);
                } else {
                    tvFechaCreacion.setText("");
                }
                
                tvRol.setText(usuario.getRol());
            }

            @Override
            public void onError() {
                Toast.makeText(PerfilActivity.this, "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
            }
        });

        //Presupuesto reactivo en tiempo real para observar cambios en ajustes
        userRepo.getPresupuestoLiveData().observe(this, presupuesto -> {
            tvPresupuesto.setText(String.valueOf(presupuesto));
        });
    }

    private void actualizarNombre(String nombre) {
        // Show progress bar
        progressBarNombre.setVisibility(View.VISIBLE);
        btnEditNombre.setEnabled(false);
        
        userRepo.actualizarNombre(nombre, new UserRepository.OnActualizarNombreCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    // Hide progress bar and re-enable button
                    progressBarNombre.setVisibility(View.GONE);
                    btnEditNombre.setEnabled(true);
                    tvNombre.setText(nombre);
                    Toast.makeText(PerfilActivity.this, getString(R.string.nombre_actualizado), Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError() {
                runOnUiThread(() -> {
                    // Hide progress bar and re-enable button on error
                    progressBarNombre.setVisibility(View.GONE);
                    btnEditNombre.setEnabled(true);
                    Toast.makeText(PerfilActivity.this, getString(R.string.error_actualizar_nombre), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
