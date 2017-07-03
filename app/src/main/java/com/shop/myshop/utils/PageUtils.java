package com.shop.myshop.utils;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.shop.myshop.entity.PageResult;
import com.shop.myshop.http.OkHttpHelper;
import com.shop.myshop.http.SpotsCallBack;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/28 0028.
 */

public class PageUtils {
    private static final int STATE_NORMAL = 0;//正常状态
    private static final int STATE_REFRESH = 1;//刷新状态
    private static final int STATE_MORE = 2;//加载更多状态
    private int state = STATE_NORMAL;//默认状态是正常状态

    private static Builder builder;
    private OkHttpHelper okHttpHelper;

    private PageUtils() {
        okHttpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder() {
        builder = new Builder();
        return builder;
    }

    public void request() {
        requestData();
    }

    public void putParam(String key, Object value){
        builder.putParam(key, value);
    }

    private void initRefreshLayout() {
        builder.refreshLayout.setLoadMore(builder.canLoadMore);
        builder.refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (builder.pageIndex <= builder.totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(builder.context, "没有更多数据", Toast.LENGTH_SHORT).show();
                    builder.refreshLayout.finishRefreshLoadMore();
                }
            }
        });
    }

    private void refreshData() {
        builder.pageIndex = 1;
        state = STATE_REFRESH;
        requestData();
    }

    private void loadMoreData() {
        builder.pageIndex += 1;
        state = STATE_MORE;
        requestData();
    }

    public void requestData() {
        okHttpHelper.get(buildUrl(), new RequestCallBack(builder.context));
    }

    private String buildUrl() {
        return builder.url + "?" + builderUrlParams();
    }

    private String builderUrlParams() {
        HashMap<String, Object> map = builder.params;
        map.put("curPage", builder.pageIndex);
        map.put("pageSize", builder.pageSize);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    private <T> void showData(List<T> datas, int totalPage, int totalCount) {
        if (builder.onPageListener != null) {
            switch (state) {
                case STATE_NORMAL:
                    builder.onPageListener.load(datas, totalPage, totalCount);
                    break;
                case STATE_REFRESH:
                    builder.onPageListener.refresh(datas, totalPage, totalCount);
                    builder.refreshLayout.finishRefresh();
                    break;
                case STATE_MORE:
                    builder.onPageListener.loadMore(datas, totalPage, totalCount);
                    builder.refreshLayout.finishRefreshLoadMore();
                    break;
            }
        }
    }

    public interface OnPageListener<T> {
        void load(List<T> datas, int totalPage, int totalCount);

        void refresh(List<T> datas, int totalPage, int totalCount);

        void loadMore(List<T> datas, int totalPage, int totalCount);
    }

    public static class Builder {
        private Context context;
        private Type type;
        private String url;
        private MaterialRefreshLayout refreshLayout;
        private boolean canLoadMore;

        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 10;

        private HashMap<String, Object> params = new HashMap<>(5);

        private OnPageListener onPageListener;

        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }

        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout) {
            this.refreshLayout = refreshLayout;
            return builder;
        }

        public Builder setLoadMore(boolean loadMore) {
            this.canLoadMore = loadMore;
            return builder;
        }

        public Builder setPageSize(int pageSize) {
            this.pageSize = pageSize;
            return builder;
        }


        public Builder putParam(String key, Object value) {
            params.put(key, value);
            return builder;
        }

        public PageUtils build(Context context, Type type) {
            this.context = context;
            this.type = type;
            validate();
            return new PageUtils();
        }

        private void validate() {
            if (context == null) {
                throw new RuntimeException("Context can't be null");
            }
            if (this.url == null || "".equals(this.url)) {
                throw new RuntimeException("Url can't be null");
            }
            if (refreshLayout == null) {
                throw new RuntimeException("MaterialRefreshLayout can't be null");
            }
        }
    }

    class RequestCallBack<T> extends SpotsCallBack<PageResult<T>> {

        public RequestCallBack(Context mContext) {
            super(mContext);
            // /把要解析的类型传给BaseCallBack，OkHttpHelper解析数据使用，否则包NullPoint
            super.mType = builder.type;
        }

        @Override
        public void onSuccess(Response response, PageResult<T> pageResult) {
            builder.pageIndex = pageResult.getCurrentPage();
            builder.totalPage = pageResult.getTotalPage();
            showData(pageResult.getList(), builder.totalPage, pageResult.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {

        }
    }
}
