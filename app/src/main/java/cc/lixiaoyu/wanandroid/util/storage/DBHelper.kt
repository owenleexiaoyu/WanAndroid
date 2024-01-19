package cc.lixiaoyu.wanandroid.util.storage

import cc.lixiaoyu.wanandroid.entity.HistoryData
import org.litepal.LitePal

/**
 * DBHelper实现类
 */
class DBHelper {
    fun addHistoryData(historyData: String?): Boolean {
//        //先查重
//        List<HistoryData> result = LitePal.where("data=?", historyData)
//                .find(HistoryData.class);
//        if(result == null){
//            //使用Litepal保存数据
        val hd = HistoryData(historyData!!)
        return hd.save()
        //        }
//        return false;
    }

    fun deleteHistoryData(historyData: String?) {
        LitePal.deleteAll(HistoryData::class.java, "data=?", historyData)
    }

    fun clearAllHistoryData() {
        LitePal.deleteAll(HistoryData::class.java)
    }

    fun loadAllHistoryData(): List<String> {
        val hdList = LitePal.findAll(
            HistoryData::class.java
        )
        val list: MutableList<String> = ArrayList(hdList.size)
        for (hd in hdList) {
            list.add(hd.data)
        }
        return list
    }
}