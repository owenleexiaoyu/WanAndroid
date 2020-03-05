package cc.lixiaoyu.wanandroid.core.tree;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.SystemAdapter;
import cc.lixiaoyu.wanandroid.base.MVPBaseFragment;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.ui.activity.SubClassActivity;


public class KnowledgeTreeFragment extends MVPBaseFragment<KnowledgeTreeContract.Presenter>
        implements KnowledgeTreeContract.View{

    private static final String TAG = "KnowledgeTreeFragment";
    @BindView(R.id.fsystem_recyclerview)
    RecyclerView mRecyclerView;
    private SystemAdapter mAdapter;
    private Unbinder mUnbinder;

    public static KnowledgeTreeFragment newInstanse(){
        return new KnowledgeTreeFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mUnbinder = ButterKnife.bind(this, view);

        mAdapter = new SystemAdapter(R.layout.item_system_one,
                new ArrayList<PrimaryClass>(0));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PrimaryClass pc = mAdapter.getData().get(position);
                mPresenter.openPrimaryClassDetail(pc);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_system;
    }

    @Override
    public void showKnowledgeTreeData(List<PrimaryClass> primaryClassList) {
        mAdapter.addData(primaryClassList);
    }

    @Override
    public void showOpenPrimaryClassDetail(PrimaryClass primaryClass) {
        SubClassActivity.actionStart(getActivity(), primaryClass);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    protected KnowledgeTreeContract.Presenter createPresenter() {
        return new KnowledgeTreePresenter();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 回到列表顶部
     */
    public void jumpToListTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }
}
