package cc.lixiaoyu.wanandroid.core.todo.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import cc.lixiaoyu.wanandroid.R
import cc.lixiaoyu.wanandroid.core.todo.model.TodoEntity.TodoItem
import cc.lixiaoyu.wanandroid.core.todo.adapter.TodoPagerAdapter
import cc.lixiaoyu.wanandroid.core.todo.vm.TodoViewModel
import cc.lixiaoyu.wanandroid.databinding.ActivityTodoBinding

class TodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var pagerAdapter: TodoPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        initView()
    }

    private fun initView() {
        binding.todoToolbar.title = getString(R.string.todo_tool)
        setSupportActionBar(binding.todoToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        viewModel.typeIndex.observe(this) {
            binding.todoTypeGroup.children.iterator().forEach {  view ->
                (view as? RadioButton)?.setTextColor(ContextCompat.getColor(this, R.color.Accent))
            }
            (binding.todoTypeGroup[it] as? RadioButton)?.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        }
        binding.todoTypeGroup.setOnCheckedChangeListener { _, checkedId ->
            val categoryIndex = when (checkedId) {
                R.id.todo_type_default -> 0
                R.id.todo_type_work -> 1
                R.id.todo_type_learn -> 2
                R.id.todo_type_life -> 3
                else -> 0
            }
            viewModel.setTypeIndex(categoryIndex)
            viewModel.getTodoLists(categoryIndex)
        }
        pagerAdapter = TodoPagerAdapter(this)
        binding.todoViewpager.adapter = pagerAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    companion object {
        const val TODO_STATUS_UNDONE = 0
        const val TODO_STATUS_DONE = 1
    }
}