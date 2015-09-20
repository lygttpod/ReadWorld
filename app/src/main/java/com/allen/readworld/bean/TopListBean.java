package com.allen.readworld.bean;

/**
 * Created by Allen on 15/9/19.
 */
public class TopListBean {
    private String tname;
    private String tid;

    public TopListBean(String tname, String tid) {
        this.tname = tname;
        this.tid = tid;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
