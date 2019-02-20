package com.coral.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectRangeViewDemoActivity extends AppCompatActivity {

    private DoubleSlideSeekBar mDoubleslideWithrule;
    private DoubleSlideSeekBar mDoubleslideWithoutrule;
    private TextView mTvMinRule;
    private TextView mTvMaxRule;
    private TextView mTvMinWithoutRule;
    private TextView mTvMaxWithoutRule;

    private SelectRangeItemsView rangeItemsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_range_demo);

        rangeItemsView = findViewById(R.id.selectRangeView);

        initView();
        setListener();

        initData();
    }

    private void initData() {
        List<String> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            if (i == 0) {
                days.add("当天");
            } else {
                days.add("第" + (i + 1) + "天");
            }
        }

        rangeItemsView.setItems(days, 0, 1);
    }

    private void setListener() {
        // 用法
        mDoubleslideWithrule.setOnRangeListener(new DoubleSlideSeekBar.onRangeListener() {
            @Override
            public void onRange(float low, float big) {
                mTvMinRule.setText("最小值" + String.format("%.0f" , low));
                mTvMaxRule.setText("最大值" + String.format("%.0f" , big));
            }
        });
        mDoubleslideWithoutrule.setOnRangeListener(new DoubleSlideSeekBar.onRangeListener() {
            @Override
            public void onRange(float low, float big) {
                mTvMinWithoutRule.setText("最小值" + String.format("%.0f" , low));
                mTvMaxWithoutRule.setText("最大值" + String.format("%.0f" , big));
            }
        });
    }

    private void initView() {
        mDoubleslideWithrule = (DoubleSlideSeekBar) findViewById(R.id.doubleslide_withrule);
        mDoubleslideWithoutrule = (DoubleSlideSeekBar) findViewById(R.id.doubleslide_withoutrule);
        mTvMinRule = (TextView) findViewById(R.id.tv_min_rule);
        mTvMaxRule = (TextView) findViewById(R.id.tv_max_rule);
        mTvMinWithoutRule = (TextView) findViewById(R.id.tv_min_without_rule);
        mTvMaxWithoutRule = (TextView) findViewById(R.id.tv_max_without_rule);
    }
}
