package com.miapp.agentegamer.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PresupuestoManager {

    private static final String PREFS = "finanazas_prefs";
    private static final String KEY_PRESUPUESTO = "presupuesto";

    public static void guardarPresupuesto(Context context, double presupuesto) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        prefs.edit().putFloat(KEY_PRESUPUESTO, (float) presupuesto).apply();
    }

    public static double obtenerPresupuesto(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        return prefs.getFloat(KEY_PRESUPUESTO, 100f);
    }
}
