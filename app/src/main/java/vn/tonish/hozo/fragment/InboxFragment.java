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
import vn.tonish.hozo.adapter.NotificationAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

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

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_notify;
    }

    @Override
    protected void initView() {
        TextViewHozo tvUnread = (TextViewHozo) findViewById(R.id.tv_unread);
        lvList = (RecyclerView) findViewById(R.id.lvList);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        LogUtils.d(TAG, "InboxFragment life cycle , initData");
        initList();
//        getCacheDataPage(sinceDate);
//        getNotifications(false);
    }

    private void initList() {
        notificationAdapter = new NotificationAdapter(getActivity(), notifications);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
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
                if (notifications.get(position).getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)) {
                    DialogUtils.showOkDialog(getActivity(), getString(R.string.app_name), notifications.get(position).getContent(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            }
                    );
                } else {
                    Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, notifications.get(position).getTaskId());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
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

        call = ApiClient.getApiService().getMyNotifications(UserManager.getUserToken(), params);
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
                    }

                    notifications.addAll(notificationResponse);

//                    Collections.sort(notifications, new Comparator<Notification>() {
//                        @Override
//                        public int compare(Notification o1, Notification o2) {
//                            return o2.getCreatedDateAt().compareTo(o1.getCreatedDateAt());
//                        }
//                    });

                    if (notificationResponse.size() > 0)
                        since = notificationResponse.get(notificationResponse.size() - 1).getCreatedAt();

                    if (notificationResponse.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        notificationAdapter.stopLoadMore();
                    }

                    refreshList();
                    LogUtils.d(TAG, "getNotifications notificationResponse size : " + notificationResponse.size());
                    LogUtils.d(TAG, "getNotifications notifications size : " + notifications.size());
//                    NotificationManager.insertNotifications(notificationResponse);

                    if (since == null) Utils.cancelAllNotification(getActivity());

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getNotifications();
                        }
                    });
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
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
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

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            lvList.smoothScrollToPosition(0);
        }
    };
}
