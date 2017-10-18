package com.rain.ximalaya.activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.rain.ximalaya.BuildConfig;
import com.rain.ximalaya.R;
import com.rain.ximalaya.adapters.FragmentsPagerAdapter;
import com.rain.ximalaya.api.PlayService;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;

public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSDK();

        setTitle(R.string.page_home);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_main);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout_main);
        mViewPager.setAdapter(new FragmentsPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected boolean allowDisplayHome() {
        return false;
    }

    private void initSDK() {
        CommonRequest.getInstanse().init(this, BuildConfig.XIMALAYA_CLIENT_SECRET);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayService.getInstance(this).release();
    }
}
