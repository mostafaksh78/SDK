package com.nexo.sdk.dataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Buses")
public class Bus {
    public static final String ACTION = "BUS_ACTION";
    @NonNull
    @PrimaryKey
    private String token;
    private String type;
    private boolean upgrade;
    private String name;

    public Bus(@NonNull String token, String type, boolean upgrade, String name) {
        this.token = token;
        this.type = type;
        this.upgrade = upgrade;
        this.name = name;
    }

    public static String getTokenRoot(String token) {
        int index = token.lastIndexOf("M");
        return token.substring(0,index + 1);
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

    public boolean isUpgrade() {
        return upgrade;
    }

    public void setUpgrade(boolean upgrade) {
        this.upgrade = upgrade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
