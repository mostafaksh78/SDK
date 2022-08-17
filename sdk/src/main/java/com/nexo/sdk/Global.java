package com.nexo.sdk;

import android.content.Context;
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
    public static Socket socket;
    public static final List<String> queue = new ArrayList<>();
    public static PrintWriter output;
    public static DataInputStream input;
    public static volatile DatabaseClass database;
    public static LocalBroadcastManager manager;
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
    public static boolean checkNetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public static void configDatabase(Context context){
        Global.database = Room.databaseBuilder(context, DatabaseClass.class, "Nexo")
                .allowMainThreadQueries()   //Allows room to do operation on main thread
                .build();
    }
    public static void configBroadcastManager(Context context){
        manager = LocalBroadcastManager.getInstance(context);
    }
}
