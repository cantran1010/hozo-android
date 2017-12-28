package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AssignActivity;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class BidderOpenView extends RelativeLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvDoneRate, tvPrice;
    private RatingBar rbRate;
    private ButtonHozo btnAssign;
    private Bidder bidder;
    private int taskId;
    private int assinerCount;
    private int workerCount;

    public BidderOpenView(Context context) {
        super(context);
        initView();
    }

    public BidderOpenView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BidderOpenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BidderOpenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_bidder_open, this, true);
        imgAvatar = findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);
        tvName = findViewById(R.id.tv_name);
        tvDoneRate = findViewById(R.id.tv_poster_done_rate);
        rbRate = findViewById(R.id.rb_rate);
        btnAssign = findViewById(R.id.btn_assign);
        tvPrice = findViewById(R.id.tv_price);
    }

    public void updateData(final Bidder bidder, String type) {
        LogUtils.d(TAG, "updateData bidder : " + bidder.toString());
        this.bidder = bidder;
        Utils.displayImageAvatar(getContext(), imgAvatar, bidder.getAvatar());
        tvName.setText(bidder.getFullName());
        rbRate.setRating(bidder.getTaskerAverageRating());
        tvPrice.setText(getContext().getString(R.string.price_bidder, Utils.formatNumber(bidder.getPrice())));

        String percentDone = (int) (bidder.getPosterDoneRate() * 100) + "% " + getContext().getString(R.string.completion_rate);
        tvDoneRate.setText(percentDone);
        if (type.equals(getContext().getString(R.string.assign))) {
            btnAssign.setVisibility(View.VISIBLE);
            btnAssign.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentAssign = new Intent(getContext(), AssignActivity.class);
                    intentAssign.putExtra(Constants.OFFER_TASK_ID, getTaskId());
                    intentAssign.putExtra(Constants.BIDDER_ID, bidder.getId());
                    intentAssign.putExtra(Constants.WORKER_COUNT, getWorkerCount());
                    intentAssign.putExtra(Constants.ASSSIGNER_COUNT, getAssinerCount());
                    intentAssign.putExtra(Constants.BIDDER_ID, bidder.getId());
                    ((BaseActivity) getContext()).startActivityForResult(intentAssign, Constants.REQUEST_CODE_RATE, TransitionScreen.RIGHT_TO_LEFT);
                }
            });
        } else {
            btnAssign.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_avatar:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, bidder.getId());
                intent.putExtra(Constants.IS_MY_USER, bidder.getId() == UserManager.getMyUser().getId());
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

    public int getAssinerCount() {
        return assinerCount;
    }

    public void setAssinerCount(int assinerCount) {
        this.assinerCount = assinerCount;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }
}
