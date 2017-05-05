package vn.tonish.hozo.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.model.Work;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.MultipartRequest;
import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.MyGridView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;

/**
 * Created by LongBui on 4/12/17.
 */

public class PostATaskActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PostATaskActivity.class.getSimpleName();
    private TextViewHozo tvTitle;
    protected ButtonHozo btnNext;
    protected RelativeLayout layoutStartTime, layoutEndTime, layoutDate;
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private ArrayList<Image> images = new ArrayList<>();
    private String imgPath;
    private TextViewHozo tvStartTime, tvEndTime, tvDate;
    private Date dateWork;
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendarTimeStart = Calendar.getInstance();
    private Calendar calendarTimeEnd = Calendar.getInstance();
    private EdittextHozo edtDayWork, edtWorkName, edtDescription, edtAgeFrom, edtAgeTo;
    private Spinner spGender;
    private Category category;
    private ImageView imgClose;
    private int imageAttachCount;
    private Integer[] imagesArr;


    protected int getLayout() {
        return R.layout.activity_post_a_task;
    }

    @Override
    protected void initView() {

        imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);

        btnNext = (ButtonHozo) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        edtWorkName = (EdittextHozo) findViewById(R.id.edt_task_name);

        edtDayWork = (EdittextHozo) findViewById(R.id.edt_number_day);

        edtDescription = (EdittextHozo) findViewById(R.id.edt_description);
        edtAgeFrom = (EdittextHozo) findViewById(R.id.edt_age_from);
        edtAgeTo = (EdittextHozo) findViewById(R.id.edt_age_to);

        grImage = (MyGridView) findViewById(R.id.gr_image);

        spGender = (Spinner) findViewById(R.id.sp_gender);

        layoutStartTime = (RelativeLayout) findViewById(R.id.layout_start_time);
        layoutStartTime.setOnClickListener(this);

        layoutEndTime = (RelativeLayout) findViewById(R.id.layout_end_time);
        layoutEndTime.setOnClickListener(this);

        tvStartTime = (TextViewHozo) findViewById(R.id.tv_start_time);
        tvEndTime = (TextViewHozo) findViewById(R.id.tv_end_time);

        layoutDate = (RelativeLayout) findViewById(R.id.date_layout);
        layoutDate.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        category = (Category) getIntent().getSerializableExtra(Constants.EXTRA_CATEGORY);
        tvTitle.setText(category.getName());

        Image image = new Image();
        image.setAdd(true);

        images.add(image);

        imageAdapter = new ImageAdapter(this, images);
        grImage.setAdapter(imageAdapter);

        grImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (images.get(position).isAdd) {
                    PickImageDialog pickImageDialog = new PickImageDialog(PostATaskActivity.this);
                    pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
                        @Override
                        public void onCamera() {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                            startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
                        }

                        @Override
                        public void onGallery() {
                            Intent intent = new Intent(PostATaskActivity.this, AlbumActivity.class);
                            startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE);
                        }
                    });
                    pickImageDialog.showView();
                } else {
                    Intent intent = new Intent(PostATaskActivity.this, PreviewImageActivity.class);
                    intent.putExtra(Constants.EXTRA_IMAGE_PATH, images.get(position).getPath());
                    startActivity(intent);
                }
            }
        });

        imageAdapter.setImageAdapterListener(new ImageAdapter.ImageAdapterListener() {
            @Override
            public void onImageAdapterListener() {


                PickImageDialog pickImageDialog = new PickImageDialog(PostATaskActivity.this);
                pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
                    @Override
                    public void onCamera() {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
                    }


                    @Override
                    public void onGallery() {
                        Intent intent = new Intent(PostATaskActivity.this, AlbumActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE);
                    }
                });
                pickImageDialog.showView();
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                finish();
                break;

            case R.id.btn_next:
                doNext();
                break;

            case R.id.tv_address:
                Intent intent = new Intent(this, PostATaskMapActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADDRESS);
                break;

            case R.id.layout_start_time:
                TimePickerDialog timeStartPickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tvStartTime.setText(hourOfDay + ":" + minute);
                                calendarTimeStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendarTimeStart.set(Calendar.MINUTE, minute);
                                tvStartTime.setTextColor(ContextCompat.getColor(PostATaskActivity.this, R.color.tv_black));
                            }
                        }, calendarTimeStart.get((Calendar.HOUR_OF_DAY)), calendarTimeStart.get(Calendar.MINUTE), false);
                timeStartPickerDialog.show();
                break;

            case R.id.layout_end_time:
                TimePickerDialog timeEndPickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tvEndTime.setText(hourOfDay + ":" + minute);
                                calendarTimeEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendarTimeEnd.set(Calendar.MINUTE, minute);
                                tvEndTime.setTextColor(ContextCompat.getColor(PostATaskActivity.this, R.color.tv_black));
                            }
                        }, calendarTimeEnd.get((Calendar.HOUR_OF_DAY)), calendarTimeEnd.get(Calendar.MINUTE), false);
                timeEndPickerDialog.show();
                break;

            case R.id.date_layout:
                openDatePicker();
                break;
        }
    }

    private void doNext() {

        if (edtWorkName.getText().toString().length() < 10) {
            edtWorkName.requestFocus();
            edtWorkName.setError(getString(R.string.post_a_task_name_error));
            return;
        } else if (tvDate.getText().toString().equals(getString(R.string.date_pick_hint))) {
            tvDate.requestFocus();
            tvDate.setError(getString(R.string.post_a_task_date_error));
            return;
        } else if (edtDayWork.getText().toString().equals("")) {
            edtDayWork.requestFocus();
            edtDayWork.setError(getString(R.string.post_a_task_day_error));
            return;
        } else if (tvStartTime.getText().toString().equals(getString(R.string.start_time_hint))) {
            tvStartTime.requestFocus();
            tvStartTime.setError(getString(R.string.post_a_task_start_time_error));
            return;
        } else if (tvEndTime.getText().toString().equals(getString(R.string.end_time_hint))) {
            tvEndTime.requestFocus();
            tvEndTime.setError(getString(R.string.post_a_task_end_time_error));
            return;
        } else if (edtDescription.getText().toString().length() < 25) {
            edtDescription.requestFocus();
            edtDescription.setError(getString(R.string.post_a_task_description_error));
            return;
        } else if (edtAgeFrom.getText().toString().equals("")) {
            edtAgeFrom.requestFocus();
            edtAgeFrom.setError(getString(R.string.post_a_task_start_time_error));
            return;
        } else if (edtAgeTo.getText().toString().equals("")) {
            edtAgeTo.requestFocus();
            edtAgeTo.setError(getString(R.string.post_a_task_end_time_error));
            return;
        }

        if (images.size() == 1) {
            Work work = new Work();
            work.setName(edtWorkName.getText().toString());
            work.setDate(tvDate.getText().toString());
            work.setNumberDays(Integer.valueOf(edtDayWork.getText().toString()));
            work.setStartTime(tvStartTime.getText().toString());
            work.setEndTime(tvEndTime.getText().toString());
            work.setDescription(edtDescription.getText().toString());
            work.setGenderWorker(spGender.getSelectedItemPosition());
            work.setAgeFromWorker(Integer.valueOf(edtAgeFrom.getText().toString()));
            work.setAgeToWorker(Integer.valueOf(edtAgeTo.getText().toString()));

            Intent intent = new Intent(this, PostATaskMapActivity.class);
            intent.putExtra(Constants.EXTRA_WORK, work);
            intent.putExtra(Constants.EXTRA_CATEGORY, category);

            startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE);
        } else {
            // attach image
            attachAllFile();
        }

    }


    private void attachAllFile() {

        ProgressDialogUtils.showProgressDialog(this);

        //because images attach have icon '+' so size file = size image -1
        imageAttachCount = images.size() - 1;
        imagesArr = new Integer[images.size() - 1];

        for (int i = 0; i < images.size() - 1; i++) {
            LogUtils.d(TAG, " attachAllFile image " + i + " : " + images.get(i).getPath());
            File file = new File(images.get(i).getPath());
            attachFile(file, i);
        }

    }

    private void attachFile(final File file, final int position) {
        final MultipartRequest multipartRequest = new MultipartRequest(this, NetworkConfig.API_UPDATE_AVATA, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e(TAG, "volley onErrorResponse : " + error.toString());

                if (error.getMessage().equals(Constants.ERROR_AUTHENTICATION)) {
                    // HTTP Status Code: 401 Unauthorized
                    // Refresh token
                    NetworkUtils.RefreshToken(PostATaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish(JSONObject jsonResponse) {
                            UserManager.insertUserLogin(new DataParse().getUserEntiny(PostATaskActivity.this, jsonResponse), PostATaskActivity.this);
                            attachFile(file, position);
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(PostATaskActivity.this, PostATaskActivity.this.getString(vn.tonish.hozo.R.string.all_network_error_msg), new DialogUtils.ConfirmDialogOkCancelListener() {
                        @Override
                        public void onOkClick() {
                            attachFile(file, position);
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
                imageAttachCount--;
                imagesArr[position] = DataParse.getAvatarTempId(s);
                finishAttachImage();

            }
        }, file);
        Volley.newRequestQueue(this).add(multipartRequest);
    }

    private void finishAttachImage() {

        if (imageAttachCount == 0) {
            Work work = new Work();
            work.setName(edtWorkName.getText().toString());
            work.setDate(tvDate.getText().toString());
            work.setNumberDays(Integer.valueOf(edtDayWork.getText().toString()));
            work.setStartTime(tvStartTime.getText().toString());
            work.setEndTime(tvEndTime.getText().toString());
            work.setDescription(edtDescription.getText().toString());
            work.setGenderWorker(spGender.getSelectedItemPosition());
            work.setAgeFromWorker(Integer.valueOf(edtAgeFrom.getText().toString()));
            work.setAgeToWorker(Integer.valueOf(edtAgeTo.getText().toString()));

            JSONArray mJSONArray = new JSONArray(Arrays.asList(imagesArr));
            work.setArrImageAttack(mJSONArray.toString());

            Intent intent = new Intent(this, PostATaskMapActivity.class);
            intent.putExtra(Constants.EXTRA_WORK, work);
            intent.putExtra(Constants.EXTRA_CATEGORY, category);

            startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE);
        }

        ProgressDialogUtils.dismissProgressDialog();

    }

    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        calendar.set(year, monthOfYear, dayOfMonth);
                        tvDate.setTextColor(ContextCompat.getColor(PostATaskActivity.this, R.color.tv_black));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);

        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        LogUtils.d(TAG, "onActivityResult requestCode : " + requestCode + " , resultCode : " + resultCode);

        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {

            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            images.addAll(0, imagesSelected);
            imageAdapter.notifyDataSetChanged();

        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {

            String selectedImagePath = getImagePath();
            Image image = new Image();
            image.setAdd(false);
            image.setPath(selectedImagePath);
            images.add(0, image);
            imageAdapter.notifyDataSetChanged();
        } else if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            setResult(Constants.POST_A_TASK_RESPONSE_CODE);
            finish();
        }

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
