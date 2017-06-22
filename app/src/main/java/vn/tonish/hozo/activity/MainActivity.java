package vn.tonish.hozo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.BlockDialog;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;
import vn.tonish.hozo.fragment.SettingFragment;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
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
    private TextViewHozo tvCountMsg;

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

        tvCountMsg = (TextViewHozo) findViewById(R.id.tv_count_new_push);
    }

    @Override
    protected void initData() {

        showFragment(R.id.layout_container, SelectTaskFragment.class, false, new Bundle(), TransitionScreen.FADE_IN);
        updateMenuUi(1);

        layoutPostATask.setOnClickListener(this);
        layoutBrowserTask.setOnClickListener(this);
        layoutMyTask.setOnClickListener(this);
        layoutInBox.setOnClickListener(this);
        layoutOther.setOnClickListener(this);

        Intent intentPush = getIntent();
        if (intentPush.hasExtra(Constants.NOTIFICATION_EXTRA)) {
            Notification notification = (Notification) intentPush.getSerializableExtra(Constants.NOTIFICATION_EXTRA);

            if (notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_USER)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_TASK)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_COMMENT)) {
//                DialogUtils.showOkDialog(this, getString(R.string.app_name), notification.getContent(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
//                            @Override
//                            public void onSubmit() {
//
//                            }
//                        }
//                );

                BlockDialog blockDialog = new BlockDialog(MainActivity.this);
                blockDialog.showView();
                blockDialog.updateContent(notification.getContent());

            } else if (notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_TASK) || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_COMMENT)) {
                BlockDialog blockDialog = new BlockDialog(MainActivity.this);
                blockDialog.showView();
                blockDialog.updateContent(notification.getContent());
            } else {
                int taskId = notification.getTaskId();
                Intent intent = new Intent(this, TaskDetailActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskId);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
            }

        } else if (intentPush.hasExtra(Constants.TASK_ID_EXTRA)) {
            int taskId = intentPush.getIntExtra(Constants.TASK_ID_EXTRA, 0);
            Intent intent = new Intent(this, TaskDetailActivity.class);
            intent.putExtra(Constants.TASK_ID_EXTRA, taskId);
            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra(Constants.NOTIFICATION_EXTRA)) {
            Notification notification = (Notification) intent.getSerializableExtra(Constants.NOTIFICATION_EXTRA);

            if (notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_USER)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_TASK)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_COMMENT)) {
//                DialogUtils.showOkDialog(this, getString(R.string.app_name), notification.getContent(), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
//                            @Override
//                            public void onSubmit() {
//
//                            }
//                        }
//                );

                BlockDialog blockDialog = new BlockDialog(MainActivity.this);
                blockDialog.showView();
                blockDialog.updateContent(notification.getContent());

            } else if (notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_TASK) || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_COMMENT)) {
                BlockDialog blockDialog = new BlockDialog(MainActivity.this);
                blockDialog.showView();
                blockDialog.updateContent(notification.getContent());
            } else {
                int taskId = notification.getTaskId();
                Intent intentDetail = new Intent(this, TaskDetailActivity.class);
                intentDetail.putExtra(Constants.TASK_ID_EXTRA, taskId);
                startActivity(intentDetail, TransitionScreen.RIGHT_TO_LEFT);
            }

        } else if (intent.hasExtra(Constants.TASK_ID_EXTRA)) {
            int taskId = intent.getIntExtra(Constants.TASK_ID_EXTRA, 0);
            Intent intentDetail = new Intent(this, TaskDetailActivity.class);
            intentDetail.putExtra(Constants.TASK_ID_EXTRA, taskId);
            startActivity(intentDetail, TransitionScreen.RIGHT_TO_LEFT);
        }
    }


    @Override
    protected void resumeData() {
        updateCountMsg();
        registerReceiver(broadcastPushCount, new IntentFilter(Constants.BROAD_CAST_PUSH_COUNT));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastPushCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.layout_post_a_task:

                Intent intentRefresh = new Intent();
                intentRefresh.setAction(Constants.BROAD_CAST_REFRESH_CATEGORY);
                sendBroadcast(intentRefresh);

                if (tabIndex == 1) break;

                showFragment(R.id.layout_container, SelectTaskFragment.class, false, new Bundle(), TransitionScreen.LEFT_TO_RIGHT);
                tabIndex = 1;
                updateMenuUi(1);
                break;

            case R.id.layout_browser_task:
                if (tabIndex == 2) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_SEARCH);
                    sendBroadcast(intentAnswer);
                    break;
                }

                if (tabIndex > 2) {
                    showFragment(R.id.layout_container, BrowseTaskFragment.class, false, new Bundle(), TransitionScreen.LEFT_TO_RIGHT);
                } else {
                    showFragment(R.id.layout_container, BrowseTaskFragment.class, false, new Bundle(), TransitionScreen.RIGHT_TO_LEFT);
                }
                tabIndex = 2;
                updateMenuUi(2);
                break;

            case R.id.layout_my_task:
                if (tabIndex == 3) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_MY_TASK);
                    sendBroadcast(intentAnswer);
                    break;
                }

                if (tabIndex > 3) {
                    showFragment(R.id.layout_container, MyTaskFragment.class, false, new Bundle(), TransitionScreen.LEFT_TO_RIGHT);
                } else {
                    showFragment(R.id.layout_container, MyTaskFragment.class, false, new Bundle(), TransitionScreen.RIGHT_TO_LEFT);
                }
                tabIndex = 3;
                updateMenuUi(3);
                break;

            case R.id.layout_inbox:

                if (tabIndex == 4) {
                    Intent intentAnswer = new Intent();
                    intentAnswer.setAction(Constants.BROAD_CAST_SMOOTH_TOP_INBOX);
                    sendBroadcast(intentAnswer);
                    break;
                }

                if (tabIndex > 4) {
                    showFragment(R.id.layout_container, InboxFragment.class, false, new Bundle(), TransitionScreen.LEFT_TO_RIGHT);
                } else {
                    showFragment(R.id.layout_container, InboxFragment.class, false, new Bundle(), TransitionScreen.RIGHT_TO_LEFT);
                }
                tabIndex = 4;
                updateMenuUi(4);
                break;

            case R.id.layout_other:
                if (tabIndex == 5) break;

                showFragment(R.id.layout_container, SettingFragment.class, false, new Bundle(), TransitionScreen.RIGHT_TO_LEFT);
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

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Utils.showLongToast(this, getString(R.string.message_exit_app), true, false);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void updateCountMsg() {
        int pushCount = PreferUtils.getNewPushCount(this);

        if (pushCount == 0) {
            tvCountMsg.setVisibility(View.GONE);
        } else {
            tvCountMsg.setVisibility(View.VISIBLE);
            tvCountMsg.setText(String.valueOf(PreferUtils.getNewPushCount(this)));
        }
    }

    private final BroadcastReceiver broadcastPushCount = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateCountMsg();
        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
