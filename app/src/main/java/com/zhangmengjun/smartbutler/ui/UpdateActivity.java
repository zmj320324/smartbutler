package com.zhangmengjun.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.print.PrinterId;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.utils.L;

import java.io.File;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：UpdateActivity
 * 创建者：WALLMUD
 * 创建时间：2018/9/5 17:40
 * 描述：TODO
 */
public class UpdateActivity extends BaseActivity{

    //正在下载
    public static final int HANDLER_LODING =1001;
    //下载完成
    public static final int HANDLER_OK=1002;
    //下载失败
    public static final  int HANDLER_ON=1003;

    private TextView tv_size;
    private String url;
    private String path;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_LODING:
                    //实时更新进度
                    Bundle bundle = msg.getData();
                    long transferredBytes =bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    tv_size.setText(transferredBytes+" / " +totalSize);
                    break;
                case HANDLER_OK:
                    tv_size.setText("下载成功.");
                    //启动这个应用安装
                    startIntallApk();
                    break;
                case HANDLER_ON:
                    tv_size.setText("下载失败");
                    break;
            }
        }
    };

    //启动安装
    private void startIntallApk() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initView();
    }

    private void initView() {
        tv_size= findViewById(R.id.tv_size);
        path = FileUtils.getSDCardPath()+"/"+System.currentTimeMillis()+".apk";
        //下载
        url =getIntent().getStringExtra("url");
        if(!TextUtils.isEmpty(url)){
            //下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
                    L.i("transferredBytes:" + transferredBytes +".totalsize" + totalSize );
                    Message msg = new Message();
                    msg.what = HANDLER_LODING;
                    Bundle bundle = new Bundle();
                    bundle.putLong("transferredBytes",transferredBytes);
                    bundle.putLong("totalSize",totalSize);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            },new HttpCallback(){
                @Override
                public void onSuccess(String t) {
                    L.i("成功");
                    handler.sendEmptyMessage(HANDLER_OK);
                }

                @Override
                public void onFailure(VolleyError error) {
                    L.i("失败"+error.toString());
                    handler.sendEmptyMessage(HANDLER_ON);
                }
            });
        }
    }
}
