package com.jpardogo.android.listbuddies.Utils.sharepreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jpardogo.android.listbuddies.Utils.Text.JsonUtils;
import com.jpardogo.android.listbuddies.Utils.Text.StringUtils;
import com.jpardogo.android.listbuddies.models.ImageBean;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于全局变量检测是否第一次启动和登录信息
 */
public class MySharePreference {

    private static MySharePreference mySharePreference;
    private Context context;

    public MySharePreference(Context context) {
        this.context = context;
    }

    public static MySharePreference newInstance(Context context) {

        if (mySharePreference == null) {
            mySharePreference = new MySharePreference(context);
        }

        return mySharePreference;
    }

    /**
     *第一次启动
     *
     * @return
     */
    public boolean isFirstLaunch() {

//		SharedPreferences preferences = PreferenceManager
//				.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getBoolean("isfirstlaunch", true);
    }

    public void setFirstLaunch() {

//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("isfirstlaunch", false);
        editor.commit();
    }

    /**
     * gesture switch
     */
    public boolean isGestureOpen() {

//		SharedPreferences preferences = PreferenceManager
//				.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getBoolean("GestureSwitch", true);
    }

    public void setGestureSwitch(boolean status) {

//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("GestureSwitch", status);
        //Log.e("GestureSwitch","status:"+status);
        editor.commit();
    }

    public void setGestureTime(long time) {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("gestureTime", time);
        editor.commit();
    }

    public long getGestureTime() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getLong("gestureTime", System.currentTimeMillis());
    }

    public void setGesture(String gesture) {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putString("gesture", StringUtils.getMD5(gesture));
        editor.commit();
    }

    public String getGesture() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getString("gesture", "");
    }


    public void setPassword(String pwd) {
        //		SharedPreferences preferences = PreferenceManager
//				.getDefaultSharedPreferences(context);

        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putString("pwd", StringUtils.getMD5(pwd));
        editor.commit();
    }

    public String getPassword() {
//		SharedPreferences preferences = PreferenceManager
//				.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getString("pwd", "");
    }

    public boolean isLogin() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);

        return !TextUtils.isEmpty(getUserId());
    }


    public void setLogin(boolean islogin, String username, String userid,
                         String mobile, String token) {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("isLogin", islogin);
        editor.putString("userid", userid);
        editor.putString("username", username);
        editor.putString("mobile", mobile);
        editor.putString("token", token);
        editor.commit();
    }

    public void setUserName(String username) {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String getUserName() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getString("username", "");
    }


    public String getMobile() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getString("mobile", "");
    }


    public String getUserId() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getString("userid", "");
    }

    public void setBankName(String bankJson) {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putString("bankJson", bankJson);
        editor.commit();
    }

    public Map<String, Object> getBankName() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        String bankJson = preferences.getString("bankJson", "");
        if (!TextUtils.isEmpty(bankJson)) {
            return JsonUtils.getMapForJson(bankJson);
        } else {
            return null;
        }
    }

    public void setImages(String tag,List<ImageBean> images) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
//SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putString(tag, json2String(tag,images));
        editor.apply();
    }

    private String json2String(String tag,List<ImageBean> images){
        int len = images.size();
        JSONObject jsonObject = new JSONObject();
        JSONArray imagesJson = new JSONArray();
        try {
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("url", images.get(i).getUrl());
                json.put("comment", images.get(i).getComment());
                imagesJson.put(i,json);
            }
            jsonObject.put(tag, imagesJson);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public List<ImageBean> getImages(String tag) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
//SecurePreferences preferences = new SecurePreferences(context);
        String imagesJson = preferences.getString(tag, "");
        List<ImageBean> list = new ArrayList<ImageBean>();
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject(imagesJson);
            JSONArray array = jsonObject.getJSONArray(tag);
            int len = array.length();
            for (int i = 0; i<len;i++){
                list.add(new ImageBean(array.getJSONObject(i).getString("url"),array.getJSONObject(i).getString("comment")));
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return list;
    }




    public void setKaihu(boolean isKaihu) {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("kaihu", isKaihu);
        editor.commit();
    }

    public boolean isKaiHu() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getBoolean("kaihu", false);
    }

    /*
     * @param
     */
    public void setToken(String access_token) {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        Editor editor = preferences.edit();
        editor.putString("token", access_token);
        editor.commit();
    }

    public String getToken() {
//		SharedPreferences preferences = PreferenceManager
//		.getDefaultSharedPreferences(context);
        SecurePreferences preferences = new SecurePreferences(context);
        return preferences.getString("token", "");
    }

    /**
     *写文件
     *
     * @param filename
     * @param data
     * @param <T>
     */
    public <T> void writeToFile(String filename, T... data) {
        // TODO Auto-generated method stub
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(context.getFilesDir().toString() + "/"
                            + filename));

            for (T list : data) {
                out.writeObject(list);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读文件
     *
     * @param filename
     * @param <T>
     * @return
     */
    public <T> Object getFromFile(String filename) {

        try {
            ObjectInputStream inputStream = new ObjectInputStream(
                    new FileInputStream(context.getFilesDir().toString() + "/"
                            + filename));
            Object data = inputStream.readObject();
            return data;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
