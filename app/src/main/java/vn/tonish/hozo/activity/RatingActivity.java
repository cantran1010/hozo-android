package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ViewPageRatingAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.TaskResponse;

/**
 * Created by CanTran on 11/21/17.
 */

public class RatingActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private ViewPageRatingAdapter adapter;
    private TaskResponse taskResponse;
    private String type;
    private ImageView imgClose, imgNext, imgBack;

    @Override
    protected int getLayout() {
        return R.layout.activity_rating;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.container);
        imgClose = (ImageView) findViewById(R.id.img_close);
        imgNext = (ImageView) findViewById(R.id.img_rating_next);
        imgBack = (ImageView) findViewById(R.id.img_rating_back);
        imgClose.setOnClickListener(this);
        imgNext.setOnClickListener(this);
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.TASK_RESPONSE_RATING)) {
            taskResponse = (TaskResponse) intent.getExtras().get(Constants.TASK_RESPONSE_RATING);
        }
        if (taskResponse == null) taskResponse = new TaskResponse();

        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            type = Constants.ROLE_POSTER;
            imgNext.setVisibility(View.GONE);
            imgBack.setVisibility(View.GONE);
        } else {
            type = Constants.ROLE_TASKER;
            imgNext.setVisibility(View.VISIBLE);
            imgBack.setVisibility(View.VISIBLE);
        }

        adapter = new ViewPageRatingAdapter(this, taskResponse, type);
        viewPager.setAdapter(adapter);

    }

    @Override
    protected void resumeData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            case R.id.img_rating_next:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                break;
            case R.id.img_rating_back:
                if (viewPager.getCurrentItem() > 0) {
                    imgBack.setAlpha(1f);
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                } else imgBack.setAlpha(0.2f);
                break;

        }
    }
}
