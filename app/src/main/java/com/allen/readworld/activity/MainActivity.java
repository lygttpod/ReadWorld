package com.allen.readworld.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.allen.readworld.R;
import com.allen.readworld.adapter.NewsTopListAdapter;
import com.allen.readworld.bean.TopListBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PullToRefreshListView pullToRefreshListView;
    private NewsTopListAdapter newsTopListAdapter;
    private List<TopListBean> topListBeans;
    private Gson gson;
    private AsyncHttpClient asyncHttpClient;

    String urlString = "http://c.3g.163.com/nc/topicset/android/subscribe/manage/listspecial.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sendRequest();
        initPullToRefresh();


    }
    private void init(){

        topListBeans = new ArrayList<TopListBean>();
        newsTopListAdapter = new NewsTopListAdapter(MainActivity.this,topListBeans);
    }
    private void initPullToRefresh(){
        pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.toplist_pull_to_refresh);
        String label = DateUtils.formatDateTime(MainActivity.this, System.currentTimeMillis(),
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

                sendRequest();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,
                        NewsListActivity.class);
                intent.putExtra("tid", topListBeans.get(position - 1).getTid());
                startActivity(intent);
            }
        });
        ListView actualListView = pullToRefreshListView.getRefreshableView();
        actualListView.setAdapter(newsTopListAdapter);
    }
    private void sendRequest (){
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get(urlString, new AsyncHttpResponseHandler() {
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                handlejson(bytes);
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                pullToRefreshListView.onRefreshComplete();
//                newsTopListAdapter.notifyDataSetChanged();
//
//            }
//        });
    }

    private void handlejson(byte[] bytes){
        String jsonStr = new String(bytes);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("tList");
            for (int b = 0; b < jsonArray.length(); b++) {
                JSONObject json = jsonArray.getJSONObject(b);
                TopListBean topListBean = new TopListBean(json.getString("tname"),json.getString("tid"));
                topListBeans.add(topListBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        topListBeans = gson.fromJson(tList, new TypeToken<ArrayList<TopListBean>>(){
//        }.getType());
    }

}
