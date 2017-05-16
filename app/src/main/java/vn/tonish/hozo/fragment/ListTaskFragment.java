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
import vn.tonish.hozo.database.manager.UserManager;
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
        option.put("category_id", "1");
        option.put("currency", "VND");
        option.put("min_worker_rate", "150000");
        option.put("max_worker_rate", "200000");
        option.put("start_daytime", "10:00:00+07:00");
        option.put("end_daytime", "17:00:00+07:00");
        option.put("city", "Hà Nội");
        option.put("gender", "male");
        option.put("min_age", "18");
        option.put("max_age", "25");
        option.put("status", "open");
        option.put("poster_id", "123");
        option.put("tasker_id", "123");
        option.put("page", "2");
        option.put("sort_by", "worker_rate");
        option.put("ascending", "true");
        ApiClient.getApiService().getDetailTask(UserManager.getUserToken(getContext()), option).enqueue(new Callback<List<TaskResponse>>() {
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