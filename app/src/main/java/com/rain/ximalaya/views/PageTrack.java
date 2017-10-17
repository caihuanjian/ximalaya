package com.rain.ximalaya.views;

import android.content.Context;
import android.support.v17.leanback.widget.VerticalGridView;

import com.rain.ximalaya.R;
import com.rain.ximalaya.adapters.TrackAdapter;
import com.rain.ximalaya.model.Param;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.track.SearchTrackList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HwanJ.Choi on 2017-10-16.
 */

public class PageTrack extends Page<SearchTrackList> {

    private VerticalGridView mRecyclerView;
    private TrackAdapter mAdapter;

    public PageTrack(Context context) {
        super(context);
    }

    @Override
    public void init() {
        inflate(getContext(), R.layout.view_track, this);
        mRecyclerView = (VerticalGridView) findViewById(R.id.rv_search_track);
        mAdapter = new TrackAdapter(getContext());
        mRecyclerView.setNumColumns(3);
        mRecyclerView.setAdapter(mAdapter);
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
                updateView(searchTrackList);
                isSearching = false;
            }

            @Override
            public void onError(int i, String s) {
                isSearching = false;
            }
        });
    }

    @Override
    public void updateView(SearchTrackList data) {
        mAdapter.updateData(data.getTracks());
    }

    @Override
    public void onUserVisiable(boolean visiable) {
        super.onUserVisiable(visiable);
        if (visiable) {
            loadData(false);
        }
    }
}
