package com.coral.widget;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout ll_content;

    private List<Class> pageClassList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        pageClassList = new ArrayList<>();
        pageClassList.add(SelectRangeViewDemoActivity.class);
        pageClassList.add(FixedRecyclerViewDemoActivity.class);
        pageClassList.add(DynamicListViewDemoActivity.class);

    }

    private void initView() {
        ll_content = findViewById(R.id.ll_content);
        ll_content.removeAllViews();
        int count = pageClassList.size();
        for (int i = 0; i < count; i++) {
            TextView tv = new TextView(this);
            tv.setPadding(20, 20, 0, 20);
            tv.setTextColor(Color.WHITE);
            tv.setText(pageClassList.get(i).getSimpleName());
            tv.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 15, 0, 15);
            ll_content.addView(tv, lp);

            final int pos = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, pageClassList.get(pos));
                    startActivity(intent);
                }
            });
        }
    }

}
