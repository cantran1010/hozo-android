package vn.tonish.hozo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TouchImageView;

/**
 * Created by LongBui on 4/19/2017.
 */
public class PreviewImageActivity extends BaseActivity implements View.OnClickListener {


    private TouchImageView imgPreview;
    private RelativeLayout layoutBack;


    @Override
    protected int getLayout() {
        return R.layout.preview_image_activity;
    }

    @Override
    protected void initView() {
        imgPreview = (TouchImageView) findViewById(R.id.img_preview);
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
    }

    @Override
    protected void initData() {

        layoutBack.setOnClickListener(this);

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
            case R.id.layout_back:
                finish();
                break;
        }
    }
}


