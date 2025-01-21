package cc.lixiaoyu.wanandroid.core.search

import cc.lixiaoyu.wanandroid.util.storage.SPUtil


object SearchHistoryStorage {
    private const val KEY = "SEARCH_HISTORY_LIST"

    fun addHistoryData(historyData: String): Boolean {
        val existingList = loadAllHistoryData()
        if (historyData in existingList) {
            return false
        }
        val newList: MutableList<String> = mutableListOf()
        newList.addAll(existingList)
        newList.add(historyData)
        saveHistoryData(newList)
        return true
    }

    fun clearAllHistoryData() {
        SPUtil.saveData(KEY, "")
    }

    fun loadAllHistoryData(): List<String> {
        val result = SPUtil.getData(KEY, "") as? String ?: ""
        if (result.isEmpty()) {
            return emptyList()
        }
        return result.split(",")
    }

    private fun saveHistoryData(historyData: List<String>) {
        SPUtil.saveData(KEY, historyData.joinToString(","))
    }

}