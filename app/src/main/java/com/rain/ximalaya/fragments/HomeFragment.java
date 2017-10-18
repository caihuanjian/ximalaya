package com.rain.ximalaya.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rain.ximalaya.CustomSubscribe;
import com.rain.ximalaya.R;
import com.rain.ximalaya.activitys.TagActivity;
import com.rain.ximalaya.adapters.BaseAdapter;
import com.rain.ximalaya.adapters.CategoryAdapter;
import com.rain.ximalaya.adapters.TagAdapter;
import com.rain.ximalaya.fragments.base.BaseFragment;
import com.rain.ximalaya.views.CustomPopWindow;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class HomeFragment extends BaseFragment {

    private static final int CLUMN = 6;

    private View mRootView;
    private CategoryAdapter mCategoryAdapter;
    private TagAdapter mTagAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_home);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), CLUMN));
        mCategoryAdapter = new CategoryAdapter(getContext());
        recyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Category>() {
            @Override
            public void onItemClick(View view, final Category category, int position) {
                CustomPopWindow customPopWindow = new CustomPopWindow.PopupWindowBuilder(getContext()).
                        setView(R.layout.view_tag_dialog).size(1000, 600).setFocusable(true).create();
                final View contentView = customPopWindow.getContentView();
                final RecyclerView rv = (RecyclerView) contentView.findViewById(R.id.rv_tag_dialog);
                rv.setLayoutManager(new GridLayoutManager(getContext(), CLUMN));
                mTagAdapter = new TagAdapter(getContext());
                mTagAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Tag>() {
                    @Override
                    public void onItemClick(View view, Tag tag, int position) {
                        Intent intent = new Intent(getContext(), TagActivity.class);
                        intent.putExtra(DTransferConstants.TAG_NAME, tag.getTagName());
                        intent.putExtra(DTransferConstants.CATEGORY_ID, category.getId());
                        intent.putExtra(DTransferConstants.CATEGORY_NAME, category.getCategoryName());

                        startActivity(intent);
                    }
                });
                rv.setAdapter(mTagAdapter);
                loadTagByCategoryId(category.getId());
                int[] location = new int[2];
                mRootView.getLocationOnScreen(location);
                customPopWindow.showAtLocation(mRootView, Gravity.TOP | Gravity.LEFT, (mRootView.getWidth() - 1000) / 2, (mRootView.getHeight() - 600 + location[1]) / 2);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadCategory();
    }

    private void loadTagByCategoryId(long id) {
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.CATEGORY_ID, String.valueOf(id));
        map.put(DTransferConstants.TYPE, String.valueOf(1));
        CommonRequest.getTags(map, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(TagList tagList) {
                mTagAdapter.updateData(tagList.getTagList());
                for (Tag tag : tagList.getTagList()) {
                    Log.d("chj", "标签名称：" + tag.getTagName());
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d("chj", "onerror" + s);
            }
        });
    }

    private void loadCategory() {
        final Observable<CategoryList> observable = Observable.create(new Observable.OnSubscribe<CategoryList>() {

            @Override
            public void call(final Subscriber<? super CategoryList> subscriber) {
                Map<String, String> params = new HashMap<>();
                CommonRequest.getCategories(params, new IDataCallBack<CategoryList>() {

                    @Override
                    public void onSuccess(CategoryList categoryList) {
                        subscriber.onNext(categoryList);
                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.d("chj", "code:" + code + ",error:" + message);
                        if (code == 604 ) {
                            loadCategory();
                        }
                    }
                });
            }
        });
        observable.subscribe(new CustomSubscribe<CategoryList>() {
            @Override
            public void onNext(CategoryList categoryList) {
                final List<Category> categories = categoryList.getCategories();
                mCategoryAdapter.updateData(categories);
                for (Category category : categories) {
                    Log.d("chj", "name:" + category.getCategoryName() + ",id:" + category.getId());
                }
            }
        });
    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
