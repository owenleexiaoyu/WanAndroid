package cc.lixiaoyu.wanandroid.core.todo.ui

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity
import cc.lixiaoyu.wanandroid.core.todo.vm.TodoViewModel
import cc.lixiaoyu.wanandroid.util.ToastUtil

open class BaseTodoFragment: Fragment() {

    protected val viewModel: TodoViewModel by lazy { ViewModelProvider(requireActivity())[TodoViewModel::class.java] }

    /**
     * 显示修改todoitem的对话框
     */
    protected fun showUpdateTodoDialog(item: TodoEntity.TodoItem) {
        val ctx = context ?: return
        val dialogView = LayoutInflater.from(ctx).inflate(R.layout.layout_dialog_edit_todo, null)
        val builder = AlertDialog.Builder(ctx)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()
        val etTitle = dialogView.findViewById<EditText>(R.id.dlog_edit_todo_title)
        val etContent = dialogView.findViewById<EditText>(R.id.dlog_edit_todo_content)
        val tvTime = dialogView.findViewById<TextView>(R.id.dlog_edit_todo_time)
        val btnCancel = dialogView.findViewById<Button>(R.id.dlog_edit_todo_cancel)
        val btnEdit = dialogView.findViewById<Button>(R.id.dlog_edit_todo_edit)
        etTitle.setText(item.title)
        etContent.setText(item.content)
        tvTime.text = "时间：${item.dateStr}"
        tvTime.setOnClickListener {
            val pickerDialog = DatePickerDialog(
                ctx,
                DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT
            )
            pickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                tvTime.text = "时间：" + year + "-" + (month + 1) + "-" + dayOfMonth
            }
            pickerDialog.show()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        btnEdit.setOnClickListener(View.OnClickListener {
            val title = etTitle.text.toString().trim { it <= ' ' }
            if (title.isEmpty()) {
                ToastUtil.showToast("标题不能为空")
                return@OnClickListener
            }
            val content = etContent.text.toString().trim { it <= ' ' }
            val text = tvTime.text.toString().trim { it <= ' ' }
            val index = text.indexOf("：") + 1
            val dateStr = text.substring(index)
            item.title = title
            item.content = content
            item.dateStr = dateStr
            viewModel.updateTodoItem(item)
            dialog.dismiss()
        })
    }

    protected fun showDeleteTodoDialog(item: TodoEntity.TodoItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("提示")
            .setMessage("确认删除待办事项吗？")
            .setCancelable(true)
            .setPositiveButton(getString(R.string.confirm)) { _, _ -> viewModel.deleteTodoItem(item) }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}