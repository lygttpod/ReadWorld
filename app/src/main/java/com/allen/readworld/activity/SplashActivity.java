package com.allen.readworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.allen.readworld.R;
import com.allen.readworld.application.MyAppcaltion;
import com.allen.readworld.bean.TopListBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 15/9/23.
 */
public class SplashActivity extends Activity {
    List<TopListBean> topListBeans;
    String urlString = "http://c.3g.163.com/nc/topicset/android/subscribe/manage/listspecial.html";

    MyAppcaltion myAppcaltion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myAppcaltion = (MyAppcaltion)getApplicationContext();
        topListBeans = new ArrayList<>();
        sendRequest();
    }

    private void sendRequest (){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                handlejson(bytes);
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this,HomeActivity1.class);
                startActivity(intent);
                this.onFinish();

            }

            @Override
            public void onFinish() {
                super.onFinish();

            }
        });
    }

    private void handlejson(byte[] bytes){
        String jsonStr = new String(bytes);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("tList");
            for (int b = 0; b < jsonArray.length()-8; b++) {
                JSONObject json = jsonArray.getJSONObject(b);
                TopListBean topListBean = new TopListBean(json.getString("tname"),json.getString("tid"));
                topListBeans.add(topListBean);
            }
            myAppcaltion.setTopListBeans(topListBeans);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
