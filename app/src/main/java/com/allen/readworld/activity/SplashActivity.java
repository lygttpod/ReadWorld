package com.allen.readworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.allen.readworld.R;
import com.allen.readworld.application.MyAppcaltion;

import com.allen.readworld.db.greenrobot.gen.ChannelItem;
import com.allen.readworld.utils.GreenDaoUtils;
import com.allen.readworld.utils.LogUtil;
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
    GreenDaoUtils greenDaoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        myAppcaltion = (MyAppcaltion)getApplicationContext();
       // topListBeans = new ArrayList<>();
        greenDaoUtils = myAppcaltion.getGreenDaoUtils();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                userChannelList = new ArrayList<ChannelItem>();
                otherChannelList = new ArrayList<ChannelItem>();

                if (greenDaoUtils.getChannelItems(1).size()<=0){
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
        StringBuilder stringBuilder = new StringBuilder();
        try {
            jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("tList");
            for (int b = 0; b < jsonArray.length(); b++) {
                JSONObject json = jsonArray.getJSONObject(b);

                stringBuilder.append("tname="+json.getString("tname")+"tid="+json.getString("tid")+"\n");

                int isSelect = 0;
                if (json.get("tname").equals("头条")
                        || json.get("tname").equals("娱乐")
                        || json.get("tname").equals("科技")
                        || json.get("tname").equals("手机")) {
                    isSelect = 1;
                    ChannelItem channelItem = new ChannelItem(
                            json.getString("tname"), json.getString("tid"), b,
                            isSelect);
                    userChannelList.add(channelItem);
                } else {
                    ChannelItem channelItem = new ChannelItem(
                            json.getString("tname"), json.getString("tid"), b+100,
                            isSelect);
                    otherChannelList.add(channelItem);
                }

            }
            LogUtil.d("data", stringBuilder.toString());
            greenDaoUtils.saveUserChannel(userChannelList);
            greenDaoUtils.saveOtherChannel(otherChannelList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
