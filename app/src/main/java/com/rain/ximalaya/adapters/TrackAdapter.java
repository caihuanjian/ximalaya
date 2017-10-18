package com.rain.ximalaya.adapters;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rain.ximalaya.R;
import com.rain.ximalaya.utils.CommonUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class TrackAdapter extends BaseAdapter<Track> {

    public TrackAdapter(Context c) {
        super(c);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_track;
    }

    @Override
    public void convert(VH viewHolder, Track track, int posistion) {
        viewHolder.setText(R.id.tv_track_title, track.getTrackTitle());
        viewHolder.setText(R.id.tv_play_count, CommonUtil.formatPlayCount(track.getPlayCount()));
        if (track.getCommentCount() > 0) {
            viewHolder.setText(R.id.tv_comment_count, track.getCommentCount() + "");
        }

        MultiTransformation multiTransformation = new MultiTransformation(
                new CircleCrop(),
                new ColorFilterTransformation(mContext.getResources().getColor(R.color.mask_color)));
        Glide.with(mContext).load(track.getCoverUrlMiddle())
                .apply(RequestOptions.bitmapTransform(multiTransformation))
                .apply(RequestOptions.placeholderOf(R.drawable.default_holder))
                .into((ImageView) viewHolder.getView(R.id.iv_album_poster));
    }
}
