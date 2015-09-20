package com.allen.readworld.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allen.readworld.R;
import com.allen.readworld.bean.TopListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 15/9/19.
 */
public class NewsTopListAdapter extends BaseAdapter {
    private Context mContext;
    private List<TopListBean> topListBeans;

    public NewsTopListAdapter(Context mContext, List<TopListBean> topListBeans) {
        super();
        this.mContext = mContext;
        this.topListBeans = topListBeans;
    }

    @Override
    public int getCount() {
        return topListBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder =null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.top_list_item,null);
            viewHolder.tname = (TextView)convertView.findViewById(R.id.tname_TV);
           // viewHolder.tid = (TextView)convertView.findViewById(R.id.tid_TV);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.tname.setText(topListBeans.get(position).getTname());
       // viewHolder.tid.setText(topListBeans.get(position).getTid());
        return convertView;
    }

    public class ViewHolder{
        TextView tname;
       // TextView tid;
    }
}
