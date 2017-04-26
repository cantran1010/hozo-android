package vn.tonish.hozo.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ImageAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.model.Work;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.MyGridView;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;

/**
 * Created by MAC2015 on 4/12/17.
 */

public class PostATaskActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PostATaskActivity.class.getSimpleName();
    private TextView tvCancel, tvTitle;
    protected Button btnNext;
    protected RelativeLayout layoutStartTime, layoutEndTime, layoutDate;
    private MyGridView grImage;
    private ImageAdapter imageAdapter;
    private ArrayList<Image> images = new ArrayList<>();
    private String imgPath;
    private TextView tvStartTime, tvEndTime, tvDate;
    private Date dateWork;
    private Calendar calendar = Calendar.getInstance();
    private Calendar calendarTimeStart = Calendar.getInstance();
    private Calendar calendarTimeEnd = Calendar.getInstance();
    private EditText edtDayWork, edtWorkName, edtDescription, edtAgeFrom, edtAgeTo;
    private Spinner spGender;

    @Override
    protected int getLayout() {
        return R.layout.activity_post_a_task;
    }

    @Override
    protected void initView() {

        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        tvDate = (TextView) findViewById(R.id.tv_date);
        edtWorkName = (EditText) findViewById(R.id.edt_task_name);

        edtDayWork = (EditText) findViewById(R.id.edt_number_day);

        edtDescription = (EditText) findViewById(R.id.edt_description);
        edtAgeFrom = (EditText) findViewById(R.id.edt_age_from);
        edtAgeTo = (EditText) findViewById(R.id.edt_age_to);

        grImage = (MyGridView) findViewById(R.id.gr_image);

        spGender = (Spinner) findViewById(R.id.sp_gender);

        layoutStartTime = (RelativeLayout) findViewById(R.id.layout_start_time);
        layoutStartTime.setOnClickListener(this);

        layoutEndTime = (RelativeLayout) findViewById(R.id.layout_end_time);
        layoutEndTime.setOnClickListener(this);

        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);

        layoutDate = (RelativeLayout) findViewById(R.id.date_layout);
        layoutDate.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        Category category = (Category) getIntent().getSerializableExtra(Constants.EXTRA_CATEGORY);
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
            case R.id.tv_cancel:
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

        if (edtWorkName.getText().toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        } else if (tvDate.toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        } else if (edtDayWork.getText().toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        } else if (tvStartTime.getText().toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        }
        if (tvEndTime.getText().toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        } else if (edtDescription.getText().toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        } else if (edtAgeFrom.getText().toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        } else if (edtAgeTo.getText().toString().equals("")) {
            Utils.showLongToast(this, getString(R.string.err_input_data));
            return;
        }

        Work work = new Work();
        work.setName(edtWorkName.getText().toString());
        work.setDate(tvDate.toString());
        work.setNumberDays(Integer.valueOf(edtDayWork.getText().toString()));
        work.setStartTime(tvStartTime.getText().toString());
        work.setEndTime(tvEndTime.getText().toString());
        work.setDescription(edtDescription.getText().toString());
        work.setGenderWorker(spGender.getSelectedItemPosition());
        work.setAgeFromWorker(Integer.valueOf(edtAgeFrom.getText().toString()));
        work.setAgeToWorker(Integer.valueOf(edtAgeTo.getText().toString()));

        Intent intent = new Intent(this, PostATaskMapActivity.class);
        intent.putExtra(Constants.EXTRA_WORK, work);
        intent.putParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES, images);

        startActivity(intent);
    }

    private void openDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        calendar.set(year, monthOfYear, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
