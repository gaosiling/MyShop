package com.shop.myshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.shop.myshop.application.GLApplication;
import com.shop.myshop.entity.User;
import com.shop.myshop.entity.msg.LoginRespMsg;
import com.shop.myshop.http.Contents;
import com.shop.myshop.http.OkHttpHelper;
import com.shop.myshop.http.SpotsCallBack;
import com.shop.myshop.utils.ToastUtils;
import com.shop.myshop.widget.ClearEditText;
import com.shop.myshop.widget.LToolbar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/5 0005.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    LToolbar mToolbar;
    @BindView(R.id.login_phone_cet)
    ClearEditText mETPhone;
    @BindView(R.id.login_pwd_cet)
    ClearEditText mETPwd;

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initToolBar();
    }

    private void initToolBar() {
        mToolbar.setLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick({R.id.login_btn, R.id.login_reg_tv})
    public void loginOrReg(View view){
        switch (view.getId()) {
            case R.id.login_btn :
                String phone = mETPhone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    ToastUtils.show(this, "请输入手机号");
                    return;
                }
                String pwd = mETPwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)) {
                    ToastUtils.show(this, "请输入密码");
                    return;
                }

                Map<String, Object> params = new HashMap<>(2);
                params.put("phone", phone);
                params.put("password", pwd);
                okHttpHelper.post(Contents.API.LOGIN, params, new SpotsCallBack<LoginRespMsg<User>>(this) {
                    @Override
                    public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                        GLApplication application = GLApplication.getInstance();
                        application.putUser(userLoginRespMsg.getData(),userLoginRespMsg.getToken());
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });
                break;
            case R.id.login_reg_tv:

                break;
        }
    }
}
