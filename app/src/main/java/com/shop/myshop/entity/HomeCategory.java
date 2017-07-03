package com.shop.myshop.entity;

/**
 * Created by Administrator on 2017/2/15 0015.
 */
public class HomeCategory extends Category {
    private int imgBig;
    private int imgSmallTop;
    private int imgSmallBottom;

    public HomeCategory(int imgBig, int imgSmallBottom, int imgSmallTop) {
        this.imgBig = imgBig;
        this.imgSmallBottom = imgSmallBottom;
        this.imgSmallTop = imgSmallTop;
    }

    public HomeCategory(long id, String name, int imgBig, int imgSmallBottom, int imgSmallTop) {
        super(id, name);
        this.imgBig = imgBig;
        this.imgSmallBottom = imgSmallBottom;
        this.imgSmallTop = imgSmallTop;
    }

    public HomeCategory(String name, int imgBig, int imgSmallBottom, int imgSmallTop) {
        super(name);
        this.imgBig = imgBig;
        this.imgSmallBottom = imgSmallBottom;
        this.imgSmallTop = imgSmallTop;
    }

    public int getImgBig() {
        return imgBig;
    }

    public void setImgBig(int imgBig) {
        this.imgBig = imgBig;
    }

    public int getImgSmallBottom() {
        return imgSmallBottom;
    }

    public void setImgSmallBottom(int imgSmallBottom) {
        this.imgSmallBottom = imgSmallBottom;
    }

    public int getImgSmallTop() {
        return imgSmallTop;
    }

    public void setImgSmallTop(int imgSmallTop) {
        this.imgSmallTop = imgSmallTop;
    }
}
