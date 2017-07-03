package com.shop.myshop.entity;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public class Category extends  BaseBean {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
