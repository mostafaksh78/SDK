package com.nexo.sdk.dataModels;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity (tableName = "Scenarios")
public class Scenario {
    public static final String ACTION = "SCENARIO_ACTION";

    @NonNull
    @PrimaryKey
    private String id;
    private String name;
    private String time;
    private String status;
    private String tokens;
    private String deviceStatuses;
    private String userIDs;

    public Scenario(@NonNull String id, String name, String time, String status, String tokens, String deviceStatuses, String userIDs) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.status = status;
        this.tokens = tokens;
        this.deviceStatuses = deviceStatuses;
        this.userIDs = userIDs;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public String getDeviceStatuses() {
        return deviceStatuses;
    }

    public void setDeviceStatuses(String deviceStatuses) {
        this.deviceStatuses = deviceStatuses;
    }

    public String getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(String userIDs) {
        this.userIDs = userIDs;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scenario device = (Scenario) o;
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
