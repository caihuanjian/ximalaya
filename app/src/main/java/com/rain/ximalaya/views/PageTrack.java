package com.rain.ximalaya.views;

import android.content.Context;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.rain.ximalaya.R;
import com.rain.ximalaya.adapters.BaseAdapter;
import com.rain.ximalaya.adapters.LoadMoreWrapper;
import com.rain.ximalaya.adapters.TrackAdapter;
import com.rain.ximalaya.api.PlayService;
import com.rain.ximalaya.model.Param;
import com.rain.ximalaya.views.interfaces.FocusUpCallback;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.track.SearchTrackList;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HwanJ.Choi on 2017-10-16.
 */

public class PageTrack extends Page<SearchTrackList> {

    private static final int COLUMN = 3;
    private VerticalGridView mRecyclerView;
    private LoadMoreWrapper<Track> mLoadMoreAdapter;

    public PageTrack(Context context) {
        super(context);
    }

    @Override
    public void init() {
        inflate(getContext(), R.layout.view_track, this);
        mRecyclerView = (VerticalGridView) findViewById(R.id.rv_search_track);
        TrackAdapter trackAdapter = new TrackAdapter(getContext());

        trackAdapter.setKeyListener(new BaseAdapter.OnItemKeyListener() {
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

        trackAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Track>() {

            @Override
            public void onItemClick(View view, Track track, int position) {
                PlayService.getInstance(getContext()).playTrack(track);
            }
        });
        mLoadMoreAdapter = new LoadMoreWrapper<>(trackAdapter, mRecyclerView);
        mLoadMoreAdapter.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("chj", "on load more");
                loadMore();
            }
        });
        mRecyclerView.setNumColumns(COLUMN);
        mRecyclerView.setAdapter(mLoadMoreAdapter);
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
        map.put(DTransferConstants.PAGE, String.valueOf(1));
        CommonRequest.getSearchedTracks(map, new IDataCallBack<SearchTrackList>() {

            @Override
            public void onSuccess(SearchTrackList searchTrackList) {
                updateView(searchTrackList, false);
                isSearching = false;
            }

            @Override
            public void onError(int i, String s) {
                isSearching = false;
            }
        });
    }

    private void loadMore() {
        final Param param = getCurrentParam();
        param.setPage(param.getPage() + 1);
        mLastLoadParam = param;
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, param.getSearchKey());
        map.put(DTransferConstants.CATEGORY_ID, String.valueOf(param.getCategoryId()));
        map.put(DTransferConstants.PAGE, String.valueOf(param.getPage()));
        CommonRequest.getSearchedTracks(map, new IDataCallBack<SearchTrackList>() {

            @Override
            public void onSuccess(SearchTrackList searchTrackList) {
                updateView(searchTrackList, true);
                mLoadMoreAdapter.setLoading(false);
            }

            @Override
            public void onError(int i, String s) {
                mLoadMoreAdapter.setLoading(false);
                Log.d("chj", "on error" + s);
            }
        });
    }

    @Override
    public void updateView(SearchTrackList data, boolean append) {
        if (append) {
            mLoadMoreAdapter.appendData(data.getTracks());
        } else {
            mLoadMoreAdapter.updateData(data.getTracks());
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
