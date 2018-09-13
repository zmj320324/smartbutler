package com.zhangmengjun.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangmengjun.smartbutler.MainActivity;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.entity.MyUser;
import com.zhangmengjun.smartbutler.utils.ShareUtils;
import com.zhangmengjun.smartbutler.view.CustomDialog;


import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：LoginActivity
 * 创建者：WALLMUD
 * 创建时间：2018/7/26 18:57
 * 描述：TODO
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //注册按钮
    private Button btn_registered;
    private EditText user_name;
    private EditText user_password;
    private Button btn_login;
    private CheckBox keep_password;
    //忘记密码
    private TextView tv_forget;

    private CustomDialog customDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();
    }

    private void initview() {
        btn_registered = findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);

        user_name = findViewById(R.id.user_name);
        user_password = findViewById(R.id.user_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        keep_password = findViewById(R.id.keep_password);

        tv_forget = findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

//        customDialog = new CustomDialog(this,100,100,R.layout.dialog_lodding,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        customDialog = new CustomDialog(this,R.layout.dialog_lodding,R.style.Theme_dialog);
        //屏幕外点击无效
        customDialog.setCancelable(false);

        //设置选中的状态
        Boolean isCheck = ShareUtils.getBoolean(this,"keep_password",false);
        keep_password.setChecked(isCheck);
        if(isCheck){
            //设置密码
            user_name.setText(ShareUtils.getString(this,"name",""));
            user_password.setText(ShareUtils.getString(this,"password",""));
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPassword_activity.class
                ));
                break;
            case R.id.btn_registered:
                startActivity(new Intent(this, Registered_activity.class));
                break;
            case R.id.btn_login:
                //1.获取输入框的值
                String name = user_name.getText().toString().trim();
                String password = user_password.getText().toString().trim();
                //2.判断用户名和密码是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {
                    customDialog.show();
                    //登陆
                    MyUser myUser = new MyUser();
                    myUser.setUsername(name);
                    myUser.setPassword(password);
                    myUser.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            customDialog.dismiss();
                            if (e == null) {
                                //判断邮箱是否登陆
                                if(myUser.getEmailVerified()){
                                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(LoginActivity.this, "邮箱验证失败!!!请前往邮箱验证.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, "登陆失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                L.i("登陆失败：" + e.toString());
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空！！！", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    //假设我现在输入用户名和密码，但是我不点登陆，而是直接退出
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //保存状态
        ShareUtils.putBoolean(this,"keep_password",keep_password.isChecked());
        //是否记住密码
        if(keep_password.isChecked()){
            //记住用户名和密码
            ShareUtils.putString(this,"name",user_name.getText().toString().trim());
            ShareUtils.putString(this,"password",user_password.getText().toString().trim());
        }else{
            //删除用户名和密码
            ShareUtils.deleShare(this,"name");
            ShareUtils.deleShare(this,"password");
        }
    }
}
