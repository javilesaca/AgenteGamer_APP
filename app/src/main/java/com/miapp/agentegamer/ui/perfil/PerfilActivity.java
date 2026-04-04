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

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.model.UsuarioEntity;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.ui.common.BaseNavActivity;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * PerfilActivity
 * --------------
 * Pantalla que muestra la información del perfil del usuario.
 * Muestra datos como email, nombre, presupuesto mensual, fecha de creación
 * y rol del usuario. Permite editar el nombre mediante un ActivityResultLauncher.
 * 
 * Características:
 * - Muestra información del usuario desde Firestore
 * - Presupuesto reactivo en tiempo real (se actualiza si cambia en Ajustes)
 * - Botón para editar el nombre que lanza EditProfileActivity
 * - Indicador de carga mientras se actualiza el nombre
 * - Navegación inferior para acceder a otras secciones
 * 
 * @see EditProfileActivity
 * @see UserRepository
 */
@AndroidEntryPoint
public class PerfilActivity extends BaseNavActivity {

    // TextViews para mostrar información del usuario
    private TextView tvEmail, tvPresupuesto, tvNombre, tvFechaCreacion, tvRol;
    // Botón para editar el nombre
    private ImageButton btnEditNombre;
    // ProgressBar mostrado durante actualización del nombre
    private ProgressBar progressBarNombre;
    
    @Inject
    UserRepository userRepo;

    // ActivityResultLauncher para recibir resultado de EditProfileActivity
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

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa las vistas, configura el botón de edición de nombre,
     * carga el perfil del usuario y establece la navegación inferior.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
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
        setupBottomNavigation(R.id.nav_home);
    }

    /**
     * Carga los datos del perfil del usuario desde Firestore.
     * Actualiza los TextViews con la información del usuario (email, nombre,
     * presupuesto, fecha de creación y rol). También observa cambios en el
     * presupuesto para mantenerlo actualizado en tiempo real.
     */
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

    /**
     * Actualiza el nombre del usuario en Firestore.
     * Muestra un indicador de progreso mientras realiza la actualización.
     * Si es exitoso, actualiza el TextView del nombre y muestra un Toast.
     * Si falla, muestra un Toast con el mensaje de error.
     * 
     * @param nombre Nuevo nombre a establecer
     */
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
