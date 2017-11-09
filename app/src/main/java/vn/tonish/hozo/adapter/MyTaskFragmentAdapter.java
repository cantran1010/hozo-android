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
    private MyTaskPosterFragment tab1;
    private MyTaskWorkerFragment tab2;


    public MyTaskFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.tabCount = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                tab1 = new MyTaskPosterFragment();
                return tab1;
            case 1:
                tab2 = new MyTaskWorkerFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public void onRefreshTab(int pos) {
        if (pos == 0)
            if (tab1 != null)
                tab1.onRefresh();
            else if (tab2 != null) tab2.onRefresh();
    }


    public void search(String query, int pos) {
        if (pos == 0)
            tab1.search(query);
        else tab2.search(query);

    }

    public void resetState(int pos) {
        if (pos == 0)
            tab1.endlessRecyclerViewScrollListener.resetState();
        else tab2.endlessRecyclerViewScrollListener.resetState();

    }


}
