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
import vn.tonish.hozo.activity.TaskDetailNewActivity;
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
    private static final int LIMIT = 20;
    private String sinceStr;
    private boolean isLoadingMoreFromServer = true;
    private Call<List<TaskResponse>> call;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private LinearLayoutManager linearLayoutManager;
    private String filter = "";
    private boolean isReloadMyTask = false;

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
        isReloadMyTask = false;
        initList();
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

                if (isLoadingMoreFromServer) {
                    getTaskFromServer(sinceStr, LIMIT, filter);
                }

            }
        };
        rcvTask.addOnScrollListener(endlessRecyclerViewScrollListener);

        myTaskAdapter.setMyTaskAdapterListener(new MyTaskAdapter.MyTaskAdapterListener() {
            @Override
            public void onMyTaskAdapterClickListener(int position) {
                TaskResponse taskResponse = taskResponses.get(position);
                LogUtils.d(TAG, "myTaskAdapter.setMyTaskAdapterListener , taskResponse : " + taskResponse);

                Intent intent = new Intent(getActivity(), TaskDetailNewActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                startActivityForResult(intent, Constants.REQUEST_CODE_TASK_EDIT, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER));

        //refresh my task fragment after copy task
        if (isReloadMyTask) {
            Intent intent = new Intent();
            intent.putExtra(Constants.REFRESH_EXTRA, "refresh");
            intent.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK);
            getActivity().sendBroadcast(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra(Constants.MYTASK_FILTER_EXTRA)) {
                filter = intent.getStringExtra(Constants.MYTASK_FILTER_EXTRA);
                onRefresh();
            } else {
                rcvTask.smoothScrollToPosition(0);
                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) onRefresh();
            }

        }
    };

    private void getTaskFromServer(final String since, final int limit, final String filter) {

        if (call != null) call.cancel();

        Map<String, String> params = new HashMap<>();
        params.put("role", Constants.ROLE_TASKER);
        if (since != null) {
            params.put("since", since);
        }
        params.put("limit", limit + "");

        if (!filter.equals(""))
            params.put("status", filter);

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
                            getTaskFromServer(since, limit, filter);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTaskFromServer(since, limit, filter);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

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
                            getTaskFromServer(since, limit, filter);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

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
        } else if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.RESULT_CODE_TASK_DELETE) {
            TaskResponse taskEdit = (TaskResponse) data.getSerializableExtra(Constants.EXTRA_TASK);
            for (int i = 0; i < taskResponses.size(); i++) {
                if (taskResponses.get(i).getId() == taskEdit.getId()) {
                    taskResponses.remove(i);
                    myTaskAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            isReloadMyTask = true;
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        sinceStr = null;
        myTaskAdapter.onLoadMore();
        rcvTask.smoothScrollToPosition(0);
        getTaskFromServer(null, LIMIT, filter);
    }

}
