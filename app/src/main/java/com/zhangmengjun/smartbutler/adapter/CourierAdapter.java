package com.zhangmengjun.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.entity.CourierData;

import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.adapter
 * 文件名：CourierAdapter
 * 创建者：WALLMUD
 * 创建时间：2018/8/13 16:40
 * 描述：快递查询
 */
public class CourierAdapter extends BaseAdapter{

    private Context context;
    private List<CourierData> list;
    //布局加载器
    private LayoutInflater layoutInflater;
    private CourierData courierData;


    public CourierAdapter(Context context,List<CourierData> list){
        this.context =context;
        this.list = list;
        //布局加载器获取系统服务
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder= null;

        //view加载布局文件,获取控件
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_courier_item,null);
            viewHolder.tv_datetime = convertView.findViewById(R.id.tv_datetime);
            viewHolder.tv_zone = convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_remark = convertView.findViewById(R.id.tv_remark);
            //设置缓存
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置数据,修改控件效果
        courierData = list.get(position);
        viewHolder.tv_remark.setText(courierData.getRemark());
        viewHolder.tv_datetime.setText(courierData.getDatetime());
        viewHolder.tv_zone.setText(courierData.getZone());
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    class  ViewHolder{
        private TextView tv_datetime;
        private TextView tv_zone;
        private TextView tv_remark;

    }
}
