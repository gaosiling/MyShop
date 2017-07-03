package com.shop.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.shop.myshop.R;
import com.shop.myshop.adapter.BaseAdapter;
import com.shop.myshop.adapter.CategoryAdapter;
import com.shop.myshop.adapter.WaresAdapter;
import com.shop.myshop.adapter.decoration.DividerGridItemDecoration;
import com.shop.myshop.adapter.decoration.DividerItemDecoration;
import com.shop.myshop.entity.Banner;
import com.shop.myshop.entity.Category;
import com.shop.myshop.entity.PageResult;
import com.shop.myshop.entity.Wares;
import com.shop.myshop.http.BaseCallBack;
import com.shop.myshop.http.Contents;
import com.shop.myshop.http.OkHttpHelper;
import com.shop.myshop.http.SpotsCallBack;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/1/12 0012.
 */
public class Fragment_Category extends Fragment {

    private static final int STATE_NORMAL = 0;//正常状态
    private static final int STATE_REFRESH = 1;//刷新状态
    private static final int STATE_MORE = 2;//加载更多状态
    private int state = STATE_NORMAL;//默认状态是正常状态

    private int currPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;


    @BindView(R.id.category_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.category_sl)
    SliderLayout mSliderLayout;
    @BindView(R.id.category_wares_rv)
    RecyclerView mWaresRecyclerView;
    @BindView(R.id.category_mrl)
    MaterialRefreshLayout mRefreshLayout;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        initRefreshLayout();
        requestCategoryData();
        requestBannerData();
        return view;
    }

    private void requestCategoryData() {
        okHttpHelper.get(Contents.API.CATEGORY_LIST,
                new SpotsCallBack<List<Category>>(getContext()) {
                    @Override
                    public void onSuccess(Response response, List<Category> categories) {
                        showCategoryData(categories);
                        Log.e("TAG", "onSuccess");
                        if (categories != null && categories.size() > 0) {
                            requestWares(categories.get(0).getId());
                        }
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {
                        Log.e("TAG", "onError" + e);
                    }
                });
    }

    private void requestBannerData() {
        String url = Contents.API.BANNER + "?type=2";
        okHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, final List<Banner> banners) {
                Log.e("TAG", "onSuccess");
                showBannerData(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.e("TAG", "onError" + e);
            }
        });
    }

    private void showCategoryData(List<Category> categories) {
        mCategoryAdapter = new CategoryAdapter(getContext(), categories);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);
                currPage = 1;
                pageSize = 10;
                state = STATE_NORMAL;
                requestWares(category.getId());

            }
        });
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    private void showBannerData(List<Banner> banners) {
        if (banners != null) {
            for (Banner banner : banners) {
                DefaultSliderView sliderView = new DefaultSliderView(getActivity());
                sliderView.image(banner.getImgUrl())
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);
            }
        }
//
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//设置默认指示器
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        ;//设置自定义动画
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);//设置转场效果
        mSliderLayout.setDuration(3000);
    }

    private void requestWares(long categoryId) {
        String url = Contents.API.WARES_LIST
                + "?categoryId=" + categoryId + "&curPage=" + currPage + "&pageSize=" + pageSize;
        okHttpHelper.get(url, new BaseCallBack<PageResult<Wares>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onSuccess(Response response, PageResult<Wares> waresPageResult) {
                currPage = waresPageResult.getCurrentPage();
                totalPage = waresPageResult.getTotalPage();
                showWaresData(waresPageResult.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }
        });
    }

    private void showWaresData(List<Wares> datas) {
        switch (state) {
            case STATE_NORMAL:
                if (mWaresAdapter == null) {
                    mWaresAdapter = new WaresAdapter(getContext(), datas);

                    mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast.makeText(getContext(), "点击了：" + position, Toast.LENGTH_SHORT).show();
                        }
                    });
                    mWaresRecyclerView.setAdapter(mWaresAdapter);
                    mWaresRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mWaresRecyclerView.addItemDecoration(new DividerGridItemDecoration(
                            getContext()));
                }else {
                    mWaresAdapter.clearData();
                    mWaresAdapter.addData(datas);
                }
                break;
            case STATE_REFRESH:
                mWaresAdapter.clearData();
                mWaresAdapter.addData(datas);
                mWaresRecyclerView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
                break;
            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getItemCount(), datas);
                mWaresRecyclerView.scrollToPosition(mWaresAdapter.getItemCount());
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }

    private void initRefreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (currPage <= totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getContext(), "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData() {
        currPage = 1;
        state = STATE_REFRESH;
        requestCategoryData();
    }

    private void loadMoreData() {
        currPage += 1;
        state = STATE_MORE;
        requestCategoryData();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        null.unbind();
//    }

//    @OnClick({R.id.category_rv, R.id.category_sl, R.id.category_wares_rv, R.id.category_mrl})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.category_rv:
//                break;
//            case R.id.category_sl:
//                break;
//            case R.id.category_wares_rv:
//                break;
//            case R.id.category_mrl:
//                break;
//        }
//    }
}
