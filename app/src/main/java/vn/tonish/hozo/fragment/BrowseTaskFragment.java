package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AdvanceSettingsActivity;
import vn.tonish.hozo.activity.BrowserTaskMapActivity;
import vn.tonish.hozo.activity.TaskDetailActivity;
import vn.tonish.hozo.adapter.TaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.TaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.MiniTask;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;

import static vn.tonish.hozo.R.id.edt_search;
import static vn.tonish.hozo.R.id.fr_search;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by CanTran on 4/11/17.
 */

public class BrowseTaskFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = BrowseTaskFragment.class.getSimpleName();
    private final static int limit = 20;
    private ImageView imgSearch, imgLocation, imgControls, imgBack, imgClear;
    private RelativeLayout layoutHeader, layoutSearch;
    private EdittextHozo edtSearch;
    private RecyclerView rcvTask;
    private TaskAdapter taskAdapter;
    private final List<TaskResponse> taskList = new ArrayList<>();
    private String sinceStr = null;
    private String query = null;
    private final String strSortBy = null;
    private Animation rtAnimation;
    private Animation lanimation;
    private boolean isLoadingMoreFromServer = true;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private Call<List<TaskResponse>> call;


    @Override
    protected int getLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected void initView() {
        rtAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        lanimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgLocation = (ImageView) findViewById(R.id.img_location);
        imgControls = (ImageView) findViewById(R.id.img_controls);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        imgBack = (ImageView) findViewById(R.id.img_back);
        layoutHeader = (RelativeLayout) findViewById(R.id.browse_task_header);
        edtSearch = (EdittextHozo) findViewById(edt_search);
        layoutSearch = (RelativeLayout) findViewById(fr_search);
        rcvTask = (RecyclerView) findViewById(R.id.lv_list);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        imgControls.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgClear.setOnClickListener(this);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
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

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setUpRecyclerView();
    }


    private void setUpRecyclerView() {
        taskAdapter = new TaskAdapter(getActivity(), taskList);
        LinearLayoutManager lvManager = new LinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(lvManager);
        rcvTask.setAdapter(taskAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(lvManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (isLoadingMoreFromServer) getTaskResponse(sinceStr, strSortBy, query);
            }
        };

        rcvTask.addOnScrollListener(endlessRecyclerViewScrollListener);

        taskAdapter.setTaskAdapterListener(new TaskAdapter.TaskAdapterListener() {
            @Override
            public void onTaskAdapterClickListener(int position) {
                LogUtils.d(TAG, "onclick");
                TaskResponse taskResponse = taskList.get(position);
                Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }


    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_SEARCH));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
    }

    private void getTaskResponse(final String since, final String sortBytask, final String query) {
        Map<String, String> option;
        option = DataParse.setParameterGetTasks(sortBytask, String.valueOf(limit), since, query);
        LogUtils.d(TAG, "getTaskResponse option : " + option.toString());
        call = ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), option);
        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                LogUtils.d(TAG, "getTaskResponse code : " + response.code());
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "getTaskResponse body : " + response.body());
                    if (response.code() == Constants.HTTP_CODE_OK) {
                        List<TaskResponse> taskResponses = response.body();
                        LogUtils.d(TAG, "getTaskFromServer taskResponses size : " + taskResponses.size());
                        if (since == null) {
                            taskList.clear();
                            endlessRecyclerViewScrollListener.resetState();
                        }
                        if (taskResponses.size() > 0)
                            sinceStr = taskResponses.get(taskResponses.size() - 1).getCreatedAt();
                        taskList.addAll(taskResponses);
                        taskAdapter.notifyDataSetChanged();
                        LogUtils.d(TAG, "getTaskResponse size : " + taskList.size());
                        TaskManager.insertTasks(DataParse.convertListTaskResponseToTaskEntity(taskResponses));

                        if (taskResponses.size() < limit) {
                            isLoadingMoreFromServer = false;
                            taskAdapter.stopLoadMore();
                        }

                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getTaskResponse(since, sortBytask, query);
                        }
                    });

                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getActivity(), error.message(), true, false);
                }


                onStopRefresh();

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
                showSearch(layoutSearch, true);
                showSearch(layoutHeader, false);

                break;
            case R.id.img_back:
                query = null;
                edtSearch.setText("");
                showSearch(layoutHeader, true);
                showSearch(layoutSearch, false);
                endlessRecyclerViewScrollListener.resetState();
                onRefresh();
                break;
            case R.id.img_clear:
                edtSearch.setText("");
                break;
            case R.id.img_controls:
                startActivity(new Intent(getActivity(), AdvanceSettingsActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                break;
        }

    }

    private void search() {
        if (edtSearch.getText().toString().isEmpty()) {
            edtSearch.setError(getActivity().getString(R.string.empty_search));
        } else {
            sinceStr = null;
            query = edtSearch.getText().toString();
            getTaskResponse(sinceStr, strSortBy, query);
        }
        hideKeyBoard(getActivity());

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
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }

    private void showSearch(final View view, boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
            view.startAnimation(rtAnimation);
        } else {
            view.startAnimation(lanimation);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, Constants.DURATION);
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

    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rcvTask.smoothScrollToPosition(0);
        }
    };

}

