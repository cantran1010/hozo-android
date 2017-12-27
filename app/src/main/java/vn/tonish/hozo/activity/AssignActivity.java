package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ViewPageAssignAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.BidResponse;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by CanTran on 11/21/17.
 */

public class AssignActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AssignActivity.class.getSimpleName();
    private ViewPager viewPager;
    private ImageView imgNext;
    private ImageView imgBack;
    private List<BidResponse> bidResponses = new ArrayList<>();
    private int taskID, bidderID, workerCount, assignerCount;
    private ViewPageAssignAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_assign;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.container);
        imgNext = (ImageView) findViewById(R.id.img_rating_next);
        imgBack = (ImageView) findViewById(R.id.img_rating_back);
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.OFFER_TASK_ID)) {
            taskID = intent.getExtras().getInt(Constants.OFFER_TASK_ID);
            LogUtils.d(TAG, "check task id :" + taskID);
            getBidders(taskID);
        }
        if (intent.hasExtra(Constants.BIDDER_ID)) {
            bidderID = intent.getExtras().getInt(Constants.BIDDER_ID);
            LogUtils.d(TAG, "check bidder id :" + bidderID);
        }
        if (intent.hasExtra(Constants.BIDDER_ID)) {
            workerCount = intent.getExtras().getInt(Constants.WORKER_COUNT);
            LogUtils.d(TAG, "check workerCount  :" + workerCount);
        }
        if (intent.hasExtra(Constants.BIDDER_ID)) {
            assignerCount = intent.getExtras().getInt(Constants.ASSSIGNER_COUNT);
            LogUtils.d(TAG, "check assignerCount:" + assignerCount);
        }
        setDataForPageView();


    }


    private void setDataForPageView() {
        adapter = new ViewPageAssignAdapter(this, bidResponses, taskID, workerCount, assignerCount);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateUi(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private int getPosition(int bidderID) {
        int pos = 0;
        for (int i = 0; i < bidResponses.size(); i++) {
            if (bidderID == bidResponses.get(i).getId()) {
                pos = i;
                break;
            }

        }
        return pos;

    }

    private void updateUi(int position) {
        if (position == 0)
            imgBack.setVisibility(View.GONE);
        else imgBack.setVisibility(View.VISIBLE);
        if (position == bidResponses.size() - 1)
            imgNext.setVisibility(View.GONE);
        else imgNext.setVisibility(View.VISIBLE);
    }

    private void getBidders(final int id) {
        ProgressDialogUtils.showProgressDialog(this);
        ApiClient.getApiService().getBidders(UserManager.getUserToken(), id).enqueue(new Callback<List<BidResponse>>() {
            @Override
            public void onResponse(Call<List<BidResponse>> call, Response<List<BidResponse>> response) {
                APIError error = ErrorUtils.parseError(response);
                LogUtils.d(TAG, "TaskResponse code : " + response.code());
                LogUtils.d(TAG, "TaskResponse body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    bidResponses.clear();
                    bidResponses.addAll(response.body());
                    updateUi(getPosition(bidderID));
                    adapter.notifyDataSetChanged();
                    viewPager.setCurrentItem(getPosition(bidderID));
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(AssignActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getBidders(id);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    DialogUtils.showOkDialog(AssignActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(AssignActivity.this);
                } else {
                    DialogUtils.showOkDialog(AssignActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                }

                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<List<BidResponse>> call, Throwable t) {
                LogUtils.e(TAG, "ERROR : " + t.getMessage());
                DialogUtils.showRetryDialog(AssignActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getBidders(id);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }


    @Override
    protected void resumeData() {
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_rating_next:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                break;
            case R.id.img_rating_back:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                break;
        }
    }
}
