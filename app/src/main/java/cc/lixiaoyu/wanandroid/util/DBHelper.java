package cc.lixiaoyu.wanandroid.util;

import java.util.List;

/**
 * 数据库操作接口，提供应用中需要数据库存储的的方法
 */
public interface DBHelper {
    /**
     * 添加一个搜索历史数据
     * @param historyData
     * @return
     */
    boolean addHistoryData(String historyData);

    /**
     * 删除一条搜索历史
     * @param historyData
     * @return
     */
    void deleteHistoryData(String historyData);

    /**
     * 清空所有的搜索历史
     * @return
     */
    void clearAllHistoryData();

    /**
     * 加载所有的搜索历史
     * @return
     */
    List<String> loadAllHistoryData();
}
