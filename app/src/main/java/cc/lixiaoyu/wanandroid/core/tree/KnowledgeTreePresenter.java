package cc.lixiaoyu.wanandroid.core.tree;

import java.util.List;

import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.Optional;
import io.reactivex.functions.Consumer;

public class KnowledgeTreePresenter extends KnowledgeTreeContract.Presenter {

    private KnowledgeTreeContract.Model mModel;

    public KnowledgeTreePresenter() {
        mModel = new KnowledgeTreeModel();
    }

    @Override
    public void getKnowledgeTreeData() {

        mModel.getKnowledgeTreeData()
                .subscribe(new Consumer<Optional<List<PrimaryClass>>>() {
                    @Override
                    public void accept(Optional<List<PrimaryClass>> primaryClassList) throws Exception {
                        //回调给View层进行数据展示
                        getView().showKnowledgeTreeData(primaryClassList.getIncludeNull());
                    }
                });
    }

    @Override
    public void openPrimaryClassDetail(PrimaryClass primaryClass) {
        getView().showOpenPrimaryClassDetail(primaryClass);
    }

    @Override
    public void start() {
        getKnowledgeTreeData();
    }
}
