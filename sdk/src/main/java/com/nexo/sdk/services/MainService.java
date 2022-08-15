package com.nexo.sdk.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.nexo.sdk.Global;
import com.nexo.sdk.protocol.MessageParser;
import com.nexo.sdk.threads.Connection;
import com.nexo.sdk.threads.Listener;
import com.nexo.sdk.threads.Sender;

public class MainService extends Service implements Connection.ConnectionCallBack, Listener.MessageCallback {
    private Listener listener;
    private Sender sender;
    private static MainService service;

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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sender.stop();
        listener.stop();
    }
    private void startConnection(){
        Connection connection = new Connection(this);
        Thread thread = new Thread(connection);
        thread.start();
    }

    @Override
    public void connectionCallBack(final boolean connection) {
        Runnable runnable = () -> {
            if (connection){
                Global.manager.sendBroadcast(new Intent(Global.CONNECTION_ACTION).putExtra(Global.CONNECTION,true));
                listener = new Listener(MainService.this,MainService.this);
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
                // check for reconnecting
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //                new Thread(new Reconnection(this)).start();
                new Thread(new Connection(this)).start();

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
    public void disconnected() {
        Global.manager.sendBroadcast(new Intent(Global.CONNECTION_ACTION).putExtra(Global.CONNECTION,false));
        // check for reconnecting
        //                new Thread(new Reconnection(this)).start();
        new Thread(new Connection(this)).start();

    }

}
