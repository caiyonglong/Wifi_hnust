package com.cyl.wifi_hnust.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yonglong on 2016/5/10.
 */
public class Utils {
    public static boolean saveUserInfo(Context context, String number, String password){
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username",number);
        edit.putString("pwd",password);
        edit.commit();
        return true;
    }
    public static Map<String,String> getUserInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String number = sp.getString("username", null);
        String password = sp.getString("pwd", null);
        Map<String ,String> userMap = new HashMap<String,String>();
        userMap.put("username", number);
        userMap.put("password", password);
        return userMap;
    }
    public static boolean saveSwitch(Context context,boolean status){
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("switch", status);
        edit.commit();
        return true;
    }
    public static boolean getSwitchStautus(Context context){
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return sp.getBoolean("switch", false);
    }
}
