package cc.lixiaoyu.wanandroid.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Banner {


    private List<BannerData> data;
    @SerializedName("errorCode")
    private int errorcode;
    @SerializedName("errorMsg")
    private String errormsg;


    public void setData(List<BannerData> data) {
        this.data = data;
    }

    public List<BannerData> getData() {
        return data;
    }


    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public int getErrorcode() {
        return errorcode;
    }


    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public class BannerData {

        private String desc;
        private int id;
        @SerializedName("imagePath")
        private String imagepath;
        @SerializedName("isVisible")
        private int isvisible;
        private int order;
        private String title;
        private int type;
        private String url;


        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }


        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }


        public void setImagepath(String imagepath) {
            this.imagepath = imagepath;
        }

        public String getImagepath() {
            return imagepath;
        }


        public void setIsvisible(int isvisible) {
            this.isvisible = isvisible;
        }

        public int getIsvisible() {
            return isvisible;
        }


        public void setOrder(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
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


        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

    }

}

