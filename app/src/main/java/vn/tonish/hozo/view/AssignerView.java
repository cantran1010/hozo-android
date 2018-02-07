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
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class AssignerView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvDoneRate, tvPrice;
    private RatingBar ratingBar;
    private ButtonHozo btnCancel;
    private Assigner assigner;
    private int taskId;
    private int posterID;

    public AssignerView(Context context) {
        super(context);
        initView();
    }

    public AssignerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AssignerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AssignerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_assigner, this, true);
        imgAvatar = findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        tvName = findViewById(R.id.tv_name);
        ratingBar = findViewById(R.id.rb_rate);
        tvPrice = findViewById(R.id.tv_price);
        btnCancel = findViewById(R.id.btn_cancel);
        tvDoneRate = findViewById(R.id.tv_poster_done_rate);
    }

    public void updateData(final Assigner assigner, String assignType) {
        LogUtils.d(TAG, "AssignerCallView , updateData assigner : " + assigner.toString());
        this.assigner = assigner;
        Utils.displayImageAvatar(getContext(), imgAvatar, assigner.getAvatar());
        tvName.setText(assigner.getFullName());
        ratingBar.setRating(assigner.getTaskerAverageRating());
        LogUtils.d(TAG, "AssignerCallView , updateData assigner : " + (UserManager.getMyUser().getId() == getPosterID()));
        if (UserManager.getMyUser().getId() == getPosterID()) {
            tvPrice.setVisibility(VISIBLE);
            tvPrice.setText(getContext().getString(R.string.price_bidder, Utils.formatNumber(assigner.getPrice())));
        } else tvPrice.setVisibility(GONE);
        String percentDone = (int) (assigner.getPosterDoneRate() * 100) + "% " + getContext().getString(R.string.completion_rate);
        tvDoneRate.setText(percentDone);
        if (assignType.equals(getContext().getString(R.string.call))) {
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction("MyBroadcast");
                    intentAnswer.putExtra(Constants.ASSIGNER_CANCEL_BID_EXTRA, assigner);
                    getContext().sendBroadcast(intentAnswer);
                }
            });


        } else if (assignType.equals(getContext().getString(R.string.rate))) {
            btnCancel.setVisibility(View.GONE);
        } else {
            btnCancel.setVisibility(View.GONE);
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

    public int getPosterID() {
        return posterID;
    }

    public void setPosterID(int posterID) {
        this.posterID = posterID;
    }
}
