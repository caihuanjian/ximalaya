package com.rain.ximalaya.views;

import android.content.Context;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.rain.ximalaya.R;
import com.rain.ximalaya.activitys.AlbumDetailActivity;
import com.rain.ximalaya.adapters.AlbumAdapter;
import com.rain.ximalaya.adapters.BaseAdapter;
import com.rain.ximalaya.adapters.LoadMoreWrapper;
import com.rain.ximalaya.model.Param;
import com.rain.ximalaya.views.interfaces.FocusUpCallback;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HwanJ.Choi on 2017-10-16.
 */

public class PageAlbum extends Page<SearchAlbumList> {

    private VerticalGridView mRecyclerView;
    private final static int COLUMN = 3;

    private LoadMoreWrapper<Album> mLoadMoreWrapper;
    private Param mLastLoadParam;

    public PageAlbum(Context context) {
        super(context);
    }

    @Override
    public void init() {
        inflate(getContext(), R.layout.view_album, this);
        mRecyclerView = (VerticalGridView) findViewById(R.id.rv_search_album);
        AlbumAdapter albumAdapter = new AlbumAdapter(getContext());
        albumAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Album>() {
            @Override
            public void onItemClick(View view, Album album, int position) {
                AlbumDetailActivity.startSelf(getContext(), album);
            }
        });
        albumAdapter.setKeyListener(new BaseAdapter.OnItemKeyListener() {
            @Override
            public boolean onItemKey(View view, int keyCode, KeyEvent event, int position) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if (position < COLUMN) {
                        final FocusUpCallback focusUpListener = getFocusUpListener();
                        if (focusUpListener != null) {
                            focusUpListener.focusUp();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        mLoadMoreWrapper = new LoadMoreWrapper<>(albumAdapter, mRecyclerView);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("chj", "onLoadMore");
                loadMore();
            }
        });
        mRecyclerView.setNumColumns(COLUMN);
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    @Override
    public void loadData(boolean force) {
        if ((isSearching || mLastLoadParam == getCurrentParam()) && !force) {
            return;
        }
        isSearching = true;
        final Param param = getCurrentParam();
        mLastLoadParam = param;
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, param.getSearchKey());
        map.put(DTransferConstants.CATEGORY_ID, String.valueOf(param.getCategoryId()));
        map.put(DTransferConstants.PAGE, String.valueOf(param.getPage()));
        CommonRequest.getSearchedAlbums(map, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                updateView(searchAlbumList, false);
                isSearching = false;
            }

            @Override
            public void onError(int i, String s) {
                isSearching = false;
            }
        });
    }

    private void loadMore() {
        final Param currentParam = getCurrentParam();
        currentParam.setPage(currentParam.getPage() + 1);
        mLastLoadParam = currentParam;
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, currentParam.getSearchKey());
        map.put(DTransferConstants.CATEGORY_ID, String.valueOf(currentParam.getCategoryId()));
        map.put(DTransferConstants.PAGE, String.valueOf(currentParam.getPage()));
        CommonRequest.getSearchedAlbums(map, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                updateView(searchAlbumList, true);
                mLoadMoreWrapper.setLoading(false);
            }

            @Override
            public void onError(int i, String s) {
                mLoadMoreWrapper.setLoading(false);
            }
        });

    }

    @Override
    public void updateView(SearchAlbumList data, boolean append) {
        if (append) {
            mLoadMoreWrapper.appendData(data.getAlbums());
        } else {
            mLoadMoreWrapper.updateData(data.getAlbums());
        }
    }

    @Override
    public void onUserVisiable(boolean visiable) {
        super.onUserVisiable(visiable);
        if (visiable) {
            loadData(false);
        }
    }

}
