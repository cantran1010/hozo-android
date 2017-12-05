package vn.tonish.hozo.fragment.support;

import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.BaseFragment;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by LongBui on 11/1/17.
 */

public class SupportPhoneFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected int getLayout() {
        return R.layout.support_phone_fragment;
    }

    @Override
    protected void initView() {
        ButtonHozo btnCall = (ButtonHozo) findViewById(R.id.btn_call);
        btnCall.setOnClickListener(this);
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

            case R.id.btn_call:
                Utils.call(getContext(), getString(R.string.hot_line));
                break;

        }
    }
}
