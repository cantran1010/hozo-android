package vn.tonish.hozo.activity;

import android.content.Intent;
import android.net.Uri;

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
import vn.tonish.hozo.dialog.AlertDialogOkFullScreen;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.BlockResponse;
import vn.tonish.hozo.rest.responseRes.UpdateResponse;
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
        checkUpdate();
    }

    private void checkUpdate() {
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("device_type", getString(R.string.device_type));
            jsonRequest.put("version", Utils.getCurrentVersion(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.d(TAG, "checkUpdate data request : " + jsonRequest.toString());
        final RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().apdateVersion(body).enqueue(new Callback<UpdateResponse>() {
            @Override
            public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                LogUtils.d(TAG, "checkUpdate data code : " + response.code());
                LogUtils.d(TAG, "checkUpdate data body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    UpdateResponse updateResponse = response.body();
                    if (updateResponse.getForceUpdate().equalsIgnoreCase("true")) {
                        DialogUtils.showForceUpdateDialog(SplashActivity.this, new AlertDialogOkFullScreen.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                updateVerSion();
                            }
                        });

                    } else if (updateResponse.getRecommendUpdate().equalsIgnoreCase("true")) {
                        showUpdateDialog();
                    } else {
                        if (UserManager.checkLogin())
                            checkBlockUser();
                        else {
                            startActivity(HomeActivity.class, TransitionScreen.FADE_IN);
                            finish();
                        }
                    }

                } else {
                    DialogUtils.showRetryDialog(SplashActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            checkUpdate();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<UpdateResponse> call, Throwable throwable) {
                DialogUtils.showRetryDialog(SplashActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        checkUpdate();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

    }

    private void showUpdateDialog() {
        DialogUtils.showReCommendUpdateDialog(SplashActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
            @Override
            public void onSubmit() {
                updateVerSion();
            }

            @Override
            public void onCancel() {
                if (UserManager.checkLogin())
                    checkBlockUser();
                else {
                    startActivity(HomeActivity.class, TransitionScreen.FADE_IN);
                    finish();
                }
            }
        });

    }

    private void updateVerSion() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                    ("market://details?id=" + getPackageName())));
        } catch (Exception e) {
            e.printStackTrace();
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
