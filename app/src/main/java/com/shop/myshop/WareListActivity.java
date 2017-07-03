package com.shop.myshop;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.shop.myshop.adapter.HWAdapter;
import com.shop.myshop.adapter.decoration.DividerItemDecoration;
import com.shop.myshop.entity.PageResult;
import com.shop.myshop.entity.Wares;
import com.shop.myshop.http.Contents;
import com.shop.myshop.utils.PageUtils;
import com.shop.myshop.widget.LToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WareListActivity extends Activity implements PageUtils.OnPageListener
        , TabLayout.OnTabSelectedListener, View.OnClickListener {

    private static final int TAG_DEFAULT = 0;
    private static final int TAG_SALE = 1;
    private static final int TAG_PRICE = 2;
    private static final int ACTION_LIST = 1;
    private static final int ACTION_GRID = 2;

    @BindView(R.id.ware_list_tl)
    TabLayout mTabLayout;
    @BindView(R.id.ware_list_mrl)
    MaterialRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar)
    LToolbar mToolbar;
    @BindView(R.id.ware_list_summary_tv)
    TextView mSummaryTv;
    @BindView(R.id.ware_list_rv)
    RecyclerView mWaresRecyclerView;


    private int orderBy = 0;
    private long campaignId = 0;
    private HWAdapter mWaresAdapter;
    PageUtils pageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_list);
        ButterKnife.bind(this);

        //获取传过来的campaignId
        campaignId = getIntent().getLongExtra(Contents.COMPAIGNID, 0);
        initToolBar();
        initTab();
        getData();
    }

    private void initToolBar() {
        mToolbar.setLeftButtonClickListener(this);
        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        mToolbar.setRightButtonClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_LIST);
    }

    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(this);
    }

    private void getData() {
        pageUtils = PageUtils.newBuilder()
                .setUrl(Contents.API.WARES_CAMPAIGN_LIST)
                .putParam("campaignId", campaignId)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this, new TypeToken<PageResult<Wares>>() {
                }.getType());
        pageUtils.request();
    }

    @Override
    public void load(List datas, int totalPage, int totalCount) {
        mSummaryTv.setText("共有" + totalCount + "件商品");
        if (mWaresAdapter == null) {
            mWaresAdapter = new HWAdapter(this, datas);
            mWaresRecyclerView.setAdapter(mWaresAdapter);
            mWaresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mWaresRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mWaresRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }

    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        //回滚到第0位
        mWaresRecyclerView.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pageUtils.putParam("orderBy", orderBy);
        pageUtils.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.toolbar_leftButton) {
            WareListActivity.this.finish();
        }else if(v.getId() == R.id.toolbar_rightButton) {
            int action = (int) v.getTag();
            switch (action) {
                case ACTION_LIST :
                    mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
                    mToolbar.getRightButton().setTag(ACTION_GRID);
                    mWaresRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                    mWaresAdapter.resetLayout(R.layout.item_grid_wares);
                    break;
                case ACTION_GRID :
                    mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
                    mToolbar.getRightButton().setTag(ACTION_LIST);
                    mWaresRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                    mWaresAdapter.resetLayout(R.layout.item_hot_wares);
                    break;
            }
        }
    }
}
