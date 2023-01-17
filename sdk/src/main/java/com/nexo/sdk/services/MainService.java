package com.nexo.sdk.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import com.nexo.sdk.Global;
import com.nexo.sdk.protocol.MessageParser;
import com.nexo.sdk.threads.Connection;
import com.nexo.sdk.threads.Listener;
import com.nexo.sdk.threads.Sender;
import com.nexo.sdk.threads.StopConnection;

import static com.nexo.sdk.threads.Connection.CONNECTION_TAG;
import static com.nexo.sdk.threads.Sender.TAG;

import org.jetbrains.annotations.Nullable;

public class MainService extends Service implements Connection.ConnectionCallBack, Listener.MessageCallback, StopConnection.StopConnectionCallBack {
    private Listener listener;
    private Sender sender;
    private static MainService service;
    private boolean stopped = false;
    private boolean reConnect = true;

    public void setReConnect(boolean reConnect) {
        this.reConnect = reConnect;
    }

    public static MainService instance() {
        return service;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        service = this;
        Global.manager.sendBroadcast(new Intent(Global.SERVICE_ACTION));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sender.stop();
        listener.stop();
    }
    public  void startConnection(){
        Log.d(CONNECTION_TAG,"Start 0 " + Global.connectorThreadIndicator);

        stopped = false;
        Connection connection = new Connection(this);
        Thread thread = new Thread(connection);
        thread.start();
    }

    @Override
    public void connectionCallBack(final boolean connection) {
        Log.d(CONNECTION_TAG, "connectionCallBack: " +connection);
        Runnable runnable = () -> {
            if (connection){

                Global.manager.sendBroadcast(new Intent(Global.CONNECTION_ACTION).putExtra(Global.CONNECTION,true));
                Log.d(TAG,"Listener inc : " + "start Listener : " + Global.listenerThreadIndicator);
                listener = new Listener(MainService.this,MainService.this);
                Log.d(CONNECTION_TAG, "connectionCallBack: " +"connect");
                Thread thread = new Thread(listener);
                thread.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sender = new Sender();
                Thread thread2 = new Thread(sender);
                thread2.start();
            }else{
                Global.manager.sendBroadcast(new Intent(Global.CONNECTION_ACTION).putExtra(Global.CONNECTION,false));
                if (!stopped) {
                    if (reConnect) {
                        Log.d(CONNECTION_TAG, "connectionCallBack: " +" reconnect");
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Thread(new Connection(this)).start();
                    }
                }else{
                    Log.d(CONNECTION_TAG, "connectionCallBack: " +" Stopped");
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void messageCallback(String message) {
        MessageParser parser = new MessageParser(this, message);
        new Thread(parser).start();
    }

    @Override
    public void disconnected(int i) {
        Runnable runnable = new Runnable() {
            public void run() {
                Global.manager.sendBroadcast(new Intent(Global.CONNECTION_ACTION).putExtra(Global.CONNECTION,false));
                if (!stopped) {//! stoppeed
                    if (reConnect) {
                        if (sender!=null) {
                            Log.d(TAG,"Going to stop sender");
                            sender.stop();
                        }
                        if (listener!=null) {
                            Log.d(TAG,"Going to stop listener");
                            listener.stop();
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(CONNECTION_TAG, i + " : Start 1 " + Global.connectorThreadIndicator);
                        new Thread(new Connection(MainService.this)).start();
                    }
                }
            }
        };
        new Thread(runnable).start();

    }
    public void stopConnection(){
        stopped = true;
        reConnect = false;
        listener.stop();
        sender.stop();
        StopConnection stopConnection = new StopConnection(this);
        new Thread(stopConnection).start();

    }

    @Override
    public void onStopped() {
    }
}
