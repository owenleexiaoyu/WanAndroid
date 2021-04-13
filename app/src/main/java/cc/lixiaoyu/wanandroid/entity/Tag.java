package cc.lixiaoyu.wanandroid.entity;

import java.io.Serializable;

public class Tag implements Serializable {

    private String name;
    private String url;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
}
