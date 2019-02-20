package com.coral.widget.view.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xss on 2019/2/20.
 */
public abstract class DynamicItemAdapter<T> {

    private List<T> mDatas;
    private ViewGroup mContainerView;

    private Context mContext;
    private LayoutInflater mInflater;

    private OnItemClickListener mOnItemClickListener;

    public DynamicItemAdapter(Context context) {
        mDatas = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public abstract DynamicViewHolder createViewHolder();

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }

    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setData(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSet();
    }

    public void addData(T data) {
        int length = mDatas.size();
        mDatas.add(data);
        bindViewData(length, data);
    }

    public void notifyDataSet() {
        if (mContainerView == null) {
            return;
        }
        int size = mDatas.size();
        mContainerView.removeAllViews();
        for (int i = 0; i < size; i++) {
            bindViewData(i, mDatas.get(i));
        }
    }

    public void bindViewContainer(ViewGroup container) {
        if (this.mContainerView == null) {
            this.mContainerView = container;
        }
    }

    private void bindViewData(final int pos, final T data) {
        if (data == null || mContainerView == null) {
            return;
        }
        DynamicViewHolder holder = createViewHolder();
        if (holder == null) {
            return;
        }
        View rootView = holder.createView(mContext, mInflater);
        if (rootView == null) {
            return;
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(data, pos);
                }
            }
        });

        mContainerView.addView(rootView);
        holder.bind(data, pos);
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T data, int position);
    }

}
