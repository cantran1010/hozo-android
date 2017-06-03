package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.setViewBackground;

/**
 * Created by LongBui on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyTaskFragment.class.getSimpleName();
    private TextViewHozo tvWorker, tvPoster;
    private RecyclerView rcvTask;
    private final List<TaskResponse> taskResponses = new ArrayList<>();
    private MyTaskAdapter myTaskAdapter;
    private String role = Constants.ROLE_TASKER;
    public static final int LIMIT = 15;
    private String sinceStr;
    private Date sinceDate;
    private String query;
    private boolean isLoadingMoreFromServer = true;
    private boolean isLoadingMoreFromDb = true;
    private boolean isLoadingFromServer = false;
    private Call<List<TaskResponse>> call;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_my_work;
    }

    @Override
    protected void initView() {
        tvWorker = (TextViewHozo) findViewById(R.id.tv_worker);
        tvPoster = (TextViewHozo) findViewById(R.id.tv_poster);

        rcvTask = (RecyclerView) findViewById(R.id.rcv_task);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        tvWorker.setOnClickListener(this);
        tvPoster.setOnClickListener(this);

        getCacheData(role, sinceDate);
        getTaskFromServer(role, sinceStr, LIMIT, null);

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

    private void getTaskFromServer(final String role, final String since, final int limit, final String query) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        myTaskAdapter.stopLoadMore();

        Map<String, String> params = new HashMap<>();

        params.put("role", role);
        if (since != null) params.put("since", since);
        params.put("limit", limit + "");
        if (query != null) params.put("query", query);

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
                            getTaskFromServer(role, since, limit, query);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTaskFromServer(role, since, limit, query);
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
                public void onLoadMore(int page, int totalItemsCount) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheData(role, sinceDate);
                    if (isLoadingMoreFromServer) getTaskFromServer(role, sinceStr, LIMIT, query);

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

//                    if (role.equals(Constants.ROLE_TASKER)) {
//                        if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        }
//                    } else if (role.equals(Constants.ROLE_POSTER)) {
//                        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivityForResult(intent, Constants.REQUEST_CODE_TASK_EDIT, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
//                            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
//                            intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
//                        }
//                    }

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
                if (role.equals(Constants.ROLE_TASKER)) break;
                resetSelectedTab(1);
                selectedTab(1);
                break;

            case R.id.tv_poster:
                if (role.equals(Constants.ROLE_POSTER)) break;
                resetSelectedTab(2);
                selectedTab(2);
                break;

        }
    }

    private void resetSelectedTab(int position) {

        switch (position) {
            case 1:
                role = Constants.ROLE_TASKER;
                break;

            case 2:
                role = Constants.ROLE_POSTER;
                break;

            default:
                role = Constants.ROLE_TASKER;
                break;
        }

        call.cancel();
        taskResponses.clear();
        myTaskAdapter = null;
        sinceStr = null;
        sinceDate = null;
        query = null;
        isLoadingMoreFromServer = true;
        isLoadingMoreFromDb = true;
        isLoadingFromServer = false;

        getCacheData(role, sinceDate);
        getTaskFromServer(role, sinceStr, LIMIT, null);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getTaskFromServer(role, null, LIMIT, null);
    }
}
