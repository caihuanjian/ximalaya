package com.rain.ximalaya.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HwanJ.Choi on 2017-5-17.
 */

public abstract class BaseAdatper<T> extends RecyclerView.Adapter<BaseAdatper.VH> implements DataOperator<T> {


    private List<T> mDatas = new ArrayList<>();
    protected Context mContext;

    public BaseAdatper(Context c) {
        mContext = c;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void updateData(List<T> list) {
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void appendData(List<T> list) {
        int startPos = mDatas.size();
        mDatas.addAll(list);
        notifyItemRangeChanged(startPos, list.size());
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.get(parent, getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        convert(holder, mDatas.get(position), position);
        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, mDatas.get(position), position);
                }
            }
        });
    }

    public abstract int getLayoutId(int viewType);

    public abstract void convert(VH viewHolder, T t, int posistion);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class VH extends RecyclerView.ViewHolder {

        private SparseArray<View> mViews;
        private View mItemView;

        public VH(View itemView) {
            super(itemView);
            mItemView = itemView;
            mViews = new SparseArray<>();
        }

        public static VH get(ViewGroup parent, int layoutID) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layoutID, parent, false);
            return new VH(view);
        }


        public <T extends View> T getView(int id) {
            View view = mViews.get(id);
            if (view == null) {
                view = mItemView.findViewById(id);
                mViews.put(id, view);
            }
            return (T) view;
        }

        public void setText(int id, CharSequence text) {
            TextView tv = getView(id);
            tv.setText(text);
        }

    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T t, int position);
    }
}
