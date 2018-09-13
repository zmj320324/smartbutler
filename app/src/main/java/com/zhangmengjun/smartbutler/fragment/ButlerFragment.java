package com.zhangmengjun.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhangmengjun.smartbutler.R;
import com.zhangmengjun.smartbutler.adapter.ChatListAdapter;
import com.zhangmengjun.smartbutler.entity.ChatListData;
import com.zhangmengjun.smartbutler.utils.L;
import com.zhangmengjun.smartbutler.utils.ShareUtils;
import com.zhangmengjun.smartbutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：SmartButler
 * 包名：com.zhangmengjun.smartbutler.fragment
 * 文件名：WechatFragment
 * 创建者：WALLMUD
 * 创建时间：2018/7/19 16:44
 * 描述：TODO
 */
public class ButlerFragment extends Fragment implements View.OnClickListener {

    private ListView chatlistview;
    private Button btn_send;
    private EditText et_text;
    private ChatListAdapter adapter;

    private String result;

    private List<ChatListData> listData = new ArrayList<ChatListData>();

    //语音设置
    private SpeechSynthesizer mTts ;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        chatlistview = view.findViewById(R.id.chatlistview);
        btn_send= view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        et_text = view.findViewById(R.id.et_text);

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(getContext(),null);

        //设置适配器
        adapter = new ChatListAdapter(getActivity(),listData);
        chatlistview.setAdapter(adapter);

        addLeftItem("你好，我是小管家！");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_send:
                final String text = et_text.getText().toString().trim();
                if(!TextUtils.isEmpty(text) && text.length()<30){
                    et_text.setText("");
                    addRightItem(text);
                    String url="http://op.juhe.cn/robot/index?info=" + text + "&key=" + StaticClass.CHAT_KEY ;
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            L.i("智能json答案:"+t);
                            result = parsing(t);
                            addLeftItem(result);
                        }
                    });

                }else{
                    Toast.makeText(getActivity(),"输入内容不能空，或者超过30个字符",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private String parsing(String t) {
        try {
            JSONObject json_data= new JSONObject(t);
            JSONObject json_result = json_data.getJSONObject("result");
            String result = json_result.getString("text");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "抱歉能力有限，没有答案！！！";

    }

    //添加左边文本
    private void addLeftItem(String text) {
        boolean isSpeak = ShareUtils.getBoolean(getActivity(),"isSpeak",false);
        if(isSpeak){
            //开启语音合成
            startspeak(text);
        }

        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        listData.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        chatlistview.setSelection(chatlistview.getBottom());
    }
    //添加右边文本
    private void addRightItem(String text) {
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        listData.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        chatlistview.setSelection(chatlistview.getBottom());
    }


    private void startspeak(String text){
        mTts.startSpeaking( text, new SynthesizerListener(){
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {

            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }
}
