package cc.lixiaoyu.wanandroid.core.collections;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.core.tree.KnowledgeTreeContract;

public interface CollectionContract {
    interface Model extends BaseModel {

    }

    interface View extends BaseView {

    }

    abstract class  Presenter extends BasePresenter<KnowledgeTreeContract.View>{

    }
}
