package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class AssignerCallView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo;
    private RatingBar ratingBar;
    private ButtonHozo btnCall;
    private Assigner assigner;
    private int taskId;

    public AssignerCallView(Context context) {
        super(context);
        initView();
    }

    public AssignerCallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AssignerCallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AssignerCallView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_assigner_call, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        ratingBar = (RatingBar) findViewById(R.id.rb_rate);
        btnCall = (ButtonHozo) findViewById(R.id.btn_call);
    }

    public void updateData(final Assigner assigner, String assignType) {

        LogUtils.d(TAG, "AssignerCallView , updateData assigner : " + assigner.toString());
        this.assigner = assigner;
        Utils.displayImageAvatar(getContext(), imgAvatar, assigner.getAvatar());
        tvName.setText(assigner.getFullName());
        ratingBar.setRating(assigner.getTaskerAverageRating());
        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(assigner.getBiddedAt(), getContext()));
        btnCall.setText(assignType);

        if (assignType.equals(getContext().getString(R.string.call))) {
            btnCall.setVisibility(View.VISIBLE);
            btnCall.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.call(getContext(), assigner.getPhone());
                }
            });
        } else if (assignType.equals(getContext().getString(R.string.rate))) {

            if (assigner.getRating() == 0) {
                btnCall.setVisibility(View.VISIBLE);
                btnCall.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentAnswer = new Intent();
                        intentAnswer.setAction("MyBroadcast");
                        intentAnswer.putExtra(Constants.ASSIGNER_RATE_EXTRA, assigner);
                        getContext().sendBroadcast(intentAnswer);
                    }
                });
            } else {
                btnCall.setVisibility(View.GONE);
            }


        } else {
            btnCall.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_avatar:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, assigner.getId());
                intent.putExtra(Constants.IS_MY_USER, assigner.getId() == UserManager.getMyUser().getId());
                getContext().startActivity(intent);
                break;

        }
    }

    private int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
