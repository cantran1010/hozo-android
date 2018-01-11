package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ChatPrivateAdapter;
import vn.tonish.hozo.adapter.MemberAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Taskgroup;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Poster;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.sortID;

/**
 * Created by CanTran on 1/4/18.
 */

public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ContactsActivity.class.getSimpleName();
    private TaskResponse taskResponse;
    private List<Assigner> assigners = new ArrayList<>();
    private RecyclerView rcvMember, rcvChat;
    private TextViewHozo tvTaskName, tvCount;
    private int posterID, myUserID;


    @Override
    protected int getLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        rcvMember = (RecyclerView) findViewById(R.id.rcv_member);
        rcvChat = (RecyclerView) findViewById(R.id.rcv_chat);
        tvTaskName = (TextViewHozo) findViewById(R.id.tv_task_name);
        tvCount = (TextViewHozo) findViewById(R.id.tv_count);
        findViewById(R.id.img_back).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.TASK_DETAIL_EXTRA);
        posterID = taskResponse.getPoster().getId();
        myUserID = UserManager.getMyUser().getId();
        updateListAssigners();
        updateMessageRoom();
        checkTask();

    }

    private void addGroup(List<Assigner> list) {
        Assigner assigner = new Assigner();
        assigner.setFullName(getString(R.string.group_chat));
        assigner.setId(taskResponse.getId());
        list.add(0, assigner);
    }


    private void updateListAssigners() {
        Assigner assigner;
        Poster poster = taskResponse.getPoster();
        tvTaskName.setText(taskResponse.getTitle());
        if (posterID == myUserID)
            assigners = taskResponse.getAssignees();
        else {
            assigner = new Assigner();
            assigner.setId(poster.getId());
            assigner.setAvatar(poster.getAvatar());
            assigner.setFullName(poster.getFullName());
            assigner.setPhone(poster.getPhone());
            assigners.add(assigner);
        }
        addGroup(assigners);
        MemberAdapter memberAdapter = new MemberAdapter(ContactsActivity.this, assigners);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(ContactsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rcvMember.setLayoutManager(horizontalLayoutManagaer);
        rcvMember.setAdapter(memberAdapter);
        memberAdapter.setMemberAdapterListener(new MemberAdapter.memberAdapterListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    startActivityForResult(intentContact, Constants.REQUEST_CODE_CHAT, TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                    intentContact.putExtra(Constants.ASSIGNER_PRIVATE_ID, assigners.get(position).getId());
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }
            }
        });

    }

    private void updateMessageRoom() {
        final List<Assigner> chatAssigners = new ArrayList<>();
        final ChatPrivateAdapter chatPrivateAdapter = new ChatPrivateAdapter(this, taskResponse.getId(), taskResponse.getPoster().getId(), chatAssigners);
        LinearLayoutManager layoutManagaer
                = new LinearLayoutManager(ContactsActivity.this, LinearLayoutManager.VERTICAL, false);
        rcvChat.setLayoutManager(layoutManagaer);
        rcvChat.setAdapter(chatPrivateAdapter);
        chatPrivateAdapter.setChatPrivateListener(new ChatPrivateAdapter.ChatPrivateListener() {
            @Override
            public void onClick(int position) {
                if (chatAssigners.get(position).getId() == taskResponse.getId()) {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    startActivityForResult(intentContact, Constants.REQUEST_CODE_CHAT, TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                    intentContact.putExtra(Constants.ASSIGNER_PRIVATE_ID, chatAssigners.get(position).getId());
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }

            }
        });

        FirebaseDatabase.getInstance().getReference().child("task-messages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(String.valueOf(taskResponse.getId())).exists()) {
                    addGroup(chatAssigners);
                    chatPrivateAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("private-messages").child(String.valueOf(taskResponse.getId())).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "dataSnapshot" + dataSnapshot.getKey() + "assigners" + assigners.toString());
                int id;
                if (posterID == myUserID)
                    id = posterID;
                else id = myUserID;
                for (Assigner assigner1 : assigners
                        ) {
                    if (dataSnapshot.getKey().equalsIgnoreCase(sortID(id, assigner1.getId()))) {
                        chatAssigners.add(assigner1);
                    }
                }
                chatPrivateAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "dataSnapshot" + chatAssigners.toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });

    }

    private void checkTask() {
        FirebaseDatabase.getInstance().getReference().child("groups-task").child(String.valueOf(taskResponse.getId())).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Taskgroup map = dataSnapshot.getValue(Taskgroup.class);
//                String descrip = (String) map.get("title");
             LogUtils.d(TAG, "groupTask" + map.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });

    }

    private final BroadcastReceiver broadcastReceiverSmsCount = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG, "broadcastReceiverSmsCount onReceive");
            if (intent.hasExtra(Constants.COUNT_NEW_CHAT_EXTRA)) {
                int count = intent.getIntExtra(Constants.COUNT_NEW_CHAT_EXTRA, 0);
                tvCount.setText(String.valueOf(count));
                LogUtils.d(TAG, "broadcastReceiverSmsCount onReceive" + count);
            }


        }
    };

    @Override
    protected void resumeData() {
        registerReceiver(broadcastReceiverSmsCount, new IntentFilter(Constants.BROAD_CAST_PUSH_CHAT_COUNT));
        LogUtils.d(TAG, "ChatFragment resumeData start");

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadcastReceiverSmsCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

        }

    }
}
