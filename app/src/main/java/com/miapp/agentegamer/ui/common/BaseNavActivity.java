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
 * Clase base abstracta para actividades que utilizan navegación inferior (Bottom Navigation).
 * Proporciona configuración común para el BottomNavigationView y la navegación entre actividades.
 * 
 * Las clases derivadas solo necesitan llamar a setupBottomNavigation() en onCreate()
 * con el ID del elemento de menú que debe estar seleccionado.
 * 
 * Navegación:
 * - nav_home -> MainActivity
 * - nav_gastos -> ListaGastosActivity
 * - nav_juegos -> ListaJuegosActivity
 * - nav_wishlist -> ListaWishlistActivity
 * - nav_lanzamientos -> LanzamientosActivity
 * 
 * @see com.google.android.material.bottomnavigation.BottomNavigationView
 */
public abstract class BaseNavActivity extends AppCompatActivity {

    private int mSelectedItemId = -1;

    /**
     * Configura la barra de navegación inferior para esta actividad.
     * Establece el elemento seleccionado, configura el listener para navegar
     * a las actividades correspondientes y aplica las banderas de navegación apropiadas.
     * 
     * @param selectedItemId El ID del elemento de menú que debe estar seleccionado
     */
    protected void setupBottomNavigation(int selectedItemId) {
        mSelectedItemId = selectedItemId;
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        
        if (bottomNav == null) {
            return;
        }
        
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
     * Resetea la selección del bottom nav cada vez que la actividad vuelve al frente.
     * Esto corrige el bug donde al navegar entre actividades, el bottom nav mantenía
     * seleccionado el ítem de la actividad anterior.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mSelectedItemId != -1) {
            BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
            if (bottomNav != null && bottomNav.getSelectedItemId() != mSelectedItemId) {
                bottomNav.setSelectedItemId(mSelectedItemId);
            }
        }
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
