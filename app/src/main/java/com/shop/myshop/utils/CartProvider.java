package com.shop.myshop.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.shop.myshop.entity.ShoppingCart;
import com.shop.myshop.entity.Wares;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24 0024.
 */
//购物车的存储器
public class CartProvider {
    private static final String CART_JSON = "cart_json";

    private SparseArray<ShoppingCart> datas = null;
    private Context mContext;

    private static CartProvider cartProvider = null;

    public CartProvider(Context context) {
        this.datas = new SparseArray<>(10);//存放购物车记录
        this.mContext = context;
        listToSparse();
    }

    public static CartProvider getCartProviderInstance(Context context) {
        if (cartProvider == null) {
            cartProvider = new CartProvider(context);
        }
        return cartProvider;
    }

    //存放商品
    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get(cart.getId().intValue());//获取商品
        if (temp != null) {//购物车已经有该商品
            temp.setCount(temp.getCount() + 1);
        } else {//没有
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(), temp);
        commit();
    }

    //存放商品
    public void put(Wares wares) {
        ShoppingCart cart = convertData(wares);
        put(cart);
    }

    //更新
    public void update(ShoppingCart cart) {
        datas.put(cart.getId().intValue(), cart);
        commit();
    }

    //删除
    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }

    public void commit() {
        List<ShoppingCart> carts = sparseToList();
        PreferencesUtils.putString(mContext, CART_JSON, GsonUtils.toJson(carts));
    }

    //获取集合
    private List<ShoppingCart> sparseToList() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        return list;
    }

    //从本地获取数据
    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    public List<ShoppingCart> getDataFromLocal() {
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = GsonUtils.fromJson(json, new TypeToken<List<ShoppingCart>>() {
            }.getType());
        }
        return carts;
    }

    private void listToSparse() {
        List<ShoppingCart> carts = getDataFromLocal();
        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                datas.put(cart.getId().intValue(), cart);
            }
        }
    }

    //把商品转换为ShoppingCart
    private ShoppingCart convertData(Wares item) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());
        cart.setStock(item.getStock());
        return cart;
    }

}
