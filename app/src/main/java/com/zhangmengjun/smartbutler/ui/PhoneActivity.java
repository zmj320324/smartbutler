package com.zhangmengjun.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：PhoneActivity
 * 创建者：WALLMUD
 * 创建时间：2018/8/14 18:57
 * 描述：归属地查询
 */
public class PhoneActivity extends BaseActivity implements View.OnClickListener {
    //输入框
    private EditText et_number;
    //公司logo
    private ImageView iv_company;
    //结果
    private TextView tv_result;
    //按钮
    private Button btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_del,btn_query;
    //标记位
    private boolean flag=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        initView();
    }

    private void initView() {
        et_number = findViewById(R.id.et_number);
        iv_company= findViewById(R.id.iv_company);
        tv_result = findViewById(R.id.tv_result);

        btn_0 = findViewById(R.id.btn_0);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_del = findViewById(R.id.btn_del);
        btn_query = findViewById(R.id.btn_query);
        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_query.setOnClickListener(this);

        //长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        //获取输入框的内容
        String str = et_number.getText().toString();
        switch (v.getId()){
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if(flag){
                    str="";
                    flag=false;
                    et_number.setText("");
                }

                //每次结尾添加一
                et_number.setText(str + ((Button)v).getText());
                //移动光标
                et_number.setSelection(str.length() + 1 );
                break;
            case R.id.btn_del:
                if(!TextUtils.isEmpty(str) && str.length()>0){
                    //每次结尾减去一
                    et_number.setText(str.substring(0,str.length()-1));
                    et_number.setSelection(str.length()-1);
                }
                break;
            case R.id.btn_query:
                getPhone(str);
                break;
        }

    }

    //获取归属地
    private void getPhone(String str) {
        String url="http://apis.juhe.cn/mobile/get?phone="+str+"&key="+ StaticClass.PHONE_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                Toast.makeText(PhoneActivity.this,"结果:" + t, Toast.LENGTH_SHORT).show();
                L.i("phone:"+t);
                parsingJson(t);

            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject json_data = new JSONObject(t);
            JSONObject json_result = json_data.getJSONObject("result");

            String province = json_result.getString("province");
            String city = json_result.getString("city");
            String areacode = json_result.getString("areacode");
            String zip = json_result.getString("zip");
            String company = json_result.getString("company");
            String card = json_result.getString("card");

            tv_result.setText("归属地:" + province + city + "\n"
                    + "区号:" + areacode  + "\n"
                    + "邮编:" + zip + "\n"
                    + "运营商:" + company  + "\n"
                    + "类型:" + card );

            //图片显示
            switch (company){
                case "移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }

            flag =true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
