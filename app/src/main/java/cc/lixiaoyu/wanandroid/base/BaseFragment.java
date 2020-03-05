package cc.lixiaoyu.wanandroid.base;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基类Fragment，封装公共逻辑
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(attachLayout(), container, false);
        //绑定butterknife
        unbinder = ButterKnife.bind(this,view);
        initView(view);
        return view;
    }


    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化view
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 加载布局文件
     * @return
     */
    protected abstract int attachLayout();

    /**
     * 解除ButterKnife的绑定
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
            unbinder = null;
        }
    }
}
