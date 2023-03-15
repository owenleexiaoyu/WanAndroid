package cc.lixiaoyu.wanandroid.core.tree;

import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.util.network.BaseModelFactory;
import cc.lixiaoyu.wanandroid.entity.Optional;
import cc.lixiaoyu.wanandroid.util.network.RetrofitManager;
import io.reactivex.Observable;

public class KnowledgeTreeModel implements KnowledgeTreeContract.Model {
    private WanAndroidService mService;

    public KnowledgeTreeModel(){
        mService = RetrofitManager.getInstance().getWanAndroidService();
    }
    @Override
    public Observable<Optional<List<PrimaryClass>>> getKnowledgeTreeData() {
        return BaseModelFactory.compose(mService.getKnowledgeTreeData());
    }
}
