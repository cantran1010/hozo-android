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
import vn.tonish.hozo.activity.other.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class BidderView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo, tvPrice;
    private RatingBar ratingBar;
    private Bidder bidder;

    public BidderView(Context context) {
        super(context);
        initView();
    }

    public BidderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BidderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BidderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.bidder_view, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);
        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        ratingBar = (RatingBar) findViewById(R.id.rb_rate);
    }

    public void updateData(Bidder bidder) {
        LogUtils.d(TAG, "BidderView , updateData bidder : " + bidder.toString());
        this.bidder = bidder;
        Utils.displayImageAvatar(getContext(), imgAvatar, bidder.getAvatar());
        tvName.setText(bidder.getFullName());
        ratingBar.setRating(bidder.getTaskerAverageRating());
        if (bidder.getBidedAt() != null)
            tvTimeAgo.setText(DateTimeUtils.getTimeAgo(bidder.getBidedAt(), getContext()));
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
}
