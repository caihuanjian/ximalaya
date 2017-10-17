package com.rain.ximalaya.factory;

import android.support.v4.app.Fragment;

import com.rain.ximalaya.fragments.AlbumFragment;
import com.rain.ximalaya.fragments.HomeFragment;
import com.rain.ximalaya.fragments.PayFragment;
import com.rain.ximalaya.fragments.SearchFragment;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class FragmentFactory {

    public static FragmentFactory getInstance() {
        return SingleTon.FACTORY;
    }

    public final static class SingleTon {
        final static FragmentFactory FACTORY = new FragmentFactory();
    }

    public Fragment createFragment(int position) {
        Fragment result = null;
        switch (position) {
            case 0:
                result = HomeFragment.newInstance();
                break;
            case 1:
                result = SearchFragment.newInstance();
                break;
            case 2:
                result = PayFragment.newInstance();
                break;
        }
        return result;
    }

    public Fragment creatAlbumFragment(long categoryId, String tagName) {
        return AlbumFragment.newInstance(categoryId, tagName);
    }
}
