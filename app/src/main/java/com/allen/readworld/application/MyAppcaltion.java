package com.allen.readworld.application;

import android.app.Application;
import android.content.Context;

import com.allen.readworld.bean.TopListBean;
import com.allen.readworld.db.SQLHelper;
import com.allen.readworld.utils.GreenDaoUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allen on 15/9/20.
 */
public class MyAppcaltion extends Application {
    private static MyAppcaltion mAppApplication;
    private SQLHelper sqlHelper;
    List<TopListBean> topListBeans;

    GreenDaoUtils greenDaoUtils;

    public List<TopListBean> getTopListBeans() {
        return topListBeans;
    }

    public void setTopListBeans(List<TopListBean> topListBeans) {
        this.topListBeans = topListBeans;
    }

    public GreenDaoUtils getGreenDaoUtils() {
        return greenDaoUtils;
    }

    public void setGreenDaoUtils(GreenDaoUtils greenDaoUtils) {
        this.greenDaoUtils = greenDaoUtils;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
        greenDaoUtils = new GreenDaoUtils(mAppApplication);
        topListBeans = new ArrayList<TopListBean>();
        initImageLoader(getApplicationContext());
        mAppApplication.setGreenDaoUtils(greenDaoUtils);
    }

    public static void initImageLoader(Context context) {
        // 缓存文件的目录
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "ReadWorld/imageloader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .memoryCacheExtraOptions(480, 800)
                        // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)
                        // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5加密
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass  your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // 由原先的discCache -> diskCache
                .diskCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout(5s),readTimeout(30)超时时间
                .writeDebugLogs() // Remove for release app

                .build();
        // 全局初始化此配置
        ImageLoader.getInstance().init(config);
    }
    /** 获取Application */
    public static MyAppcaltion getApp() {
        return mAppApplication;
    }
    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
        //整体摧毁的时候调用这个方法
    }
}
