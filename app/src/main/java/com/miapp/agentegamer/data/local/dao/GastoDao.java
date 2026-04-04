package com.miapp.agentegamer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;

import com.miapp.agentegamer.data.local.entity.GastoEntity;

import java.util.List;

/**
 * Interfaz de acceso a datos (DAO) para la gestión de gastos de juegos.
 * <p>
 * Define todas las operaciones CRUD y consultas sobre la tabla {@code gastos}
 * de la base de datos Room. Esta interfaz es utilizada por Room para generar
 * la implementación automática de los métodos de acceso a datos.
 * <p>
 * Las consultas permiten obtener gastos por usuario, filtrar por mes y año,
 * calcular totales, y realizar operaciones de mantenimiento como eliminar
 * todos los gastos de un usuario.
 *
 * @see Dao
 * @see GastoEntity
 */
@Dao
public interface GastoDao {

    /**
     * Inserta un nuevo gasto en la base de datos.
     * <p>
     * El gasto se añade a la tabla {@code gastos}. Room generará automáticamente
     * un ID único si {@code autoGenerate = true} está configurado en la entidad.
     *
     * @param gasto Objeto {@link GastoEntity} a insertar
     */
    @Insert
    void insertGasto(GastoEntity gasto);

    /**
     * Actualiza un gasto existente en la base de datos.
     * <p>
     * El gasto se identifica por su clave primaria (campo {@code id}).
     *
     * @param gasto Objeto {@link GastoEntity} con los datos actualizados
     */
    @Update
    void updateGasto(GastoEntity gasto);

    /**
     * Elimina un gasto de la base de datos.
     * <p>
     * El gasto se identifica por su clave primaria (campo {@code id}).
     *
     * @param gasto Objeto {@link GastoEntity} a eliminar
     */
    @Delete
    void deleteGasto(GastoEntity gasto);

    /**
     * Obtiene todos los gastos de un usuario ordenados por fecha descendente.
     * <p>
     * Devuelve un {@link LiveData} que permite observar cambios en tiempo real
     * en la base de datos. Los gastos se ordenan del más reciente al más antiguo.
     *
     * @param userId Identificador único del usuario en Firebase
     * @return LiveData con la lista de gastos del usuario
     */
    @Query("SELECT * FROM gastos WHERE userId = :userId ORDER BY fecha DESC")
    LiveData<List<GastoEntity>> getAllGastos(String userId);

    /**
     * Elimina todos los gastos de un usuario.
     * <p>
     * Operación de mantenimiento que borra todos los registros de gastos
     * asociados al usuario especificado.
     *
     * @param userId Identificador único del usuario en Firebase
     */
    @Query("DELETE FROM gastos WHERE userId = :userId")
    void deleteAll(String userId);

    /**
     * Obtiene el gasto total de un usuario para un mes y año específicos.
     * <p>
     * Utiliza la fecha convertida desde milisegundos a formato UNIX epoch
     * para filtrar los registros. Devuelve 0 si no hay gastos en el período.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param mes Mes (1-12)
     * @param anio Año (ej. 2024)
     * @return LiveData con el total de gastos del mes
     */
    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
            "WHERE userId = :userId " +
            "AND strftime('%m',fecha/1000,'unixepoch') = printf('%02d', :mes) " +
            "AND strftime('%Y',fecha/1000,'unixepoch') = CAST(:anio AS TEXT)")
    LiveData<Double> getGastoTotalMes(String userId, int mes, int anio);

    /**
     * Obtiene el total gastado en un mes específico usando los campos {@code mes} y {@code anio}.
     * <p>
     * Esta consulta utiliza los campos almacenados directamente en la entidad
     * en lugar de calcularlos a partir de la fecha. Es útil cuando se necesita
     * una consulta síncrona.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param mes Mes (1-12)
     * @param anio Año
     * @return Total de gastos como primitive double
     */
    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos WHERE userId = :userId AND mes = :mes AND anio = :anio")
    double getTotalGastadoMes(String userId, int mes, int anio);

    /**
     * Obtiene los totales mensuales de gastos en un rango de fechas.
     * <p>
     * Agrupa los gastos por mes y calcula la suma de cada mes. Limita el número
     * de resultados para evitar cargar excesivos datos históricos.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param startDate Fecha de inicio en milisegundos (UNIX epoch)
     * @param limit Número máximo de meses a devolver
     * @return Lista de objetos {@link MonthlyTotal} con el mes y el total
     */
    @Query("SELECT strftime('%Y-%m', fecha/1000, 'unixepoch') as mes, " +
           "SUM(precio) as total " +
           "FROM gastos " +
           "WHERE userId = :userId AND fecha >= :startDate " +
           "GROUP BY mes " +
           "ORDER BY mes ASC " +
           "LIMIT :limit")
    List<MonthlyTotal> getMonthlyTotals(String userId, long startDate, int limit);

    /**
     * Clase auxiliar para representar el total mensual de gastos.
     * <p>
     * Se utiliza como resultado de la consulta {@link #getMonthlyTotals}.
     * Contiene el mes en formato {@code YYYY-MM} y el total de gastos.
     */
    class MonthlyTotal {
        /** Mes en formato YYYY-MM */
        public String mes;
        /** Total de gastos del mes */
        public double total;
    }

    /**
     * Obtiene los gastos más recientes de un usuario con un límite.
     * <p>
     * Útil para mostrar un resumen de los últimos gastos en la UI.
     * Devuelve un LiveData para observar cambios en tiempo real.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param limit Número máximo de gastos a devolver
     * @return LiveData con la lista de gastos recientes
     */
    @Query("SELECT * FROM gastos WHERE userId = :userId ORDER BY fecha DESC LIMIT :limit")
    LiveData<List<GastoEntity>> getRecentGastos(String userId, int limit);

    /**
     * Obtiene el total de gastos en un rango de fechas específico.
     * <p>
     * Devuelve un LiveData que permite observar cambios cuando se modifica
     * la base de datos dentro del rango especificado.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param startDate Fecha de inicio en milisegundos (UNIX epoch)
     * @param endDate Fecha de fin en milisegundos (UNIX epoch)
     * @return LiveData con el total de gastos en el rango
     */
    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
           "WHERE userId = :userId AND fecha >= :startDate AND fecha < :endDate")
    LiveData<Double> getTotalForDateRange(String userId, long startDate, long endDate);

    /**
     * Obtiene el total de gastos en un rango de fechas de forma síncrona.
     * <p>
     * Versión síncrona de {@link #getTotalForDateRange} que devuelve el resultado
     * directamente. Útil para operaciones que no requieren LiveData.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param startDate Fecha de inicio en milisegundos (UNIX epoch)
     * @param endDate Fecha de fin en milisegundos (UNIX epoch)
     * @return Total de gastos en el rango
     */
    @Query("SELECT IFNULL(SUM(precio),0) FROM gastos " +
           "WHERE userId = :userId AND fecha >= :startDate AND fecha < :endDate")
    double getTotalForDateRangeSync(String userId, long startDate, long endDate);

}
