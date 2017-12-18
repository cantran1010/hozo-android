package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ViewPageRatingAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 11/21/17.
 */

public class RatingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = RatingActivity.class.getSimpleName();
    private ViewPager viewPager;
    private TaskResponse taskResponse;
    private String type;
    private ImageView imgNext;
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.activity_rating;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.container);
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
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
            LogUtils.d(TAG, "check task reponse :" + taskResponse.toString());
        }
        if (taskResponse == null) taskResponse = new TaskResponse();

        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            type = Constants.ROLE_POSTER;
            imgNext.setVisibility(View.VISIBLE);
            imgBack.setVisibility(View.VISIBLE);
            if (taskResponse.getAssigneeCount() == 1) {
                imgNext.setVisibility(View.GONE);
                imgBack.setVisibility(View.GONE);
            }
        } else {
            type = Constants.ROLE_TASKER;
            imgNext.setVisibility(View.GONE);
            imgBack.setVisibility(View.GONE);
        }
        ViewPageRatingAdapter adapter = new ViewPageRatingAdapter(this, taskResponse, type);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.d("TAG", "position: " + position);
                if (position == 0)
                    imgBack.setVisibility(View.GONE);
                else imgBack.setVisibility(View.VISIBLE);
                if (position == taskResponse.getAssigneeCount() - 1)
                    imgNext.setVisibility(View.GONE);
                else imgNext.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        adapter.setRatingListener(new ViewPageRatingAdapter.RatingListener() {
            @Override
            public void success() {
                if (isFinishRating()) {
                    setResult(Constants.RESPONSE_CODE_RATE);
                    finish();
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }

    private boolean isFinishRating() {
        boolean isFinish = false;
        if (type.equalsIgnoreCase(Constants.ROLE_TASKER) || taskResponse.getAssigneeCount() < 2)
            isFinish = true;
        if (type.equalsIgnoreCase(Constants.ROLE_POSTER) && (taskResponse.getAssigneeCount() > 1)) {
            if (isCheckRating()) {
                isFinish = true;
            }
        }
        return isFinish;
    }

    private boolean isCheckRating() {
        for (Assigner assigner : taskResponse.getAssignees()
                ) {
            if (assigner.getRating() < 1) {
                return false;
            }
        }
        return true;
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
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                break;

        }
    }
}
