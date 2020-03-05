package cc.lixiaoyu.wanandroid.ui.activity;

import android.content.Context;
import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.SearchResultAdapter;
import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.base.BaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.entity.ArticlePage;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 这个Activity没有使用MVP模式，而是传统的MVC模式
 */
public class SearchResultActivity extends BaseSwipeBackActivity {

    private static final String TAG = "SearchResultActivity";

    //控件
    @BindView(R.id.sresult_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.sreult_refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.sresult_recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.sresult_btn_up)
    FloatingActionButton mBtnUp;

    private SearchResultAdapter mAdapter;

    private WanAndroidService mService;
    //当前的页数
    private int mCurrentPage = 0;
    private String mKeyword;
    @Override
    protected void initData() {
        //从搜索页获得传递过来的关键词参数
        Intent intent = getIntent();
        mKeyword = intent.getStringExtra("key");
        mService = RetrofitHelper.getInstance().getWanAndroidService();
    }

    @Override
    protected void initView() {
        //设置返回按钮
        mToolbar.setTitle("搜索结果："+mKeyword);
        setSupportActionBar(mToolbar);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);

        //初始化Adapter
        mAdapter = new SearchResultAdapter(R.layout.item_recyclerview_search_result,
                new ArrayList<ArticlePage.Article>(0));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticlePage.Article article = mAdapter.getData().get(position);
                ArticleDetailActivity.actionStart(SearchResultActivity.this,
                        article.getTitle(), article.getLink());
            }
        });
        getResultList();
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
                getResultList();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                loadMoreResults();
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
        return R.layout.activity_search_result;
    }

    /**
     * 获取搜索结果，默认展示第0页结果
     */
    private void getResultList(){
        //将当前页置0
        mCurrentPage = 0;
        mService.searchArticle(mCurrentPage, mKeyword)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<WanAndroidResult<ArticlePage>>() {
                    @Override
                    public boolean test(WanAndroidResult<ArticlePage> result) throws Exception {
                        return result.getErrorCode() == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResult<ArticlePage>>() {
                    @Override
                    public void accept(WanAndroidResult<ArticlePage> result) throws Exception {
                        mAdapter.replaceData(result.getData().getArticleList());
                        mRefreshLayout.finishRefresh();
                    }
                });
    }

    /**
     * 加载下一页搜索结果
     */
    private void loadMoreResults(){
        mCurrentPage++;
        mService.searchArticle(mCurrentPage, mKeyword)
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<WanAndroidResult<ArticlePage>>() {
                    @Override
                    public boolean test(WanAndroidResult<ArticlePage> result) throws Exception {
                        return result.getErrorCode() == 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WanAndroidResult<ArticlePage>>() {
                    @Override
                    public void accept(WanAndroidResult<ArticlePage> result) throws Exception {
                        mAdapter.addData(result.getData().getArticleList());
                        mRefreshLayout.finishLoadMore();
                    }
                });
    }

    public static void actionStart(Context context, String key) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra("key", key);
        context.startActivity(intent);
    }
}
