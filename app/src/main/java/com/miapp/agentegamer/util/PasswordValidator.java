package com.miapp.agentegamer.util;

import com.miapp.agentegamer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * PasswordValidator
 * -----------------
 * Política de contraseñas del registro: mínimo 6 caracteres
 * (exigido por Firebase Auth), una mayúscula y un signo.
 * Devuelve exactamente qué requisitos incumple cada contraseña
 * para que la UI explique el motivo del rechazo.
 */
public final class PasswordValidator {

    /** Longitud mínima exigida (coincide con el mínimo de Firebase Auth). */
    public static final int MIN_LENGTH = 6;

    private PasswordValidator() {
    }

    /**
     * Indica si la contraseña cumple todos los requisitos.
     *
     * @param password Contraseña a evaluar (puede ser null)
     * @return true si cumple longitud, mayúscula y signo
     */
    public static boolean esValida(String password) {
        return requisitosIncumplidos(password).isEmpty();
    }

    /**
     * Devuelve los requisitos incumplidos como recursos de string,
     * en orden: longitud, mayúscula, signo.
     *
     * @param password Contraseña a evaluar (puede ser null)
     * @return Lista vacía si cumple todo; si no, un recurso por requisito pendiente
     */
    public static List<Integer> requisitosIncumplidos(String password) {
        List<Integer> pendientes = new ArrayList<>();
        if (password == null || password.length() < MIN_LENGTH) {
            pendientes.add(R.string.error_register_password_corta);
        }
        boolean tieneMayuscula = false;
        boolean tieneSigno = false;
        if (password != null) {
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (Character.isUpperCase(c)) {
                    tieneMayuscula = true;
                } else if (!Character.isLetterOrDigit(c)) {
                    tieneSigno = true;
                }
            }
        }
        if (!tieneMayuscula) {
            pendientes.add(R.string.error_register_password_mayuscula);
        }
        if (!tieneSigno) {
            pendientes.add(R.string.error_register_password_signo);
        }
        return pendientes;
    }
}
