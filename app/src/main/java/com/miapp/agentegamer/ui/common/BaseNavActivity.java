package com.miapp.agentegamer.ui.common;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miapp.agentegamer.R;
import com.miapp.agentegamer.ui.ajustes.AjustesActivity;
import com.miapp.agentegamer.ui.games.ListaJuegosActivity;
import com.miapp.agentegamer.ui.gastos.ListaGastosActivity;
import com.miapp.agentegamer.ui.lanzamientos.LanzamientosActivity;
import com.miapp.agentegamer.ui.main.MainActivity;
import com.miapp.agentegamer.ui.perfil.PerfilActivity;
import com.miapp.agentegamer.ui.wishlist.ListaWishlistActivity;

/**
 * BaseNavActivity
 * ---------------
 * Base class for activities that use bottom navigation.
 * Provides common setup for BottomNavigationView and activity navigation.
 */
public abstract class BaseNavActivity extends AppCompatActivity {

    /**
     * Sets up the bottom navigation bar for this activity.
     * 
     * @param selectedItemId The menu item ID to highlight as selected
     */
    protected void setupBottomNavigation(int selectedItemId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        
        if (bottomNav == null) {
            return;
        }
        
        bottomNav.setMenu(R.menu.bottom_nav_menu);
        bottomNav.setSelectedItemId(selectedItemId);
        
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            // Don't re-launch the same activity
            if (itemId == selectedItemId) {
                return true;
            }
            
            // Navigate to the selected activity
            navigateToActivity(itemId);
            
            return true;
        });
    }

    /**
     * Navigates to the activity corresponding to the given menu item ID.
     * Uses FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_SINGLE_TOP for smooth navigation.
     * 
     * @param itemId The menu item ID to navigate to
     */
    private void navigateToActivity(int itemId) {
        Class<?> targetActivity = null;
        
        if (itemId == R.id.nav_home) {
            targetActivity = MainActivity.class;
        } else if (itemId == R.id.nav_gastos) {
            targetActivity = ListaGastosActivity.class;
        } else if (itemId == R.id.nav_juegos) {
            targetActivity = ListaJuegosActivity.class;
        } else if (itemId == R.id.nav_wishlist) {
            targetActivity = ListaWishlistActivity.class;
        } else if (itemId == R.id.nav_lanzamientos) {
            targetActivity = LanzamientosActivity.class;
        }
        
        if (targetActivity != null && !this.getClass().equals(targetActivity)) {
            Intent intent = new Intent(this, targetActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }
}
