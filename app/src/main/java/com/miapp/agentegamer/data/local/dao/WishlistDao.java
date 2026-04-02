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

    @Query("SELECT * FROM wishlist WHERE userId = :userId")
    LiveData<List<WishlistEntity>> getWishlist(String userId);

    @Update
    void actualizar(WishlistEntity juego);

    @Delete
    void borrar(WishlistEntity juego);

    @Query("SELECT * FROM wishlist WHERE userId = :userId")
    List<WishlistEntity> getWishlistSync(String userId);

    @Query("SELECT SUM(precioEstimado) FROM wishlist WHERE userId = :userId")
    Double getTotalGastado(String userId);

    @Query("DELETE FROM wishlist WHERE userId = :userId")
    void deleteAll(String userId);

}
