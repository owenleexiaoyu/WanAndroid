package cc.lixiaoyu.wanandroid.util;

import org.litepal.LitePal;

import cc.lixiaoyu.wanandroid.entity.User;

public class DataManager {
    private static User me = LitePal.findFirst(User.class);

    //获取当前的数据库中保存的用户，用于判断是否登陆等
    public static User getCurrentUser(){
        return me;
    }

    public static void clearCurrentUser(){
        LitePal.deleteAll(User.class);
    }
}
