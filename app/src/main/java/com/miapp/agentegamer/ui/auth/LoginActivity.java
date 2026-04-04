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

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private Button btnRegister;
    private boolean isLoading = false;

    @Inject
    UserRepository userRepository;

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

    private void setLoading(boolean loading) {
        isLoading = loading;
        btnLogin.setEnabled(!loading);
        btnLogin.setText(loading ? R.string.loading : R.string.btn_login);
    }
}
