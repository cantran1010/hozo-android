package vn.tonish.hozo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.tonish.hozo.fragment.MyTaskPosterFragment;
import vn.tonish.hozo.fragment.MyTaskWorkerFragment;

/**
 * Created by CanTran on 07/11/2017.
 */

public class MyTaskFragmentAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public MyTaskFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.tabCount = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyTaskPosterFragment tab1 = new MyTaskPosterFragment();
                return tab1;
            case 1:
                MyTaskWorkerFragment tab2 = new MyTaskWorkerFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
