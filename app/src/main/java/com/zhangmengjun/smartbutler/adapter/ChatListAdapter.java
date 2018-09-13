package com.zhangmengjun.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.entity.ChatListData;
import com.zhangmengjun.smartbutler.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.adapter
 * 文件名：ChatListAdapter
 * 创建者：WALLMUD
 * 创建时间：2018/8/15 11:02
 * 描述：对话adapter
 */
public class ChatListAdapter extends BaseAdapter {

    //左边的type
    public static final int VALUE_LEFT_TEXT=0;
    //右边的type
    public static final int VALUE_RIGHT_TEXT=1;

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ChatListData> listData =new ArrayList<ChatListData>();

    public ChatListAdapter(Context context,List<ChatListData> listData){
        this.context= context;
        this.listData= listData;
        //获取系统布局服务
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeftText viewHolderLeftText =null;
        ViewHolderRightText viewHolderRightText =null;
        //获取当前要显示的type，根据这个type来区分数据的加载
        int type = getItemViewType(position);
        L.i("posion:"+position+"view:" +convertView);
        if(convertView == null){
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = new ViewHolderLeftText();
                    convertView = layoutInflater.inflate(R.layout.left_item,null);
                    viewHolderLeftText.tv_left_text= convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeftText);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = new ViewHolderRightText();
                    convertView = layoutInflater.inflate(R.layout.right_item,null);
                    viewHolderRightText.tv_right_text= convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRightText);
                    break;
            }
        }else{
            switch(type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText = (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText = (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }

        //赋值
        ChatListData data = listData.get(position);
        switch (type){
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRightText.tv_right_text.setText(data.getText());
                break;
        }

        return convertView;
    }

    //根据数据源的position实现返回的item
    @Override
    public int getItemViewType(int position) {
        ChatListData data =listData.get(position);
        return data.getType();
    }

    //返回所有的layout的数量
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //左边的文本
    class ViewHolderLeftText{
        private TextView tv_left_text;

    }
    //右边的文本
    class ViewHolderRightText{
        private TextView tv_right_text;
    }
}
