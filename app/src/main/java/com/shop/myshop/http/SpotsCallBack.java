package com.shop.myshop.http;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/17 0017.
 */
public abstract class SpotsCallBack<T> extends BaseCallBack<T> {
    private Context mContext;
    private SpotsDialog mDialog;

    public SpotsCallBack(Context mContext) {
        this.mContext = mContext;
        mDialog = new SpotsDialog(mContext, "拼命加载中……");
    }
//显示对话框
    public void showDialog(){
        mDialog.show();
    }
//关闭对话框
    public void dismissDialog(){
        if(mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void setMessage(String message){
        mDialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
        Log.i("TAG", "SpotsCallBack onFailure()"+request.url().toString());
        Log.e("TAG", "onFailure"+e);
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
