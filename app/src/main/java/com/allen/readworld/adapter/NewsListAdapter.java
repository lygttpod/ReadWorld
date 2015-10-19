package com.allen.readworld.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.readworld.R;
import com.allen.readworld.bean.NewsListBean;
import com.allen.readworld.bean.TopListBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Allen on 15/9/19.
 */
public class NewsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<NewsListBean> newsListBeans;

    public NewsListAdapter(Context mContext, List<NewsListBean> newsListBeans) {
        super();
        this.mContext = mContext;
        this.newsListBeans = newsListBeans;
    }

    @Override
    public int getCount() {
        return newsListBeans.size();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.news_list_item,null);
            viewHolder.title = (TextView)convertView.findViewById(R.id.news_title);
            viewHolder.digest = (TextView)convertView.findViewById(R.id.news_digest);
            viewHolder.ptime = (TextView)convertView.findViewById(R.id.news_time);
            viewHolder.imgsrc = (ImageView)convertView.findViewById(R.id.imageView_news);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.title.setText(newsListBeans.get(position).getTitle());
        viewHolder.digest.setText(newsListBeans.get(position).getDigest());
        viewHolder.ptime.setText(newsListBeans.get(position).getPtime());

        // 显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.base_list_default_icon)
                .showImageOnFail(R.mipmap.base_list_default_icon).cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance()
                .displayImage(
                        newsListBeans.get(position).getImgsrc(),
                        viewHolder.imgsrc, options);
        return convertView;
    }

    public class ViewHolder{

        TextView title;
        TextView digest;
        TextView ptime;
        ImageView imgsrc;
    }
}
