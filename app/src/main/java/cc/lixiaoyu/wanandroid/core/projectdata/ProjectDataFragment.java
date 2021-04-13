package cc.lixiaoyu.wanandroid.core.projectdata;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import cc.lixiaoyu.wanandroid.base.MVPBasePageFragment;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.ProjectTitle;
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.core.login.LoginActivity;
import cc.lixiaoyu.wanandroid.util.ToastUtil;

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
                new ArrayList<>());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Article article = (Article) adapter.getData().get(position);
                mPresenter.openArticleDetail(article);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //点击收藏按钮
                //判断是否用户登陆
                if(!mPresenter.getDataManager().getLoginState()){
                    //未登录，前往登陆页面进行登陆操作
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                }else{
                    //登陆后，可以进行文章的收藏与取消收藏操作
                    //如果文章已经被收藏了，就取消收藏，如果没有收藏，就收藏
                    if(mAdapter.getItem(position).isCollect()){
                        mPresenter.cancelCollectArticle(position, mAdapter.getItem(position));
                    }else{
                        mPresenter.collectArticle(position, mAdapter.getItem(position));
                    }
                }
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
    public void showProjectArticlesByCid(List<Article> dataList) {
        mAdapter.replaceData(dataList);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showOpenArticleDetail(Article article) {
        //携带Title和URL跳转到ArticleDetailActivity
        ArticleDetailActivity.actionStart(getActivity(), article.toDetailParam());
    }

    @Override
    public void showCollectArticle(boolean success, int position) {
        if(success){
            ToastUtil.showToast("收藏文章成功");
            mAdapter.getData().get(position).setCollect(true);
            mAdapter.notifyDataSetChanged();
        }else{
            ToastUtil.showToast("收藏文章失败");
        }
    }

    @Override
    public void showCancelCollectArticle(boolean success, int position) {
        if(success){
            ToastUtil.showToast("取消收藏文章成功");
            mAdapter.getData().get(position).setCollect(false);
            mAdapter.notifyDataSetChanged();
        }else{
            ToastUtil.showToast("取消收藏文章失败");
        }
    }

    @Override
    public void showLoadMoreProjectArticleByCid(List<Article> dataList, boolean success) {
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

    /**
     * 回到列表顶部
     */
    public void jumpToListTop(){
        mRecyclerView.smoothScrollToPosition(0);
    }
}
