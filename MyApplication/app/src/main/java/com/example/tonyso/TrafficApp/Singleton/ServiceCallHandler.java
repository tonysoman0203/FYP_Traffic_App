package com.example.tonyso.TrafficApp.Singleton;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by SMK338 on 07/08/2015.
 */
public class ServiceCallHandler {
    private final String ErrorMessage_Tag = "ErrorMessage";
    private final String ReturnCode = "ReturnCode";
    private final String isSuccess_TAG = "isSuccess";
    private final String CompatibleActionLink = "CompatibleActionLink";
    Activity context;
    String link="",tmp="";
    private static final String JSON = "http://maps.google.com/maps/api/place/search/json?location=40.717859";
    public ServiceCallHandler(Activity context) {
        this.context = context;
    }

    public int needReturnCode(JSONObject json) {
        if (json == null) {
            return -1;
        }
        /*try {
            tmp = getCompatibleActionLink(json);
            if (tmp.length()>0 || !tmp.isEmpty()) {
                setCompatibleActionLink(tmp);
            }else{
                tmp = "";
            }
            return json.getInt(ReturnCode);
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        setCompatibleActionLink(tmp);
        */
        return -1;
    }

    public boolean checkResultOutput(JSONObject object) {
        try {
            JSONObject jsonObject = object.getJSONObject("Result");
            System.out.println(object.toString());
            JSONObject obj = jsonObject.getJSONObject("Output");
            System.out.println(obj.toString());
            final String isSuccessCode = obj.getString(isSuccess_TAG);
            return isSuccessCode.equals("Y");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkNeedUpdate(JSONObject object) {
        try {
            JSONObject jsonObject = object.getJSONObject("Result");
            //
            JSONObject obj = jsonObject.getJSONObject("Output");
            //
            String needUpdate = obj.getString("needUpdate");
            return needUpdate != null && needUpdate.equals("Y");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public JSONObject getResultOutput(JSONObject object) {
        try {
            JSONObject jsonObject = object.getJSONObject("Result");
            return jsonObject.getJSONObject("Output");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public int getFailCount(JSONObject object) {
        try {
            return object.getInt("failCount");
        } catch (JSONException json) {
            json.printStackTrace();
        }
        return -1;
    }

    public String needJsonErrorMsg(JSONObject jsonObject) {
        try {
            return jsonObject.getString(ErrorMessage_Tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
