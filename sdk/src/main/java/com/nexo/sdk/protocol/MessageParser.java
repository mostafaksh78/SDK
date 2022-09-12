package com.nexo.sdk.protocol;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nexo.sdk.Global;
import com.nexo.sdk.dataModels.Bus;
import com.nexo.sdk.dataModels.Device;
import com.nexo.sdk.dataModels.IfThen;
import com.nexo.sdk.dataModels.Remote;
import com.nexo.sdk.dataModels.Room;
import com.nexo.sdk.dataModels.Scenario;
import com.nexo.sdk.dataModels.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageParser implements Runnable {
    private final Context context;
    private String message;

    public MessageParser(Context context, String message) {
        this.context = context;
        this.message = message;
    }

    public MessageParser(Context context) {
        this.context = context;
    }

    protected void doInBackground(String message) {
        try {
            Protocol protocol = new Protocol(new JSONObject(message));
            switch (protocol.getCommand()){
                case 1:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            JSONObject data = protocol.getData();
                            if (data.has("Result")) {
                                String token = "NOT_DEFINED";
                                if (data.has("Token")) {
                                    token = data.getString("Token");
                                }
                                String result = data.getString("Result");
                            }
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject object = new JSONObject(message);
                            String dataS = object.getString("Data");
                            if (!dataS.equals("")) {
                                JSONObject data = protocol.getData();
                                if (!data.toString().equals("{}")) {
                                    if (data.has("Result")) {
                                        String token = "NOT_DEFINED";
                                        if (data.has("Token")) {
                                            token = data.getString("Token");
                                            Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.JOB,Global.RESULT).putExtra(Global.ID,token).putExtra(Global.TEXT,data.getString("Result")));
                                        }

                                    }
                                    else if (data.has("Devices")) {
                                        Global.database.getDeviceDao().deleteAll();
                                        JSONArray devices = data.getJSONArray("Devices");
                                        for (int i = 0; i < devices.length(); i++) {
                                            try {
                                                JSONObject device = devices.getJSONObject(i);
                                                String token = "";
                                                try {
                                                    token = device.getString("Device_Token");
                                                } catch (JSONException e) {
                                                    token = device.getString("Token");
                                                }
                                                String type = token.split("M")[0];
                                                boolean update = device.getBoolean("Update");
                                                String ssid, password;
                                                password = device.getString("Password");
                                                ssid = device.getString("SSID");
                                                String deviceLabel = device.getString("Label");
                                                String deviceStatus = null;
                                                if (device.has("Devices_Status")){
                                                    deviceStatus = device.getString("Devices_Status");
                                                }else{
                                                    deviceStatus = device.getString("Status");
                                                }
//
                                                boolean fastAccess = (device.getString("EasyMode").equals("1") || device.getString("EasyMode").equals("true"));
                                                String roomID = ((device.getString("RoomID").equals("null") || "".equals(device.getString("RoomID"))) ? "-1" : device.getString("RoomID"));
                                                JSONArray users = device.getJSONArray("Users");
                                                Log.i("RoomId", roomID);
                                                boolean online = device.getString("Online").equals("1");
                                                Device device1 = new Device(token,type,deviceLabel,deviceStatus,roomID,users.toString(),fastAccess,update,ssid,password,online);
                                                if (Global.database!=null){
                                                    Global.database.getDeviceDao().insert(device1);
                                                }
                                            } catch (JSONException | NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Global.database.getDeviceDao().deleteType("SB8");
                                        Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.JOB,Global.ALL));
                                    }
                                    else if (data.has("Job")) {
                                        String job = data.getString("Job");
                                        Log.i("CommandDebug", "Command 1 : " + job);
                                        switch (job) {
                                            case "Update": {
                                                String token = data.getString("Token");
                                                String label = data.getString("Label");
                                                JSONArray users = data.getJSONArray("Users");
                                                String status = data.getString("Status");
                                                String roomID = data.getString("RoomID");
                                                boolean update = data.getBoolean("Update");
                                                boolean fastAccess = data.getBoolean("EasyMode");
                                                String ssid = data.getString("SSID");
                                                String password = data.getString("Password");
                                                String online = "";
                                                String type = token.split("M")[0];
                                                if (Global.database.getDeviceDao().ifExist(token)) {
                                                    Log.d("RegisterDebugger", "UPDATE " + token);
                                                    Global.database.getDeviceDao().updateWithoutOnline(token,type,label,users.toString(),status,roomID,update,fastAccess,ssid,password);
                                                } else {
                                                    Log.d("RegisterDebugger", "INSERT : " + token);
                                                    if (data.has("Online")) {
                                                        online = data.getString("Online");
                                                        Device device = new Device(token, type, label, status, roomID, users.toString(), fastAccess, update, ssid, password, online.equals("1"));
                                                        Global.database.getDeviceDao().insert(device);

                                                    }else{
                                                        Device device = new Device(token, type, label, status, roomID, users.toString(), fastAccess, update, ssid, password, false);
                                                        Global.database.getDeviceDao().insert(device);
                                                    }
                                                }
                                                Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.ID,token).putExtra(Global.JOB,Global.UPDATE));
                                                break;
                                            }
                                            case "Delete": {
                                                String token = data.getString("Token");
                                                Global.database.getDeviceDao().deleteWithToken(token);
                                                Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.ID,token).putExtra(Global.JOB,Global.DELETE));
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
                case 2:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            JSONObject data = protocol.getData();
                            Log.i("RsultCom24", "sad");
                            if (data.has("Result")) {
                                String token = "NOT_DEFINED";
                                if (data.has("Token")) {
                                    token = data.getString("Token");
                                    String result = data.getString("Result");
                                    String status = data.getString("Status");
                                    Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.JOB,Global.RESULT).putExtra(Global.ID,token).putExtra(Global.STATUS,status).putExtra(Global.TEXT,result));
                                }
                            }
                            break;
                        }
                        case Protocol.Methods.requestGet: {
                            Device[] devices = Global.database.getDeviceDao().getAllDevices();
                            for (Device device : devices) {
                                String token = device.getToken();
                                String status = device.getStatus();
                                JSONObject data = new JSONObject().put("Token", token).put("Status", status);
                                Protocol protocol1 = new Protocol(2, Protocol.Methods.requestSet, data);
                                // TODO
                            }
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            if (data.has("Result")) {
                                String token = "NOT_DEFINED";
                                if (data.has("Token")) {
                                    token = data.getString("Token");
                                    String result = data.getString("Result");
                                    String status = data.getString("Status");
                                    Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.JOB,Global.RESULT).putExtra(Global.ID,token).putExtra(Global.STATUS,status).putExtra(Global.TEXT,result));

                                }
                            }else if (data.has("Token")) {
                                String token = data.getString("Token");
                                if (data.has("Status")) {
                                    Global.database.getDeviceDao().updateStatus(token, data.getString("Status"));
                                    Intent intent = new Intent(Device.ACTION);
                                    intent.putExtra(Global.JOB, Global.UPDATE);
                                    intent.putExtra(Global.ID,token);
                                    Global.manager.sendBroadcast(intent);
                                } else if (data.has("Status_Connection")) {
                                    boolean connection = data.getBoolean("Status_Connection");
                                    Global.database.getDeviceDao().updateConnection(token, connection);
                                    Intent intent = new Intent(Device.ACTION);
                                    intent.putExtra(Global.JOB, Global.CONNECTION_ACTION);
                                    intent.putExtra(Global.ID,token);
                                    intent.putExtra(Global.CONNECTION,connection);
                                    Global.manager.sendBroadcast(intent);
                                }
                            }else if (data.has("Status_Connection")) {
                                boolean connection = data.getBoolean("Status_Connection");
                                Intent intent = new Intent(Device.ACTION);
                                intent.putExtra(Global.JOB, Global.CONNECTION_ACTION);
                                intent.putExtra(Global.CONNECTION,connection);
                                Global.manager.sendBroadcast(intent);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 3:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            if (!data.toString().equals("{}")) {
                                try {
                                    if (data.has("Users")) {
                                        JSONArray usersArray = data.getJSONArray("Users");
                                        Global.database.getUserDao().deleteAll();
                                        for (int i = 0; i < usersArray.length(); i++) {
                                            JSONObject user = usersArray.getJSONObject(i);
                                            String userID = user.getString("UserID");
                                            String user_name = user.getString("User_Name");
                                            String passWord = user.getString("Pass");
                                            String customerID = user.getString("Customer_ID");
                                            String name = user.getString("Name");
                                            String family = user.getString("Family");
                                            String email = user.getString("Email");
                                            int rule = user.getInt("Role");
                                            String phoneNumber = user.getString("Phone");
                                            String gender = user.getString("Gender");
                                            String question = user.getString("Question");
                                            String answer = user.getString("Answer");
                                            String birthDay = user.getString("Birthdate");
                                            User user1 = new User(userID, user_name, passWord, rule,customerID,name,family,phoneNumber,email,birthDay,gender,question,answer );
                                            Global.database.getUserDao().insert(user1);
                                        }
                                        Intent intent = new Intent(User.ACTION);
                                        intent.putExtra(Global.JOB, Global.ALL);
                                        Global.manager.sendBroadcast(intent);
                                    } else if (data.has("Job")) {
                                        switch (data.getString("Job")) {
                                            case "Update": {
                                                Log.i("UserResponceCheck", "Update");
                                                try {
                                                    String user_name = data.getString("User_Name");
                                                    String userID = data.getString("UserID");
                                                    String passWord = data.getString("Pass");
                                                    String customerID = data.getString("Customer_ID");
                                                    String name = data.getString("Name");
                                                    String family = data.getString("Family");
                                                    String email = data.getString("Email");
                                                    int rule = data.getInt("Role");
                                                    String phoneNumber = data.getString("Phone");
                                                    String gender = data.getString("Gender");
                                                    String question = data.getString("Question");
                                                    String answer = data.getString("Answer");
                                                    String birthDay = data.getString("Birthdate");
                                                    User user = new User(userID, user_name, passWord, rule, customerID, name, family, phoneNumber, email, birthDay, gender, question, answer);
                                                    Global.database.getUserDao().update(user);
                                                    Intent intent = new Intent(User.ACTION);
                                                    intent.putExtra(Global.JOB, Global.UPDATE);
                                                    intent.putExtra(Global.ID, userID);
                                                    Global.manager.sendBroadcast(intent);
                                                } catch (Exception ex) {
                                                    Log.i("MyCustomUser", ex.toString());
                                                }
                                                break;
                                            }
                                            case "Insert": {
                                                String user_name = data.getString("User_Name");
                                                String userID = data.getString("UserID");
                                                String passWord = data.getString("Pass");
                                                String customerID = data.getString("Customer_ID");
                                                String name = data.getString("Name");
                                                String family = data.getString("Family");
                                                String email = data.getString("Email");
                                                int rule = data.getInt("Role");
                                                String phoneNumber = data.getString("Phone");
                                                String gender = data.getString("Gender");
                                                String question = data.getString("Question");
                                                String answer = data.getString("Answer");
                                                String birthDay = data.getString("Birthdate");
                                                User user = new User(userID, user_name, passWord, rule, customerID, name, family, phoneNumber, email, birthDay, gender, question, answer);
                                                Global.database.getUserDao().insert(user);
                                                Intent intent = new Intent(User.ACTION);
                                                intent.putExtra(Global.JOB, Global.INSERT);
                                                intent.putExtra(Global.ID, userID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Delete": {
                                                String userID = data.getString("UserID");
                                                Global.database.getUserDao().deleteByUserId(userID);
                                                Global.database.getDeviceDao().deleteUser(userID);
                                                Global.database.getRoomDao().deleteUser(userID);
                                                Global.database.getVoiceDao().deleteUser(userID);
                                                Global.database.getRemoteDao().deleteUser(userID);
                                                Global.database.getScenarioDao().deleteUser(userID);
                                                Intent intent = new Intent(User.ACTION);
                                                intent.putExtra(Global.JOB, Global.DELETE);
                                                intent.putExtra(Global.ID, userID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Insert New":{
                                                String userID = data.getString("UserID");
                                                String phoneNumber = data.getString("Phone");
                                                Intent intent = new Intent(User.ACTION);
                                                intent.putExtra(Global.JOB, Global.NEW_USER);
                                                intent.putExtra(Global.PHONE,phoneNumber);
                                                intent.putExtra(Global.ID,userID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                        }
                                    } else if (data.has("Result")){
                                        String result = data.getString("Result");
                                        if (result.equals("UserName Exist")){
                                            Intent intent = new Intent(User.ACTION);
                                            intent.putExtra(Global.JOB, Global.ERROR);
                                            intent.putExtra(Global.TEXT,Global.USER_NAME_EXIST) ;
                                            Global.manager.sendBroadcast(intent);
                                        }else if (result.equals("Phone Exist")){
                                            Intent intent = new Intent(User.ACTION);
                                            intent.putExtra(Global.JOB, Global.ERROR);
                                            intent.putExtra(Global.TEXT,Global.PHONE_EXSIT) ;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.d("LoginDebug","EXP");
                                }
                            } else {
                                Log.d("LoginDebug","Send broadcast ");
                                Intent intent = new Intent(User.ACTION);
                                intent.putExtra(Global.JOB, Global.ALL);
                                Global.manager.sendBroadcast(intent);
                            }
                            break;
                        }
                    }
                    break;
                }
                case 5:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            Log.i("Authentication", "WRONG");
                            Global.manager.sendBroadcast(new Intent(Global.AUTH_ACTION).putExtra(Global.RESULT,false));
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.responseGet: {
                            Log.i("Authentication", "Logged IN");
                            String userID = protocol.getData().getString("UserID");
                            Global.manager.sendBroadcast(new Intent(Global.AUTH_ACTION).putExtra(Global.RESULT,true).putExtra(Global.ID,userID));
                            break;
                        }
                    }
                    break;
                }
                case 7:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            JSONArray success = data.getJSONArray("Success");
                            JSONArray fail = data.getJSONArray("Fail");
                            for (int i = 0; i < success.length(); i++) {
                                String token = success.getString(i);
                                Global.database.getDeviceDao().setUpgrade(token,false);
                            }
                            for (int i = 0; i < fail.length(); i++) {
                                String token = fail.getString(i);
                                Global.database.getDeviceDao().setUpgrade(token,true);
                            }
                            Intent intent = new Intent(Device.ACTION);
                            intent.putExtra(Global.JOB,Global.OTA);
                            intent.putExtra(Global.SUCCESS,success.toString());
                            intent.putExtra(Global.FAIL,fail.toString());
                            Global.manager.sendBroadcast(intent);
                            break;
                        }
                    }
                }
                case 9:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject object = new JSONObject(message);
                            String dataS = object.getString("Data");
                            if (!dataS.equals("")) {
                                JSONObject data = protocol.getData();
                                if (!data.toString().equals("{}")) {
                                   if (data.has("Rooms")) {
                                        Global.database.getRoomDao().deleteAll();
                                        JSONArray rooms = data.getJSONArray("Rooms");
                                        for (int i = 0; i < rooms.length(); i++) {
                                            JSONObject room = rooms.getJSONObject(i);
                                            String name = room.getString("Room_Name");
                                            String id = room.getString("RoomID");
                                            JSONArray users = room.getJSONArray("Users");
                                            int icon = room.getInt("Icon");
                                            Global.database.getRoomDao().insert(new Room(id,users.toString(),name,icon));
                                        }
                                        Intent intent = new Intent(Room.ACTION);
                                        intent.putExtra(Global.JOB, Global.ALL);
                                        Global.manager.sendBroadcast(intent);
                                    } else if (data.has("Job")) {
                                        switch (data.getString("Job")) {
                                            case "Update": {
                                                String roomName = data.getString("Room_Name");
                                                String roomID = data.getString("RoomID");
                                                JSONArray jsonTokens = data.getJSONArray("Device_Token");
                                                String[] tokens = new String[jsonTokens.length()];
                                                for (int i = 0; i < jsonTokens.length(); i++) {
                                                    tokens[i] =(jsonTokens.getString(i));
                                                }
                                                String users = data.getJSONArray("Users").toString();
                                                int icon = data.getInt("Icon");
                                                Global.database.getRoomDao().update(new Room(roomID,users.toString(),roomName,icon));
                                                Global.database.getDeviceDao().setRooms(roomID,tokens);
                                                Intent intent = new Intent(Room.ACTION);
                                                intent.putExtra(Global.JOB, Global.UPDATE);
                                                intent.putExtra(Global.ID,roomID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Delete": {
                                                String roomID = data.getString("RoomID");
                                                Device[] roomDevices = Global.database.getDeviceDao().getRoomDevices(roomID);
                                                Global.database.getRoomDao().deleteWithRoomID(roomID);
                                                Global.database.getDeviceDao().setRoomIDofRoom("-1",roomID);
                                                for (Device d :
                                                        roomDevices) {
                                                    Intent intent = new Intent(Device.ACTION);
                                                    intent.putExtra(Global.JOB, Global.UPDATE);
                                                    intent.putExtra(Global.ID,d.getToken());
                                                    Global.manager.sendBroadcast(intent);
                                                }
                                                Intent intent = new Intent(Room.ACTION);
                                                intent.putExtra(Global.JOB, Global.DELETE);
                                                intent.putExtra(Global.ID,roomID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Insert": {
                                                String roomName = data.getString("Room_Name");
                                                String roomID = data.getString("RoomID");
                                                JSONArray jsonTokens = data.getJSONArray("Device_Token");
                                                String[] tokens = new String[jsonTokens.length()];
                                                for (int i = 0; i < jsonTokens.length(); i++) {
                                                    tokens[i] =(jsonTokens.getString(i));
                                                }                                                String users = data.getJSONArray("Users").toString();
                                                int icon = data.getInt("Icon");
                                                Global.database.getRoomDao().insert(new Room(roomID,users.toString(),roomName,icon));
                                                Global.database.getDeviceDao().setRooms(roomID,tokens);
                                                Intent intent = new Intent(Room.ACTION);
                                                intent.putExtra(Global.JOB, Global.INSERT);
                                                intent.putExtra(Global.ID,roomID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                        }

                                    }
                                } else {
                                    Intent intent = new Intent(Room.ACTION);
                                    intent.putExtra(Global.JOB, Global.ALL);
                                    Global.manager.sendBroadcast(intent);
                                }
                                break;
                            } else {
                                Intent intent = new Intent(Room.ACTION);
                                intent.putExtra(Global.JOB, Global.ALL);
                                Global.manager.sendBroadcast(intent);
                            }
                        }
                    }
                    break;
                }
                case 10:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject object = new JSONObject(message);
                            String dataS = object.getString("Data");
                            if (!dataS.equals("")) {
                                JSONObject data = protocol.getData();
                                if (!data.toString().equals("{}")) {
                                    if (data.has("Scenario")) {
                                        JSONArray scenarios = data.getJSONArray("Scenario");
                                        Global.database.getScenarioDao().deleteAll();
                                        for (int i = 0; i < scenarios.length(); i++) {
                                            try {
                                                JSONObject scenario = scenarios.getJSONObject(i);
                                                String id = scenario.getString("ScenarioID");
                                                String name = scenario.getString("Name");
                                                String time = scenario.getString("Time");
                                                String status = scenario.getString("Status");
                                                JSONArray deviceTokens = (scenario.getJSONArray("Tokens"));
                                                JSONArray deviceStatus = (scenario.getJSONArray("Device_Status"));
                                                JSONArray users = (scenario.getJSONArray("Users"));
                                                Scenario scenario1 = new Scenario(id,name,time,status,deviceTokens.toString(),deviceStatus.toString(),users.toString());
                                                Global.database.getScenarioDao().insert(scenario1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Intent intent = new Intent(Scenario.ACTION);
                                        intent.putExtra(Global.JOB, Global.ALL);
                                        Global.manager.sendBroadcast(intent);
                                    } else if (data.has("Job")) {
                                        switch (data.getString("Job")) {
                                            case "Action":{
                                                String scenarioID = data.getString("ScenarioID");
                                                Intent intent = new Intent(Scenario.ACTION);
                                                intent.putExtra(Global.JOB, Global.ACTION);
                                                intent.putExtra(Global.ID,scenarioID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Update": {
                                                String scenarioID = data.getString("ScenarioID");
                                                String scenarioName = data.getString("Name");
                                                String scenarioTime = data.getString("Time");
                                                String scenarioDeviceTokens = data.getString("Tokens");
                                                String scenarioStatus;
                                                if (data.has("Status")) {
                                                    scenarioStatus = data.getString("Status");
                                                } else {
                                                    scenarioStatus = "0";
                                                }
                                                String scenarioDeviceStatus = data.getString("Device_Status");
                                                String scenarioUsers = data.getJSONArray("Users").toString();
                                                Scenario scenario = new Scenario(scenarioID,scenarioName,scenarioTime,scenarioStatus,scenarioDeviceTokens,scenarioDeviceStatus,scenarioUsers);
                                                Global.database.getScenarioDao().update(scenario);
                                                Intent intent = new Intent(Scenario.ACTION);
                                                intent.putExtra(Global.JOB, Global.UPDATE);
                                                intent.putExtra(Global.ID,scenarioID);
                                                Global.manager.sendBroadcast(intent);
                                                Log.i("Errrasd", "e.toString()");
                                                break;
                                            }
                                            case "Delete": {
                                                String scenarioID = data.getString("ScenarioID");
                                                Global.database.getScenarioDao().deleteByID(scenarioID);
                                                Intent intent = new Intent(Scenario.ACTION);
                                                intent.putExtra(Global.JOB, Global.DELETE);
                                                intent.putExtra(Global.ID,scenarioID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Insert": {
                                                String scenarioID = data.getString("ScenarioID");
                                                String scenarioName = data.getString("Name");
                                                String scenarioTime = data.getString("Time");
                                                String scenarioDeviceTokens = data.getString("Tokens");
                                                String scenarioStatus;
                                                if (data.has("Status")) {
                                                    scenarioStatus = data.getString("Status");
                                                } else {
                                                    scenarioStatus = "0";
                                                }
                                                String scenarioDeviceStatus = data.getString("Device_Status");
                                                String scenarioUsers = data.getJSONArray("Users").toString();
                                                Scenario scenario = new Scenario(scenarioID,scenarioName,scenarioTime,scenarioStatus,scenarioDeviceTokens,scenarioDeviceStatus,scenarioUsers);
                                                Global.database.getScenarioDao().insert(scenario);
                                                Intent intent = new Intent(Scenario.ACTION);
                                                intent.putExtra(Global.JOB, Global.INSERT);
                                                intent.putExtra(Global.ID,scenarioID);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
                case 13:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            if (!data.toString().equals("{}")) {
                                if (data.has("Connection")) {
                                    if (!data.toString().equals("{}")) {
                                        if (data.has("Messages")) {
                                            Global.manager.sendBroadcast(new Intent(Global.MESSAGES_ACTION).putExtra(Global.JOB,Global.ALL).putExtra(Global.MESSAGES, data.toString()));
                                        } else if (data.has("Job")) {
                                            switch (data.getString("Job")) {
                                                case "Insert": {
                                                    String body = data.getString("Body");
                                                    String subject = data.getString("Subject");
                                                    Global.manager.sendBroadcast(new Intent(Global.MESSAGES_ACTION).putExtra(Global.JOB,Global.INSERT).putExtra(Global.BODY, body).putExtra(Global.SUBJECT,subject));
                                                    break;
                                                }
                                                case "Delete": {
                                                    String messageID = data.getString("Message_ID");
                                                    Global.manager.sendBroadcast(new Intent(Global.MESSAGES_ACTION).putExtra(Global.JOB,Global.DELETE).putExtra(Global.ID,messageID));
                                                    break;
                                                }
                                                case "Update": {
                                                    String body = data.getString("Body");
                                                    String subject = data.getString("Subject");
                                                    String messageID = data.getString("Message_ID");
                                                    Global.manager.sendBroadcast(new Intent(Global.MESSAGES_ACTION).putExtra(Global.JOB,Global.UPDATE).putExtra(Global.BODY, body).putExtra(Global.SUBJECT,subject).putExtra(Global.ID,messageID));
                                                    break;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                case 15:{
                    switch (protocol.getMethod()){
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet:{
                            JSONObject data = protocol.getData();
                            if (!data.toString().equals("{}")) {
                                if (data.has("Result")){
                                    Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.JOB,Global.CONNECTION).putExtra(Global.TEXT,data.getString("Result")));
                                }
                                else if (data.has("wifi")){
                                    String wifi = data.getString("wifi");
                                    String pass = data.getString("pass");
                                    String token = data.getString("Token");
                                    List<String> devices = new ArrayList<>();
                                    devices.add(token);
                                    if (token.split("M")[0].equals("B8")){
                                        Device[] relaysOfBus = Global.database.getDeviceDao().getRelaysOfBus(token);
                                        for (Device d :
                                                relaysOfBus) {
                                            devices.add(d.getToken());
                                        }
                                    }
                                    Global.database.getDeviceDao().updateSSIDandPassword(wifi,pass,devices);
                                    Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.JOB,Global.CONNECTION).putExtra(Global.SSID,wifi).putExtra(Global.PASS,pass).putExtra(Global.ID,token));
                                }
                            }
                            break;
                        }
                    }
                }
                case 18:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            if (!data.toString().equals("{}")) {
                                if (data.has("Connection")) {
                                    JSONArray array = data.getJSONArray("Connection");
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        String token = object.getString("Token");
                                        boolean status = object.getString("Status").equals("1");
                                        Global.database.getDeviceDao().updateOnline(token, status);
                                    }
                                    Intent intent = new Intent(Device.ACTION);
                                    intent.putExtra(Global.JOB,Global.CONNECTION_ALL);
                                    Global.manager.sendBroadcast(intent);
                                } else if (data.has("Token")) {
                                    String token = data.getString("Token");
                                    String type = token.split("M")[0];
                                    if (!(type.equals("B8") || type.equals("B16"))) {
                                        boolean status = data.getString("Status").equals("1");
                                        Global.database.getDeviceDao().updateOnline(token, status);
                                        Intent intent = new Intent(Device.ACTION);
                                        intent.putExtra(Global.JOB,Global.UPDATE);
                                        intent.putExtra(Global.ID,token);
                                        Global.manager.sendBroadcast(intent);
                                    } else {
                                        int index = Integer.parseInt(token.split("M")[token.split("M").length - 1]);
                                        if (index == 0) {
                                            for (int i = 1; i <= 8; i++) {
                                                String tokenRb = token.substring(0, token.lastIndexOf("M")) +
                                                        "M" +
                                                        i;
                                                Log.i("BusToken", tokenRb);
                                                boolean status = data.getString("Status").equals("1");
                                                Global.database.getDeviceDao().updateOnline(token, status);
                                                Intent intent = new Intent(Device.ACTION);
                                                intent.putExtra(Global.JOB,Global.UPDATE);
                                                intent.putExtra(Global.ID,token);
                                                Global.manager.sendBroadcast(intent);
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
                case 22:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject object2 = new JSONObject(message);
                            String dataS = object2.getString("Data");
                            if (!dataS.equals("")) {
                                JSONObject data = protocol.getData();
                                if (!data.toString().equals("{}")) {
                                    if (data.has("Remotes")) {
                                        JSONArray remote = data.getJSONArray("Remotes");
                                        List<String> previousIDs = Global.database.getRemoteDao().getAllIds();
                                        List<String> v = obtainIDs(remote);
                                        try {
                                            Global.database.getRemoteDao().clearOthers(v);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        for (int i = 0; i < remote.length(); i++) {
                                            JSONObject object = remote.getJSONObject(i);
                                            String id = object.getString("Remote_ID");
                                            String name = object.getString("Label");
                                            String type = object.getString("Type");
                                            String model = object.getString("Model");
                                            String brand = object.getString("Brand");
                                            JSONArray userIDs = object.getJSONArray("Users");
                                            String IrID = object.getString("Token");
                                            String p = object.getString("Protocol");
                                            String userMake = object.getString("User_Make");
                                            String roomID = null;
                                            if (object.has("Room_ID")) {
                                                roomID = object.getString("Room_ID");
                                            }
                                            if (roomID == null || "".equals(roomID) || "null".equals(roomID))
                                                roomID = "-1";
                                            if (previousIDs.indexOf(id) == -1) {
                                                Remote remote1 = new Remote(id,name,type,"-1",model,brand,userIDs.toString(),p,userMake,IrID,roomID);
                                                Global.database.getRemoteDao().insert(remote1);
                                            } else {
                                                Global.database.getRemoteDao().updateWithoutKey(id, name, type, model, brand, userIDs.toString(), p, userMake, IrID, roomID);
                                            }
                                        }
                                        Intent intent = new Intent(Remote.ACTION);
                                        intent.putExtra(Global.JOB, Global.ALL);
                                        Global.manager.sendBroadcast(intent);
                                    } else if (data.has("Job")) {
                                        String job = data.getString("Job");
                                        switch (job) {
                                            case "Update_Pronto":{
                                                String remoteID = data.getString("Remote_ID");
                                                JSONArray keys = data.getJSONArray("Keys");
                                                JSONArray bk = new JSONArray();
                                                String beforekeys = Global.database.getRemoteDao().getRemote(remoteID).getKeys();
                                                if (!beforekeys.equals("-1")) {
                                                    bk = new JSONArray(beforekeys);
                                                }
                                                String s = keys.toString();
                                                if (keys.length() == 0) s = "-1";
                                                Global.database.getRemoteDao().updateKey(remoteID, s);
                                                Global.manager.sendBroadcast(new Intent(Remote.ACTION).putExtra(Global.JOB, Global.UPDATE_PRONTO).putExtra(Global.ID, remoteID).putExtra(Global.BEFORE_KEY, bk.toString()));
                                                break;

                                            }
                                            case "Update": {
                                                String id = data.getString("Remote_ID");
                                                String name = data.getString("Label");
                                                String type = data.getString("Type");
                                                String model = data.getString("Model");
                                                String brand = data.getString("Brand");
                                                JSONArray userIDs = data.getJSONArray("Users");
                                                String IrID = data.getString("Token");
                                                String p = data.getString("Protocol");
                                                String userMake = data.getString("User_Make");
                                                String roomID = null;
                                                if (data.has("Room_ID")) {
                                                    roomID = data.getString("Room_ID");
                                                }
                                                if (roomID == null || "".equals(roomID) || "null".equals(roomID))
                                                    roomID = "-1";
                                                Global.database.getRemoteDao().updateWithoutKey(id, name, type, model, brand, userIDs.toString(), p, userMake, IrID, roomID);
                                                Intent intent = new Intent(Remote.ACTION);
                                                intent.putExtra(Global.JOB, Global.UPDATE);
                                                intent.putExtra(Global.ID, id);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Insert": {
                                                String id = data.getString("Remote_ID");
                                                String name = data.getString("Label");
                                                String type = data.getString("Type");
                                                String model = data.getString("Model");
                                                String brand = data.getString("Brand");
                                                JSONArray userIDs = data.getJSONArray("Users");
                                                String IrID = data.getString("Token");
                                                String p = data.getString("Protocol");
                                                String userMake = data.getString("User_Make");
                                                String roomID = null;
                                                if (data.has("Room_ID")) {
                                                    roomID = data.getString("Room_ID");
                                                }
                                                if (roomID == null || "".equals(roomID) || "null".equals(roomID))
                                                    roomID = "-1";
                                                Remote remote1 = new Remote(id,name,type,"-1",model,brand,userIDs.toString(),p,userMake,IrID,roomID);
                                                Global.database.getRemoteDao().insert(remote1);  Intent intent = new Intent(Remote.ACTION);
                                                intent.putExtra(Global.JOB, Global.INSERT);
                                                intent.putExtra(Global.ID, id);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Delete": {
                                                String id = data.getString("Remote_ID");
                                                Global.database.getRemoteDao().deleteByID(id);
                                                Intent intent = new Intent(Remote.ACTION);
                                                intent.putExtra(Global.JOB, Global.DELETE);
                                                intent.putExtra(Global.ID, id);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;

                }
                case 23:{
                    JSONObject data = protocol.getData();
                    if (data.has("Result")) {
                        String result = data.getString("Result");
                        if ("OK".equals(result) || "Ok".equals(result) || "oK".equals(result)) {
                            if (data.has("Pronto")) {
                                String pronto = data.getString("Pronto");
                                Intent intent = new Intent(Remote.ACTION).putExtra(Global.JOB,Global.CODE_LEARN).putExtra(Global.PRONTO,pronto);
                                Global.manager.sendBroadcast(intent);
                            }
                        }
                    }
                    break;
                }
                case 24:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N:
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            if (data.has("Result")) {
                                String token = "NOT_DEFINED";
                                if (data.has("Token")) {
                                    token = data.getString("Token");
                                }
                                String result = data.getString("Result");
                                Global.manager.sendBroadcast(new Intent(Device.ACTION).putExtra(Global.JOB,Global.RGB).putExtra(Global.RESULT,result).putExtra(Global.ID,token));
                            }
                            break;
                        }
                    }

                }
                case 27:{
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            try {
                                if (!data.toString().equals("{}")) {
                                    if (data.has("IfThens")) {
                                        Global.database.getIfThenDao().deleteAll();
                                        JSONArray thens = data.getJSONArray("IfThens");
                                        for (int i = 0; i < thens.length(); i++) {
                                            try {
                                                JSONObject object = thens.getJSONObject(i);
                                                JSONObject anIf = object.getJSONObject("If");
                                                JSONObject then = object.getJSONObject("Then");
                                                String name = object.getString("Name");
                                                JSONArray users = object.getJSONArray("Users");
                                                String id = object.getString("ID");
                                                String status = object.getString("Status");
                                                IfThen ifThen = new IfThen(id,name,status,users.toString(),anIf.toString(),then.toString());
                                                Global.database.getIfThenDao().insert(ifThen);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Intent intent = new Intent(IfThen.ACTION);
                                        intent.putExtra(Global.JOB, Global.ALL);
                                        Global.manager.sendBroadcast(intent);
                                    } else if (data.has("Job")) {
                                        String job = data.getString("Job");
                                        switch (job) {
                                            case "Update": {
                                                JSONObject anIf = data.getJSONObject("If");
                                                JSONObject then = data.getJSONObject("Then");
                                                String name = data.getString("Name");
                                                JSONArray users = data.getJSONArray("Users");
                                                String id = data.getString("ID");
                                                String status = data.getString("Status");
                                                IfThen ifThen = new IfThen(id,name,status,users.toString(),anIf.toString(),then.toString());
                                                Global.database.getIfThenDao().update(ifThen);
                                                Intent intent = new Intent(IfThen.ACTION);
                                                intent.putExtra(Global.JOB, Global.UPDATE).putExtra(Global.ID,id);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Insert": {
                                                JSONObject anIf = data.getJSONObject("If");
                                                JSONObject then = data.getJSONObject("Then");
                                                String name = data.getString("Name");
                                                JSONArray users = data.getJSONArray("Users");
                                                String id = data.getString("ID");
                                                String status = data.getString("Status");
                                                IfThen ifThen = new IfThen(id,name,status,users.toString(),anIf.toString(),then.toString());
                                                Global.database.getIfThenDao().insert(ifThen);
                                                Intent intent = new Intent(IfThen.ACTION);
                                                intent.putExtra(Global.JOB, Global.INSERT).putExtra(Global.ID,id);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                            case "Delete": {
                                                String id = data.getString("ID");
                                                Global.database.getIfThenDao().deleteByID(id);
                                                Intent intent = new Intent(IfThen.ACTION);
                                                intent.putExtra(Global.JOB, Global.DELETE).putExtra(Global.ID,id);
                                                Global.manager.sendBroadcast(intent);
                                                break;
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }

                    break;
                }
                case 30: {
                    switch (protocol.getMethod()) {
                        case Protocol.Methods.responseGet_N:
                        case Protocol.Methods.responseSet_N: {
                            break;
                        }
                        case Protocol.Methods.responseSet:
                        case Protocol.Methods.requestSet:
                        case Protocol.Methods.responseGet: {
                            JSONObject data = protocol.getData();
                            try {
                                if (!data.toString().equals("{}")) {
                                    if (data.has("Devices")) {
                                        Global.database.getBusDao().deleteAll();
                                        JSONArray buses = data.getJSONArray("Devices");
                                        for (int i = 0; i < buses.length(); i++) {
                                            try {
                                                JSONObject object = buses.getJSONObject(i);
                                                String token = object.getString("Token");
                                                String label = object.getString("Label");
                                                Bus bus = new Bus(token,token.split("M")[0],false,label);
                                                Global.database.getBusDao().insert(bus);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        Global.database.getBusDao().deleteType("SB8");
                                        Intent intent = new Intent(Bus.ACTION);
                                        intent.putExtra(Global.JOB, Global.ALL);
                                        Global.manager.sendBroadcast(intent);
                                    } else if (data.has("Job")) {
                                        String job = data.getString("Job");
                                        switch (job) {
                                            case "Insert": {
                                                String token = data.getString("Token");
                                                String label = data.getString("Label");
                                                String ssid = data.getString("SSID");
                                                String pass = data.getString("Pass");
                                                List<String> relayIDs = new ArrayList<>();
                                                String tokenRoot = Bus.getTokenRoot(token);
                                                switch (token.split("M")[0]){
                                                    case "B8":{
                                                        for (int i = 1; i < 8; i++) {
                                                            relayIDs.add(tokenRoot + i);
                                                        }
                                                        break;
                                                    }
                                                    case "B16":{
                                                        for (int i = 1; i < 16; i++) {
                                                            relayIDs.add(tokenRoot + i);
                                                        }
                                                        break;
                                                    }
                                                }
                                                Bus bus = new Bus(token,token.split("M")[0],false,label);
                                                Global.database.getBusDao().insert(bus);
                                                Global.database.getDeviceDao().updateSSIDandPassword(ssid , pass, relayIDs);
                                                break;
                                            }
                                            case "Update": {
                                                String token = data.getString("Token");
                                                String label = data.getString("Label");
                                                String ssid = data.getString("SSID");
                                                String pass = data.getString("Pass");
                                                List<String> relayIDs = new ArrayList<>();
                                                String tokenRoot = Bus.getTokenRoot(token);
                                                switch (token.split("M")[0]){
                                                    case "B8":{
                                                        for (int i = 1; i < 8; i++) {
                                                            relayIDs.add(tokenRoot + i);
                                                        }
                                                        break;
                                                    }
                                                    case "B16":{
                                                        for (int i = 1; i < 16; i++) {
                                                            relayIDs.add(tokenRoot + i);
                                                        }
                                                        break;
                                                    }
                                                }
                                                Bus bus = new Bus(token,token.split("M")[0],false,label);
                                                Global.database.getBusDao().update(bus);
                                                Global.database.getDeviceDao().updateSSIDandPassword(ssid , pass, relayIDs);
                                                break;
                                            }
                                            case "Delete": {
                                                String token = data.getString("Token");
                                                List<String> relayIDs = new ArrayList<>();
                                                String tokenRoot = Bus.getTokenRoot(token);
                                                switch (token.split("M")[0]){
                                                    case "B8":{
                                                        for (int i = 1; i < 8; i++) {
                                                            relayIDs.add(tokenRoot + i);
                                                        }
                                                        break;
                                                    }
                                                    case "B16":{
                                                        for (int i = 1; i < 16; i++) {
                                                            relayIDs.add(tokenRoot + i);
                                                        }
                                                        break;
                                                    }
                                                }
                                                Global.database.getBusDao().deleteByID(token);
                                                Global.database.getDeviceDao().deleteThem(relayIDs);
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<String> obtainIDs(JSONArray remote) throws JSONException {
        List<String> id = new ArrayList<>();
        for (int i = 0; i < remote.length(); i++) {
            JSONObject jsonObject = remote.getJSONObject(i);
            id.add(jsonObject.getString("Remote_ID"));
        }
        return id;
    }


    @Override
    public void run() {
        doInBackground(message);
    }
}
