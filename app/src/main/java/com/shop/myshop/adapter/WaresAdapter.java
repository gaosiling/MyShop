package com.shop.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shop.myshop.R;
import com.shop.myshop.entity.Wares;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class WaresAdapter extends SimpleAdapter<Wares> {

    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.item_grid_wares , datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, Wares item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.hot_wares_img_sdv);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        Log.i("TAG", "item.getImgUrl()======="+item.getImgUrl());
        holder.getTextView(R.id.hot_wares_title_tv).setText(item.getName());
        holder.getTextView(R.id.hot_wares_price_tv).setText("￥"+item.getPrice()+"");
//        holder.getButton(R.id.XX).setOnClickListener();
    }
}
