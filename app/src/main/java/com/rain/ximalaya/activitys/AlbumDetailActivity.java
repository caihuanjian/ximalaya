package com.rain.ximalaya.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.VerticalGridView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rain.ximalaya.R;
import com.rain.ximalaya.adapters.BaseAdapter;
import com.rain.ximalaya.adapters.TrackAdapter;
import com.rain.ximalaya.api.PlayService;
import com.rain.ximalaya.utils.Constant;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by HwanJ.Choi on 2017-10-13.
 */

public class AlbumDetailActivity extends BaseActivity {

    private VerticalGridView mGridView;
    private TrackAdapter mAdapter;

    private ImageView mPoster;


    @Override
    protected boolean allowDisplayHome() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        final Album album = getIntent().getExtras().getParcelable(Constant.KEY_ALBUM_PARCELABLE);
        initView(album);
        loadTracks(album.getId());
        setListener();
    }

    private void setListener() {

        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Track>() {
            @Override
            public void onItemClick(View view, Track track, int position) {
                PlayService.getInstance(AlbumDetailActivity.this).playTrack(track);
            }
        });
    }

    private void initView(Album album) {
        mGridView = (VerticalGridView) findViewById(R.id.vg_album_detail);
        mPoster = (ImageView) findViewById(R.id.iv_album_poster);
        mGridView.setNumColumns(3);
        mAdapter = new TrackAdapter(AlbumDetailActivity.this);
        mGridView.setAdapter(mAdapter);

        Glide.with(this).load(album.getCoverUrlLarge())
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10, 0)))
                .into(mPoster);
    }

    private void loadTracks(long albumId) {
        Map<String, String> trackParam = new HashMap<>();
        trackParam.put(DTransferConstants.ALBUM_ID, String.valueOf(albumId));
        trackParam.put(DTransferConstants.SORT, "asc");
        trackParam.put(DTransferConstants.PAGE, String.valueOf(1));
        CommonRequest.getTracks(trackParam, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                mAdapter.updateData(trackList.getTracks());
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void startSelf(Context context, Album album) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY_ALBUM_PARCELABLE, album);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
