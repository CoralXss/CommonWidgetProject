package com.coral.widget.view.recyclerview.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xss on 2019/2/20.
 */
public abstract class BaseRecyclerAdapter<T, H extends BaseRecyclerViewHolder> extends RecyclerView.Adapter<H> {

    private List<T> datas = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addData(T data) {
        datas.add(data);
        notifyDataSetChanged();
    }

    public abstract H createBaseViewHolder(@NonNull ViewGroup parent, int viewType);

    @NonNull
    @Override
    public H onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull H holder, final int position) {
        final T data = datas.get(position);
        if (holder != null && data != null) {
            holder.bindView(data, position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(data, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T data, int pos);
    }
}
