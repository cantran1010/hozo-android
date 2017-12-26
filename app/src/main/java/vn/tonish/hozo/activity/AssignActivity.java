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
    private int taskID;

    @Override
    protected int getLayout() {
        return R.layout.activity_assign;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.container);
        ImageView imgClose = (ImageView) findViewById(R.id.img_back);
        imgClose.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.OFFER_TASK_ID)) {
            taskID = intent.getExtras().getInt(Constants.OFFER_TASK_ID);
            LogUtils.d(TAG, "check task id :" + taskID);
            getBidders(taskID);
        }


    }


    private void setDataForPageView() {
        ViewPageAssignAdapter adapter = new ViewPageAssignAdapter(this, bidResponses, taskID);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void getBidders(final int id) {
        ProgressDialogUtils.showProgressDialog(this);

        ApiClient.getApiService().getBidders(UserManager.getUserToken(), id).enqueue(new Callback<List<BidResponse>>() {
            @Override
            public void onResponse(Call<List<BidResponse>> call, Response<List<BidResponse>> response) {
                LogUtils.d(TAG, "TaskResponse code : " + response.code());
                LogUtils.d(TAG, "TaskResponse body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    bidResponses.addAll(response.body());
                    setDataForPageView();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(AssignActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getBidders(id);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    APIError error = ErrorUtils.parseError(response);
                    DialogUtils.showOkDialog(AssignActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(AssignActivity.this);
                } else {
                    DialogUtils.showRetryDialog(AssignActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getBidders(id);
                        }

                        @Override
                        public void onCancel() {

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
        }
    }
}
