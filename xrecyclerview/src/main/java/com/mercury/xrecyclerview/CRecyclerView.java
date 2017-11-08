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

    private final AdapterDataObserver observer = new DataObserver();
    private WrapAdapter mWrapAdapter;
    private Adapter     mAdapter;


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

    @Override
    public void setAdapter(Adapter adapter) {
        WrapAdapter wrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(wrapAdapter);
        mWrapAdapter = wrapAdapter;
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(observer);
        //一开始手动调用
        observer.onChanged();

    }

    public void refreshComplete() {
        mRefreshHeader.refreshComplete();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        /**
         * 一开始的down是item所在的parent消费,滑动后才交给Recyclerview.如果在开始的时候仍然让mLastY保持为-1的话,
         * 则手指在item上开始向下滑动时就会得到错误的偏很大的deltaY值,使得刷新布局过早出现
         */
        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.logI("down:" + mLastY);
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaY = e.getRawY() - mLastY;
                LogUtil.logI("lastY:" + mLastY);
                LogUtil.logI("rawY:" + e.getRawY());
                mLastY = e.getRawY();
                if (isOnTop()) {
                    LogUtil.logI("lastY:" + deltaY);
                    mRefreshHeader.onMove(deltaY / DEGREE);
                }
                break;
            default:
                //reset
                mLastY = -1;
                if (isOnTop() && mRefreshHeader.releaseAction()) {
                    if (loadingListener != null) {
                        loadingListener.onRefresh();
                    }
                    LogUtil.logI("refresh", "onTouchEvent: ");
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
                LogUtil.logI("refresh_bind", mRefreshHeader.getVisibleHeight() + "");
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
            LogUtil.logI("refresh_bind", "getItemCount: " + mAdapter.getItemCount());
            if (mAdapter != null) {
                return mAdapter.getItemCount() + 1;
            }
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            LogUtil.logI("refresh_bind", "position:" + position);
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            int adjustPos = position - 1;
            if (adjustPos < mAdapter.getItemCount()) {
                return mAdapter.getItemViewType(adjustPos);
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            LogUtil.logI("refresh_bind", "getItemId: " + position);
            if (mAdapter != null && position >= 1) {
                int adjustPos = position - 1;
                return mAdapter.getItemId(adjustPos);
            }
            return -1;
        }
    }

    LoadingListener loadingListener;

    public interface LoadingListener {
        void onRefresh();
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
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
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
