package com.shop.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shop.myshop.R;
import com.shop.myshop.entity.Wares;
import com.shop.myshop.utils.CartProvider;
import com.shop.myshop.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22 0022.
 */

public class HWAdapter extends SimpleAdapter<Wares> {
    CartProvider provider;

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.item_hot_wares , datas);
        provider = CartProvider.getCartProviderInstance(context);
    }

    @Override
    protected void convert(BaseViewHolder holder, final Wares item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.hot_wares_img_sdv);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
        holder.getTextView(R.id.hot_wares_title_tv).setText(item.getName());
        holder.getTextView(R.id.hot_wares_price_tv).setText(item.getPrice()+"");
        Button button = holder.getButton(R.id.hot_wares_add_btn);
        if(button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    provider.put(item);//直接传商品
                    ToastUtils.show(mContext, "已成功添加到购物车");
                }
            });
        }
    }

    public void resetLayout(int layoutId) {
        this.mLayoutResId = layoutId;
        notifyItemRangeChanged(0, getItemCount());
    }
}
