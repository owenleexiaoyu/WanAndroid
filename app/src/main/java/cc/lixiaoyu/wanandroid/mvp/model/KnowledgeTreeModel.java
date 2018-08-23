package cc.lixiaoyu.wanandroid.mvp.model;

import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.mvp.contract.KnowledgeTreeContract;
import cc.lixiaoyu.wanandroid.util.RetrofitUtil;
import retrofit2.Call;

public class KnowledgeTreeModel implements KnowledgeTreeContract.Model {
    private WanAndroidService mService;

    public KnowledgeTreeModel(){
        mService = RetrofitUtil.getWanAndroidService();
    }
    @Override
    public Call<WanAndroidResult<List<PrimaryClass>>> getKnowledgeTreeData() {
        return mService.getKnowledgeTreeData();
    }
}
