package com.zhangmengjun.smartbutler.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.service.SmsService;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.ShareUtils;
import com.zhangmengjun.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;


import static android.Manifest.permission.RECEIVE_SMS;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：SettingActivity
 * 创建者：WALLMUD
 * 创建时间：2018/7/20 11:30
 * 描述：TODO
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    //语音播报
    private Switch sw_speak;
    //智能短信提醒
    private Switch sw_sms;
    //检测更新
    private LinearLayout ll_update;
    private TextView tv_version;

    private String versionName;
    private int versionCode;

    private String url;
    //扫一扫
    private LinearLayout ll_scan;
    //扫描结果
    private TextView tv_scan_result;
    //生成二维码
    private LinearLayout ll_qr_code;
    //我的位置
    private LinearLayout ll_my_location;
    //关于软件
    private LinearLayout ll_about;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        sw_speak = findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);

        Boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        sw_sms = findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);

        Boolean isSms = ShareUtils.getBoolean(this, "isSms", false);
        sw_sms.setChecked(isSms);

        ll_update = findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);
        tv_version = findViewById(R.id.tv_version);

        try {
            getVersionNameCode();
            tv_version.setText("检测版本:" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("检测版本");
//            e.printStackTrace();
        }

        ll_scan = findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        tv_scan_result=findViewById(R.id.tv_scan_result);

        ll_qr_code =findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);

        ll_my_location = findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);

        ll_about = findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // 接受短信权限回调
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 表示用户授权
                L.i("用户授予权限，开始监听短信！！！");
            } else {
                //用户拒绝权限
                L.i("用户拒绝授予权限，无法监听短信！！！");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_speak:
                //切换相反
                sw_speak.setSelected(!sw_speak.isSelected());
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;
            case R.id.sw_sms:
                //切换相反
                sw_sms.setSelected(!sw_sms.isSelected());
                ShareUtils.putBoolean(this, "isSms", sw_sms.isChecked());
                if (sw_sms.isChecked()) {
                    //申请读写权限
                    if (ContextCompat.checkSelfPermission(this, RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                        if (RECEIVE_SMS.equals(RECEIVE_SMS)) {
                            L.i("请求获取接收短信权限");
                            ActivityCompat.requestPermissions(this, new String[]{RECEIVE_SMS}, 1);
                        }
                    } else {
                        L.i("已获取接收短信权限");
                    }

                    startService(new Intent(this, SmsService.class));
                } else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;
            case R.id.ll_update:
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("json:" + t);
                        parsingJson(t);
                    }
                });
                break;
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(this,QrCodeActivity.class));
                break;
            case R.id.ll_my_location:
                startActivity(new Intent(this,LocationActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code =jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if(code>versionCode){
                showUpdateDialog(jsonObject.getString("content"));
            }else{
                Toast.makeText(this,"当前是最新版本",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("有新版本")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SettingActivity.this,UpdateActivity.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //什么也不做，也会执行dismiss方法
            }
        }).show();
    }

    //获取版本号和code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionCode = info.versionCode;
        versionName = info.versionName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            tv_scan_result.setText(scanResult);
        }
    }
}
