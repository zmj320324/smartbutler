package com.zhangmengjun.smartbutler.entity;

import cn.bmob.v3.BmobUser;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.entity
 * 文件名：MyUser
 * 创建者：WALLMUD
 * 创建时间：2018/7/27 11:55
 * 描述：用户类
 */
public class MyUser extends BmobUser {
    private Integer age;
    private Boolean sex;
    private String desc;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
