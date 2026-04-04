package com.miapp.agentegamer.util;

import java.util.Currency;
import java.util.Locale;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * MoneyUtils
 * ----------
 * Utilidad para el formateo de cantidades monetarias.
 * Proporciona métodos estáticos para formatear dinero en diferentes monedas
 * y realizar conversiones entre ellas.
 * 
 * Características:
 * - Formateo de moneda con símbolos locales (€, $, £)
 * - Soporte para códigos ISO 4217 (EUR, USD, GBP)
 * - Conversión de EUR a otras monedas usando tasas fijas
 * - Locale español para formato de números
 * 
 * @see java.text.NumberFormat
 * @see java.util.Currency
 */
public class MoneyUtils {

    // Locale español para formateo de números
    private static final Locale LOCALE_ES = new Locale("es", "ES");

    // Tasas de cambio fijas desde EUR (base)
    // Estas tasas son estáticas y no se actualizan en tiempo real
    private static final Map<String, Double> EXCHANGE_RATES = new HashMap<>();
    static {
        EXCHANGE_RATES.put("EUR", 1.0);
        EXCHANGE_RATES.put("USD", 1.08);
        EXCHANGE_RATES.put("GBP", 0.86);
    }

    /**
     * Formatea una cantidad de dinero en euros (compatibilidad hacia atrás).
     * Ejemplo: 250 -> "250,00 €"
     * 
     * @param cantidad Cantidad a formatear en euros
     * @return String formateado con el símbolo del euro
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
     *   format(250, "EUR") -> "250,00 €"
     *   format(250, "USD") -> "$250,00"
     *   format(250, "GBP") -> "£250.00"
     * 
     * @param cantidad Cantidad a formatear
     * @param currencyCode Código de moneda ISO 4217
     * @return String formateado con el símbolo de la moneda
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
     * 
     * @param montoEUR Monto en euros a convertir
     * @param monedaDestino Código de la moneda destino
     * @return Monto convertido, o el monto original si no hay tasa definida
     */
    public static double convertirDesdeEUR(double montoEUR, String monedaDestino) {
        if (monedaDestino == null || monedaDestino.isEmpty()) {
            return montoEUR;
        }
        Double tasa = EXCHANGE_RATES.get(monedaDestino.toUpperCase());
        return tasa != null ? montoEUR * tasa : montoEUR;
    }
}
