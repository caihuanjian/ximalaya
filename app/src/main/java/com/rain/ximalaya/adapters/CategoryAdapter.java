package com.rain.ximalaya.adapters;

import android.content.Context;

import com.rain.ximalaya.R;
import com.ximalaya.ting.android.opensdk.model.category.Category;

/**
 * Created by HwanJ.Choi on 2017-10-12.
 */

public class CategoryAdapter extends BaseAdatper<Category> {

    public CategoryAdapter(Context c) {
        super(c);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_category;
    }

    @Override
    public void convert(VH viewHolder, Category category, int posistion) {
        viewHolder.setText(R.id.tv_category_title, category.getCategoryName());
    }
}
