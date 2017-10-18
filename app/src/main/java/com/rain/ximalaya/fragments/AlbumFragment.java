package com.rain.ximalaya.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rain.ximalaya.R;
import com.rain.ximalaya.activitys.AlbumDetailActivity;
import com.rain.ximalaya.adapters.AlbumAdapter;
import com.rain.ximalaya.adapters.BaseAdapter;
import com.rain.ximalaya.fragments.base.BaseFragment;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class AlbumFragment extends BaseFragment {

    private View mRootView;
    private AlbumAdapter mAlbumAdapter;

    private static final int CLUMN = 3;

    private static final String KEY_ARGS_ID = "key_args_category_id";
    private static final String KEY_ARGS_TAG = "key_args_tag_name";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_album, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_album);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), CLUMN));
        mAlbumAdapter = new AlbumAdapter(getContext());
        recyclerView.setAdapter(mAlbumAdapter);
        mAlbumAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Album>() {
            @Override
            public void onItemClick(View view, Album album, int position) {
                AlbumDetailActivity.startSelf(getContext(), album);
            }
        });
        final Bundle arguments = getArguments();
        final long categoryId = arguments.getLong(KEY_ARGS_ID);
        final String tagName = arguments.getString(KEY_ARGS_TAG);
        loadAlbum(categoryId, tagName);
    }

    private void loadAlbum(long categoryId, String tagName) {
        Map<String, String> albumParam = new HashMap<>();
        albumParam.put(DTransferConstants.CATEGORY_ID, String.valueOf(categoryId));
        albumParam.put(DTransferConstants.CALC_DIMENSION, "1");
        albumParam.put(DTransferConstants.TAG_NAME, tagName);
        CommonRequest.getAlbumList(albumParam, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(AlbumList albumList) {
                final List<Album> albums = albumList.getAlbums();
                mAlbumAdapter.updateData(albums);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    public static AlbumFragment newInstance(long categoryId, String tagName) {
        Bundle args = new Bundle();
        args.putLong(KEY_ARGS_ID, categoryId);
        args.putString(KEY_ARGS_TAG, tagName);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
