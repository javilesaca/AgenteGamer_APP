package com.miapp.agentegamer.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FechaUtils {

    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * Devuelve el timestamp actual en milisegundos
     */
    public static long ahoraTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Calcula los días hasta una fecha pasada como String (yyyy-MM-dd)
     */
    public static long diasHasta(String fechaLanzamiento) {
        try {
            Date fechaJuego = FORMAT.parse(fechaLanzamiento);
            if (fechaJuego == null) return Long.MAX_VALUE;

            return diasHasta(fechaJuego.getTime());

        } catch (ParseException e) {
            return Long.MAX_VALUE;
        }
    }

    /**
     * Calcula los días hasta una fecha en milisegundos
     */
    public static long diasHasta(long fechaMs) {
        long hoy = System.currentTimeMillis();
        long diff = fechaMs - hoy;
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    /**
     * Parsea una fecha yyyy-MM-dd a Date
     */
    public static Date parseFecha(String fecha) {
        try {
            return FORMAT.parse(fecha);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Devuelve la fecha de hoy en formato yyyy-MM-dd
     */
    public static String hoy() {
        return FORMAT.format(new Date());
    }

    /**
     * Devuelve la fecha dentro de X días en formato yyyy-MM-dd
     */
    public static String dentroDeDias(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, dias);
        return FORMAT.format(cal.getTime());
    }
}


