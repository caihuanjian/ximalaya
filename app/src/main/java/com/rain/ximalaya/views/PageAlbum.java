package com.rain.ximalaya.views;

import android.content.Context;
import android.support.v17.leanback.widget.VerticalGridView;
import android.view.KeyEvent;
import android.view.View;

import com.rain.ximalaya.R;
import com.rain.ximalaya.activitys.AlbumDetailActivity;
import com.rain.ximalaya.adapters.AlbumAdapter;
import com.rain.ximalaya.adapters.BaseAdatper;
import com.rain.ximalaya.model.Param;
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

    private AlbumAdapter mAdapter;
    private Param mLastLoadParam;

    public PageAlbum(Context context) {
        super(context);
    }

    @Override
    public void init() {
        inflate(getContext(), R.layout.view_album, this);
        mRecyclerView = (VerticalGridView) findViewById(R.id.rv_search_album);
        mAdapter = new AlbumAdapter(getContext());
        mRecyclerView.setNumColumns(COLUMN);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdatper.OnItemClickListener<Album>() {
            @Override
            public void onItemClick(View view, Album album, int position) {
                AlbumDetailActivity.startSelf(getContext(), album);
            }
        });
        mAdapter.setKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    final int indexOfChild = mRecyclerView.indexOfChild(v);
                    if (indexOfChild < COLUMN) {

                    }
                }
                return false;
            }
        });
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
        CommonRequest.getSearchedAlbums(map, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                updateView(searchAlbumList);
                isSearching = false;
            }

            @Override
            public void onError(int i, String s) {
                isSearching = false;
            }
        });
    }

    @Override
    public void updateView(SearchAlbumList data) {
        mAdapter.updateData(data.getAlbums());
    }

    @Override
    public void onUserVisiable(boolean visiable) {
        super.onUserVisiable(visiable);
        if (visiable) {
            loadData(false);
        }
    }

}
