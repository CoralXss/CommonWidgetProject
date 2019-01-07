package com.coral.widget;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xss on 2019/1/5.
 */
public class SelectRangeItemsView extends FrameLayout {

    private LinearLayout contentView;
    private VerticalRangeSlideSeekBar slideSeekBar;


    public SelectRangeItemsView(@NonNull Context context) {
        super(context);
        init();
    }

    public SelectRangeItemsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SelectRangeItemsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectRangeItemsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.base_layout_select_range_items_view, this, true);

        contentView = findViewById(R.id.contentView);
        slideSeekBar = findViewById(R.id.rangeSlideSeekBar);

        slideSeekBar.setOnRangeChangedListener(new VerticalRangeSlideSeekBar.OnRangeChangedListener() {
            @Override
            public void onRange(int lowIndex, int highIndex) {
                int size = contentView.getChildCount();
                for (int i = 0; i < size; i++) {
                    setItemSelected(contentView.getChildAt(i), i >= lowIndex && i <= highIndex);
                }
            }
        });
    }

    public void setItems(List<String> datas, int beginIndex, int endIndex) {
        int size = datas.size();

        slideSeekBar.setTickCount(size + 1);
        slideSeekBar.setSelectedLowIndex(beginIndex);
        slideSeekBar.setSelectedHighIndex(endIndex);

        contentView.removeAllViews();
        contentView.setGravity(Gravity.CENTER_VERTICAL);
        for (int i = 0; i < size; i++) {
            TextView tv = new TextView(getContext());
            if (i >= beginIndex && i < endIndex) {
                setItemSelected(tv, true);
            }
            tv.setText(datas.get(i));
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setPadding(20, 0, 0, 0);

            contentView.addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, slideSeekBar.getAvgLineLength()));
        }
    }

    private void setItemSelected(View view, boolean isSelected) {
        view.setBackgroundColor(isSelected ? Color.parseColor("#0D1989FA") : Color.WHITE);
    }
}
