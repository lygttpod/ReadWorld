package com.allen.readworld.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.allen.readworld.R;
import com.allen.readworld.activity.NewsDetailActivity;
import com.allen.readworld.adapter.NewsListAdapter;
import com.allen.readworld.bean.NewsListBean;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Allen on 15/9/23.
 */
public class NewListFragment extends Fragment {
    private ProgressBar progressBar;
    private PullToRefreshListView pullToRefreshListView;
    private AsyncHttpClient asyncHttpClient;
    NewsListAdapter newsListAdapter;
    ArrayList<NewsListBean> newsListBeans;
    String tid ="";
    int c=0;
    String count = (c+"-"+(c+10));


    public NewListFragment(String tid) {
        this.tid = tid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newlist_fragment,container,false);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar2);
        init();
        initPullToRefresh(view);
        sendRequest(count,true);
        return view;
    }

    private void init(){
        //tid = getIntent().getStringExtra("tid");
        newsListBeans = new ArrayList<NewsListBean>();
        newsListAdapter = new NewsListAdapter(getActivity(),newsListBeans);
    }

    private void initPullToRefresh(View view){
        pullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.toplist_pull_to_refresh);
        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
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
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewsDetailActivity.class);
                intent.putExtra("docid",newsListBeans.get(position-1).getDocid());
                intent.putExtra("img", newsListBeans.get(position - 1).getImgsrc());
                startActivity(intent);
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
            public void onStart() {
                super.onStart();
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }

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
                progressBar.setVisibility(ProgressBar.GONE);
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
