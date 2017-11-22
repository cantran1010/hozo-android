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
                TaskDetailTab1Fragment tab1 = new TaskDetailTab1Fragment();
                return tab1;
            case 1:
                TaskDetailTab2Fragment tab2 = new TaskDetailTab2Fragment();
                return tab2;
            case 2:
                TaskDetailTab3Fragment tab3 = new TaskDetailTab3Fragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
