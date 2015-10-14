package com.allen.readworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.allen.readworld.R;
import com.allen.readworld.application.MyAppcaltion;
import com.allen.readworld.bean.ChannelItem;
import com.allen.readworld.bean.ChannelManage;
import com.allen.readworld.bean.TopListBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Allen on 15/9/23.
 */
public class SplashActivity extends BaseActivity {
    //List<TopListBean> topListBeans;
    String urlString = "http://c.3g.163.com/nc/topicset/android/subscribe/manage/listspecial.html";

    MyAppcaltion myAppcaltion;
    private ProgressBar progressBar;

    List<ChannelItem> userChannelList;
    List<ChannelItem> otherChannelList;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        myAppcaltion = (MyAppcaltion)getApplicationContext();
       // topListBeans = new ArrayList<>();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                userChannelList = new ArrayList<ChannelItem>();
                otherChannelList = new ArrayList<ChannelItem>();
                if (ChannelManage.getManage(MyAppcaltion.getApp().getSQLHelper())
                        .getUserChannel().size() <= 0) {
                    sendRequest();
                }else {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this,HomeActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        },1000);

    }

    private void sendRequest (){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {


            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                handlejson(bytes);
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this,HomeActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();

            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(ProgressBar.GONE);

            }
        });
    }


    private void handlejson(byte[] bytes){
        String jsonStr = new String(bytes);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("tList");
            for (int b = 0; b < jsonArray.length(); b++) {
                JSONObject json = jsonArray.getJSONObject(b);

                int isSelect = 0;
                if (json.get("tname").equals("头条")
                        || json.get("tname").equals("娱乐")
                        || json.get("tname").equals("科技")
                        || json.get("tname").equals("手机")) {
                    isSelect = 1;
                    ChannelItem channelItem = new ChannelItem(b,
                            json.getString("tname"), json.getString("tid"), b,
                            isSelect);
                    userChannelList.add(channelItem);
                } else {
                    ChannelItem channelItem = new ChannelItem(b,
                            json.getString("tname"), json.getString("tid"), b,
                            isSelect);
                    otherChannelList.add(channelItem);
                }

//                TopListBean topListBean = new TopListBean(json.getString("tname"),json.getString("tid"));
//                topListBeans.add(topListBean);
            }
            ChannelManage.getManage(MyAppcaltion.getApp().getSQLHelper())
                    .saveOtherChannel(otherChannelList);
            ChannelManage.getManage(MyAppcaltion.getApp().getSQLHelper())
                    .saveUserChannel(userChannelList);
            //myAppcaltion.setTopListBeans(topListBeans);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
