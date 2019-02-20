package com.coral.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coral.widget.view.list.DynamicAddListView;
import com.coral.widget.view.list.DynamicItemAdapter;
import com.coral.widget.view.list.DynamicViewHolder;
import com.coral.widget.view.list.TagsView;

import java.util.ArrayList;
import java.util.List;

public class DynamicListViewDemoActivity extends AppCompatActivity {

    private List<String> datas = new ArrayList<>();

    private DynamicAddListView dynamicAddListView;

    private TagsView tagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_list_view_demo);

        initData();
        initView();
    }

    private void initData() {
        for (int i = 0 ; i < 5; i++) {
            datas.add("ITEM " + (i + 1));
        }
    }

    private void initView() {
        dynamicAddListView = findViewById(R.id.dynamicAddListView);
        MyDynamicItemAdapter adapter = new MyDynamicItemAdapter(this);
        adapter.setData(datas);
        dynamicAddListView.setAdapter(adapter);

        adapter.setOnItemClickListener(new DynamicItemAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(String data, int position) {
                Toast.makeText(DynamicListViewDemoActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

        tagsView = findViewById(R.id.tagsView);
        tagsView.setDatas(datas);

    }

    class MyDynamicItemAdapter extends DynamicItemAdapter<String> {

        public MyDynamicItemAdapter(Context context) {
            super(context);
        }

        @Override
        public DynamicViewHolder createViewHolder() {
            return new MyDynamicViewHolder();
        }
    }

    class MyDynamicViewHolder extends DynamicViewHolder<String> {

        private TextView textView;

        @Override
        protected View createView(Context context, LayoutInflater inflater) {
            textView = new TextView(context);
            return textView;
        }

        @Override
        protected void bind(String data, int position) {
            textView.setText(data);
        }
    }

}
