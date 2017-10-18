package com.rain.ximalaya.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.rain.ximalaya.R;
import com.rain.ximalaya.fragments.base.BaseFragment;
import com.rain.ximalaya.model.Param;
import com.rain.ximalaya.views.Page;
import com.rain.ximalaya.views.PageAlbum;
import com.rain.ximalaya.views.PageRadio;
import com.rain.ximalaya.views.PageTrack;
import com.rain.ximalaya.views.interfaces.FocusUpCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HwanJ.Choi on 2017-10-16.
 */

public class SearchFragment extends BaseFragment implements View.OnClickListener, FocusUpCallback {

    private View mRootView;
    private EditText mSearchInput;
    private Button mSearchBtn;
    private ViewPager mViewpager;
    private TabLayout mTabLayout;

    private SearchPageAdapter mAdapter;
    private List<Page> contentViews = new ArrayList<>();

    private Param mCurrentParm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchInput = (EditText) mRootView.findViewById(R.id.et_search);
        mSearchBtn = (Button) mRootView.findViewById(R.id.btn_search);
        mViewpager = (ViewPager) mRootView.findViewById(R.id.viewpager_search);
        mTabLayout = (TabLayout) mRootView.findViewById(R.id.tablayout_search);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SearchPageAdapter();
        mViewpager.setAdapter(mAdapter);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < contentViews.size(); i++) {
                    Page page = contentViews.get(i);
                    if (i == position) {
                        page.onUserVisiable(true);
                    } else {
                        page.onUserVisiable(false);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
        mSearchBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        final String key = String.valueOf(mSearchInput.getText());
        mCurrentParm = new Param();
        mCurrentParm.setCategoryId(0);
        mCurrentParm.setSearchKey(key);
        mCurrentParm.setPage(1);
        for (Page page : contentViews) {
            page.prepareParam(mCurrentParm);
        }
        contentViews.get(mViewpager.getCurrentItem()).forceLoad();
    }

    @Override
    public void focusUp() {
        LinearLayout tabContainer = (LinearLayout) mTabLayout.getChildAt(0);
        final View view = tabContainer.getChildAt(mViewpager.getCurrentItem());
        if (view != null) {
            view.requestFocus();
        }
    }

    private class SearchPageAdapter extends PagerAdapter {

        private ViewFratory mViewFratory = new ViewFratory(getContext());

        private final String[] TITLES = new String[]{
                "专辑", "声音", "主播"};

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Page target;
            if (position < contentViews.size()) {
                target = contentViews.get(position);

            } else {
                target = mViewFratory.createView(position);
                contentViews.add(position, target);
                target.setFocusUpListener(SearchFragment.this);
            }
            container.addView(target);
            return target;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(contentViews.get(position));
        }
    }

    private class ViewFratory {

        private LayoutInflater mInflater;

        ViewFratory(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        public Page createView(int positon) {
            Page result;
            switch (positon) {
                case 0:
                    result = new PageAlbum(getContext());
                    break;
                case 1:
                    result = new PageTrack(getContext());
                    break;
                case 2:
                    result = new PageRadio(getContext());
                    break;
                default:
                    throw new IndexOutOfBoundsException("IndexOutOfBoundsException");
            }
            return result;
        }
    }

    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
