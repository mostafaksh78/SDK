package com.nexo.sdk.dataModels;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Remotes")
public class Remote {
    public static final String ACTION = "REMOTE_ACTION";

    @NonNull
    @PrimaryKey
    private String id;
    private String label;
    private String type;
    private String keys;
    private String model;
    private String brand;
    private String userIDs;
    private String protocol;
    private String userMake;
    private String irID;
    private String roomID;

    public Remote(@NonNull String id, String label, String type, String keys, String model, String brand, String userIDs, String protocol, String userMake, String irID, String roomID) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.keys = keys;
        this.model = model;
        this.brand = brand;
        this.userIDs = userIDs;
        this.protocol = protocol;
        this.userMake = userMake;
        this.irID = irID;
        this.roomID = roomID;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(String userIDs) {
        this.userIDs = userIDs;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUserMake() {
        return userMake;
    }

    public void setUserMake(String userMake) {
        this.userMake = userMake;
    }

    public String getIrID() {
        return irID;
    }

    public void setIrID(String irID) {
        this.irID = irID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Remote device = (Remote) o;
        return id.equals(device.id);
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(id);
        }
        return id.length();
    }
}
