package com.nexo.sdk.threads;

import android.util.Log;

import com.nexo.sdk.Global;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable {
    public static final String CONNECTION_TAG  = "ConnectionInfo";
    private final ConnectionCallBack callBack;

    public Connection(ConnectionCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        try {
            if (Global.socket == null) {
                Global.socket = new Socket(Global.SERVER_IP, Global.SERVER_PORT);
                Global.socket.setKeepAlive(true);
                Global.socket.setSendBufferSize(Integer.MAX_VALUE);
                Global.output = new PrintWriter(Global.socket.getOutputStream());
                Global.input = new DataInputStream((Global.socket.getInputStream()));
                Log.i(CONNECTION_TAG, "Connected");
                callBack.connectionCallBack(true);
                return;
            } else if (!Global.socket.isConnected()) {
                Global.socket = new Socket(Global.SERVER_IP, Global.SERVER_PORT);
                Global.socket.setKeepAlive(true);
                Global.socket.setSendBufferSize(Integer.MAX_VALUE);
                Global.output = new PrintWriter(Global.socket.getOutputStream());
                Global.input = new DataInputStream((Global.socket.getInputStream()));
                Log.i(CONNECTION_TAG, "Connected 1");
                callBack.connectionCallBack(true);
                return;
            } else {
                if (Global.socket.isClosed()) {
                    Global.socket = new Socket(Global.SERVER_IP, Global.SERVER_PORT);
                    Global.socket.setKeepAlive(true);
                    Global.socket.setSendBufferSize(Integer.MAX_VALUE);
                    Global.output = new PrintWriter(Global.socket.getOutputStream());
                    Global.input = new DataInputStream((Global.socket.getInputStream()));
                    Log.i(CONNECTION_TAG, "Connected 2");
                    callBack.connectionCallBack(true);
                    return;
                } else if (Global.socket.isOutputShutdown()) {
                    Global.socket = new Socket(Global.SERVER_IP, Global.SERVER_PORT);
                    Global.socket.setKeepAlive(true);
                    Global.socket.setSendBufferSize(Integer.MAX_VALUE);
                    Global.output = new PrintWriter(Global.socket.getOutputStream());
                    Global.input = new DataInputStream((Global.socket.getInputStream()));
                    Log.i(CONNECTION_TAG, "Connected 3");
                    callBack.connectionCallBack(true);
                    return;
                } else if (Global.socket.isInputShutdown()) {
                    Global.socket = new Socket(Global.SERVER_IP, Global.SERVER_PORT);
                    Global.socket.setKeepAlive(true);
                    Global.socket.setSendBufferSize(Integer.MAX_VALUE);
                    Global.output = new PrintWriter(Global.socket.getOutputStream());
                    Global.input = new DataInputStream((Global.socket.getInputStream()));
                    Log.i(CONNECTION_TAG, "Connected 4");
                    callBack.connectionCallBack(true);
                    return;
                }else {
                    Log.i(CONNECTION_TAG, "Connected 5");
                    callBack.connectionCallBack(true);
                    return;
                }
            }
        } catch (Exception e) {
            Global.socket = null;
            Global.input = null;
            Global.output = null;
            Log.i(CONNECTION_TAG,"not Connected : " + e);
            callBack.connectionCallBack(false);
        }

    }

    public interface ConnectionCallBack{
        void connectionCallBack(boolean connection);
    }
}
