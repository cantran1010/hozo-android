package vn.tonish.hozo.activity;

import android.content.Intent;

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
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.BlockResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by CanTran on 4/11/17.
 */

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        if (UserManager.checkLogin())
            checkBlockUser();
        else {
            startActivity(HomeActivity.class, TransitionScreen.FADE_IN);
            finish();
        }
    }

    private void checkBlockUser() {
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("user_id", UserManager.getMyUser().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "checkBlockUser data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());

        ApiClient.getApiService().checkBlockUser(body).enqueue(new Callback<BlockResponse>() {
            @Override
            public void onResponse(Call<BlockResponse> call, Response<BlockResponse> response) {
                LogUtils.d(TAG, "checkBlockUser , code : " + response.code());
                LogUtils.d(TAG, "checkBlockUser , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_OK) {

                    BlockResponse blockResponse = response.body();

                    if (blockResponse.getIsBlock()) {
                        Intent intent = new Intent(SplashActivity.this, BlockActivity.class);
                        intent.putExtra(Constants.BLOCK_EXTRA, blockResponse);
                        startActivity(intent, TransitionScreen.FADE_IN);
                        finish();
                    } else {
                        startActivity(MainActivity.class, TransitionScreen.FADE_IN);
                        finish();
                    }

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(SplashActivity.this);
                } else {
                    DialogUtils.showRetryDialog(SplashActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            checkBlockUser();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<BlockResponse> call, Throwable t) {
                DialogUtils.showRetryDialog(SplashActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        checkBlockUser();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    @Override
    protected void resumeData() {

    }
}
