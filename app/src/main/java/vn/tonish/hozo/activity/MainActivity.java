package vn.tonish.hozo.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.SettingFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBD.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout layoutPostATask, layoutBrowserTask, layoutMyTask, layoutInBox, layoutOther;
    private ImageView imgPostATask, imgBrowserTask, imgMyTask, imgInbox, imgOther;
    private TextViewHozo tvPostATask, tvBrowserTask, tvMyTask, tvInbox, tvOther;
    private int tabIndex = 1;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        layoutPostATask = (LinearLayout) findViewById(R.id.layout_post_a_task);
        layoutBrowserTask = (LinearLayout) findViewById(R.id.layout_browser_task);
        layoutMyTask = (LinearLayout) findViewById(R.id.layout_my_task);
        layoutInBox = (LinearLayout) findViewById(R.id.layout_inbox);
        layoutOther = (LinearLayout) findViewById(R.id.layout_other);

        imgPostATask = (ImageView) findViewById(R.id.img_post_a_task);
        imgBrowserTask = (ImageView) findViewById(R.id.img_browser_task);
        imgMyTask = (ImageView) findViewById(R.id.img_my_task);
        imgInbox = (ImageView) findViewById(R.id.img_inbox);
        imgOther = (ImageView) findViewById(R.id.img_other);

        tvPostATask = (TextViewHozo) findViewById(R.id.tv_post_a_task);
        tvBrowserTask = (TextViewHozo) findViewById(R.id.tv_browser_task);
        tvMyTask = (TextViewHozo) findViewById(R.id.tv_my_task);
        tvInbox = (TextViewHozo) findViewById(R.id.tv_inbox);
        tvOther = (TextViewHozo) findViewById(R.id.tv_other);
    }

    @Override
    protected void initData() {
        openFragment(R.id.layout_container, SelectTaskFragment.class, false, TransitionScreen.FADE_IN);
        updateMenuUi(1);

        layoutPostATask.setOnClickListener(this);
        layoutBrowserTask.setOnClickListener(this);
        layoutMyTask.setOnClickListener(this);
        layoutInBox.setOnClickListener(this);
        layoutOther.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.layout_post_a_task:
                if (tabIndex == 1) break;
                openFragment(R.id.layout_container, SelectTaskFragment.class, false, TransitionScreen.LEFT_TO_RIGHT);
                tabIndex = 1;
                updateMenuUi(1);
                break;

            case R.id.layout_browser_task:
                if (tabIndex == 2) break;
                if (tabIndex > 2) {
                    openFragment(R.id.layout_container, BrowseTaskFragment.class, false, TransitionScreen.LEFT_TO_RIGHT);
                } else {
                    openFragment(R.id.layout_container, BrowseTaskFragment.class, false, TransitionScreen.RIGHT_TO_LEFT);
                }
                tabIndex = 2;
                updateMenuUi(2);
                break;

            case R.id.layout_my_task:
                if (tabIndex == 3) break;
                if (tabIndex > 3) {
                    openFragment(R.id.layout_container, MyTaskFragment.class, false, TransitionScreen.LEFT_TO_RIGHT);
                } else {
                    openFragment(R.id.layout_container, MyTaskFragment.class, false, TransitionScreen.RIGHT_TO_LEFT);
                }
                tabIndex = 3;
                updateMenuUi(3);
                break;

            case R.id.layout_inbox:
                if (tabIndex == 4) break;
                if (tabIndex > 4) {
                    openFragment(R.id.layout_container, InboxFragment.class, false, TransitionScreen.LEFT_TO_RIGHT);
                } else {
                    openFragment(R.id.layout_container, InboxFragment.class, false, TransitionScreen.RIGHT_TO_LEFT);
                }
                tabIndex = 4;
                updateMenuUi(4);
                break;

            case R.id.layout_other:
                if (tabIndex == 5) break;
                openFragment(R.id.layout_container, SettingFragment.class, false, TransitionScreen.RIGHT_TO_LEFT);
                tabIndex = 5;
                updateMenuUi(5);
                break;

        }
    }

    public void updateMenuUi(int positionMenu) {
        tabIndex = positionMenu;
        imgPostATask.setImageResource(R.drawable.menu_post_task);
        imgBrowserTask.setImageResource(R.drawable.menu_browser_task);
        imgMyTask.setImageResource(R.drawable.menu_my_task);
        imgInbox.setImageResource(R.drawable.menu_inbox);
        imgOther.setImageResource(R.drawable.menu_other);

        tvPostATask.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvBrowserTask.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvMyTask.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvInbox.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvOther.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));

        switch (positionMenu) {
            case 1:
                tvPostATask.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                imgPostATask.setImageResource(R.drawable.menu_post_task_selected);
                break;
            case 2:
                tvBrowserTask.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                imgBrowserTask.setImageResource(R.drawable.menu_browser_task_selected);
                break;
            case 3:
                tvMyTask.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                imgMyTask.setImageResource(R.drawable.menu_my_task_selected);
                break;
            case 4:
                tvInbox.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                imgInbox.setImageResource(R.drawable.menu_inbox_selected);
                break;
            case 5:
                tvOther.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                imgOther.setImageResource(R.drawable.menu_other_selected);
                break;
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
