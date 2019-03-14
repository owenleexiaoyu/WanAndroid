package cc.lixiaoyu.wanandroid.entity;

import org.litepal.crud.LitePalSupport;

/**
 * 搜索历史的实体类
 */
public class HistoryData extends LitePalSupport{
    private int id;
    private String data;

    public HistoryData(String data){
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
