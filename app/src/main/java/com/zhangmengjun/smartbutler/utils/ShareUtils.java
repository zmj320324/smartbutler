package com.zhangmengjun.smartbutler.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.utils
 * 文件名：ShareUtils
 * 创建者：WALLMUD
 * 创建时间：2018/7/20 19:56
 * 描述：SharedPreferences 封装
 */
public class ShareUtils {

    public static final String NAME="config";

    public static void putString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context,String key,String defvalue){
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getString(key,defvalue);
    }

    public static void putInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }

    public static int getInt(Context context,String key,int defvalue){
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,defvalue);
    }

    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context,String key,boolean defvalue){
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defvalue);
    }

    public static void deleShare(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    public static void clearShare(Context context){
        SharedPreferences sp =context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }


}
