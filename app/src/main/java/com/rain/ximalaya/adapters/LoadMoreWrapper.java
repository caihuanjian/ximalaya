package com.rain.ximalaya.adapters;

import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by HwanJ.Choi on 2017-10-17.
 */
public class LoadMoreWrapper<T> extends RecyclerView.Adapter<BaseAdapter.VH> implements DataOperator<T> {

    private RecyclerView mRecyclerView;

    protected OnLoadMoreListener mLoadMoreListener;

    private BaseAdapter<T> mBaseAdapter;

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    protected boolean isLoading = false;

    public LoadMoreWrapper(BaseAdapter<T> adapter, RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mBaseAdapter = adapter;
        init();
    }

    private void init() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int itemCount;
                int lastItemPosition;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                    itemCount = layoutManager.getItemCount();
                    lastItemPosition = layoutManager.findLastVisibleItemPosition();
                    if (!isLoading && lastItemPosition + 1 >= itemCount && dy > 0) {
                        if (mLoadMoreListener != null) {
                            mLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }

                } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    itemCount = layoutManager.getItemCount();
                    lastItemPosition = layoutManager.findLastVisibleItemPosition();

                    if (!isLoading && lastItemPosition + 1 >= itemCount && dy > 0) {
                        if (mLoadMoreListener != null) {
                            mLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    itemCount = layoutManager.getItemCount();
                    int[] positon = null;
                    positon = layoutManager.findLastVisibleItemPositions(positon);

                    for (int i = 0; i < positon.length; i++) {
                        if (!isLoading && positon[i] + 1 >= itemCount && dy > 0) {
                            if (mLoadMoreListener != null) {
                                mLoadMoreListener.onLoadMore();
                            }
                            isLoading = true;
                            break;
                        }
                    }
                } else if (isV17GridLayoutManger(recyclerView)) {
                    final int selectedPosition = ((VerticalGridView) recyclerView).getSelectedPosition();
                    itemCount = recyclerView.getLayoutManager().getItemCount();
                    if (!isLoading && selectedPosition + 5 > itemCount && dy > 0) {
                        if (mLoadMoreListener != null) {
                            mLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public BaseAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return mBaseAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseAdapter.VH holder, int position) {
        mBaseAdapter.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemCount() {
        return mBaseAdapter.getItemCount();
    }

    @Override
    public void updateData(List<T> list) {
        mBaseAdapter.updateData(list);
        notifyDataSetChanged();
    }

    @Override
    public void appendData(List<T> list) {
        final int startPosition = mBaseAdapter.getItemCount();
        mBaseAdapter.appendData(list);
        notifyItemRangeChanged(startPosition, list.size());
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    private boolean isV17GridLayoutManger(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() == null)
            return false;
        return recyclerView.getLayoutManager().getClass().getCanonicalName().equals("android.support.v17.leanback.widget.GridLayoutManager");
    }
}