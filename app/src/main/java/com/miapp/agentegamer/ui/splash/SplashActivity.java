package com.miapp.agentegamer.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.miapp.agentegamer.AgenteGamerApplication;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.ui.auth.LoginActivity;
import com.miapp.agentegamer.ui.main.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * SplashActivity
 * -------------
 * Pantalla de bienvenida que se muestra al iniciar la aplicación.
 * Muestra el logo de AgenteGamer, el tagline "Tu asistente gaming financiero"
 * y realiza la precarga de datos de juegos en segundo plano.
 * 
 * Después de 1.5 segundos, verifica el estado de autenticación del usuario:
 * - Si hay un usuario autenticado, navega a MainActivity
 * - Si no hay usuario, navega a LoginActivity
 * 
 * @see MainActivity
 * @see LoginActivity
 */
@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    // Tiempo de duración del splash en milisegundos
    private static final int SPLASH_DELAY_MS = 1500;

    /**
     * Método que se ejecuta al crear la actividad.
     * Inicializa la vista, precarga los datos de juegos y programa
     * la redirección después del tiempo definido.
     * 
     * @param savedInstanceState Estado guardado de la actividad (puede ser null)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Preload games data during splash screen
        ((AgenteGamerApplication) getApplication()).preloadGames();

        // Redirect after delay
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthAndRedirect, SPLASH_DELAY_MS);
    }

    /**
     * Verifica el estado de autenticación del usuario y redirige a la pantalla
     * correspondiente (Login o Main) según corresponda.
     * Este método se ejecuta después del delay configurado en SPLASH_DELAY_MS.
     */
    private void checkAuthAndRedirect() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        Intent intent;
        if (auth.getCurrentUser() != null) {
            // User is logged in, go to main
            intent = new Intent(this, MainActivity.class);
        } else {
            // User is not logged in, go to login
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}
