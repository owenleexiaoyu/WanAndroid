package cc.lixiaoyu.wanandroid.core.projectdata;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.ProjectDataAdapter;
import cc.lixiaoyu.wanandroid.base.MVPBasePageFragment;
import cc.lixiaoyu.wanandroid.entity.ProjectPage;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.ui.activity.ArticleDetailActivity;

public class ProjectDataFragment extends MVPBasePageFragment<ProjectDataContract.Presenter>
        implements ProjectDataContract.View{
    private static final String ARGUMENTS_KEY = "projecttitle";

    @BindView(R.id.fproject_data_smartrefreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.fproject_data_recyclerview)
    RecyclerView mRecyclerView;

    private ProjectTitle mTitle = null;
    private ProjectDataAdapter mAdapter;

    public static ProjectDataFragment newInstance(ProjectTitle title){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENTS_KEY, title);
        ProjectDataFragment fragment = new ProjectDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = (ProjectTitle) bundle.getSerializable(ARGUMENTS_KEY);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mAdapter = new ProjectDataAdapter(R.layout.item_recyclerview_project_data,
                new ArrayList<ProjectPage.ProjectData>(0));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProjectPage.ProjectData article = (ProjectPage.ProjectData) adapter.getData().get(position);
                mPresenter.openArticleDetail(article);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //点击收藏按钮
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRefreshLayout.setRefreshHeader(new MaterialHeader(getActivity()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //强制刷新数据
                prepareFetchData(true);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mPresenter.loadMoreProjectArticlesByCid(mTitle.getId() + "");
            }
        });
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_project_data;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getProjectArticlesByCid(mTitle.getId()+"");
    }

    @Override
    protected ProjectDataContract.Presenter createPresenter() {
        return new ProjectDataPresenter();
    }

    @Override
    public void fetchData() {
        mRefreshLayout.autoRefresh();
        mPresenter.getProjectArticlesByCid(mTitle.getId()+"");
    }

    @Override
    public void showProjectArticlesByCid(List<ProjectPage.ProjectData> dataList) {
        mAdapter.replaceData(dataList);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showOpenArticleDetail(ProjectPage.ProjectData data) {
        //携带Title和URL跳转到ArticleDetailActivity
        ArticleDetailActivity.actionStart(getActivity(), data.getTitle(), data.getLink());
    }

    @Override
    public void showCollectArticle(ProjectPage.ProjectData article) {

    }

    @Override
    public void showCancelCollectArticle(ProjectPage.ProjectData article) {

    }

    @Override
    public void showLoadMoreProjectArticleByCid(List<ProjectPage.ProjectData> dataList, boolean success) {
        //如果成功
        if (success) {
            mAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
        }
        //如果失败
        else {
            mRefreshLayout.finishLoadMore(false);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
