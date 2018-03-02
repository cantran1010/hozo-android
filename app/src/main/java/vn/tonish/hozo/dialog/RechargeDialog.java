package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.InviteFriendActivity;
import vn.tonish.hozo.activity.payment.MyWalletActivity;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 6/21/17.
 */

public class RechargeDialog extends BaseDialog implements View.OnClickListener {

    private static final String TAG = RechargeDialog.class.getSimpleName();
    private TextViewHozo tvRechargeDes;
    private Context context;

    public RechargeDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.recharge_dialog;
    }

    @Override
    protected void initData() {
        ImageView imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);

        tvRechargeDes = findViewById(R.id.tv_recharge_des);

        ButtonHozo btnWallet = findViewById(R.id.btn_wallet);
        btnWallet.setOnClickListener(this);

        ButtonHozo btnCollaborators = findViewById(R.id.btn_collaborators);
        btnCollaborators.setOnClickListener(this);
    }

    public void updateUi(int price) {
        LogUtils.d(TAG, "updateUi , price : " + price);
        tvRechargeDes.setText(getContext().getString(R.string.bid_not_enough_money, Utils.formatNumber(price)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_close:
                hideView();
                break;

            case R.id.btn_wallet:
                ((BaseActivity) context).startActivity(MyWalletActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                hideView();
                break;

            case R.id.btn_collaborators:
                ((BaseActivity) context).startActivity(InviteFriendActivity.class, TransitionScreen.RIGHT_TO_LEFT);
                hideView();
                break;

        }
    }

}
