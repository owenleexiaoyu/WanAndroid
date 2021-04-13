package cc.lixiaoyu.wanandroid.core.search;

import android.content.DialogInterface;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.base.MVPBaseSwipeBackActivity;
import cc.lixiaoyu.wanandroid.entity.HotKey;
import cc.lixiaoyu.wanandroid.core.detail.ArticleDetailActivity;
import cc.lixiaoyu.wanandroid.core.search.result.SearchResultActivity;

public class SearchActivity extends MVPBaseSwipeBackActivity<SearchContract.Presenter>
        implements SearchContract.View{

    @BindView(R.id.search_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_et_wordinput)
    EditText mEtInput;
    @BindView(R.id.search_btn_search)
    Button mBtnSearch;
    @BindView(R.id.search_btn_clearHistory)
    Button mBtnClearHistory;

    @BindView(R.id.search_tf_history)
    TagFlowLayout mTfHistory;
    @BindView(R.id.search_tf_hotkeys)
    TagFlowLayout mTfHotkeys;
    @BindView(R.id.search_tf_commonsite)
    TagFlowLayout mTfCommonSite;

    @Override
    protected void initData() {
        mPresenter.start();
    }

    @Override
    protected void initView() {
        //设置返回按钮
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //设置搜索按钮的点击事件
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = mEtInput.getText().toString().trim();
                if(!TextUtils.isEmpty(word)){
                    mPresenter.searchArticle(word);
                }
                mEtInput.setText("");
            }
        });
        //设置清空历史记录按钮的点击事件
        mBtnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClearHistoryDialog();
            }
        });
    }

    /**
     * 显示清空历史记录的弹窗
     */
    private void showClearHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        AlertDialog dialog = builder.setTitle("提示")
                .setMessage("确认清空历史记录吗")
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.clearHistory();
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

    @Override
    protected int attachLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showSearchHistory(final List<String> histroyWords) {
        mTfHistory.setAdapter(new TagAdapter<String>(histroyWords){

            @Override
            public View getView(FlowLayout parent, int position, String historyWord) {
                TextView tv = (TextView) LayoutInflater
                        .from(SearchActivity.this)
                        .inflate(R.layout.layout_nav_item, mTfHistory, false);
                tv.setText(historyWord);
                return tv;
            }
        });
        mTfHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String historyWord = histroyWords.get(position);
                showSearchResult(historyWord);
                return true;
            }
        });
    }

    @Override
    public void showHotkey(final List<HotKey> hotKeys) {
        mTfHotkeys.setAdapter(new TagAdapter<HotKey>(hotKeys){

            @Override
            public View getView(FlowLayout parent, int position, HotKey hotKey) {
                TextView tv = (TextView) LayoutInflater
                        .from(SearchActivity.this)
                        .inflate(R.layout.layout_nav_item, mTfHotkeys, false);
                tv.setText(hotKey.getName());
                return tv;
            }
        });
        mTfHotkeys.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String hotKey = hotKeys.get(position).getName();
                showSearchResult(hotKey);
                return true;
            }
        });
    }

    @Override
    public void showCommonSite(final List<WebSite> webSites) {
        mTfCommonSite.setAdapter(new TagAdapter<WebSite>(webSites){

            @Override
            public View getView(FlowLayout parent, int position, WebSite webSite) {
                TextView tv = (TextView) LayoutInflater
                        .from(SearchActivity.this)
                        .inflate(R.layout.layout_nav_item, mTfCommonSite, false);
                tv.setText(webSite.getName());
                return tv;
            }
        });
        mTfCommonSite.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                WebSite webSite = webSites.get(position);
                ArticleDetailActivity.actionStart(SearchActivity.this,
                        webSite.toDetailParam());
                return true;
            }
        });
    }

    @Override
    public void showSearchResult(String keyword) {
        //打开搜索结果页
        SearchResultActivity.actionStart(this, keyword);
        //关闭当前页面
        finish();
    }
}
