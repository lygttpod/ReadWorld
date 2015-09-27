package com.allen.readworld.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allen.readworld.R;
import com.allen.readworld.bean.ImgBean;
import com.allen.readworld.bean.NewsDetailBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Allen on 15/9/20.
 */
public class NewsDetailActivity extends Activity {
    String docid = null;
    String img = null;
    NewsDetailBean newsDetailBean;
    TextView title;
    TextView ptime;
    WebView webView;
    ImageView imageView;
    List<ImgBean> imgBeans;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);
        imgBeans = new ArrayList<>();
        docid = getIntent().getStringExtra("docid");
        img = getIntent().getStringExtra("img");
        title = (TextView) findViewById(R.id.textView_title);
        ptime = (TextView) findViewById(R.id.textView_ptime);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar3);

        sendRequest();
    }

    private void sendRequest() {
        String urlString = "http://c.3g.163.com/nc/article/" + docid + "/full.html";

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


            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }

    private void handlejson(byte[] bytes) {
        String jsonStr = new String(bytes);
        System.out.println(jsonStr);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr).getJSONObject(docid);
            //String imgs = jsonObject.getString("img");
            // System.out.println(imgs);
            JSONArray jsonArray = jsonObject.getJSONArray("img");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ImgBean imgbean = new ImgBean(json.getString("src"));
                imgBeans.add(imgbean);
            }
            newsDetailBean = new NewsDetailBean(jsonObject.getString("body"), jsonObject.getString("title"), jsonObject.getString("ptime"));
            //initImg();
            initData(newsDetailBean);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initData(NewsDetailBean newsDetailBean) {
        String img;
        String imgHttp;
        StringBuffer imgBuffer = new StringBuffer();
        for (int i = 0; i < imgBeans.size(); i++) {
            imgHttp = imgBeans.get(i).getSrc();
            //img= "<img src="+imgHttp+"alt="+"图片加载中..." +"/>";
            img= "<img src=\""+imgHttp+"\" alt=\" "+"图片加载中…\""+"width=\""+"320\"" +"\"/>";
            imgBuffer.append(img);

        }
        title.setText(newsDetailBean.getTitle());
        ptime.setText(newsDetailBean.getPtime());
        webView.loadData(imgBuffer.toString()+newsDetailBean.getBody(), "text/html; charset=UTF-8", null);

    }

    private void initImg() {
        // 显示图片的配置
        if (imgBeans.size()!=0){
            String imgurl = imgBeans.get(0).getSrc();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory(true)
                    .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
            ImageLoader.getInstance()
                    .displayImage(
                            imgurl,
                            imageView, options);
        }


    }
}
