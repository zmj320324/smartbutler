package com.zhangmengjun.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.ui
 * 文件名：AboutActivity
 * 创建者：WALLMUD
 * 创建时间：2018/9/12 17:15
 * 描述：TODO
 */
public class AboutActivity extends BaseActivity{

    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String>arrayAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //去除阴影
        getSupportActionBar().setElevation(0);
        initView();
    }

    private void initView() {
        mListView = findViewById(R.id.mListView);

        mList.add("应用名:"+getString(R.string.app_name));
        mList.add("版本号:"+ UtilTools.getVersion(this));
        mList.add("官网：www.zhangmengjun.com");

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(arrayAdapter);
    }


}
