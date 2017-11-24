package vn.tonish.hozo.fragment.task_detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AssignersActivity;
import vn.tonish.hozo.activity.BiddersActivity;
import vn.tonish.hozo.activity.BlockTaskActivity;
import vn.tonish.hozo.activity.ChatActivity;
import vn.tonish.hozo.activity.RatingActivity;
import vn.tonish.hozo.activity.task_detail.TaskDetailNewActivity;
import vn.tonish.hozo.adapter.AssignerCallAdapter;
import vn.tonish.hozo.adapter.PosterOpenAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.string.call;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_RATE;

/**
 * Created by LongBui on 8/22/17.
 */

public class TaskDetailTab2Fragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = TaskDetailTab2Fragment.class.getSimpleName();

    private TaskResponse taskResponse;

    private RecyclerView rcvBidder;
    private ArrayList<Bidder> bidders = new ArrayList<>();

    private RecyclerView rcvAssign;
    private ArrayList<Assigner> assigners = new ArrayList<>();

    private TextViewHozo tvSeeMoreBidders, tvSeeMoreAssigners;
    private TextViewHozo tvBidderCount, tvAssignCount, tvNoBidder;

    private String bidderType = "";
    private String assigerType = "";
    private static final int MAX_WORKER = 5;

    private ButtonHozo btnChat;

    @Override
    protected int getLayout() {
        return R.layout.task_detail_tab2_layout;
    }

    @Override
    protected void initView() {
        createSwipeToRefresh();

        rcvBidder = (RecyclerView) findViewById(R.id.rcv_bidders);
        rcvAssign = (RecyclerView) findViewById(R.id.rcv_assign);

        tvSeeMoreBidders = (TextViewHozo) findViewById(R.id.tv_see_more_bidders);
        tvSeeMoreBidders.setOnClickListener(this);
        tvSeeMoreAssigners = (TextViewHozo) findViewById(R.id.tv_see_more_assigns);
        tvSeeMoreAssigners.setOnClickListener(this);

        tvBidderCount = (TextViewHozo) findViewById(R.id.tv_bidder_count);
        tvAssignCount = (TextViewHozo) findViewById(R.id.tv_assign_count);

        tvNoBidder = (TextViewHozo) findViewById(R.id.tv_no_bidder);

        btnChat = (ButtonHozo) findViewById(R.id.btn_chat);
//        btnChat.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        taskResponse = ((TaskDetailNewActivity) getActivity()).getTaskResponse();
        updateUi();
    }

    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("MyBroadcast"));
        LogUtils.d(TAG, "resumeData start");
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d(TAG, "setUserVisibleHint start , isVisibleToUser : " + isVisibleToUser);
        if (isVisibleToUser) {
//            taskResponse = ((TaskDetailNewActivity) getActivity()).getTaskResponse();
//            updateUi();
            getData();
        }
    }

    public void updateUi() {
        LogUtils.d(TAG, "updateUi start");

        if (!isAdded() || getActivity() == null) return;

        //fix bug fabric
        if (taskResponse.getStatus() == null) return;

        bidderType = "";
        assigerType = "";

        rcvBidder.setVisibility(View.VISIBLE);
        rcvAssign.setVisibility(View.VISIBLE);


        if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            bidderType = getString(R.string.assign);
            assigerType = getString(call);

            rcvBidder.setVisibility(View.VISIBLE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            assigerType = getString(call);

            rcvBidder.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            rcvBidder.setVisibility(View.GONE);

            assigerType = getString(R.string.rate);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            rcvBidder.setVisibility(View.VISIBLE);
            rcvAssign.setVisibility(View.VISIBLE);

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED) && taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {

            rcvBidder.setVisibility(View.VISIBLE);
            rcvAssign.setVisibility(View.VISIBLE);
        }

        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            rcvBidder.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
            rcvBidder.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
            rcvBidder.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);
        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
            rcvBidder.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);
        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {

        } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
            rcvBidder.setVisibility(View.GONE);
        }

        // make an offer
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {

        } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED) && taskResponse.getPoster().getId() != UserManager.getMyUser().getId()) {

        }

        // update bidder list
        bidders = (ArrayList<Bidder>) taskResponse.getBidders();
        refreshBidderList(bidderType);

        //update assigners list
        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();
        refreshAssignerList(assigerType);

        tvBidderCount.setText(getString(R.string.bidder, taskResponse.getBidderCount()));
        tvAssignCount.setText(getString(R.string.assign_people, taskResponse.getAssigneeCount()));

        if (bidders.size() == 0) {
            tvBidderCount.setVisibility(View.GONE);
            rcvBidder.setVisibility(View.GONE);
        } else {
            if (rcvBidder.getVisibility() == View.VISIBLE)
                tvBidderCount.setVisibility(View.VISIBLE);
            else
                tvBidderCount.setVisibility(View.GONE);
        }

        if (assigners.size() == 0) {
            tvAssignCount.setVisibility(View.GONE);
            rcvAssign.setVisibility(View.GONE);
        } else {
            if (rcvAssign.getVisibility() == View.VISIBLE)
                tvAssignCount.setVisibility(View.VISIBLE);
            else
                tvAssignCount.setVisibility(View.GONE);
        }

//        else {
//            rcvAssign.setVisibility(View.VISIBLE);
//            tvSeeMoreAssigners.setVisibility(View.VISIBLE);
//        }

        if (bidders.size() == 0 && assigners.size() == 0)
            tvNoBidder.setVisibility(View.VISIBLE);
        else
            tvNoBidder.setVisibility(View.GONE);

        updateChatBtn();

    }

    private void refreshBidderList(String bidderType) {
        PosterOpenAdapter posterOpenAdapter;
        if (bidders.size() > MAX_WORKER) {

            ArrayList<Bidder> biddersNew = new ArrayList<Bidder>();
            biddersNew.addAll(bidders.subList(0, MAX_WORKER));

            if (rcvBidder.getVisibility() == View.VISIBLE)
                tvSeeMoreBidders.setVisibility(View.VISIBLE);
            else
                tvSeeMoreBidders.setVisibility(View.GONE);

            posterOpenAdapter = new PosterOpenAdapter(biddersNew, bidderType);
        } else {
            tvSeeMoreBidders.setVisibility(View.GONE);
            posterOpenAdapter = new PosterOpenAdapter(bidders, bidderType);
        }
        rcvBidder.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvBidder.setLayoutManager(linearLayoutManager);
        posterOpenAdapter.setTaskId(taskResponse.getId());
        rcvBidder.setAdapter(posterOpenAdapter);
    }

    private void refreshAssignerList(String assignType) {
        AssignerCallAdapter assignerAdapter;
        if (assigners.size() > MAX_WORKER) {

            ArrayList<Assigner> assignersNew = new ArrayList<>();
            assignersNew.addAll(assigners.subList(0, MAX_WORKER));

            if (rcvAssign.getVisibility() == View.VISIBLE)
                tvSeeMoreAssigners.setVisibility(View.VISIBLE);
            else
                tvSeeMoreAssigners.setVisibility(View.GONE);

            assignerAdapter = new AssignerCallAdapter(assignersNew, assignType);

        } else {
            tvSeeMoreAssigners.setVisibility(View.GONE);
            assignerAdapter = new AssignerCallAdapter(assigners, assignType);
        }
        rcvAssign.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvAssign.setLayoutManager(linearLayoutManager);
        assignerAdapter.setTaskId(taskResponse.getId());
        rcvAssign.setAdapter(assignerAdapter);
    }

    private void doSeeMoreAssigns() {
        Intent intent = new Intent(getActivity(), AssignersActivity.class);
        intent.putExtra(Constants.TASK_RESPONSE_EXTRA, taskResponse);
        intent.putExtra(Constants.ASSIGNER_TYPE_EXTRA, assigerType);
        startActivityForResult(intent, Constants.REQUEST_CODE_SEND_ASSIGNER, TransitionScreen.DOWN_TO_UP);
    }

    private void doSeeMoreBidders() {
        Intent intent = new Intent(getActivity(), BiddersActivity.class);
        intent.putExtra(Constants.TASK_RESPONSE_EXTRA, taskResponse);
        intent.putExtra(Constants.BIDDER_TYPE_EXTRA, bidderType);
        startActivityForResult(intent, Constants.REQUEST_CODE_SEND_BINDDER, TransitionScreen.DOWN_TO_UP);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent.hasExtra(Constants.EXTRA_TASK)) {
                taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
                Utils.updateRole(taskResponse);
                updateUi();
                ((TaskDetailNewActivity) getActivity()).setTaskResponse(taskResponse);
            } else if (intent.hasExtra(Constants.ASSIGNER_RATE_EXTRA)) {
                Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_RATE_EXTRA);
                Intent intentRate = new Intent(getActivity(), RatingActivity.class);
                intentRate.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
                intentRate.putExtra(Constants.USER_ID_EXTRA, assigner.getId());
                intentRate.putExtra(Constants.AVATAR_EXTRA, assigner.getAvatar());
                intentRate.putExtra(Constants.NAME_EXTRA, assigner.getFullName());
                startActivityForResult(intentRate, Constants.REQUEST_CODE_RATE, TransitionScreen.UP_TO_DOWN);
            } else if (intent.hasExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA)) {
                DialogUtils.showOkAndCancelDialog(getActivity(), getString(R.string.cancel_bid_title), getString(R.string.cancel_bid_content), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        Assigner assigner = (Assigner) intent.getSerializableExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA);
                        doCancelBid(assigner);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_RATE && resultCode == RESPONSE_CODE_RATE) {
            getData();
        }
        if (requestCode == Constants.REQUEST_CODE_SEND_ASSIGNER && resultCode == RESPONSE_CODE_RATE) {
            getData();
        } else if (requestCode == Constants.REQUEST_CODE_SEND_BINDDER && resultCode == Constants.RESULT_CODE_BIDDER) {
            if (data.hasExtra(Constants.EXTRA_BIDDER_TASKRESPONSE)) {
                taskResponse = (TaskResponse) data.getExtras().get(Constants.EXTRA_BIDDER_TASKRESPONSE);
                Utils.updateRole(taskResponse);
                updateUi();
                ((TaskDetailNewActivity) getActivity()).setTaskResponse(taskResponse);
            }
        } else if (requestCode == Constants.REQUEST_CODE_SEND_ASSIGNER && resultCode == Constants.RESULT_CODE_ASSIGNER) {
            if (data.hasExtra(Constants.EXTRA_ASSIGNER_TASKRESPONSE)) {
                taskResponse = (TaskResponse) data.getExtras().get(Constants.EXTRA_ASSIGNER_TASKRESPONSE);
                Utils.updateRole(taskResponse);
                updateUi();
                ((TaskDetailNewActivity) getActivity()).setTaskResponse(taskResponse);
            }
        } else if (requestCode == Constants.REQUEST_CODE_CHAT && resultCode == Constants.RESULT_CODE_CHAT) {
            getData();
        }
    }

    private void doCancelBid(final Assigner assigner) {
        ProgressDialogUtils.showProgressDialog(getActivity());
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
                    Utils.updateRole(taskResponse);
                    updateUi();
                    ((TaskDetailNewActivity) getActivity()).setTaskResponse(taskResponse);
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doCancelBid(assigner);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
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

    private void getData() {
        Map<String, Boolean> option = new HashMap<>();
        option.put("viewer", true);

        Call<TaskResponse> call = ApiClient.getApiService().getDetailTask(UserManager.getUserToken(), taskResponse.getId(), option);
        call.enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "getDetailTask , status code : " + response.code());
                LogUtils.d(TAG, "getDetailTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    taskResponse = response.body();
                    Utils.updateRole(taskResponse);
                    updateUi();
                    ((TaskDetailNewActivity) getActivity()).setTaskResponse(taskResponse);

                    Intent intentComment = new Intent();
                    intentComment.setAction(Constants.BROAD_CAST_MY);
                    intentComment.putExtra(Constants.BIDDER_EXTRA, 0);
                    getActivity().sendBroadcast(intentComment);

                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "getDetailTask errorBody" + error.toString());
                    if (error.status().equals(Constants.TASK_DETAIL_INPUT_REQUIRE) || error.status().equals(Constants.TASK_DETAIL_NO_EXIT)) {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.task_detail_no_exit), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.TASK_DETAIL_BLOCK)) {
                        Intent intent = new Intent(getActivity(), BlockTaskActivity.class);
                        intent.putExtra(Constants.TITLE_INFO_EXTRA, getString(R.string.task_detail_block));
                        intent.putExtra(Constants.CONTENT_INFO_EXTRA, getString(R.string.task_detail_block_reasons) + " " + error.message());
                        startActivity(intent, TransitionScreen.FADE_IN);
                    } else {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.offer_system_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getData();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getData();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                onStopRefresh();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                LogUtils.e(TAG, "getDetailTask , error : " + t.getMessage());
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getData();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                onStopRefresh();
            }
        });

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
    }

    private void updateChatBtn() {
        assigners = (ArrayList<Assigner>) taskResponse.getAssignees();

        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) || taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED)) {
                if (assigners.size() > 0) {
                    btnChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openChatScreen();
                        }
                    });
                } else
                    btnChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogUtils.showOkDialog(getActivity(), getString(R.string.app_name), getString(R.string.poster_chat_msg), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });
                        }
                    });
            } else {
                btnChat.setVisibility(View.GONE);
            }
        } else {
            if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
                btnChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openChatScreen();
                    }
                });
            } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN)) {
                btnChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogUtils.showOkDialog(getActivity(), getString(R.string.app_name), getString(R.string.bidder_chat_msg), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                });
            } else {
                btnChat.setVisibility(View.GONE);
            }
        }

    }

    private void openChatScreen() {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
        intent.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
        intent.putExtra(Constants.TITLE_INFO_EXTRA, taskResponse.getTitle());
        startActivityForResult(intent, Constants.REQUEST_CODE_CHAT, TransitionScreen.DOWN_TO_UP);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_see_more_bidders:
                doSeeMoreBidders();
                break;

            case R.id.tv_see_more_assigns:
                doSeeMoreAssigns();
                break;

//            case R.id.btn_chat:
//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                intent.putExtra(Constants.TASK_ID_EXTRA, taskResponse.getId());
//                intent.putExtra(Constants.USER_ID_EXTRA, taskResponse.getPoster().getId());
//                intent.putExtra(Constants.TITLE_INFO_EXTRA, taskResponse.getTitle());
//                startActivityForResult(intent, Constants.REQUEST_CODE_CHAT, TransitionScreen.DOWN_TO_UP);
//                break;

        }
    }
}
