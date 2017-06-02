package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.TaskDetailActivity;
import vn.tonish.hozo.adapter.MyTaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.manager.TaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 6/2/17.
 */

public class MyTaskPosterFragment extends BaseFragment {

    private static final String TAG = MyTaskPosterFragment.class.getSimpleName();
    private RecyclerView rcvTask;
    private List<TaskResponse> taskResponses = new ArrayList<>();
    private MyTaskAdapter myTaskAdapter;
    private String role = Constants.ROLE_POSTER;
    public static final int LIMIT = 15;
    private String sinceStr;
    private Date sinceDate;
    boolean isLoadingMoreFromServer = true;
    boolean isLoadingMoreFromDb = true;
    boolean isLoadingFromServer = false;
    private Call<List<TaskResponse>> call;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_task_poster;
    }

    @Override
    protected void initView() {
        rcvTask = (RecyclerView) findViewById(R.id.rcv_task);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        getCacheData(role, sinceDate);
        getTaskFromServer(role, sinceStr, LIMIT);
    }


    private void getCacheData(String role, Date sinceDateIn) {
        LogUtils.d(TAG, "getCacheData start , role : " + role + " , sinceDate : " + sinceDateIn);
        List<TaskEntity> taskEntities = TaskManager.getTaskSince(sinceDateIn, role);
        if (taskEntities.size() > 0)
            sinceDate = taskEntities.get(taskEntities.size() - 1).getCreatedAt();
        taskResponses.addAll(DataParse.converListTaskEntityToTaskResponse(taskEntities));
        refreshList();
        if (taskResponses.size() < LIMIT) isLoadingMoreFromDb = false;

        LogUtils.d(TAG, "getCacheData , taskResponses : " + taskResponses.toString());
        LogUtils.d(TAG, "getCacheData , taskResponses size : " + taskResponses.size());
    }

    @Override
    protected void resumeData() {

    }

    private void getTaskFromServer(final String role, final String since, final int limit) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        myTaskAdapter.stopLoadMore();

        Map<String, String> params = new HashMap<>();

        params.put("role", role);
        if (since != null) params.put("since", since);
        params.put("limit", limit + "");

        LogUtils.d(TAG, "getTaskFromServer start , param : " + params);

        call = ApiClient.getApiService().getMyTask(UserManager.getUserToken(), params);
        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {

                LogUtils.d(TAG, "getTaskFromServer code : " + response.code());
                LogUtils.d(TAG, "getTaskFromServer body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<TaskResponse> taskResponsesBody = response.body();

                    LogUtils.d(TAG, "getTaskFromServer taskResponsesBody size : " + taskResponsesBody.size());

                    if (taskResponsesBody.size() > 0)
                        sinceStr = taskResponsesBody.get(taskResponsesBody.size() - 1).getCreatedAt();

                    for (int i = 0; i < taskResponsesBody.size(); i++)
                        taskResponsesBody.get(i).setRole(role);

                    for (int i = taskResponsesBody.size() - 1; i >= 0; i--)
                        Utils.checkContainsTaskResponse(taskResponses, taskResponsesBody.get(i));

                    TaskManager.insertTasks(DataParse.convertListTaskResponseToTaskEntity(taskResponsesBody));

                    if (taskResponsesBody.size() < LIMIT) {
                        myTaskAdapter.stopLoadMore();
                        isLoadingMoreFromServer = false;
                    }

                    refreshList();

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getTaskFromServer(role, since, limit);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTaskFromServer(role, since, limit);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                isLoadingFromServer = false;
                onStopRefresh();
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                LogUtils.e(TAG, "getTaskFromServer error : " + t.getMessage());
//                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
//                    @Override
//                    public void onSubmit() {
//                        getTaskFromServer(role, since, limit, query);
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });

                isLoadingFromServer = false;
                myTaskAdapter.stopLoadMore();
                if (myTaskAdapter == null) myTaskAdapter.notifyDataSetChanged();
                onStopRefresh();
            }
        });
    }

    private void refreshList() {
        if (myTaskAdapter == null) {
            myTaskAdapter = new MyTaskAdapter(getActivity(), taskResponses);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            rcvTask.setLayoutManager(linearLayoutManager);
            rcvTask.setAdapter(myTaskAdapter);

            rcvTask.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheData(role, sinceDate);
                    if (isLoadingMoreFromServer) getTaskFromServer(role, sinceStr, LIMIT);

                }
            });

            myTaskAdapter.setMyTaskAdapterListener(new MyTaskAdapter.MyTaskAdapterListener() {
                @Override
                public void onMyTaskAdapterClickListener(int position) {
                    TaskResponse taskResponse = taskResponses.get(position);
                    LogUtils.d(TAG, "myTaskAdapter.setMyTaskAdapterListener , taskResponse : " + taskResponse);

                    Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                    startActivityForResult(intent, Constants.REQUEST_CODE_TASK_EDIT, TransitionScreen.RIGHT_TO_LEFT);
                }
            });

        } else {

            myTaskAdapter.notifyDataSetChanged();
//            rcvTask.post(new Runnable() {
//                public void run() {
//                    myTaskAdapter.notifyItemInserted(taskResponses.size() - 2);
//                }
//            });
        }

        LogUtils.d(TAG, "refreshList , taskReponse size : " + taskResponses.size());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.RESULT_CODE_TASK_EDIT) {
            TaskResponse taskEdit = (TaskResponse) data.getSerializableExtra(Constants.EXTRA_TASK);
            for (int i = 0; i < taskResponses.size(); i++) {
                if (taskResponses.get(i).getId() == taskEdit.getId()) {
                    taskResponses.set(i, taskEdit);
                    myTaskAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getTaskFromServer(role, null, LIMIT);
    }
}
