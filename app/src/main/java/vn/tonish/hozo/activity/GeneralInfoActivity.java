package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.view.CustomWebView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/18/17.
 */

public class GeneralInfoActivity extends BaseActivity implements View.OnClickListener {

    private CustomWebView customWebView;
    private TextViewHozo tvTitle;

    @Override
    protected int getLayout() {
        return R.layout.activity_general_info;
    }

    @Override
    protected void initView() {
        customWebView = (CustomWebView) findViewById(R.id.custom_web_view);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        customWebView.loadUrl(getIntent().getStringExtra(Constants.URL_EXTRA));
        tvTitle.setText(getIntent().getStringExtra(Constants.TITLE_INFO_EXTRA));
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;

        }
    }
}
