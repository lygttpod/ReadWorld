package com.allen.readworld.bean;

/**
 * Created by hardy on 2015/9/24.
 */
public class ImgBean {
//    "img":[
//    {
//        "ref":"",
//            "pixel":"640*320",
//            "alt":"",
//            "src":"http://img5.cache.netease.com/3g/2015/9/6/20150906083757bdf81.jpg"
//    },
    String src;

    public ImgBean(String src) {
        this.src = src;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
