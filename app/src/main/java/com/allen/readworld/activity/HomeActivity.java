package com.allen.readworld.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.readworld.R;
import com.allen.readworld.adapter.MyFragmentAdapter;
import com.allen.readworld.application.MyAppcaltion;

import com.allen.readworld.db.greenrobot.gen.ChannelItem;
import com.allen.readworld.fragment.NewListFragment;
import com.allen.readworld.utils.GreenDaoUtils;
import com.allen.readworld.utils.ScreenSizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 15/9/23.
 */
public class HomeActivity extends BaseActivity {
    //List<TopListBean> topListBeans;
    MyFragmentAdapter myFragmentAdapter;
    List<ChannelItem> userChannelList;
    String urlString = "http://c.3g.163.com/nc/topicset/android/subscribe/manage/listspecial.html";
    private HorizontalScrollView mNaviga_scroll;
    private LinearLayout mNavigation;
    private int columnSelectIndex = 0;

    private int mScreenWidth = 0;
    private ViewPager viewPager;
    private List<Fragment> viewList = null;
    // private Fragment fragment;

    // private MyFragmentAdapter myFragmentAdapter;

    /**
     * 请求CODE
     */
    public final static int CHANNELREQUEST = 1;
    /**
     * 调整返回的RESULTCODE
     */
    public final static int CHANNELRESULT = 10;

    private ImageView channelIV;

    private GreenDaoUtils greenDaoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        greenDaoUtils = new GreenDaoUtils(HomeActivity.this);
        channelIV = (ImageView) findViewById(R.id.add_naviga_itme_bt);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        userChannelList = new ArrayList<>();

        viewList = new ArrayList<Fragment>();
        mScreenWidth = ScreenSizeUtil.getScreenWidth(HomeActivity.this);
        mNaviga_scroll = (HorizontalScrollView) findViewById(R.id.naviga_scroll);
        mNavigation = (LinearLayout) findViewById(R.id.naviga_view);
        initColumnData();
        initTabColumn();
        initViewPage();
        selectTab(0);
        channelIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, ChannelActivity.class);
                startActivityForResult(intent, CHANNELREQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHANNELREQUEST:
                if (resultCode == CHANNELRESULT) {
                    initColumnData();
                    initTabColumn();
                    initViewPage();
                    selectTab(0);
                    Toast.makeText(HomeActivity.this, "onActivityResult", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViewPage() {
        viewList.clear();
        for (int i = 0; i < userChannelList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("tid", userChannelList.get(i).getTid());
            //Fragment fragment = new NewListFragment();
            NewListFragment newListFragment = new NewListFragment();
            newListFragment.setArguments(bundle);
            viewList.add(newListFragment);
        }

        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), viewList);

        viewPager.setAdapter(myFragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取Column栏目 数据
     */
    private void initColumnData() {
//        userChannelList = ChannelManage.getManage(MyAppcaltion.getApp().getSQLHelper())
//                .getUserChannel();
        userChannelList = greenDaoUtils.getChannelItems(1);
    }


    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mNavigation.removeAllViews();
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 20;
            params.rightMargin = 20;
            params.gravity = Gravity.CENTER;
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mNavigation.getChildCount(); i++) {
                        View localView = mNavigation.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            selectTab(i);
                            viewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mNavigation.addView(columnTextView, i, params);
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mNavigation.getChildCount(); i++) {
            View checkView = mNavigation.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mNaviga_scroll.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j < mNavigation.getChildCount(); j++) {
            View checkView = mNavigation.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

}
