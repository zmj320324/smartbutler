package com.zhangmengjun.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.entity.MyUser;
import com.zhangmengjun.smartbutler.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：Registered_activity
 * 创建者：WALLMUD
 * 创建时间：2018/7/26 20:04
 * 描述：注册
 */
public class Registered_activity extends BaseActivity implements View.OnClickListener {

    private EditText user_name;
    private EditText user_age;
    private EditText user_desc;
    private RadioGroup user_sex;
    private EditText user_password;
    private EditText user_password1;
    private EditText user_email;
    private Button btn_registered;
    //性别
    private Boolean isGender = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();
    }

    private void initView() {
        user_name = findViewById(R.id.user_name);
        user_age = findViewById(R.id.user_age);
        user_desc = findViewById(R.id.user_desc);
        user_sex = findViewById(R.id.user_sex);
        user_password = findViewById(R.id.user_password);
        user_password1 = findViewById(R.id.user_password1);
        user_email = findViewById(R.id.user_email);
        btn_registered = findViewById(R.id.btn_registered);

        btn_registered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registered:
                //点击获取到输入框的值
                String name = user_name.getText().toString().trim();
                String age = user_age.getText().toString().trim();
                String desc = user_desc.getText().toString().trim();
                String password = user_password.getText().toString().trim();
                String password1 = user_password1.getText().toString().trim();
                String email = user_email.getText().toString().trim();

                //判断是否为空
                if (!TextUtils.isEmpty(name)
                        & !TextUtils.isEmpty(age)
                        & !TextUtils.isEmpty(password)
                        & !TextUtils.isEmpty(password1)
                        & !TextUtils.isEmpty(email)) {
                    //判断两次输入密码是否一致
                    if (password.equals(password1)) {
                        //判断性别
                        user_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.man) {
                                    isGender = true;
                                } else if (checkedId == R.id.woman) {
                                    isGender = false;
                                }
                            }
                        });

                        //判断简介是否为空
                        if (TextUtils.isEmpty(desc)) {
                            desc = "这个人很懒什么都没有留下！！！";
                        }

                        //注册
                        MyUser myUser = new MyUser();
                        myUser.setUsername(name);
                        myUser.setAge(Integer.parseInt(age));
                        myUser.setDesc(desc);
                        myUser.setSex(isGender);
                        myUser.setEmail(email);
                        myUser.setPassword(password);
                        //邮箱验证已经收费
                        myUser.setEmailVerified(true);
                        myUser.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if(e==null){
                                    Toast.makeText(Registered_activity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(Registered_activity.this,"注册失败！！！"+e.toString(),Toast.LENGTH_SHORT).show();
                                    L.i("注册失败："+e.toString());
                                }
                            }
                        });

                    }else{
                        Toast.makeText(this, "两次输入的密码不一致！！！", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "输入框不能为空！！！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
