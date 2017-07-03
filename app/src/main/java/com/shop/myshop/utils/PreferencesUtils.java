package com.shop.myshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class PreferencesUtils {
    private static final String PREFERENCE_NAME = "shop_common";

    //存入购物车信息
    public static boolean putString(Context context, String key, String value){
        //MODE_PRIVATE应用内部进行访问
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    //读取购物车信息
    public static String getString(Context context, String key){
        return getString(context, key, null);
    }
    //读取购物车信息，有默认值
    private static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }
}
