package com.zhangmengjun.smartbutler.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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

    //保存图片到ShareUtils
    public static void putImageToShare(Context context, ImageView imageView){
        BitmapDrawable bitmapDrawable= (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //1.将bitmap压缩成字节数组输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);
        //2.利用Base64将我们的字节数组输出流转换成String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imgString = new String(Base64.encodeToString(byteArray,Base64.DEFAULT));
        //3.将String保存到shareUtils
        ShareUtils.putString(context,"image_title",imgString);
    }

    // 从ShareUtils获取图片
    public static void getImageFromShare(Context context,ImageView imageView){
        //1.拿到String
        String imgString = ShareUtils.getString(context,"image_title","");
        if(!TextUtils.isEmpty(imgString)){
            //2.利用Base64将我们的String转化
            byte[] byteArray = Base64.decode(imgString,Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            //3.生成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            imageView.setImageBitmap(bitmap);
        }
    }

    //获取版本号
    public static  String getVersion(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info =pm.getPackageInfo(context.getPackageName(),0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "未知版本";
        }
    }



}
