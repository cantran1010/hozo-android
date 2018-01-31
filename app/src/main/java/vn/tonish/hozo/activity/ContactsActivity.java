package vn.tonish.hozo.activity;

import android.content.Intent;
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
import vn.tonish.hozo.adapter.MemberAdapter;
import vn.tonish.hozo.adapter.MemberVerticalAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Member;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.rest.responseRes.Poster;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.DataParse.coverAssignerToMember;
import static vn.tonish.hozo.utils.Utils.sortID;

/**
 * Created by CanTran on 1/4/18.
 */

public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ContactsActivity.class.getSimpleName();
    private TaskResponse taskResponse;
    private final List<Member> members = new ArrayList<>();
    private final List<Member> chatMembers = new ArrayList<>();
    private RecyclerView rcvMember, rcvChat;
    private TextViewHozo tvTaskName, tvCount;
    private int posterID, myUserID;
    private MemberVerticalAdapter chatPrivateAdapter;
    private DatabaseReference horizotalReference, verticalReference, groupTaskReference;
    private ChildEventListener VerticalListener, groupTaskListener, HorizotalListener;
    private int sortId;


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
        if (posterID == UserManager.getMyUser().getId())
            sortId = posterID;
        else sortId = UserManager.getMyUser().getId();
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.TASK_DETAIL_EXTRA);
        posterID = taskResponse.getPoster().getId();
        myUserID = UserManager.getMyUser().getId();
        updateListMembers();
        updateMessageRoom();
        checkTask();
    }

    @Override
    protected void resumeData() {

    }

    private void addGroup(List<Member> list) {
        Member assigner = new Member();
        assigner.setFull_name(getString(R.string.group_chat));
        assigner.setId(taskResponse.getId());
        list.add(0, assigner);
    }


    private void updateListMembers() {
        members.clear();
        Member member;
        Poster poster = taskResponse.getPoster();
        tvTaskName.setText(taskResponse.getTitle());
        if (posterID == myUserID)
            members.addAll(coverAssignerToMember(taskResponse.getAssignees()));
        else {
            member = new Member();
            member.setId(poster.getId());
            member.setAvatar(poster.getAvatar());
            member.setFull_name(poster.getFullName());
            member.setPhone(poster.getPhone());
            members.add(member);
        }
        addGroup(members);
        MemberAdapter memberAdapter = new MemberAdapter(ContactsActivity.this, members);
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
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    intentContact.putExtra(Constants.ASSIGNER_EXTRA, members.get(position));
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }
            }
        });

    }

    private void updateMessageRoom() {
        chatPrivateAdapter = new MemberVerticalAdapter(this, chatMembers);
        LinearLayoutManager layoutManagaer
                = new LinearLayoutManager(ContactsActivity.this, LinearLayoutManager.VERTICAL, false);
        rcvChat.setLayoutManager(layoutManagaer);
        rcvChat.setAdapter(chatPrivateAdapter);
        chatPrivateAdapter.setMemberVerticalListener(new MemberVerticalAdapter.ChatPrivateListener() {
            @Override
            public void onClick(int position) {
                if (chatMembers.get(position).getId() == taskResponse.getId() && chatMembers.get(position).getFull_name().equalsIgnoreCase(getString(R.string.group_chat))) {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(ContactsActivity.this, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    intentContact.putExtra(Constants.ASSIGNER_EXTRA, chatMembers.get(position));
                    startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }

            }
        });

        groupListener();
        memberVerticalListener();


    }

    private void memberVerticalListener() {
        //--------list chat assiger-----------
        for (final Member member : members
                ) {

            VerticalListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    LogUtils.d(TAG, "VerticalListener , onChildAdded : " + dataSnapshot.toString());
                    if (dataSnapshot.exists()) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setId(dataSnapshot.getKey());
                        member.setMessage(message);
                        if (!chatMembers.contains(member)) {
                            chatMembers.add(member);
                        }
                        chatPrivateAdapter.notifyDataSetChanged();
                    }
                    countReadSms();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    LogUtils.d(TAG, "VerticalListener , onChildChanged : " + dataSnapshot.toString());
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setId(dataSnapshot.getKey());
                    member.setMessage(message);
                    chatPrivateAdapter.notifyDataSetChanged();
                    countReadSms();

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
            verticalReference = FirebaseDatabase.getInstance().getReference().child("private-messages").child(String.valueOf(taskResponse.getId())).child(sortID(sortId, member.getId()));
            verticalReference.orderByKey().limitToLast(1).addChildEventListener(VerticalListener);


        }
    }

    private void groupListener() {
        //--------list member assiger-----------
        HorizotalListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "HorizotalListener , onChildAdded : " + dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    if (chatMembers.size() == 0 || !chatMembers.get(0).getFull_name().equalsIgnoreCase(getString(R.string.group_chat)))
                        addGroup(chatMembers);
                    Message message = dataSnapshot.getValue(Message.class);
                    message.setId(dataSnapshot.getKey());
                    chatMembers.get(0).setMessage(message);
                    chatPrivateAdapter.notifyDataSetChanged();
                    countReadSms();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "HorizotalListener , onChildChanged : " + dataSnapshot.toString());
                Message message = dataSnapshot.getValue(Message.class);
                message.setId(dataSnapshot.getKey());
                chatMembers.get(0).setMessage(message);
                chatPrivateAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "HorizotalListener , onChildChanged : " + chatMembers.get(0).toString());
                countReadSms();
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

        horizotalReference = FirebaseDatabase.getInstance().getReference().child("task-messages").child(String.valueOf(taskResponse.getId()));
        horizotalReference.orderByKey().limitToLast(1).addChildEventListener(HorizotalListener);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (HorizotalListener != null && horizotalReference != null)
            horizotalReference.removeEventListener(HorizotalListener);
        if (VerticalListener != null && verticalReference != null)
            verticalReference.removeEventListener(VerticalListener);
        if (groupTaskListener != null && groupTaskReference != null)
            groupTaskReference.removeEventListener(groupTaskListener);
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

    private void countReadSms() {
        LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint map : " + chatMembers.toString());
        int n = 0;
        for (Member member : chatMembers
                ) {
            Map<String, Boolean> map = member.getMessage().getReads();
            if ((map != null && !map.containsKey(String.valueOf(UserManager.getMyUser().getId())))) {
                n++;
            }
        }
        tvCount.setText(String.valueOf(n));
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
