package com.miapp.agentegamer.util;

import java.util.Currency;
import java.util.Locale;
import java.text.NumberFormat;

public class MoneyUtils {

    private static final Locale LOCALE_ES = new Locale("es", "ES");

    /**
     * Formatea una cantidad de dinero en euros (compatibilidad hacia atrás)
     * Ejemplo: 250 -> 250,00 €
     */
    public static String format(double cantidad) {
        return format(cantidad, "EUR");
    }

    /**
     * Formatea una cantidad de dinero con la moneda indicada.
     * Soporta códigos ISO 4217: "EUR", "USD", "GBP", etc.
     * Si el código es nulo o no reconocido, usa EUR por defecto.
     *
     * Ejemplos:
     *   format(250, "EUR") -> 250,00 €
     *   format(250, "USD") -> 250,00 US$
     */
    public static String format(double cantidad, String currencyCode) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(LOCALE_ES);
        if (currencyCode != null && !currencyCode.isEmpty()) {
            try {
                formato.setCurrency(Currency.getInstance(currencyCode));
            } catch (IllegalArgumentException e) {
                // Código de moneda no reconocido, mantener EUR por defecto
            }
        }
        return formato.format(cantidad);
    }
}
