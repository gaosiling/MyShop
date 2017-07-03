package com.shop.myshop.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/16 0016.
 */
public abstract class BaseCallBack<T> {
    public Type mType;

    /**
     * 将泛型T转为Type
     */
    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass = subclass.getGenericSuperclass();//得到泛型的父类
        if (superclass instanceof Class){
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType)superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallBack(){
        mType = getSuperclassTypeParameter(getClass());
    }

    //网络请求之前调用
    public abstract void onRequestBefore(Request request);
    public abstract void onFailure(Request request, IOException e);
    public abstract void onSuccess(Response response, T t);
    public abstract void onError(Response response, int code, Exception e);
    public abstract void onResponse(Response response);
}
