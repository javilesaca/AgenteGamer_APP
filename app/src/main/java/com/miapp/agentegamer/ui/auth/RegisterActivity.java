package com.miapp.agentegamer.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import dagger.hilt.android.AndroidEntryPoint;
import com.google.firebase.firestore.FirebaseFirestore;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.util.PasswordValidator;
import com.miapp.agentegamer.data.repository.UserRepositoryImpl;
import com.miapp.agentegamer.domain.repository.UserRepository;
import com.miapp.agentegamer.ui.main.MainActivity;

import java.util.HashMap;
import java.util.List;
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
     * Valida todos los campos del formulario, exige la política de
     * contraseñas (mínimo 6 caracteres, una mayúscula y un signo),
     * crea el usuario en Firebase Auth y guarda los datos adicionales
     * (nombre, presupuesto, rol) en Firestore.
     *
     * Muestra errores específicos según la causa del fallo:
     * - Requisitos de contraseña incumplidos (validación local)
     * - Email con formato inválido o ya registrado
     * - Contraseña rechazada por Firebase, error de red o error genérico
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

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_login_invalid_email));
            Toast.makeText(this, R.string.error_login_invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }

        List<Integer> requisitosPendientes = PasswordValidator.requisitosIncumplidos(password);
        if (!requisitosPendientes.isEmpty()) {
            String detalle = describirRequisitos(requisitosPendientes);
            etPassword.setError(detalle);
            Toast.makeText(this, detalle, Toast.LENGTH_LONG).show();
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
                    // Sin "rol": los roles se asignan fuera del cliente (consola o
                    // Custom Claims). Las reglas firestore.rules rechazan creaciones
                    // con rol e impiden modificarlo después.

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
                    int messageRes = resolveRegisterError(e);
                    if (messageRes == R.string.error_register_password_requisitos) {
                        etPassword.setError(getString(messageRes));
                    } else if (messageRes == R.string.error_register_email_exists
                            || messageRes == R.string.error_login_invalid_email) {
                        etEmail.setError(getString(messageRes));
                    }
                    Toast.makeText(this, messageRes, Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Une los requisitos incumplidos en un mensaje legible.
     *
     * @param requisitosPendientes Recursos de string de cada requisito pendiente
     * @return Texto como "Te falta: mínimo 6 caracteres, al menos una mayúscula"
     */
    private String describirRequisitos(List<Integer> requisitosPendientes) {
        StringBuilder detalle = new StringBuilder(getString(R.string.error_register_password_faltan));
        detalle.append(": ");
        for (int i = 0; i < requisitosPendientes.size(); i++) {
            if (i > 0) {
                detalle.append(", ");
            }
            detalle.append(getString(requisitosPendientes.get(i)).toLowerCase(java.util.Locale.ROOT));
        }
        return detalle.toString();
    }

    /**
     * Resuelve el mensaje de error a mostrar según la causa del fallo
     * en la creación de la cuenta.
     *
     * @param e Excepción devuelta por Firebase Auth
     * @return Recurso de string con el mensaje específico
     */
    private int resolveRegisterError(Exception e) {
        if (e instanceof FirebaseNetworkException) {
            return R.string.error_login_network;
        }
        if (e instanceof FirebaseAuthWeakPasswordException) {
            return R.string.error_register_password_requisitos;
        }
        if (e instanceof FirebaseAuthUserCollisionException) {
            return R.string.error_register_email_exists;
        }
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return R.string.error_login_invalid_email;
        }
        return R.string.error_login_generic;
    }
}
