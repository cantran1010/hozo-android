package vn.tonish.hozo.activity.payment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
import vn.tonish.hozo.model.Bank;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.BankResponse;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 12/25/17.
 */

public class BankActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BankActivity.class.getSimpleName();
    private Bank bank;
    private ButtonHozo btnAddBank;
    private EdittextHozo edtReceiver, edtCardNumber, edtBranch;
    private Spinner spBank;
    private ArrayList<BankResponse> bankResponses;
    private TextViewHozo tvTitle;
    private boolean isEdit = false;
    private CircleImageView imgAvatar;
    private TextViewHozo tvUserName;

    @Override
    protected int getLayout() {
        return R.layout.activity_bank;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        btnAddBank = (ButtonHozo) findViewById(R.id.btn_add_bank);
        btnAddBank.setOnClickListener(this);

        edtReceiver = (EdittextHozo) findViewById(R.id.edt_receiver);
        edtCardNumber = (EdittextHozo) findViewById(R.id.edt_card_number);
        edtBranch = (EdittextHozo) findViewById(R.id.edt_branch);

        spBank = (Spinner) findViewById(R.id.sp_bank);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        tvUserName = (TextViewHozo) findViewById(R.id.tv_user_name);
    }

    @Override
    protected void initData() {

        Utils.displayImageAvatar(this, imgAvatar, UserManager.getMyUser().getAvatar());
        tvUserName.setText(UserManager.getMyUser().getFullName());

        if (getIntent().hasExtra(Constants.BANK_EDIT_EXTRA)) isEdit = true;
        getBanks();

        if (isEdit) {
            bank = (Bank) getIntent().getSerializableExtra(Constants.BANK_EDIT_EXTRA);

            edtReceiver.setText(bank.getReceiver());
            edtCardNumber.setText(bank.getCardNumber());
            edtBranch.setText(bank.getBranchName());

            tvTitle.setText(getString(R.string.edit_bank_title));
            btnAddBank.setText(getString(R.string.btn_add_bank));
        }

    }

    @Override
    protected void resumeData() {

    }

    private void getBanks() {
        ApiClient.getApiService().getBanks(UserManager.getUserToken()).enqueue(new Callback<List<BankResponse>>() {
            @Override
            public void onResponse(Call<List<BankResponse>> call, Response<List<BankResponse>> response) {
                LogUtils.d(TAG, "getBanks code : " + response.code());
                LogUtils.d(TAG, "getBanks response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {

                    bankResponses = (ArrayList<BankResponse>) response.body();

                    List<String> list = new ArrayList<>();
                    for (BankResponse bankResponse : response.body()) {
                        list.add(bankResponse.getName());
                    }

                    HozoSpinnerAdapter dataAdapter = new HozoSpinnerAdapter(BankActivity.this, list);
                    spBank.setAdapter(dataAdapter);

                    if (isEdit) {
                        spBank.setSelection(getPositionByKey(bank.getBankName()));
                    }

                } else {
                    DialogUtils.showRetryDialog(BankActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getBanks();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<BankResponse>> call, Throwable t) {
                DialogUtils.showRetryDialog(BankActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getBanks();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

    private void doAddBank() {

        if (TextUtils.isEmpty(edtReceiver.getText().toString())) {
            edtReceiver.requestFocus();
            edtReceiver.setError(getString(R.string.add_bank_null_receiver));
            return;
        }

        if (TextUtils.isEmpty(edtCardNumber.getText().toString())) {
            edtCardNumber.requestFocus();
            edtCardNumber.setError(getString(R.string.add_bank_null_number));
            return;
        }

        if (TextUtils.isEmpty(edtBranch.getText().toString())) {
            edtBranch.requestFocus();
            edtBranch.setError(getString(R.string.add_bank_null_branch));
            return;
        }

        if (bankResponses == null || bankResponses.size() == 0) {
            Utils.showLongToast(this, getString(R.string.bank_null), true, false);
            return;
        }

        if (isEdit) {
            ProgressDialogUtils.showProgressDialog(this);
            final JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("key", bankResponses.get(spBank.getSelectedItemPosition()).getKey());
                jsonRequest.put("name", edtReceiver.getText().toString());
                jsonRequest.put("number", edtCardNumber.getText().toString());
                jsonRequest.put("branch", edtBranch.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LogUtils.d(TAG, "doAddBank data request : " + jsonRequest.toString());
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

            ApiClient.getApiService().editBank(UserManager.getUserToken(), bank.getId(), body).enqueue(new Callback<Bank>() {
                @Override
                public void onResponse(Call<Bank> call, Response<Bank> response) {

                    LogUtils.d(TAG, "doAddBank onResponse : " + response.body());
                    LogUtils.d(TAG, "doAddBank code : " + response.code());

                    if (response.code() == Constants.HTTP_CODE_OK) {
                        Utils.showLongToast(BankActivity.this, getString(R.string.edit_bank_success), false, false);
                        bank = response.body();
                        Intent intent = new Intent();
                        intent.putExtra(Constants.BANK_EXTRA, bank);
                        setResult(Constants.EDIT_BANK_RESULT_CODE, intent);
                        finish();
                    } else {
                        APIError error = ErrorUtils.parseError(response);
                        DialogUtils.showOkDialog(BankActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                    ProgressDialogUtils.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<Bank> call, Throwable t) {
                    DialogUtils.showRetryDialog(BankActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doAddBank();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                }
            });
        } else {
            ProgressDialogUtils.showProgressDialog(this);
            final JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("key", bankResponses.get(spBank.getSelectedItemPosition()).getKey());
                jsonRequest.put("name", edtReceiver.getText().toString());
                jsonRequest.put("number", edtCardNumber.getText().toString());
                jsonRequest.put("branch", edtBranch.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LogUtils.d(TAG, "doAddBank data request : " + jsonRequest.toString());
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

            ApiClient.getApiService().addBank(UserManager.getUserToken(), body).enqueue(new Callback<Bank>() {
                @Override
                public void onResponse(Call<Bank> call, Response<Bank> response) {

                    LogUtils.d(TAG, "doAddBank onResponse : " + response.body());
                    LogUtils.d(TAG, "doAddBank code : " + response.code());

                    if (response.code() == Constants.HTTP_CODE_OK) {
                        Utils.showLongToast(BankActivity.this, getString(R.string.add_bank_success), false, false);
                        bank = response.body();
                        Intent intent = new Intent();
                        intent.putExtra(Constants.BANK_EXTRA, bank);
                        setResult(Constants.BANK_RESULT_CODE, intent);
                        finish();
                    } else {
                        APIError error = ErrorUtils.parseError(response);
                        DialogUtils.showOkDialog(BankActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                    ProgressDialogUtils.dismissProgressDialog();
                }

                @Override
                public void onFailure(Call<Bank> call, Throwable t) {
                    DialogUtils.showRetryDialog(BankActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doAddBank();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    ProgressDialogUtils.dismissProgressDialog();
                }
            });
        }

    }

    private int getPositionByKey(String key) {
        for (int i = 0; i < bankResponses.size(); i++) {
            if (key.equals(bankResponses.get(i).getName())) return i;
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.btn_add_bank:
                doAddBank();
                break;

        }
    }

}
