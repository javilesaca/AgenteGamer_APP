package com.miapp.agentegamer.util;

import java.util.Calendar;

/**
 * PeriodoFinancieroUtils
 * ---------------------
 * Utilidad para obtener información del período financiero actual.
 * Proporciona métodos estáticos para obtener el mes y año actual,
 * así como el timestamp de inicio del mes actual.
 * 
 * Útil para cálculos financieros y consultas a la base de datos
 * que requieren filtrar por el período actual (mes en curso).
 * 
 * @see java.util.Calendar
 */
public class PeriodoFinancieroUtils {

    /**
     * Obtiene el número del mes actual (1-12).
     * 
     * @return Mes actual (enero=1, diciembre=12)
     */
    public static int getMesActual() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * Obtiene el año actual.
     * 
     * @return Año actual (ej: 2026)
     */
    public static int getAnioActual() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * Obtiene el timestamp (milisegundos) del inicio del mes actual.
     * El timestamp se configura a las 00:00:00 del primer día del mes.
     * 
     * @return Timestamp en milisegundos del inicio del mes actual
     */
    public static long inicioMesActual() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTimeInMillis();
    }
}
