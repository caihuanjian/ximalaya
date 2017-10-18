package com.rain.ximalaya.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.rain.ximalaya.R;

/**
 * Created by HwanJ.Choi on 2017-10-16.
 */

public class PageRadio extends Page {

    private RecyclerView mRecyclerView;

    public PageRadio(Context context) {
        super(context);
    }

    @Override
    public void init() {
        inflate(getContext(), R.layout.view_radio, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_search_radio);
    }

    @Override
    public void loadData(boolean force) {

    }

    @Override
    public void updateView(Object data, boolean append) {

    }

}
