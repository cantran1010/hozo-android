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
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/21/2017.
 */

public class UserView extends LinearLayout {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvTimeAgo, tvPrice;
    private RatingBar rbRate;
    private ButtonHozo btnCall;

    public UserView(Context context) {
        super(context);
        initView();
    }

    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public UserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.user_view, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        rbRate = (RatingBar) findViewById(R.id.rb_rate);
        btnCall = (ButtonHozo) findViewById(R.id.btn_call);
        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
    }

    public void updateData(User user) {

        Utils.displayImageAvatar(getContext(), imgAvatar, user.getAvatar());
        tvName.setText(user.getFullName());
//        tvTimeAgo.setText();

        btnCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
