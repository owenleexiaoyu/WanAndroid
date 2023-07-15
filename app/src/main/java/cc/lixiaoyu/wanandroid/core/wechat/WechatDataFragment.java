package cc.lixiaoyu.wanandroid.core.wechat;

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
import cc.lixiaoyu.wanandroid.base.mvp.MVPBasePageFragment;
import cc.lixiaoyu.wanandroid.entity.Article;
import cc.lixiaoyu.wanandroid.entity.WechatTitle;
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.core.account.ui.LoginActivity;
import cc.lixiaoyu.wanandroid.util.ToastUtil;

public class WechatDataFragment extends MVPBasePageFragment<WechatDataContract.Presenter>
        implements WechatDataContract.View{
    private static final String ARGUMENTS_KEY = "projecttitle";

    @BindView(R.id.fwechat_data_smartrefreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.fwechat_data_recyclerview)
    RecyclerView mRecyclerView;

    private WechatTitle mTitle = null;
    private WechatDataAdapter mAdapter;

    public static WechatDataFragment newInstance(WechatTitle title){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGUMENTS_KEY, title);
        WechatDataFragment fragment = new WechatDataFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitle = (WechatTitle) bundle.getSerializable(ARGUMENTS_KEY);
        }
        mPresenter.getWechatArticlesById(mTitle.getId());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {
        mAdapter = new WechatDataAdapter(R.layout.item_recyclerview_article,
                new ArrayList<Article>(0));
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
                mPresenter.loadMoreWechatArticlesById(mTitle.getId());
            }
        });
    }

    @Override
    protected int attachLayout() {
        return R.layout.fragment_wechat_data;
    }

    @Override
    protected WechatDataContract.Presenter createPresenter() {
        return new WechatDataPresenter();
    }

    @Override
    public void fetchData() {
        mRefreshLayout.autoRefresh();
        mPresenter.getWechatArticlesById(mTitle.getId());
    }

    @Override
    public void showWechatArticlesById(List<Article> dataList) {
        mAdapter.replaceData(dataList);
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void showOpenArticleDetail(Article data) {
        //携带Title和URL跳转到ArticleDetailActivity
        ArticleDetailActivity.actionStart(getActivity(), data.toDetailParam());
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
    public void showLoadMoreWechatArticleById(List<Article> dataList, boolean success) {
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
