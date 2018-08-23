package cc.lixiaoyu.wanandroid.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.KnowledgeTreeContract;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import cc.lixiaoyu.wanandroid.util.ToastUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowledgeTreePresenter implements KnowledgeTreeContract.Presenter {
    private KnowledgeTreeContract.View mView;
    private KnowledgeTreeContract.Model mModel;

    public KnowledgeTreePresenter(KnowledgeTreeContract.View view,
                                  KnowledgeTreeContract.Model model){
        mView = view;
        mModel = model;
        mView.setPresenter(this);
    }
    @Override
    public void getKnowledgeTreeData() {
        Call<WanAndroidResult<List<PrimaryClass>>> call = mModel.getKnowledgeTreeData();
        call.enqueue(new Callback<WanAndroidResult<List<PrimaryClass>>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<List<PrimaryClass>>> call,
                                   Response<WanAndroidResult<List<PrimaryClass>>> response) {
                WanAndroidResult<List<PrimaryClass>> result = response.body();
                if(result.getErrorCode() == 0){
                    //回调给View层进行数据展示
                    mView.showKnowledgeTreeData(result.getData());
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<List<PrimaryClass>>> call, Throwable t) {
                ToastUtil.showToast("出错了");
            }
        });
    }

    @Override
    public void openPrimaryClassDetail(PrimaryClass primaryClass) {
        mView.showOpenPrimaryClassDetail(primaryClass);
    }

    @Override
    public void start() {
        getKnowledgeTreeData();
    }
}
