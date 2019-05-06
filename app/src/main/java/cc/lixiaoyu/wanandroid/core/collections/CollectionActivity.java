package cc.lixiaoyu.wanandroid.core.collections;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import cc.lixiaoyu.wanandroid.adapter.CollectionAdapter;
import cc.lixiaoyu.wanandroid.base.MVPBaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.entity.CollectionPage;
import cc.lixiaoyu.wanandroid.ui.activity.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.util.ToastUtil;

public class CollectionActivity extends MVPBaseSwipeBackActivity<CollectionContract.Presenter>
        implements CollectionContract.View {

    private static final String TAG = "CollectionActivity";

    //控件
    @BindView(R.id.collection_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.collection_refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.collection_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.collection_btn_up)
    FloatingActionButton mBtnUp;

    //Adapter
    private CollectionAdapter mAdapter;

    @Override
    protected CollectionContract.Presenter createPresenter() {
        return new CollectionPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        //设置返回按钮
        mToolbar.setTitle("我的收藏");
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        mAdapter = new CollectionAdapter(R.layout.item_recyclerview_collection,
                new ArrayList<CollectionPage.CollectionArticle>(0));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CollectionPage.CollectionArticle article = mAdapter.getData().get(position);
                mPresenter.openArticleDetail(article);
            }
        });
        //设置取消收藏按钮的事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CollectionPage.CollectionArticle article = mAdapter.getItem(position);
                Log.e(TAG, "onItemChildClick: "+article.getTitle());
                mPresenter.unCollectArticle(position, article.getId());
            }
        });

        //设置RecyclerView的Adapter和布局管理器
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置SmartRefreshLayout的Header和Footer
        mRefreshLayout.setRefreshHeader(new MaterialHeader(this));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        //设置下拉刷新和上拉加载更多的监听器
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPresenter.getCollectionArticleList();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mPresenter.loadMoreCollectionArticle();
            }
        });

        //点击按钮回到列表顶部
        mBtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    protected int attachLayout() {
        return R.layout.activity_collection;
    }

    @Override
    public void showCollectionArticleList(List<CollectionPage.CollectionArticle> articleList) {
        mAdapter.replaceData(articleList);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showLoadMoreCollectionArticle(boolean success, List<CollectionPage.CollectionArticle> articleList) {
        if(success){
            mAdapter.addData(articleList);
            mRefreshLayout.finishLoadMore();
        }
    }



    @Override
    public void showUnCollectArticle(boolean success ,int position) {
        if(success){
            ToastUtil.showToast("取消收藏成功");
            mAdapter.remove(position);
        }else{
            ToastUtil.showToast("取消收藏失败");
        }
    }

    @Override
    public void showOpenArticleDetail(CollectionPage.CollectionArticle article) {
        ArticleDetailActivity.actionStart(CollectionActivity.this,
                article.getTitle(), article.getLink());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
