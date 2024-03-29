package com.nexo.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.Room;

import com.nexo.sdk.database.DatabaseClass;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Global {
    public static final String SERVER_IP = "server.nexobuilding.com";
    public static final int SERVER_PORT = 6500;
    public static final String PASS = "PASS";
    public static final String SSID = "SSID";
    public static final String PHONE_EXSIT = "PHONE_EXSIT";
    public static final String ERROR = "ERROR";
    public static final String USER_NAME_EXIST = "USER_NAME_EXIST";
    public static final String PHONE = "PHONE";
    public static final String NEW_USER = "NEW_USER";
    public static final String OTA = "OTA";
    public static final String FAIL = "FAIL";
    public static final String SUCCESS = "SUCCESS";
    public static final String MESSAGES_ACTION = "MESSAGE_ACTION";
    public static final String MESSAGES = "MESSAGES";
    public static final String BODY = "BODY";
    public static final String SUBJECT = "SUBJECT";
    public static volatile Socket socket;
    public static volatile List<String> queue = new ArrayList<>();
    public static volatile PrintWriter output;
    public static volatile DataInputStream input;
    public static volatile DatabaseClass database;
    public static volatile LocalBroadcastManager manager;
    public static final String JOB = "Job";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String INSERT = "INSERT";
    public static final String ALL = "ALL";
    public static final String RESULT = "RESULT";
    public static final String ID = "ID";
    public static final String TEXT = "TEXT";
    public static final String STATUS = "STATUS";
    public static final String AUTH_ACTION = "AUTH_ACTION";
    public static final String CONNECTION_ALL = "CONNECTION_ALL";
    public static final String UPDATE_PRONTO = "UPDATE_PRONTO";
    public static final String BEFORE_KEY = "BEFORE_KEY";
    public static final String CODE_LEARN = "CODE_LEARN";
    public static final String PRONTO = "PRONTO";
    public static final String RGB = "RGB";
    public static final String CONNECTION_ACTION = "CONNECTION_ACTION";
    public static final String CONNECTION = "CONNECTION";
    public static final String SERVICE_ACTION = "SERVICE_ACTION";
    public static final String ACTION = "ACTION";
    public static int version;
    public static volatile int senderThreadIndicator = 0;
    public static volatile int listenerThreadIndicator = 0;
    public static volatile int connectorThreadIndicator = 0;

    public static boolean checkNetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public static void configDatabase(Context context){
        Global.database = Room.databaseBuilder(context, DatabaseClass.class, "Nexo")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
    }
    public static void configBroadcastManager(Context context){
        manager = LocalBroadcastManager.getInstance(context);
    }
}
