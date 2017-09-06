package com.mercury.xrecyclerview;

import android.util.Log;

/**
 * Created by wang.zhonghao on 2017/9/6.
 */

public class LogUtil {

    protected static String TAG = "Mercury";

    protected static boolean DEBUG = true;

    public static void logI(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }

    }

    public static void logI(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }

    }
}
