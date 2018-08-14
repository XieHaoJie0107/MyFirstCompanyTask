package xhj.zime.com.mymaptest.Util;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import xhj.zime.com.mymaptest.bean.BaseDataBack;
import xhj.zime.com.mymaptest.bean.BaseDataBean;
import xhj.zime.com.mymaptest.bean.DataBean;
import xhj.zime.com.mymaptest.bean.TaskBeansBean;
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

    public static TaskBeansBean handleTaskBeansResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String userString  = jsonObject.toString();
                Gson gson = new Gson();
                return gson.fromJson(userString,TaskBeansBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static DataBean handleDataResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String userString  = jsonObject.toString();
                Gson gson = new Gson();
                return gson.fromJson(userString,DataBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static BaseDataBack handleBaseDataBackResponse(String response) {
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String userString  = jsonObject.toString();
                Gson gson = new Gson();
                return gson.fromJson(userString,BaseDataBack.class);
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
