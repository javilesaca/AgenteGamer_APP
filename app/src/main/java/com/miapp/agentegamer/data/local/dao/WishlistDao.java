package com.miapp.agentegamer.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miapp.agentegamer.data.local.entity.WishlistEntity;

import java.util.List;

@Dao
public interface WishlistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WishlistEntity juego);

    @Query("SELECT * FROM wishlist")
    LiveData<List<WishlistEntity>> getWishlist();

    @Update
    void actualizar(WishlistEntity juego);

    @Delete
    void borrar(WishlistEntity juego);

    @Query("SELECT * FROM wishlist")
    List<WishlistEntity> getWishlistSync();

    @Query("SELECT SUM(precioEstimado) FROM wishlist")
    Double getTotalGastado();

}
