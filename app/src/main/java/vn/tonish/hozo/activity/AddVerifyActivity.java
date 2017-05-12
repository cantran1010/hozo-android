package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;

public class AddVerifyActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.activity_add_verify;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);

    }

    @Override
    protected void initData() {
        imgBack.setOnClickListener(this);

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
