package com.shop.myshop.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shop.myshop.R;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

public class LToolbar extends Toolbar {
    ImageView imageView;
    private View mView;
    private EditText mSearchView;
    private TextView mTextTitle;
    private Button mRightButton;
    private ImageButton mLeftButton;

    private LayoutInflater mInflater;
    public LToolbar(Context context) {
        this(context, null);
    }

    public LToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

        setContentInsetsRelative(10, 10);

        if(attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.LToolbar, defStyleAttr, 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.LToolbar_rightButtonIcon);
            if(rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }
            final Drawable leftIcon = a.getDrawable(R.styleable.LToolbar_leftButtonIcon);
            if(rightIcon != null) {
                setLeftButtonIcon(leftIcon);
            }

            CharSequence rightButtonText = a.getText(R.styleable.LToolbar_rightButtonText);
            if(rightButtonText != null) {
                setRightButtonText(rightButtonText);
            }

            //拿到attrs中的属性值
            boolean isShowSearchView = a.getBoolean(R.styleable.LToolbar_isShowSearchView, false);
            if(isShowSearchView) {
                showSearchView();
                hideTitleView();
            }
            a.recycle();
        }
    }

    public void setRightButtonText(CharSequence rightButtonText) {
        if(mRightButton != null) {
            mRightButton.setText(rightButtonText);
        }
        mRightButton.setVisibility(VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable rightIcon){
        if(mRightButton != null) {
            mRightButton.setBackground(rightIcon);
            mRightButton.setVisibility(View.VISIBLE);
        }
    }

    public void setRightButtonIcon(int icon){
//        setRightButtonIcon(getResources().getDrawable(icon));

        if(mRightButton != null) {
            mRightButton.setBackgroundResource(icon);
            mRightButton.setVisibility(View.VISIBLE);
        }
    }
    public void setLeftButtonIcon(Drawable rightIcon){
        if(mLeftButton != null) {
            mLeftButton.setImageDrawable(rightIcon);
            mLeftButton.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        if(mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);
            mLeftButton = (ImageButton) mView.findViewById(R.id.toolbar_leftButton);
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL);
            addView(mView, layoutParams);
        }

    }
    /**
     * 重写setTitle
     * @param resId
     */
    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }
    @Override
    public void setTitle(CharSequence title) {
        initView();
        if(mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showSearchView(){
        if(mSearchView != null) {
            mSearchView.setVisibility(View.VISIBLE);
        }
    }
    public void hideSearchView(){
        if(mSearchView != null) {
            mSearchView.setVisibility(View.GONE);
        }
    }
    public void showTitleView(){
        if(mTextTitle != null) {
            mTextTitle.setVisibility(View.VISIBLE);
        }
    }
    public void hideTitleView(){
        if(mTextTitle != null) {
            mTextTitle.setVisibility(View.GONE);
            mRightButton.setVisibility(View.GONE);
        }
    }

    public void setRightButtonClickListener(OnClickListener listener){
        mRightButton.setOnClickListener(listener);
    }
    public void setLeftButtonClickListener(OnClickListener listener){
        mLeftButton.setOnClickListener(listener);
    }

    public Button getRightButton(){
        return this.mRightButton;
    }
}
