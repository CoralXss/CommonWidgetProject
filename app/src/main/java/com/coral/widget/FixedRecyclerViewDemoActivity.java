package com.coral.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coral.widget.view.recyclerview.FixedRecyclerView;
import com.coral.widget.view.recyclerview.NonScrollLinearLayoutManager;
import com.coral.widget.view.recyclerview.base.BaseRecyclerAdapter;
import com.coral.widget.view.recyclerview.base.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * ScrollView 嵌套 RecyclerView，解决滑动冲突
 * 方式一：FixedRecyclerView + NonScrollLinearLayoutManager
 */
public class FixedRecyclerViewDemoActivity extends AppCompatActivity {

    private FixedRecyclerView fixedRecyclerView;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_recycler_view_demo);

        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 36; i++) {
            datas.add("ITEMS " + i);
        }
    }

    private void initView() {
        fixedRecyclerView = findViewById(R.id.fixedRecyclerView);
        LinearLayoutManager linearLayoutManager = new NonScrollLinearLayoutManager(this);
        fixedRecyclerView.setLayoutManager(linearLayoutManager);

        MyAdapter adapter = new MyAdapter();
        adapter.setDatas(datas);
        fixedRecyclerView.setAdapter(adapter);
    }

    class MyAdapter extends BaseRecyclerAdapter<String, MyViewHolder> {

        @Override
        public MyViewHolder createBaseViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setTextSize(24f);
            return new MyViewHolder(textView);
        }
    }

    class MyViewHolder extends BaseRecyclerViewHolder<String> {
        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }

        @Override
        public void bindView(String data, int position) {
            tv.setText(data);
        }
    }
}
