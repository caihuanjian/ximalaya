package com.rain.ximalaya.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rain.ximalaya.factory.FragmentFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */
public class FragmentsPagerAdapter extends FragmentPagerAdapter {


    private Map<Integer, Fragment> fragments = new HashMap<>();

    private static final String[] PAGE_TITLE = new String[]{"所有分类", "搜索", "付费"};

    public FragmentsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment != null)
            return fragment;
        fragments.put(position, FragmentFactory.getInstance().createFragment(position));
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return PAGE_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLE[position];
    }
}