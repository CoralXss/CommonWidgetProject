package com.coral.widget.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by xss on 2018/1/9.
 */

public class NonScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnable;

    public NonScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public NonScrollLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NonScrollLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NonScrollLinearLayoutManager(Context context, boolean isScroll) {
        super(context);
        this.isScrollEnable = isScroll;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollEnable && super.canScrollVertically();
    }

    /**
     * 设置 RecyclerView 是否可以垂直滑动
     * @param isEnable
     */
    public void setScrollEnable(boolean isEnable) {
        this.isScrollEnable = isEnable;
    }
}
