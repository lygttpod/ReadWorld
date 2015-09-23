package com.allen.readworld.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.readworld.R;
import com.allen.readworld.adapter.MyFragmentAdapter;
import com.allen.readworld.application.MyAppcaltion;
import com.allen.readworld.bean.TopListBean;
import com.allen.readworld.fragment.NewListFragment;
import com.allen.readworld.utils.ScreenSizeUtil;
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
public class HomeActivity1 extends FragmentActivity {
    List<TopListBean> topListBeans;
    MyAppcaltion myAppcation;
    String urlString = "http://c.3g.163.com/nc/topicset/android/subscribe/manage/listspecial.html";

    private HorizontalScrollView mNaviga_scroll;
    private LinearLayout mNavigation;
    private int columnSelectIndex = 0;

    private int mScreenWidth = 0;
    private ViewPager viewPager;
    private List<Fragment> viewList = null;
    private Fragment fragment;

    private MyFragmentAdapter myFragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        myAppcation = (MyAppcaltion)getApplicationContext();
        topListBeans = myAppcation.getTopListBeans();
        viewList = new ArrayList<Fragment>();
        mScreenWidth = ScreenSizeUtil.getScreenWidth(HomeActivity1.this);
        mNaviga_scroll = (HorizontalScrollView) findViewById(R.id.naviga_scroll);
        mNavigation = (LinearLayout) findViewById(R.id.naviga_view);
        setNavigation(topListBeans);
        initViewPage();

    }
    private void initViewPage(){
        for (int i = 0; i < topListBeans.size(); i++) {
            fragment = new NewListFragment(topListBeans.get(i).getTid());
            viewList.add(fragment);
        }
        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),viewList);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(myFragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setNavigation(List<TopListBean> topListBeans) {
        int count = topListBeans.size();
        mNavigation.removeAllViews();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 12;
            params.rightMargin = 12;

            TextView localTextView = new TextView(this);
            localTextView
                    .setBackgroundResource(R.drawable.selector_navigation_btn);
            localTextView.setGravity(Gravity.CENTER);
            //localTextView.setPadding(0, 5, 0, 5);
            localTextView.setPadding(0, 2, 0, 2);
            localTextView.setId(i);
            localTextView.setText(topListBeans.get(i).getTname());
            localTextView.setTextColor(getResources().getColor(
                    R.color.top_category_scroll_text_color_day));
            localTextView.setTextSize(20);
            if (columnSelectIndex == i) {
                localTextView.setSelected(true);
            }
            localTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mNavigation.getChildCount(); i++) {
                        View localView = mNavigation.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            viewPager.setCurrentItem(i/2);
                            selectTab(i / 2);
                        }
                    }
                }
            });
            mNavigation.addView(localTextView, params);
            if (i != count - 1) {
                ImageView imageView = new ImageView(this);
                imageView.setImageDrawable(getResources().getDrawable(
                        R.mipmap.nav_line));
                LinearLayout.LayoutParams split = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mNavigation.addView(imageView, split);
            }
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion * 2;
        // for (int i = 0; i < mNavigation.getChildCount(); i++) {
        View checkView = mNavigation.getChildAt(tab_postion * 2);
        int k = checkView.getMeasuredWidth();
        int l = checkView.getLeft();
        int i2 = l + k / 2 - mScreenWidth / 2;
        // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
        mNaviga_scroll.smoothScrollTo(i2, 0);
        // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
        // mItemWidth , 0);
        // }
        // 判断是否选中
        for (int j = 0; j < mNavigation.getChildCount(); j++) {
            View checkView1 = mNavigation.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion * 2) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView1.setSelected(ischeck);
        }
    }

}
