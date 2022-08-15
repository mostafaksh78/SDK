package com.nexo.sdk.threads;

import android.content.Context;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.nexo.sdk.Crypto;
import com.nexo.sdk.Global;

public class Listener implements Runnable {
    private static final String TAG = "ListenerTAG";
    private final MessageCallback messageCallback;
    private final Context context;

    public Listener(MessageCallback messageCallback, Context context) {
        this.messageCallback = messageCallback;
        this.context = context;
    }


    private boolean cancelFlag;

    @Override
    public void run() {
        String message = "";
        byte[] mbytes = new byte[1024];
        cancelFlag = true;
        while (cancelFlag) {
            try {
                if (Global.checkNetConnection(context)) {
                    if (Global.socket.isConnected()) {
                        if (Global.input != null) {
                            try {
                                int read = Global.input.read(mbytes);
                                Log.i("Read", "Read : " + read);
                                if (read != -1) {
                                    message = message.concat(new String(mbytes, 0, read));
                                    if (read != mbytes.length) {
                                        String[] split = message.split("\n");
                                        if (message.charAt(message.length() - 1) != '\n') {
                                            //Not End Of message
                                            message = split[split.length - 1];
                                            for (int i = 0; i < split.length - 1; i++) {
                                                parseMessage(split[i]);
                                            }
                                        } else {
                                            //End Of message
                                            for (String s : split) {
                                                parseMessage(s);
                                            }
                                            message = "";
                                        }
                                    }
                                } else {
                                    messageCallback.disconnected();
                                    cancel();
                                    break;
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "read EXP");
                                Global.socket = null;
                                Global.input = null;
                                Global.output = null;
                                messageCallback.disconnected();
                                cancel();

                            }
                        } else {
                            Log.i(TAG, "input null");
                            messageCallback.disconnected();
                            cancel();
                        }
                    } else {
                        Log.i(TAG, "socket disconnected");
                        messageCallback.disconnected();
                        cancel();
                    }
                } else {
                    Log.i(TAG, "network disconnected");
                    messageCallback.disconnected();
                    cancel();
                }
            } catch (Exception e) {
                Log.e(TAG, "Net EXP");
                e.printStackTrace();
            }
        }
    }

    private void parseMessage(String message) {
        if (message != null) {
            Log.i("Encryption", "UnDecrypted : " + message);
            try {
                message = Crypto.decrypt(message);
            } catch (Exception e) {
                Log.i("Encryption", "Error in decryption : " + e.toString());
            }
            Log.i("Encryption", "Decrypted : " + message);
            String[] split = message.split("\n");
            for (int i = 0; i < split.length; i++) {
                Log.i("CommunicationByServer", "SendBack : " + split[i]);
                Log.i("SplitLogic", "Split size : " + split.length);
                Message message1 = new Message();
                message1.obj = split[i];
                messageCallback.messageCallback(split[i]);
            }
        } else {
            Log.i("MessageDebug", "Message is null");
        }
    }

    public void stop() {
        cancel();
        try {
            Global.input.close();
            Global.socket.close();
            Global.socket = null;
            Global.output = null;
            Global.input = null;
            Log.i("LogoutDebug", "Listener Stop");
        } catch (Exception e) {
            Log.i("ErrorInExit", " Stage 3 : " + e);
            e.printStackTrace();
        }
    }

    public void cancel() {
        cancelFlag = false;
    }

    public interface MessageCallback {
        void messageCallback(String message);

        void disconnected();
    }
}
