package vn.tonish.hozo.fragment.support;

import android.view.View;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;

/**
 * Created by LongBui on 11/1/17.
 */

public class SupportRateFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = SupportRateFragment.class.getSimpleName();
    private ButtonHozo btnSend;
    private RatingBar ratingBar;
    private EdittextHozo edtContent;

    @Override
    protected int getLayout() {
        return R.layout.support_rate_fragment;
    }

    @Override
    protected void initView() {
        btnSend = (ButtonHozo) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        ratingBar = (RatingBar) findViewById(R.id.rb_rate);
        edtContent = (EdittextHozo) findViewById(R.id.edt_content);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_send:

                break;

        }
    }
}
