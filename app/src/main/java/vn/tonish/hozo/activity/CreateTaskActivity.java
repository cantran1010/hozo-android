package vn.tonish.hozo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CustomArrayAdapter;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AgeDialog;
import vn.tonish.hozo.dialog.AlertDialogCancelTask;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.HourDialog;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
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
import static vn.tonish.hozo.utils.Utils.formatNumber;

/**
 * Created by LongBui on 8/18/17.
 */

public class CreateTaskActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = CreateTaskActivity.class.getSimpleName();
    private ScrollView scrollView;
    private EdittextHozo edtTitle, edtDescription, edtNumberWorker;
    private TextViewHozo tvTitleMsg, tvDesMsg, tvWorkingHour;
    private static final int MAX_LENGTH_TITLE = 80;
    private static final int MAX_LENGTH_DES = 500;
    private static final int MAX_HOURS = 12;
    private double lat, lon;
    private String address;
    private TimePickerDialog timeEndPickerDialog;
    private Calendar calendar = GregorianCalendar.getInstance();
    private TextViewHozo tvDate, tvTime, tvTotalPrice, tvMoreShow, tvMoreHide;
    private AutoCompleteTextView edtBudget;
    private Category category;
    private TextViewHozo tvAddress;
    private RelativeLayout layoutAddress, workingHourLayout;
    private static final int MAX_BUGDET = 500000;
    private static final int MIN_BUGDET = 10000;
    private CustomArrayAdapter adapter;
    private static final int MAX_WORKER = 30;
    private ArrayList<String> vnds = new ArrayList<>();
    private LinearLayout layoutMore;
    private RadioGroup radioSex;
    private RadioButton radioMale, radioFemale, radioNon;
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private final ArrayList<Image> images = new ArrayList<>();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgPath;

    private TextViewHozo tvAge;
    private CheckBox cbOnline, cbAuto;
    private int imageAttachCount;
    private int[] imagesArr;
    private int ageFrom = 18;
    private int ageTo = 60;
    private boolean isAddMoreInfo = false;

    @Override
    protected int getLayout() {
        return R.layout.create_task_fragment;
    }

    @Override
    protected void initView() {
        scrollView = findViewById(R.id.scroll_view);

        ImageView imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);

        edtTitle = findViewById(R.id.edt_task_name);
        edtDescription = findViewById(R.id.edt_description);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);

        tvTitleMsg = findViewById(R.id.tv_title_msg);
        tvDesMsg = findViewById(R.id.tv_des_msg);

        layoutAddress = findViewById(R.id.layout_address);
        layoutAddress.setOnClickListener(this);

        edtBudget = findViewById(R.id.edt_budget);
        edtNumberWorker = findViewById(R.id.edt_number_worker);
        tvTotalPrice = findViewById(R.id.tv_total_price);

        tvWorkingHour = findViewById(R.id.tv_working_hour);

        RelativeLayout layoutDate = findViewById(R.id.date_layout);
        layoutDate.setOnClickListener(this);

        RelativeLayout layoutTime = findViewById(R.id.time_layout);
        layoutTime.setOnClickListener(this);

        ButtonHozo btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        tvAddress = findViewById(R.id.tv_address);

        layoutMore = findViewById(R.id.more_layout);

        grImage = findViewById(R.id.gr_image);

        tvAge = findViewById(R.id.tv_age);
        tvAge.setOnClickListener(this);

        radioSex = findViewById(R.id.radio_sex);
        radioMale = findViewById(R.id.radio_male);
        radioFemale = findViewById(R.id.radio_female);
        radioNon = findViewById(R.id.radio_non);

        cbOnline = findViewById(R.id.cb_online_task);
        cbAuto = findViewById(R.id.cb_auto_pick);

        tvMoreShow = findViewById(R.id.tv_more_show);
        tvMoreShow.setOnClickListener(this);

        tvMoreHide = findViewById(R.id.tv_more_hide);
        tvMoreHide.setOnClickListener(this);

        workingHourLayout = findViewById(R.id.working_hour_layout);
        workingHourLayout.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        calendar.add(Calendar.MINUTE, 40);
        tvDate.setText(DateTimeUtils.fromCalendarToDate(calendar));
        tvTime.setText(DateTimeUtils.fromCalendarToTime(calendar));

        category = (Category) getIntent().getSerializableExtra(Constants.EXTRA_CATEGORY);

        edtTitle.setHint(category.getSuggestTitle());
        edtDescription.setHint(category.getSuggestDescription());

        tvTitleMsg.setText(getString(R.string.post_a_task_msg_length, 0, MAX_LENGTH_TITLE));
        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > MAX_LENGTH_TITLE) {
                    edtTitle.setText(editable.subSequence(0, MAX_LENGTH_TITLE));
                    edtTitle.setSelection(MAX_LENGTH_TITLE);
                } else
                    tvTitleMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MAX_LENGTH_TITLE));
            }
        });

        tvDesMsg.setText(getString(R.string.post_a_task_msg_length, 0, MAX_LENGTH_DES));
        edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() > MAX_LENGTH_DES) {
                    edtDescription.setText(editable.subSequence(0, MAX_LENGTH_DES));
                    edtDescription.setSelection(MAX_LENGTH_DES);
                } else
                    tvDesMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MAX_LENGTH_DES));
            }
        });

        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                adapter = null;
                vnds.clear();
                if (!edtBudget.getText().toString().equals("") && Long.valueOf(getLongAutoCompleteTextView(edtBudget)) <= MAX_BUGDET)
                    edtBudget.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    edtBudget.setThreshold(edtBudget.getText().length());
                    formatMoney(Long.parseLong(edtBudget.getText().toString().trim().replace(",", "").replace(".", "")));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtBudget.getText().toString().equals(""))
                    if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) > MAX_BUGDET) {
                        edtBudget.setText(edtBudget.getText().toString().substring(0, edtBudget.length() - 1));
                        edtBudget.setError(getString(R.string.max_budget_error, Utils.formatNumber(MAX_BUGDET)));
                        edtBudget.setSelection(edtBudget.getText().toString().length());
                    } else {
                        updateTotalPayment();
                    }
            }
        });

        edtNumberWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!edtNumberWorker.getText().toString().equals("") && Integer.valueOf(edtNumberWorker.getText().toString()) <= MAX_WORKER)
                    edtNumberWorker.setError(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!edtNumberWorker.getText().toString().equals("")) {
                    if (Integer.valueOf(edtNumberWorker.getText().toString()) > MAX_WORKER) {
                        edtNumberWorker.setText(edtNumberWorker.getText().toString().substring(0, edtNumberWorker.getText().toString().length() - 1));
                        edtNumberWorker.setError(getString(R.string.max_number_worker_error, MAX_WORKER));
                        edtNumberWorker.setSelection(edtNumberWorker.getText().toString().length());
                    } else {
                        edtNumberWorker.setError(null);
                        updateTotalPayment();
                    }
                }
            }
        });

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
                        Utils.showLongToast(CreateTaskActivity.this, getString(R.string.post_a_task_max_attach_err), true, false);
                    } else {
                        checkPermission();
                    }
                } else {
                    Intent intent = new Intent(CreateTaskActivity.this, PreviewImageActivity.class);
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

    private void permissionGranted() {
        PickImageDialog pickImageDialog = new PickImageDialog(CreateTaskActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(CreateTaskActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA, images.size() - 1);
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraPermission && readExternalFile) {
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

    private Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    private String getImagePath() {
        return imgPath;
    }

    private void updateTotalPayment() {

        try {
            if (edtBudget.getText().toString().equals("") || edtNumberWorker.getText().toString().equals("")) {
                tvTotalPrice.setText("");
            } else {
                Long bBudget = Long.valueOf(getLongAutoCompleteTextView(edtBudget));
                Long bNumberWorker = Long.valueOf(getLongEdittext(edtNumberWorker));
                Long total = bBudget * bNumberWorker;
                tvTotalPrice.setText(formatNumber(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void formatMoney(long mn) {
        if (mn * 10 >= MIN_BUGDET && mn * 10 <= MAX_BUGDET && mn * 10 % 1000 == 0)
            vnds.add(String.valueOf(formatNumber(mn * 10)));
        if (mn * 100 >= MIN_BUGDET && mn * 100 <= MAX_BUGDET && mn * 100 % 1000 == 0)
            vnds.add(String.valueOf(formatNumber(mn * 100)));
        if (mn * 1000 >= MIN_BUGDET && mn * 1000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 1000)));
        if (mn * 10000 >= MIN_BUGDET && mn * 10000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 10000)));
        if (mn * 100000 >= MIN_BUGDET && mn * 100000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 100000)));
        if (mn * 1000000 >= MIN_BUGDET && mn * 1000000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 1000000)));
        if (mn * 10000000 >= MIN_BUGDET && mn * 10000000 <= MAX_BUGDET)
            vnds.add(String.valueOf(formatNumber(mn * 10000000)));

        adapter = new CustomArrayAdapter(this, vnds);
        edtBudget.setAdapter(adapter);
    }

    private void openDatePicker() {
        @SuppressWarnings("deprecation") DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {

                        if (view.isShown()) {
                            calendar.set(year, monthOfYear, dayOfMonth);
                            String strDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            tvDate.setText(strDate);
                            tvDate.setError(null);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        Calendar calendarMax = Calendar.getInstance();
        // Set calendar to 1 day next from today
        calendarMax.add(Calendar.DAY_OF_MONTH, 1);
        // Set calendar to 1 month next
        calendarMax.add(Calendar.MONTH, 1);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);
        datePickerDialog.getDatePicker().setMaxDate(calendarMax.getTimeInMillis());
        datePickerDialog.setTitle(getString(R.string.post_task_date_picker_title));
        datePickerDialog.show();
    }


    private void openTimeLayout() {
        //noinspection deprecation
        timeEndPickerDialog = new TimePickerDialog(CreateTaskActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (view.isShown()) {
                            Calendar c2 = Calendar.getInstance();
                            if (calendar.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                                    && calendar.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                                    && calendar.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
                                    && (hourOfDay < c2.get(Calendar.HOUR_OF_DAY) || (hourOfDay == c2.get(Calendar.HOUR_OF_DAY) && minute <= (c2.get(Calendar.MINUTE) + 30)))) {
                                Utils.showLongToast(CreateTaskActivity.this, getString(R.string.post_task_time_start_error), true, false);
                            } else {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                String strTime = hourOfDay + ":" + minute;
                                tvDate.setText(strTime);
                                tvDate.setError(null);
                            }
                        }
                    }
                }, calendar.get((Calendar.HOUR_OF_DAY)), calendar.get(Calendar.MINUTE), false);
        timeEndPickerDialog.setTitle(getString(R.string.post_task_time_picker_title));
        timeEndPickerDialog.show();
    }

    private void doNext() {

        if (edtTitle.getText().toString().length() < 10) {
            edtTitle.requestFocus();
            edtTitle.setError(getString(R.string.post_a_task_name_error));
            return;
        } else if (tvDate.getText().toString().equals("")) {
            tvDate.requestFocus();
            tvDate.setError(getString(R.string.post_a_task_date_error));
            return;
        } else if (DateTimeUtils.minutesBetween(Calendar.getInstance(), calendar) < 30) {
            tvDate.requestFocus();
            tvDate.setError(getString(R.string.post_task_time_start_error));
            return;
        } else if (TextUtils.isEmpty(address)) {
            tvAddress.requestFocus();
            tvAddress.setError(getString(R.string.post_task_address_error));
            return;
        } else if (edtBudget.getText().toString().equals("0") || edtBudget.getText().toString().equals("")) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.post_a_task_budget_error));
            return;
        } else if (edtNumberWorker.getText().toString().equals("0") || edtNumberWorker.getText().toString().equals("")) {
            edtNumberWorker.requestFocus();
            edtNumberWorker.setError(getString(R.string.post_a_task_number_worker_error));
            return;
        } else if (Integer.valueOf(edtNumberWorker.getText().toString()) > MAX_WORKER) {
            edtNumberWorker.setError(getString(R.string.max_number_worker_error, MAX_WORKER));
            return;
        } else if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) < MIN_BUGDET) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.min_budget_error, Utils.formatNumber(MIN_BUGDET)));
            return;
        } else if (!Utils.validateInput(this, edtTitle.getText().toString())) {
            edtTitle.requestFocus();
            edtTitle.setError(getString(R.string.post_a_task_input_error));
            return;
        } else if (!Utils.validateInput(this, edtDescription.getText().toString())) {
            edtDescription.requestFocus();
            edtDescription.setError(getString(R.string.post_a_task_input_error));
            return;
        }

        if (isAddMoreInfo && images.size() > 1) doAttachFiles();
        else createTaskOnServer();

    }

    private void doAttachFiles() {
        ProgressDialogUtils.showProgressDialog(this);

        imageAttachCount = images.size() - 1;
        imagesArr = new int[images.size() - 1];

        for (int i = 0; i < images.size() - 1; i++) {
            LogUtils.d(TAG, " attachAllFile image " + i + " : " + images.get(i).getPath());
            File file = new File(images.get(i).getPath());
            attachFile(file, i);
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
                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    ImageResponse imageResponse = response.body();
                    imageAttachCount--;
                    if (imageResponse != null)
                        imagesArr[position] = imageResponse.getIdTemp();

                    if (imageAttachCount == 0)
                        createTaskOnServer();
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(CreateTaskActivity.this);
                }

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                imageAttachCount--;
                if (imageAttachCount == 0)
                    createTaskOnServer();
            }
        });
    }

    private void createTaskOnServer() {
        ProgressDialogUtils.showProgressDialog(this);
        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("category_id", category.getId());
            jsonRequest.put("title", edtTitle.getText().toString());
            jsonRequest.put("description", edtDescription.getText().toString());
            jsonRequest.put("start_time", DateTimeUtils.fromCalendarIso(calendar));
            jsonRequest.put("end_time", DateTimeUtils.fromCalendarIso(getEndTime()));

            jsonRequest.put("latitude", lat);
            jsonRequest.put("longitude", lon);

            String[] arrAddress = address.split(",");

            jsonRequest.put("city", arrAddress.length >= 2 ? arrAddress[arrAddress.length - 2].trim() : arrAddress[arrAddress.length - 1].trim());
            jsonRequest.put("district", arrAddress.length >= 3 ? arrAddress[arrAddress.length - 3].trim() : arrAddress[arrAddress.length - 1].trim());

            jsonRequest.put("address", address);

            jsonRequest.put("worker_rate", Integer.valueOf(getLongAutoCompleteTextView(edtBudget)));
            jsonRequest.put("worker_count", Integer.valueOf(getLongEdittext(edtNumberWorker)));

            if (isAddMoreInfo) {

                if (imagesArr.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < imagesArr.length; i++)
                        jsonArray.put(imagesArr[i]);
                    jsonRequest.put("attachments", jsonArray);
                }

                jsonRequest.put("min_age", ageFrom);
                jsonRequest.put("max_age", ageTo);

                if (radioMale.isChecked()) {
                    jsonRequest.put("gender", Constants.GENDER_MALE);
                } else if (radioFemale.isChecked()) {
                    jsonRequest.put("gender", Constants.GENDER_FEMALE);
                }

                jsonRequest.put("isOnline", cbOnline.isChecked());
                jsonRequest.put("isAuto", cbAuto.isChecked());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "createNewTask data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "request body create task : " + body.toString());

        ApiClient.getApiService().createNewTask(UserManager.getUserToken(), body).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                LogUtils.d(TAG, "createNewTask onResponse : " + response.body());
                LogUtils.d(TAG, "createNewTask code : " + response.code());

                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    Utils.showLongToast(CreateTaskActivity.this, getString(R.string.post_a_task_complete), false, false);
                    setResult(Constants.POST_A_TASK_RESPONSE_CODE);
                    finish();
                    FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(CreateTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doNext();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(CreateTaskActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.e(TAG, "createNewTask errorBody : " + error.toString());
                    DialogUtils.showOkDialog(CreateTaskActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                        @Override
                        public void onSubmit() {

                        }
                    });

                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                LogUtils.e(TAG, "createNewTask onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(CreateTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doNext();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private Calendar getEndTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(calendar.getTime());
        result.add(Calendar.HOUR_OF_DAY, Integer.valueOf(tvWorkingHour.getText().toString()));
        return result;
    }

    private String getLongEdittext(EdittextHozo edittextHozo) {
        return edittextHozo.getText().toString().replace(",", "").replace(".", "");
    }

    private String getLongAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().replace(",", "").replace(".", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_ADDRESS && resultCode == Constants.RESULT_CODE_ADDRESS) {

            lat = data.getDoubleExtra(Constants.LAT_EXTRA, 0);
            lon = data.getDoubleExtra(Constants.LON_EXTRA, 0);
            address = data.getStringExtra(Constants.EXTRA_ADDRESS);

            tvAddress.setText(address);
            tvAddress.setError(null);
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            images.addAll(0, imagesSelected);
            imageAdapter.notifyDataSetChanged();
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            final String selectedImagePath = getImagePath();
            LogUtils.d(TAG, "onActivityResult selectedImagePath : " + selectedImagePath);
            Image image = new Image();
            image.setAdd(false);
            image.setPath(selectedImagePath);
            images.add(0, image);
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void doClose() {
        if (edtTitle.getText().toString().trim().equals("") && tvDate.getText().toString().trim().equals("")
                && edtDescription.getText().toString().trim().equals("")) {
            finish();
            return;
        }

        final AlertDialogCancelTask alertDialogCancelTask = new AlertDialogCancelTask(CreateTaskActivity.this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.date_layout:
                openDatePicker();
                break;

            case R.id.time_layout:
                openTimeLayout();
                break;

            case R.id.btn_next:
                doNext();
                break;

            case R.id.layout_address:
                Intent intent = new Intent(this, PlaceActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_ADDRESS, TransitionScreen.FADE_IN);
                break;

            case R.id.img_close:
                doClose();
                break;

            case R.id.tv_age:
                AgeDialog ageDialog = new AgeDialog(CreateTaskActivity.this);
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

            case R.id.tv_more_show:
                isAddMoreInfo = true;
                tvMoreShow.setVisibility(View.GONE);
                layoutMore.setVisibility(View.VISIBLE);
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 200);
                break;

            case R.id.tv_more_hide:
                isAddMoreInfo = false;
                tvMoreShow.setVisibility(View.VISIBLE);
                layoutMore.setVisibility(View.GONE);
                break;

            case R.id.working_hour_layout:
                HourDialog hourDialog = new HourDialog(CreateTaskActivity.this);
                hourDialog.setHour(Integer.valueOf(tvWorkingHour.getText().toString()));
                hourDialog.setHourDialogListener(new HourDialog.HourDialogListener() {
                    @Override
                    public void onHourDialogLister(int hour) {
                        tvWorkingHour.setText(String.valueOf(hour));
                    }
                });
                hourDialog.showView();
                break;

        }
    }

}
