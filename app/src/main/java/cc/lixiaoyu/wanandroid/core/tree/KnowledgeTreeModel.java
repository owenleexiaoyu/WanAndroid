package cc.lixiaoyu.wanandroid.core.tree;

import java.util.List;

import cc.lixiaoyu.wanandroid.api.WanAndroidService;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.core.tree.KnowledgeTreeContract;
import cc.lixiaoyu.wanandroid.util.BaseModelFactory;
import cc.lixiaoyu.wanandroid.util.RetrofitHelper;
import io.reactivex.Observable;

public class KnowledgeTreeModel implements KnowledgeTreeContract.Model {
    private WanAndroidService mService;

    public KnowledgeTreeModel(){
        mService = RetrofitHelper.getInstance().getWanAndroidService();
    }
    @Override
    public Observable<List<PrimaryClass>> getKnowledgeTreeData() {
        return BaseModelFactory.compose(mService.getKnowledgeTreeData());
    }
}
