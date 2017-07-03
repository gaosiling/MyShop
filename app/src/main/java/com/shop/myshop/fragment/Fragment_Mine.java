package com.shop.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shop.myshop.LoginActivity;
import com.shop.myshop.R;
import com.shop.myshop.application.GLApplication;
import com.shop.myshop.entity.User;
import com.shop.myshop.http.Contents;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/1/12 0012.
 */
public class Fragment_Mine extends BaseFragment {
    @BindView(R.id.mine_head_civ)
    CircleImageView mHeadCIV;
    @BindView(R.id.mine_username_tv)
    TextView mUsernameTV;
    @BindView(R.id.mine_layout_btn)
    Button mLogoutBtn;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void init() {
        showUser();
    }

    @OnClick(R.id.mine_username_tv)
    public void click(View view) {
        switch (view.getId()) {
            case R.id.mine_username_tv:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, Contents.REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void showUser() {
        User user = GLApplication.getInstance().getUser();
        if (user == null) {
            mLogoutBtn.setVisibility(View.GONE);
            mHeadCIV.setImageResource(R.drawable.default_head);
            mUsernameTV.setText(R.string.to_login);
        } else {
            mLogoutBtn.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(user.getLogo_url())) {
                Picasso.with(getActivity()).load(Contents.API.BASE_URL + user.getLogo_url()).into(mHeadCIV);
            }
            mUsernameTV.setText(user.getUsername());
        }
    }
}
