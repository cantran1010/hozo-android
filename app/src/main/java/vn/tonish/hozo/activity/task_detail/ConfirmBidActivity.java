package vn.tonish.hozo.activity.task_detail;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.GeneralInfoActivity;
import vn.tonish.hozo.activity.payment.MyWalletActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.AlertDialogOkNonTouch;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.showSoftKeyboard;

/**
 * Created by LongBui on 11/15/17.
 */

public class ConfirmBidActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ConfirmBidActivity.class.getSimpleName();
    private static final int MAX_BUGDET = 1000000;
    private static final int MIN_BUGDET = 10000;
    private TextViewHozo tvTitle, tvDate, tvTime, tvHour, tvPolicy, tvDes;
    private EdittextHozo edtSms, edtBudget;
    private TaskResponse taskResponse;
    private ImageView imgEdit;

    @Override
    protected int getLayout() {
        return R.layout.confrim_bid_activity;
    }

    @Override
    protected void initView() {
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvHour = (TextViewHozo) findViewById(R.id.tv_hour);
        edtBudget = (EdittextHozo) findViewById(R.id.edt_budget);
        edtSms = (EdittextHozo) findViewById(R.id.edt_sms);
        tvPolicy = (TextViewHozo) findViewById(R.id.tv_policy);

        ButtonHozo btnBid = (ButtonHozo) findViewById(R.id.btn_bid);
        btnBid.setOnClickListener(this);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        ImageView imgEdit = (ImageView) findViewById(R.id.img_edit);
        imgEdit.setOnClickListener(this);

        tvDes = (TextViewHozo) findViewById(R.id.tv_des);
    }

    @Override
    protected void initData() {
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.TASK_DETAIL_EXTRA);

        tvTitle.setText(taskResponse.getTitle());
        if (taskResponse.getStartTime() != null)
            tvDate.setText(DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));
        else tvDate.setText("");

        if (taskResponse.getStartTime() != null && taskResponse.getEndTime() != null)
            tvTime.setText(DateTimeUtils.getOnlyHourFromIso(taskResponse.getStartTime()));
        else tvTime.setText("");

        try {
            tvHour.setText(String.valueOf(DateTimeUtils.hoursBetween(DateTimeUtils.toCalendar(taskResponse.getStartTime()), DateTimeUtils.toCalendar(taskResponse.getEndTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        edtBudget.addTextChangedListener(new NumberTextWatcher(edtBudget));
        edtBudget.setText(String.valueOf(taskResponse.getWorkerRate()));
        tvDes.setText(getString(R.string.bid_des1, Utils.formatNumber(taskResponse.getBidDepositAmount())));
        edtBudget.setEnabled(false);
        String text = getString(R.string.bid_confirm_policy);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);

        ClickableSpan conditionClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                openGeneralInfoActivity(getString(R.string.other_condition), "http://hozo.vn/dieu-khoan-su-dung/?ref=app");
            }
        };

        ssBuilder.setSpan(
                conditionClickableSpan, // Span to add
                text.indexOf(getString(R.string.bid_confirm_policy_link)), // Start of the span (inclusive)
                text.indexOf(getString(R.string.bid_confirm_policy_link)) + String.valueOf(getString(R.string.bid_confirm_policy_link)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        ssBuilder.setSpan(
                new ForegroundColorSpan(Color.parseColor("#14a3cf")), // Span to add
                text.indexOf(getString(R.string.bid_confirm_policy_link)), // Start of the span (inclusive)
                text.indexOf(getString(R.string.bid_confirm_policy_link)) + String.valueOf(getString(R.string.bid_confirm_policy_link)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        tvPolicy.setText(ssBuilder);
        tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        tvPolicy.setHighlightColor(Color.TRANSPARENT);
    }

    private void openGeneralInfoActivity(String title, String url) {
        Intent intent = new Intent(this, GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, url);
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }

    private String getLongAutoCompleteTextView(EdittextHozo autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().replace(",", "").replace(".", "").replace(" ", "");
    }

    @Override
    protected void resumeData() {

    }

    private void doOffer() {
        ProgressDialogUtils.showProgressDialog(this);
        String sms = edtSms.getText().toString().trim();
        String price = getLongAutoCompleteTextView(edtBudget);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAMETER_OFFER_PRICE, price);
            if (!sms.isEmpty())
                jsonRequest.put(Constants.PARAMETER_OFFER_SMS, sms);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().bidsTask(UserManager.getUserToken(), taskResponse.getId(), body).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, final Response<TaskResponse> response) {
                LogUtils.d(TAG, "bidsTask status code : " + response.code());
                LogUtils.d(TAG, "bidsTask body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    DialogUtils.showOkDialogNonTouch(ConfirmBidActivity.this, getString(R.string.create_task_title), getString(R.string.bid_success), getString(R.string.create_task_ok), new AlertDialogOkNonTouch.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            setResult(Constants.BID_RESPONSE_CODE);
                            finish();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BAD_REQUEST) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "bidsTask errorBody : " + error.toString());
                    if (error.status().equals(Constants.BID_ERROR_SAME_TIME)) {
                        DialogUtils.showOkDialog(ConfirmBidActivity.this, getString(R.string.error), getString(R.string.offer_error_time), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.BID_LIMIT_OFFER)) {
                        DialogUtils.showOkDialog(ConfirmBidActivity.this, getString(R.string.bid_limmit_title), getString(R.string.bid_limmit_offer_error), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.BID_ERROR_INVALID_DATA)) {
                        DialogUtils.showOkDialog(ConfirmBidActivity.this, getString(R.string.app_name), getString(R.string.offer_invalid_data), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals(Constants.BID_NOT_ENOUGH_BALANCE)) {
                        DialogUtils.showOkAndCancelDialog(ConfirmBidActivity.this, getString(R.string.app_name), getString(R.string.bid_not_enough_money, Utils.formatNumber(taskResponse.getBidDepositAmount())), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                startActivity(MyWalletActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    } else {
                        DialogUtils.showOkDialog(ConfirmBidActivity.this, getString(R.string.offer_system_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(ConfirmBidActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doOffer();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(ConfirmBidActivity.this);
                } else {
                    DialogUtils.showRetryDialog(ConfirmBidActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doOffer();
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
                DialogUtils.showRetryDialog(ConfirmBidActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doOffer();
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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_bid:
                if (edtBudget.getText().toString().trim().isEmpty()) {
                    edtBudget.requestFocus();
                    edtBudget.setError(getString(R.string.post_a_task_budget_error));
                } else if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) > MAX_BUGDET) {
                    edtBudget.requestFocus();
                    edtBudget.setError(getString(R.string.max_budget_error, Utils.formatNumber(MAX_BUGDET)));
                } else if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) < MIN_BUGDET) {
                    edtBudget.requestFocus();
                    edtBudget.setError(getString(R.string.min_budget_error, Utils.formatNumber(MIN_BUGDET)));
                } else
                    doOffer();
                break;

            case R.id.img_back:
                finish();
                break;
            case R.id.img_edit:
                edtBudget.setEnabled(true);
                if (edtBudget.length() > 4)
                    edtBudget.setSelection(edtBudget.length() - 4);
                showSoftKeyboard(this, edtBudget);
                break;

        }
    }
}
