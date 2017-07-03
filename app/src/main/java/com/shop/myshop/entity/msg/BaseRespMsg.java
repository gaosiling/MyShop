package com.shop.myshop.entity.msg;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/2 0002.
 */

public class BaseRespMsg implements Serializable {
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_ERROR = 0;
    public static final String MSG_SUCCESS = "success";

    protected int status = STATUS_SUCCESS;
    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
