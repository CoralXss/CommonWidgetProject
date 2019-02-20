package com.coral.widget.view.recyclerview.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xss on 2019/2/20.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindView(T data, int position);
}
