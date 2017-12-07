package vn.tonish.hozo.activity.task;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.fragment.postTask.CreateTaskFragment;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by CanTran on 12/6/17.
 */

public class PostTaskActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = PostTaskActivity.class.getSimpleName();
    public Category category;
    public TaskResponse taskResponse = new TaskResponse();
    private ImageView imgMenu, imgSaveDraf;
    private PopupMenu popup;
    public String status;
    public int taskId;
    public String taskType = "";
    private boolean isCopy = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_post_task;
    }

    @Override
    protected void initView() {
        imgSaveDraf = (ImageView) findViewById(R.id.tv_save);

        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(this);
        imgSaveDraf.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        FragmentManager fm = getFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getFragmentManager().getBackStackEntryCount() == 0) finish();
            }
        });
        showFragment(R.id.layout_container, CreateTaskFragment.class, false, new Bundle(), TransitionScreen.FADE_IN);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_TASK)) {
            imgSaveDraf.setVisibility(View.GONE);
            imgMenu.setVisibility(View.VISIBLE);
            showPopup();
            taskResponse = (TaskResponse) intent.getSerializableExtra(Constants.EXTRA_TASK);
            LogUtils.d(TAG, "PostATaskActivity , taskResponse : " + taskResponse.toString());
            taskId = taskResponse.getId();
            if (intent.hasExtra(Constants.TASK_EDIT_EXTRA)) {
                taskType = intent.getStringExtra(Constants.TASK_EDIT_EXTRA);
                switch (taskType) {
                    case Constants.TASK_EDIT:
                        imgSaveDraf.setVisibility(View.GONE);
                        imgMenu.setVisibility(View.GONE);
                        popup.getMenu().findItem(R.id.delete_task).setVisible(false);
                        popup.getMenu().findItem(R.id.save_task).setVisible(true);
                        break;
                    case Constants.TASK_COPY:
                        imgSaveDraf.setVisibility(View.VISIBLE);
                        imgMenu.setVisibility(View.GONE);
                        break;
                    case Constants.TASK_DRAFT:
                        imgSaveDraf.setVisibility(View.GONE);
                        imgMenu.setVisibility(View.VISIBLE);
                        popup.getMenu().findItem(R.id.delete_task).setVisible(true);
                        popup.getMenu().findItem(R.id.save_task).setVisible(true);
                        break;
                }

            }
        } else {
            imgSaveDraf.setVisibility(View.VISIBLE);
            imgMenu.setVisibility(View.GONE);
            category = (Category) intent.getSerializableExtra(Constants.EXTRA_CATEGORY);
        }
    }


    private void showPopup() {
        {
            //Creating the instance of PopupMenu
            popup = new PopupMenu(this, imgMenu);
            popup.getMenuInflater().inflate(R.menu.menu_create_task, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.save_task:
                            status = Constants.CREATE_TASK_STATUS_DRAFT;
//                            doSave();
                            break;

                        case R.id.delete_task:
                            DialogUtils.showOkAndCancelDialog(
                                    PostTaskActivity.this, getString(R.string.title_delete_task), getString(R.string.content_detete_task), getString(R.string.cancel_task_ok),
                                    getString(R.string.cancel_task_cancel), new AlertDialogOkAndCancel.AlertDialogListener() {
                                        @Override
                                        public void onSubmit() {
//                                            doDeleteTask();
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View view) {

    }
}
