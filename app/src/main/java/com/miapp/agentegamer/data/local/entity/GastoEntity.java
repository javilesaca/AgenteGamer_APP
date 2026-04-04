package com.miapp.agentegamer.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Calendar;

/**
 * Entidad de Room que representa un gasto de juego en la base de datos.
 * <p>
 * Esta clase mapea la tabla {@code gastos} de la base de datos SQLite local.
 * Cada instancia representa un gasto realizado por un usuario en la compra de
 * un juego, incluyendo información como el nombre, precio, fecha y URL de
 * la imagen del juego.
 * <p>
 * La entidad está indexada por los campos {@code mes}, {@code anio} y
 * {@code userId} para optimizar las consultas de filtrado y ordenamiento.
 *
 * @see Entity
 */
@Entity(tableName = "gastos", indices = {@Index(value = {"mes", "anio"}), @Index(value = {"userId"})})
public class GastoEntity {

    /** Clave primaria auto-generada para cada gasto */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /** Identificador único del usuario en Firebase */
    private String userId;
    
    /** Nombre del juego asociado al gasto */
    private String nombreJuego;
    
    /** Precio del juego en la compra */
    private double precio;
    
    /** Fecha de compra en milisegundos (UNIX epoch) */
    private long fecha;  // Guardada en milisegundos
    
    /** Mes de la compra (1-12), calculado automáticamente desde la fecha */
    private int mes;
    
    /** Año de la compra, calculado automáticamente desde la fecha */
    private int anio;
    
    /** URL de la imagen del juego */
    private String imagenUrl;

    /**
     * Constructor completo para crear una nueva entidad de gasto.
     * <p>
     * Calcula automáticamente los campos {@code mes} y {@code anio} a partir
     * de la fecha proporcionada.
     *
     * @param userId Identificador único del usuario en Firebase
     * @param nombreJuego Nombre del juego comprado
     * @param precio Precio del juego
     * @param fecha Fecha de compra en milisegundos (UNIX epoch)
     * @param imagenUrl URL de la imagen del juego
     */
    // Constructor
    public GastoEntity(String userId, String nombreJuego, double precio, long fecha, String imagenUrl) {
        this.userId = userId;
        this.nombreJuego = nombreJuego;
        this.precio = precio;
        this.fecha = fecha;
        this.imagenUrl = imagenUrl;

        Calendar cal = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(fecha);

        this.mes = cal.get(Calendar.MONTH) +1;
        this.anio = cal.get(Calendar.YEAR);
    }

    // GETTERS Y SETTERS
    
    /**
     * Obtiene el identificador único del gasto.
     *
     * @return ID del gasto en la base de datos
     */
    public int getId() { return id; }

    /**
     * Establece el identificador único del gasto.
     *
     * @param id Nuevo ID del gasto
     */
    public void setId(int id) { this.id = id; }

    /**
     * Obtiene el identificador del usuario que realizó el gasto.
     *
     * @return ID de usuario en Firebase
     */
    public String getUserId() { return userId; }

    /**
     * Establece el identificador del usuario.
     *
     * @param userId Nuevo ID de usuario
     */
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Obtiene el nombre del juego del gasto.
     *
     * @return Nombre del juego
     */
    public String getNombreJuego() { return nombreJuego; }

    /**
     * Establece el nombre del juego.
     *
     * @param nombreJuego Nuevo nombre del juego
     */
    public void setNombreJuego(String nombreJuego) { this.nombreJuego = nombreJuego; }

    /**
     * Obtiene el precio del juego.
     *
     * @return Precio en la moneda correspondiente
     */
    public double getPrecio() { return precio; }

    /**
     * Establece el precio del juego.
     *
     * @param precio Nuevo precio
     */
    public void setPrecio(double precio) { this.precio = precio; }

    /**
     * Obtiene la fecha de compra en milisegundos.
     *
     * @return Fecha en milisegundos (UNIX epoch)
     */
    public long getFecha() { return fecha; }

    /**
     * Establece la fecha de compra y recalcula el mes y año.
     * <p>
     * Al cambiar la fecha, se actualizan automáticamente los campos
     * {@code mes} y {@code anio} derivados de la nueva fecha.
     *
     * @param fecha Nueva fecha en milisegundos (UNIX epoch)
     */
    public void setFecha(long fecha) {
        this.fecha = fecha;
        Calendar cal = Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"));
        cal.setTimeInMillis(fecha);
        this.mes = cal.get(Calendar.MONTH) + 1;
        this.anio = cal.get(Calendar.YEAR);
    }

    /**
     * Obtiene el mes de la compra.
     *
     * @return Mes (1-12)
     */
    public int getMes() {
        return mes;
    }

    /**
     * Establece el mes de la compra.
     *
     * @param mes Nuevo mes (1-12)
     */
    public void setMes(int mes){
        this.mes = mes;
    }

    /**
     * Establece el año de la compra.
     *
     * @param anio Nuevo año
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * Obtiene el año de la compra.
     *
     * @return Año
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Obtiene la URL de la imagen del juego.
     *
     * @return URL de la imagen, o {@code null} si no hay imagen
     */
    public String getImagenUrl() { return imagenUrl; }

    /**
     * Establece la URL de la imagen del juego.
     *
     * @param imagenUrl Nueva URL de la imagen
     */
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

}
