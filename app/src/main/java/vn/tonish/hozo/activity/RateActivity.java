package vn.tonish.hozo.activity;

import android.widget.RatingBar;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 4/25/2017.
 */

public class RateActivity extends BaseActivity {

    private RatingBar ratingBar;

    @Override
    protected int getLayout() {
        return R.layout.rate_activity;
    }

    @Override
    protected void initView() {
        ratingBar = (RatingBar) findViewById(R.id.rb_rate);
    }

    @Override
    protected void initData() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

//                Toast.makeText(getApplicationContext(), "Your Selected Ratings  : " + String.valueOf(rating), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void resumeData() {

    }
}
