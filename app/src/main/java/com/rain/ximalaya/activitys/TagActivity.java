package com.rain.ximalaya.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.rain.ximalaya.R;
import com.rain.ximalaya.adapters.TagFragmentAdapter;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class TagActivity extends BaseActivity {


    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private TagFragmentAdapter<Tag> mAdapter;

    @Override
    protected boolean allowDisplayHome() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        final String tagName = getIntent().getStringExtra(DTransferConstants.TAG_NAME);
        String category = getIntent().getStringExtra(DTransferConstants.CATEGORY_NAME);
        final long categoryId = getIntent().getLongExtra(DTransferConstants.CATEGORY_ID, 0);
        setTitle(category);

        mViewPager = (ViewPager) findViewById(R.id.viewpager_activity_tag);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout_activity_tag);
        mAdapter = new TagFragmentAdapter<>(getSupportFragmentManager(), categoryId);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);

        loadTagByCategoryId(categoryId, tagName);
        setListner();
    }

    private void loadTagByCategoryId(long id, final String triggerTag) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.CATEGORY_ID, String.valueOf(id));
        map.put(DTransferConstants.TYPE, String.valueOf(1));
        CommonRequest.getTags(map, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(TagList tagList) {
                Log.d("chj", tagList.getTagList().toString());
                mAdapter.updateData(tagList.getTagList());
                mViewPager.setCurrentItem(getTriggerItem(triggerTag, tagList.getTagList()));
            }

            @Override
            public void onError(int i, String s) {
                Log.d("chj", "onerror" + s);
            }
        });
    }

    private int getTriggerItem(String tagName, List<Tag> tags) {
        for (int i = 0; i < tags.size(); i++) {
            if (tagName.equals(tags.get(i).getTagName()))
                return i;
        }
        return 0;
    }

    private void setListner() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                final CharSequence tagName = mAdapter.getPageTitle(position);
//                loadAlbumList(m);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
