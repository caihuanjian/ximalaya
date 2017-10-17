package com.rain.ximalaya;

import android.app.Application;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.httputil.Config;

/**
 * Created by HwanJ.Choi on 2017-10-11.
 */

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        initSDK();
//        initHttpConfig();
    }

    private void initSDK() {
        CommonRequest.getInstanse().init(this, BuildConfig.XIMALAYA_CLIENT_SECRET);
    }

    private void initHttpConfig() {
        Config config = new Config();
        config.connectionTimeOut = 10000;
        config.readTimeOut = 3000;
        CommonRequest.getInstanse().setHttpConfig(config);
    }

}
