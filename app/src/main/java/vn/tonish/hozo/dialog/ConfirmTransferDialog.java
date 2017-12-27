package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Bank;
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
    private ButtonHozo btnConfirm;

    public ConfirmTransferDialog(@NonNull Context context) {
        super(context);
        setCancelable(true);
    }

    @Override
    protected int getLayout() {
        return R.layout.confirm_transfer_dialog;
    }

    @Override
    protected void initData() {
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);

        tvMoney = findViewById(R.id.tv_money);
        tvReceiver = findViewById(R.id.tv_receiver);
        tvCardNumber = findViewById(R.id.tv_card_number);
        tvBankName = findViewById(R.id.tv_bank_name);

        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    public void updateUi(long price, Bank bank) {
        tvMoney.setText(getContext().getString(R.string.unit3, Utils.formatNumber(price)));
        tvReceiver.setText(bank.getReceiver());
        tvCardNumber.setText(bank.getCardNumber());
        tvBankName.setText(bank.getBankName());
    }

    private void doConfirm() {

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
