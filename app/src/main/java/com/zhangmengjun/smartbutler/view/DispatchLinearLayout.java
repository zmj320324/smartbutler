package com.zhangmengjun.smartbutler.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.view
 * 文件名：DispatchLinearLayout
 * 创建者：WALLMUD
 * 创建时间：2018/9/4 11:56
 * 描述：事件分发
 */
public class DispatchLinearLayout extends LinearLayout {

    private DispatchKeyEventListener dispatchKeyEventListener;

    public DispatchLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DispatchLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DispatchLinearLayout(Context context) {
        super(context);
    }

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return dispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener dispatchKeyEventListener) {
        this.dispatchKeyEventListener = dispatchKeyEventListener;
    }

    //接口
    public static interface DispatchKeyEventListener{
       boolean dispatchKeyEvent(KeyEvent event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果不为空，调用了去获取事件
        if(dispatchKeyEventListener !=null){
            return dispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
