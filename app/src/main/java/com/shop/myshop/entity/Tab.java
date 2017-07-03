package com.shop.myshop.entity;

/**
 * Created by Administrator on 2017/1/12 0012.
 */
public class Tab {

    private Class fragment;
    private int icon;
    private int title;

    public Tab(Class fragment, int icon, int title) {
        this.fragment = fragment;
        this.icon = icon;
        this.title = title;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
