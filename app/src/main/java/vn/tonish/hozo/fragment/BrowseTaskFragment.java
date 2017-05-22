package vn.tonish.hozo.fragment;

import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AdvanceSettingsActivity;
import vn.tonish.hozo.activity.BrowerTaskMapActivity;
import vn.tonish.hozo.adapter.TaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.TaskEntity;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.TaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.MiniTask;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.EdittextHozo;

import static vn.tonish.hozo.R.id.edt_search;
import static vn.tonish.hozo.R.id.fr_search;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by Can Tran on 4/11/17.
 */

public class BrowseTaskFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = BrowseTaskFragment.class.getSimpleName();
    public final static int limit = 10;
    private ImageView imgSearch, imgLocation, imgControls, imgBack, imgClear;
    private FrameLayout layoutHeader, layoutSearch;
    private EdittextHozo edtSearch;
    private RecyclerView rcvTask;
    private TaskAdapter taskAdapter;
    private LinearLayoutManager lvManager;
    private List<TaskResponse> taskList = new ArrayList<>();
    private String since;
    boolean isLoadingMoreFromServer = true;
    boolean isLoadingMoreFromDb = true;
    boolean isLoadingFromServer = false;
    private Date sinceDate;
    private Animation rtAnimation;
    private Animation lanimation;


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
        layoutHeader = (FrameLayout) findViewById(R.id.browse_task_header);
        edtSearch = (EdittextHozo) findViewById(edt_search);
        layoutSearch = (FrameLayout) findViewById(fr_search);
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

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        getCacheDataFirstPage();
        getTaskResponse(false, "", "");

    }

    @Override
    protected void resumeData() {

    }

    private void getCacheDataFirstPage() {
        LogUtils.d(TAG, "getCacheDataFirstPage start");
        List<TaskEntity> taskEntities = TaskManager.getFirstPage();
        if (taskEntities.size() > 0)
            sinceDate = taskEntities.get(taskEntities.size() - 1).getCreatedAt();
        taskList = DataParse.converListTaskEntityToTaskResponse(taskEntities);

        if (taskEntities.size() < limit) isLoadingMoreFromDb = false;
        refreshList();
    }

    private void getCacheDataPage() {
        LogUtils.d(TAG, "getCacheDataPage start");
        List<TaskEntity> taskEntities = TaskManager.getTaskEntitiesSince(sinceDate);

        if (taskEntities.size() > 0)
            sinceDate = taskEntities.get(taskEntities.size() - 1).getCreatedAt();

        if (taskEntities.size() < limit) isLoadingMoreFromDb = false;

        taskList.addAll(DataParse.converListTaskEntityToTaskResponse(taskEntities));
        refreshList();
    }


    public void getTaskResponse(final boolean isSince, String SortBy, String query) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        taskAdapter.stopLoadMore();
        LogUtils.d(TAG, "getTaskList start");
        Map<String, String> option = new HashMap<>();
        if (!(isSince && since != null))
            since = "";

        option = DataParse.setParameterGetTasks(SettingManager.getSettingEntiny(), SortBy, String.valueOf(limit), since, query);
        ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), option).enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                LogUtils.d(TAG, "getTaskResponse code : " + response.code());
                LogUtils.d(TAG, "getTaskResponse body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<TaskResponse> taskResponses = response.body();
                    for (int i = 0; i < taskResponses.size(); i++) {
                        if (!checkContainsTaskResponse(taskList, taskResponses.get(i)))
                            taskList.add(taskResponses.get(i));
                    }
                    since = taskResponses.get(taskResponses.size() - 1).getCreatedAt();
                    if (taskResponses.size() < limit) {
                        isLoadingMoreFromServer = false;
                        taskAdapter.stopLoadMore();
                    }
                    refreshList();
                    TaskManager.insertTasks(DataParse.convertListTaskResponseToTaskEntity(taskResponses));
                    LogUtils.d(TAG, "getTasksonResponse size : " + taskList.size());
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getTaskResponse(isSince, "", "");
                        }
                    });

                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getTaskResponse(isSince, "", "");
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
                LogUtils.e(TAG, "getNotifications , onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getTaskResponse(isSince, "", "");
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                taskAdapter.stopLoadMore();
                isLoadingFromServer = false;
                onStopRefresh();
            }
        });
    }

    private void refreshList() {
        if (taskAdapter == null) {
            taskAdapter = new TaskAdapter(getActivity(), taskList);
            lvManager = new LinearLayoutManager(getActivity());
            rcvTask.setLayoutManager(lvManager);
            rcvTask.setAdapter(taskAdapter);

            rcvTask.addOnScrollListener(new EndlessRecyclerViewScrollListener(lvManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheDataPage();
                    if (isLoadingMoreFromServer) getTaskResponse(false, "", "");

                }
            });

        } else {
            taskAdapter.notifyDataSetChanged();
        }

        LogUtils.d(TAG, "refreshList , TaskResponse size : " + taskList.size());

    }

    private boolean checkContainsTaskResponse(List<TaskResponse> taskResponses, TaskResponse response) {
        for (int i = 0; i < taskResponses.size(); i++)
            if (taskResponses.get(i).getId() == response.getId()) return true;
        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_location:
                goToMapScren();
                break;
            case R.id.img_search:
                showSearch(layoutSearch, 200, true);
                showSearch(layoutHeader, 200, false);
                break;
            case R.id.img_back:
                showSearch(layoutHeader, 200, true);
                showSearch(layoutSearch, 200, false);
                break;
            case R.id.img_clear:
                edtSearch.setText("");
                break;
            case R.id.img_controls:
                startActivity(new Intent(getContext(), AdvanceSettingsActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                break;
        }

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

        Intent intent = new Intent(getActivity(), BrowerTaskMapActivity.class);
        intent.putParcelableArrayListExtra(Constants.LIST_TASK_EXTRA, miniTasks);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }

    private void showSearch(final View view, int duration, boolean isShow) {
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
            }, duration);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getTaskResponse(false, "", "");
    }

}

