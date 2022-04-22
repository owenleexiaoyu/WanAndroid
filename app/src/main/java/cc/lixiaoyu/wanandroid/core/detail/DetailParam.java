package cc.lixiaoyu.wanandroid.core.detail;

import java.io.Serializable;

public class DetailParam implements Serializable {

    private int articleId;
    private String title;
    private String link;
    private DetailType detailType;

    public DetailParam(int id, String title, String link, DetailType detailType) {
        articleId = id;
        this.title = title;
        this.link = link;
        this.detailType = detailType;
    }

    public int getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    /**
     * 详情页的内容是否可以收藏，只有 ARTICLE 类型的才可以收藏
     * @return
     */
    public boolean isCollectable() {
        return detailType == DetailType.ARTICLE;
    }

    public enum DetailType {
        ARTICLE,
        WEBPAGE
    }
}
