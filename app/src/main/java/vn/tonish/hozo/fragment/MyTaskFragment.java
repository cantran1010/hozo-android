package vn.tonish.hozo.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import vn.tonish.hozo.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.adapter.MyTaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.TextViewHozo;

import static android.content.ContentValues.TAG;
import static vn.tonish.hozo.utils.Utils.setViewBackground;

/**
 * Created by LongBui on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private TextViewHozo tvWorker, tvPoster;
    private RecyclerView rcvTask;
    private List<TaskResponse> taskResponses = new ArrayList<>();
    private MyTaskAdapter myTaskAdapter;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_my_work;
    }

    @Override
    protected void initView() {
        tvWorker = (TextViewHozo) findViewById(R.id.tv_worker);
        tvPoster = (TextViewHozo) findViewById(R.id.tv_poster);

        rcvTask = (RecyclerView) findViewById(R.id.rcv_task);
    }

    @Override
    protected void initData() {
        tvWorker.setOnClickListener(this);
        tvPoster.setOnClickListener(this);

        getTask("worker");
    }

    @Override
    protected void resumeData() {

    }

    private void getTask(final String role) {
        Map<String, String> params = new HashMap<>();
        params.put("role", "poster");
        LogUtils.d(TAG, "getTask start , param : " + params);

        ApiClient.getApiService().getMyTask(UserManager.getUserToken(), params).enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {

                LogUtils.d(TAG, "getTask code : " + response.code());
                LogUtils.d(TAG, "getTask body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponses = response.body();
                    refreshList();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getTask(role);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTask(role);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {

            }
        });
    }

    private void refreshList() {
        if (myTaskAdapter == null) {
            myTaskAdapter = new MyTaskAdapter(getActivity(), taskResponses);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            rcvTask.setLayoutManager(linearLayoutManager);
            rcvTask.setAdapter(myTaskAdapter);

//            lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//                @Override
//                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//
//                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);
//
//                    if (isLoadingMoreFromDb) getCacheDataPage();
//                    if (isLoadingMoreFromServer) getNotifications(true);
//
//                }
//            });

        } else {
            myTaskAdapter.notifyDataSetChanged();
        }

        LogUtils.d(TAG, "refreshList , taskReponse size : " + taskResponses.size());
    }

    private void selectedTab(int position) {
        if (position == 1) {
            tvWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            setViewBackground(tvWorker, ContextCompat.getDrawable(getContext(), R.drawable.my_task_worker_active));
            tvPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
            setViewBackground(tvPoster, ContextCompat.getDrawable(getContext(), R.drawable.my_task_poster_default));
        } else {
            tvWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.hozo_bg));
            setViewBackground(tvWorker, ContextCompat.getDrawable(getContext(), R.drawable.my_task_worker_default));
            tvPoster.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            setViewBackground(tvPoster, ContextCompat.getDrawable(getContext(), R.drawable.my_task_poster_active));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_worker:
                selectedTab(1);
                break;

            case R.id.tv_poster:
                selectedTab(2);
                break;

        }

    }
}
