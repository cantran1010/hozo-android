package vn.tonish.hozo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.dialog.TimePickerDialog;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by MAC2015 on 4/12/17.
 */

public class PostATaskActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout layoutBack;
    private Button btnNext;
    private LinearLayout layoutStartTime, layoutEndTime;

    @Override
    protected int getLayout() {
        return R.layout.activity_post_a_task;
    }

    @Override
    protected void initView() {
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        btnNext = (Button) findViewById(R.id.btn_next);

        layoutStartTime = (LinearLayout) findViewById(R.id.layout_start_time);
        layoutStartTime.setOnClickListener(this);

        layoutEndTime = (LinearLayout) findViewById(R.id.layout_end_time);
        layoutEndTime.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        layoutBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_next:
                Intent intent = new Intent(this, PostATaskMapActivity.class);
                startActivity(intent);
                break;

            case R.id.layout_start_time:
                TimePickerDialog timePickerDialog = new TimePickerDialog(this);
                timePickerDialog.setOnPickLister(new TimePickerDialog.OnPickLister() {
                    @Override
                    public void onPick(int time) {
                        Utils.showLongToast(PostATaskActivity.this, time + "");
                    }
                });
                timePickerDialog.showView();
                break;

            case R.id.layout_end_time:

                break;
        }
    }
}
