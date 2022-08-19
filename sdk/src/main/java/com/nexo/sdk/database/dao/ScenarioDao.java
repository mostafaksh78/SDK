package com.nexo.sdk.database.dao;

import android.os.Build;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.Scenario;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@Dao
public abstract class ScenarioDao {
    @Insert
    public abstract void insert(Scenario... scenarios);
    @Delete
    public abstract void delete(Scenario... scenarios);
    @Update
    public abstract void update(Scenario scenario);
    @Query("SELECT * FROM Scenarios WHERE id = :token")
    public abstract Scenario getScenario(String token);
    @Query("SELECT * FROM Scenarios")
    public abstract Scenario[] getAllScenario();

    @Query("SELECT * FROM Scenarios Where userIDs LIKE '%' || :userID || '%'")
    public abstract Scenario[] findUser(String userID);
    public void deleteUser(String userID) {
        Scenario[] remotes = findUser(userID);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (Scenario scenario : remotes) {
                    JSONArray userIDs = new JSONArray(scenario.getUserIDs());
                    for (int j = 0; j < userIDs.length(); j++) {
                        if (userID.equals(userIDs.getString(j))) {
                            userIDs.remove(j);
                            j--;
                        }
                    }
                    scenario.setUserIDs(userIDs.toString());
                    Global.database.getScenarioDao().update(scenario);
                }
            } else {
                for (Scenario scenario : remotes) {
                    JSONArray userIDs = new JSONArray(scenario.getUserIDs());
                    List<String> userList = new ArrayList<>();
                    for (int j = 0; j < userIDs.length(); j++) {
                        userList.add(userIDs.getString(j));
                    }
                    userList.remove(userID);
                    scenario.setUserIDs(new JSONArray(userList).toString());
                    Global.database.getScenarioDao().update(scenario);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Query("DELETE FROM Scenarios")
    public abstract void deleteAll();
    @Query("DELETE FROM Scenarios WHERE id =:scenarioID")
    public abstract void deleteByID(String scenarioID);
    @Query("SELECT * FROM Scenarios WHERE userIDs LIKE '%' || :userID || '%' AND NOT time = '-1'")
    public abstract void getSchedules(String userID);
    @Query("SELECT * FROM Scenarios WHERE userIDs LIKE '%' || :userID || '%' AND  time = '-1'")
    public abstract void getScenarios(String userID);
}
