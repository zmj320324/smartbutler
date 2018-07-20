package com.zhangmengjun.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhangmengjun.smartbutler.R;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.fragment
 * 文件名：GirlFragment
 * 创建者：WALLMUD
 * 创建时间：2018/7/19 16:47
 * 描述：TODO
 */
public class GirlFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl,null);
        return view;
    }
}
