package com.nexo.sdk.threads;

import android.util.Log;

import com.nexo.sdk.Crypto;
import com.nexo.sdk.Global;

import org.apache.commons.codec.binary.Base64;

public class Sender implements Runnable {
    public static String TAG = "SenderTAG";
    boolean run = true;
    @Override
    public void run() {
        try {
            while (run) {
                if (Global.queue.size()>0) {
                    try {
                        Log.d(TAG,"Send");
                        send(Global.queue.get(0));
                        Global.queue.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d(TAG,"run : false END");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void send(String letter){
        Log.i("CommunicationByServer" ,"Send : " + letter);
        String s = null;
        try {
            s = new String(Base64.encodeBase64(Crypto.encrypt((letter).getBytes())))+ "##";
            Log.i("Encryption",s);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Encryption","Error");
        }
        Global.output.write(s);
        Global.output.flush();
    }

    public void stop() {
        setRun(false);
        try {
            Global.output.flush();
            Global.output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setRun(boolean run) {
        Log.d(TAG,"set run false ");
        this.run = run;
    }

}
