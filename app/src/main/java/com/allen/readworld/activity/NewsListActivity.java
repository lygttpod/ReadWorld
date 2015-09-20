package com.allen.readworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.allen.readworld.R;

import com.allen.readworld.adapter.NewsListAdapter;
import com.allen.readworld.bean.NewsBean;
import com.allen.readworld.bean.NewsListBean;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Allen on 15/9/20.
 */
public class NewsListActivity extends Activity {
    private PullToRefreshListView pullToRefreshListView;
    private AsyncHttpClient asyncHttpClient;
    NewsListAdapter newsListAdapter;
    ArrayList<NewsListBean> newsListBeans;
    String tid ="";
    int c=0;
    String count = (c+"-"+(c+10));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initPullToRefresh();
        sendRequest(count,true);
    }
    private void init(){
        tid = getIntent().getStringExtra("tid");
        newsListBeans = new ArrayList<NewsListBean>();
        newsListAdapter = new NewsListAdapter(NewsListActivity.this,newsListBeans);
    }

    private void initPullToRefresh(){
        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.toplist_pull_to_refresh);
        String label = DateUtils.formatDateTime(NewsListActivity.this, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout startLayout = pullToRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLayout.setLastUpdatedLabel(label);
        startLayout.setLastUpdatedLabel("上次更新时间" + label);
        startLayout.setPullLabel("下拉刷新");
        startLayout.setRefreshingLabel("正在刷新");
        startLayout.setReleaseLabel("松开刷新");
        ILoadingLayout endLayout = pullToRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLayout.setPullLabel("上滑加载更多");
        endLayout.setRefreshingLabel("正在加载");
        endLayout.setReleaseLabel("松开加载更多");

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                sendRequest("0-10",true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                c=newsListBeans.size();
                count = (c+"-"+(c+10));
                sendRequest(count,false);
            }
        });
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        ListView actualListView = pullToRefreshListView.getRefreshableView();
        actualListView.setAdapter(newsListAdapter);
    }
    private void sendRequest (String count, final boolean isClear){
        String urlString = "http://c.3g.163.com/nc/article/headline/"+tid+"/"+count+".html";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (isClear){
                    newsListBeans.clear();
                }
                handlejson(bytes);
//
            }

            @Override
            public void onFinish() {
                super.onFinish();
                pullToRefreshListView.onRefreshComplete();

                newsListAdapter.notifyDataSetChanged();
            }
        });

    }
    private void handlejson(byte[] bytes){
        String jsonString = new String(bytes);
        JSONObject jsonObject = null;
        try {
             jsonObject= new JSONObject(jsonString);
//            String tidString = jsonObject.getString(tid);
            JSONArray tidArr = jsonObject.getJSONArray(tid);
            for (int i = 0; i < tidArr.length(); i++) {
                JSONObject json = tidArr.getJSONObject(i);
                NewsListBean newsListBean = new NewsListBean(json.getString("title"),json.getString("digest"),json.getString("docid"),json.getString("replyCount"),json.getString("ptime"),json.getString("imgsrc"));
                newsListBeans.add(newsListBean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
