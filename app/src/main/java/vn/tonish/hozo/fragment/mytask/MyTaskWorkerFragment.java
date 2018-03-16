package vn.tonish.hozo.fragment.mytask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PostTaskActivity;
import vn.tonish.hozo.activity.task_detail.DetailTaskActivity;
import vn.tonish.hozo.adapter.MyTaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.StatusEntity;
import vn.tonish.hozo.database.manager.StatusManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

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
    public EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private LinearLayoutManager linearLayoutManager;
    private String filter = "";
    private TextViewHozo tvNoData;
    private String mQuery = "";
    private ArrayList<String> listStatus;

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_task_poster;
    }

    @Override
    protected void initView() {
        rcvTask = (RecyclerView) findViewById(R.id.rcv_task);
        tvNoData = (TextViewHozo) findViewById(R.id.tv_no_data);

        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        getStatus();
        initList();
    }

    private void getStatus() {
        listStatus = new ArrayList<>();
        List<StatusEntity> statusEntities = StatusManager.getStatuswithRole(Constants.ROLE_TASKER);
        if (!(statusEntities == null || !(statusEntities.size() > 0))) {
            for (StatusEntity statusEntity : statusEntities) {
                if (statusEntity.isSelected())
                    listStatus.add(statusEntity.getStatus());
            }
        }
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK_WORKER));
        LogUtils.d(TAG, "MyTaskPosterFragment resumeData");

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
                    getTaskFromServer(sinceStr, LIMIT, filter, mQuery);
                }

            }
        };

        rcvTask.addOnScrollListener(endlessRecyclerViewScrollListener);

        myTaskAdapter.setMyTaskAdapterListener(new MyTaskAdapter.MyTaskAdapterListener() {
            @Override
            public void onMyTaskAdapterClickListener(int position) {
                TaskResponse taskResponse = taskResponses.get(position);
                LogUtils.d(TAG, "myTaskAdapter.setMyTaskAdapterListener , taskResponse : " + taskResponse);
                if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_DRAFT)) {
                    Intent intentEdit = new Intent(getActivity(), PostTaskActivity.class);
                    intentEdit.putExtra(Constants.EXTRA_TASK, taskResponse);
                    intentEdit.putExtra(Constants.TASK_EDIT_EXTRA, Constants.TASK_DRAFT);
                    startActivityForResult(intentEdit, Constants.REQUEST_CODE_TASK_EDIT, TransitionScreen.RIGHT_TO_LEFT);
                } else {
                    Intent intent = new Intent(getActivity(), DetailTaskActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                    startActivityForResult(intent, Constants.REQUEST_CODE_TASK_EDIT, TransitionScreen.RIGHT_TO_LEFT);
                }

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG, "broadcastReceiverSmoothToTop onReceive");
            if (intent.hasExtra(Constants.MYTASK_FILTER_EXTRA)) {
                filter = intent.getStringExtra(Constants.MYTASK_FILTER_EXTRA);
                onRefresh();
            } else {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        rcvTask.smoothScrollToPosition(0);
                    }
                });
                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) onRefresh();
            }

        }
    };

    private void getTaskFromServer(final String since, final int limit, final String filter, final String query) {
        if (call != null) call.cancel();
        Map<String, String> params = new HashMap<>();
        params.put("role", Constants.ROLE_TASKER);
        if (since != null) params.put("since", since);
        params.put("limit", limit + "");
        if (!query.isEmpty()) params.put("query", query);
        if (!filter.equals(""))
            params.put("status", filter);
        LogUtils.d(TAG, "getTaskFromServer start , param : " + params);

        call = ApiClient.getApiService().getMyTask(UserManager.getUserToken(), params, listStatus);
        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                LogUtils.d(TAG, "getTaskFromServer code : " + response.code());
                LogUtils.d(TAG, "getTaskFromServer body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<TaskResponse> taskResponsesBody = response.body();

                    LogUtils.d(TAG, "getTaskFromServer taskResponsesBody size : " + (taskResponsesBody != null ? taskResponsesBody.size() : 0));

                    if ((taskResponsesBody != null ? taskResponsesBody.size() : 0) > 0)
                        sinceStr = taskResponsesBody != null ? taskResponsesBody.get(taskResponsesBody.size() - 1).getCreatedAt() : null;

                    if (since == null) {
                        taskResponses.clear();
                        endlessRecyclerViewScrollListener.resetState();
                    }

                    for (TaskResponse taskReponse : taskResponsesBody)
                        taskReponse.setRole(Constants.ROLE_TASKER);
                    taskResponses.addAll(taskResponsesBody);

//                    TaskManager.insertTasks(DataParse.convertListTaskResponseToTaskEntity(taskResponsesBody));

                    if (taskResponsesBody.size() < LIMIT) {
                        myTaskAdapter.stopLoadMore();
                        isLoadingMoreFromServer = false;
                    }

                    refreshList();
                    LogUtils.d(TAG, "getTaskFromServer taskResponses size : " + taskResponses.size());

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getTaskFromServer(since, limit, filter, query);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    Utils.showLongToast(getActivity(), getString(R.string.invalid_error), true, false);
                } else {
                    myTaskAdapter.stopLoadMore();
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTaskFromServer(since, limit, filter, query);
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
                            getTaskFromServer(since, limit, filter, query);
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

        if (taskResponses.size() > 0)
            tvNoData.setVisibility(View.GONE);
        else
            tvNoData.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "onActivityResult , requestCode : " + requestCode + " , resultCode: " + resultCode);
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
            Bundle bundle = new Bundle();
            bundle.putString(Constants.ROLE_EXTRA, Constants.ROLE_POSTER);
            openFragment(R.id.layout_container, MyTaskFragment.class, bundle, false, TransitionScreen.RIGHT_TO_LEFT);
        } else if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            onRefresh();
        } else if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.RESULT_CODE_TASK_CANCEL)
            onRefresh();
    }

    public void search(String query) {
        if (call != null) call.cancel();
        mQuery = query;
        sinceStr = null;
        getTaskFromServer(null, LIMIT, filter, query);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        LogUtils.d(TAG, "onRefresh start");
        getStatus();
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        sinceStr = null;
        myTaskAdapter.onLoadMore();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                rcvTask.smoothScrollToPosition(0);
            }
        });
        getTaskFromServer(null, LIMIT, filter, mQuery);
    }
}
