package com.miapp.agentegamer.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * Módulo de Hilt que proporciona las instancias de Firebase necesarias para
 * la autenticación y la base de datos en la nube (Firestore).
 * <p>
 * Este módulo provee las dependencias principales de Firebase utilizadas en la
 * aplicación: {@link FirebaseAuth} para la autenticación de usuarios y
 * {@link FirebaseFirestore} para el almacenamiento y recuperación de datos
 * en la nube.
 * <p>
 * Se instala en {@link SingletonComponent} para garantizar que las instancias
 * de Firebase sean singleton durante todo el ciclo de vida de la aplicación.
 * Esto es importante porque Firebase Auth y Firestore mantienen estados internos
 * que deben persistir.
 * <p>
 * La anotación {@code @Module} indica que esta clase es un módulo de Hilt que
 * define métodos para proporcionar dependencias. {@code @InstallIn(SingletonComponent.class)}
 * especifica que las dependencias proporcionadas estarán disponibles en todo
 * el aplicativo.
 *
 * @see Module
 * @see InstallIn
 * @see SingletonComponent
 * @see FirebaseAuth
 * @see FirebaseFirestore
 */
@Module
@InstallIn(SingletonComponent.class)
public class FirebaseModule {

    /**
     * Proporciona la instancia singleton de Firebase Authentication.
     * <p>
     * Este método devuelve la instancia compartida de {@link FirebaseAuth}, que
     * se utiliza para gestionar la autenticación de usuarios en la aplicación.
     * Permite operaciones como registro, inicio de sesión, cierre de sesión y
     * gestión del usuario actual.
     * <p>
     * La anotación {@code @Singleton} garantiza que se utilice la misma instancia
     * de FirebaseAuth en toda la aplicación, evitando la creación de múltiples
     * instancias que podrían causar comportamientos inesperados.
     *
     * @return instancia singleton de {@link FirebaseAuth} configurada y lista para usar
     * @see FirebaseAuth#getInstance()
     * @see Singleton
     */
    @Provides
    @Singleton
    public static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    /**
     * Proporciona la instancia singleton de Firebase Firestore.
     * <p>
     * Este método devuelve la instancia compartida de {@link FirebaseFirestore},
     * que se utiliza como base de datos NoSQL en la nube para almacenar y
     * recuperar datos de usuarios, gastos, wishlist y otros datos persistentes.
     * <p>
     * La anotación {@code @Singleton} garantiza que se utilice la misma instancia
     * de Firestore en toda la aplicación, manteniendo la consistencia de los datos
     * y el estado de la conexión.
     *
     * @return instancia singleton de {@link FirebaseFirestore} configurada y lista para usar
     * @see FirebaseFirestore#getInstance()
     * @see Singleton
     */
    @Provides
    @Singleton
    public static FirebaseFirestore provideFirebaseFirestore() {
        return FirebaseFirestore.getInstance();
    }
}