package com.rain.ximalaya.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rain.ximalaya.R;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import static com.rain.ximalaya.utils.CommonUtil.formatPlayCount;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class AlbumAdapter extends BaseAdapter<Album> {


    public AlbumAdapter(Context c) {
        super(c);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_album;
    }

    @Override
    public void convert(VH viewHolder, Album album, int posistion) {
        viewHolder.setText(R.id.tv_title, album.getAlbumTitle());
        viewHolder.setText(R.id.tv_introduction, album.getAlbumIntro());
        viewHolder.setText(R.id.tv_track_num, mContext.getString(R.string.track_num, album.getIncludeTrackCount()));
        viewHolder.setText(R.id.tv_play_count, formatPlayCount(album.getPlayCount()));
        Glide.with(mContext).load(album.getCoverUrlLarge()).apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .apply(RequestOptions.placeholderOf(R.drawable.default_holder))
                .into((ImageView) viewHolder.getView(R.id.iv_album_poster));

    }


}
