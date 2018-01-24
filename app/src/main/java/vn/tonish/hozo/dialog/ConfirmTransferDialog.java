package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Bank;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.rest.responseRes.WithdrawMoneyReponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 6/21/17.
 */

public class ConfirmTransferDialog extends BaseDialogFullScreenAnimFadeInOut implements View.OnClickListener {

    private static final String TAG = ConfirmTransferDialog.class.getSimpleName();
    private ImageView imgClose;
    private TextViewHozo tvMoney, tvReceiver, tvCardNumber, tvBankName;
    private Bank bank;
    private long price;

    public ConfirmTransferDialog(@NonNull Context context) {
        super(context);
        setCancelable(true);
    }

    public interface ConfirmTransferListener {
        public void onConfirm();
    }

    private ConfirmTransferListener confirmTransferListener;

    public ConfirmTransferListener getConfirmTransferListener() {
        return confirmTransferListener;
    }

    public void setConfirmTransferListener(ConfirmTransferListener confirmTransferListener) {
        this.confirmTransferListener = confirmTransferListener;
    }

    @Override
    protected int getLayout() {
        return R.layout.confirm_transfer_dialog;
    }

    @Override
    protected void initData() {
        ImageView imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);

        tvMoney = findViewById(R.id.tv_money);
        tvReceiver = findViewById(R.id.tv_receiver);
        tvCardNumber = findViewById(R.id.tv_card_number);
        tvBankName = findViewById(R.id.tv_bank_name);

        ButtonHozo btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    public void updateUi(long price, Bank bank) {
        LogUtils.d(TAG, "updateUi , bank : " + bank.toString());
        LogUtils.d(TAG, "updateUi , price : " + price);

        this.bank = bank;
        this.price = price;

        tvMoney.setText(getContext().getString(R.string.unit3, Utils.formatNumber(price)));
        tvReceiver.setText(bank.getReceiver());
        tvCardNumber.setText(bank.getCardNumber());
        tvBankName.setText(getContext().getString(R.string.bank_name_confirm, bank.getBankName(), bank.getVnBankName()));
    }

    private void doConfirm() {
        ProgressDialogUtils.showProgressDialog(getContext());
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("bank_id", bank.getId());
            jsonRequest.put("amount", price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doConfirm data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().withdrawMoney(UserManager.getUserToken(), body).enqueue(new Callback<WithdrawMoneyReponse>() {
            @Override
            public void onResponse(Call<WithdrawMoneyReponse> call, Response<WithdrawMoneyReponse> response) {

                LogUtils.d(TAG, "doConfirm onResponse : " + response.body());
                LogUtils.d(TAG, "doConfirm code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Utils.showLongToast(getContext(), getContext().getString(R.string.withdraw_success), false, false);
                    hideView();
                    if (confirmTransferListener != null) confirmTransferListener.onConfirm();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    if (error.status().equals("invalid_data")) {
                        DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), getContext().getString(R.string.withdraw_error_invalid_data), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("no_bank_account")) {
                        DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), getContext().getString(R.string.withdraw_error_no_bank), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("not_enough_balance")) {
                        DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), getContext().getString(R.string.withdraw_error_not_enough), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    } else if (error.status().equals("system_error")) {
                        DialogUtils.showOkDialog(getContext(), getContext().getString(R.string.error), getContext().getString(R.string.withdraw_error_invalid_data), getContext().getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                            @Override
                            public void onSubmit() {

                            }
                        });
                    }
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<WithdrawMoneyReponse> call, Throwable t) {
                DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doConfirm();
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

            case R.id.img_close:
                hideView();
                break;

            case R.id.btn_confirm:
                doConfirm();
                break;

        }
    }

}
