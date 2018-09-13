package com.zhangmengjun.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.adapter.GirlAdapter;
import com.zhangmengjun.smartbutler.entity.GirlData;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.PicassoUtils;
import com.zhangmengjun.smartbutler.utils.StaticClass;
import com.zhangmengjun.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.fragment
 * 文件名：GirlFragment
 * 创建者：WALLMUD
 * 创建时间：2018/7/19 16:47
 * 描述：TODO
 */
public class GirlFragment extends Fragment {

    private GridView mGridView;
    private List<GirlData> mList= new ArrayList<GirlData>();
    private GirlAdapter adapter;
    //提示框
    private CustomDialog customDialog;
    //预览图片
    private ImageView iv_img;
    //图片地址的数据
    private List<String> mListUrl = new ArrayList<String>();
    //photoview
    private PhotoViewAttacher photoViewAttacher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView = view.findViewById(R.id.mGridView);

        customDialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT
                ,R.layout.dialog_girl,R.style.pop_anim_style, Gravity.CENTER);
        iv_img= customDialog.findViewById(R.id.iv_img);

        RxVolley.get(StaticClass.GIRL_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                L.i("zmj:"+t);
                parseJson(t);
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                PicassoUtils.loadImageView(getActivity(),mListUrl.get(position),iv_img);
                //缩放
                photoViewAttacher = new PhotoViewAttacher(iv_img);
                photoViewAttacher.update();

                customDialog.show();

            }
        });

    }

    private void parseJson(String t) {
        try {
            JSONObject json_data = new JSONObject(t);
            JSONArray json_array = json_data.getJSONArray("data");
            for(int i = 0 ;i<json_array.length();i++){
                JSONObject data = (JSONObject) json_array.get(i);
                if (data.has("image_url")) {
                    String url = data.getString("image_url");
                    L.i("xfy:" + url);
                    mListUrl.add(url);
                    GirlData girlData = new GirlData();
                    girlData.setImageurl(url);
                    mList.add(girlData);
                }
            }
            adapter=new GirlAdapter(getContext(),mList);
            mGridView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
