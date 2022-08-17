package com.nexo.sdk.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.User;

import java.util.ArrayList;
import java.util.List;

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
    public  User[] getRelatives(User user){
        List<User> relatives = new ArrayList<>();
        int rule = user.getRule();
        switch (rule){
            case 1:
            case 0:{
                List<User> commonCustomer = Global.database.getUserDao().getCommonCustomer(user.getCustomerID());
                relatives.addAll(commonCustomer);
                break;
            }
            case 3:
            case 2:{
                relatives.add(user);
                break;
            }
        }
        return relatives.toArray(new User[0]);
    }
    @Query("SELECT * FROM Users WHERE customerID =:customerID")
    protected abstract List<User> getCommonCustomer(String customerID);
}
