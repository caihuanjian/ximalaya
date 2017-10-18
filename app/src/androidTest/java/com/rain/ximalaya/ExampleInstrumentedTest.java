package com.rain.ximalaya;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.rain.ximalaya.model.Param;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.rain.ximalaya", appContext.getPackageName());
    }


    @Test
    public void testAll() {
        Param param = new Param();
        param.setSearchKey("爱情");
        param.setPageCount(200);
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.SEARCH_KEY, param.getSearchKey());
        map.put(DTransferConstants.CATEGORY_ID, String.valueOf(param.getCategoryId()));
        map.put(DTransferConstants.PAGE, String.valueOf(param.getPage()));
        CommonRequest.getSearchedAlbums(map, new IDataCallBack<SearchAlbumList>() {
            @Override
            public void onSuccess(SearchAlbumList searchAlbumList) {
                System.out.print(searchAlbumList.getAlbums().size());
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }
}
