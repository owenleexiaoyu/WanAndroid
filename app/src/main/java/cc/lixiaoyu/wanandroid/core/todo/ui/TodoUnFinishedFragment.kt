package cc.lixiaoyu.wanandroid.core.todo.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity
import cc.lixiaoyu.wanandroid.core.todo.adapter.TodoUnFinishedAdapter
import cc.lixiaoyu.wanandroid.core.todo.adapter.TodoUnfinishedItemClickListener
import cc.lixiaoyu.wanandroid.databinding.LayoutTodoUndoneBinding
import cc.lixiaoyu.wanandroid.util.ToastUtil
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.constant.RefreshState
import java.text.SimpleDateFormat
import java.util.Date

class TodoUnFinishedFragment: BaseTodoFragment() {

    private lateinit var binding: LayoutTodoUndoneBinding
    private val adapter: TodoUnFinishedAdapter = TodoUnFinishedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutTodoUndoneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.lundoneBtnAdd.setOnClickListener {
            showAddTodoDialog()
        }
        binding.lundoneSmartrefreshlayout.apply {
            setRefreshHeader(MaterialHeader(context))
            setOnRefreshListener {
                viewModel.getTodoLists(viewModel.typeIndex.value ?: 0)
            }
        }
        adapter.itemClickListener = object : TodoUnfinishedItemClickListener {
            override fun onDelete(item: TodoEntity.TodoItem, position: Int) {
                showDeleteTodoDialog(item)
            }

            override fun onFinish(item: TodoEntity.TodoItem, position: Int) {
                viewModel.changeTodoItemStatus(item, TodoActivity.TODO_STATUS_DONE)
            }

            override fun onUpdate(item: TodoEntity.TodoItem, position: Int) {
                this@TodoUnFinishedFragment.showUpdateTodoDialog(item)
            }
        }
        binding.lundoneRecyclerview.adapter = adapter
        binding.lundoneRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel.unfinishedList.observe(viewLifecycleOwner) {
            adapter.setNewData(it)
            if (it.isEmpty()) {
                binding.lundoneRecyclerview.visibility = View.GONE
                binding.listEmptyView.root.visibility = View.VISIBLE
            } else {
                binding.lundoneRecyclerview.visibility = View.VISIBLE
                binding.listEmptyView.root.visibility = View.GONE
            }
            if (binding.lundoneSmartrefreshlayout.state == RefreshState.Refreshing) {
                binding.lundoneSmartrefreshlayout.finishRefresh()
            }
        }
    }

    /**
     * 显示添加todo的对话框
     */
    private fun showAddTodoDialog() {
        val ctx = context ?: return
        val date = Date()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dialogView = LayoutInflater.from(ctx).inflate(R.layout.layout_dialog_add_todo, null)
        val builder = AlertDialog.Builder(ctx)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.show()
        val etTitle = dialogView.findViewById<EditText>(R.id.dlog_add_todo_title)
        val etContent = dialogView.findViewById<EditText>(R.id.dlog_add_todo_content)
        val tvTime = dialogView.findViewById<TextView>(R.id.dlog_add_todo_time)
        val btnCancel = dialogView.findViewById<Button>(R.id.dlog_add_todo_cancel)
        val btnAdd = dialogView.findViewById<Button>(R.id.dlog_add_todo_add)
        val dateStr = format.format(date)
        tvTime.text = "时间：$dateStr"
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
        btnAdd.setOnClickListener(View.OnClickListener {
            val title = etTitle.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(title)) {
                ToastUtil.showToast("标题不能为空")
                return@OnClickListener
            }
            val content = etContent.text.toString().trim { it <= ' ' }
            val text = tvTime.text.toString().trim { it <= ' ' }
            val index = text.indexOf("：") + 1
            val dateStr = text.substring(index)
            val type = viewModel.typeIndex.value ?: 0
            viewModel.addTodoItem(title, content, dateStr, type)
            dialog.dismiss()
        })
    }
}