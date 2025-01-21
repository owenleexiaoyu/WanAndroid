package cc.lixiaoyu.wanandroid.core.todo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cc.lixiaoyu.wanandroid.api.WanAndroidService
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity
import cc.lixiaoyu.wanandroid.core.todo.ui.TodoActivity.Companion.TODO_STATUS_UNDONE
import cc.lixiaoyu.wanandroid.util.ToastUtil
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class TodoViewModel: ViewModel() {

    private val apiService: WanAndroidService = RetrofitManager.wanAndroidService

    private val _typeIndex: MutableLiveData<Int> = MutableLiveData(0)
    val typeIndex: LiveData<Int> = _typeIndex

    private val _unfinishedList: MutableLiveData<List<TodoEntity.TodoItem>> = MutableLiveData(
        emptyList()
    )
    val unfinishedList: LiveData<List<TodoEntity.TodoItem>> = _unfinishedList

    private val _finishedList: MutableLiveData<List<TodoEntity.TodoItem>> = MutableLiveData(
        emptyList()
    )
    val finishedList: LiveData<List<TodoEntity.TodoItem>> = _finishedList

    init {
        getTodoLists(_typeIndex.value ?: 0)
    }

    fun setTypeIndex(index: Int) {
        if (index < 0 || index >= 4) {
            throw IllegalStateException("UnSupported index($index)")
        }
        _typeIndex.value = index
    }

    fun getTodoLists(type: Int) {
        viewModelScope.launch {
            try {
                val resp = apiService.getTodoListByType(type)
                val entity = resp.data ?: return@launch
                val unfinishedList = mutableListOf<TodoEntity.TodoItem>()
                entity.todoList?.forEach {
                    it.todoList?.forEach { item ->
                        unfinishedList.add(item)
                    }
                }
                val finishedList = mutableListOf<TodoEntity.TodoItem>()
                entity.doneList?.forEach {
                    it.todoList?.forEach { item ->
                        finishedList.add(item)
                    }
                }
                _unfinishedList.value = unfinishedList
                _finishedList.value = finishedList
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }


    /**
     * 添加一条待办事项
     */
    fun addTodoItem(title: String, content: String, time: String, type: Int) {
        viewModelScope.launch {
            try {
                val resp = apiService.addTodoItem(title, content, time, type)
                val todoItem = resp.data ?: return@launch
                val mutableList = _unfinishedList.value?.toMutableList() ?: mutableListOf()
                mutableList.add(todoItem)
                _unfinishedList.value = mutableList
                ToastUtil.showToast("添加 Todo 成功")
            } catch (t: Throwable) {
                t.printStackTrace()
                ToastUtil.showToast("添加 Todo 失败")
            }
        }
    }

    /**
     * 删除一条待办事项
     */
    fun deleteTodoItem(item: TodoEntity.TodoItem) {
        val status = item.status
        viewModelScope.launch {
            try {
                val result = apiService.deleteTodoItem(item.id).data
                if (status == TODO_STATUS_UNDONE) {
                    val newUnfinishedList = _unfinishedList.value?.filter { item.id != it.id } ?: emptyList()
                    _unfinishedList.value = newUnfinishedList
                } else {
                    val newFinishedList = _finishedList.value?.filter { item.id != it.id } ?: emptyList()
                    _finishedList.value = newFinishedList
                }
                ToastUtil.showToast("删除 Todo 成功")
            } catch (t: Throwable) {
                t.printStackTrace()
                ToastUtil.showToast("删除 Todo 失败")
            }
        }
    }

    /**
     * 修改一条待办事项的状态
     */
    fun changeTodoItemStatus(item: TodoEntity.TodoItem, targetStatus: Int) {
        if (item.status == targetStatus) {
            return
        }
        viewModelScope.launch {
            try {
                val resp = apiService.updateTodoItem(item.id, item.title, item.content, item.dateStr, item.type, targetStatus)
                val newItem = resp.data ?: return@launch
                if (targetStatus == TODO_STATUS_UNDONE) { // 从已完成变成未完成
                    val newFinishedList = _finishedList.value?.filter { it.id != newItem.id } ?: emptyList()
                    _finishedList.value = newFinishedList
                    val newUnfinishedList = (_unfinishedList.value?.toMutableList() ?: mutableListOf()).apply {
                        add(item)
                    }
                    _unfinishedList.value = newUnfinishedList
                } else {
                    val newUnfinishedList = _unfinishedList.value?.filter { it.id != newItem.id } ?: emptyList()
                    _unfinishedList.value = newUnfinishedList
                    val newFinishedList = (_finishedList.value?.toMutableList() ?: mutableListOf()).apply {
                        add(item)
                    }
                    _finishedList.value = newFinishedList
                }
            } catch (t: Throwable) {
                ToastUtil.showToast("修改 Todo 状态失败")
            }
        }
    }

    fun updateTodoItem(item: TodoEntity.TodoItem) {
        viewModelScope.launch {
            try {
                val resp = apiService.updateTodoItem(item.id, item.title, item.content,
                    item.dateStr, item.type, item.status)
                val newItem = resp.data ?: return@launch
                if (newItem.status == TODO_STATUS_UNDONE) { // 修改的是未完成的待办事项
                    val index = _unfinishedList.value?.indexOfFirst { it.id == newItem.id } ?: -1
                    if (index >= 0) {
                        val newUnfinishedList = _unfinishedList.value?.toMutableList()?.apply {
                            removeAt(index)
                            add(index, newItem)
                        } ?: mutableListOf()
                        _unfinishedList.value = newUnfinishedList
                    }
                } else {  // 修改的是已完成的事项
                    val index = _finishedList.value?.indexOfFirst { it.id == newItem.id } ?: -1
                    if (index >= 0) {
                        val newFinishedList = _finishedList.value?.toMutableList()?.apply {
                            removeAt(index)
                            add(index, newItem)
                        } ?: mutableListOf()
                        _finishedList.value = newFinishedList
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                ToastUtil.showToast("任务详情修改失败")
            }
        }
    }
}