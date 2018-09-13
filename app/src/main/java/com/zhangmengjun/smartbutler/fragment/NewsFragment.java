package com.zhangmengjun.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.adapter.NewsAdapter;
import com.zhangmengjun.smartbutler.entity.NewsData;
import com.zhangmengjun.smartbutler.ui.WebViewActivity;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.fragment
 * 文件名：NewsFragment
 * 创建者：WALLMUD
 * 创建时间：2018/7/19 16:58
 * 描述：TODO
 */
public class NewsFragment extends Fragment {
    private ListView mListView;
    private List<NewsData> newsDataList = new ArrayList<NewsData>();

    //存储标题
    private List<String> mListTitle = new ArrayList<>();
    //存储地址
    private List<String> mListUrl = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListView = view.findViewById(R.id.mListView);
        String url="http://v.juhe.cn/weixin/query?key="+ StaticClass.NEWS_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                L.i("json:"+t);
                parsejson(t);
            }
        });

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.i("position:"+position);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title",mListTitle.get(position));
                bundle.putString("url",mListUrl.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void parsejson(String t) {
        try {
            JSONObject json_data= new JSONObject(t);
            JSONObject json_result = json_data.getJSONObject("result");
            JSONArray json_array = json_result.getJSONArray("list");
            for(int i=0;i<json_array.length();i++){
                JSONObject data = (JSONObject) json_array.get(i);
                String title =data.getString("title");
                String url = data.getString("url");
                String source=data.getString("source");
                String imgurl=data.getString("firstImg");
                if(TextUtils.isEmpty(imgurl)){
                    imgurl="https://cdn.wkzf.com/wkweb_fe/img/source/index/1.jpg";
                }

                mListTitle.add(title);
                mListUrl.add(url);

                NewsData newsData = new NewsData();
                newsData.setTitle(title);
                newsData.setSource(source);
                newsData.setImgUrl(imgurl);
                newsDataList.add(newsData);
            }

            NewsAdapter adapter = new NewsAdapter(getActivity(),newsDataList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
