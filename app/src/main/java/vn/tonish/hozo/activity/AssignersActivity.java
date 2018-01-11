package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.AssignerAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_RATE;

public class AssignersActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = AssignersActivity.class.getSimpleName();
    private RecyclerView rcvAssign;
    private TaskResponse taskResponse;
    private ArrayList<Assigner> assigners;
    private String assignType = "";
    private AssignerAdapter assignerAdapter;
    private TextViewHozo tvTitle;
    private boolean isAssigner = false;
    private int assignersIdRate;
    private boolean isRatting = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_assigners;
    }

    @Override
    protected void initView() {
        rcvAssign = (RecyclerView) findViewById(R.id.lvList);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        assigners = new ArrayList<>();
        taskResponse = new TaskResponse();
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        taskResponse = (TaskResponse) intent.getExtras().get(Constants.TASK_RESPONSE_EXTRA);
        assignType = intent.getStringExtra(Constants.ASSIGNER_TYPE_EXTRA);
        tvTitle.setText(getString(R.string.assigners, taskResponse.getAssigneeCount()));
        updateList();
    }

    private void updateList() {
        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();
        assignerAdapter = new AssignerAdapter(assigners, assignType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvAssign.setLayoutManager(linearLayoutManager);
        assignerAdapter.setTaskId(taskResponse.getId());
        assignerAdapter.setPosterID(taskResponse.getPoster().getId());
        rcvAssign.setAdapter(assignerAdapter);
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
                if (isAssigner) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_ASSIGNER_TASKRESPONSE, taskResponse);
                    setResult(Constants.RESULT_CODE_ASSIGNER, intent);
                }

                if (isRatting) {
                    setResult(Constants.RESPONSE_CODE_RATE);
                }

                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if (isAssigner) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_ASSIGNER_TASKRESPONSE, taskResponse);
            setResult(Constants.RESULT_CODE_ASSIGNER, intent);
        }

        if (isRatting) {
            setResult(Constants.RESPONSE_CODE_RATE);
        }

        finish();
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.hasExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA)) {
                DialogUtils.showOkAndCancelDialog(AssignersActivity.this, getString(R.string.cancel_bid_title), getString(R.string.cancel_bid_content), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA);
                        doCancelBid(assigner);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else if (intent.hasExtra(Constants.ASSIGNER_RATE_EXTRA)) {
                Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_RATE_EXTRA);
                Intent intentRate = new Intent(AssignersActivity.this, RatingActivity.class);
                intentRate.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                intentRate.putExtra(Constants.USER_ID_EXTRA, assigner.getId());
                intentRate.putExtra(Constants.AVATAR_EXTRA, assigner.getAvatar());
                intentRate.putExtra(Constants.NAME_EXTRA, assigner.getFullName());
                assignersIdRate = assigner.getId();
                startActivityForResult(intentRate, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_RATE && resultCode == RESPONSE_CODE_RATE) {
            for (int i = 0; i < assigners.size(); i++) {
                if (assigners.get(i).getId() == assignersIdRate) assigners.get(i).setRating(1);
            }
            assignerAdapter.notifyDataSetChanged();
            isRatting = true;
        }

    }

    private void doCancelBid(final Assigner assigner) {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("user_id", assigner.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doCancelBid data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().cancelBid(UserManager.getUserToken(), taskResponse.getId(), body).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {

                LogUtils.d(TAG, "doCancelBid onResponse : " + response.body());
                LogUtils.d(TAG, "doCancelBid code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body();
                    isAssigner = true;
                    updateRole();
                    LogUtils.d(TAG, "BiddersActivity broadcastReceiver listen");
                    assigners.clear();
                    assigners.addAll((ArrayList<Assigner>) taskResponse.getAssignees());
                    assignerAdapter.notifyDataSetChanged();
                    tvTitle.setText(getString(R.string.assigners, taskResponse.getAssigneeCount()));
                    LogUtils.d(TAG, "BiddersActivity broadcastReceiver bidders size : " + assigners.size());

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(AssignersActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doCancelBid(assigner);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(AssignersActivity.this);
                } else {
                    DialogUtils.showRetryDialog(AssignersActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doCancelBid(assigner);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {

            }
        });
    }

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
