package cc.lixiaoyu.wanandroid.entity;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

public class ProjectPage {

    private int curPage;
    @SerializedName("datas")
    private List<ProjectData> dataList;
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


    public void setDataList(List<ProjectData> dataList) {
        this.dataList = dataList;
    }
    public List<ProjectData> getDataList() {
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

    public class ProjectData {

        private String apkLink;
        private String author;
        private int chapterId;
        private String chapterName;
        private boolean collect;
        private int courseId;
        private String desc;
        private String envelopePic;
        private boolean fresh;
        private int id;
        private String link;
        private String niceDate;
        private String origin;
        private String projectLink;
        private long publishTime;
        private int superChapterId;
        private String superChapterName;
        private List<ProjectPage.Tags> tags;
        private String title;
        private int type;
        private int userId;
        private int visible;
        private int zan;


        public void setApkLink(String apkLink) {
            this.apkLink = apkLink;
        }
        public String getApkLink() {
            return apkLink;
        }


        public void setAuthor(String author) {
            this.author = author;
        }
        public String getAuthor() {
            return author;
        }


        public void setChapterId(int chapterId) {
            this.chapterId = chapterId;
        }
        public int getChapterId() {
            return chapterId;
        }


        public void setChapterName(String chapterName) {
            this.chapterName = chapterName;
        }
        public String getChapterName() {
            return chapterName;
        }


        public void setCollect(boolean collect) {
            this.collect = collect;
        }
        public boolean isCollect() {
            return collect;
        }


        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }
        public int getCourseId() {
            return courseId;
        }


        public void setDesc(String desc) {
            this.desc = desc;
        }
        public String getDesc() {
            return desc;
        }


        public void setEnvelopePic(String envelopePic) {
            this.envelopePic = envelopePic;
        }
        public String getEnvelopePic() {
            return envelopePic;
        }


        public void setFresh(boolean fresh) {
            this.fresh = fresh;
        }
        public boolean getFresh() {
            return fresh;
        }


        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }


        public void setLink(String link) {
            this.link = link;
        }
        public String getLink() {
            return link;
        }


        public void setNiceDate(String niceDate) {
            this.niceDate = niceDate;
        }
        public String getNiceDate() {
            return niceDate;
        }


        public void setOrigin(String origin) {
            this.origin = origin;
        }
        public String getOrigin() {
            return origin;
        }


        public void setProjectLink(String projectLink) {
            this.projectLink = projectLink;
        }
        public String getProjectLink() {
            return projectLink;
        }


        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }
        public long getPublishTime() {
            return publishTime;
        }


        public void setSuperChapterId(int superChapterId) {
            this.superChapterId = superChapterId;
        }
        public int getSuperChapterId() {
            return superChapterId;
        }


        public void setSuperChapterName(String superChapterName) {
            this.superChapterName = superChapterName;
        }
        public String getSuperChapterName() {
            return superChapterName;
        }


        public void setTags(List<ProjectPage.Tags> tags) {
            this.tags = tags;
        }
        public List<ProjectPage.Tags> getTags() {
            return tags;
        }


        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }


        public void setType(int type) {
            this.type = type;
        }
        public int getType() {
            return type;
        }


        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }


        public void setVisible(int visible) {
            this.visible = visible;
        }
        public int getVisible() {
            return visible;
        }


        public void setZan(int zan) {
            this.zan = zan;
        }
        public int getZan() {
            return zan;
        }

    }

    public class Tags {

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

}


