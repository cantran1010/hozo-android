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
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.R.id.lvList;

public class BiddersActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = BiddersActivity.class.getSimpleName();
    private RecyclerView rcvBidders;
    private ImageView imgBack;
    private TaskResponse taskResponse;
    private ArrayList<Bidder> bidders;
    private String bidderType;
    private PosterOpenAdapter posterOpenAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_bidders;
    }

    @Override
    protected void initView() {
        rcvBidders = (RecyclerView) findViewById(lvList);
        imgBack = (ImageView) findViewById(R.id.img_back);
        bidders = new ArrayList<>();
        taskResponse = new TaskResponse();
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        taskResponse = (TaskResponse) intent.getExtras().get(Constants.TASK_RESPONSE_EXTRA);
        bidderType = intent.getStringExtra(Constants.BIDDER_TYPE_EXTRA);
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
                finish();
                break;
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.hasExtra(Constants.EXTRA_TASK)) {
                LogUtils.d("TAG", "BiddersActivity broadcastReceiver listen");
                taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
                bidders.clear();
                bidders.addAll((ArrayList<Bidder>) taskResponse.getBidders());
                posterOpenAdapter.notifyDataSetChanged();
                LogUtils.d("TAG", "BiddersActivity broadcastReceiver bidders size : " + bidders.size());
//                updateList();


            }
        }
    };
}
