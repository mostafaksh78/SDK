package com.nexo.sdk.threads;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.nexo.sdk.Global;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Reconnection implements Runnable {
    private final Handler handler;
    private boolean cancelB = false;
    private final Connection.ConnectionCallBack callBack;
    public Reconnection(Connection.ConnectionCallBack callBack) {
        this.callBack = callBack;
        HandlerThread thread = new HandlerThread("Check");
        thread.start();
        Looper looper = thread.getLooper();
        this.handler = new Handler(looper);
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
                Log.i(Connection.CONNECTION_TAG,"Connected");
                callBack.connectionCallBack(true);
                return;
            }
        } catch (Exception e) {
            Global.socket = null;
            Global.input = null;
            Global.output = null;
            Log.i(Connection.CONNECTION_TAG,"Connected : " + e);
        }
        if (!cancelB) {
            handler.postDelayed(this,1000);
        }
    }

    public void cancel() {
        cancelB = true;
    }
}
