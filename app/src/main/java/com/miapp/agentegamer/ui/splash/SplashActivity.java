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

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY_MS = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Preload games data during splash screen
        ((AgenteGamerApplication) getApplication()).preloadGames();

        // Redirect after delay
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthAndRedirect, SPLASH_DELAY_MS);
    }

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
