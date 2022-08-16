package com.nexo.sdk.dataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Rooms")
public class Room {
    public static final String ACTION = "ROOM_ACTION";

    @NonNull
    @PrimaryKey
    private String id;
    private String userIDs;
    private String name;
    private int icon;

    public Room(@NonNull String id, String userIDs, String name, int icon) {
        this.id = id;
        this.userIDs = userIDs;
        this.name = name;
        this.icon = icon;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(String userIDs) {
        this.userIDs = userIDs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
