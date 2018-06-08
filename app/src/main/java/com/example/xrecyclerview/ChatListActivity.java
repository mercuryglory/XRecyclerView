package com.example.xrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xrecyclerview.adapter.BaseRecylerAdapter;
import com.mercury.xrecyclerview.CRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wang.zhonghao on 2017/9/6.
 * 扩展RecyclerView的仿QQ列表
 */

public class ChatListActivity extends AppCompatActivity implements CRecyclerView.LoadingListener {

    CRecyclerView mCRecyclerView;
    MyAdapter mMyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        mMyAdapter = new MyAdapter(this);
        mCRecyclerView = (CRecyclerView) findViewById(R.id.crecyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        mCRecyclerView.setLayoutManager(manager);
        mCRecyclerView.setAdapter(mMyAdapter);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add("初始化" + i + "个条目");
        }

        mMyAdapter.setData(list);

        mCRecyclerView.setOnLoadingListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                for (int i = 5; i < 10; i++) {
                    list.add("我是第" + i + "个项目");
                }
                mMyAdapter.addData(list, 0);
                mCRecyclerView.refreshComplete();
            }
        }, 1500);

    }

    public class MyAdapter extends BaseRecylerAdapter {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_text, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            String text = (String) mData.get(position);
            viewHolder.tvText.setText(text);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvText;

            ViewHolder(View view) {
                super(view);
                tvText = view.findViewById(R.id.tv_text);
            }
        }
    }
}
