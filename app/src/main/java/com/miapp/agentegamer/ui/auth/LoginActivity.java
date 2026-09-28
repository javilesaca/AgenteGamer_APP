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
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.FirebaseTooManyRequestsException;
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
     * Valida cada campo por separado, muestra estado de carga
     * y maneja los resultados de la autenticación.
     *
     * Si la autenticación es exitosa, navega a MainActivity.
     * Si falla, muestra un mensaje específico según la causa:
     * - Campos vacíos o email con formato inválido (validación local)
     * - FirebaseNetworkException: error de conexión
     * - FirebaseTooManyRequestsException: demasiados intentos
     * - FirebaseAuthInvalidUserException: cuenta inexistente o deshabilitada
     * - FirebaseAuthInvalidCredentialsException: contraseña incorrecta o email inválido
     * - Otras excepciones: error genérico
     */
    private void login() {
        if (isLoading) return;

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.error_login_empty_email));
            Toast.makeText(this, R.string.error_login_empty_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_login_invalid_email));
            Toast.makeText(this, R.string.error_login_invalid_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError(getString(R.string.error_login_empty_password));
            Toast.makeText(this, R.string.error_login_empty_password, Toast.LENGTH_SHORT).show();
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
                    int messageRes = resolveLoginError(e);
                    // Marcado de campos unificado: errores de campo -> setError, resto solo Toast
                    if (messageRes == R.string.error_login_wrong_password) {
                        etPassword.setError(getString(messageRes));
                    } else if (messageRes == R.string.error_login_user_not_found
                            || messageRes == R.string.error_login_invalid_email
                            || messageRes == R.string.error_login_credentials) {
                        etEmail.setError(getString(messageRes));
                        if (messageRes == R.string.error_login_credentials) {
                            etPassword.setError(getString(messageRes));
                        }
                    }
                    Toast.makeText(this, messageRes, Toast.LENGTH_LONG).show();
                });
    }

    /**
     * Resuelve el mensaje de error a mostrar según la causa del fallo
     * de inicio de sesión.
     *
     * @param e Excepción devuelta por Firebase Auth
     * @return Recurso de string con el mensaje específico
     */
    private int resolveLoginError(Exception e) {
        if (e instanceof FirebaseNetworkException) {
            return R.string.error_login_network;
        }
        if (e instanceof FirebaseTooManyRequestsException) {
            return R.string.error_login_too_many_attempts;
        }
        // reCAPTCHA / Play Integrity (clase según versión del SDK): sin import rígido
        String simpleName = e.getClass().getSimpleName().toLowerCase();
        String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        if (simpleName.contains("recaptcha") || msg.contains("recaptcha")) {
            return R.string.error_login_recaptcha;
        }
        if (e instanceof FirebaseAuthInvalidUserException) {
            String code = normalizeCode(((FirebaseAuthInvalidUserException) e).getErrorCode());
            if (code.contains("USER_NOT_FOUND")) {
                return R.string.error_login_user_not_found;
            }
            if (code.contains("USER_DISABLED")) {
                return R.string.error_login_user_disabled;
            }
            if (code.contains("OPERATION_NOT_ALLOWED")) {
                return R.string.error_login_operation_not_allowed;
            }
            return R.string.error_login_credentials;
        }
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            String code = normalizeCode(((FirebaseAuthInvalidCredentialsException) e).getErrorCode());
            if (code.contains("WRONG_PASSWORD")) {
                return R.string.error_login_wrong_password;
            }
            if (code.contains("INVALID_EMAIL")) {
                return R.string.error_login_invalid_email;
            }
            if (code.contains("OPERATION_NOT_ALLOWED")) {
                return R.string.error_login_operation_not_allowed;
            }
            // Códigos nuevos anti-enumeración: credencial combinada inválida
            if (code.contains("INVALID_CREDENTIAL") || code.contains("INVALID_LOGIN_CREDENTIALS")) {
                return R.string.error_login_credentials;
            }
            return R.string.error_login_credentials;
        }
        // Fallback por código en mensaje (algunas versiones lo exponen así)
        String upperMsg = msg.toUpperCase();
        if (upperMsg.contains("OPERATION_NOT_ALLOWED")) {
            return R.string.error_login_operation_not_allowed;
        }
        if (upperMsg.contains("INVALID_LOGIN_CREDENTIALS") || upperMsg.contains("INVALID_CREDENTIAL")) {
            return R.string.error_login_credentials;
        }
        return R.string.error_login_generic;
    }

    private String normalizeCode(String code) {
        return code != null ? code.toUpperCase() : "";
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
