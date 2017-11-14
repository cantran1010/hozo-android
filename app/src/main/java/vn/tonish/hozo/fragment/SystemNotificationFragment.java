package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.activity.TaskDetailNewActivity;
import vn.tonish.hozo.adapter.NotificationAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.BlockDialog;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.PUSH_TYPE_ADMIN_NEW_TASK_ALERT;
import static vn.tonish.hozo.common.Constants.PUSH_TYPE_POSTER_CANCELED;

/**
 * Created by LongBui on 4/4/2017.
 * Edit by Cantran
 */

public class SystemNotificationFragment extends BaseFragment {
    private static final String TAG = SystemNotificationFragment.class.getSimpleName();

    private NotificationAdapter notificationAdapter;
    private RecyclerView lvList;
    private final List<Notification> notifications = new ArrayList<>();
    private String since;
    public static final int LIMIT = 20;
    private boolean isLoadingMoreFromServer = true;
    private Call<List<Notification>> call;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private LinearLayoutManager linearLayoutManager;
    private TextViewHozo tvNoData;
    private static final int TIME_DELAY = 2000;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_notifacation;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        tvNoData = (TextViewHozo) findViewById(R.id.tv_no_data);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        initList();
    }

    private void initList() {
        notificationAdapter = new NotificationAdapter(getActivity(), notifications);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        lvList.setLayoutManager(linearLayoutManager);
        lvList.setAdapter(notificationAdapter);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);
                if (isLoadingMoreFromServer) getNotifications();
            }
        };

        lvList.addOnScrollListener(endlessRecyclerViewScrollListener);

        notificationAdapter.setNotificationAdapterListener(new NotificationAdapter.NotificationAdapterListener() {
            @Override
            public void onNotificationAdapterListener(final int position) {

                updateReadNotification(position);

                if (notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)) {
                    if (TextUtils.isEmpty(notifications.get(position).getExternalLink())) {
                        BlockDialog blockDialog = new BlockDialog(getActivity());
                        blockDialog.showView();
                        blockDialog.updateContent(notifications.get(position).getContent());
                    } else {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.admin_link_title), notifications.get(position).getContent(), getString(R.string.admin_link_submit), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                Utils.openBrowser(getActivity(), notifications.get(position).getExternalLink());
                            }
                        });
                    }
                } else if (notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)
                        || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ACTIVE_USER)
                        || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ACTIVE_TASK)
                        || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ACTIVE_COMMENT)) {

                    BlockDialog blockDialog = new BlockDialog(getActivity());
                    blockDialog.showView();
                    blockDialog.updateContent(notifications.get(position).getContent());

                } else if (notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_BLOCK_TASK) || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_BLOCK_COMMENT)) {
                    BlockDialog blockDialog = new BlockDialog(getActivity());
                    blockDialog.showView();
                    blockDialog.updateContent(notifications.get(position).getContent());
                } else if (notifications.get(position).getEvent().equals(PUSH_TYPE_POSTER_CANCELED)) {
                    DialogUtils.showOkDialog(getActivity(), getString(R.string.cancel_task_by_poster_title), getString(R.string.cancel_task_by_poster), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                } else if (notifications.get(position).getEvent().equals(PUSH_TYPE_ADMIN_NEW_TASK_ALERT)) {
                    Intent intent = new Intent(getActivity(), TaskDetailNewActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, notifications.get(position).getTaskId());
                    startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                } else if (notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_TASK_COMPLETE)) {
                    Intent intent = new Intent(getActivity(), TaskDetailNewActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, notifications.get(position).getTaskId());

                    if (notifications.get(position).getUserId() == UserManager.getMyUser().getId())
                        intent.putExtra(Constants.TAB_EXTRA, 1);
                    else
                        intent.putExtra(Constants.TAB_EXTRA, 0);

                    startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                } else {
                    Intent intent = new Intent(getActivity(), TaskDetailNewActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, notifications.get(position).getTaskId());
                    startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                }
            }
        });
    }

    private void updateReadNotification(final int position) {
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("read", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "updateReadNotification data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().updateReadNotification(UserManager.getUserToken(), notifications.get(position).getId(), body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "updateReadNotification onResponse : " + response.body());
                LogUtils.d(TAG, "updateReadNotification code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    notifications.get(position).setRead(true);
                    notificationAdapter.notifyDataSetChanged();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateReadNotification(position);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    LogUtils.d(TAG, "ERROR when updateReadNotification");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LogUtils.d(TAG, "ERROR updateReadNotification : " + t.getMessage());
            }
        });

    }

    private void getNotifications() {
        LogUtils.d(TAG, "getNotifications start");
        Map<String, String> params = new HashMap<>();
        params.put("type", Constants.NOTIFICATION_SYS);
        if (since != null)
            params.put("since", since);
        params.put("limit", LIMIT + "");
        LogUtils.d(TAG, "getNotifications params : " + params.toString());

        call = ApiClient.getApiService().getMyNotificationsGroup(UserManager.getUserToken(), params);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                LogUtils.d(TAG, "getNotifications code : " + response.code());
                LogUtils.d(TAG, "getNotifications body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    List<Notification> notificationResponse = response.body();
                    if (since == null) {
                        notifications.clear();
                        endlessRecyclerViewScrollListener.resetState();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PreferUtils.setNewPushCount(getActivity(), 0);
                                if (getActivity() != null && getActivity() instanceof MainActivity)
                                    ((MainActivity) getActivity()).updateCountMsg();
                                Intent intentPushCount = new Intent();
                                intentPushCount.setAction(Constants.BROAD_CAST_PUSH_COUNT);
                                if (getActivity() != null)
                                    getActivity().sendBroadcast(intentPushCount);
                            }
                        }, TIME_DELAY);

                    }
                    notifications.addAll(notificationResponse != null ? notificationResponse : null);
                    if ((notificationResponse != null ? notificationResponse.size() : 0) > 0)
                        since = notificationResponse.get(notificationResponse.size() - 1).getCreatedAt();

                    if (notificationResponse.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        notificationAdapter.stopLoadMore();
                    }
                    refreshList();
                    for (Notification notification : notifications)
                        if (getActivity() != null)
                            Utils.cancelNotification(getActivity(), notification.getId());

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getNotifications();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getNotifications();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                onStopRefresh();
                refreshList();
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                LogUtils.e(TAG, "getNotifications , onFailure : " + t.getMessage());
                if (!t.getMessage().equals("Canceled")) {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            notificationAdapter.onLoadMore();
                            getNotifications();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                    notificationAdapter.stopLoadMore();
                    onStopRefresh();
                    refreshList();
                }
            }
        });
    }

    private void refreshList() {
        notificationAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "refreshList , notification size : " + notifications.size());

        if (notifications.size() > 0)
            tvNoData.setVisibility(View.GONE);
        else
            tvNoData.setVisibility(View.VISIBLE);

    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_SYS_TEM));
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

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        isLoadingMoreFromServer = true;
        since = null;
        notificationAdapter.onLoadMore();
        getNotifications();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            openFragment(R.id.layout_container, MyTaskFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);
            updateMenuUi(3);
        }
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG, "broadcastReceiverSmoothToTop onReceive");
            lvList.smoothScrollToPosition(0);
            if (linearLayoutManager.findFirstVisibleItemPosition() == 0) onRefresh();
        }
    };
}
