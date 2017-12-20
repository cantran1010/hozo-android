package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.PosterOpenAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.lvList;

public class BiddersActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = BiddersActivity.class.getSimpleName();
    private RecyclerView rcvBidders;
    private TaskResponse taskResponse;
    private ArrayList<Bidder> bidders;
    private String bidderType = "";
    private PosterOpenAdapter posterOpenAdapter;
    private TextViewHozo tvTitle;
    private boolean isBidder = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_bidders;
    }

    @Override
    protected void initView() {
        rcvBidders = (RecyclerView) findViewById(lvList);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        bidders = new ArrayList<>();
        taskResponse = new TaskResponse();
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        taskResponse = (TaskResponse) intent.getExtras().get(Constants.TASK_RESPONSE_EXTRA);
        bidderType = intent.getStringExtra(Constants.BIDDER_TYPE_EXTRA);
        tvTitle.setText(getString(R.string.bidders, taskResponse.getBidderCount()));
        updateList();
    }

    private void updateList() {
        bidders = (ArrayList<Bidder>) taskResponse.getBidders();
        posterOpenAdapter = new PosterOpenAdapter(bidders, bidderType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBidders.setLayoutManager(linearLayoutManager);
        posterOpenAdapter.setTaskId(taskResponse.getId());
        rcvBidders.setAdapter(posterOpenAdapter);
    }

    @Override
    protected void resumeData() {
        registerReceiver(broadcastReceiver, new IntentFilter("MyBroadcast"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if (isBidder) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_BIDDER_TASKRESPONSE, taskResponse);
                    setResult(Constants.RESULT_CODE_BIDDER, intent);
                }
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isBidder) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_BIDDER_TASKRESPONSE, taskResponse);
            setResult(Constants.RESULT_CODE_BIDDER, intent);
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.hasExtra(Constants.EXTRA_TASK)) {
                isBidder = true;
                LogUtils.d(TAG, "BiddersActivity broadcastReceiver listen");
                taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
                updateRole();
                bidders.clear();
                bidders.addAll((ArrayList<Bidder>) taskResponse.getBidders());
                posterOpenAdapter.notifyDataSetChanged();
                tvTitle.setText(getString(R.string.bidders, taskResponse.getBidderCount()));
                LogUtils.d(TAG, "BiddersActivity broadcastReceiver bidders size : " + bidders.size());

            }
        }
    };

    private void updateRole() {

        //fix crash on fabric -> I don't know why crash :(
        if (UserManager.getMyUser() == null) return;

        //poster
        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            taskResponse.setRole(Constants.ROLE_POSTER);
        }
        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)
                || (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED))
                || (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED))
                || taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)
                || taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            taskResponse.setRole(Constants.ROLE_TASKER);
        }
        // find task
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            taskResponse.setRole(Constants.ROLE_FIND_TASK);
        }

    }
}
