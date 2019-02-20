package com.coral.widget.view.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

/**
 * Created by xss on 2019/2/20.
 * https://github.com/google/flexbox-layout
 */
public class TagsView extends FlexboxLayout {

    private TagAdapter tagAdapter;

    public TagsView(Context context) {
        super(context);
        init(context);
    }

    public TagsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TagsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setFlexDirection(FlexDirection.ROW);
    }

    public void setDatas(List<String> datas) {
        tagAdapter = new TagAdapter(getContext());
        tagAdapter.setData(datas);
        tagAdapter.bindViewContainer(this);
        tagAdapter.notifyDataSet();
    }

    class TagAdapter extends DynamicItemAdapter<String> {

        public TagAdapter(Context context) {
            super(context);
        }

        @Override
        public DynamicViewHolder createViewHolder() {
            return new TagViewHolder();
        }
    }

    class TagViewHolder extends DynamicViewHolder<String> {

        private TextView textView;

        @Override
        protected View createView(Context context, LayoutInflater inflater) {
            textView = new TextView(context);
            FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setOrder(-1);
            lp.setFlexGrow(2);
            textView.setLayoutParams(lp);
            return textView;
        }

        @Override
        protected void bind(String data, int position) {
            if (data != null) {
                textView.setText(data);
            }
        }
    }
}
