package com.nexo.sdk.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.dataModels.Scenario;
import com.nexo.sdk.dataModels.User;

@Dao
public abstract class UserDao {
    @Insert
    public abstract void insert(User... users);
    @Delete
    public abstract void delete(User... users);
    @Update
    public abstract void update(User user);
    @Query("SELECT * FROM Users WHERE userID = :token")
    public abstract User getUser(String token);
    @Query("SELECT * FROM Users")
    public abstract User[] getAllUsers();
    @Query("DELETE FROM Users")
    public abstract void deleteAll();
    @Query("DELETE FROM Users WHERE userID =:userID")
    public abstract void deleteByUserId(String userID);
}
