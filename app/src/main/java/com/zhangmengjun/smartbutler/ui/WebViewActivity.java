package com.zhangmengjun.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.utils.L;


/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：WebViewActivity
 * 创建者：WALLMUD
 * 创建时间：2018/8/16 10:51
 * 描述：新闻详情
 */
public class WebViewActivity extends BaseActivity{

    //进度
    private ProgressBar mProgressBar;
    //网页
    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }

    private void initView() {
        mProgressBar= findViewById(R.id.mProgressBar);
        mWebView = findViewById(R.id.mWebView);

        Intent intent =getIntent();
        String title = intent.getStringExtra("title");
        final String url= intent.getStringExtra("url");
        L.i("url"+url);
        //设置标题
        getSupportActionBar().setTitle(title);

        //进行加载页面逻辑
        //支持js
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        //设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        //
        mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(url);

        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient(){
            // 重写此方法表明点击网页里面的链接还在当前的webview里跳转，不跳到浏览器那边
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    public class WebViewClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100){
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
