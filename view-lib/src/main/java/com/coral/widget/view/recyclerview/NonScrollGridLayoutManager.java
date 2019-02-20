package com.coral.widget.view.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by xss on 2018/1/9.
 */

public class NonScrollGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnable;

    public NonScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NonScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NonScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public NonScrollGridLayoutManager(Context context, int spanCount, boolean isScroll) {
        super(context, spanCount);
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
