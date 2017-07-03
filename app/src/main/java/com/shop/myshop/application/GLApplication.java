package com.shop.myshop.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.shop.myshop.entity.User;
import com.shop.myshop.utils.UserLocalData;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2017/2/20 0020.
 */
public class GLApplication extends Application {
    private static GLApplication mInstance;
    private User user;

    public static GLApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initUser();

        //基本使用的初始化方法
        Fresco.initialize(this);
        ShareSDK.initSDK(this);

    }

    private void initUser() {
        user = UserLocalData.getUser(this);
    }

    public User getUser(){
        return user;
    }

    public void putUser(User user, String token){
        this.user = user;
        UserLocalData.putUser(this,user);
        UserLocalData.putToken(this,token);
    }

    public void clearUser(){
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ShareSDK.stopSDK(this);
    }
}
