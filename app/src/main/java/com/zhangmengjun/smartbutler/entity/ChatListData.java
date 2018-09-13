package com.zhangmengjun.smartbutler.entity;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.entity
 * 文件名：ChatListData
 * 创建者：WALLMUD
 * 创建时间：2018/8/15 11:04
 * 描述：对话列表的实体
 */
public class ChatListData {
    //type
    private int type;
    //文本
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
