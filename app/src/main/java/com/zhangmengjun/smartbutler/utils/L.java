package com.zhangmengjun.smartbutler.utils;

import android.util.Log;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.utils
 * 文件名：L
 * 创建者：WALLMUD
 * 创建时间：2018/7/20 15:26
 * 描述：TODO
 */
public class L {
    public static final boolean DEBUG = true;
    public static final String TAG = "Smartbutler";

    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void i(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void w(String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }

}
