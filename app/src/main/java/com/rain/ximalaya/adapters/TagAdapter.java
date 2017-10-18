package com.rain.ximalaya.adapters;

import android.content.Context;

import com.rain.ximalaya.R;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class TagAdapter extends BaseAdapter<Tag> {
    public TagAdapter(Context c) {
        super(c);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_tag;
    }

    @Override
    public void convert(VH viewHolder, Tag tag, int posistion) {
        viewHolder.setText(R.id.tv_tag_title, tag.getTagName());
        if (posistion == 0) {
            viewHolder.itemView.requestFocus();
        }
    }
}
