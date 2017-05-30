package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
import vn.tonish.hozo.adapter.NotificationAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.NotificationEntity;
import vn.tonish.hozo.database.manager.NotificationManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/4/2017.
 */

public class InboxFragment extends BaseFragment {

    private static final String TAG = InboxFragment.class.getSimpleName();

    protected LinearLayoutManager linearLayoutManager;
    private NotificationAdapter notificationAdapter;
    protected RecyclerView lvList;
    private List<Notification> notifications = new ArrayList<>();
    protected TextViewHozo tvUnread;
    private String since;
    private Date sinceDate;
    public static final int LIMIT = 15;
    boolean isLoadingMoreFromServer = true;
    boolean isLoadingMoreFromDb = true;
    boolean isLoadingFromServer = false;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_notify;
    }

    @Override
    protected void initView() {
        tvUnread = (TextViewHozo) findViewById(R.id.tv_unread);
        lvList = (RecyclerView) findViewById(R.id.lvList);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        getCacheDataPage(sinceDate);
        getNotifications(false);
    }

//    private void getCacheDataFirstPage() {
//        LogUtils.d(TAG, "getCacheDataFirstPage start");
//        List<NotificationEntity> notificationEntities = NotificationManager.getFirstPage();
//        if (notificationEntities.size() > 0)
//            sinceDate = notificationEntities.get(notificationEntities.size() - 1).getCreatedAt();
//        notifications = DataParse.converListNotificationEntity(notificationEntities);
//
//        if (notificationEntities.size() < LIMIT) isLoadingMoreFromDb = false;
//        refreshList();
//    }

    private void getCacheDataPage(Date sinceDateSearch) {
        LogUtils.d(TAG, "getCacheDataPage start");
        List<NotificationEntity> notificationEntities = NotificationManager.getNotificationsSince(sinceDateSearch);
        LogUtils.d(TAG, "getCacheDataPage notificationEntities : " + notificationEntities.toString());
        LogUtils.d(TAG, "getCacheDataPage notificationEntities size : " + notificationEntities.size());

        if (notificationEntities.size() > 0)
            sinceDate = notificationEntities.get(notificationEntities.size() - 1).getCreatedAt();

        if (notificationEntities.size() < LIMIT) isLoadingMoreFromDb = false;

        notifications.addAll(DataParse.converListNotificationEntity(notificationEntities));
        refreshList();
    }

    public void getNotifications(final boolean isSince) {

        if (isLoadingFromServer) return;
        isLoadingFromServer = true;
        notificationAdapter.stopLoadMore();

        LogUtils.d(TAG, "getNotifications start");
        Map<String, String> params = new HashMap<>();

        if (isSince && since != null)
            params.put("since", since);

        params.put("limit", LIMIT + "");

        ApiClient.getApiService().getMyNotifications(UserManager.getUserToken(), params).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                LogUtils.d(TAG, "getNotifications code : " + response.code());
                LogUtils.d(TAG, "getNotifications body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {

                    List<Notification> notificationResponse = response.body();
                    for (int i = 0; i < notificationResponse.size(); i++) {
                        if (!checkContainsNotification(notifications, notificationResponse.get(i)))
                            notifications.add(notificationResponse.get(i));
                    }
                    if (notificationResponse.size() > 0)
                        since = notificationResponse.get(notificationResponse.size() - 1).getCreatedAt();

                    if (notificationResponse.size() < LIMIT) {
                        isLoadingMoreFromServer = false;
                        notificationAdapter.stopLoadMore();
                    }

                    refreshList();

                    NotificationManager.insertNotifications(DataParse.convertListNotification(notificationResponse));

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getNotifications(isSince);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getNotifications(isSince);
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
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                LogUtils.e(TAG, "getNotifications , onFailure : " + t.getMessage());
//                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
//                    @Override
//                    public void onSubmit() {
//                        getNotifications(isSince);
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });

                notificationAdapter.stopLoadMore();
                isLoadingFromServer = false;
                onStopRefresh();
            }
        });
    }

    private void refreshList() {
        if (notificationAdapter == null) {
            notificationAdapter = new NotificationAdapter(getActivity(), notifications);
            linearLayoutManager = new LinearLayoutManager(getActivity());
            lvList.setLayoutManager(linearLayoutManager);
            lvList.setAdapter(notificationAdapter);

            lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);

                    if (isLoadingMoreFromDb) getCacheDataPage(sinceDate);
                    if (isLoadingMoreFromServer) getNotifications(true);

                }
            });

            notificationAdapter.setNotificationAdapterListener(new NotificationAdapter.NotificationAdapterListener() {
                @Override
                public void onNotificationAdapterListener(int position) {
                    Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, notifications.get(position).getTaskId());
                    intent.putExtra(Constants.TASK_TYPE, Constants.TASK_TYPE_ONLY_VIEW);
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
            });

        } else {
            notificationAdapter.notifyDataSetChanged();
        }

        LogUtils.d(TAG, "refreshList , notification size : " + notifications.size());

    }

    private boolean checkContainsNotification(List<Notification> notifications, Notification notification) {
        for (int i = 0; i < notifications.size(); i++)
            if (notifications.get(i).getId() == notification.getId()) return true;
        return false;
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getNotifications(false);
    }
}
