package com.shop.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.shop.myshop.R;
import com.shop.myshop.WareDetailActivity;
import com.shop.myshop.adapter.BaseAdapter;
import com.shop.myshop.adapter.HWAdapter;
import com.shop.myshop.adapter.decoration.DividerItemDecoration;
import com.shop.myshop.entity.PageResult;
import com.shop.myshop.entity.Wares;
import com.shop.myshop.http.Contents;
import com.shop.myshop.utils.PageUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/1/12 0012.
 */
public class Fragment_Hot extends Fragment {

    private HWAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.hot_rv);
        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.hot_mrl);

        PageUtils pageUtils = PageUtils.newBuilder()
                .setUrl(Contents.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(new PageUtils.OnPageListener() {
                    @Override
                    public void load(List datas, int totalPage, int totalCount) {
                        mAdapter = new HWAdapter(getContext(), datas);
                        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
//                                Toast.makeText(getContext(), "点击了：" + position, Toast.LENGTH_SHORT).show();
                                //获得条目商品实体
                                Wares wares = mAdapter.getItem(position);
                                //打开商品详情的Activity并将商品传过去
                                Intent intent = new Intent(getActivity(), WareDetailActivity.class);
                                intent.putExtra(Contents.WARE, wares);//实体需要实现Serializable
                                startActivity(intent);
                            }
                        });
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                                getContext(), DividerItemDecoration.VERTICAL_LIST));
                    }

                    @Override
                    public void refresh(List datas, int totalPage, int totalCount) {
                        mAdapter.clearData();
                        mAdapter.addData(datas);
                        mRecyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void loadMore(List datas, int totalPage, int totalCount) {
                        mAdapter.addData(mAdapter.getItemCount(), datas);
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount());
                    }
                })
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .build(getContext(), new TypeToken<PageResult<Wares>>() {
                }.getType());
        pageUtils.request();
        return view;
    }

//    public void test(){
//        PageUtils pageUtils = null;
//        pageUtils.putParam("key","value");
//        pageUtils.request();
//    }
}