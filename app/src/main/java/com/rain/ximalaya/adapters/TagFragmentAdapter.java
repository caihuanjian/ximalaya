package com.rain.ximalaya.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rain.ximalaya.factory.FragmentFactory;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class TagFragmentAdapter<T> extends FragmentPagerAdapter {


    private final long mCategoryId;

    private Map<Integer, Fragment> fragments = new HashMap<>();

    private List<T> mDatas = new ArrayList<>();

    public void updateData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public TagFragmentAdapter(FragmentManager fm, long categoryId) {
        super(fm);
        mCategoryId = categoryId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null)
            return fragment;
        final Tag tag = getAlbumByPos(position);

        fragments.put(position, FragmentFactory.getInstance().creatAlbumFragment(mCategoryId, tag.getTagName()));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final Tag tag = getAlbumByPos(position);
        return tag.getTagName();
    }

    private <T extends Tag> T getAlbumByPos(int pos) {
        final T t = (T) mDatas.get(pos);
        return t;
    }
}
