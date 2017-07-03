package com.shop.myshop;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shop.myshop.entity.Wares;
import com.shop.myshop.http.Contents;
import com.shop.myshop.utils.CartProvider;
import com.shop.myshop.utils.ToastUtils;
import com.shop.myshop.widget.LToolbar;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;

public class WareDetailActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.toolbar)
    LToolbar mToolbar;

    private Wares mWare;
    private WebAppInterface mAppInterface;
    private CartProvider cartProvider;
    private SpotsDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);
        ButterKnife.bind(this);

        //接收热卖传过来的商品实体
        Serializable serializable = getIntent().getSerializableExtra(Contents.WARE);
        if (serializable == null) {//为空返回热卖
            finish();
        }
        mWare = (Wares) serializable;

        cartProvider = new CartProvider(this);
        mDialog  = new SpotsDialog(this, "loading……");
        mDialog.show();

        initToolBar();
        initWebView();
    }

    private void initToolBar() {
        mToolbar.setLeftButtonClickListener(this);
        mToolbar.setRightButtonText("分享");
        mToolbar.setRightButtonClickListener(this);
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);//设置是否阻止图像的显示
        settings.setAppCacheEnabled(true);//设置缓存
        mWebView.loadUrl(Contents.API.WARES_DETAIL);//打开指定URL的html文件
        mAppInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(mAppInterface, "appInterface");
        mWebView.setWebViewClient(new WVC());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_leftButton:
                this.finish();
                break;
            case R.id.toolbar_rightButton:
                showShare();
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(getString(R.string.ssdk_oks_share));
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mWare.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

    class WebAppInterface {
        private Context mContext;

        public WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void showDetail() {
            mWebView.loadUrl("javascript:showDetail(" + mWare.getId() + ")");
        }

        @JavascriptInterface
        public void buy(long id) {
            cartProvider.put(mWare);//j把商品存入购物车存储器
            ToastUtils.show(mContext, "成功添加到购物车");
        }

        @JavascriptInterface
        public void addToFavorites(long id) {

        }
    }

    class WVC extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("phoenix://")) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:232323"));
                if (ActivityCompat.checkSelfPermission(WareDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(WareDetailActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 0x11);
                    return true;
                }
                startActivity(intent);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(mDialog != null  && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            mAppInterface.showDetail();
        }
    }
}
