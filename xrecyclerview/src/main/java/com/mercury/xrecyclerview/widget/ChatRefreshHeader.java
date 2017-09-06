package com.mercury.xrecyclerview.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mercury.xrecyclerview.BaseRefreshHeader;
import com.mercury.xrecyclerview.R;

/**
 * Created by wang.zhonghao on 2017/9/6.
 */

public class ChatRefreshHeader extends LinearLayout implements BaseRefreshHeader{

    private FrameLayout container;
    private ImageView mImageView;

    public ChatRefreshHeader(@NonNull Context context) {
        super(context);
        initView();
    }

    public ChatRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {
        container = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout
                .header_chatting, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, 0);
        addView(container, params);

        mImageView = (ImageView) container.findViewById(R.id.iv_refresh);
        mImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                .progressbar));

        AnimationDrawable animation = (AnimationDrawable) mImageView.getDrawable();
        animation.start();

    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) container
                .getLayoutParams();
        layoutParams.height = height;
        container.setLayoutParams(layoutParams);

    }

    public int getVisibleHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) container
                .getLayoutParams();
        return layoutParams.height;
    }

    @Override
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());

        }
    }

    @Override
    public boolean releaseAction() {
        return false;
    }

    @Override
    public void refreshComplete() {

    }
}
