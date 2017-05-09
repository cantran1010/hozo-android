package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Work;

/**
 * Created by LongBui on 4/21/2017.
 */

public class WorkDetailView extends LinearLayout implements View.OnClickListener {

    protected CircleImageView imgAvatar;

    private TextViewHozo tvName, tvTitle, tvTimeAgo, tvWorkType, tvDescription;
    private RatingBar rbRate;
    private ImageView imgMobile, imgEmail, imgFacebook;
    private TextViewHozo tvPrice, tvDate, tvTime, tvAddress, tvStatus;
    private ButtonHozo btnOffer,btnCallRate;

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
        tvName = (TextViewHozo) findViewById(R.id.tv_name);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        tvWorkType = (TextViewHozo) findViewById(R.id.tv_work_type);
        tvDescription = (TextViewHozo) findViewById(R.id.tv_description);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);

        imgMobile = (ImageView) findViewById(R.id.img_mobile_verify);
        imgEmail = (ImageView) findViewById(R.id.img_email_verify);
        imgFacebook = (ImageView) findViewById(R.id.img_facebook_verify);

        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
        tvDate = (TextViewHozo) findViewById(R.id.tv_date);
        tvTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);

        btnOffer = (ButtonHozo) findViewById(R.id.btn_offer);
        btnOffer.setOnClickListener(this);

        tvStatus = (TextViewHozo) findViewById(R.id.tv_status);

        btnCallRate = (ButtonHozo) findViewById(R.id.btn_call_rate);

    }

    public void updateWork(Work work) {
        tvName.setText(work.getUser().getFull_name());

        tvTime.setText(work.getName());
        tvTimeAgo.setText(work.getTimeAgo());
        tvWorkType.setText(work.getWorkTypeName());
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

    public void updateStatus(String status, Drawable drawable) {
//        tvStatus.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.bg_border_recruitment));

        tvStatus.setText(status);
        final int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            tvStatus.setBackgroundDrawable(drawable);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tvStatus.setBackground(drawable);
        }
    }

    public void updateBtnCallRate(boolean isShow,boolean isCall,String text){
        if(isShow){
            btnCallRate.setVisibility(View.VISIBLE);
            btnCallRate.setText(text);
            if(isCall){
                btnCallRate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }else{
                btnCallRate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }else{
            btnCallRate.setVisibility(View.GONE);
        }
    }



    @Override
    public void onClick(View v) {

    }
}
