package com.nexo.sdk.dataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "VoiceCommands")
public class Voice {
    public static final String ACTION = "VOICE_ACTION";

    @NonNull
    @PrimaryKey
    private String soundID;
    private String name;
    private String deviceTokens;
    private String deviceStatuses;
    private String userIDs;
    private String letter;

    public Voice(@NonNull String soundID, String name, String deviceTokens, String deviceStatuses, String userIDs, String letter) {
        this.soundID = soundID;
        this.name = name;
        this.deviceTokens = deviceTokens;
        this.deviceStatuses = deviceStatuses;
        this.userIDs = userIDs;
        this.letter = letter;
    }

    @NonNull
    public String getSoundID() {
        return soundID;
    }

    public void setSoundID(@NonNull String soundID) {
        this.soundID = soundID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceTokens() {
        return deviceTokens;
    }

    public void setDeviceTokens(String deviceTokens) {
        this.deviceTokens = deviceTokens;
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

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
