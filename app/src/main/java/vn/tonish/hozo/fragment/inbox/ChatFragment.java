package vn.tonish.hozo.fragment.inbox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ChatRoomAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.fragment.mytask.MyTaskFragment;
import vn.tonish.hozo.model.ChatRoom;
import vn.tonish.hozo.model.Member;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.NotifyChatRoomResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.MyLinearLayoutManager;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.DataParse.coverAssignerToMember;
import static vn.tonish.hozo.utils.Utils.sortID;

/**
 * Created by LongBui on 9/16/17.
 */

public class ChatFragment extends BaseFragment {

    private static final String TAG = ChatFragment.class.getSimpleName();
    private RecyclerView rcvChatRooms;
    private List<TaskResponse> taskResponses = new ArrayList<>();
    private Call<List<TaskResponse>> call;
    private TextViewHozo tvNoData;
    private MyLinearLayoutManager lvManager;
    private ChatRoomAdapter chatRoomAdapter;
    private DatabaseReference horizotalReference, verticalReference;
    private ChildEventListener VerticalListener, HorizotalListener;
    private List<ChatRoom> chatRooms = new ArrayList<>();
    private int myID;
    private ToggleButton tgOnOffNotify;

    @Override
    protected int getLayout() {
        return R.layout.chat_fragment;
    }

    @Override
    protected void initView() {
        rcvChatRooms = (RecyclerView) findViewById(R.id.rcv_chat_rooms);
        tvNoData = (TextViewHozo) findViewById(R.id.tv_no_data);
        tgOnOffNotify = (ToggleButton) findViewById(R.id.tg_on_off);
        createSwipeToRefresh();
    }

    @Override
    protected void initData() {
        myID = UserManager.getMyUser().getId();
        tgOnOffNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d(TAG, "tgOnOffNotify : " + tgOnOffNotify.isChecked());
                updateNofityOnOff(tgOnOffNotify.isChecked());
            }
        });
    }

    @Override
    protected void resumeData() {
        getChatRooms();
        getNotifyOnOff();
        getActivity().registerReceiver(broadcastReceiverSmoothToTop, new IntentFilter(Constants.BROAD_CAST_SMOOTH_TOP_CHAT));
    }

    private void getNotifyOnOff() {
        Call<NotifyChatRoomResponse> callNotify = ApiClient.getApiService().getChatRoomsNotify(UserManager.getUserToken());
        callNotify.enqueue(new Callback<NotifyChatRoomResponse>() {
            @Override
            public void onResponse(Call<NotifyChatRoomResponse> call, Response<NotifyChatRoomResponse> response) {
                LogUtils.d(TAG, "getNotifyOnOff code : " + response.code());
                LogUtils.d(TAG, "getNotifyOnOff body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    NotifyChatRoomResponse notifyChatRoomResponse = response.body();
                    tgOnOffNotify.setChecked(notifyChatRoomResponse.isNotificationChatGroup());
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
            public void onFailure(Call<NotifyChatRoomResponse> call, Throwable t) {
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
            jsonRequest.put("notification_chat", isOnOff);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "updateNofityOnOff data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().updateChatRoomsNotify(UserManager.getUserToken(), body).enqueue(new Callback<NotifyChatRoomResponse>() {
            @Override
            public void onResponse(Call<NotifyChatRoomResponse> call, Response<NotifyChatRoomResponse> response) {

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
            public void onFailure(Call<NotifyChatRoomResponse> call, Throwable t) {
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

    private void getChatRooms() {
        call = ApiClient.getApiService().getChatRooms(UserManager.getUserToken());
        call.enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
//                LogUtils.d(TAG, "getChatRooms code : " + response.code());
//                LogUtils.d(TAG, "getChatRooms body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponses.clear();
                    chatRooms.clear();
                    taskResponses = response.body();
                    chatRooms.addAll(coverTasksToChatRooms(taskResponses));
                    updateUI();
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
                    LogUtils.d(TAG, "getChatRooms code : " + response.code());
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
                LogUtils.e(TAG, "getChatRooms , onFailure : " + t.getMessage());
                if (!t.getMessage().equalsIgnoreCase("Canceled"))
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

    private void updateUI() {
        if (chatRoomAdapter == null) {
            chatRoomAdapter = new ChatRoomAdapter(getActivity(), chatRooms);
            lvManager = new MyLinearLayoutManager(getActivity());
            rcvChatRooms.setLayoutManager(lvManager);
            rcvChatRooms.setAdapter(chatRoomAdapter);
            if (taskResponses.size() > 0) {
                rcvChatRooms.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
            } else {
                rcvChatRooms.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < chatRooms.size(); i++) {
                memberVerticalListener(chatRooms.get(i), taskResponses.get(i).getPoster().getId());
                groupListener(chatRooms.get(i));
            }
        }

    }

    private List<ChatRoom> coverTasksToChatRooms(List<TaskResponse> taskResponses) {
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (TaskResponse taskResponse : taskResponses
                ) {
            chatRooms.add(coverTasksToChatRoom(taskResponse));
        }
        return chatRooms;

    }

    private ChatRoom coverTasksToChatRoom(TaskResponse response) {
        ChatRoom chatRoom = new ChatRoom();
        List<Member> memberList = new ArrayList<>();
        Member member = new Member();
        member.setFull_name(getString(R.string.group_chat));
        member.setId(response.getId());
        memberList.add(0, member);
        chatRoom.setId(response.getId());
        chatRoom.setName(response.getTitle());
        chatRoom.setTaskResponse(response);
        chatRoom.setCreated_at(response.getStartTime());
        if (response.getWorkerCount() > 1) {
            if (response.getPoster().getId() == myID)
                memberList.addAll(coverAssignerToMember(response.getAssignees()));
            else {
                member = new Member();
                member.setFull_name(response.getPoster().getFullName());
                member.setId(response.getPoster().getId());
                member.setAvatar(response.getPoster().getAvatar());
                member.setPhone(response.getPoster().getPhone());
                memberList.add(member);
            }
        }
        chatRoom.setMembers(memberList);
        return chatRoom;

    }

    private void memberVerticalListener(final ChatRoom chatRoom, int posterID) {
        int Id;
        if (posterID == myID)
            Id = posterID;
        else Id = myID;
        //--------list chat member-----------
        final List<Member> list = chatRoom.getMembers();
        for (int i = 0; i < list.size(); i++) {
            final int p = i;
            VerticalListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        LogUtils.d(TAG, "check onChildAdded" + dataSnapshot.toString());
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setId(dataSnapshot.getKey());
                        list.get(p).setMessage(message);
                        if (chatRoomAdapter != null) chatRoomAdapter.notifyDataSetChanged();
                        sendBroasdCast();
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        LogUtils.d(TAG, "check onChildChanged" + dataSnapshot.toString());
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setId(dataSnapshot.getKey());
                        list.get(p).setMessage(message);
                        if (chatRoomAdapter != null) chatRoomAdapter.notifyDataSetChanged();
                        sendBroasdCast();
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            verticalReference = FirebaseDatabase.getInstance().getReference().child("private-messages").child(String.valueOf(chatRoom.getTaskResponse().getId())).child(sortID(Id, list.get(p).getId()));
            verticalReference.orderByKey().limitToLast(1).addChildEventListener(VerticalListener);


        }
    }

    private void groupListener(final ChatRoom chatRoom) {
        final List<Member> list = chatRoom.getMembers();
        //--------list member assiger-----------
        HorizotalListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "check onChildAdded groupListener" + dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setId(dataSnapshot.getKey());
                    list.get(0).setMessage(message);
                    if (chatRoomAdapter != null) chatRoomAdapter.notifyDataSetChanged();
                    sendBroasdCast();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "check onChildAdded groupListener" + dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setId(dataSnapshot.getKey());
                    list.get(0).setMessage(message);
                    if (chatRoomAdapter != null) chatRoomAdapter.notifyDataSetChanged();
                    sendBroasdCast();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        horizotalReference = FirebaseDatabase.getInstance().getReference().child("task-messages").child(String.valueOf(chatRoom.getTaskResponse().getId()));
        horizotalReference.orderByKey().limitToLast(1).addChildEventListener(HorizotalListener);
    }

    @Override
    public void onStop() {
        LogUtils.d(TAG, "onStop()");
        super.onStop();
        try {
            getActivity().unregisterReceiver(broadcastReceiverSmoothToTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (HorizotalListener != null && horizotalReference != null)
            horizotalReference.removeEventListener(HorizotalListener);
        if (VerticalListener != null && verticalReference != null)
            verticalReference.removeEventListener(VerticalListener);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (call != null) call.cancel();
        call = null;
        chatRoomAdapter = null;
        if (HorizotalListener != null && horizotalReference != null) {
            horizotalReference.removeEventListener(HorizotalListener);
        }
        if (VerticalListener != null && verticalReference != null) {
            verticalReference.removeEventListener(VerticalListener);
        }
        getChatRooms();

    }

    private int getCountRoomUnRead() {
        int count = 0;
        for (ChatRoom room : chatRooms
                ) {
            for (Member member : room.getMembers()
                    ) {
                if (member.getMessage() != null) {
                    Map<String, Boolean> map = member.getMessage().getReads();
                    if ((map != null && !map.containsKey(String.valueOf(UserManager.getMyUser().getId())))) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            openFragment(R.id.layout_container, MyTaskFragment.class, new Bundle(), false, TransitionScreen.RIGHT_TO_LEFT);
            updateMenuUi(3);
        }
    }

    private void sendBroasdCast() {
        Intent intentNewMsg = new Intent();
        intentNewMsg.setAction(Constants.BROAD_CAST_PUSH_CHAT_COUNT);
        intentNewMsg.putExtra(Constants.COUNT_NEW_CHAT_EXTRA, getCountRoomUnRead());
        if (getActivity() != null) getActivity().sendBroadcast(intentNewMsg);
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
