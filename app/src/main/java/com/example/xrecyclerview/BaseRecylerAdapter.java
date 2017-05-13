package com.example.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者:    wang.zhonghao
 * 创建时间:  2016/10/16
 * 描述:     提供给使用RecyclerView的列表适配器使用的基类
 */
public abstract class BaseRecylerAdapter<T> extends RecyclerView.Adapter {
    public Context mContext;

    public List<T> mData = new ArrayList<>();

    public LayoutInflater mInflater;


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> list) {
        this.mData.clear();
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        int lastIndex = this.mData.size();
        if (this.mData.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void removePos(int position) {
        if(this.mData.size() > 0) {
            mData.remove(position);
            notifyItemRemoved(position);
        }

    }

    public void remove(Object obj) {
        if(this.mData.size() > 0) {
            mData.remove(obj);
        }

    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

}
