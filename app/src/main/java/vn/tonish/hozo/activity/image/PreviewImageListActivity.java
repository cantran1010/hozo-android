package vn.tonish.hozo.activity.image;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.PreviewPagerAdapter;
import vn.tonish.hozo.common.Constants;

/**
 * Created by LongBui on 4/19/2017.
 */
public class PreviewImageListActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager pager;
    private CircleIndicator mIndicator;

    public static final String EXTRA_POSITION = "extra_position";

    @Override
    protected int getLayout() {
        return R.layout.activity_preview_image_list;
    }

    @Override
    protected void initView() {
        pager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (CircleIndicator) findViewById(R.id.indicator);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        int startPosition = intent.getIntExtra(Constants.IMAGE_POSITITON_EXTRA, 0);
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


