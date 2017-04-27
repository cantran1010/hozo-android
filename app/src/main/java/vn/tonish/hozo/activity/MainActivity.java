package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.HelpFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBD.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView layoutPostTask, layoutBrowserTask, layoutMyTask, layoutOther, layoutInbox;
    private BroadcastReceiver badgeChangeListener;
    private IntentFilter intentFilter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        layoutPostTask = (TextView) findViewById(R.id.layout_post_a_task);
        layoutBrowserTask = (TextView) findViewById(R.id.layout_browser_task);
        layoutMyTask = (TextView) findViewById(R.id.layout_my_task);
        layoutInbox = (TextView) findViewById(R.id.layout_inbox);
        layoutOther = (TextView) findViewById(R.id.layout_other);
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
        layoutBrowserTask.setOnClickListener(this);
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
