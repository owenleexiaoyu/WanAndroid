package cc.lixiaoyu.wanandroid.core.todo.model

class TodoEntity {
    var doneList: List<Donelist>? = null
    var todoList: List<Todolist>? = null
    var type = 0

    inner class Donelist {
        var date: Long = 0
        var todoList: List<TodoItem>? = null
    }

    inner class Todolist {
        var date: Long = 0
        var todoList: List<TodoItem>? = null
    }

    inner class TodoItem {
        var completeDate: Long = 0
        var completeDateStr: String? = null
        var content: String? = null
        var date: Long = 0
        var dateStr: String? = null
        var id = 0
        var priority = 0
        var status = 0
        var title: String? = null
        var type = 0
        var userId = 0
    }
}