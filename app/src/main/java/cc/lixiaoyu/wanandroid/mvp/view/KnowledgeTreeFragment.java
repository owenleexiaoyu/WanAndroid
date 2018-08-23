package cc.lixiaoyu.wanandroid.mvp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.SystemAdapter;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.mvp.contract.KnowledgeTreeContract;
import cc.lixiaoyu.wanandroid.ui.activity.SubClassActivity;


public class KnowledgeTreeFragment extends Fragment implements KnowledgeTreeContract.View{
    private static final String TAG = "KnowledgeTreeFragment";
    @BindView(R.id.fsystem_recyclerview)
    RecyclerView mRecyclerView;
    private SystemAdapter mAdapter;

    private KnowledgeTreeContract.Presenter mPresenter;
    private Unbinder mUnbinder;

    public static KnowledgeTreeFragment newInstanse(){
        return new KnowledgeTreeFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system,container, false);
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
        return view;
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
    public void setPresenter(KnowledgeTreeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
