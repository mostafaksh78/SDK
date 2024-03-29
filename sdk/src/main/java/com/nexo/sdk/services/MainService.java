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
    public static final String NEW_RECONNECT_SYSTEM_DEBUG = "NEW_RE_TAG";
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
        Log.d("Debugggggger","Service BroadCast");
        Global.manager.sendBroadcast(new Intent(Global.SERVICE_ACTION));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sender!=null) {
            sender.stop();
        }
        if (listener!=null) {
            listener.stop();
        }
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
                Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Connection callback true now want to start listener if not exist " + Global.listenerThreadIndicator);
                if (Global.listenerThreadIndicator == 0) {
                    Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Connection callback true now want to starting listener " + Global.listenerThreadIndicator);
                    listener = new Listener(MainService.this,MainService.this);
                    Log.d(CONNECTION_TAG, "connectionCallBack: " +"connect");
                    Thread thread = new Thread(listener);
                    thread.start();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Connection callback true now want to start sender if not exist " + Global.senderThreadIndicator);
                if (Global.senderThreadIndicator == 0) {
                    Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Connection callback true now want to starting sender " + Global.senderThreadIndicator);
                    sender = new Sender();
                    Thread thread2 = new Thread(sender);
                    thread2.start();
                }
            }else{
                Global.manager.sendBroadcast(new Intent(Global.CONNECTION_ACTION).putExtra(Global.CONNECTION,false));
                Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Connection callback false" );
                if (!stopped) {
                    Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"not stopped" );
                    if (reConnect) {
                        Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"starting reconnect" );
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
                Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Disconnected : " + i );
                Global.manager.sendBroadcast(new Intent(Global.CONNECTION_ACTION).putExtra(Global.CONNECTION,false));
                if (!stopped) {//! stoppeed
                    Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Disconnected : not stopeed " );
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
                    if (reConnect) {
                        Log.d(NEW_RECONNECT_SYSTEM_DEBUG,"Disconnected : reconnect " );
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
