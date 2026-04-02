package com.miapp.agentegamer.util;

import java.util.Currency;
import java.util.Locale;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class MoneyUtils {

    private static final Locale LOCALE_ES = new Locale("es", "ES");

    // Tasas de cambio fijas desde EUR (base)
    private static final Map<String, Double> EXCHANGE_RATES = new HashMap<>();
    static {
        EXCHANGE_RATES.put("EUR", 1.0);
        EXCHANGE_RATES.put("USD", 1.08);
        EXCHANGE_RATES.put("GBP", 0.86);
    }

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
        // Convertir desde EUR a la moneda destino
        double cantidadConvertida = convertirDesdeEUR(cantidad, currencyCode);
        
        NumberFormat formato = NumberFormat.getCurrencyInstance(LOCALE_ES);
        if (currencyCode != null && !currencyCode.isEmpty()) {
            try {
                formato.setCurrency(Currency.getInstance(currencyCode));
            } catch (IllegalArgumentException e) {
                // Código de moneda no reconocido, mantener EUR por defecto
            }
        }
        return formato.format(cantidadConvertida);
    }

    /**
     * Convierte un monto desde EUR a la moneda destino.
     * Si la moneda no tiene tasa definida, retorna el monto sin convertir.
     */
    public static double convertirDesdeEUR(double montoEUR, String monedaDestino) {
        if (monedaDestino == null || monedaDestino.isEmpty()) {
            return montoEUR;
        }
        Double tasa = EXCHANGE_RATES.get(monedaDestino.toUpperCase());
        return tasa != null ? montoEUR * tasa : montoEUR;
    }
}
