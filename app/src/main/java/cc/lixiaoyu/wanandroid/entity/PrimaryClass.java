package cc.lixiaoyu.wanandroid.entity;

import java.util.List;

/**
 * 知识体系的一级分类的实体类
 */
public class PrimaryClass {

    private List<SubClass> children;

    private int courseId;
    private int id;
    private String name;
    private int order;
    private int parentChapterId;
    private int visible;


    public void setChildren(List<SubClass> children) {
        this.children = children;
    }
    public List<SubClass> getChildren() {
        return children;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


    public void setOrder(int order) {
        this.order = order;
    }
    public int getOrder() {
        return order;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
    public int getVisible() {
        return visible;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public class SubClass {

        private List<String> children;
        private int courseId;
        private int id;
        private String name;
        private int order;
        private int parentChapterId;
        private int visible;


        public void setChildren(List<String> children) {
            this.children = children;
        }
        public List<String> getChildren() {
            return children;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }


        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setOrder(int order) {
            this.order = order;
        }
        public int getOrder() {
            return order;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }
        public int getVisible() {
            return visible;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getParentChapterId() {
            return parentChapterId;
        }

        public void setParentChapterId(int parentChapterId) {
            this.parentChapterId = parentChapterId;
        }
    }

}

