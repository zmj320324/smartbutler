package com.zhangmengjun.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zhangmengjun.smartbutler.R;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：QrCodeActivity
 * 创建者：WALLMUD
 * 创建时间：2018/9/10 20:06
 * 描述：生成二维码
 */
public class QrCodeActivity extends BaseActivity {

    private ImageView iv_qr_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        
        initView();
    }

    private void initView() {
        iv_qr_code = findViewById(R.id.iv_qr_code);
        //屏幕的宽
        int width = getResources().getDisplayMetrics().widthPixels;

        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("我是智能管家", width/2, width/2,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}
