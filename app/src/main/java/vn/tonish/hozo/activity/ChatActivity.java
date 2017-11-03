package vn.tonish.hozo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.MessageAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 9/18/17.
 */

public class ChatActivity extends BaseTouchActivity implements View.OnClickListener {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private RecyclerView rcvMessage;
    private MessageAdapter messageAdapter;
    private ImageView btnSend;
    private EdittextHozo edtMsg;
    private ImageView imgBack;
    private int taskId;
    private List<Message> messages = new ArrayList<>();
    private boolean isLoadingMoreFromServer = true;
    private DatabaseReference myRef;
    private String lastKeyMsg;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private int posterId;
    private DatabaseReference messageCloudEndPoint;
    private TextViewHozo tvTitle;
    private boolean isLoading = false;
    private RelativeLayout mainLayout;

    private static final int PAGE_COUNT = 31;
    private ValueEventListener valueEventListener;
    private ChildEventListener childEventListener;
    private ChildEventListener memberEventListener;
    private Query recentPostsQuery = null;
    private ImageView imgMenu;
    private DatabaseReference memberCloudEndPoint;

    @Override
    protected int getLayout() {
        return R.layout.chat_activity;
    }

    @Override
    protected void initView() {
        rcvMessage = findViewById(R.id.rcv_msg);
        btnSend = findViewById(R.id.btn_send);
        edtMsg = findViewById(R.id.edt_msg);
        imgBack = findViewById(R.id.img_back);
        tvTitle = findViewById(R.id.tv_title);

        btnSend.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        mainLayout = findViewById(R.id.main_layout);

        imgMenu = findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);
        posterId = getIntent().getIntExtra(Constants.USER_ID_EXTRA, 0);
        tvTitle.setText(getIntent().getStringExtra(Constants.TITLE_INFO_EXTRA));

        LogUtils.d(TAG, "initData , taskId : " + taskId);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        messageCloudEndPoint = myRef.child("task-messages").child(String.valueOf(taskId));
        setUpMessageList();

        memberCloudEndPoint = myRef.child("members").child(String.valueOf(UserManager.getMyUser().getId()));

        memberEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                Member member = dataSnapshot.getValue(Member.class);
//                LogUtils.d(TAG, "memberEventListener onChildAdded , member : " + member.toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "memberEventListener onChildChanged , dataSnapshot : " + dataSnapshot.toString());
                Map<String, Boolean> groups = (Map<String, Boolean>) dataSnapshot.getValue();
                LogUtils.d(TAG, "memberEventListener onChildChanged , groups : " + groups.toString());
                if (groups.containsKey(String.valueOf(taskId)) && !groups.get(String.valueOf(taskId))) {
//                    DialogUtils.showOkDialog(ChatActivity.this, getString(R.string.kick_out_chat_title), getString(R.string.kick_out_chat_content), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
//                        @Override
//                        public void onSubmit() {
//
//                        }
//                    });
                    Utils.showLongToast(ChatActivity.this, getString(R.string.kick_out_chat_content), true, false);
                    setResult(Constants.RESULT_CODE_CHAT);
                    finish();
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

        memberCloudEndPoint.addChildEventListener(memberEventListener);
    }

    @Override
    protected void resumeData() {
        PreferUtils.setPushShow(this, false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferUtils.setPushShow(this, true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMessageList() {
        LogUtils.d(TAG, "setUpMessageList start");
        messageAdapter = new MessageAdapter(this, messages, posterId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rcvMessage.setLayoutManager(linearLayoutManager);
        rcvMessage.setAdapter(messageAdapter);

        edtMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    rcvMessage.smoothScrollToPosition(0);
                }
            }
        });

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.performClick();
                    Utils.hideKeyBoard(ChatActivity.this);
                    rcvMessage.requestFocus();
                    return true;
                }
                return false;
            }
        });

        rcvMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.performClick();
                    Utils.hideKeyBoard(ChatActivity.this);
                    rcvMessage.requestFocus();
                    return true;
                }
                return false;
            }
        });

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);
                if (isLoadingMoreFromServer) getMessage();

            }
        };
        rcvMessage.addOnScrollListener(endlessRecyclerViewScrollListener);

        if (childEventListener != null)
            messageCloudEndPoint.removeEventListener(childEventListener);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message message = dataSnapshot.getValue(Message.class);
                message.setId(dataSnapshot.getKey());
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , message : " + message.toString());
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , message id : " + dataSnapshot.getKey());
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , checkContain(message) : " + checkContain(message));

                if (checkContain(message)) return;

                messages.add(0, message);
                messageAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , messages size : " + messages.size());

                Map<String, Boolean> map = new HashMap<>();
                map.put(String.valueOf(UserManager.getMyUser().getId()), Boolean.valueOf(true));

                messageCloudEndPoint.child(dataSnapshot.getKey()).child("reads").child(String.valueOf(UserManager.getMyUser().getId())).setValue(true);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Message message = dataSnapshot.getValue(Message.class);
//                LogUtils.d(TAG, "messageCloudEndPoint onChildChanged , message : " + message.toString());
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

        messageCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(childEventListener);

    }

    private void getMessage() {

        if (isLoading) return;

        isLoading = true;

        valueEventListener = null;

        if (lastKeyMsg == null) {
            LogUtils.d(TAG, "getMessage start");
            recentPostsQuery = messageCloudEndPoint.orderByKey().limitToLast(PAGE_COUNT);
        } else {
            LogUtils.d(TAG, "getMessage lastKeyMsg : " + lastKeyMsg);
            recentPostsQuery = messageCloudEndPoint.orderByKey().endAt(lastKeyMsg).limitToLast(PAGE_COUNT);
        }

        if (valueEventListener != null && recentPostsQuery != null)
            recentPostsQuery.removeEventListener(valueEventListener);

        if (valueEventListener != null)
            messageCloudEndPoint.removeEventListener(valueEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Message> messagesAdded = new ArrayList<Message>();
                int i = 0;

                LogUtils.d(TAG, "addValueEventListener dataSnapshot.getChildrenCount() : " + dataSnapshot.getChildrenCount());

                if (dataSnapshot.getChildrenCount() == 0) messageAdapter.stopLoadMore();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (dataSnapshot.getChildrenCount() < PAGE_COUNT) {
                        Message message = dataSnapshot1.getValue(Message.class);
                        message.setId(dataSnapshot1.getKey());

                        LogUtils.d(TAG, "addValueEventListener recentPostsQuery : " + message.toString());
                        LogUtils.d(TAG, "addValueEventListener recentPostsQuery key : " + dataSnapshot1.getKey());

                        if (!checkContain(message))
                            messagesAdded.add(0, message);
                        isLoadingMoreFromServer = false;

                        messageAdapter.stopLoadMore();
                    } else {
                        if (i == 0) lastKeyMsg = dataSnapshot1.getKey();
                        else {
                            Message message = dataSnapshot1.getValue(Message.class);
                            message.setId(dataSnapshot1.getKey());
                            LogUtils.d(TAG, "addValueEventListener recentPostsQuery : " + message.toString());

                            if (!checkContain(message))
                                messagesAdded.add(0, message);
                        }
                    }

                    i++;

                }

                messages.addAll(messagesAdded);
                if (messageAdapter != null) messageAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "addValueEventListener messages size : " + messages.size());
                isLoading = false;


                if (valueEventListener != null && recentPostsQuery != null)
                    recentPostsQuery.removeEventListener(valueEventListener);

                if (valueEventListener != null)
                    messageCloudEndPoint.removeEventListener(valueEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isLoading = false;

                if (valueEventListener != null && recentPostsQuery != null)
                    recentPostsQuery.removeEventListener(valueEventListener);

                if (valueEventListener != null)
                    messageCloudEndPoint.removeEventListener(valueEventListener);
            }
        };

        valueEventListener = recentPostsQuery.addValueEventListener(valueEventListener);

    }

    private void doSend() {

        if (edtMsg.getText().toString().trim().equals("")) return;

        String key = messageCloudEndPoint.push().getKey();

        Message message = new Message();
        message.setUser_id(UserManager.getMyUser().getId());
        message.setMessage(edtMsg.getText().toString().trim());

        Map<String, Boolean> reads = new HashMap<>();
        reads.put(String.valueOf(UserManager.getMyUser().getId()), true);

        message.setReads(reads);

        messageCloudEndPoint.child(key).setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {

                    LogUtils.d(TAG, "onComplete databaseError : " + databaseError.toString());
                    Utils.showLongToast(ChatActivity.this, getString(R.string.permission_chat_error), true, false);
                    finish();

                }
            }
        });
        message.setId(key);

        LogUtils.d(TAG, "doSend , message : " + message.toString());

        edtMsg.setText(getString(R.string.empty));
    }

    private boolean checkContain(Message message) {
        for (int i = 0; i < messages.size(); i++)
            if (message.getId().equals(messages.get(i).getId())) return true;

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null)
            messageCloudEndPoint.removeEventListener(valueEventListener);
        if (childEventListener != null)
            messageCloudEndPoint.removeEventListener(childEventListener);
        if (memberEventListener != null)
            memberCloudEndPoint.removeEventListener(memberEventListener);
    }

    public void showMenu() {

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, imgMenu);
        popup.getMenuInflater().inflate(R.menu.menu_chat, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.menu_detail_task:
                        Intent intent = new Intent(ChatActivity.this, TaskDetailNewActivity.class);
                        intent.putExtra(Constants.TASK_ID_EXTRA, taskId);
                        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                        break;

                }
                return true;
            }
        });

        popup.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_send:
                doSend();
                break;

            case R.id.img_back:
                finish();
                break;

            case R.id.img_menu:
                showMenu();
                break;

        }
    }
}
