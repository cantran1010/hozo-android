package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.ChatActivity;
import vn.tonish.hozo.adapter.ChatRoomAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 9/16/17.
 */

public class ChatFragment extends BaseFragment {

    private static final String TAG = ChatFragment.class.getSimpleName();
    private RecyclerView rcvChatRooms;
    private ChatRoomAdapter chatRoomAdapter;
    private List<TaskResponse> taskResponses = new ArrayList<>();
    private Call<List<TaskResponse>> call;
    private TextViewHozo tvNoData;
    private LinearLayoutManager lvManager;

    @Override
    protected int getLayout() {
        return R.layout.chat_fragment;
    }

    @Override
    protected void initView() {
        rcvChatRooms = (RecyclerView) findViewById(R.id.rcv_chat_rooms);
        tvNoData = (TextViewHozo) findViewById(R.id.tv_no_data);

        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_CHAT));
        LogUtils.d(TAG, "ChatFragment resumeData start");
        getChatRooms();
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

    private void getChatRooms() {
        call = ApiClient.getApiService().getChatRooms(UserManager.getUserToken());
        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                LogUtils.d(TAG, "getChatRooms code : " + response.code());
                LogUtils.d(TAG, "getChatRooms body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    PreferUtils.setNewPushChatCount(getActivity(), 0);
                    taskResponses = response.body();
                    LogUtils.d(TAG, "getChatRooms taskResponsesBody size : " + taskResponses.size());
                    refreshChatRooms();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getChatRooms();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getChatRooms();
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
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getChatRooms();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                onStopRefresh();
            }
        });
    }

    private void refreshChatRooms() {
        chatRoomAdapter = new ChatRoomAdapter(getActivity(), taskResponses);
        lvManager = new LinearLayoutManager(getActivity());
        rcvChatRooms.setLayoutManager(lvManager);
        rcvChatRooms.setAdapter(chatRoomAdapter);

        chatRoomAdapter.setChatRoomListener(new ChatRoomAdapter.ChatRoomListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponses.get(position).getId());
                intent.putExtra(Constants.USER_ID_EXTRA, taskResponses.get(position).getPoster().getId());
                intent.putExtra(Constants.TITLE_INFO_EXTRA, taskResponses.get(position).getTitle());
                startActivity(intent, TransitionScreen.DOWN_TO_UP);
            }
        });

        if (taskResponses.size() > 0) {
            rcvChatRooms.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            rcvChatRooms.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        taskResponses.clear();
        getChatRooms();
    }

    private final BroadcastReceiver broadcastReceiverSmoothToTop = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG, "broadcastReceiverSmoothToTop onReceive");
            rcvChatRooms.smoothScrollToPosition(0);
            if (lvManager.findFirstVisibleItemPosition() == 0) onRefresh();
        }
    };

}
