package com.allen.readworld.bean;

/**
 * Created by Allen on 15/9/20.
 */
public class NewsListBean {
//    "url_3w":"http://news.163.com/15/0919/14/B3SNK1NI00014OMD.html",
//            "votecount":4381,
//            "replyCount":4832,
//            "pixel":"550*380",
//            "digest":"称卫星象征着朝鲜不屈的精神，不容许别国反对。",
//            "url":"http://3g.163.com/news/15/0919/14/B3SNK1NI00014OMD.html",
//            "docid":"B3SNK1NI00014OMD",
//            "title":"朝鲜称发射卫星是朝鲜主权",
//            "source":"新华网",
//            "priority":83,
//            "lmodify":"2015-09-19 14:02:00",
//            "subtitle":"",
//            "boardid":"news3_bbs",
//            "imgsrc":"http://img1.cache.netease.com/catchpic/B/B6/B683A42937CAC2F5C2442215C95CD6D6.jpg",
//            "ptime":"2015-09-19 14:02:00"
    String title;
    String digest;
    String docid;
    String replyCount;
    String ptime;
    String imgsrc;

    public NewsListBean(String title, String digest, String docid, String replyCount, String ptime, String imgsrc) {
        this.title = title;
        this.digest = digest;
        this.docid = docid;
        this.replyCount = replyCount;
        this.ptime = ptime;
        this.imgsrc = imgsrc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(String replyCount) {
        this.replyCount = replyCount;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }
}
