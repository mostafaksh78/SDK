package com.nexo.sdk.protocol;

import org.json.JSONException;
import org.json.JSONObject;

public class Protocol  {
    private int command = 0;
    private int method = 0;
    private JSONObject data = new JSONObject();
    public Protocol(JSONObject jsonObject) throws JSONException {
        this.command = jsonObject.getInt("Com");
        this.method = jsonObject.getInt("Meth");
        try {
            this.data = jsonObject.getJSONObject("Data");
        } catch (JSONException e) {
            this.data = new JSONObject();
//            e.printStackTrace();
        }
    }
    public Protocol(int command,int method,JSONObject data) {
        this.command = command;
        this.data = data;
        this.method = method;
    }
    public String getProtocol() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("Data" , data);
        object.put("Meth",method);
        object.put("Com",command);
        return object.toString();
    }

    public int getCommand() {
        return command;
    }

    public int getMethod() {
        return method;
    }

    public JSONObject getData() {
        return data;
    }

    public static class Methods{
        public static final int requestSet = 1;
        public static final int requestGet = 2;
        public static final int responseSet = 3;
        public static final int responseGet = 4;
        public static final int responseSet_N = 5;
        public static final int responseGet_N = 6;
    }
}
