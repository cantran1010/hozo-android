package vn.tonish.hozo.activity.payment;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.HozoSpinnerAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 6/14/18.
 */
public class RechargeStripeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = RechargeStripeActivity.class.getSimpleName();
    private Spinner spMonth, spYear;
    private EdittextHozo edtName, edtNumber, edtCvn;
    public static String PUBLISH_KEY = "";
    private int amount;
    private TextViewHozo tvAmount;

    @Override
    protected int getLayout() {
        return R.layout.recharge_stripe_activity;
    }

    @Override
    protected void initView() {

        edtName = (EdittextHozo) findViewById(R.id.edt_visa_name);
        edtNumber = (EdittextHozo) findViewById(R.id.edt_number_visa);
        edtCvn = (EdittextHozo) findViewById(R.id.edt_cvn_visa);

        spMonth = (Spinner) findViewById(R.id.sp_month);
        spYear = (Spinner) findViewById(R.id.sp_year);

        tvAmount = (TextViewHozo) findViewById(R.id.tv_amount);

        findViewById(R.id.btn_recharge).setOnClickListener(this);

    }

    @Override
    protected void initData() {

        PUBLISH_KEY = getString(R.string.stripe_publish_key);
        amount = getIntent().getIntExtra(Constants.AMOUNT_EXTRA, 0);

        tvAmount.setText(getString(R.string.amount_recharge, Utils.formatNumber(amount)));

        LogUtils.d(TAG, "Publish key : " + PUBLISH_KEY);

        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++)
            months.add(String.valueOf(i));
        HozoSpinnerAdapter monthAdapter = new HozoSpinnerAdapter(this, months);
        spMonth.setAdapter(monthAdapter);

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String> years = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            years.add(String.valueOf(currentYear + i));
        }
        HozoSpinnerAdapter yearAdapter = new HozoSpinnerAdapter(this, years);
        spYear.setAdapter(yearAdapter);

    }

    @Override
    protected void resumeData() {

    }

    private void doNext() {
        ProgressDialogUtils.showProgressDialog(this);
//        final Card cardToSave = new Card("4242424242424242", 12, 2019, "369");
        final Card cardToSave = new Card(edtNumber.getText().toString(), Integer.valueOf(spMonth.getSelectedItem().toString()), Integer.valueOf(spYear.getSelectedItem().toString()), edtCvn.getText().toString(),
                edtName.getText().toString(), null, null, null, null, null, null, "VND");
        LogUtils.d(TAG, "doNext , cardToSave : " + cardToSave.toString());

        final Stripe stripe = new Stripe(this, PUBLISH_KEY);
        stripe.createToken(
                cardToSave,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                        // Token is created using Checkout or Elements!
                        // Get the payment token ID submitted by the form:

                        Log.d(TAG, "doNext token : " + token.toString());
                        doCheckoutStripe(token.getId());
                    }

                    public void onError(Exception error) {
                        Log.e(TAG, "doNext error to string : " + error.toString());
                        Log.e(TAG, "doNext errot msg : " + error.getLocalizedMessage());
                        DialogUtils.showOkDialog(RechargeStripeActivity.this, getString(R.string.error), error.getLocalizedMessage(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                        ProgressDialogUtils.dismissProgressDialog();
                    }

                }
        );
    }

    private void doCheckoutStripe(final String tokenId) {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("amount", amount);
            jsonRequest.put("provider", "stripe");
            jsonRequest.put("method", "visa");
            jsonRequest.put("token", tokenId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doCheckoutStripe data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().depositStripe(UserManager.getUserToken(), body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                LogUtils.d(TAG, "doCheckoutStripe onResponse : " + response.body());
                LogUtils.d(TAG, "doCheckoutStripe code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Utils.showLongToast(RechargeStripeActivity.this, getString(R.string.recharge_success), false, false);
                    setResult(Constants.PROMOTION_RESULT_CODE);
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNPROCESSABLE_ENTITY) {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "doCheckoutStripe error : " + error.toString());
                    DialogUtils.showOkDialog(RechargeStripeActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(RechargeStripeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doCheckoutStripe(tokenId);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LogUtils.d(TAG, "doCheckoutStripe onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(RechargeStripeActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doCheckoutStripe(tokenId);
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_recharge:
                doNext();
                break;

        }
    }
}
