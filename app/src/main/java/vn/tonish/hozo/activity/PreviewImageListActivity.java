package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.PreviewPagerAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.view.SimpleViewPagerIndicator;

/**
 * Created by LongBui on 4/19/2017.
 */
public class PreviewImageListActivity extends BaseActivity implements View.OnClickListener {

    public ViewPager pager;
    public SimpleViewPagerIndicator mIndicator;
    public int startPosition;

    public static final String EXTRA_POSITION = "extra_position";
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.preview_image_list_activity;
    }

    @Override
    protected void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (SimpleViewPagerIndicator) findViewById(R.id.indicator);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        startPosition = intent.getIntExtra(Constants.IMAGE_POSITITON_EXTRA, 0);
        ArrayList<String> attachments = intent.getStringArrayListExtra(Constants.IMAGE_ATTACHS_EXTRA);

        PreviewPagerAdapter adapter = new PreviewPagerAdapter(this, attachments);
        pager.setAdapter(adapter);
        pager.setCurrentItem(startPosition);
        mIndicator.setViewPager(pager);
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

            case R.id.img_back:
                finish();
            break;

        }
    }
}


