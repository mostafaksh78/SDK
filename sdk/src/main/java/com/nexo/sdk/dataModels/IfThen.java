package com.nexo.sdk.dataModels;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "IfThen")
public class IfThen {
    public static final String ACTION = "IFTHEN_ACTION";

    @NonNull
    @PrimaryKey
    private String id;
    private String name;
    private String status;
    private String user;
    private String ifState;
    private String thenState;

    public IfThen(@NonNull String id, String name, String status, String user, String ifState, String thenState) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.user = user;
        this.ifState = ifState;
        this.thenState = thenState;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getIfState() {
        return ifState;
    }

    public void setIfState(String ifState) {
        this.ifState = ifState;
    }

    public String getThenState() {
        return thenState;
    }

    public void setThenState(String thenState) {
        this.thenState = thenState;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IfThen device = (IfThen) o;
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

