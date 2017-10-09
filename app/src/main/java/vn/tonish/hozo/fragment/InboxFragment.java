package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ToggleButton;

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
import vn.tonish.hozo.rest.responseRes.NofifySystemResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.PUSH_TYPE_ADMIN_NEW_TASK_ALERT;
import static vn.tonish.hozo.common.Constants.PUSH_TYPE_POSTER_CANCELED;

/**
 * Created by LongBui on 4/4/2017.
 */

public class InboxFragment extends BaseFragment {

    private static final String TAG = InboxFragment.class.getSimpleName();

    private NotificationAdapter notificationAdapter;
    private RecyclerView lvList;
    private final List<Notification> notifications = new ArrayList<>();
    private String since;
    //    private Date sinceDate;
    public static final int LIMIT = 20;
    private boolean isLoadingMoreFromServer = true;
    //    private boolean isLoadingMoreFromDb = true;
//    private boolean isLoadingFromServer = false;
    private Call<List<Notification>> call;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private LinearLayoutManager linearLayoutManager;
    private ToggleButton tbOnOffNotification;
    private TextViewHozo tvNoData;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_inbox;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        tbOnOffNotification = (ToggleButton) findViewById(R.id.tg_on_off);
        tvNoData = (TextViewHozo) findViewById(R.id.tv_no_data);

        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        LogUtils.d(TAG, "InboxFragment life cycle , initData");

        tbOnOffNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d(TAG, "tgOnOffNotify : " + tbOnOffNotification.isChecked());
                updateNofityOnOff(tbOnOffNotification.isChecked());
            }
        });

        initList();
//        getCacheDataPage(sinceDate);
//        getNotifications(false);

        PreferUtils.setNewPushCount(getActivity(), 0);
        PreferUtils.setNewPushChatCount(getActivity(), 0);

        Intent intentPushCount = new Intent();
        intentPushCount.setAction(Constants.BROAD_CAST_PUSH_COUNT);
        getActivity().sendBroadcast(intentPushCount);

        getNotifyOnOff();

    }

    private void getNotifyOnOff() {
        Call<NofifySystemResponse> callNotify = ApiClient.getApiService().getNotifySystem(UserManager.getUserToken());
        callNotify.enqueue(new Callback<NofifySystemResponse>() {
            @Override
            public void onResponse(Call<NofifySystemResponse> call, Response<NofifySystemResponse> response) {
                LogUtils.d(TAG, "getNotifyOnOff code : " + response.code());
                LogUtils.d(TAG, "getNotifyOnOff body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    NofifySystemResponse nofifySystemResponse = response.body();
                    tbOnOffNotification.setChecked(nofifySystemResponse.isNotificationSystem());
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getNotifyOnOff();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getNotifyOnOff();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<NofifySystemResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getNotifyOnOff();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void updateNofityOnOff(final boolean isOnOff) {
        ProgressDialogUtils.showProgressDialog(getActivity());
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("notification_system", isOnOff);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "updateNofityOnOff data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().updateNotifySystem(UserManager.getUserToken(), body).enqueue(new Callback<NofifySystemResponse>() {
            @Override
            public void onResponse(Call<NofifySystemResponse> call, Response<NofifySystemResponse> response) {

                LogUtils.d(TAG, "updateNofityOnOff onResponse : " + response.body());
                LogUtils.d(TAG, "updateNofityOnOff code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {


                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateNofityOnOff(isOnOff);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            updateNofityOnOff(isOnOff);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<NofifySystemResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateNofityOnOff(isOnOff);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
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

//                    if (isLoadingMoreFromDb) getCacheDataPage(sinceDate);
                if (isLoadingMoreFromServer) getNotifications();

            }
        };

        lvList.addOnScrollListener(endlessRecyclerViewScrollListener);

        notificationAdapter.setNotificationAdapterListener(new NotificationAdapter.NotificationAdapterListener() {
            @Override
            public void onNotificationAdapterListener(int position) {

                updateReadNotification(position);

                if (notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)
                        || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)
                        || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ACTIVE_USER)
                        || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ACTIVE_TASK)
                        || notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ACTIVE_COMMENT)) {
//                    DialogUtils.showOkDialog(getActivity(), getString(R.string.app_name), notifications.get(position).getContent(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
//                                @Override
//                                public void onSubmit() {
//
//                                }
//                            }
//                    );

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
                    intent.putExtra(Constants.TAB_EXTRA, 1);
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

//    private void getCacheDataPage(Date sinceDateSearch) {
//        LogUtils.d(TAG, "getCacheDataPage start");
//        List<Notification> notificationEntities = NotificationManager.getNotificationsSince(sinceDateSearch);
//        LogUtils.d(TAG, "getCacheDataPage notificationEntities : " + notificationEntities.toString());
//        LogUtils.d(TAG, "getCacheDataPage notificationEntities size : " + notificationEntities.size());
//
//        if (notificationEntities.size() > 0)
//            sinceDate = notificationEntities.get(notificationEntities.size() - 1).getCreatedDateAt();
//
//        if (notificationEntities.size() < LIMIT) isLoadingMoreFromDb = false;
//
//        notifications.addAll(notificationEntities);
//        refreshList();
//    }

    private void getNotifications() {
//        ProgressDialogUtils.showProgressDialog(getActivity());
//        if (isLoadingFromServer) return;
//        isLoadingFromServer = true;
//        notificationAdapter.stopLoadMore();

        LogUtils.d(TAG, "getNotifications start");
        Map<String, String> params = new HashMap<>();

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
//                    for (int i = 0; i < notificationResponse.size(); i++) {
//                        notificationResponse.get(i).setCreatedDateAt(DateTimeUtils.getDateFromStringIso(notificationResponse.get(i).getCreatedAt()));
//
////                        if (!notifications.contains(notificationResponse.get(i)))
////                            notifications.add(notificationResponse.get(i));
//
////                        if (!checkContainsNotification(notifications, notificationResponse.get(i)))
//                            notifications.add(notificationResponse.get(i));
//                    }

                    if (since == null) {
                        notifications.clear();
                        endlessRecyclerViewScrollListener.resetState();

                        PreferUtils.setNewPushCount(getActivity(), 0);
                        PreferUtils.setNewPushChatCount(getActivity(), 0);

                        if (getActivity() != null && getActivity() instanceof MainActivity)
                            ((MainActivity) getActivity()).updateCountMsg();

                        Intent intentPushCount = new Intent();
                        intentPushCount.setAction(Constants.BROAD_CAST_PUSH_COUNT);
                        if (getActivity() != null) getActivity().sendBroadcast(intentPushCount);
                    }

                    notifications.addAll(notificationResponse != null ? notificationResponse : null);

//                    Collections.sort(notifications, new Comparator<Notification>() {
//                        @Override
//                        public int compare(Notification o1, Notification o2) {
//                            return o2.getCreatedDateAt().compareTo(o1.getCreatedDateAt());
//                        }
//                    });

                    if ((notificationResponse != null ? notificationResponse.size() : 0) > 0)
                        since = notificationResponse.get(notificationResponse.size() - 1).getCreatedAt();

                    if (notificationResponse.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        notificationAdapter.stopLoadMore();
                    }

                    refreshList();
                    LogUtils.d(TAG, "getNotifications notificationResponse size : " + notificationResponse.size());
                    LogUtils.d(TAG, "getNotifications notifications size : " + notifications.size());
//                    NotificationManager.insertNotifications(notificationResponse);

                    //        if (since == null) Utils.cancelAllNotification(getActivity());

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

//                isLoadingFromServer = false;
                onStopRefresh();
                refreshList();
//                ProgressDialogUtils.dismissProgressDialog();
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
//                isLoadingFromServer = false;
                    onStopRefresh();
                    refreshList();
//                ProgressDialogUtils.dismissProgressDialog();
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

    private boolean checkContainsNotification(List<Notification> notifications, Notification notification) {
        for (int i = 0; i < notifications.size(); i++)
            if (notifications.get(i).getId() == notification.getId()) return true;
        return false;
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_INBOX));
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
            lvList.smoothScrollToPosition(0);
            if (linearLayoutManager.findFirstVisibleItemPosition() == 0) onRefresh();
        }
    };
}
