package cc.lixiaoyu.wanandroid.core.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity
import cc.lixiaoyu.wanandroid.core.todo.adapter.TodoFinishedAdapter
import cc.lixiaoyu.wanandroid.core.todo.adapter.TodoFinishedItemClickListener
import cc.lixiaoyu.wanandroid.databinding.LayoutTodoDoneBinding
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.constant.RefreshState

class TodoFinishedFragment: BaseTodoFragment() {

    private lateinit var binding: LayoutTodoDoneBinding
    private val adapter: TodoFinishedAdapter = TodoFinishedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutTodoDoneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        binding.ldoneSmartrefreshlayout.apply {
            setRefreshHeader(MaterialHeader(context))
            setOnRefreshListener {
                viewModel.getTodoLists(viewModel.typeIndex.value ?: 0)
            }
        }
        adapter.itemClickListener = object : TodoFinishedItemClickListener {
            override fun onDelete(item: TodoEntity.TodoItem, position: Int) {
                this@TodoFinishedFragment.showDeleteTodoDialog(item)
            }

            override fun onUnFinish(item: TodoEntity.TodoItem, position: Int) {
                viewModel.changeTodoItemStatus(item, TodoActivity.TODO_STATUS_UNDONE)
            }

            override fun onUpdate(item: TodoEntity.TodoItem, position: Int) {
                this@TodoFinishedFragment.showUpdateTodoDialog(item)
            }
        }
        binding.ldoneRecyclerview.adapter = adapter
        binding.ldoneRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        viewModel.finishedList.observe(viewLifecycleOwner) {
            adapter.setNewData(it)
            if (it.isEmpty()) {
                binding.ldoneRecyclerview.visibility = View.GONE
                binding.listEmptyView.root.visibility = View.VISIBLE
            } else {
                binding.ldoneRecyclerview.visibility = View.VISIBLE
                binding.listEmptyView.root.visibility = View.GONE
            }
            if (binding.ldoneSmartrefreshlayout.state == RefreshState.Refreshing) {
                binding.ldoneSmartrefreshlayout.finishRefresh()
            }
        }
    }
}