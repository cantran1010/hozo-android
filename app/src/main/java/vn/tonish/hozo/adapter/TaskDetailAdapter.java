package vn.tonish.hozo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.tonish.hozo.fragment.task_detail.TaskDetailTab1Fragment;
import vn.tonish.hozo.fragment.task_detail.TaskDetailTab2Fragment;
import vn.tonish.hozo.fragment.task_detail.TaskDetailTab3Fragment;

/**
 * Created by LongBui on 8/22/17.
 */

public class TaskDetailAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public TaskDetailAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TaskDetailTab1Fragment();
            case 1:
                return new TaskDetailTab2Fragment();
            case 2:
                return new TaskDetailTab3Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
