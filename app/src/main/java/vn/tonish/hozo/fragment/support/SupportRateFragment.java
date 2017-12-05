package vn.tonish.hozo.fragment.support;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RatingBar;

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
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 11/1/17.
 */

public class SupportRateFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = SupportRateFragment.class.getSimpleName();
    private RatingBar ratingBar;
    private EdittextHozo edtContent;

    @Override
    protected int getLayout() {
        return R.layout.support_rate_fragment;
    }

    @Override
    protected void initView() {
        ButtonHozo btnSend = (ButtonHozo) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        ratingBar = (RatingBar) findViewById(R.id.rb_rate);
        edtContent = (EdittextHozo) findViewById(R.id.edt_content);

        TextViewHozo tvGoPlayStore = (TextViewHozo) findViewById(R.id.tv_go_play_store);
        tvGoPlayStore.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    edtContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

//                else {
//                    //Assign your image again to the view, otherwise it will always be gone even if the text is 0 again.
//                    edtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
//                }
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    private void doRate() {

        if (ratingBar.getRating() == 0) {
            Utils.showLongToast(getActivity(), getString(R.string.rate_miss_star), true, false);
            return;
        }

        ProgressDialogUtils.showProgressDialog(getActivity());
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("body", edtContent.getText().toString().trim());
            jsonRequest.put("rating", ratingBar.getRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "doRate data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().sendFeedback(UserManager.getUserToken(), body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                LogUtils.d(TAG, "doRate onResponse : " + response.body());
                LogUtils.d(TAG, "doRate code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    Utils.showLongToast(getActivity(), getString(R.string.rate_success), false, false);
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doRate();
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
                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doRate();
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

            case R.id.btn_send:
                doRate();
                break;

            case R.id.tv_go_play_store:
                Utils.openPlayStore(getActivity());
                break;

        }
    }
}
