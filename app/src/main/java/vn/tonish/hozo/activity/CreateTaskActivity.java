package vn.tonish.hozo.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.adapter.CustomArrayAdapter;
import vn.tonish.hozo.adapter.HozoSpinnerAdapter;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.entity.PostTaskEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.PostTaskManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AgeDialog;
import vn.tonish.hozo.dialog.AlertDialogCancelTask;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.HozoNumberDialog;
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
import vn.tonish.hozo.view.ExpandableLayout;
import vn.tonish.hozo.view.MyGridView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.string.post_task_map_get_location_error_next;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;
import static vn.tonish.hozo.utils.Utils.formatNumber;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;

/**
 * Created by LongBui on 8/18/17.
 */

public class CreateTaskActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = CreateTaskActivity.class.getSimpleName();
    private ScrollView scrollView;
    private EdittextHozo edtTitle, edtDescription, edtCoupon;
    private TextViewHozo tvTitleMsg, tvDesMsg, tvWorkingHour, tvNumberWorker;
    private static final int MAX_LENGTH_TITLE = 50;
    private static final int MAX_LENGTH_DES = 500;
    private static final int MIN_LENGTH_TITLE = 10;
    private double lat, lon;
    private String address = "";
    private TimePickerDialog timeEndPickerDialog;
    private Calendar calendar = GregorianCalendar.getInstance();
    private TextViewHozo tvTitle, tvDate, tvTime, tvTotalPrice, tvMoreShow, tvMoreHide;
    private AutoCompleteTextView edtBudget;
    private Category category;
    private static final int MAX_BUGDET = 500000;
    private static final int MIN_BUGDET = 10000;
    private static final int MAX_WORKER = 30;
    private static final int MIN_WORKER = 1;
    private static final int MIN_HOUR = 1;
    private static final int MAX_HOUR = 12;
    private CustomArrayAdapter adapter;
    private ArrayList<String> vnds = new ArrayList<>();
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private final ArrayList<Image> images = new ArrayList<>();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imgPath;
    private TextViewHozo tvAge;
    private AppCompatCheckBox cbOnline, cbAuto;
    private int imageAttachCount;
    private int[] imagesArr;
    private int ageFrom = 18;
    private int ageTo = 60;
    private TaskResponse taskResponse;
    private boolean isCopy = false;
    private int countImageCopy;
    private int taskId;
    private ButtonHozo btnNext;
    private String status;
    private ImageView imgMenu, imgSaveDraf;
    private PopupMenu popup;
    private Spinner spGender;
    private String strGender = "";
    private ExpandableLayout advanceExpandableLayout;


    // copy or edit
    private String taskType = "";

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;

    @Override
    protected int getLayout() {
        return R.layout.activity_create_task;
    }

    @Override
    protected void initView() {
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);

        edtTitle = (EdittextHozo) findViewById(R.id.edt_task_name);
        edtDescription = (EdittextHozo) findViewById(R.id.edt_description);
        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);

        tvTitleMsg = (TextViewHozo) findViewById(R.id.tv_title_msg);
        tvDesMsg = (TextViewHozo) findViewById(R.id.tv_des_msg);

        edtBudget = (AutoCompleteTextView) findViewById(R.id.edt_budget);
        edtCoupon = (EdittextHozo) findViewById(R.id.edt_coupon);
        tvNumberWorker = (TextViewHozo) findViewById(R.id.tv_number_worker);
        tvTotalPrice = (TextViewHozo) findViewById(R.id.tv_total_price);
        imgSaveDraf = (ImageView) findViewById(R.id.tv_save);

        tvWorkingHour = (TextViewHozo) findViewById(R.id.tv_working_hour);
        spGender = (Spinner) findViewById(R.id.sp_gender);
        advanceExpandableLayout = (ExpandableLayout) findViewById(R.id.advance_expandable_layout);


        btnNext = (ButtonHozo) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        grImage = (MyGridView) findViewById(R.id.gr_image);

        tvAge = (TextViewHozo) findViewById(R.id.tv_age);
        tvAge.setOnClickListener(this);
        cbOnline = (AppCompatCheckBox) findViewById(R.id.cb_online_task);
        cbAuto = (AppCompatCheckBox) findViewById(R.id.cb_auto_pick);

        tvMoreShow = (TextViewHozo) findViewById(R.id.tv_more_show);
        tvMoreShow.setOnClickListener(this);

        tvMoreHide = (TextViewHozo) findViewById(R.id.tv_more_hide);
        tvMoreHide.setOnClickListener(this);

        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(this);
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        tvWorkingHour.setOnClickListener(this);
        tvNumberWorker.setOnClickListener(this);
        imgSaveDraf.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        Constants.MAX_IMAGE_ATTACH = 6;

        edtCoupon.setFocusable(true);
        edtCoupon.setText("");
        edtCoupon.setFocusable(true);
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        autocompleteView.setThreshold(1);

        // Register a listener that receives callbacks when a suggestion has been selected
        autocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        final AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("VN")
                .build();

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, null,
                autocompleteFilter);
        autocompleteView.setAdapter(placeAutocompleteAdapter);

        Intent intent = getIntent();

        final Image image = new Image();
        image.setAdd(true);
        images.add(image);

        imageAdapter = new ImageAdapter(this, images);
        grImage.setAdapter(imageAdapter);

        getDefaultAddress();

        if (intent.hasExtra(Constants.EXTRA_TASK)) {

            imgSaveDraf.setVisibility(View.GONE);
            imgMenu.setVisibility(View.VISIBLE);
            showPopup();
            taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
            LogUtils.d(TAG, "PostATaskActivity , taskResponse : " + taskResponse.toString());
            taskId = taskResponse.getId();

            if (intent.hasExtra(Constants.TASK_EDIT_EXTRA)) {
                edtCoupon.setFocusable(false);
                taskType = intent.getStringExtra(Constants.TASK_EDIT_EXTRA);

                if (taskType.equals(Constants.TASK_EDIT)) {
                    edtCoupon.setFocusable(false);
                    imgMenu.setVisibility(View.GONE);
                    btnNext.setText(getString(R.string.btn_edit_task));

                    popup.getMenu().findItem(R.id.delete_task).setVisible(false);
                    popup.getMenu().findItem(R.id.save_task).setVisible(true);
                } else if (taskType.equals(Constants.TASK_COPY)) {
                    popup.getMenu().findItem(R.id.delete_task).setVisible(false);
                    popup.getMenu().findItem(R.id.save_task).setVisible(true);
                } else if (taskType.equals(Constants.TASK_DRAFT)) {
                    popup.getMenu().findItem(R.id.delete_task).setVisible(true);
                    popup.getMenu().findItem(R.id.save_task).setVisible(true);
                }

            }

            category = DataParse.convertCatogoryEntityToCategory(CategoryManager.getCategoryById(taskResponse.getCategoryId()));

            edtTitle.setText(taskResponse.getTitle());
            tvTitleMsg.setText(getString(R.string.post_a_task_msg_length, edtTitle.getText().toString().length(), MAX_LENGTH_TITLE));

            try {

                if (DateTimeUtils.minutesBetween(Calendar.getInstance(), DateTimeUtils.toCalendar(taskResponse.getStartTime())) < 30)
                    calendar.add(Calendar.MINUTE, 40);
                else
                    calendar = DateTimeUtils.toCalendar(taskResponse.getStartTime());

//                tvDate.setText(DateTimeUtils.fromCalendarIsoCreateTask(calendar));
                tvWorkingHour.setText(String.valueOf(DateTimeUtils.hoursBetween(DateTimeUtils.toCalendar(taskResponse.getStartTime()), DateTimeUtils.toCalendar(taskResponse.getEndTime()))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            lat = taskResponse.getLatitude();
            lon = taskResponse.getLongitude();

            address = taskResponse.getAddress();

            autocompleteView.setText(address);
            if (taskResponse.getWorkerRate() > 0)
                edtBudget.setText(Utils.formatNumber(taskResponse.getWorkerRate()));
            tvNumberWorker.setText(String.valueOf(taskResponse.getWorkerCount()));
            updateTotalPayment();
            edtDescription.setText(taskResponse.getDescription());
            tvDesMsg.setText(getString(R.string.post_a_task_msg_length, edtDescription.getText().toString().length(), MAX_LENGTH_DES));

            if (taskResponse.getMinAge() != 0)
                ageFrom = taskResponse.getMinAge();

            if (taskResponse.getMaxAge() != 0)
                ageTo = taskResponse.getMaxAge();

            tvAge.setText(getString(R.string.post_a_task_age, ageFrom, ageTo));
            if (taskResponse.getGender() != null)
                strGender = taskResponse.getGender();

            cbOnline.setChecked(taskResponse.isOnline());
            cbAuto.setChecked(taskResponse.isAutoAssign());

            if (taskResponse.isAdvance()) {
                tvMoreShow.setVisibility(View.GONE);
                if (!advanceExpandableLayout.isExpanded()) {
                    advanceExpandableLayout.toggle();
                }
            } else {
                tvMoreShow.setVisibility(View.VISIBLE);
                if (advanceExpandableLayout.isExpanded()) {
                    advanceExpandableLayout.toggle();
                }
            }

            if (taskResponse.getAttachments() != null && taskResponse.getAttachments().size() > 0) {
                isCopy = true;
                checkPermission();
            }

        } else {
            imgSaveDraf.setVisibility(View.VISIBLE);
            imgMenu.setVisibility(View.GONE);
            tvTitleMsg.setTextColor(ContextCompat.getColor(CreateTaskActivity.this, R.color.color_create_task_lable));
            tvTitleMsg.setText(getString(R.string.post_a_task_msg_length, 0, MAX_LENGTH_TITLE));
            tvDesMsg.setText(getString(R.string.post_a_task_msg_length, 0, MAX_LENGTH_DES));

            category = (Category) intent.getSerializableExtra(Constants.EXTRA_CATEGORY);
            calendar.add(Calendar.MINUTE, 40);
        }

        tvTitle.setText(category.getName());

        tvDate.setText(DateTimeUtils.fromCalendarToDate(calendar));
        tvTime.setText(DateTimeUtils.fromCalendarToTime(calendar));

        edtTitle.setHint(category.getSuggestTitle());
        edtDescription.setHint(category.getSuggestDescription());

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
                } else if (editable.toString().length() < MIN_LENGTH_TITLE) {
                    tvTitleMsg.setTextColor(ContextCompat.getColor(CreateTaskActivity.this, R.color.color_count_word));
                    tvTitleMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MIN_LENGTH_TITLE));
                } else {
                    tvTitleMsg.setTextColor(ContextCompat.getColor(CreateTaskActivity.this, R.color.color_create_task_lable));
                    tvTitleMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MAX_LENGTH_TITLE));
                }
            }
        });

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
                    hideSoftKeyboard(CreateTaskActivity.this, edtDescription);

                } else
                    tvDesMsg.setText(getString(R.string.post_a_task_msg_length, editable.toString().length(), MAX_LENGTH_DES));
            }
        });
        edtCoupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = edtCoupon.getText().toString().trim();
                if (charSequence.length() != 0) {
                    if (s.length() == 6 && !s.contains(" ") && !s.contains(".") && !s.contains(",") && !s.contains("-") && !s.contains("_")) {
                        hideSoftKeyboard(CreateTaskActivity.this, edtCoupon);
                    } else if (s.length() >= 6) {
                        edtCoupon.setError(getString(R.string.code_coupon_error));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        edtBudget.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);

            }

        });

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
        createGenderSpinner();
    }


    private void createGenderSpinner() {
        List<String> state = new ArrayList<>();
        state.add(getString(R.string.gender_vn_any));
        state.add(getString(R.string.gender_vn_male));
        state.add(getString(R.string.gender_vn_mafele));
        HozoSpinnerAdapter dataAdapter = new HozoSpinnerAdapter(this, state);
        spGender.setAdapter(dataAdapter);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        strGender = "";
                        break;
                    case 1:
                        strGender = Constants.GENDER_MALE;
                        ;
                        break;
                    case 2:
                        strGender = Constants.GENDER_FEMALE;
                        ;
                        break;
                    default:
                        strGender = "";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getDefaultAddress() {
        PostTaskEntity postTaskEntity = PostTaskManager.getPostTaskEntity();
        if (postTaskEntity != null) {
            lat = postTaskEntity.getLon();
            lon = postTaskEntity.getLat();
            address = postTaskEntity.getAddress();
            LogUtils.d(TAG, "getDefaultAddress" + postTaskEntity.toString());
            autocompleteView.setText(address);
        }
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

        if (isCopy) {
            isCopy = false;
            countImageCopy = taskResponse.getAttachments().size();
            ProgressDialogUtils.showProgressDialog(this);
            for (int i = 0; i < taskResponse.getAttachments().size(); i++) {
                Glide.with(this)
                        .load(taskResponse.getAttachments().get(i))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                LogUtils.d(TAG, "onResourceReady complete , resource , width : " + resource.getWidth() + " , height : " + resource.getHeight());

                                // sometime glide recycled bitmap
                                resource = resource.copy(resource.getConfig(), true); // safe copy
                                Glide.clear(this); // added to release original bitmap

                                File fileSave = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
                                Utils.compressBitmapToFile(resource, fileSave.getPath());
                                LogUtils.d(TAG, "onResourceReady complete , path : " + fileSave.getPath());

                                Image imageCopy = new Image();
                                imageCopy.setAdd(false);
                                imageCopy.setPath(fileSave.getPath());
                                images.add(0, imageCopy);
                                imageAdapter.notifyDataSetChanged();

                                countImageCopy--;
                                if (countImageCopy == 0)
                                    ProgressDialogUtils.dismissProgressDialog();
                            }
                        });
            }
        } else {
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
            if (edtBudget.getText().toString().equals("")) {
                tvTotalPrice.setText("0");
            } else {
                Long bBudget = Long.valueOf(getLongAutoCompleteTextView(edtBudget));
                Long bNumberWorker = Long.valueOf(tvNumberWorker.getText().toString());
                Long total = bBudget * bNumberWorker;
                tvTotalPrice.setText(formatNumber(total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void formatMoney(long mn) {
        if (mn * 10 >= MIN_BUGDET && mn * 10 <= MAX_BUGDET && mn * 10 % 1000 == 0)
            vnds.add(formatNumber(mn * 10));
        if (mn * 100 >= MIN_BUGDET && mn * 100 <= MAX_BUGDET && mn * 100 % 1000 == 0)
            vnds.add(formatNumber(mn * 100));
        if (mn * 1000 >= MIN_BUGDET && mn * 1000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 1000));
        if (mn * 10000 >= MIN_BUGDET && mn * 10000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 10000));
        if (mn * 100000 >= MIN_BUGDET && mn * 100000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 100000));
        if (mn * 1000000 >= MIN_BUGDET && mn * 1000000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 1000000));
        if (mn * 10000000 >= MIN_BUGDET && mn * 10000000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 10000000));

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
//                                String strTime = hourOfDay + ":" + minute;
//                                tvTime.setText(strTime);
                                tvTime.setText(DateTimeUtils.fromCalendarToTime(calendar));
                                tvTime.setError(null);
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
            tvTime.requestFocus();
            tvTime.setError(getString(R.string.post_task_time_start_error));
            return;
        } else if (!autocompleteView.getText().toString().trim().equals(address.trim()) || (address.equals("") && !autocompleteView.getText().toString().trim().equals(""))) {
            autocompleteView.requestFocus();
            autocompleteView.setError(getString(R.string.post_task_address_error_google));

            address = "";
            lat = 0;
            lon = 0;
            autocompleteView.setText("");
            return;
        } else if (TextUtils.isEmpty(address)) {
            autocompleteView.requestFocus();
            autocompleteView.setError(getString(R.string.post_task_address_error));
            return;
        } else if (edtBudget.getText().toString().equals("0") || edtBudget.getText().toString().equals("")) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.post_a_task_budget_error));
            return;
        } else if (Long.valueOf(getLongAutoCompleteTextView(edtBudget)) > MAX_BUGDET) {
            edtBudget.requestFocus();
            edtBudget.setError(getString(R.string.max_budget_error, Utils.formatNumber(MAX_BUGDET)));
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
        } else if (ageFrom >= ageTo) {
            Utils.showLongToast(this, getString(R.string.select_age_error), true, false);
            return;
        } else if (!isCoupon(edtCoupon.getText().toString().trim())) {
            if (!advanceExpandableLayout.isExpanded()) showAdvance();
            edtCoupon.requestFocus();
            edtCoupon.setError(getString(R.string.code_coupon_error));
            return;
        }

        if (images.size() > 1) doAttachFiles();
        else createTaskOnServer();

    }

    private boolean isCoupon(String s) {
        if (s.replace(" ", "").replace(".", "").replace(",", "").length() == 6)
            return true;
        else
            return false;
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
            jsonRequest.put("title", edtTitle.getText().toString().trim());
            jsonRequest.put("description", edtDescription.getText().toString().trim());
            jsonRequest.put("start_time", DateTimeUtils.fromCalendarIso(calendar));
            jsonRequest.put("end_time", DateTimeUtils.fromCalendarIso(getEndTime()));

            jsonRequest.put("latitude", lat);
            jsonRequest.put("longitude", lon);

            if (address != null && lat != 0 && lon != 0) {
                String[] arrAddress = address.split(",");
                jsonRequest.put("city", arrAddress.length >= 2 ? arrAddress[arrAddress.length - 2].trim() : arrAddress[arrAddress.length - 1].trim());
                jsonRequest.put("district", arrAddress.length >= 3 ? arrAddress[arrAddress.length - 3].trim() : arrAddress[arrAddress.length - 1].trim());
                jsonRequest.put("address", address);
                PostTaskEntity postTaskEntity = new PostTaskEntity();
                postTaskEntity.setId(0);
                postTaskEntity.setLat(lat);
                postTaskEntity.setLon(lon);
                postTaskEntity.setAddress(address);
                PostTaskManager.insertPostTask(postTaskEntity);
            }

            if (!getLongAutoCompleteTextView(edtBudget).trim().equals(""))
                jsonRequest.put("worker_rate", Integer.valueOf(getLongAutoCompleteTextView(edtBudget)));
            jsonRequest.put("worker_count", Integer.valueOf(tvNumberWorker.getText().toString()));
            jsonRequest.put("status", status);
//            jsonRequest.put("advance", layoutMore.getVisibility() == View.VISIBLE);
            jsonRequest.put("advance", getAdvanceSetting());

            if (imagesArr != null && imagesArr.length > 0) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < imagesArr.length; i++)
                    jsonArray.put(imagesArr[i]);
                jsonRequest.put("attachments", jsonArray);
            }

            jsonRequest.put("min_age", ageFrom);
            jsonRequest.put("max_age", ageTo);
            jsonRequest.put("gender", strGender);
            jsonRequest.put("online", cbOnline.isChecked());
            jsonRequest.put("auto_assign", cbAuto.isChecked());
            if (!edtCoupon.getText().toString().trim().isEmpty())
                jsonRequest.put("referrer", edtCoupon.getText().toString().trim());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "createNewTask data request : " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        LogUtils.d(TAG, "request body create task : " + body.toString());

        if (taskId != 0 && !taskType.equals(Constants.TASK_COPY)) {
            LogUtils.d(TAG, "createNewTask edit task -------> : " + taskId);
            ApiClient.getApiService().editTask(UserManager.getUserToken(), taskId, body).enqueue(new Callback<TaskResponse>() {
                @Override
                public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                    LogUtils.d(TAG, "createNewTask onResponse : " + response.body());
                    LogUtils.d(TAG, "createNewTask code : " + response.code());
                    APIError error = ErrorUtils.parseError(response);
                    if (response.code() == Constants.HTTP_CODE_CREATED) {
                        if (status.equals(Constants.CREATE_TASK_STATUS_DRAFT))
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.draft_a_task_complete), false, false);
                        else
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
                        if (error.status().equals(Constants.POST_TASK_DUPLICATE)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.duplicate_task_error), true, false);
                        } else if (error.status().equals(Constants.INVALID_CATEGORY)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_category), true, false);
                        } else if (error.status().equals(Constants.INVALID_TITLE)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_title), true, false);
                        } else if (error.status().equals(Constants.INVALID_DESCRIPTION)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_description), true, false);
                        } else if (error.status().equals(Constants.INVALID_TIME)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_time), true, false);
                        } else if (error.status().equals(Constants.INVALID_ADRESS)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_address), true, false);
                        } else if (error.status().equals(Constants.INVALID_WORKER_RATE)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_worker_rate), true, false);
                        } else if (error.status().equals(Constants.INVALID_WORKER_COUNT)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_worker_count), true, false);
                        } else if (error.status().equals(Constants.INVALID_GENDER)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_gender), true, false);
                        } else if (error.status().equals(Constants.INVALID_AGE)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_age_between), true, false);
                        } else if (error.status().equals(Constants.INVALID_UPDATE_TASK_FAILED)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.update_task_failed), true, false);
                        } else if (error.status().equals(Constants.NO_PERMISSION)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.no_permission), true, false);
                        } else if (error.status().equals(Constants.NO_TASK)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.no_task), true, false);
                        } else if (error.status().equals(Constants.EMPTY_PARAMETERS)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.empty_parameters), true, false);
                        } else if (error.status().equals(Constants.UPDATE_NOT_ALLOWED)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.update_not_allowed), true, false);
                        } else {

                            DialogUtils.showOkDialog(CreateTaskActivity.this, getString(R.string.invalid_error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });


                        }
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
        } else {
            ApiClient.getApiService().createNewTask(UserManager.getUserToken(), body).enqueue(new Callback<TaskResponse>() {
                @Override
                public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                    LogUtils.d(TAG, "createNewTask onResponse : " + response.body());
                    LogUtils.d(TAG, "createNewTask code : " + response.code());
                    APIError error = ErrorUtils.parseError(response);
                    if (response.code() == Constants.HTTP_CODE_CREATED) {
                        if (status.equals(Constants.CREATE_TASK_STATUS_DRAFT))
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.draft_a_task_complete), false, false);
                        else
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
                        if (error.status().equals(Constants.POST_TASK_DUPLICATE)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.duplicate_task_error), true, false);
                        } else if (error.status().equals(Constants.INVALID_TITLE)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_title), true, false);
                        } else if (error.status().equals(Constants.INVALID_TIME)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_time), true, false);
                        } else if (error.status().equals(Constants.INVALID_ADRESS)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_address), true, false);
                        } else if (error.status().equals(Constants.INVALID_DATA)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_data), true, false);
                        } else if (error.status().equals(Constants.NO_REFERRER)) {
                            edtCoupon.requestFocus();
                            if (!advanceExpandableLayout.isExpanded()) showAdvance();
                            edtCoupon.setError(getString(R.string.code_coupon_error));
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.no_referrer), true, false);
                        } else if (error.status().equals(Constants.REFERRER_USED)) {
                            edtCoupon.requestFocus();
                            if (!advanceExpandableLayout.isExpanded()) showAdvance();
                            edtCoupon.setError(getString(R.string.code_coupon_error));
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.referrer_used), true, false);
                        } else if (error.status().equals(Constants.INVALID_AGE)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.invalid_age_between), true, false);
                        } else if (error.status().equals(Constants.INVALID_UPDATE_TASK_FAILED)) {
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.update_task_failed), true, false);

                        } else if (error.status().equals(Constants.INVALID_UPDATE_TASK_FAILED))
                            Utils.showLongToast(CreateTaskActivity.this, getString(R.string.post_task_failed), true, false);
                        else {
                            LogUtils.e(TAG, "createNewTask errorBody : " + error.toString());
                            DialogUtils.showOkDialog(CreateTaskActivity.this, getString(R.string.error), error.message(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                                @Override
                                public void onSubmit() {

                                }
                            });

                        }
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
    }

    private Calendar getEndTime() {
        Calendar result = Calendar.getInstance();
        result.setTime(calendar.getTime());
        result.add(Calendar.HOUR_OF_DAY, Integer.valueOf(tvWorkingHour.getText().toString()));
        return result;
    }

    private String getLongAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        return autoCompleteTextView.getText().toString().replace(",", "").replace(".", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE_ADDRESS && resultCode == Constants.RESULT_CODE_ADDRESS) {

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
        } else if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            setResult(Constants.POST_A_TASK_RESPONSE_CODE);
            finish();
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

    private void doSave() {
        if (images.size() > 1) doAttachFiles();
        else createTaskOnServer();
    }

    private void showPopup() {
        //Creating the instance of PopupMenu
        popup = new PopupMenu(this, imgMenu);
        popup.getMenuInflater().inflate(R.menu.menu_create_task, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.save_task:
                        status = Constants.CREATE_TASK_STATUS_DRAFT;
                        doSave();
                        break;

                    case R.id.delete_task:
                        DialogUtils.showOkAndCancelDialog(
                                CreateTaskActivity.this, getString(R.string.title_delete_task), getString(R.string.content_detete_task), getString(R.string.cancel_task_ok),
                                getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                    @Override
                                    public void onSubmit() {
                                        doDeleteTask();
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                        break;
                }
                return true;
            }
        });
    }

    private void doDeleteTask() {
        ProgressDialogUtils.showProgressDialog(this);
        ApiClient.getApiService().deleteTask(UserManager.getUserToken(), taskId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                LogUtils.d(TAG, "doDeleteTask , code : " + response.code());
                LogUtils.d(TAG, "doDeleteTask , body : " + response.body());

                if (response.code() == Constants.HTTP_CODE_NO_CONTENT) {
                    Utils.showLongToast(CreateTaskActivity.this, getString(R.string.delete_task_success_msg), false, false);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.EXTRA_TASK, taskResponse);
                    setResult(Constants.RESULT_CODE_TASK_DELETE, intent);
                    finish();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(CreateTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doDeleteTask();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(CreateTaskActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "errorBody" + error.toString());
                    Utils.showLongToast(CreateTaskActivity.this, error.message(), false, true);
                }
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                DialogUtils.showRetryDialog(CreateTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doDeleteTask();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private boolean getAdvanceSetting() {
        if (imagesArr != null && imagesArr.length > 0) return true;
        if (ageFrom != 18 || ageTo != 60) return true;
//        if (!radioNon.isChecked()) return true;
        if (cbAuto.isChecked()) return true;
        if (cbOnline.isChecked()) return true;
        return false;
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private final AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            LogUtils.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            LogUtils.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private final ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                LogUtils.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            try {

                // Get the Place object from the buffer.
                final Place place = places.get(0);

                LogUtils.e(TAG, "Place address : " + place.getAddress());

                lat = place.getLatLng().latitude;
                lon = place.getLatLng().longitude;
                address = autocompleteView.getText().toString();
                autocompleteView.setError(null);

                places.release();

                edtBudget.requestFocus();

                Utils.hideKeyBoard(CreateTaskActivity.this);

            } catch (Exception e) {
                Utils.showLongToast(CreateTaskActivity.this, getString(post_task_map_get_location_error_next), true, false);
            }
        }
    };


    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        LogUtils.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

//        // TODO(Developer): Check error code and notify the user of error state and resolution.
//        Toast.makeText(this,
//                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
//                Toast.LENGTH_SHORT).show();

        Utils.showLongToast(this, getString(R.string.gg_api_error), true, false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_date:
                openDatePicker();
                break;

            case R.id.tv_time:
                openTimeLayout();
                break;
            case R.id.tv_save:
                status = Constants.CREATE_TASK_STATUS_DRAFT;
                doSave();
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
                showAdvance();
                break;

            case R.id.tv_more_hide:
                advanceExpandableLayout.toggle();
                tvMoreShow.setVisibility(View.VISIBLE);
                break;

            case R.id.tv_working_hour:
                HozoNumberDialog hozoHourDialog = new HozoNumberDialog(CreateTaskActivity.this, getString(R.string.hour_title), MIN_HOUR, MAX_HOUR);
                hozoHourDialog.setValue(Integer.valueOf(tvWorkingHour.getText().toString()));
                hozoHourDialog.setHourDialogListener(new HozoNumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberDialogLister(int number) {
                        tvWorkingHour.setText(String.valueOf(number));
                    }
//
                });
                hozoHourDialog.showView();
                break;

            case R.id.tv_number_worker:
                HozoNumberDialog hozoNumberDialog = new HozoNumberDialog(CreateTaskActivity.this, getString(R.string.number_title), MIN_WORKER, MAX_WORKER);
                hozoNumberDialog.setValue(Integer.valueOf(tvNumberWorker.getText().toString()));
                hozoNumberDialog.setHourDialogListener(new HozoNumberDialog.NumberDialogListener() {
                    @Override
                    public void onNumberDialogLister(int number) {
                        tvNumberWorker.setText(String.valueOf(number));
                        updateTotalPayment();
                    }
//
                });
                hozoNumberDialog.showView();
                break;

            case R.id.btn_next:
                status = Constants.CREATE_TASK_STATUS_PUBLISH;
                doNext();
                break;

//            case R.id.tv_save:
//                status = Constants.CREATE_TASK_STATUS_DRAFT;
//                doSave();
//                break;

            case R.id.img_menu:
                popup.show();//showing popup menu
                break;

        }
    }

    private void showAdvance() {
        tvMoreShow.setVisibility(View.GONE);
        advanceExpandableLayout.toggle();
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getBottom(), edtDescription.getBottom()).setDuration(1000);
        objectAnimator.start();
    }

}