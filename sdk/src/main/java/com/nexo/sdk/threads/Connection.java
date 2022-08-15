package com.nexo.sdk.threads;

import android.util.Log;

import com.nexo.sdk.Global;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection implements Runnable {
    public static final String CONNECTION_TAG  = "ConnectionInfo";
    private ConnectionCallBack callBack;

    public Connection(ConnectionCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        try {
            if (Global.socket == null || !Global.socket.isConnected() || Global.socket.isClosed() || Global.socket.isOutputShutdown() || Global.socket.isInputShutdown()) {
                Global.socket = new Socket(  Global.SERVER_IP,   Global.SERVER_PORT);
                Global.socket.setKeepAlive(true);
                Global.socket.setSendBufferSize(Integer.MAX_VALUE);
                Global.output = new PrintWriter(  Global.socket.getOutputStream());
                Global.input = new DataInputStream((Global.socket.getInputStream()));
                Log.i(CONNECTION_TAG,"Connected");
                callBack.connectionCallBack(true);
                return;
            }
        } catch (Exception e) {
            Global.socket = null;
            Global.input = null;
            Global.output = null;
            Log.i(CONNECTION_TAG,"Connected : " + e);
        }
        callBack.connectionCallBack(false);
    }

    public interface ConnectionCallBack{
        void connectionCallBack(boolean connection);
    }
}
