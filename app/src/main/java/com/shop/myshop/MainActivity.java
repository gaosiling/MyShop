package com.shop.myshop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.shop.myshop.entity.Tab;
import com.shop.myshop.fragment.Fragment_Cart;
import com.shop.myshop.fragment.Fragment_Category;
import com.shop.myshop.fragment.Fragment_Home;
import com.shop.myshop.fragment.Fragment_Hot;
import com.shop.myshop.fragment.Fragment_Mine;
import com.shop.myshop.widget.FragmentTabHost;
import com.shop.myshop.widget.LToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {
    @BindView(R.id.toolbar)
     LToolbar mToolbar;

    @BindView(android.R.id.tabhost)
     FragmentTabHost mTabhost;
    private LayoutInflater mInflater;
    private List<Tab> mTabs = new ArrayList<Tab>(5);
    private Fragment_Cart cart_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTab();
    }

    private void initTab() {
        Tab tab_home = new Tab(Fragment_Home.class, R.drawable.selector_icon_home, R.string.home);
        Tab tab_hot = new Tab(Fragment_Hot.class, R.drawable.selector_icon_hot, R.string.hot);
        Tab tab_category = new Tab(Fragment_Category.class, R.drawable.selector_icon_category, R.string.category);
        Tab tab_cart = new Tab(Fragment_Cart.class, R.drawable.selector_icon_cart, R.string.cart);
        Tab tab_mine = new Tab(Fragment_Mine.class, R.drawable.selector_icon_mine, R.string.mine);
        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
//        mTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));//把view作为indicator设置到tabSpec中
            //tabSpec作为一个tab添加到Tabhost中, 要跳转到的Fragment，传的数据
            mTabhost.addTab(tabSpec, tab.getFragment(), null);
        }

        mTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(getString(R.string.cart))) {
                    refData();
                }else {
                    mToolbar.showSearchView();
                    mToolbar.hideTitleView();
                }
            }
        });

        //去掉分割线
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabhost.setCurrentTab(0);
    }

    private void refData() {
        if (cart_fragment == null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null) {
                cart_fragment = (Fragment_Cart) fragment;
                cart_fragment.refData();
                cart_fragment.changeToolbar();
            }
        }else {
            cart_fragment.refData();
            cart_fragment.changeToolbar();
        }
    }

    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.tab_indicator_icon_iv);
        TextView text = (TextView) view.findViewById(R.id.tab_indicator_title_tv);
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        return view;
    }
}
