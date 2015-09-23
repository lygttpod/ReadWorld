package com.allen.readworld.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.allen.readworld.bean.NewsListBean;

import java.util.List;

/**
 * Created by Allen on 15/9/23.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> viewList = null;


    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> viewList) {
        super(fm);
        this.viewList = viewList;
    }

    @Override
    public Fragment getItem(int position) {
        return viewList.get(position);
    }

    @Override
    public int getCount() {
        return viewList.size();
    }


}
