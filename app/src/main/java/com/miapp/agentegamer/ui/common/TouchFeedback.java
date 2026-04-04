package com.miapp.agentegamer.ui.common;

import android.view.MotionEvent;
import android.view.View;

/**
 * TouchFeedback
 * ------------
 * Utilidad para aplicar efectos visuales táctiles a las vistas.
 * Proporciona una animación de escala al tocar y soltar elementos,
 * mejorando la experiencia de usuario con retroalimentación visual.
 * 
 * Efectos:
 * - ACTION_DOWN: Reduce la escala a 0.97x con duración de 120ms
 * - ACTION_UP/ACTION_CANCEL: Restaura la escala a 1.0x con duración de 120ms
 * 
 * Uso típico: Llamar a TouchFeedback.apply(view) en el adapter o al configurar el click listener.
 */
public class TouchFeedback {

    /**
     * Aplica el efecto de retroalimentación táctil a una vista.
     * Configura un OnTouchListener que anima la escala de la vista
     * cuando el usuario la toca.
     * 
     * @param view Vista a la que aplicar el efecto de retroalimentación
     */
    public static void apply(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate()
                            .scaleX(0.97f)
                            .scaleY(0.97f)
                            .setDuration(120)
                            .start();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(120)
                            .start();
                    break;
            }
            return false;
        });
    }
}

