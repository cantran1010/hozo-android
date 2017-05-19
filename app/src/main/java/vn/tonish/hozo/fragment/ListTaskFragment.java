package vn.tonish.hozo.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.TaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;

import static vn.tonish.hozo.R.id.lv_list;

/**
 * Created by Can Tran on 04/05/2017.
 */

public class ListTaskFragment extends BaseFragment {
    private final static String TAG = ListTaskFragment.class.getSimpleName();
    private RecyclerView rcvTask;
    private TaskAdapter taskAdapter;
    private LinearLayoutManager lvManager;
    private List<TaskResponse> taskList;
    private final static int limit = 20;
    private String lastTime;

    @Override
    protected int getLayout() {
        return R.layout.fragment_list_task;
    }

    @Override
    protected void initView() {
        rcvTask = (RecyclerView) findViewById(lv_list);
        lvManager = new LinearLayoutManager(getActivity());
        taskList = new ArrayList<>();
        rcvTask.setLayoutManager(lvManager);
        taskAdapter = new TaskAdapter(getActivity(), taskList);
    }

    @Override
    protected void initData() {
        rcvTask.setAdapter(taskAdapter);
        getReviews();

    }

    @Override
    protected void resumeData() {

    }

    public void getReviews() {
        ProgressDialogUtils.showProgressDialog(getContext());
        Map<String, String> option = new HashMap<>();
        option = DataParse.setParameterGetTasks(SettingManager.getSettingEntiny(), "distance", String.valueOf(limit), lastTime, "");
        ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), option).enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskList.clear();
                    taskList.addAll(response.body());
                    taskAdapter.notifyDataSetChanged();
                    LogUtils.d(TAG, "getTasksonResponse size : " + response.body());
                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.d(TAG, "onFailure : " + t.getMessage());
            }
        });
    }
}