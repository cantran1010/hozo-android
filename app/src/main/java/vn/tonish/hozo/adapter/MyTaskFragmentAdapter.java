package vn.tonish.hozo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.tonish.hozo.fragment.mytask.MyTaskPosterFragment;
import vn.tonish.hozo.fragment.mytask.MyTaskWorkerFragment;

/**
 * Created by CanTran on 07/11/2017.
 */

public class MyTaskFragmentAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = MyTaskFragmentAdapter.class.getSimpleName();
    private final int tabCount;
    private MyTaskPosterFragment myTaskPosterFragment;
    private MyTaskWorkerFragment myTaskWorkerFragment;


    public MyTaskFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.tabCount = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (myTaskPosterFragment == null)
                    myTaskPosterFragment = new MyTaskPosterFragment();
                return myTaskPosterFragment;
            case 1:
                if (myTaskWorkerFragment == null)
                    myTaskWorkerFragment = new MyTaskWorkerFragment();
                return myTaskWorkerFragment;
            default:
                return new MyTaskPosterFragment();
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void onRefreshTab(int pos) {
        if (pos == 0) {
            if (myTaskPosterFragment != null)
                myTaskPosterFragment.onRefresh();
        } else if (myTaskWorkerFragment != null) myTaskWorkerFragment.onRefresh();
    }


    public void search(String query, int pos) {
        if (pos == 0) {
            if (myTaskPosterFragment != null)
                myTaskPosterFragment.search(query);
        } else {
            if (myTaskWorkerFragment != null)
                myTaskWorkerFragment.search(query);
        }
    }

    public void resetState(int pos) {
        if (pos == 0)
            myTaskPosterFragment.endlessRecyclerViewScrollListener.resetState();
        else myTaskWorkerFragment.endlessRecyclerViewScrollListener.resetState();

    }


}
