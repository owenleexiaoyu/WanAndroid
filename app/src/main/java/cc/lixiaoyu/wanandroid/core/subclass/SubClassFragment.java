package cc.lixiaoyu.wanandroid.core.subclass;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.ArticleAdapter;
import cc.lixiaoyu.wanandroid.base.mvp.MVPBasePageFragment;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity;
import cc.lixiaoyu.wanandroid.util.ToastUtil;

public class SubClassFragment extends MVPBasePageFragment<SubClassContract.Presenter>
        implements SubClassContract.View {

    private PrimaryClass.SubClass mSubClass;
    private static final String ARGUMENTS_KEY = "subclass";
    @BindView(R.id.fsubclass_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.fsubclass_smartrefreshlayout)
    SmartRefreshLayout mRefreshLayout;
    private ArticleAdapter mAdapter;

    public static SubClassFragment newInstance(PrimaryClass.SubClass subClass) {
        Bundle bundle = new Bundle();
        SubClassFragment fragment = new SubClassFragment();
        bundle.putSerializable(ARGUMENTS_KEY, subClass);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSubClass = (PrimaryClass.SubClass) bundle.getSerializable(ARGUMENTS_KEY);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mAdapter = new ArticleAdapter(R.layout.item_recyclerview_article,
                new ArrayList<>(0));
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            Article article = mAdapter.getData().get(position);
            mPresenter.openArticleDetail(article);
        });
        mAdapter.setOnItemChildClickListener((adapter, view12, position) -> {
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
        mRefreshLayout.setOnLoadMoreListener(refreshLayout -> mPresenter.loadMoreArticleByCid(mSubClass.getId() + ""));
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_subclass_article;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getArticleListByCid(mSubClass.getId() + "");
    }

    @Override
    protected SubClassContract.Presenter createPresenter() {
        return new SubClassPresenter();
    }

    @Override
    public void fetchData() {
        mRefreshLayout.autoRefresh();
        mPresenter.getArticleListByCid(mSubClass.getId() + "");
    }

    @Override
    public void showArticleListByCid(List<Article> articles) {
        mAdapter.replaceData(articles);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showOpenArticleDetail(Article article) {
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
    public void showLoadMoreArticleByCid(List<Article> articles, boolean success) {
        //如果成功
        if (success) {
            mAdapter.addData(articles);
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
