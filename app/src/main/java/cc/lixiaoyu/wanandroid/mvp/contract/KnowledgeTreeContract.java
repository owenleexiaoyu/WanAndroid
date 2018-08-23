package cc.lixiaoyu.wanandroid.mvp.contract;

import java.util.List;

import cc.lixiaoyu.wanandroid.base.BaseModel;
import cc.lixiaoyu.wanandroid.base.BasePresenter;
import cc.lixiaoyu.wanandroid.base.BaseView;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import retrofit2.Call;

public interface KnowledgeTreeContract {
    interface Model extends BaseModel{
        Call<WanAndroidResult<List<PrimaryClass>>> getKnowledgeTreeData();
    }

    interface View extends BaseView<Presenter>{
        /**
         * 显示知识体系数据
         * @param primaryClassList
         */
        void showKnowledgeTreeData(List<PrimaryClass> primaryClassList);

        /**
         * 显示打开一个一级分类详情，查看二级分类及文章
         * @param primaryClass
         */
        void showOpenPrimaryClassDetail(PrimaryClass primaryClass);
    }

    interface Presenter extends BasePresenter{
        /**
         * 获取知识体系数据，按一级分类进行分组
         */
        void getKnowledgeTreeData();

        /**
         * 打开一个一级分类详情，查看二级分类及文章
         * @param primaryClass
         */
        void openPrimaryClassDetail(PrimaryClass primaryClass);
    }
}
