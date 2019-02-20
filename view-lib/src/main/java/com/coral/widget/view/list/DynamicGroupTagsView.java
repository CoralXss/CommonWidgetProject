package com.coral.widget.view.list;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.flexbox.FlexboxLayout;

/**
 * Created by xss on 2019/2/20.
 * https://github.com/google/flexbox-layout
 */
public class DynamicGroupTagsView extends FlexboxLayout {

    private DynamicItemAdapter mItemAdapter;

    public DynamicGroupTagsView(Context context) {
        super(context);
    }

    public DynamicGroupTagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicGroupTagsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(DynamicItemAdapter adapter) {
        this.mItemAdapter = adapter;
        this.mItemAdapter.bindViewContainer(this);
        this.mItemAdapter.notifyDataSet();
    }
}
