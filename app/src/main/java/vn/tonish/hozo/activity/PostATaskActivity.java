package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AgeDialog;
import vn.tonish.hozo.dialog.AlertDialogCancelTask;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
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
    private TextViewHozo tvTitle, tvAge;
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private final ArrayList<Image> images = new ArrayList<>();
    private String imgPath;
    private TextViewHozo tvDate;
    private final Calendar calendar = Calendar.getInstance();
    private EdittextHozo edtWorkName, edtDescription;
    private Spinner spGender;
    private Category category;
    private int imageAttachCount;
    private int[] imagesArr;
    private int ageFrom = 18;
    private int ageTo = 60;
    private EdittextHozo edtWorkingHour;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private TimePickerDialog timeEndPickerDialog;
//    private Snackbar snackbar;

    protected int getLayout() {
        return R.layout.activity_post_a_task;
    }

    @Override
    protected void initView() {

        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);

        ButtonHozo btnNext = (ButtonHozo) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        edtWorkName = (EdittextHozo) findViewById(R.id.edt_task_name);

        edtWorkingHour = (EdittextHozo) findViewById(R.id.edt_working_hour);

        edtDescription = (EdittextHozo) findViewById(R.id.edt_description);

        tvAge = (TextViewHozo) findViewById(R.id.tv_age);

        grImage = (MyGridView) findViewById(R.id.gr_image);

        spGender = (Spinner) findViewById(R.id.sp_gender);

        RelativeLayout layoutDate = (RelativeLayout) findViewById(R.id.date_layout);
        layoutDate.setOnClickListener(this);

        LinearLayout layoutAge = (LinearLayout) findViewById(R.id.layout_age);
        layoutAge.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        category = (Category) getIntent().getSerializableExtra(Constants.EXTRA_CATEGORY);
        tvTitle.setText(category.getName());

        edtWorkName.setHint(category.getSuggestTitle());
        edtDescription.setHint(category.getSuggestDescription());

        final Image image = new Image();
        image.setAdd(true);

        images.add(image);

        imageAdapter = new ImageAdapter(this, images);
        grImage.setAdapter(imageAdapter);

        grImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (images.get(position).isAdd) {
                    if (images.size() >= 7) {
                        Utils.showLongToast(PostATaskActivity.this, getString(R.string.post_a_task_max_attach_err), true, false);
                    } else {
                        checkPermission();
                    }
                } else {
                    Intent intent = new Intent(PostATaskActivity.this, PreviewImageActivity.class);
                    intent.putExtra(Constants.EXTRA_IMAGE_PATH, images.get(position).getPath());
                    startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraPermission && readExternalFile) {
                // write your logic here
                permissionGranted();
            } else {
//                snackbar = Snackbar.make(findViewById(android.R.id.content),
//                        getString(R.string.attach_image_permission_message),
//                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.attach_image_permission_confirm),
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ActivityCompat.requestPermissions(PostATaskActivity.this, permissions, Constants.PERMISSION_REQUEST_CODE);
//                            }
//                        });
//                snackbar.show();
            }
        }
    }

    private void permissionGranted() {
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
                intent.putExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA, images.size() - 1);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                doClose();
                break;

            case R.id.btn_next:
                doNext();
                break;

            case R.id.tv_address:
                Intent intent = new Intent(this, PostATaskMapActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADDRESS, TransitionScreen.RIGHT_TO_LEFT);
                break;

//            case R.id.tv_age:
//
//                break;

            case R.id.layout_age:
                AgeDialog ageDialog = new AgeDialog(PostATaskActivity.this);
                ageDialog.setAgeFrom(ageFrom);
                ageDialog.setAgeTo(ageTo);
                ageDialog.setAgeDialogListener(new AgeDialog.AgeDialogListener() {
                    @Override
                    public void onAgeDialogLister(int from, int to) {
                        tvAge.setText(getString(R.string.post_a_task_age, from, to));
                        ageFrom = from;
                        ageTo = to;
                        tvAge.setError(null);
                    }
                });
                ageDialog.showView();
                break;

//            case R.id.layout_start_time:
//                TimePickerDialog timeStartPickerDialog = new TimePickerDialog(this,
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//
//                                tvStartTime.setText(hourOfDay + ":" + minute);
//                                calendarTimeStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                                calendarTimeStart.set(Calendar.MINUTE, minute);
//                                tvStartTime.setTextColor(ContextCompat.getColor(PostATaskActivity.this, R.color.tv_black));
//                                tvStartTime.setError(null);
//                            }
//                        }, calendarTimeStart.get((Calendar.HOUR_OF_DAY)), calendarTimeStart.get(Calendar.MINUTE), false);
//                timeStartPickerDialog.show();
//                break;
//
//            case R.id.layout_end_time:
//                TimePickerDialog timeEndPickerDialog = new TimePickerDialog(this,
//                        new TimePickerDialog.OnTimeSetListener() {
//
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay,
//                                                  int minute) {
//
//                                tvEndTime.setText(hourOfDay + ":" + minute);
//                                calendarTimeEnd.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                                calendarTimeEnd.set(Calendar.MINUTE, minute);
//                                tvEndTime.setTextColor(ContextCompat.getColor(PostATaskActivity.this, R.color.tv_black));
//                                tvEndTime.setError(null);
//                            }
//                        }, calendarTimeEnd.get((Calendar.HOUR_OF_DAY)), calendarTimeEnd.get(Calendar.MINUTE), false);
//                timeEndPickerDialog.show();
//                break;

            case R.id.date_layout:
                openDatePicker();
                break;
        }
    }

    private void doClose() {

        if (edtWorkName.getText().toString().trim().equals("") && tvDate.getText().toString().trim().equals("") && edtWorkingHour.getText().toString().trim().equals("")
                && edtDescription.getText().toString().trim().equals("")) {
            finish();
            return;
        }

        final AlertDialogCancelTask alertDialogCancelTask = new AlertDialogCancelTask(PostATaskActivity.this);
        alertDialogCancelTask.setAlertConfirmDialogListener(new AlertDialogCancelTask.AlertConfirmDialogListener() {
            @Override
            public void onOk() {
                alertDialogCancelTask.hideView();
                finish();
            }

            @Override
            public void onCancel() {
                alertDialogCancelTask.hideView();
            }
        });
        alertDialogCancelTask.showView();
    }

    private void doNext() {

        if (edtWorkName.getText().toString().length() < 10) {
            edtWorkName.requestFocus();
            edtWorkName.setError(getString(R.string.post_a_task_name_error));
            return;
        } else if (tvDate.getText().toString().equals("")) {
            tvDate.requestFocus();
            tvDate.setError(getString(R.string.post_a_task_date_error));
            return;
        } else if (edtWorkingHour.getText().toString().equals("")) {
            edtWorkingHour.requestFocus();
            edtWorkingHour.setError(getString(R.string.post_a_task_time_working_error));
            return;
        } else if (edtDescription.getText().toString().length() < 25) {
            edtDescription.requestFocus();
            edtDescription.setError(getString(R.string.post_a_task_description_error));
            return;
        }

//        else if (tvAge.getText().toString().equals("")) {
//            tvAge.requestFocus();
//            tvAge.setError(getString(R.string.post_a_task_age_error));
//            return;
//        }

        if (images.size() == 1) {
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setTitle(edtWorkName.getText().toString());
            taskResponse.setStartTime(DateTimeUtils.fromCalendarIso(calendar));
            taskResponse.setEndTime(DateTimeUtils.fromCalendarIso(getEndTime()));
            taskResponse.setDescription(edtDescription.getText().toString());

            if (spGender.getSelectedItemPosition() == 1) {
                taskResponse.setGender(Constants.GENDER_MALE);
            } else if (spGender.getSelectedItemPosition() == 2) {
                taskResponse.setGender(Constants.GENDER_FEMALE);
            } else {
                taskResponse.setGender(null);
            }

            taskResponse.setMinAge(ageFrom);
            taskResponse.setMaxAge(ageTo);

            Intent intent = new Intent(this, PostATaskMapActivity.class);
            intent.putExtra(Constants.EXTRA_TASK, taskResponse);
            intent.putExtra(Constants.EXTRA_CATEGORY, category);

            LogUtils.d(TAG, "doNext , taskResponse : " + taskResponse.toString());

            startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
        } else {
            // attach image
            attachAllFile();
        }

    }

    private Calendar getEndTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(calendar.getTime());
        result.add(Calendar.HOUR_OF_DAY, Integer.valueOf(edtWorkingHour.getText().toString()));
        return result;
    }

    private void attachAllFile() {

        if (Utils.isNetworkAvailable(this)) {

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ProgressDialogUtils.showProgressDialog(PostATaskActivity.this);
                    //because images attach have icon '+' so size file = size image -1
                    imageAttachCount = images.size() - 1;
                    imagesArr = new int[images.size() - 1];
                }

                @Override
                protected Void doInBackground(Void... params) {
                    for (int i = 0; i < images.size() - 1; i++) {
                        LogUtils.d(TAG, " attachAllFile image " + i + " : " + images.get(i).getPath());
                        File file = new File(images.get(i).getPath());
                        attachFile(file, i);
                    }
                    return null;
                }
            }.execute();


        } else {
            DialogUtils.showRetryDialog(this, new AlertDialogOkAndCancel.AlertDialogListener() {
                @Override
                public void onSubmit() {
                    attachAllFile();
                }

                @Override
                public void onCancel() {

                }
            });
        }

    }

    private void attachFile(final File file, final int position) {
        File fileUp = Utils.compressFile(file);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileUp);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", fileUp.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                ImageResponse imageResponse = response.body();

                imageAttachCount--;
                if (imageResponse != null)
                    imagesArr[position] = imageResponse.getIdTemp();
                finishAttachImage();

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                imageAttachCount--;
                finishAttachImage();
            }
        });
    }

    private void finishAttachImage() {

        if (imageAttachCount == 0) {
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setTitle(edtWorkName.getText().toString());
            taskResponse.setStartTime(DateTimeUtils.fromCalendarIso(calendar));
            taskResponse.setEndTime(DateTimeUtils.fromCalendarIso(getEndTime()));
            taskResponse.setDescription(edtDescription.getText().toString());

            if (spGender.getSelectedItemPosition() == 1) {
                taskResponse.setGender(Constants.GENDER_MALE);
            } else if (spGender.getSelectedItemPosition() == 2) {
                taskResponse.setGender(Constants.GENDER_FEMALE);
            } else {
                taskResponse.setGender(null);
            }

            taskResponse.setMinAge(ageFrom);
            taskResponse.setMaxAge(ageTo);

            taskResponse.setAttachmentsId(imagesArr);
            Intent intent = new Intent(this, PostATaskMapActivity.class);
            intent.putExtra(Constants.EXTRA_TASK, taskResponse);
            intent.putExtra(Constants.EXTRA_CATEGORY, category);

            startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
            ProgressDialogUtils.dismissProgressDialog();
        }

    }


    private void openDatePicker() {

        @SuppressWarnings("deprecation") DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {

                        if (view.isShown()) {
                            //noinspection deprecation
                            timeEndPickerDialog = new TimePickerDialog(PostATaskActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                                    new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {
                                            if (view.isShown()) {
                                                LogUtils.d(TAG, "openDatePicker onTimeSet , year : " + year + " , monthOfYear : " + monthOfYear + " , dayOfMonth : " + dayOfMonth);
                                                LogUtils.d(TAG, "openDatePicker onTimeSet , hourOfDay : " + hourOfDay + " , minute : " + minute + " , dayOfMonth : " + dayOfMonth);

                                                Calendar c2 = Calendar.getInstance();

                                                if (year == c2.get(Calendar.YEAR)
                                                        && monthOfYear == c2.get(Calendar.MONTH)
                                                        && dayOfMonth == c2.get(Calendar.DAY_OF_MONTH)
                                                        && (hourOfDay < c2.get(Calendar.HOUR_OF_DAY) || (hourOfDay == c2.get(Calendar.HOUR_OF_DAY) && minute <= (c2.get(Calendar.MINUTE) + 30)))) {
                                                    Utils.showLongToast(PostATaskActivity.this, getString(R.string.post_task_time_start_error), true, false);

//                                            Handler handler = new Handler();
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    timeEndPickerDialog.show();
//                                                }
//                                            }, 100);
                                                } else {
                                                    calendar.set(year, monthOfYear, dayOfMonth);
                                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                    calendar.set(Calendar.MINUTE, minute);
                                                    String strDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " " + hourOfDay + ":" + minute;
                                                    tvDate.setText(strDate);
                                                    tvDate.setError(null);
                                                }
                                            }
                                        }
                                    }, calendar.get((Calendar.HOUR_OF_DAY)), calendar.get(Calendar.MINUTE), false);
                            timeEndPickerDialog.setTitle(getString(R.string.post_task_time_picker_title));
                            timeEndPickerDialog.show();

//                        tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                        calendar.set(year, monthOfYear, dayOfMonth);
//                        tvDate.setTextColor(ContextCompat.getColor(PostATaskActivity.this, R.color.tv_black));
//                        tvDate.setError(null);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
        datePickerDialog.setTitle(getString(R.string.post_task_date_picker_title));
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
            final String selectedImagePath = getImagePath();
            LogUtils.d(TAG, "onActivityResult selectedImagePath : " + selectedImagePath);
            ProgressDialogUtils.showProgressDialog(this);
            MediaScannerConnection.scanFile(
                    PostATaskActivity.this,
                    new String[]{selectedImagePath},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            LogUtils.d(TAG, "onActivityResult onActivityResult path : " + path + " , uri : " + uri.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Image image = new Image();
                                    image.setAdd(false);
                                    image.setPath(selectedImagePath);
                                    images.add(0, image);
                                    imageAdapter.notifyDataSetChanged();
                                    ProgressDialogUtils.dismissProgressDialog();
                                }
                            });
                        }
                    });
        } else if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            setResult(Constants.POST_A_TASK_RESPONSE_CODE);
            finish();
        }

    }

    private Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    private String getImagePath() {
        return imgPath;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }

}
