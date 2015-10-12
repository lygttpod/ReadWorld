package com.allen.readworld.bean;

import com.google.gson.JsonArray;

/**
 * Created by hardy on 2015/9/24.
 */
public class NewsDetailBean {
    private String body;
    private String title;
    private String ptime;

    public NewsDetailBean(String body, String title, String ptime) {
        this.body = body;
        this.title = title;
        this.ptime = ptime;

    }



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
}
