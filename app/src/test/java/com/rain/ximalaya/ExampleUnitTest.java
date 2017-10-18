package com.rain.ximalaya;

import com.rain.ximalaya.model.Param;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.SearchAlbumList;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
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