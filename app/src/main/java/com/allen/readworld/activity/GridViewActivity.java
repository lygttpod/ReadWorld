package com.allen.readworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.allen.readworld.R;
import com.allen.readworld.adapter.NewsTopListAdapter;
import com.allen.readworld.bean.TopListBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Allen on 15/9/20.
 */
public class GridViewActivity extends Activity {
    PullToRefreshGridView pullToRefreshGridView;
    NewsTopListAdapter newsTopListAdapter;
    List<TopListBean> topListBeans;
    String urlString = "http://c.3g.163.com/nc/topicset/android/subscribe/manage/listspecial.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        topListBeans = new ArrayList<TopListBean>();
        initGrid();
        sendRequest();
    }
    private void initGrid(){

        pullToRefreshGridView = (PullToRefreshGridView)findViewById(R.id.gridView_newstitle);
        String label = DateUtils.formatDateTime(GridViewActivity.this, System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);

        pullToRefreshGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ILoadingLayout startLayout = pullToRefreshGridView
                .getLoadingLayoutProxy(true, false);
        startLayout.setLastUpdatedLabel(label);
        startLayout.setLastUpdatedLabel("上次更新时间" + label);
        startLayout.setPullLabel("下拉刷新");
        startLayout.setRefreshingLabel("正在刷新");
        startLayout.setReleaseLabel("松开刷新");
        ILoadingLayout endLayout = pullToRefreshGridView.getLoadingLayoutProxy(
                false, true);
        endLayout.setPullLabel("上滑加载更多");
        endLayout.setRefreshingLabel("正在加载");
        endLayout.setReleaseLabel("松开加载更多");
        newsTopListAdapter = new NewsTopListAdapter(GridViewActivity.this,topListBeans);
        pullToRefreshGridView.setAdapter(newsTopListAdapter);
        pullToRefreshGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GridViewActivity.this,
                        NewsListActivity.class);
                intent.putExtra("tid", topListBeans.get(position).getTid());
                startActivity(intent);
            }
        });

        pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                sendRequest();
            }
        });
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
            }

            @Override
            public void onFinish() {
                super.onFinish();

                newsTopListAdapter.notifyDataSetChanged();
                pullToRefreshGridView.onRefreshComplete();
            }
        });
    }

    private void handlejson(byte[] bytes){
        topListBeans.clear();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
