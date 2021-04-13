package cc.lixiaoyu.wanandroid.core.detail;

import java.io.Serializable;

public class ArticleDetailParam implements Serializable {

    private int articleId;
    private String title;
    private String link;

    public ArticleDetailParam(int id, String title, String link) {
        articleId = id;
        this.title = title;
        this.link = link;
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
}
