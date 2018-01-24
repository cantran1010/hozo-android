package vn.tonish.hozo.activity.payment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.BankAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.ConfirmTransferDialog;
import vn.tonish.hozo.model.Bank;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;

/**
 * Created by LongBui on 12/25/17.
 */

public class WithdrawalActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = WithdrawalActivity.class.getSimpleName();
    private final ArrayList<Bank> banks = new ArrayList<>();
    private RecyclerView rcvBank;
    private BankAdapter bankAdapter;
    private EdittextHozo edtMoney;
    private int balanceCash;

    @Override
    protected int getLayout() {
        return R.layout.withdrawal_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        ImageView imgAddBank = (ImageView) findViewById(R.id.img_add_bank);
        imgAddBank.setOnClickListener(this);

        rcvBank = (RecyclerView) findViewById(R.id.rcv_bank);

        ButtonHozo btnConfirm = (ButtonHozo) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        edtMoney = (EdittextHozo) findViewById(R.id.edt_money);

    }

    @Override
    protected void initData() {
        balanceCash = getIntent().getIntExtra(Constants.BALANCE_CASH_EXTRA, 0);
        edtMoney.addTextChangedListener(new NumberTextWatcher(edtMoney));

        setUpBank();
        getBanksList();
    }

    @Override
    protected void resumeData() {

    }

    private void setUpBank() {
        bankAdapter = new BankAdapter(banks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBank.setLayoutManager(linearLayoutManager);
        rcvBank.setAdapter(bankAdapter);

        bankAdapter.setBankAdapterLister(new BankAdapter.BankAdapterLister() {
            @Override
            public void onItemClick(int position) {
                for (Bank bank : banks
                        ) {
                    bank.setSelected(false);
                }

                banks.get(position).setSelected(true);
                bankAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDelete(final int position) {
                DialogUtils.showOkAndCancelDialog(WithdrawalActivity.this, getString(R.string.delete_bank_title),
                        getString(R.string.delete_bank_content, banks.get(position).getVnBankName()), getString(R.string.cancel_task_ok), getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                doDelete(position);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }

            @Override
            public void onEdit(int position) {
                Intent intent = new Intent(WithdrawalActivity.this, BankActivity.class);
                intent.putExtra(Constants.BANK_EDIT_EXTRA, banks.get(position));
                startActivityForResult(intent, Constants.EDIT_BANK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }

    private void doDelete(final int position) {
        ProgressDialogUtils.showProgressDialog(this);
        ApiClient.getApiService().deleteBank(UserManager.getUserToken(), banks.get(position).getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                LogUtils.d(TAG, "doDelete onResponse : " + response.body());
                LogUtils.d(TAG, "doDelete code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    banks.remove(position);
                    bankAdapter.notifyDataSetChanged();
                } else {
                    DialogUtils.showRetryDialog(WithdrawalActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getBanksList();
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
                DialogUtils.showRetryDialog(WithdrawalActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getBanksList();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void getBanksList() {
        ApiClient.getApiService().getBanksList(UserManager.getUserToken()).enqueue(new Callback<List<Bank>>() {
            @Override
            public void onResponse(Call<List<Bank>> call, Response<List<Bank>> response) {
                LogUtils.d(TAG, "getBanksList code : " + response.code());
                LogUtils.d(TAG, "getBanksList response : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    banks.addAll(response.body());
                    bankAdapter.notifyDataSetChanged();
                } else {
                    DialogUtils.showRetryDialog(WithdrawalActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getBanksList();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Bank>> call, Throwable t) {
                DialogUtils.showRetryDialog(WithdrawalActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getBanksList();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.BANK_REQUEST_CODE
                && resultCode == Constants.BANK_RESULT_CODE) {
            Bank bank = (Bank) data.getSerializableExtra(Constants.BANK_EXTRA);
            banks.add(bank);
            bankAdapter.notifyDataSetChanged();
        } else if (requestCode == Constants.EDIT_BANK_REQUEST_CODE
                && resultCode == Constants.EDIT_BANK_RESULT_CODE) {
            Bank bank = (Bank) data.getSerializableExtra(Constants.BANK_EXTRA);

            for (int i = 0; i < banks.size(); i++) {
                if (bank.getId() == banks.get(i).getId())
                    banks.set(i, bank);
                break;
            }
            bankAdapter.notifyDataSetChanged();
        }

    }

    private void doConfirm() {

        if (TextUtils.isEmpty(edtMoney.getText().toString())) {
            edtMoney.requestFocus();
            edtMoney.setError(getString(R.string.null_money_transfer));
            return;
        }

        if (balanceCash < 50000) {
            edtMoney.requestFocus();
            edtMoney.setError(getString(R.string.no_money_transfer));
            return;
        }

        if (Utils.getLongEdittext(edtMoney) < 50000) {
            edtMoney.requestFocus();
            edtMoney.setError(getString(R.string.min_money_transfer, "50.000"));
            return;
        }

        if (Utils.getLongEdittext(edtMoney) > balanceCash) {
            edtMoney.requestFocus();
            edtMoney.setError(getString(R.string.max_money_transfer, Utils.formatNumber(balanceCash)));
            return;
        }

        if (getBankSelected() == null) {
            Utils.showLongToast(this, getString(R.string.bank_select_error), true, false);
            return;
        }

        ConfirmTransferDialog confirmTransferDialog = new ConfirmTransferDialog(this);
        confirmTransferDialog.showView();
        confirmTransferDialog.updateUi(Utils.getLongEdittext(edtMoney), getBankSelected());
        confirmTransferDialog.setConfirmTransferListener(new ConfirmTransferDialog.ConfirmTransferListener() {
            @Override
            public void onConfirm() {
                finish();
            }
        });
    }

    private Bank getBankSelected() {
        for (int i = 0; i < banks.size(); i++)
            if (banks.get(i).isSelected())
                return banks.get(i);
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.img_add_bank:
                Intent intent = new Intent(WithdrawalActivity.this, BankActivity.class);
                startActivityForResult(intent, Constants.BANK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                break;

            case R.id.btn_confirm:
                doConfirm();
                break;

        }
    }
}
