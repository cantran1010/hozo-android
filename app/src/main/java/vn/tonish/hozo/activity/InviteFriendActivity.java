package vn.tonish.hozo.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ReferralResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 2/7/18.
 */

public class InviteFriendActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = InviteFriendActivity.class.getSimpleName();
    private TextViewHozo tvPrice;
    private TextViewHozo tvPromotion;
    private TextViewHozo tvlinkShare;
    private ReferralResponse referralResponse;


    @Override
    protected int getLayout() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected void initView() {
        tvPrice = (TextViewHozo) findViewById(R.id.tv_invite_price);
        TextViewHozo tvWeb = (TextViewHozo) findViewById(R.id.invite_web);
        tvPromotion = (TextViewHozo) findViewById(R.id.invite_promotion_code);
        tvlinkShare = (TextViewHozo) findViewById(R.id.invite_link_share);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        tvWeb.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        getReferralProgram();
        registerForContextMenu(tvPromotion);

    }

    @Override
    protected void resumeData() {

    }

    private void getReferralProgram() {
        ProgressDialogUtils.showProgressDialog(this);
        Call<ReferralResponse> call = ApiClient.getApiService().referralProgram(UserManager.getUserToken());
        call.enqueue(new Callback<ReferralResponse>() {
            @Override
            public void onResponse(Call<ReferralResponse> call, Response<ReferralResponse> response) {
                LogUtils.d(TAG, "getReferralProgram code : " + response.code());
                LogUtils.d(TAG, "getReferralProgram body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    referralResponse = response.body();
                    tvPrice.setText(Utils.formatNumber(Integer.parseInt(referralResponse.getPrice())));
                    tvPromotion.setText(coverCountryCode(referralResponse.getPhone()));
                    setUnderLinePolicy(tvlinkShare);

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(InviteFriendActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getReferralProgram();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(InviteFriendActivity.this);
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<ReferralResponse> call, Throwable t) {
                LogUtils.d(TAG, "getReferralProgram error : " + t.getMessage());
                DialogUtils.showRetryDialog(InviteFriendActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getReferralProgram();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void setUnderLinePolicy(TextViewHozo textViewHozo) {
        String text = getString(R.string.invite_link_share);
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);

        ClickableSpan conditionClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                doShareApp();
            }
        };
        ssBuilder.setSpan(
                conditionClickableSpan, // Span to add
                text.indexOf(getString(R.string.invite_click_link)), // Start of the span (inclusive)
                text.indexOf(getString(R.string.invite_click_link)) + String.valueOf(getString(R.string.invite_click_link)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssBuilder.setSpan(
                new ForegroundColorSpan(Color.parseColor("#444444")), // Span to add
                text.indexOf(getString(R.string.invite_click_link)), // Start of the span (inclusive)
                text.indexOf(getString(R.string.invite_click_link)) + String.valueOf(getString(R.string.invite_click_link)).length(), // End of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textViewHozo.setText(ssBuilder);
        textViewHozo.setMovementMethod(LinkMovementMethod.getInstance());
        textViewHozo.setHighlightColor(Color.TRANSPARENT);
    }

    private void openGeneralInfoActivity(String title, String link) {
        Intent intent = new Intent(this, GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, link);
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
    }

    private void doShareApp() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String content = getString(R.string.share_app_content);
            content = content + referralResponse.getUrl() + " \n";
            i.putExtra(Intent.EXTRA_TEXT, content);
            startActivity(Intent.createChooser(i, getString(R.string.share_app_title)));
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showLongToast(this, getString(R.string.share_app_error), true, false);
        }

    }

    private String coverCountryCode(String phone) {
        String numberPhone = phone;
        if (phone.startsWith("+")) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phone, "");
                String countryCode = "+" + numberProto.getCountryCode();
                LogUtils.d(TAG, "code" + countryCode);
                numberPhone = phone.replace(countryCode, getString(R.string.zero));
            } catch (NumberParseException e) {
                LogUtils.d(TAG, "error" + e.toString());
            }
        }
        return numberPhone;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, v.getId(), 0, "Copy");
        TextView mPhone = (TextView) v;
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("hozo", mPhone.getText());
        cm.setPrimaryClip(clipData);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.invite_web:
                openGeneralInfoActivity(getString(R.string.collaborators), referralResponse.getLink());
                break;
        }

    }
}
