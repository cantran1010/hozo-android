package vn.tonish.hozo.activity;

import android.content.Intent;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TouchImageView;

/**
 * Created by LongBD on 4/19/2017.
 */
public class PreviewImageActivity extends BaseActivity implements View.OnClickListener {


    private TouchImageView imgPreview;

    @Override
    protected int getLayout() {
        return R.layout.preview_image_activity;
    }

    @Override
    protected void initView() {
        imgPreview = (TouchImageView) findViewById(R.id.img_preview);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.EXTRA_IMAGE_PATH);
        Utils.displayImage(this, imgPreview, path);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}


