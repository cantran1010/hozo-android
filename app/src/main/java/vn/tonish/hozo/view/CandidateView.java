package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import vn.tonish.hozo.view.ButtonHozo;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import vn.tonish.hozo.view.TextViewHozo;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/21/2017.
 */

public class CandidateView extends LinearLayout {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo, tvPrice;
    private RatingBar rbRate;
    private ButtonHozo btnAssign;

    public CandidateView(Context context) {
        super(context);
        initView();
    }

    public CandidateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CandidateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CandidateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.candidate_view, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);
        btnAssign = (ButtonHozo) findViewById(R.id.btn_assign);
        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
    }

    public void updateData(User user) {

        Utils.displayImageAvatar(getContext(), imgAvatar, user.getAvatar());
        tvName.setText(user.getFull_name());
//        tvTimeAgo.setText();

        btnAssign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
