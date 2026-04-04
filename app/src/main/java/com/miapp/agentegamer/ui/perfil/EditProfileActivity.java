package com.miapp.agentegamer.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.miapp.agentegamer.R;
import com.miapp.agentegamer.domain.repository.UserRepository;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;

/**
 * EditProfileActivity
 * --------------------
 * Pantalla para editar el nombre de usuario.
 * Permite al usuario modificar su nombre que se guardará en Firestore.
 * 
 * Características:
 * - Muestra el nombre actual del usuario
 * - Campos de texto para ingresar el nuevo nombre
 * - Botones para guardar o cancelar la edición
 * - Indicador de progreso durante la actualización
 * - Retorna el nuevo nombre a la actividad llamante mediante ActivityResult
 * 
 * @see PerfilActivity
 * @see UserRepository
 */
@AndroidEntryPoint
public class EditProfileActivity extends AppCompatActivity {

    // Campo de texto para el nombre
    private EditText editTextNombre;
    // Botones de acción
    private Button buttonSave;
    private Button buttonCancel;
    // Indicador de progreso
    private ProgressBar progressBar;
    
    @Inject
    UserRepository userRepository;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa las vistas, carga el nombre actual del intent,
     * y configura los listeners de los botones.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI elements
        editTextNombre = findViewById(R.id.editTextNombre);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        progressBar = findViewById(R.id.progressBar);

        // Load current nombre from intent
        String currentNombre = getIntent().getStringExtra("nombre");
        if (currentNombre != null) {
            editTextNombre.setText(currentNombre);
        }

        // Set up button click listeners
        buttonSave.setOnClickListener(new View.OnClickListener() {
            /**
             * Maneja el clic en el botón de guardar.
             * Valida que el nombre no esté vacío, actualiza el nombre en Firestore
             * y retorna el resultado a la actividad llamante.
             * 
             * @param v Vista que fue clickeada
             */
            @Override
            public void onClick(View v) {
                // Get the text from EditText
                String nuevoNombre = editTextNombre.getText().toString().trim();
                
                if (!nuevoNombre.isEmpty()) {
                    // Show loading state
                    setSavingState(true);
                    // Update the nombre via UserRepository
                    userRepository.actualizarNombre(nuevoNombre, new UserRepository.OnActualizarNombreCallback() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Update successful
                                    setSavingState(false);
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("nombre", nuevoNombre);
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onError() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Update failed
                                    setSavingState(false);
                                    Toast.makeText(EditProfileActivity.this, R.string.error_actualizar_nombre, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    // Show error if empty
                    Toast.makeText(EditProfileActivity.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            /**
             * Maneja el clic en el botón de cancelar.
             * Cierra la actividad sin guardar cambios.
             * 
             * @param v Vista que fue clickeada
             */
            @Override
            public void onClick(View v) {
                // Just finish without saving
                finish();
            }
        });
    }

    /**
     * Actualiza el estado de guardado de la UI.
     * Muestra u oculta el ProgressBar y habilita/deshabilita el botón de guardar.
     * 
     * @param saving true para mostrar estado de carga, false para habilitar el botón
     */
    private void setSavingState(boolean saving) {
        if (saving) {
            progressBar.setVisibility(View.VISIBLE);
            buttonSave.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            buttonSave.setEnabled(true);
        }
    }
}