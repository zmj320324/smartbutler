package com.zhangmengjun.smartbutler.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;
import com.zhangmengjun.smartbutler.utils.StaticClass;

import cn.bmob.v3.Bmob;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.application
 * 文件名：BaseApplication
 * 创建者：WALLMUD
 * 创建时间：2018/7/19 14:27
 * 描述：Application
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化腾讯bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //初始化Bmob
        Bmob.initialize(this,StaticClass.BMOB_APP_ID);
        // 初始化创建语音配置对象
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+StaticClass.VOICE_KEY);
        //在使用百度地图SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
    }
}
