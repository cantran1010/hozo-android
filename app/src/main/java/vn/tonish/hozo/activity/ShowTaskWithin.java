package vn.tonish.hozo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;

/**
 * Created by CanTran on 6/7/17.
 */

public class ShowTaskWithin extends BaseActivity implements View.OnClickListener {
    private int radius = 0;
    private ImageView imgEveryWhere;
    private ImageView img5Km;
    private ImageView img15Km;
    private ImageView img25Km;
    private ImageView img50Km;

    @Override
    protected int getLayout() {
        return R.layout.activity_show_task_within;
    }

    @Override
    protected void initView() {
        imgEveryWhere = (ImageView) findViewById(R.id.img_everywhere);
        img5Km = (ImageView) findViewById(R.id.img_5km);
        img15Km = (ImageView) findViewById(R.id.img_15km);
        img25Km = (ImageView) findViewById(R.id.img_25km);
        img50Km = (ImageView) findViewById(R.id.img_50km);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);

        FrameLayout layoutEveryWhere = (FrameLayout) findViewById(R.id.layout_everywhere);
        FrameLayout layout5Km = (FrameLayout) findViewById(R.id.layout_5km);
        FrameLayout layout15Km = (FrameLayout) findViewById(R.id.layout_15km);
        FrameLayout layout25Km = (FrameLayout) findViewById(R.id.layout_25km);
        FrameLayout layout50Km = (FrameLayout) findViewById(R.id.layout_50km);
        layoutEveryWhere.setOnClickListener(this);
        layout5Km.setOnClickListener(this);
        layout15Km.setOnClickListener(this);
        layout25Km.setOnClickListener(this);
        layout50Km.setOnClickListener(this);
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        radius = (int) intent.getExtras().get(Constants.REQUEST_EXTRAS_RADIUS);
        switch (radius) {
            case 0:
                imgEveryWhere.setVisibility(View.VISIBLE);
                break;
            case 5:
                img5Km.setVisibility(View.VISIBLE);
                break;
            case 15:
                img15Km.setVisibility(View.VISIBLE);
                break;
            case 25:
                img25Km.setVisibility(View.VISIBLE);
                break;
            case 50:
                img50Km.setVisibility(View.VISIBLE);
                break;
        }

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
            case R.id.layout_everywhere:
                radius = 0;
                imgEveryWhere.setVisibility(View.VISIBLE);
                img5Km.setVisibility(View.GONE);
                img15Km.setVisibility(View.GONE);
                img25Km.setVisibility(View.GONE);
                img50Km.setVisibility(View.GONE);
                selectedRadius();
                break;
            case R.id.layout_5km:
                radius = 5;
                imgEveryWhere.setVisibility(View.GONE);
                img5Km.setVisibility(View.VISIBLE);
                img15Km.setVisibility(View.GONE);
                img25Km.setVisibility(View.GONE);
                img50Km.setVisibility(View.GONE);
                selectedRadius();
                break;
            case R.id.layout_15km:
                radius = 15;
                imgEveryWhere.setVisibility(View.GONE);
                img5Km.setVisibility(View.GONE);
                img15Km.setVisibility(View.VISIBLE);
                img25Km.setVisibility(View.GONE);
                img50Km.setVisibility(View.GONE);
                selectedRadius();
                break;
            case R.id.layout_25km:
                radius = 25;
                imgEveryWhere.setVisibility(View.GONE);
                img5Km.setVisibility(View.GONE);
                img15Km.setVisibility(View.GONE);
                img25Km.setVisibility(View.VISIBLE);
                img50Km.setVisibility(View.GONE);
                selectedRadius();
                break;
            case R.id.layout_50km:
                radius = 50;
                imgEveryWhere.setVisibility(View.GONE);
                img5Km.setVisibility(View.GONE);
                img15Km.setVisibility(View.GONE);
                img25Km.setVisibility(View.GONE);
                img50Km.setVisibility(View.VISIBLE);
                selectedRadius();
                break;
        }
    }

    private void selectedRadius() {
        Intent data = new Intent();
        data.putExtra(Constants.EXTRA_RADIUS, radius);
        setResult(Constants.RESULT_RADIUS, data);
        finish();
    }

}
