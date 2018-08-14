package xhj.zime.com.mymaptest.Util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import xhj.zime.com.mymaptest.bean.BaseDataBean;
import xhj.zime.com.mymaptest.bean.TaskBean;
import xhj.zime.com.mymaptest.bean.UserBean;

public class Utility {

    public static BaseDataBean handleBaseDataResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String userString  = jsonObject.toString();
                Gson gson = new Gson();
                return gson.fromJson(userString,BaseDataBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static TaskBean handleTaskResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String userString  = jsonObject.toString();
                Gson gson = new Gson();
                return gson.fromJson(userString,TaskBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static UserBean handleUserResponse(String response) {
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String userString  = jsonObject.toString();
                Gson gson = new Gson();
                return gson.fromJson(userString,UserBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
