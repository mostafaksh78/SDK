package com.nexo.sdk.threads;

import com.nexo.sdk.Global;

import java.io.IOException;

public class StopConnection implements Runnable {
    private StopConnectionCallBack connectionCallBack;

    public StopConnection(StopConnectionCallBack connectionCallBack) {
        this.connectionCallBack = connectionCallBack;
    }

    @Override
    public void run() {
        if (Global.socket != null){
            if (Global.output != null){
                Global.output.flush();
                Global.output.close();
            }
            if (Global.input != null){
                try {
                    Global.input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Global.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Global.socket = null;
            Global.output = null;
            Global.input = null;
        }else {
            Global.socket = null;
            Global.output = null;
            Global.input = null;
        }
    }
    public interface StopConnectionCallBack{
        void onStopped();
    }
}
