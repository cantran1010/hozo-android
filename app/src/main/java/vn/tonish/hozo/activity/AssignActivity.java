package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ViewPageAssignAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 11/21/17.
 */

public class AssignActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AssignActivity.class.getSimpleName();
    private ViewPager viewPager;
    private TaskResponse taskResponse;
    private ImageView imgNext;
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.activity_assign;
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.container);
        ImageView imgClose = (ImageView) findViewById(R.id.img_back);
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
        ViewPageAssignAdapter adapter = new ViewPageAssignAdapter(this, taskResponse.getId(), taskResponse.getAssignees().size(), taskResponse.getAssigneeCount(), taskResponse.getBidderCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
