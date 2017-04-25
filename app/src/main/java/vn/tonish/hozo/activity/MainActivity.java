package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.HelpFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;

/**
 * Created by LongBD.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private View layoutPostTask, layoutBrowerTask, layoutMyTask, layoutOther;
    private View layoutInbox;


    private BroadcastReceiver badgeChangeListener;

    private IntentFilter intentFilter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        layoutPostTask = findViewById(R.id.layout_post_a_task);
        layoutBrowerTask = findViewById(R.id.layout_browser_task);
        layoutMyTask = findViewById(R.id.layout_my_task);
        layoutInbox = findViewById(R.id.layout_inbox);
        layoutOther = findViewById(R.id.layout_other);
        badgeChangeListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        intentFilter = new IntentFilter(Constants.BADGE);
        registerReceiver(badgeChangeListener, intentFilter);

    }

    @Override
    protected void initData() {
        openFragment(R.id.layout_container, SelectTaskFragment.class, false);
        layoutPostTask.setOnClickListener(this);
        layoutBrowerTask.setOnClickListener(this);
        layoutMyTask.setOnClickListener(this);
        layoutInbox.setOnClickListener(this);
        layoutOther.setOnClickListener(this);

    }

    @Override
    protected void resumeData() {
        if (badgeChangeListener != null)
            registerReceiver(badgeChangeListener, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (badgeChangeListener != null)
            unregisterReceiver(badgeChangeListener);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_post_a_task:
                openFragment(R.id.layout_container, SelectTaskFragment.class, false);
                layoutPostTask.setSelected(true);
                break;

            case R.id.layout_browser_task:
                openFragment(R.id.layout_container, BrowseTaskFragment.class, false);
                break;

            case R.id.layout_my_task:
                openFragment(R.id.layout_container, MyTaskFragment.class, false);
                break;

            case R.id.layout_inbox:
                openFragment(R.id.layout_container, InboxFragment.class, false);
                break;

            case R.id.layout_other:
                openFragment(R.id.layout_container, HelpFragment.class, false);
                break;
        }
    }

}
