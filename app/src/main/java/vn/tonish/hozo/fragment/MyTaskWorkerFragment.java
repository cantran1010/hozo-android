package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import vn.tonish.hozo.activity.TaskDetailActivity;
import vn.tonish.hozo.adapter.MyTaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.manager.TaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
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

public class MyTaskWorkerFragment extends BaseFragment {

    private static final String TAG = MyTaskWorkerFragment.class.getSimpleName();
    private RecyclerView rcvTask;
    private final List<TaskResponse> taskResponses = new ArrayList<>();
    private MyTaskAdapter myTaskAdapter;
    //    private String role = Constants.ROLE_TASKER;
    private static final int LIMIT = 20;
    private String sinceStr;
    //    private Date sinceDate;
    private boolean isLoadingMoreFromServer = true;
    //    boolean isLoadingMoreFromDb = true;
//    boolean isLoadingFromServer = false;
    private Call<List<TaskResponse>> call;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_task_worker;
    }

    @Override
    protected void initView() {
        rcvTask = (RecyclerView) findViewById(R.id.rcv_task);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        initList();
//        getCacheData(sinceDate);
//        getTaskFromServer(sinceStr, LIMIT);
    }

    private void initList() {
        myTaskAdapter = new MyTaskAdapter(getActivity(), taskResponses);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(myTaskAdapter);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

//                if (isLoadingMoreFromDb) getCacheData(role, sinceDate);
                if (isLoadingMoreFromServer) {
                    getTaskFromServer(sinceStr, LIMIT);
                }

            }
        };
        rcvTask.addOnScrollListener(endlessRecyclerViewScrollListener);

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
    }


//    private void getCacheData(String role, Date sinceDateIn) {
//        LogUtils.d(TAG, "getCacheData start , role : " + role + " , sinceDate : " + sinceDateIn);
//        List<TaskEntity> taskEntities = TaskManager.getTaskSince(sinceDateIn, role);
//        if (taskEntities.size() > 0)
//            sinceDate = taskEntities.get(taskEntities.size() - 1).getCreatedAt();
//        taskResponses.addAll(DataParse.converListTaskEntityToTaskResponse(taskEntities));
//        refreshList();
//        if (taskResponses.size() < LIMIT) isLoadingMoreFromDb = false;
//
//        LogUtils.d(TAG, "getCacheData , taskResponses : " + taskResponses.toString());
//        LogUtils.d(TAG, "getCacheData , taskResponses size : " + taskResponses.size());
//    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rcvTask.smoothScrollToPosition(0);
            if(linearLayoutManager.findFirstVisibleItemPosition() == 0) onRefresh();
        }
    };

    private void getTaskFromServer(final String since, final int limit) {

//        if (isLoadingFromServer) return;
//        isLoadingFromServer = true;
        Map<String, String> params = new HashMap<>();
        params.put("role", Constants.ROLE_TASKER);
        if (since != null) {
            params.put("since", since);
        }
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

                    LogUtils.d(TAG, "getTaskFromServer taskResponsesBody size : " + (taskResponsesBody != null ? taskResponsesBody.size() : 0));

                    if (taskResponsesBody.size() > 0)
                        sinceStr = taskResponsesBody.get(taskResponsesBody.size() - 1).getCreatedAt();

//                    for (int i = taskResponsesBody.size() - 1; i >= 0; i--){
//                        taskResponsesBody.get(i).setRole(Constants.ROLE_TASKER);
//                        Utils.checkContainsTaskResponse(taskResponses, taskResponsesBody.get(i));
//                    }
//
//                    Collections.sort(taskResponses, new Comparator<TaskResponse>() {
//                        @Override
//                        public int compare(TaskResponse o1, TaskResponse o2) {
//                            return o2.getCreatedAt().compareTo(o1.getCreatedAt());
//                        }
//                    });

                    if (since == null) {
                        taskResponses.clear();
                        endlessRecyclerViewScrollListener.resetState();
                    }

                    for (TaskResponse taskResponse : taskResponsesBody)
                        taskResponse.setRole(Constants.ROLE_TASKER);

                    taskResponses.addAll(taskResponsesBody);
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
                            getTaskFromServer(since, limit);
                        }
                    });
                }else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTaskFromServer(since, limit);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

//                isLoadingFromServer = false;
                onStopRefresh();
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                LogUtils.e(TAG, "getTaskFromServer error : " + t.getMessage());
                if (!t.getMessage().equals("Canceled")) {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            myTaskAdapter.onLoadMore();
                            getTaskFromServer(since, limit);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

//                isLoadingFromServer = false;
                    myTaskAdapter.stopLoadMore();
                    onStopRefresh();
                    refreshList();
                }
            }
        });
    }

    private void refreshList() {
        myTaskAdapter.notifyDataSetChanged();
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
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        sinceStr = null;
        myTaskAdapter.onLoadMore();
        getTaskFromServer(null, LIMIT);
    }

}
