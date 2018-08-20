package cc.lixiaoyu.wanandroid.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.lixiaoyu.wanandroid.R;
import cc.lixiaoyu.wanandroid.adapter.SystemAdapter;
import cc.lixiaoyu.wanandroid.entity.PrimaryClass;
import cc.lixiaoyu.wanandroid.entity.WanAndroidResult;
import cc.lixiaoyu.wanandroid.service.WanAndroidService;
import cc.lixiaoyu.wanandroid.util.AppConst;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SystemFragment extends Fragment {
    private static final String TAG = "SystemFragment";
    @BindView(R.id.fsystem_recyclerview)
    RecyclerView mRecyclerView;
    private List<PrimaryClass> mDataList;
    private SystemAdapter systemAdapter;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    systemAdapter.notifyDataSetChanged();
                    Log.d(TAG, "handleMessage: 成功了");
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system,container, false);
        ButterKnife.bind(this, view);
        initData();
        systemAdapter = new SystemAdapter(getActivity(), mDataList);
        systemAdapter.setItemClickListener(new SystemAdapter.OnSystemItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(systemAdapter);
        return view;
    }

    private void initData(){
        mDataList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConst.WANANDROID_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WanAndroidService service = retrofit.create(WanAndroidService.class);
        Call<WanAndroidResult<List<PrimaryClass>>> call = service.getSystemData();
        call.enqueue(new Callback<WanAndroidResult<List<PrimaryClass>>>() {
            @Override
            public void onResponse(Call<WanAndroidResult<List<PrimaryClass>>> call,
                                   Response<WanAndroidResult<List<PrimaryClass>>> response) {
                WanAndroidResult<List<PrimaryClass>> result = response.body();
                if(result.getErrorCode() == 0){
                    mDataList.addAll(result.getData());
                    mHandler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Call<WanAndroidResult<List<PrimaryClass>>> call, Throwable t) {
                Toast.makeText(getActivity(), "出错了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
