package com.example.xrecyclerview;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xrecyclerview.adapter.BaseRecylerAdapter;
import com.example.xrecyclerview.bean.TestBean;
import com.mercury.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements XRecyclerView.LoadingListener {

    private static final String TAG = "Mercury";
    @BindView(R.id.rv)
    XRecyclerView  rv;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;

    MyAdapter            mMyAdapter;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mMyAdapter = new MyAdapter(this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addHeaderView(LayoutInflater.from(this).inflate(R.layout.header_layout, activityMain, false));

        rv.setAdapter(mMyAdapter);

        List<TestBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new TestBean(i));
        }

        mMyAdapter.setData(list);

        rv.setLoadingListener(this);


    }

    @Override
    public void onRefresh() {
//        rv.setNewFootview(new LoadingMoreFooter(this));
        if (rv.getHeaderView() == null) {
            rv.addHeaderView(LayoutInflater.from(this).inflate(R.layout
                    .header_layout, activityMain, false));
        }
        rv.setOriginalFooter();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<TestBean> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(new TestBean(i));
                }
                mMyAdapter.setData(list);
                rv.refreshComplete();

            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMyAdapter.getData().size() < 25) {
                    List<TestBean> list = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        list.add(new TestBean(i));
                    }
                    mMyAdapter.addData(list);
                    rv.loadMoreComplete();
                } else {
                    rv.loadMoreComplete();
                    rv.setNoMore(true);
//                    rv.removeHeaderView();
//                    View foot = LayoutInflater.from(MainActivity.this).inflate(com.mercury
//                            .xrecyclerview.R.layout
//                            .listview_footer, activityMain, false);
//                    rv.setNewFootview(foot);
//                    rv.getFooterView().setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Log.e(TAG, "成功了" );
//                        }
//                    });

                }


            }
        }, 1000);
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

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this,"haha",Toast.LENGTH_SHORT).show();
                }
            });
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_text)
            TextView tvText;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
