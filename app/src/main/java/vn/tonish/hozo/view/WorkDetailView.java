package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Work;

/**
 * Created by LongBui on 4/21/2017.
 */

public class WorkDetailView extends LinearLayout implements View.OnClickListener {

    protected CircleImageView imgAvatar;

    private TextView tvName, tvTitle, tvTimeAgo, tvWorkType, tvDescription;
    private RatingBar rbRate;
    private ImageView imgMobile, imgEmail, imgFacebook;
    private TextView tvPrice, tvDate, tvTime, tvAddress;
    private Button btnOffer;

    public WorkDetailView(Context context) {
        super(context);
        init();
    }

    public WorkDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WorkDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WorkDetailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.work_detail_view, this, true);

        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        tvName = (TextView) findViewById(R.id.tv_name);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTimeAgo = (TextView) findViewById(R.id.tv_time_ago);
        tvWorkType = (TextView) findViewById(R.id.tv_work_type);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);

        imgMobile = (ImageView) findViewById(R.id.img_mobile_verify);
        imgEmail = (ImageView) findViewById(R.id.img_email_verify);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook_verify);

        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvAddress = (TextView) findViewById(R.id.tv_address);

        btnOffer = (Button) findViewById(R.id.btn_offer);
        btnOffer.setOnClickListener(this);

    }

    public void updateWork(Work work) {
        tvName.setText(work.getUser().getFull_name());

        tvTime.setText(work.getName());
        tvTimeAgo.setText(work.getTimeAgo());
        tvWorkType.setText(work.getWorkType());
        tvDescription.setText(work.getDescription());

        tvPrice.setText(work.getPrice());
        tvDate.setText(work.getDate());
        tvTime.setText(work.getTime());
        tvAddress.setText(work.getAddress());

    }

    public void updateBtnOffer(boolean isShow) {
        if
                (isShow) btnOffer.setVisibility(View.VISIBLE);
        else
            btnOffer.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }
}
