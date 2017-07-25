package vn.tonish.hozo.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.DateTimeUtils.getOnlyIsoFromDate;
import static vn.tonish.hozo.utils.DialogUtils.showRetryDialog;

/**
 * Created by CanTran on 5/10/17.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private final static String TAG = RegisterFragment.class.getName();
    private EdittextHozo edtName, edtAddress;
    private TextViewHozo btnSave;
    private TextViewHozo tvMale, tvFemale;
    private ImageView imgMale, imgFemale;
    private TextViewHozo tvBirthday;
    private String gender;
    private final Calendar calendar = Calendar.getInstance();


    @Override
    protected int getLayout() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView() {
        tvMale = (TextViewHozo) findViewById(R.id.tv_male);
        tvFemale = (TextViewHozo) findViewById(R.id.tv_female);
        imgMale = (ImageView) findViewById(R.id.img_male);
        imgFemale = (ImageView) findViewById(R.id.img_female);
        tvBirthday = (TextViewHozo) findViewById(R.id.tv_birthday);
        edtAddress = (EdittextHozo) findViewById(R.id.edt_address);
        RelativeLayout layoutMale = (RelativeLayout) findViewById(R.id.layout_male);
        layoutMale.setOnClickListener(this);
        RelativeLayout layoutBirthday = (RelativeLayout) findViewById(R.id.layout_birthday);
        layoutBirthday.setOnClickListener(this);
        RelativeLayout layoutFemale = (RelativeLayout) findViewById(R.id.layout_female);
        layoutFemale.setOnClickListener(this);

        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        btnSave = (TextViewHozo) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        btnSave.setOnClickListener(this);
    }


    @Override
    protected void resumeData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                String name = edtName.getText().toString().trim();
                if (name.isEmpty()) {
                    edtName.setError(getActivity().getString(R.string.erro_empty_name));
                } else {
                    updateInfor();
                }
                break;
            case R.id.layout_birthday:
                openDatePicker();
                break;

            case R.id.layout_male:
                gender = getString(R.string.gender_male);
                updateGender(getString(R.string.gender_male));
                break;

            case R.id.layout_female:
                gender = getString(R.string.gender_female);
                updateGender(getString(R.string.gender_female));
                break;

        }

    }

    private void openDatePicker() {

        if (!tvBirthday.getText().toString().isEmpty()) {
            Date date = DateTimeUtils.convertToDate(tvBirthday.getText().toString());
            calendar.setTime(date);
        }

        @SuppressWarnings("deprecation") DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvBirthday.setText(getString(R.string.edit_profile_birthday, dayOfMonth, monthOfYear + 1, year));
                        calendar.set(year, monthOfYear, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 10000);
        datePickerDialog.show();
    }

    private void updateGender(String gender) {
        if (gender.equals(getString(R.string.gender_male))) {
            tvMale.setAlpha(1);
            tvFemale.setAlpha(0.7f);
            imgMale.setImageResource(R.drawable.gender_male_on);
            imgFemale.setImageResource(R.drawable.gender_female_off);
        } else if (gender.equals(getString(R.string.gender_female))) {
            tvMale.setAlpha(0.7f);
            tvFemale.setAlpha(1);
            imgMale.setImageResource(R.drawable.gender_male_off);
            imgFemale.setImageResource(R.drawable.gender_female_white);
        } else {
            tvMale.setTextColor(ContextCompat.getColor(getActivity(), R.color.tv_gray));
            tvFemale.setTextColor(ContextCompat.getColor(getActivity(), R.color.tv_gray));
            imgMale.setImageResource(R.drawable.gender_male_off);
            imgFemale.setImageResource(R.drawable.gender_female_off);
        }
    }

    private void updateInfor() {
        ProgressDialogUtils.showProgressDialog(getActivity());
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(Constants.PARAMETER_FULL_NAME, edtName.getText().toString());
            jsonRequest.put(Constants.PARAMETER_ADDRESS, edtAddress.getText().toString());

            if (!tvBirthday.getText().toString().equals(""))
                jsonRequest.put(Constants.PARAMETER_DATE_OF_BIRTH, getOnlyIsoFromDate(tvBirthday.getText().toString()));

            if (gender != null)
                jsonRequest.put(Constants.PARAMETER_GENDER, gender);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "aaaa onResponse updateInfor UserManager.getUserToken() : " + UserManager.getUserToken());

        ApiClient.getApiService().updateUser(UserManager.getUserToken(), body).enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                LogUtils.d(TAG, "onResponse updateInfor code : " + response.code());

                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "onResponse updateInfor body : " + response.body());
                    if (response.code() == Constants.HTTP_CODE_OK) {
                        if (response.body() != null) {
                            UserEntity myUser = UserManager.getMyUser();
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            myUser.setFullName(edtName.getText().toString());
                            realm.commitTransaction();
                        }
                        startActivityAndClearAllTask(new Intent(getContext(), MainActivity.class), TransitionScreen.RIGHT_TO_LEFT);
                    }
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            updateInfor();
                        }
                    });

                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(getActivity(), error.message(), true, false);
                }
                ProgressDialogUtils.dismissProgressDialog();

            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                LogUtils.e(TAG, "onFailure message : " + t.getMessage());
                ProgressDialogUtils.dismissProgressDialog();
                showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        updateInfor();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

}
