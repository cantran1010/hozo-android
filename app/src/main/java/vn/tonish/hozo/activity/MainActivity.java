package vn.tonish.hozo.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.HelpFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBD.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout layoutPostATask, layoutBrowserTask, layoutMyTask, layoutInBox, layoutOther;
    private ImageView imgPostATask, imgBrowserTask, imgMyTask, imgInbox, imgOther;
    private TextViewHozo tvPostATask, tvBrowserTask, tvMyTask, tvInbox, tvOther;

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
        openFragment(R.id.layout_container, SelectTaskFragment.class, false,true);

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
                updateMenuUi(1);
                openFragment(R.id.layout_container, SelectTaskFragment.class, false,true);
                break;

            case R.id.layout_browser_task:
                updateMenuUi(2);
                openFragment(R.id.layout_container, BrowseTaskFragment.class, false,true);
                break;

            case R.id.layout_my_task:
                updateMenuUi(3);
                openFragment(R.id.layout_container, MyTaskFragment.class, false,true);
                break;

            case R.id.layout_inbox:
                updateMenuUi(4);
                openFragment(R.id.layout_container, InboxFragment.class, false,true);
                break;

            case R.id.layout_other:
                updateMenuUi(5);
                openFragment(R.id.layout_container, HelpFragment.class, false,true);
                break;

        }
    }

    private void updateMenuUi(int positionMenu) {

//        imgPostATask = (ImageView) findViewById(R.id.img_post_a_task);
//        imgBrowserTask = (ImageView) findViewById(R.id.img_browser_task);
//        imgMyTask = (ImageView) findViewById(R.id.img_my_task);
//        imgInbox = (ImageView) findViewById(R.id.img_inbox);
//        imgOther = (ImageView) findViewById(R.id.img_other);

        tvPostATask.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvBrowserTask.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvMyTask.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvInbox.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));
        tvOther.setTextColor(ContextCompat.getColor(this, R.color.menu_non_selected));

        switch (positionMenu) {
            case 1:
                tvPostATask.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;
            case 2:
                tvBrowserTask.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;
            case 3:
                tvMyTask.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;
            case 4:
                tvInbox.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;
            case 5:
                tvOther.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                break;
        }

    }
}
