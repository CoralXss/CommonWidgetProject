package com.coral.widget.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by xss on 2019/2/20.
 */
public abstract class DynamicViewHolder<T> {

    protected abstract View createView(Context context, LayoutInflater inflater);

    protected abstract void bind(T data, int position);
}
