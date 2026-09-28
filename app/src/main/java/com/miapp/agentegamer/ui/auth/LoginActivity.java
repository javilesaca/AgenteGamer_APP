package com.miapp.agentegamer.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.data.repository.UserRepositoryImpl;
import com.miapp.agentegamer.domain.repository.UserRepository;
import dagger.hilt.android.AndroidEntryPoint;
import com.miapp.agentegamer.ui.main.MainActivity;

import javax.inject.Inject;

/**
 * LoginActivity
 * -------------
 * Pantalla de autenticación de usuarios mediante Firebase Auth.
 * Permite a los usuarios existentes iniciar sesión con email y contraseña.
 * Si el usuario no tiene cuenta, puede navegar a RegisterActivity.
 * 
 * Flujo:
 * 1. Usuario ingresa email y contraseña
 * 2. Validación de campos obligatorios
 * 3. Autenticación con Firebase Auth
 * 4. Si es exitoso, navega a MainActivity
 * 5. Si falla, muestra mensaje de error según el tipo de excepción
 * 
 * @see RegisterActivity
 * @see MainActivity
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    // Autenticador de Firebase
    private FirebaseAuth auth;
    // Campos de entrada
    private EditText etEmail, etPassword;
    // Botones de acción
    private Button btnLogin;
    private Button btnRegister;
    // Flag para evitar múltiples llamadas simultáneas
    private boolean isLoading = false;

    @Inject
    UserRepository userRepository;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa las vistas, configura los listeners de botones
     * y obtiene la instancia de FirebaseAuth.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> login());
        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    /**
     * Realiza el proceso de inicio de sesión con Firebase Auth.
     * Valida que los campos no estén vacíos, muestra estado de carga
     * y maneja los resultados de la autenticación.
     * 
     * Si la autenticación es exitosa, navega a MainActivity.
     * Si falla, muestra un mensaje de error apropiado según el tipo de excepción:
     * - FirebaseNetworkException: error de conexión
     * - FirebaseAuthInvalidUserException / FirebaseAuthInvalidCredentialsException: credenciales incorrectas
     * - Otras excepciones: error genérico
     */
    private void login() {
        if (isLoading) return;

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    if (isFinishing()) return;
                    if (userRepository instanceof UserRepositoryImpl) {
                        ((UserRepositoryImpl) userRepository).resetForNewUser();
                    }
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    if (isFinishing()) return;
                    setLoading(false);
                    int messageRes;
                    if (e instanceof FirebaseNetworkException) {
                        messageRes = R.string.error_login_network;
                    } else if (e instanceof FirebaseAuthInvalidUserException
                            || e instanceof FirebaseAuthInvalidCredentialsException) {
                        messageRes = R.string.error_login_credentials;
                    } else {
                        messageRes = R.string.error_login_generic;
                    }
                    Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Actualiza el estado de carga de la interfaz de usuario.
     * Deshabilita el botón de login y cambia su texto para indicar
     * el estado de carga durante el proceso de autenticación.
     * 
     * @param loading true para mostrar estado de carga, false para habilitar el botón
     */
    private void setLoading(boolean loading) {
        isLoading = loading;
        btnLogin.setEnabled(!loading);
        btnLogin.setText(loading ? R.string.loading : R.string.btn_login);
    }
}
