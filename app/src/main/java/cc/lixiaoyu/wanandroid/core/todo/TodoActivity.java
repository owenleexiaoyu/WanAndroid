package cc.lixiaoyu.wanandroid.core.todo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResponse;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TodoActivity extends BaseSwipeBackActivity {
    private static final String TAG = "TodoActivity";
    private static final int TODO_STATUS_UNDONE = 0;
    private static final int TODO_STATUS_DONE = 1;

    @BindView(R.id.todo_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.todo_radiogroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.todo_viewpager)
    ViewPager mViewPager;

    private SmartRefreshLayout mUndoneRefreshLayout;
    private SmartRefreshLayout mDoneRefreshLayout;
    private RecyclerView mUndoneRecyclerView;
    private RecyclerView mDoneRecyclerView;
    private TodoUndoneAdapter mUndoneAdapter;
    private TodoDoneAdapter mDoneAdapter;

    private Button mBtnAddTodo;

    private List<RadioButton> buttonList;
    private int mCurrentType = 0;

    private View view1;
    private View view2;
    private List<View> viewList;

    private WanAndroidService mService;

    @Override
    protected void initData() {
        //初始化view数据
        view1 = LayoutInflater.from(this).inflate(R.layout.layout_todo_undone, null);
        view2 = LayoutInflater.from(this).inflate(R.layout.layout_todo_done, null);
        viewList = new ArrayList<>(2);
        viewList.add(view1);
        viewList.add(view2);

        buttonList = new ArrayList<>(4);
        mService = RetrofitManager.getInstance().getWanAndroidService();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onResume() {
        super.onResume();
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton button = (RadioButton) mRadioGroup.getChildAt(i);
            buttonList.add(button);
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.d(TAG, "onCheckedChanged: checkedId="+checkedId);
                switch (checkedId){
                    case R.id.todo_radiobtn_1:
                        mCurrentType = 0;
                        break;
                    case R.id.todo_radiobtn_2:
                        mCurrentType = 1;
                        break;
                    case R.id.todo_radiobtn_3:
                        mCurrentType = 2;
                        break;
                    case R.id.todo_radiobtn_4:
                        mCurrentType = 3;
                        break;
                }
                changeTextColor(mCurrentType);
                getTodoData(mCurrentType);
            }
        });
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("TODO工具");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(viewList.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        });

        initInnerView();
    }

    /**
     * 初始化ViewPager内部两个view的控件和Adapter等
     */
    private void initInnerView() {
        mUndoneRecyclerView = view1.findViewById(R.id.lundone_recyclerview);
        mUndoneAdapter = new TodoUndoneAdapter(R.layout.item_recyclerview_todo_undone,
                new ArrayList<TodoEntity.TodoItem>(0));
        mUndoneAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.layout_list_empty_view, null));
        mUndoneAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_undone_btn_finish:
//                        Log.d(TAG, "onItemClick: 点击了完成");
                        changeTodoItemStatus(mUndoneAdapter.getItem(position), TODO_STATUS_DONE, position);
                        break;
                    case R.id.item_undone_btn_delete:
//                        Log.d(TAG, "onItemClick: 点击了删除");
                        showDeleteTodoDialog(mUndoneAdapter.getItem(position), position);
                        break;
                    case R.id.item_undone_tv_title:
                        showEditTodoDialog(mUndoneAdapter.getItem(position), position);
                        break;
                }
            }
        });
        mUndoneRecyclerView.setAdapter(mUndoneAdapter);
        mUndoneRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUndoneRefreshLayout = view1.findViewById(R.id.lundone_smartrefreshlayout);
        mUndoneRefreshLayout.setRefreshHeader(new MaterialHeader(this));
        mUndoneRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getTodoData(mCurrentType);
            }
        });

        mDoneRecyclerView = view2.findViewById(R.id.ldone_recyclerview);
        mDoneAdapter = new TodoDoneAdapter(R.layout.item_recyclerview_todo_done,
                new ArrayList<TodoEntity.TodoItem>(0));
        mDoneAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.layout_list_empty_view, null));
        mDoneAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_done_btn_unfinish:
//                        Log.d(TAG, "onItemClick: 点击了未完成");
                        changeTodoItemStatus(mDoneAdapter.getItem(position), TODO_STATUS_UNDONE, position);
                        break;
                    case R.id.item_done_btn_delete:
//                        Log.d(TAG, "onItemClick: 点击了删除");
                        showDeleteTodoDialog(mDoneAdapter.getItem(position), position);
                        break;
                }
            }
        });
        mDoneRecyclerView.setAdapter(mDoneAdapter);
        mDoneRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDoneRefreshLayout = view2.findViewById(R.id.ldone_smartrefreshlayout);
        mDoneRefreshLayout.setRefreshHeader(new MaterialHeader(this));
        mDoneRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getTodoData(mCurrentType);
            }
        });

        mBtnAddTodo = view1.findViewById(R.id.lundone_btn_add);
        mBtnAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTodoDialog();
            }
        });
        getTodoData(0);
    }

    /**
     * 显示删除头都item的提示框
     *
     * @param position
     */
    private void showDeleteTodoDialog(final TodoEntity.TodoItem item, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("提示")
                .setMessage("确认删除任务吗")
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTodoItem(item, position);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    /**
     * 删除一条todoitem
     *
     * @param item
     * @param position
     */
    private void deleteTodoItem(TodoEntity.TodoItem item, final int position) {
        final int status = item.getStatus();
        mService.deleteTodoItem(item.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResponse<String>>() {
                    @Override
                    public void accept(WanAndroidResponse<String> result) throws Exception {
                        ToastUtil.showToast("删除任务成功");
                        if (status == TODO_STATUS_UNDONE) {
                            mUndoneAdapter.remove(position);
                        } else if (status == TODO_STATUS_DONE) {
                            mDoneAdapter.remove(position);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast("删除任务失败");
                    }
                });
    }

    /**
     * 改变todoItem的状态，从未完成变为已完成，从已完成变为未完成
     *
     * @param item
     */
    private void changeTodoItemStatus(final TodoEntity.TodoItem item, final int status, final int position) {
        mService.updateTodoItem(item.getId(), item.getTitle(),
                item.getContent(), item.getDateStr(), item.getType(), status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResponse<TodoEntity.TodoItem>>() {
                    @Override
                    public void accept(WanAndroidResponse<TodoEntity.TodoItem> result) throws Exception {
                        if (status == TODO_STATUS_UNDONE) {
                            ToastUtil.showToast("重新添加到任务列表中");
                            //从已完成的列表中删除
                            mDoneAdapter.remove(position);
                            //添加到未完成的列表中
                            mUndoneAdapter.addData(0, result.getData());
                        } else if (status == TODO_STATUS_DONE) {
                            ToastUtil.showToast("已完成任务");
                            //从未完成的列表中删除
                            mUndoneAdapter.remove(position);
                            //添加到已完成的列表中
                            mDoneAdapter.addData(0, result.getData());
                        }
                    }
                });
    }

    /**
     * 更新todoItem的内容
     *
     * @param item
     */
    private void editTodoItem(TodoEntity.TodoItem item, final int position) {
        mService.updateTodoItem(item.getId(), item.getTitle(),
                item.getContent(), item.getDateStr(), item.getType(), item.getStatus())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResponse<TodoEntity.TodoItem>>() {
                    @Override
                    public void accept(WanAndroidResponse<TodoEntity.TodoItem> result) throws Exception {
                        ToastUtil.showToast("任务详情已修改");
                        TodoEntity.TodoItem todoItem = mUndoneAdapter.getData().get(position);
                        todoItem = result.getData();
                        mUndoneAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 显示添加todo的对话框
     */
    private void showAddTodoDialog() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_add_todo, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText etTitle = dialogView.findViewById(R.id.dlog_add_todo_title);
        final EditText etContent = dialogView.findViewById(R.id.dlog_add_todo_content);
        final TextView tvTime = dialogView.findViewById(R.id.dlog_add_todo_time);
        Button btnCancel = dialogView.findViewById(R.id.dlog_add_todo_cancel);
        Button btnAdd = dialogView.findViewById(R.id.dlog_add_todo_add);
        String dateStr = format.format(date);
        tvTime.setText("时间：" + dateStr);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(TodoActivity.this,
                        DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvTime.setText("时间：" + year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                });
                pickerDialog.show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast("标题不能为空");
                    return;
                }
                String content = etContent.getText().toString().trim();
                String text = tvTime.getText().toString().trim();
                int index = text.indexOf("：") + 1;
                String dateStr = text.substring(index);
                int type = mCurrentType;
                addTodoItem(title, content, dateStr, type);
                dialog.dismiss();
            }
        });
    }

    /**
     * 显示修改todoitem的对话框
     */
    private void showEditTodoDialog(final TodoEntity.TodoItem item, final int position) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_edit_todo, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText etTitle = dialogView.findViewById(R.id.dlog_edit_todo_title);
        final EditText etContent = dialogView.findViewById(R.id.dlog_edit_todo_content);
        final TextView tvTime = dialogView.findViewById(R.id.dlog_edit_todo_time);
        Button btnCancel = dialogView.findViewById(R.id.dlog_edit_todo_cancel);
        Button btnEdit = dialogView.findViewById(R.id.dlog_edit_todo_edit);

        etTitle.setText(item.getTitle());
        etContent.setText(item.getContent());
        tvTime.setText("时间：" + item.getDateStr());
        tvTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(TodoActivity.this,
                        DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvTime.setText("时间：" + year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                });
                pickerDialog.show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    ToastUtil.showToast("标题不能为空");
                    return;
                }
                String content = etContent.getText().toString().trim();
                String text = tvTime.getText().toString().trim();
                int index = text.indexOf("：") + 1;
                String dateStr = text.substring(index);
                item.setTitle(title);
                item.setContent(content);
                item.setDateStr(dateStr);
                editTodoItem(item, position);
                dialog.dismiss();
            }
        });
    }

    private void addTodoItem(String title, String content, String time, int type) {
        mService.addTodoItem(title, content, time, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResponse<TodoEntity.TodoItem>>() {
                    @Override
                    public void accept(WanAndroidResponse<TodoEntity.TodoItem> result) throws Exception {
                        mUndoneAdapter.addData(0, result.getData());
                        ToastUtil.showToast("添加成功");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast("添加失败");
                    }
                });
    }

    private void getTodoData(int type) {
        mService.getTodoListByType(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResponse<TodoEntity>>() {
                    @Override
                    public void accept(WanAndroidResponse<TodoEntity> result) throws Exception {
                        TodoEntity entity = result.getData();
                        //结束SmartRefreshLayout的刷新动作
                        mUndoneRefreshLayout.finishRefresh();
                        mDoneRefreshLayout.finishRefresh();
                        showTodoDataInList(entity);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToast("出错了");
                    }
                });
    }

    private void showTodoDataInList(TodoEntity entity) {
        //清空Adapter中的数据源
        mUndoneAdapter.getData().clear();
        mUndoneAdapter.notifyDataSetChanged();
        mDoneAdapter.getData().clear();
        mDoneAdapter.notifyDataSetChanged();
        List<TodoEntity.Todolist> todolist = entity.getTodoList();
        List<TodoEntity.Donelist> donelist = entity.getDoneList();
        int daysCount1 = todolist.size();
        for (int i = daysCount1 - 1; i >= 0; i--) {
            mUndoneAdapter.addData(todolist.get(i).getTodoList());
        }
        int daysCount2 = donelist.size();
        for (int i = daysCount2 - 1; i >= 0; i--) {
            mDoneAdapter.addData(donelist.get(i).getTodoList());
        }
    }

    /**
     * 改变RadioButton中的文字颜色
     */
    private void changeTextColor(int position) {
        for (int i = 0; i < buttonList.size(); i++) {
            if (i != position) {
                buttonList.get(i).setTextColor(getColor(R.color.light_blue));
            } else {
                buttonList.get(i).setTextColor(getColor(android.R.color.white));
            }
        }
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_todo;
    }
}
