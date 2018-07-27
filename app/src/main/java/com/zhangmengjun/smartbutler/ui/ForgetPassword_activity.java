package com.zhangmengjun.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.entity.MyUser;
import com.zhangmengjun.smartbutler.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：ForgetPassword_activity
 * 创建者：WALLMUD
 * 创建时间：2018/7/27 18:59
 * 描述：TODO
 */
public class ForgetPassword_activity extends BaseActivity implements View.OnClickListener {

    private EditText old_pwd;
    private EditText new_pwd;
    private EditText new_pwd1;
    private Button btn_alter_pwd;
    private EditText user_email;
    private Button btn_find_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        initView();
    }

    private void initView() {
        user_email = findViewById(R.id.user_email);
        btn_find_pwd = findViewById(R.id.btn_find_pwd);
        btn_find_pwd.setOnClickListener(this);
        old_pwd = findViewById(R.id.old_pwd);
        new_pwd = findViewById(R.id.new_pwd);
        new_pwd1 = findViewById(R.id.new_pwd1);
        btn_alter_pwd = findViewById(R.id.btn_alter_pwd);
        btn_alter_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_pwd:
                //获取输入框邮箱
                final String email = user_email.getText().toString().trim();
                //判断邮箱是否为空
                if (!TextUtils.isEmpty(email)) {
                    //发送重置邮件
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(ForgetPassword_activity.this, "密码重置成功。请到" + email + "重置密码操作", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgetPassword_activity.this, "密码重置失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                                L.i("密码重置失败:" + e.toString());
                            }
                        }
                    });
                    break;
                } else {
                    Toast.makeText(this, "找回密码，邮箱不能为空！！！", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.btn_alter_pwd:
                //1.获取输入框的值
                String old_pass = old_pwd.getText().toString().trim();
                String new_pass = new_pwd.getText().toString().trim();
                String new_pass1 = new_pwd1.getText().toString().trim();
                //2.判断输入是否为空
                if (!TextUtils.isEmpty(old_pass) & !TextUtils.isEmpty(new_pass) & !TextUtils.isEmpty(new_pass1)) {
                    //3.判断两次输入的密码是否一致
                    if(new_pass.equals(new_pass1)){
                        //4.重置密码
                        MyUser.updateCurrentUserPassword(old_pass, new_pass, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(ForgetPassword_activity.this,"密码修改成功!!!",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ForgetPassword_activity.this,"密码修改失败"+e.toString(),Toast.LENGTH_SHORT).show();
                                    L.i("密码修改失败："+e.toString());
                                }
                            }
                        });
                    }else{
                        Toast.makeText(ForgetPassword_activity.this,"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgetPassword_activity.this,"输入框不能为空！！！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
