package com.coral.widget.view.list;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by xss on 2019/2/20.
 */
public class DynamicAddListView extends LinearLayout {

    private DynamicItemAdapter mItemAdapter;

    public DynamicAddListView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DynamicAddListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DynamicAddListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DynamicAddListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
    }

    public void setAdapter(DynamicItemAdapter adapter) {
        this.mItemAdapter = adapter;
        this.mItemAdapter.bindViewContainer(this);
        this.mItemAdapter.notifyDataSet();
    }
}
