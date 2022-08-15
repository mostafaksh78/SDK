package com.nexo.sdk.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.dataModels.Device;
import com.nexo.sdk.dataModels.IfThen;

@Dao
public abstract class IfThenDao {
    @Insert
    public abstract void insert(IfThen... ifThens);
    @Delete
    public abstract void delete(IfThen... ifThens);
    @Update
    public abstract void update(IfThen ifThen);
    @Query("SELECT * FROM IfThen WHERE id = :token")
    public abstract IfThen getIfThen(String token);
    @Query("SELECT * FROM IfThen")
    public abstract IfThen[] getAllIfThen();
    @Query("DELETE FROM IfThen")
    public abstract void deleteAll();
    @Query("DELETE FROM IfThen WHERE id =:id")
    public abstract void deleteByID(String id);
}
