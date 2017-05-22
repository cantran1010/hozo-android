package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class BidderView extends LinearLayout {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo, tvPrice;
    private RatingBar ratingBar;

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

        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        ratingBar = (RatingBar) findViewById(R.id.rb_rate);
    }

    public void updateData(Bidder bidder) {

        LogUtils.d(TAG, "BidderView , updateData bidder : " + bidder.toString());

        Utils.displayImageAvatar(getContext(), imgAvatar, bidder.getAvatar());
        tvName.setText(bidder.getFullName());
        ratingBar.setRating(bidder.getTaskerAverageRating());
        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(bidder.getBidedAt(), getContext()));
    }
}
