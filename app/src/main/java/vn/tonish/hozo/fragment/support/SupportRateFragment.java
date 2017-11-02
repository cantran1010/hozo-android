package vn.tonish.hozo.fragment.support;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RatingBar;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 11/1/17.
 */

public class SupportRateFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = SupportRateFragment.class.getSimpleName();
    private ButtonHozo btnSend;
    private RatingBar ratingBar;
    private EdittextHozo edtContent;
    private TextViewHozo tvGoPlayStore;

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

        tvGoPlayStore = (TextViewHozo) findViewById(R.id.tv_go_play_store);
        tvGoPlayStore.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    edtContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

//                else {
//                    //Assign your image again to the view, otherwise it will always be gone even if the text is 0 again.
//                    edtContent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
//                }
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_send:

                break;

            case R.id.tv_go_play_store:
                Utils.openPlayStore(getActivity());
                break;

        }
    }
}
