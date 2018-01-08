package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.MemberAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 1/4/18.
 */

public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    private TaskResponse taskResponse;
    private List<Assigner> assigners = new ArrayList<>();
    private RecyclerView rcvMember;
    private MemberAdapter memberAdapter;
    private TextViewHozo tvTaskName;

    @Override
    protected int getLayout() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        rcvMember = (RecyclerView) findViewById(R.id.rcv_member);
        tvTaskName = (TextViewHozo) findViewById(R.id.tv_task_name);
        findViewById(R.id.img_back).setOnClickListener(this);

    }

    @Override
    protected void initData() {
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.TASK_DETAIL_EXTRA);
        tvTaskName.setText(taskResponse.getTitle());
        assigners = taskResponse.getAssignees();
        Assigner assigner = new Assigner();
        assigner.setFullName("Nhóm");
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

    }

    @Override
    protected void resumeData() {

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
