package com.shop.myshop.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class ShoppingCart extends Wares implements Serializable {
    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
