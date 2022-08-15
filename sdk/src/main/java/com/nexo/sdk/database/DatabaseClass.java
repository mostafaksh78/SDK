package com.nexo.sdk.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.nexo.sdk.dataModels.Bus;
import com.nexo.sdk.dataModels.Device;
import com.nexo.sdk.dataModels.IfThen;
import com.nexo.sdk.dataModels.Remote;
import com.nexo.sdk.dataModels.Room;
import com.nexo.sdk.dataModels.Scenario;
import com.nexo.sdk.dataModels.User;
import com.nexo.sdk.dataModels.Voice;
import com.nexo.sdk.database.dao.BusDao;
import com.nexo.sdk.database.dao.DeviceDao;
import com.nexo.sdk.database.dao.IfThenDao;
import com.nexo.sdk.database.dao.RemoteDao;
import com.nexo.sdk.database.dao.RoomDao;
import com.nexo.sdk.database.dao.ScenarioDao;
import com.nexo.sdk.database.dao.UserDao;
import com.nexo.sdk.database.dao.VoiceDao;

@Database(entities = {Bus.class  , Device.class , IfThen.class, Remote.class , Room.class , Scenario.class, User.class, Voice.class}, version = 1)
public abstract class DatabaseClass extends RoomDatabase {
    public abstract BusDao getBusDao();
    public abstract DeviceDao getDeviceDao();
    public abstract IfThenDao getIfThenDao();
    public abstract RoomDao getRoomDao();
    public abstract ScenarioDao getScenarioDao();
    public abstract UserDao getUserDao();
    public abstract VoiceDao getVoiceDao();

    public abstract RemoteDao getRemoteDao();
}
