package com.nexo.sdk.database.dao;

import android.os.Build;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.Voice;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class VoiceDao {
    @Insert
    public abstract void insert(Voice... voices);
    @Delete
    public abstract void delete(Voice... voices);
    @Update
    public abstract void update(Voice voice);
    @Query("SELECT * FROM VoiceCommands WHERE soundID = :token")
    public abstract Voice getVoice(String token);
    @Query("SELECT * FROM VoiceCommands")
    public abstract Voice[] getAllVoices();

    @Query("SELECT * FROM VoiceCommands Where userIDs LIKE '%' || :userID || '%'")
    public abstract Voice[] findUser(String userID);
    public void deleteUser(String userID) {
        Voice[] voices = findUser(userID);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Voice voice : voices) {
                    JSONArray userIDs = new JSONArray(voice.getUserIDs());
                    for (int j = 0; j < userIDs.length(); j++) {
                        if (userID.equals(userIDs.getString(j))) {
                            userIDs.remove(j);
                            j--;
                        }
                    }
                    voice.setUserIDs(userIDs.toString());
                    Global.database.getVoiceDao().update(voice);
                }
            }else {
                for (Voice room : voices) {
                    JSONArray userIDs = new JSONArray(room.getUserIDs());
                    List<String> userList = new ArrayList<>();
                    for (int j = 0; j < userIDs.length(); j++) {
                        userList.add(userIDs.getString(j));
                    }
                    userList.remove(userID);
                    room.setUserIDs(new JSONArray(userList).toString());
                    Global.database.getVoiceDao().update(room);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Query("DELETE FROM VoiceCommands")
    public abstract void deleteAll();
}
