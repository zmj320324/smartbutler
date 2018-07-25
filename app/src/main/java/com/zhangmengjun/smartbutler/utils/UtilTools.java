package com.zhangmengjun.smartbutler.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.utils
 * 文件名：UtilTools
 * 创建者：WALLMUD
 * 创建时间：2018/7/19 15:18
 * 描述：工具统一类
 */
public class UtilTools {
    //设置字体
    public static void setFont(Context context, TextView textView){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/FONT.TTF");
        textView.setTypeface(typeface);

    }
}
