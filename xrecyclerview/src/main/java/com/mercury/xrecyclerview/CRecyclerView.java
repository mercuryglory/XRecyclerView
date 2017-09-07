package com.mercury.xrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
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

    private final AdapterDataObserver observer = new DataObserver();
    private WrapAdapter mWrapAdapter;


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
        mWrapAdapter = wrapAdapter;
        mWrapAdapter.registerAdapterDataObserver(observer);

    }

    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = e.getRawY() - mLastY;
                mLastY = e.getRawY();
                LogUtil.logI("lastY:" + isOnTop());
                if (isOnTop()) {
                    mRefreshHeader.onMove(deltaY / DEGREE);
                }
                break;
            default:
                //reset
                mLastY = -1;
                if (isOnTop() && mRefreshHeader.releaseAction()) {
                    if (loadingListener != null) {
                        loadingListener.refresh();
                    }
                    Log.e("refresh", "onTouchEvent: ");
                }
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
                Log.i("refresh_bind", mRefreshHeader.getVisibleHeight() + "");
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

    LoadingListener loadingListener;

    public interface LoadingListener {
        void refresh();
    }

    public void setOnLoadingListener(LoadingListener listener) {
        loadingListener = listener;
    }

    private boolean isRefreshHeader(int position) {
        return position == 0;
    }

    private boolean isOnTop() {
        return mRefreshHeader.getParent() != null;
    }

    private class SimpleViewHolder extends ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart + 1, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition + 1, toPosition + 1 + itemCount);
        }
    }


}
