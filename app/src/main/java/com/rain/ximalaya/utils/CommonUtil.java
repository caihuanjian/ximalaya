package com.rain.ximalaya.utils;

import java.util.Locale;

/**
 * Created by HwanJ.Choi on 2017-10-13.
 */

public class CommonUtil {

    private CommonUtil() {
    }

    public static String formatPlayCount(long playCount) {
        final double yi = playCount * 1.0f / (1000 * 1000 * 1000);
        final double wan = playCount * 1.0f / 10000;
        if (yi > 1) {
            return String.format(Locale.CHINA, "%.1f", yi) + "亿";
        } else if (wan > 1) {
            return String.format(Locale.CHINA, "%.1f", wan) + "万";
        } else {
            return playCount + "";
        }

    }
}
