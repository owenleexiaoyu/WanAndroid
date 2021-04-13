package cc.lixiaoyu.wanandroid.entity;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProjectPage {

    private int curPage;
    @SerializedName("datas")
    private List<Article> dataList;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;


    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }
    public int getCurpage() {
        return curPage;
    }


    public void setDataList(List<Article> dataList) {
        this.dataList = dataList;
    }
    public List<Article> getDataList() {
        return dataList;
    }


    public void setOffset(int offset) {
        this.offset = offset;
    }
    public int getOffset() {
        return offset;
    }


    public void setOver(boolean over) {
        this.over = over;
    }
    public boolean getOver() {
        return over;
    }


    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getPagecount() {
        return pageCount;
    }


    public void setSize(int size) {
        this.size = size;
    }
    public int getSize() {
        return size;
    }


    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }
}


