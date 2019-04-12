package cc.lixiaoyu.wanandroid.util;

import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.entity.HistoryData;

/**
 * DBHelper实现类
 */
public class DBHelperImpl implements DBHelper {
    @Override
    public boolean addHistoryData(String historyData) {
        //先查重
        List<HistoryData> result = LitePal.where("data=?", historyData)
                .find(HistoryData.class);
        if(result == null){
            //使用Litepal保存数据
            HistoryData hd = new HistoryData(historyData);
            return hd.save();
        }
        return false;
    }

    @Override
    public void deleteHistoryData(String historyData) {
        LitePal.deleteAll(HistoryData.class, "data=?", historyData);
    }

    @Override
    public void clearAllHistoryData() {
        LitePal.deleteAll(HistoryData.class);
    }

    @Override
    public List<String> loadAllHistoryData() {
        List<HistoryData> hdList = LitePal.findAll(HistoryData.class);
        List<String> list = new ArrayList<>(hdList.size());
        for (HistoryData hd : hdList) {
            list.add(hd.getData());
        }
        return list;
    }
}
