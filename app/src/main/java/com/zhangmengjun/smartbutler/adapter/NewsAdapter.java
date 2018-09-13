package com.zhangmengjun.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.entity.NewsData;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.adapter
 * 文件名：NewsAdapter
 * 创建者：WALLMUD
 * 创建时间：2018/8/15 19:42
 * 描述：新闻
 */
public class NewsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<NewsData> newsDataList;
    private Context context;
    private int width,height;
    private WindowManager windowManager;

    public NewsAdapter(Context context,List<NewsData> newsDataList){
        this.context=context;
        this.newsDataList=newsDataList;
        layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width=windowManager.getDefaultDisplay().getWidth();
        height=windowManager.getDefaultDisplay().getHeight();
        L.i("width:"+width+".height:"+height);
    }

    @Override
    public int getCount() {
        return newsDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.news_item,null);
            viewHolder.iv_image = convertView.findViewById(R.id.iv_image);
            viewHolder.tv_source = convertView.findViewById(R.id.tv_source);
            viewHolder.tv_title = convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        NewsData data = newsDataList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());
        //加载图片
//        Picasso.with(context).load(data.getImgUrl()).into(viewHolder.iv_image);
        PicassoUtils.loadImageViewSize(context,data.getImgUrl(),width/3,height/8,viewHolder.iv_image);

        return convertView;
    }

    class ViewHolder{
        private ImageView iv_image;
        private TextView tv_source;
        private TextView tv_title;
    }
}
