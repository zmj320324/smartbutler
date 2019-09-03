package com.zhangmengjun.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.StaticClass;
import com.zhangmengjun.smartbutler.view.DispatchLinearLayout;


/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.service
 * 文件名：SmsService
 * 创建者：WALLMUD
 * 创建时间：2018/8/22 20:14
 * 描述：短信监听服务
 */
public class SmsService extends Service implements View.OnClickListener {

    //声明短信广播
    private SmsReceiver smsReceiver;
    //发件人号码
    private String smsPhone;
    //短信内容
    private String smsContent;
    //窗口管理器
    private WindowManager windowManager;
    //布局参数
    private WindowManager.LayoutParams layoutParams;
    //View
    private DispatchLinearLayout view;
    private TextView tv_phone;
    private TextView tv_content;
    private Button btn_send_sms;


    private HomeWatchReceiver homeWatchReceiver;
    public static final String SYSTEM_DIALOGS_RESON_KEY="reason";
    public static final String SYSTEM_DIALOGS_HOME_KEY="homekey";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        L.i("Build.VERSION.SDK_INT:"+ Build.VERSION.SDK_INT);
        L.i("init service");
        //动态注册广播
        smsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //添加action
        intentFilter.addAction(StaticClass.SMS_ACTION);
        //设置权重
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver,intentFilter);

        homeWatchReceiver = new HomeWatchReceiver();
        IntentFilter intentFilter1 =new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeWatchReceiver,intentFilter1);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");
        //注销
        unregisterReceiver(smsReceiver);
        unregisterReceiver(homeWatchReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send_sms:
                sendSms();
                //消失窗口
                if(view.getParent() != null){
                    windowManager.removeView(view);
                }
                break;
        }
    }

    //回复短信
    private void sendSms() {
        Uri uri = Uri.parse("smsto:"+smsPhone);
        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
        //设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body","");
        startActivity(intent);
    }



    //监听home键的广播
    class HomeWatchReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason =intent.getStringExtra(SYSTEM_DIALOGS_RESON_KEY);
                if(SYSTEM_DIALOGS_HOME_KEY.equals(reason)){
                    L.i("点击了home键！！");
                    if(view.getParent()!=null){
                        windowManager.removeView(view);
                    }
                }
            }

        }
    }

    ////短信广播
    public class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(StaticClass.SMS_ACTION.equals(action)){
                L.i("来短信了！！！！");
                //获取短信内容返回的是一个Object数组
                Object[] objs = (Object[]) intent.getExtras().get("pdus");
                //遍历数组得到相关数据
                for(Object obj:objs){
                    //数组元素转化成短信对象
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                    //发件人
                    smsPhone = sms.getOriginatingAddress();
                    //内容
                    smsContent=sms.getMessageBody();
                    L.i("短信内容："+smsPhone+":"+smsContent);
                    showWindow();
                }
            }

        }
    }

    //窗口提示
    private void showWindow() {
        //获取窗口服务
        windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //获取布局参数
        layoutParams = new WindowManager.LayoutParams();
        //定义宽高
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //定义标记
        layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //定义格式
        layoutParams.format = PixelFormat.TRANSLUCENT;
        //定义类型
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        //加载布局
        view = (DispatchLinearLayout) View.inflate(getApplicationContext(), R.layout.sms_item,null);

        tv_phone = view.findViewById(R.id.tv_phone);
        tv_content = view.findViewById(R.id.tv_content);
        btn_send_sms =view.findViewById(R.id.btn_send_sms);
        btn_send_sms.setOnClickListener(this);

        tv_phone.setText("发件人:"+smsPhone);
        tv_content.setText(smsContent);

        //添加View到窗口
        windowManager.addView(view,layoutParams);
        view.setDispatchKeyEventListener(dispatchKeyEventListener);

    }

    private DispatchLinearLayout.DispatchKeyEventListener dispatchKeyEventListener = new DispatchLinearLayout.DispatchKeyEventListener() {
        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            //判断是否按返回键
            if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                L.i("我按了back键!");
                if(view.getParent()!=null){
                    windowManager.removeView(view);
                }
                return true;
            }
            return false;
        }
    };
}
