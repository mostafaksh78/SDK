package com.nexo.sdk.database.dao;

import android.os.Build;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.Remote;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class RemoteDao  {
    @Insert
    public abstract void insert(Remote... remotes);
    @Delete
    public abstract void delete(Remote... remotes);
    @Update
    public abstract void update(Remote remote);
    @Query("SELECT * FROM Remotes WHERE id = :token")
    public abstract Remote getRemote(String token);
    @Query("SELECT * FROM Remotes")
    public abstract Remote[] getAllRemotes();
    @Query("SELECT * FROM Remotes Where userIDs LIKE '%' || :userID || '%'")
    public abstract Remote[] findUser(String userID);
    public void deleteUser(String userID) {
        Remote[] remotes = findUser(userID);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Remote remote : remotes) {
                    JSONArray userIDs = new JSONArray(remote.getUserIDs());
                    for (int j = 0; j < userIDs.length(); j++) {
                        if (userID.equals(userIDs.getString(j))) {
                            userIDs.remove(j);
                            j--;
                        }
                    }
                    remote.setUserIDs(userIDs.toString());
                    Global.database.getRemoteDao().update(remote);
                }
            } else {
                for (Remote remote : remotes) {
                    JSONArray userIDs = new JSONArray(remote.getUserIDs());
                    List<String> userList = new ArrayList<>();
                    for (int j = 0; j < userIDs.length(); j++) {
                        userList.add(userIDs.getString(j));
                    }
                    userList.remove(userID);
                    remote.setUserIDs(new JSONArray(userList).toString());
                    Global.database.getRemoteDao().update(remote);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Query("SELECT id FROM Remotes")
    public abstract List<String> getAllIds();
    @Query("DELETE FROM Remotes WHERE id NOT IN (:v)")
    public abstract void clearOthers(List<String> v);
    @Query("UPDATE Remotes SET label =:name, type =:type ,model =:model , brand =:brand , userIDs =:users , protocol =:protocol , userMake =:userMake , irID =:irID , roomID =:roomID WHERE id =:id")
    public abstract void updateWithoutKey(String id, String name, String type, String model, String brand, String users, String protocol, String userMake, String irID, String roomID);
    @Query("UPDATE Remotes SET keys =:s WHERE id =:remoteID")
    public abstract void updateKey(String remoteID, String s);
    @Query("DELETE FROM Remotes WHERE id =:id")
    public abstract void deleteByID(String id);
    @Query("SELECT * FROM Remotes Where userIDs LIKE '%' || :userID || '%' AND roomID =:roomID")
    public abstract List<Remote> getRoomRemotes(String userID,String roomID);
    @Query("SELECT model FROM Remotes Where userIDs LIKE '%' || :userID || '%'")
    public abstract String[] getModels(String userID);
    @Query("SELECT * FROM Remotes Where userIDs LIKE '%' || :userID || '%' AND model =:model")
    public abstract Remote[] findUserAndModel(String userID,String model);
    @Query("DELETE FROM Remotes")
    public abstract void deleteAll();

    public  void setRooms(String roomID, String[] tokens){
        for (String token :
                tokens) {
            Global.database.getRemoteDao().updateRoom(roomID,token);
        }
    }
    @Query("UPDATE Remotes SET roomID =:roomID WHERE id =:token")
    public abstract void updateRoom(String roomID,String token);
    @Query("UPDATE Remotes SET roomID=\"-1\" WHERE roomID =:roomID AND  id NOT IN (:tokensInRoom)")
    public abstract void clearRoom(String roomID, String[] tokensInRoom);
}
