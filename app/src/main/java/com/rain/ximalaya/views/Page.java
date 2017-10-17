package com.rain.ximalaya.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.rain.ximalaya.model.Param;

/**
 * Created by HwanJ.Choi on 2017-10-16.
 */

public abstract class Page<T> extends RelativeLayout {

    private boolean isUserVisiable;

    protected boolean isSearching;

    private Param mCurrentParam;

    protected Param mLastLoadParam;

    public Page(Context context) {
        this(context, null);
    }

    public Page(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Page(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public abstract void init();

    public abstract void loadData(boolean force);

    public void forceLoad() {
        loadData(true);
    }

    public abstract void updateView(T data);

    public void prepareParam(Param param) {
        mCurrentParam = param;
    }

    public Param getCurrentParam() {
        return mCurrentParam;
    }

    public void onUserVisiable(boolean visiable) {
        isUserVisiable = visiable;
    }

    public boolean isUserVisiable() {
        return isUserVisiable;
    }
}
