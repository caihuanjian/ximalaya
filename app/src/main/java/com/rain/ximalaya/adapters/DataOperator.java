package com.rain.ximalaya.adapters;

import java.util.List;

/**
 * Created by HwanJ.Choi on 2017-5-18.
 */

public interface DataOperator<T> {

    void updateData(List<T> list);

    void appendData(List<T> list);
}
