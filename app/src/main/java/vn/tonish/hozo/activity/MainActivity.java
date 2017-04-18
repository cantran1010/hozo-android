package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.HelpFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout layoutPostTask,layoutBrowerTask,layoutMyTask,layoutInbox,layoutOther;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        layoutPostTask = (LinearLayout) findViewById(R.id.layout_post_a_task);
        layoutBrowerTask = (LinearLayout) findViewById(R.id.layout_brower_task);
        layoutMyTask = (LinearLayout) findViewById(R.id.layout_my_task);
        layoutInbox = (LinearLayout) findViewById(R.id.layout_inbox);
        layoutOther = (LinearLayout) findViewById(R.id.layout_other);
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_post_a_task:
                openFragment(R.id.layout_container, SelectTaskFragment.class, false);
                break;

            case R.id.layout_brower_task:
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
