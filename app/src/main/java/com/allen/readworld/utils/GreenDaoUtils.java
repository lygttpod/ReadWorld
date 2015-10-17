package com.allen.readworld.utils;

import java.util.List;

import android.R.integer;
import android.content.Context;

import com.allen.readworld.db.greenrobot.gen.ChannelItem;
import com.allen.readworld.db.greenrobot.gen.ChannelItemDao;
import com.allen.readworld.db.greenrobot.gen.ChannelItemDao.Properties;
import com.allen.readworld.db.greenrobot.gen.DaoMaster;
import com.allen.readworld.db.greenrobot.gen.DaoMaster.OpenHelper;
import com.allen.readworld.db.greenrobot.gen.DaoSession;

import de.greenrobot.dao.query.QueryBuilder;

public class GreenDaoUtils {
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private ChannelItemDao channelItemDao;
    private Context context;

    public GreenDaoUtils(Context context) {
        super();
        this.daoMaster = getDaoMaster(context);
        this.daoSession = getDaoSession(context);
        this.channelItemDao = daoSession.getChannelItemDao();
    }

    public DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            OpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    "database_name", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     * 插入数据
     *
     * @param tname
     * @param tid
     * @param orderid
     * @param selected
     */
    public void insertGreenDao(String tname, String tid, int orderid,
                               int selected) {
        ChannelItem channelItem = new ChannelItem(null, tname, tid, orderid,
                selected);
        channelItemDao.insert(channelItem);
    }

    /**
     * 获取数据 根绝selected选取是否是用户选择的新闻频道
     *
     * @param selected 值是0/1
     * @return
     */
    public List<ChannelItem> getChannelItems(int selected) {
        QueryBuilder<ChannelItem> channelBuilder = channelItemDao
                .queryBuilder();
        channelBuilder.where(Properties.Selected.eq(selected));
        channelBuilder.orderAsc(Properties.Id);
        List<ChannelItem> channelItems = channelBuilder.list();
        return channelItems;
    }

    /**
     * 删除数据
     *
     * @param channelItem
     */
    public void deleteChannel(ChannelItem channelItem) {
        channelItemDao.delete(channelItem);
    }

    /**
     * 删除所有数据
     */
    public void deleteAllData() {
        channelItemDao.deleteAll();
    }

    /**
     * 保存用户频道到数据库
     *
     * @param userList
     */
    public void saveUserChannel(List<ChannelItem> userList) {
        for (int i = 0; i < userList.size(); i++) {
            insertGreenDao(userList.get(i).getName(), userList.get(i).getTid(), i, 1);
        }
    }

    /**
     * 保存其他频道到数据库
     *
     * @param otherList
     */
    public void saveOtherChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            insertGreenDao(otherList.get(i).getName(), otherList.get(i).getTid(), i+100, 0);
        }
    }
}

