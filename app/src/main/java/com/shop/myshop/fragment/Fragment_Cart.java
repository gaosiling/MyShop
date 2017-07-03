package com.shop.myshop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shop.myshop.MainActivity;
import com.shop.myshop.R;
import com.shop.myshop.adapter.CartAdapter;
import com.shop.myshop.adapter.decoration.DividerItemDecoration;
import com.shop.myshop.entity.ShoppingCart;
import com.shop.myshop.utils.CartProvider;
import com.shop.myshop.widget.LToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/1/12 0012.
 */
public class Fragment_Cart extends Fragment implements View.OnClickListener {
    public static final int ACTION_EDIT = 1;
    public static final int ACTION_COMPLETE = 2;

    //注解绑定控件
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkbox_all)
    CheckBox mCheckBox;
    @BindView(R.id.tv_total)
    TextView mTextTotal;
    @BindView(R.id.btn_order)
    Button mBtnOrder;
    @BindView(R.id.btn_del)
    Button mBtnDel;

    private CartAdapter mAdapter;
    private CartProvider cartProvider;
    private LToolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        cartProvider = CartProvider.getCartProviderInstance(getContext());
        showData();
        return view;
    }

    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    public void refData() {
        mAdapter.clearData();
        List<ShoppingCart> carts = cartProvider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
        mCheckBox.setChecked(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            mToolbar = (LToolbar) activity.findViewById(R.id.toolbar);
            changeToolbar();
        }
    }

    public void changeToolbar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.setRightButtonText("编辑");
        mToolbar.setRightButtonClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        if(ACTION_EDIT == action) {//当点击编辑
            showDelControl();
        }else if (ACTION_COMPLETE == action){
            hideDelControl();
        }
    }

    private void showDelControl(){
        mToolbar.setRightButtonText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_COMPLETE);
        mAdapter.checkAllOrNone(false);//取消全选
        mCheckBox.setChecked(false);//取消全选
    }

    private void hideDelControl() {
        mToolbar.setRightButtonText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);//显示合计
        mBtnOrder.setVisibility(View.VISIBLE);//显示去结算
        mBtnDel.setVisibility(View.GONE);//隐藏删除
        mToolbar.getRightButton().setTag(ACTION_EDIT);//按钮为编辑
        mAdapter.checkAllOrNone(true);//全选选中
        mAdapter.showTotalPrice();//显示价格
        mCheckBox.setChecked(true);//全选选中
    }

    @OnClick(R.id.btn_del)
    public void cartBtnClick(View view){
        mAdapter.delCart();
    }
}

