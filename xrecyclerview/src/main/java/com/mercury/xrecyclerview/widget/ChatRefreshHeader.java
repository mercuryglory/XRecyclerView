package com.mercury.xrecyclerview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mercury.xrecyclerview.BaseRefreshHeader;
import com.mercury.xrecyclerview.LogUtil;
import com.mercury.xrecyclerview.R;

/**
 * Created by wang.zhonghao on 2017/9/6.
 */

public class ChatRefreshHeader extends LinearLayout implements BaseRefreshHeader{

    private LinearLayout container;
    private ImageView      mImageView;
    private int measuredHeight;
    private boolean isRefreshing;

    public ChatRefreshHeader(@NonNull Context context) {
        super(context);
        initView();
    }

    public ChatRefreshHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    private void initView() {

        container = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout
                .header_chatting, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, 0);
        addView(container, params);

        mImageView = (ImageView) container.findViewById(R.id.iv_refresh);
        mImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable
                .progressbar));

        AnimationDrawable animation = (AnimationDrawable) mImageView.getDrawable();
        animation.start();
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        measuredHeight = getMeasuredHeight();
        LogUtil.logI("measuredHeight" + measuredHeight);
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
            //如果刷新的布局已经是可见的或者手势向下拉动了一定距离,布局要重新更新高度
            setVisibleHeight((int) delta + getVisibleHeight());

        }
    }

    @Override
    public boolean releaseAction() {
        //如果目前布局的高度已经大于本身应该拥有的最大高度,并且当前没有进行刷新,松手时就执行刷新动作
        if (!isRefreshing && getVisibleHeight() > measuredHeight) {
            isRefreshing = true;
            smoothScrollTo(measuredHeight);
            return true;
        }
        smoothScrollTo(0);
        return false;
    }

    private void smoothScrollTo(int position) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getVisibleHeight(), position);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    /**
     * 刷新动作执行完毕后执行
     */
    @Override
    public void refreshComplete() {
        isRefreshing = false;
        setVisibleHeight(0);
        smoothScrollTo(0);
        LogUtil.logI("releaseHeight " + getVisibleHeight());
    }
}
