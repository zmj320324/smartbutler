package com.zhangmengjun.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.adapter.CourierAdapter;
import com.zhangmengjun.smartbutler.entity.CourierData;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：CourierActivity
 * 创建者：WALLMUD
 * 创建时间：2018/8/7 15:20
 * 描述：TODO
 */
public class CourierActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_number;
    private Button btn_get_courier;
    private ListView mListView;

    private List<CourierData> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        btn_get_courier = findViewById(R.id.btn_get_courier);
        btn_get_courier.setOnClickListener(this);
        mListView = findViewById(R.id.mListView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_courier:
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();
                //拼接url
                String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.COURIER_KEY +
                        "&com=" + name + "&no=" + number;
                L.i("url" + url);
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                    String t = "{\"resultcode\":\"200\",\"reason\":\"查询成功\",\"result\":{\"company\":\"顺丰\",\"com\":\"sf\",\"no\":\"153554424049\",\"status\":\"1\",\"list\":[{\"datetime\":\"2018-08-13 16:06:41\",\"remark\":\"顺丰速运 已收取快件\",\"zone\":\"\"},{\"datetime\":\"2018-08-13 18:21:34\",\"remark\":\"顺丰速运 已收取快件\",\"zone\":\"\"},{\"datetime\":\"2018-08-13 20:54:15\",\"remark\":\"快件在【上海浦东临港营业点】已装车,准备发往 【上海浦江集散中心】\",\"zone\":\"\"},{\"datetime\":\"2018-08-13 21:08:43\",\"remark\":\"快件已发车\",\"zone\":\"\"},{\"datetime\":\"2018-08-13 22:13:47\",\"remark\":\"快件到达 【上海浦江集散中心】\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 03:08:52\",\"remark\":\"快件在【上海浦江集散中心】已装车,准备发往 【上海虹桥集散中心2】\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 03:19:13\",\"remark\":\"快件已发车\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 04:13:03\",\"remark\":\"快件到达 【上海虹桥集散中心2】\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 04:44:21\",\"remark\":\"快件在【上海虹桥集散中心2】已装车,准备发往 【上海徐汇新华营业点】\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 05:48:00\",\"remark\":\"快件已发车\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 06:18:16\",\"remark\":\"快件到达 【上海徐汇新华营业点】\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 07:17:24\",\"remark\":\"正在派送途中,请您准备签收(派件人:赵飞,电话:15935890824)\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 08:06:30\",\"remark\":\"快件交给赵飞，正在派送途中（联系电话：15935890824）\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 11:35:09\",\"remark\":\"快件交给邱玉华，正在派送途中（联系电话：13657999484）\",\"zone\":\"\"},{\"datetime\":\"2018-08-14 11:35:49\",\"remark\":\"已签收(前台 ),感谢使用顺丰,期待再次为您服务\",\"zone\":\"\"}]},\"error_code\":0}";
                    parsingJson(t);
//                    RxVolley.get(url, new HttpCallback() {
//                        @Override
//                        public void onSuccess(String t) {
//                            Toast.makeText(CourierActivity.this,t,Toast.LENGTH_SHORT).show();
//                            L.i("json"+t);
//                            //解析json
//                            parsingJson(t);
//                        }
//                    });
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //解析数据
    private void parsingJson(String t) {

        try {
            JSONObject json_data = new JSONObject(t);
            JSONObject json_result = json_data.getJSONObject("result");
            JSONArray json_array = json_result.getJSONArray("list");
            for (int i = 0; i < json_array.length(); i++) {
                JSONObject json = (JSONObject) json_array.get(i);

                CourierData courierData = new CourierData();
                courierData.setRemark(json.getString("remark"));
                courierData.setZone(json.getString("zone"));
                courierData.setDatetime(json.getString("datetime"));
                mList.add(courierData);
            }
            //倒序处理
            Collections.reverse(mList);
            CourierAdapter adapter = new CourierAdapter(this, mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
