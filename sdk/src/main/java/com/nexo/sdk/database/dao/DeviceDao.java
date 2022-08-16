package com.nexo.sdk.database.dao;

import android.os.Build;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.Device;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class DeviceDao {
    @Insert
    public abstract void insert(Device... devices);
    @Delete
    public abstract void delete(Device... devices);
    @Update
    public abstract void update(Device devices);
    @Query("SELECT * FROM Devices WHERE token = :token")
    public abstract Device getDevice(String token);
    @Query("SELECT * FROM Devices")
    public abstract Device[] getAllDevices();
    @Query("DELETE FROM Devices")
    public abstract void deleteAll();

    public boolean ifExist(String token) {
        Device device = getDevice(token);
        return device != null;
    }
    @Query("UPDATE Devices SET type = :type , label =:label , userIDs =:users,userIDs =:users,userIDs =:users,status =:status,roomID =:roomID,upgrade=:update,fastAccess =:fastAccess,ssid=:ssid,password=:password WHERE token =:token")
    public abstract void updateWithoutOnline(String token, String type, String label, String users, String status, String roomID, boolean update, boolean fastAccess, String ssid, String password);
    @Query("DELETE FROM Devices WHERE token=:token")
    public abstract void deleteWithToken(String token);
    @Query("UPDATE Devices SET status = :status WHERE token =:token")
    public abstract void updateStatus(String token, String status);
    @Query("UPDATE Devices SET online = :s WHERE token =:token")
    public abstract void updateConnection(String token, boolean s);
    @Query("SELECT * FROM Devices Where userIDs LIKE '%' || :userID || '%'")
    public abstract Device[] findUser(String userID);
    public void deleteUser(String userID){
        Device[] devices = findUser(userID);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Device device : devices) {
                    JSONArray userIDs = new JSONArray(device.getUserIDs());
                    for (int j = 0; j < userIDs.length(); j++) {
                        if (userID.equals(userIDs.getString(j))) {
                            userIDs.remove(j);
                            j--;
                        }
                    }
                    device.setUserIDs(userIDs.toString());
                    Global.database.getDeviceDao().update(device);
                }
            }else {
                for (Device device : devices) {
                    JSONArray userIDs = new JSONArray(device.getUserIDs());
                    List<String> userList = new ArrayList<>();
                    for (int j = 0; j < userIDs.length(); j++) {
                        userList.add(userIDs.getString(j));
                    }
                    userList.remove(userID);
                    device.setUserIDs(new JSONArray(userList).toString());
                    Global.database.getDeviceDao().update(device);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public  void setRooms(String roomID, String[] tokens){
        for (String token :
                tokens) {
            Global.database.getDeviceDao().updateRoom(roomID,token);
        }
    }
    @Query("UPDATE Devices SET roomID =:roomID WHERE token =:token")
    public abstract void updateRoom(String roomID,String token);

    @Query("UPDATE Devices SET online =:online WHERE token =:token")
    public abstract void updateOnline(String token, boolean online);
    @Query("UPDATE Devices SET ssid =:ssid , password =:pass WHERE token IN (:relayIDs)")
    public abstract void updateSSIDandPassword(String ssid, String pass, List<String> relayIDs);
    @Query("DELETE FROM Devices WHERE token IN (:relayIDs)")
    public abstract void deleteThem(List<String> relayIDs);
}