package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.ArticleAdapter;
import cc.lixiaoyu.wanandroid.base.BasePageFragment;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.mvp.contract.SubClassContract;

public class SubClassFragment extends BasePageFragment implements SubClassContract.View{

    private PrimaryClass.SubClass mSubClass;
    private static final String ARGUMENTS_KEY = "subclass";
    private Unbinder unbinder;
    @BindView(R.id.fsubclass_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fsubclass_smartrefreshlayout)
    SmartRefreshLayout mRefreshLayout;
    private ArticleAdapter mAdapter;
    private List<ArticlePage.Article> mArticleList;

    private SubClassContract.Presenter mPresenter;

    public static SubClassFragment newInstance(PrimaryClass.SubClass subClass){
        Bundle bundle = new Bundle();
        SubClassFragment fragment = new SubClassFragment();
        bundle.putSerializable(ARGUMENTS_KEY,subClass);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!= null){
            mSubClass = (PrimaryClass.SubClass) bundle.getSerializable(ARGUMENTS_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subclass_article, container, false);
        unbinder = ButterKnife.bind(this, view);

        mAdapter = new ArticleAdapter(R.layout.item_recyclerview_article,
                new ArrayList<ArticlePage.Article>(0));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticlePage.Article article = mAdapter.getData().get(position);
                mPresenter.openArticleDetail(article);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //
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
                mPresenter.loadMoreArticleByCid(mSubClass.getId()+"");
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除Butterknife绑定
        unbinder.unbind();
    }

    @Override
    public void fetchData() {
        showRefreshing();
        mPresenter.getArticleListByCid(mSubClass.getId()+"");
    }

    @Override
    public void showArticleListByCid(List<ArticlePage.Article> articles) {

    }

    @Override
    public void showOpenArticleDetail(ArticlePage.Article article) {

    }

    @Override
    public void showCollectArticle(ArticlePage.Article article) {

    }

    @Override
    public void showCancelCollectArticle(ArticlePage.Article article) {

    }

    @Override
    public void showRefreshing() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void completeRefresh() {
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void setPresenter(SubClassContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
