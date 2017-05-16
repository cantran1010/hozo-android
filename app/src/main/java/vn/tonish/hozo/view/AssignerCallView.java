package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/21/2017.
 */

public class AssignerCallView extends LinearLayout {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo;
    private RatingBar ratingBar;
    private ButtonHozo btnCall;

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
        layoutInflater.inflate(R.layout.assigner_view_call, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        ratingBar = (RatingBar) findViewById(R.id.rb_rate);
        btnCall = (ButtonHozo) findViewById(R.id.btn_call);
    }

    public void updateData(final Assigner assigner) {

        LogUtils.d(TAG, "AssignerCallView , updateData assigner : " + assigner.toString());

        Utils.displayImageAvatar(getContext(), imgAvatar, assigner.getAvatar());
        tvName.setText(assigner.getFullName());
        ratingBar.setRating(assigner.getTaskerAverageRating());
        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(assigner.getBiddedAt(), getContext()));

        btnCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.call(getContext(), assigner.getPhone());
            }
        });
    }
}
