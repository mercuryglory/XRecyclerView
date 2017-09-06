package com.example.xrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xrecyclerview.adapter.BaseRecylerAdapter;
import com.example.xrecyclerview.bean.TestBean;
import com.mercury.xrecyclerview.CRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wang.zhonghao on 2017/9/6.
 */

public class ChatListActivity extends AppCompatActivity {

    CRecyclerView mCRecyclerView;
    MyAdapter mMyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        mMyAdapter = new MyAdapter(this);
        mCRecyclerView = (CRecyclerView) findViewById(R.id.crecyclerview);
        mCRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCRecyclerView.setAdapter(mMyAdapter);

        List<TestBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new TestBean(i));
        }

        mMyAdapter.setData(list);
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
            TestBean bean = (TestBean) mData.get(position);
            viewHolder.tvText.setText(bean.getNumber() + "");
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_text)
            TextView tvText;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
