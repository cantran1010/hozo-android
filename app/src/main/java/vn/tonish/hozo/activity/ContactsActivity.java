package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ChatPrivateAdapter;
import vn.tonish.hozo.adapter.MemberAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 1/4/18.
 */

public class ContactsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ContactsActivity.class.getSimpleName();
    private TaskResponse taskResponse;
    private List<Assigner> assigners = new ArrayList<>();
    private RecyclerView rcvMember, rcvChat;
    private MemberAdapter memberAdapter;
    private ChatPrivateAdapter chatPrivateAdapter;
    private TextViewHozo tvTaskName, tvCount;


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
        Assigner assigner;
        tvTaskName.setText(taskResponse.getTitle());
        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId())
            assigners = taskResponse.getAssignees();
        else {
            assigner = new Assigner();
            assigner.setId(taskResponse.getPoster().getId());
            assigner.setAvatar(taskResponse.getPoster().getAvatar());
            assigner.setFullName(taskResponse.getPoster().getFullName());
            assigners.add(assigner);
        }
        assigner = new Assigner();
        assigner.setFullName("Nhóm");
        assigner.setId(taskResponse.getId());
        assigners.add(0, assigner);
        memberAdapter = new MemberAdapter(ContactsActivity.this, assigners);
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
        chatPrivateAdapter = new ChatPrivateAdapter(this, assigners);
        LinearLayoutManager layoutManagaer
                = new LinearLayoutManager(ContactsActivity.this, LinearLayoutManager.VERTICAL, false);
        rcvChat.setLayoutManager(layoutManagaer);
        rcvChat.setAdapter(chatPrivateAdapter);
        chatPrivateAdapter.setChatPrivateListener(new ChatPrivateAdapter.ChatPrivateListener() {
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
