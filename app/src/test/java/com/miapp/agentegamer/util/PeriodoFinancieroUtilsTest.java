package com.miapp.agentegamer.util;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class PeriodoFinancieroUtilsTest {

    @Test
    public void testGetMesActual_entre1y12() {
        int mes = PeriodoFinancieroUtils.getMesActual();
        assertTrue("Mes debe estar entre 1 y 12", mes >= 1 && mes <= 12);
    }

    @Test
    public void testGetMesActual_coincideConCalendar() {
        int mesEsperado = Calendar.getInstance().get(Calendar.MONTH) + 1;
        assertEquals(mesEsperado, PeriodoFinancieroUtils.getMesActual());
    }

    @Test
    public void testGetAnioActual_anioRazonable() {
        int anio = PeriodoFinancieroUtils.getAnioActual();
        assertTrue("Anio debe ser >= 2024", anio >= 2024);
    }

    @Test
    public void testGetAnioActual_coincideConCalendar() {
        int anioEsperado = Calendar.getInstance().get(Calendar.YEAR);
        assertEquals(anioEsperado, PeriodoFinancieroUtils.getAnioActual());
    }

    @Test
    public void testInicioMesActual_diaHora0() {
        long inicioMes = PeriodoFinancieroUtils.inicioMesActual();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(inicioMes);
        assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
    }

    @Test
    public void testInicioMesActual_menorAHoraActual() {
        long inicioMes = PeriodoFinancieroUtils.inicioMesActual();
        long ahora = System.currentTimeMillis();
        assertTrue("Inicio de mes debe ser menor a hora actual", inicioMes <= ahora);
    }
}
