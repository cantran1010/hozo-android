package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BrowserTaskMapActivity;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.activity.SettingActivity;
import vn.tonish.hozo.activity.task_detail.DetailTaskActivity;
import vn.tonish.hozo.adapter.TaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.manager.TaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.MiniTask;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.edt_search;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;
import static vn.tonish.hozo.utils.Utils.showSearch;

/**
 * Created by CanTran on 4/11/17.
 */

public class BrowseTaskFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = BrowseTaskFragment.class.getSimpleName();
    private final static int limit = 20;
    private ImageView imgSearch, imgLocation, imgBack, imgClear;
    private RelativeLayout layoutHeader, layoutSearch;
    private EdittextHozo edtSearch;
    private RecyclerView rcvTask;
    private TaskAdapter taskAdapter;
    private ImageView imgFilter;
    private final List<TaskResponse> taskList = new ArrayList<>();
    private String sinceStr = null;
    private String query = null;
    private final String strSortBy = null;
    private boolean isLoadingMoreFromServer = true;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private Call<List<TaskResponse>> call;
    private LinearLayoutManager linearLayoutManager;
    private TextViewHozo tvCountNewTask;

    @Override
    protected int getLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgLocation = (ImageView) findViewById(R.id.img_location);
        imgFilter = (ImageView) findViewById(R.id.img_filter);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        imgBack = (ImageView) findViewById(R.id.img_back);
        layoutHeader = (RelativeLayout) findViewById(R.id.browse_task_header);
        edtSearch = (EdittextHozo) findViewById(edt_search);
        layoutSearch = (RelativeLayout) findViewById(R.id.fr_search);
        rcvTask = (RecyclerView) findViewById(R.id.lv_list);
        rcvTask.setHasFixedSize(true);
        tvCountNewTask = (TextViewHozo) findViewById(R.id.tvCountNewTask);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        imgFilter.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgClear.setOnClickListener(this);
        tvCountNewTask.setOnClickListener(this);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                hideKeyBoard(getActivity());
                return actionId == EditorInfo.IME_ACTION_SEARCH;
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSearch.getText().toString().length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                }
                search();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        rcvTask.setItemAnimator(new FadeInAnimator());
        taskAdapter = new TaskAdapter(getActivity(), taskList);
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(taskAdapter);
        scaleInAnimationAdapter.setFirstOnly(false);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator(.5f));
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(scaleInAnimationAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (isLoadingMoreFromServer) getTaskResponse(sinceStr, strSortBy, query);
            }
        };

        rcvTask.addOnScrollListener(endlessRecyclerViewScrollListener);

        taskAdapter.setTaskAdapterListener(new TaskAdapter.TaskAdapterListener() {
            @Override
            public void onTaskAdapterClickListener(int position) {
                LogUtils.d(TAG, "onclick");
                TaskResponse taskResponse = taskList.get(position);
                Intent intent = new Intent(getActivity(), DetailTaskActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                startActivityForResult(intent, Constants.REQUEST_CODE_TASK_EDIT, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }

    @Override
    protected void resumeData() {
        updateCountNewTask(MainActivity.countNewTask);
        PreferUtils.setLastTimeCountTask(getActivity(), DateTimeUtils.fromCalendarIso(Calendar.getInstance()));
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_SEARCH));
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

    private void getTaskResponse(final String since, final String sortBytask, final String query) {
        Map<String, String> option = new HashMap<>();
        option.put("limit", String.valueOf(limit));
        if (since != null) option.put("since", since);
        if (query != null) option.put("query", query);
        call = ApiClient.getApiService().getTasks(UserManager.getUserToken(), option);
        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                LogUtils.d(TAG, "getTaskResponse code : " + response.code());
                LogUtils.d(TAG, "getTaskResponse body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<TaskResponse> taskResponses = response.body();
                    LogUtils.d(TAG, "getTaskFromServer taskResponses size : " + (taskResponses != null ? taskResponses.size() : 0));
                    if (since == null) {
                        taskList.clear();
                        endlessRecyclerViewScrollListener.resetState();
                    }
                    if ((taskResponses != null ? taskResponses.size() : 0) > 0)
                        sinceStr = taskResponses.get((taskResponses != null ? taskResponses.size() : 0) - 1).getCreatedAt();
                    taskList.addAll(taskResponses != null ? taskResponses : null);
                    taskAdapter.notifyDataSetChanged();
                    LogUtils.d(TAG, "getTaskResponse size : " + taskList.size());
                    TaskManager.insertTasks(DataParse.convertListTaskResponseToTaskEntity(taskResponses));

                    if (taskResponses.size() < limit) {
                        isLoadingMoreFromServer = false;
                        taskAdapter.stopLoadMore();
                    }

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getTaskResponse(since, sortBytask, query);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getActivity(), error.message(), true, false);
                }


                onStopRefresh();
                if (!(taskList.size() > 0)) {
                    findViewById(R.id.noitem).setVisibility(View.VISIBLE);
                    findViewById(R.id.swpRefresh).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.noitem).setVisibility(View.GONE);
                    findViewById(R.id.swpRefresh).setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                onStopRefresh();
                LogUtils.e(TAG, "getTaskResponse , onFailure : " + t.getMessage());

                if (!t.getMessage().equals("Canceled")) {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            taskAdapter.onLoadMore();
                            getTaskResponse(since, sortBytask, query);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                    taskAdapter.stopLoadMore();
                    onStopRefresh();
                    taskAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_location:
                goToMapScren();
                break;
            case R.id.img_search:
                showSearch(getActivity(), layoutSearch, true);
                showSearch(getActivity(), layoutHeader, false);

                break;
            case R.id.img_back:
                query = null;
                edtSearch.setText("");
                showSearch(getActivity(), layoutHeader, true);
                showSearch(getActivity(), layoutSearch, false);
                endlessRecyclerViewScrollListener.resetState();
                onRefresh();
                break;
            case R.id.img_clear:
                edtSearch.setText("");
                break;
            case R.id.img_filter:
                startActivityForResult(new Intent(getActivity(), SettingActivity.class), Constants.REQUEST_CODE_SETTING, TransitionScreen.RIGHT_TO_LEFT);
                break;
            case R.id.tvCountNewTask:
                onRefresh();
                break;
        }

    }

    private void search() {
        sinceStr = null;
        query = edtSearch.getText().toString();
        getTaskResponse(sinceStr, strSortBy, query);
    }

    private void goToMapScren() {
        ArrayList<MiniTask> miniTasks = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {
            MiniTask miniTask = new MiniTask();
            miniTask.setId(taskList.get(i).getId());
            miniTask.setTitle(taskList.get(i).getTitle());
            miniTask.setAddress(taskList.get(i).getAddress());
            miniTask.setLat(taskList.get(i).getLatitude());
            miniTask.setLon(taskList.get(i).getLongitude());

            miniTasks.add(miniTask);
        }

        Intent intent = new Intent(getActivity(), BrowserTaskMapActivity.class);
        intent.putParcelableArrayListExtra(Constants.LIST_TASK_EXTRA, miniTasks);
        startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_SETTING && resultCode == Constants.RESULT_CODE_SETTING) {
            onRefresh();
        } else if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.RESULT_CODE_TASK_EDIT) {
            TaskResponse taskEdit = (TaskResponse) data.getSerializableExtra(Constants.EXTRA_TASK);
            if (taskEdit.getOfferStatus() != null && !taskEdit.getOfferStatus().equals("")) {
                for (int i = 0; i < taskList.size(); i++) {
                    if (taskList.get(i).getId() == taskEdit.getId()) {
                        taskList.remove(i);
                        taskAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_TASK_EDIT && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            openFragment(R.id.layout_container, MyTaskFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);
            updateMenuUi(3);
        } else if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            openFragment(R.id.layout_container, MyTaskFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);
            updateMenuUi(3);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        sinceStr = null;
        taskAdapter.onLoadMore();
        getTaskResponse(null, strSortBy, query);

        // update lastTime
        // set new task = 0
        updateCountNewTask(0);
        PreferUtils.setLastTimeCountTask(getActivity(), DateTimeUtils.fromCalendarIso(Calendar.getInstance()));
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).updateNewTask(0);
        MainActivity.countNewTask = 0;
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.COUNT_NEW_TASK_EXTRA)) {
                int count = intent.getIntExtra(Constants.COUNT_NEW_TASK_EXTRA, 0);
                updateCountNewTask(count);
            } else {
                rcvTask.smoothScrollToPosition(0);
                if (linearLayoutManager.findFirstVisibleItemPosition() == 0) onRefresh();
            }
        }
    };

    private void updateCountNewTask(int count) {

        if (count > 99) count = 99;
        if (count > 0) {
            tvCountNewTask.setText(getString(R.string.brower_new_task, count));
            tvCountNewTask.setVisibility(View.VISIBLE);
        } else
            tvCountNewTask.setVisibility(View.GONE);
    }

}

