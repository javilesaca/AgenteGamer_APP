package com.miapp.agentegamer.ui.viewmodel;

import com.miapp.agentegamer.data.local.entity.GastoEntity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Tests para el filtrado por mes actual del dashboard.
 * Regresión: la tarjeta "Total gastado", el estado y las recomendaciones
 * comparan contra el presupuesto MENSUAL, por lo que solo deben contar
 * los gastos del mes en curso (antes mezclaban el histórico acumulado).
 */
public class FiltrarMesActualTest {

    private static long fecha(int anio, int mes) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(anio, mes - 1, 15, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private static GastoEntity gasto(String nombre, double precio, int anio, int mes) {
        return new GastoEntity("uid-demo", nombre, precio, fecha(anio, mes), null);
    }

    @Test
    public void soloCuentaElMesActual() {
        Calendar ahora = Calendar.getInstance();
        int mesActual = ahora.get(Calendar.MONTH) + 1;
        int anioActual = ahora.get(Calendar.YEAR);
        int mesPrevio = mesActual == 1 ? 12 : mesActual - 1;
        int anioPrevio = mesActual == 1 ? anioActual - 1 : anioActual;

        List<GastoEntity> gastos = new ArrayList<>();
        gastos.add(gasto("Juego actual", 59.99, anioActual, mesActual));
        gastos.add(gasto("Juego previo", 39.99, anioPrevio, mesPrevio));

        List<GastoEntity> resultado = GastoViewModel.filtrarMesActual(gastos);

        assertEquals(1, resultado.size());
        assertEquals("Juego actual", resultado.get(0).getNombreJuego());
    }

    @Test
    public void mismoMesDeOtroAnioNoCuenta() {
        Calendar ahora = Calendar.getInstance();
        int mesActual = ahora.get(Calendar.MONTH) + 1;

        List<GastoEntity> gastos = new ArrayList<>();
        gastos.add(gasto("Juego viejo", 10.0, 2020, mesActual));

        assertTrue(GastoViewModel.filtrarMesActual(gastos).isEmpty());
    }

    @Test
    public void listaNulaYVaciaSonSeguras() {
        assertTrue(GastoViewModel.filtrarMesActual(null).isEmpty());
        assertTrue(GastoViewModel.filtrarMesActual(new ArrayList<>()).isEmpty());
    }
}
