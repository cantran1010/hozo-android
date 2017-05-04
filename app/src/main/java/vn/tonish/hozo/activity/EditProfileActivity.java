package vn.tonish.hozo.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import vn.tonish.hozo.view.EdittextHozo;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.MultipartRequest;
import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;


/**
 * Created by LongBui on 4/21/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = EditProfileActivity.class.getSimpleName();

    protected ButtonHozo btnSave;
    private CircleImageView imgAvatar;
    private String imgPath;
    private TextViewHozo tvCancel;
    private ImageView imgCamera;
    private EdittextHozo edtName, edtAddress;
    private TextViewHozo tvBirthday;
    private Calendar calendar = Calendar.getInstance();
    private File file;
    private User user;
    private String avataId;

    @Override
    protected int getLayout() {
        return R.layout.edit_profile_activity;
    }

    @Override
    protected void initView() {
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        btnSave = (ButtonHozo) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        imgCamera = (ImageView) findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(this);

        edtName = (EdittextHozo) findViewById(R.id.edt_name);
        edtAddress = (EdittextHozo) findViewById(R.id.edt_address);

        tvBirthday = (TextViewHozo) findViewById(R.id.tv_birthday);
        tvBirthday.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        user = (User) getIntent().getSerializableExtra(Constants.DATA);

        edtName.setText(user.getFull_name());
        edtAddress.setText(user.getAddress());
        tvBirthday.setText(user.getDate_of_birth());
        Utils.displayImageAvatar(this, imgAvatar, user.getAvatar());
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_save:
                doSave();
                break;

            case R.id.img_camera:
                doPickImage();
                break;

            case R.id.tv_cancel:
                finish();
                break;

            case R.id.tv_birthday:
                openDatePicker();
                break;

        }
    }

    private void doSave() {
        MultipartRequest multipartRequest = new MultipartRequest(this, NetworkConfig.API_UPDATE_AVATA, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, "volley onErrorResponse : " + error.toString());

                if (error.getMessage().equals(Constants.ERROR_AUTHENTICATION)) {
                    // HTTP Status Code: 401 Unauthorized
                    // Refresh token
                    NetworkUtils.RefreshToken(EditProfileActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish(JSONObject jsonResponse) {
                            UserManager.insertUserLogin(new DataParse().getUserEntiny(EditProfileActivity.this, jsonResponse), EditProfileActivity.this);
                            doSave();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(EditProfileActivity.this, EditProfileActivity.this.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                        @Override
                        public void onOkClick() {
                            doSave();
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
                }
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                LogUtils.d(TAG, "volley onResponse : " + s);

                HashMap<String, String> dataRequest = new HashMap<>();
                dataRequest.put("full_name", edtName.getText().toString());
                dataRequest.put("address", edtAddress.getText().toString());
                dataRequest.put("date_of_birth", tvBirthday.getText().toString());
                dataRequest.put("avatar_id", DataParse.getAvatarTempId(s));
                NetworkUtils.postVolleyFormData(true, true, true, EditProfileActivity.this, NetworkConfig.API_UPDATE_USER, dataRequest, new NetworkUtils.NetworkListener() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            user.setFull_name(edtName.getText().toString());
                            user.setAddress(edtAddress.getText().toString());
                            user.setDate_of_birth(tvBirthday.getText().toString());
                            user.setAvatar(jsonResponse.getJSONObject("data").getJSONObject("user").getString("avatar"));

                            Intent intent = new Intent();
                            intent.putExtra(Constants.DATA,user);
                            setResult(Constants.RESULT_CODE_UPDATE_PROFILE,intent);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

            }
        }, file);
        Volley.newRequestQueue(this).add(multipartRequest);
    }

    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvBirthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        calendar.set(year, monthOfYear, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            String imgPath = data.getStringExtra(Constants.EXTRA_IMAGE_PATH);
            Utils.displayImage(EditProfileActivity.this, imgAvatar, imgPath);

            file = new File(imgPath);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(EditProfileActivity.this, CropImageActivity.class);
            intent.putExtra(Constants.EXTRA_IMAGE_PATH, getImagePath());
            startActivityForResult(intent, Constants.REQUEST_CODE_CROP_IMAGE);
        } else if (requestCode == Constants.REQUEST_CODE_CROP_IMAGE && resultCode == Constants.RESPONSE_CODE_CROP_IMAGE) {
            String imgPath = data.getStringExtra(Constants.EXTRA_IMAGE_PATH);
            Utils.displayImage(EditProfileActivity.this, imgAvatar, imgPath);

            file = new File(imgPath);
        }

    }

    private void doPickImage() {

        PickImageDialog pickImageDialog = new PickImageDialog(EditProfileActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(EditProfileActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                intent.putExtra(Constants.EXTRA_IS_CROP_PROFILE, true);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
            }
        });
        pickImageDialog.showView();
    }

    public Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return imgPath;
    }
}
