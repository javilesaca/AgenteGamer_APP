package com.miapp.agentegamer.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import dagger.hilt.android.AndroidEntryPoint;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.repository.UserRepositoryImpl;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.ui.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * RegisterActivity
 * ---------------
 * Pantalla de registro de nuevos usuarios en la aplicación.
 * Permite crear una cuenta nueva con email, contraseña, nombre
 * y presupuesto mensual inicial.
 * 
 * Flujo:
 * 1. Usuario completa el formulario de registro
 * 2. Validación de campos obligatorios y formato de presupuesto
 * 3. Creación de usuario en Firebase Auth
 * 4. Guardado de datos adicionales en Firestore (nombre, presupuesto, rol)
 * 5. Si es exitoso, navega a MainActivity
 * 
 * @see LoginActivity
 * @see MainActivity
 */
@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    // Autenticador de Firebase
    private FirebaseAuth auth;
    // Instancia de Firestore para guardar datos del usuario
    private FirebaseFirestore db;

    // Campos de entrada del formulario
    private EditText etEmail, etPassword, etNombre, etPresupuesto;
    // Botón de registro
    private Button btnRegister;

    @Inject
    UserRepository userRepository;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa las vistas, configura Firebase Auth y Firestore,
     * y establece el listener del botón de registro.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etNombre = findViewById(R.id.etNombre);
        etPresupuesto = findViewById(R.id.etPresupuesto);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> registrarUsuario());
    }

    /**
     * Procesa el registro de un nuevo usuario.
     * Valida todos los campos del formulario, crea el usuario en Firebase Auth,
     * y guarda los datos adicionales (nombre, presupuesto, rol) en Firestore.
     * 
     * Pasos del proceso:
     * 1. Validar que todos los campos estén llenos
     * 2. Parsear el presupuesto como número válido
     * 3. Crear usuario en Firebase Auth
     * 4. Guardar documento en Firestore con datos del usuario
     * 5. Navegar a MainActivity si todo es exitoso
     * 
     * Muestra errores Toast según el tipo de fallo.
     */
    private void registrarUsuario() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String presupuestoTxt = etPresupuesto.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || presupuestoTxt.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        double presupuesto;
        try {
            presupuesto = Double.parseDouble(presupuestoTxt);
        } catch (NumberFormatException nfc) {
            Toast.makeText(this, R.string.presupuesto_invalido, Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    if (isFinishing()) return;

                    FirebaseUser user = result.getUser();
                    if (user == null) {
                        Toast.makeText(this, R.string.error_login_generic, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String uid = user.getUid();

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("email", email);
                    userData.put("nombre", nombre);
                    userData.put("presupuestoMensual", presupuesto);
                    userData.put("fechaCreacion", FieldValue.serverTimestamp());
                    userData.put("rol", "USER");

                    db.collection("users")
                            .document(uid)
                            .set(userData)
                            .addOnSuccessListener(unused -> {
                                if (isFinishing()) return;
                                if (userRepository instanceof UserRepositoryImpl) {
                                    ((UserRepositoryImpl) userRepository).resetForNewUser();
                                }
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                if (isFinishing()) return;
                                Toast.makeText(this, R.string.error_guardar_perfil, Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    if (isFinishing()) return;
                    Toast.makeText(this, R.string.error_login_generic, Toast.LENGTH_SHORT).show();
                });
    }
}
