package vn.tonish.hozo.activity.setting;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.Calendar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/16/17.
 */

public class TimeSettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = TimeSettingActivity.class.getSimpleName();
    private ImageView imgBack;
    private TextViewHozo btnTimeStart, btnTimeEnd;
    private ButtonHozo btnReset, btnSave;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected int getLayout() {
        return R.layout.activity_time_setting;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        btnTimeStart = (TextViewHozo) findViewById(R.id.tv_time_start);
        btnTimeEnd = (TextViewHozo) findViewById(R.id.tv_time_end);
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);
        btnSave = (ButtonHozo) findViewById(R.id.btn_save);

    }

    @Override
    protected void initData() {
        imgBack.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnTimeEnd.setOnClickListener(this);
        btnTimeStart.setOnClickListener(this);


    }

    @Override
    protected void resumeData() {


    }

    private void getTimeStart(final boolean isSart) {
        TimePickerDialog timeEndPickerDialog = new TimePickerDialog(TimeSettingActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        if (isSart) {
                            btnTimeStart.setText(hourOfDay + ":" + minute);
                        } else {
                            btnTimeEnd.setText(hourOfDay + ":" + minute);
                        }
                    }
                }, calendar.get((Calendar.HOUR_OF_DAY)), calendar.get(Calendar.MINUTE), false);
        timeEndPickerDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_time_start:
                getTimeStart(true);
                break;
            case R.id.tv_time_end:
                getTimeStart(false);
                break;
        }

    }
}
