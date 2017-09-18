package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.MessageAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;

/**
 * Created by LongBui on 9/18/17.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ChatActivity.class.getSimpleName();
    private RecyclerView rcvMessage;
    private MessageAdapter messageAdapter;
    private ButtonHozo btnSend;
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

    private static final int PAGE_COUNT = 13;

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

        btnSend.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        taskId = getIntent().getIntExtra(Constants.TASK_ID_EXTRA, 0);
        posterId = getIntent().getIntExtra(Constants.USER_ID_EXTRA, 0);

        LogUtils.d(TAG, "initData , taskId : " + taskId);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        messageCloudEndPoint = myRef.child("task-messages").child(String.valueOf(taskId));

        setUpMessageList();
    }

    @Override
    protected void resumeData() {

    }

    private void setUpMessageList() {
        LogUtils.d(TAG, "setUpMessageList start");
        messageAdapter = new MessageAdapter(this, messages, posterId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rcvMessage.setLayoutManager(linearLayoutManager);
        rcvMessage.setAdapter(messageAdapter);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);
                if (isLoadingMoreFromServer) getMessage();

            }
        };
        rcvMessage.addOnScrollListener(endlessRecyclerViewScrollListener);

    }

    private void getMessage() {

        Query recentPostsQuery;
        if (lastKeyMsg == null) {
            LogUtils.d(TAG, "getMessage start");
            recentPostsQuery = myRef.child("task-messages").child(String.valueOf(taskId)).orderByKey().limitToLast(PAGE_COUNT);
        } else {
            LogUtils.d(TAG, "getMessage lastKeyMsg : " + lastKeyMsg);
            recentPostsQuery = myRef.child("task-messages").child(String.valueOf(taskId)).orderByKey().endAt(lastKeyMsg).limitToLast(PAGE_COUNT);
        }

        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Message> messagesAdded = new ArrayList<Message>();
                int i = 0;

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    LogUtils.d(TAG, "addValueEventListener dataSnapshot.getChildrenCount() : " + dataSnapshot.getChildrenCount());

                    if (dataSnapshot.getChildrenCount() < PAGE_COUNT) {
                        Message message = dataSnapshot1.getValue(Message.class);

                        LogUtils.d(TAG, "addValueEventListener recentPostsQuery : " + message.toString());

                        messagesAdded.add(0, message);
                        isLoadingMoreFromServer = false;

                        messageAdapter.stopLoadMore();
                    } else {
                        if (i == 0) lastKeyMsg = dataSnapshot1.getKey();
                        else {
                            Message message = dataSnapshot1.getValue(Message.class);

                            LogUtils.d(TAG, "addValueEventListener recentPostsQuery : " + message.toString());

                            messagesAdded.add(0, message);
                        }
                    }

                    i++;

                }

                messages.addAll(messagesAdded);
                if (messageAdapter != null) messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void doSend() {

        if (edtMsg.getText().toString().trim().equals("")) return;

        String key = messageCloudEndPoint.push().getKey();

        Message message = new Message(UserManager.getMyUser().getId(), edtMsg.getText().toString().trim(), ServerValue.TIMESTAMP);
        messageCloudEndPoint.child(key).setValue(message);

        LogUtils.d(TAG, "doSend , message : " + message.toString());
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

        }
    }
}
