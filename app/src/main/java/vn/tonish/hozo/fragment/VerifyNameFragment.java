package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by CanTran on 5/10/17.
 */

public class VerifyNameFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = VerifyNameFragment.class.getName();
    private EdittextHozo edtName;
    private TextViewHozo btnSave;


    @Override
    protected int getLayout() {
        return R.layout.verify_name_fragment;
    }

    @Override
    protected void initView() {
        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        btnSave = (TextViewHozo) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        setDefaultSetting();
        btnSave.setOnClickListener(this);
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtName.getText().toString().trim().length() > 5 && (edtName.getText().toString().trim().length() < 50)) {
                    btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
                    btnSave.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                updateFullName();
                break;
        }

    }

    private void setDefaultSetting() {
        SettingEntiny settingEntiny = new SettingEntiny();
        settingEntiny.setUserId(UserManager.getMyUser().getId());
        settingEntiny.setLatitude(21.028511);
        settingEntiny.setLongitude(105.804817);
        settingEntiny.setRadius(50);
        settingEntiny.setGender(getString(R.string.gender_any));
        settingEntiny.setMinWorkerRate(1000);
        settingEntiny.setMaxWorkerRate(100000000);
        SettingManager.insertSetting(settingEntiny);

    }



    private void updateFullName() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.USER_ID, UserManager.getMyUser().getId());
            jsonRequest.put(Constants.USER_MOBILE, UserManager.getMyUser().getPhone());
            jsonRequest.put(Constants.USER_FULL_NAME, edtName.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.d(TAG, "onResponse status code : " + response.code());
                LogUtils.d(TAG, "onResponse body : " + response.body().toString());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        DataParse.updateUser(response.body(), getContext());
                        LogUtils.d(TAG, "update User : " + UserManager.getMyUser().toString());
                    }
                    startActivityAndClearAllTask(new Intent(getContext(), MainActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                } else {

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                ProgressDialogUtils.dismissProgressDialog();
                showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateFullName();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }
}
