package com.zhangmengjun.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.utils.ShareUtils;
import com.zhangmengjun.smartbutler.utils.StaticClass;
import com.zhangmengjun.smartbutler.utils.UtilTools;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：SplashActivity
 * 创建者：WALLMUD
 * 创建时间：2018/7/23 15:11
 * 描述：首页闪屏
 */
public class SplashActivity extends AppCompatActivity {

    private TextView tv_splash;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否是第一次运行
                    if(isFirst()){
                        startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();
    }

    private void initView(){
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,2000);
        tv_splash = findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFont(this,tv_splash);

    }

    //判断程序是否是第一次运行
    private boolean isFirst(){
        boolean isFirst= ShareUtils.getBoolean(this,StaticClass.SHARE_IS_FIRST,true);
        if(isFirst){
            ShareUtils.putBoolean(this,StaticClass.SHARE_IS_FIRST,false);
            return true;
        }else{
            return false;
        }
    }

    //禁止返回键
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
