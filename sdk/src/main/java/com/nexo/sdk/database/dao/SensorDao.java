package com.nexo.sdk.database.dao;

import android.os.Build;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.Device;
import com.nexo.sdk.dataModels.Sensor;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public abstract class SensorDao {
    @Insert
    public abstract void insert(Sensor... devices);
    @Delete
    public abstract void delete(Sensor... devices);
    @Update
    public abstract void update(Sensor devices);
    @Query("SELECT * FROM Sensors WHERE token = :token")
    public abstract Sensor getDevice(String token);
    @Query("SELECT * FROM Sensors")
    public abstract Sensor[] getAllDevices();
    @Query("DELETE FROM Sensors")
    public abstract void deleteAll();

    @Query("SELECT EXISTS(SELECT * FROM Sensors WHERE token = :token)")
    public abstract boolean ifExist(String token);
    @Query("UPDATE Sensors SET type = :type , label =:label , userIDs =:users,userIDs =:users,userIDs =:users,status =:status,roomID =:roomID,upgrade=:update,fastAccess =:fastAccess,ssid=:ssid,password=:password WHERE token =:token")
    public abstract void updateWithoutOnline(String token, String type, String label, String users, String status, String roomID, boolean update, boolean fastAccess, String ssid, String password);
    @Query("DELETE FROM Sensors WHERE token=:token")
    public abstract void deleteWithToken(String token);
    @Query("UPDATE Sensors SET status = :status WHERE token =:token")
    public abstract void updateStatus(String token, String status);
    @Query("UPDATE Sensors SET online = :s WHERE token =:token")
    public abstract void updateConnection(String token, boolean s);
    @Query("SELECT * FROM Sensors Where userIDs LIKE '%' || :userID || '%'")
    public abstract Sensor[] findUser(String userID);
    public void deleteUser(String userID){
        Sensor[] devices = findUser(userID);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Sensor device : devices) {
                    JSONArray userIDs = new JSONArray(device.getUserIDs());
                    for (int j = 0; j < userIDs.length(); j++) {
                        if (userID.equals(userIDs.getString(j))) {
                            userIDs.remove(j);
                            j--;
                        }
                    }
                    device.setUserIDs(userIDs.toString());
                    Global.database.getSensorDao().update(device);
                }
            }else {
                for (Sensor device : devices) {
                    JSONArray userIDs = new JSONArray(device.getUserIDs());
                    List<String> userList = new ArrayList<>();
                    for (int j = 0; j < userIDs.length(); j++) {
                        userList.add(userIDs.getString(j));
                    }
                    userList.remove(userID);
                    device.setUserIDs(new JSONArray(userList).toString());
                    Global.database.getSensorDao().update(device);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public  void setRooms(String roomID, String[] tokens){
        for (String token :
                tokens) {
            Global.database.getSensorDao().updateRoom(roomID,token);
        }
    }
    @Query("UPDATE Sensors SET roomID =:roomID WHERE token =:token")
    public abstract void updateRoom(String roomID,String token);

    @Query("UPDATE Sensors SET online =:online WHERE token =:token")
    public abstract void updateOnline(String token, boolean online);
    @Query("UPDATE Sensors SET ssid =:ssid , password =:pass WHERE token IN (:relayIDs)")
    public abstract void updateSSIDandPassword(String ssid, String pass, List<String> relayIDs);
    @Query("DELETE FROM Sensors WHERE token IN (:relayIDs)")
    public abstract void deleteThem(List<String> relayIDs);
    @Query("SELECT * FROM Sensors Where fastAccess = 1")
    public abstract Sensor[] getAllFastAccess();
    @Query("SELECT * FROM Sensors Where userIDs LIKE '%' || :userID || '%' AND roomID =:roomID")
    public abstract List<Sensor> getRoomDevices(String userID,String roomID);
    @Query("SELECT type FROM Sensors Where userIDs LIKE '%' || :userID || '%'")
    public abstract List<String> findCategories(String userID);

    @Query("SELECT * FROM Sensors Where userIDs LIKE '%' || :userID || '%' AND type =:type")
    public abstract List<Sensor> getDeviceByTypeAndUser(String type,String userID);
    @Query("UPDATE Sensors SET upgrade =:b where token =:token")
    public abstract void setUpgrade(String token, boolean b);
    @Query("UPDATE Sensors SET roomID =:roomID WHERE roomID=:preRoomID")
    public abstract void setRoomIDofRoom(String roomID,String preRoomID);
    @Query("SELECT * FROM Sensors WHERE roomID=:roomID")
    public abstract Sensor[] getRoomDevices(String roomID);
    @Query("DELETE FROM Sensors where type=:type")
    public abstract void deleteType(String type);
}
