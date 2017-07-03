package com.shop.myshop.entity;

/**
 * Created by Administrator on 2017/2/16 0016.
 */
public class Banner extends BaseBean {
    private String name;
    private String description;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String imgUrl;

}
