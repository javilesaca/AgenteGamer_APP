package com.miapp.agentegamer.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.miapp.agentegamer.data.local.dao.GastoDao;
import com.miapp.agentegamer.data.local.dao.LanzamientoDao;
import com.miapp.agentegamer.data.local.dao.WishlistDao;
import com.miapp.agentegamer.data.local.entity.GastoEntity;
import com.miapp.agentegamer.data.local.entity.LanzamientoEntity;
import com.miapp.agentegamer.data.local.entity.WishlistEntity;

@Database(entities = {GastoEntity.class, WishlistEntity.class, LanzamientoEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract GastoDao gastoDao();
    public abstract WishlistDao wishlistDao();
    public abstract LanzamientoDao lanzamientoDao();


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "agente_gamer_db"
                    ).fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
