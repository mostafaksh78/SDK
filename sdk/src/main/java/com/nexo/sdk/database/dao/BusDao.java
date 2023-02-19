package com.nexo.sdk.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.dataModels.Bus;

@Dao
public abstract class BusDao {
    @Insert
    public abstract void insert(Bus... buses);
    @Delete
    public abstract void delete(Bus... buses);
    @Update
    public abstract void update(Bus buses);
    @Query("SELECT * FROM Buses WHERE token = :token")
    public abstract Bus getBuses(String token);
    @Query("SELECT * FROM Buses")
    public abstract Bus[] getAllBuses();
    @Query("DELETE FROM Buses")
    public abstract void deleteAll();
    @Query("DELETE FROM Buses WHERE token =:token")
    public abstract void deleteByID(String token);
    @Query("DELETE FROM Buses WHERE type =:type")
    public abstract void deleteType(String type);
    @Query("SELECT EXISTS(SELECT * FROM Buses WHERE token = :token)")
    public abstract void ifExist(String token);
}
