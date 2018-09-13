package com.zhangmengjun.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.entity.GirlData;
import com.zhangmengjun.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.adapter
 * 文件名：GridAdapter
 * 创建者：WALLMUD
 * 创建时间：2018/8/21 10:40
 * 描述：TODO
 */
public class GirlAdapter extends BaseAdapter {

    private Context context;
    private List<GirlData> mList;
    private LayoutInflater layoutInflater;
    private GirlData girlData;
    private WindowManager windowManager;
    //屏幕宽
    private int width;

    public GirlAdapter(Context context, List<GirlData> mList){
        this.context=context;
        this.mList=mList;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width=windowManager.getDefaultDisplay().getWidth();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.girl_item,null);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        girlData = mList.get(position);
        String url = girlData.getImageurl();
        PicassoUtils.loadImageViewSize(context,url,width/2,500,viewHolder.imageView);
        return convertView;
    }


    class ViewHolder{
        private ImageView imageView;
    }
}
