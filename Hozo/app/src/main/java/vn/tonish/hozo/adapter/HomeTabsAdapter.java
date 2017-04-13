package vn.tonish.hozo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.tonish.hozo.fragment.BrowseTaskFragment;
import vn.tonish.hozo.fragment.HelpFragment;
import vn.tonish.hozo.fragment.InboxFragment;
import vn.tonish.hozo.fragment.MyTaskFragment;
import vn.tonish.hozo.fragment.SelectTaskFragment;

/**
 * Created by MAC2015 on 4/12/17.
 */

public class HomeTabsAdapter extends FragmentPagerAdapter {

    public HomeTabsAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new SelectTaskFragment();
            case 1:
                return new BrowseTaskFragment();
            case 2:
                return new MyTaskFragment();
            case 3:
                return new InboxFragment();
            case 4:
                return new HelpFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
