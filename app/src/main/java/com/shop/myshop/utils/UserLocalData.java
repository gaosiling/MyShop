package com.shop.myshop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.shop.myshop.entity.User;
import com.shop.myshop.http.Contents;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class UserLocalData {
    public static void putUser(Context context, User user) {
        String user_json = GsonUtils.toJson(user);
        PreferencesUtils.putString(context, Contents.USER_JSON, user_json);
    }

    public static void putToken(Context context, String token){
        PreferencesUtils.putString(context,Contents.TOKEN, token);
    }

    public static User getUser(Context context){
        String user_json = PreferencesUtils.getString(context, Contents.USER_JSON);
        if(!TextUtils.isEmpty(user_json)) {
            return GsonUtils.fromJson(user_json, User.class);
        }
        return null;
    }

    public static String getToken(Context context){
        return PreferencesUtils.getString(context,Contents.TOKEN);
    }

    public static void clearUser(Context context){
        PreferencesUtils.putString(context, Contents.USER_JSON, "");
    }

    public static void clearToken(Context context){
        PreferencesUtils.putString(context, Contents.TOKEN , "");
    }
}
