package com.nexo.sdk.dataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "Devices")
public class Device {
    public static final String ACTION = "DEVICE_ACTION";

    @NonNull
    @PrimaryKey
    private String token;
    private String type;
    private String label;
    private String status;
    private String roomID;
    private String userIDs;
    private boolean fastAccess;
    private boolean upgrade;
    private String ssid;
    private String password;
    private boolean online;

    public Device(@NonNull String token, String type, String label, String status, String roomID, String userIDs, boolean fastAccess, boolean upgrade, String ssid, String password, boolean online) {
        this.token = token;
        this.type = type;
        this.label = label;
        this.status = status;
        this.roomID = roomID;
        this.userIDs = userIDs;
        this.fastAccess = fastAccess;
        this.upgrade = upgrade;
        this.ssid = ssid;
        this.password = password;
        this.online = online;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    public void setToken(@NonNull String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(String userIDs) {
        this.userIDs = userIDs;
    }

    public boolean isFastAccess() {
        return fastAccess;
    }

    public void setFastAccess(boolean fastAccess) {
        this.fastAccess = fastAccess;
    }

    public boolean getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
