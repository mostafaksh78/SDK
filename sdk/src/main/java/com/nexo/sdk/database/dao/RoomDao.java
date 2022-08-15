package com.nexo.sdk.database.dao;

import android.os.Build;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.Room;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class RoomDao {
    @Insert
    public abstract void insert(Room... rooms);
    @Delete
    public abstract void delete(Room... rooms);
    @Update
    public abstract void update(Room room);
    @Query("SELECT * FROM Rooms WHERE id = :token")
    public abstract Room getRoom(String token);
    @Query("SELECT * FROM Rooms")
    public abstract Room[] getAllRoom();
    @Query("SELECT * FROM Rooms Where userIDs LIKE '%' || :userID || '%'")
    public abstract Room[] findUser(String userID);
    public void deleteUser(String userID) {
        Room[] rooms = findUser(userID);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Room room : rooms) {
                    JSONArray userIDs = new JSONArray(room.getUserIDs());
                    for (int j = 0; j < userIDs.length(); j++) {
                        if (userID.equals(userIDs.getString(j))) {
                            userIDs.remove(j);
                            j--;
                        }
                    }
                    room.setUserIDs(userIDs.toString());
                    Global.database.getRoomDao().update(room);
                }
            }else {
                for (Room room : rooms) {
                    JSONArray userIDs = new JSONArray(room.getUserIDs());
                    List<String> userList = new ArrayList<>();
                    for (int j = 0; j < userIDs.length(); j++) {
                        userList.add(userIDs.getString(j));
                    }
                    userList.remove(userID);
                    room.setUserIDs(new JSONArray(userList).toString());
                    Global.database.getRoomDao().update(room);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Query("DELETE FROM Rooms WHERE id =:roomID")
    public abstract void deleteWithRoomID(String roomID);
    @Query("DELETE FROM Rooms")
    public abstract void deleteAll();
}
