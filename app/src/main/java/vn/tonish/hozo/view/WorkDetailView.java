package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PreviewImageListActivity;
import vn.tonish.hozo.adapter.ImageDetailTaskAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/21/2017.
 */

public class WorkDetailView extends LinearLayout implements View.OnClickListener {

    protected CircleImageView imgAvatar;

    private TextViewHozo tvName, tvTitle, tvTimeAgo, tvWorkType, tvDescription,tvImageAttachTitle;
    private RatingBar rbRate;
    private ImageView imgMobile, imgEmail, imgFacebook;
    private TextViewHozo tvPrice, tvDate, tvTime, tvAddress, tvStatus;
    private ButtonHozo btnOffer, btnCallRate;
    private MyGridView myGridView;

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

        myGridView = (MyGridView) findViewById(R.id.gr_image);

        tvImageAttachTitle = (TextViewHozo) findViewById(R.id.tv_img_attach_title);

    }

    public void updateWork(TaskResponse taskResponse) {
        Utils.displayImage(getContext(), imgAvatar, taskResponse.getPoster().getAvatar());
        tvName.setText(taskResponse.getPoster().getFullName());
        rbRate.setRating(taskResponse.getPoster().getPosterAverageRating());
        tvTitle.setText(taskResponse.getTitle());
        tvTime.setText(taskResponse.getTitle());
        tvTimeAgo.setText(taskResponse.getEndTime());
        tvWorkType.setText(taskResponse.getCategoryId() + "");
        tvDescription.setText(taskResponse.getDescription());

        tvPrice.setText(taskResponse.getCurrency());
        tvDate.setText(taskResponse.getStartTime());
        tvTime.setText(taskResponse.getEndTime());
        tvAddress.setText(taskResponse.getAddress());

        final ArrayList<String> attachments = (ArrayList<String>) taskResponse.getAttachments();
        attachments.addAll(attachments);
        attachments.addAll(attachments);
        attachments.addAll(attachments);
        attachments.addAll(attachments);

        if (attachments.size() > 0) {
            ImageDetailTaskAdapter imageDetailTaskAdapter = new ImageDetailTaskAdapter(getContext(), attachments);
            myGridView.setAdapter(imageDetailTaskAdapter);

            myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), PreviewImageListActivity.class);
                    intent.putStringArrayListExtra(Constants.IMAGE_ATTACHS_EXTRA, attachments);
                    intent.putExtra(Constants.IMAGE_POSITITON_EXTRA, position);
                    getContext().startActivity(intent);
                }
            });
        }else{
            tvImageAttachTitle.setVisibility(View.GONE);
        }

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

    public void updateBtnCallRate(boolean isShow, boolean isCall, String text) {
        if (isShow) {
            btnCallRate.setVisibility(View.VISIBLE);
            btnCallRate.setText(text);
            if (isCall) {
                btnCallRate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                btnCallRate.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        } else {
            btnCallRate.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {

    }
}
