package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ChatPrivateAdapter;
import vn.tonish.hozo.adapter.MemberAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Poster;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.sortID;

/**
 * Created by CanTran on 1/4/18.
 */

public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ContactsActivity.class.getSimpleName();
    private TaskResponse taskResponse;
    private final List<Assigner> assigners = new ArrayList<>();
    private final List<Assigner> chatAssigners = new ArrayList<>();
    private RecyclerView rcvMember, rcvChat;
    private TextViewHozo tvTaskName, tvCount;
    private int posterID, myUserID;
    private ChatPrivateAdapter chatPrivateAdapter;
    private DatabaseReference assignersReference, messageReference, groupTaskReference;
    private ChildEventListener messageListener, groupTaskListener, assignersListener;


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
        ImageView imgCall = (ImageView) findViewById(R.id.img_call);
        ImageView imgSms = (ImageView) findViewById(R.id.img_sms);
        imgCall.setOnClickListener(this);
        imgSms.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.TASK_DETAIL_EXTRA);
        posterID = taskResponse.getPoster().getId();
        myUserID = UserManager.getMyUser().getId();
        updateListMembers();
        updateMessageRoom();
        checkTask();

    }

    private void addGroup(List<Assigner> list) {
        Assigner assigner = new Assigner();
        assigner.setFullName(getString(R.string.group_chat));
        assigner.setId(taskResponse.getId());
        list.add(0, assigner);
    }


    private void updateListMembers() {
        assigners.clear();
        Assigner assigner;
        Poster poster = taskResponse.getPoster();
        tvTaskName.setText(taskResponse.getTitle());
        if (posterID == myUserID)
            assigners.addAll(taskResponse.getAssignees());
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
                    startActivity(intentContact,TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    intentContact.putExtra(Constants.ASSIGNER_EXTRA, assigners.get(position));
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }
            }
        });

    }

    private void updateMessageRoom() {
        chatPrivateAdapter = new ChatPrivateAdapter(this, taskResponse.getId(), taskResponse.getPoster().getId(), chatAssigners);
        LinearLayoutManager layoutManagaer
                = new LinearLayoutManager(ContactsActivity.this, LinearLayoutManager.VERTICAL, false);
        rcvChat.setLayoutManager(layoutManagaer);
        rcvChat.setAdapter(chatPrivateAdapter);
        chatPrivateAdapter.setChatPrivateListener(new ChatPrivateAdapter.ChatPrivateListener() {
            @Override
            public void onClick(int position) {
                if (chatAssigners.get(position).getId() == taskResponse.getId() && chatAssigners.get(position).getFullName().equalsIgnoreCase(getString(R.string.group_chat))) {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    intentContact.putExtra(Constants.ASSIGNER_EXTRA, chatAssigners.get(position));
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }

            }
        });

        groupListener();

        //--------list chat assiger-----------

        messageListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "dataSnapshot" + dataSnapshot.getKey() + "assigners" + assigners.toString());
                for (Assigner assigner1 : assigners
                        ) {
                    if (dataSnapshot.getKey().equalsIgnoreCase(sortID(myUserID, assigner1.getId()))) {
                        chatAssigners.add(assigner1);
                        chatPrivateAdapter.notifyDataSetChanged();
                    }
                }
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
        };
        messageReference = FirebaseDatabase.getInstance().getReference();
        messageReference.child("private-messages").child(String.valueOf(taskResponse.getId())).addChildEventListener(messageListener);

    }

    private void groupListener() {
        //--------list member assiger-----------
        assignersListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "dataSnapshot-getChildrenCount" + dataSnapshot.getChildrenCount());
                if (dataSnapshot.getChildrenCount() > 0 && (chatAssigners.size() == 0 || chatAssigners.get(0).getId() != taskResponse.getId())) {
                    addGroup(chatAssigners);
                    chatPrivateAdapter.notifyDataSetChanged();
                }
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
        };

        assignersReference = FirebaseDatabase.getInstance().getReference().child("task-messages").child(String.valueOf(taskResponse.getId()));
        assignersReference.addChildEventListener(assignersListener);
    }

    private void checkTask() {
        LogUtils.d(TAG, "checkTask start , task id : " + taskResponse.getId());
        groupTaskListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()) {
                    case "block":
                        boolean block = (boolean) dataSnapshot.getValue();
                        if (block) {
                            Utils.showLongToast(ContactsActivity.this, getString(R.string.block_task), true, false);
                            finish();
                        }
                        break;
                    case "close":
                        boolean close = (boolean) dataSnapshot.getValue();
                        if (close) {
                            Utils.showLongToast(ContactsActivity.this, getString(R.string.close_task), true, false);
                            finish();
                        }
                        break;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                LogUtils.d(TAG, "checkTask members  add, task id : " + dataSnapshot.toString());
                switch (dataSnapshot.getKey()) {
                    case "block":
                        boolean block = (boolean) dataSnapshot.getValue();
                        if (block) {
                            Utils.showLongToast(ContactsActivity.this, getString(R.string.block_task), true, false);
                            finish();
                        }
                        break;
                    case "close":
                        boolean close = (boolean) dataSnapshot.getValue();
                        if (close) {
                            Utils.showLongToast(ContactsActivity.this, getString(R.string.close_task), true, false);
                            finish();
                        }
                        break;
                    case "members":
                        Map<String, Boolean> members = (Map<String, Boolean>) dataSnapshot.getValue();
                        if (members.containsKey(String.valueOf(posterID)) && !members.get(String.valueOf(posterID))) {
                            Utils.showLongToast(ContactsActivity.this, getString(R.string.kick_out_task_content), true, false);
                            finish();
                        }

                        break;

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
        groupTaskReference = FirebaseDatabase.getInstance().getReference().child("groups-task").child(String.valueOf(taskResponse.getId()));
        groupTaskReference.addChildEventListener(groupTaskListener);
    }

    private final BroadcastReceiver broadcastCountNewMsg = new BroadcastReceiver() {

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
        registerReceiver(broadcastCountNewMsg, new IntentFilter(Constants.BROAD_CAST_PUSH_CHAT_COUNT));
        LogUtils.d(TAG, "ChatFragment resumeData start");

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            unregisterReceiver(broadcastCountNewMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (assignersListener != null && assignersReference != null)
            assignersReference.removeEventListener(assignersListener);
        if (messageListener != null && messageReference != null)
            messageReference.removeEventListener(messageListener);
        if (groupTaskListener != null && groupTaskReference != null)
            groupTaskReference.removeEventListener(groupTaskListener);
        chatPrivateAdapter.killListener();
    }

    private void doCall() {
        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
        } else {
            Utils.call(this, taskResponse.getPoster().getPhone());
        }
    }

    private void doSms() {
        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
        } else {
            Utils.sendSms(this, taskResponse.getPoster().getPhone(), "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_call:
                doCall();
                break;

            case R.id.img_sms:
                doSms();
                break;

        }

    }
}
