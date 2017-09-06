package com.mercury.xrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mercury.xrecyclerview.widget.ChatRefreshHeader;

/**
 * Created by wang.zhonghao on 2017/9/6.
 */

public class CRecyclerView extends RecyclerView {

    public static final int TYPE_REFRESH_HEADER = 10100;
    private ChatRefreshHeader mRefreshHeader;
    public static final int DEGREE = 3;

    private float mLastY = -1;


    public CRecyclerView(Context context) {
        this(context, null);
    }

    public CRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mRefreshHeader = new ChatRefreshHeader(getContext());

    }

    public void setAdapter(Adapter adapter) {
        WrapAdapter wrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(wrapAdapter);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = e.getRawY() - mLastY;
                LogUtil.logI("deltaY:" + deltaY);
                mLastY = e.getRawY();
                mRefreshHeader.onMove(deltaY / DEGREE);
                break;
            default:
                //reset
                mLastY = -1;
                break;

        }
        return super.onTouchEvent(e);
    }

    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter mAdapter;

        public WrapAdapter(Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(mRefreshHeader);
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type == TYPE_REFRESH_HEADER) {
                return;
            }
            int adjustPos = position - 1;
            if (mAdapter != null) {
                mAdapter.onBindViewHolder(holder, adjustPos);
            }
        }

        @Override
        public int getItemCount() {
            if (mAdapter != null) {
                return mAdapter.getItemCount() + 1;
            }
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            return mAdapter.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            if (mAdapter != null && position >= 1) {
                int adjustPos = position - 1;
                return mAdapter.getItemId(adjustPos);
            }
            return -1;
        }
    }

    private boolean isRefreshHeader(int position) {
        return position == 0;
    }

    private class SimpleViewHolder extends ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

}
