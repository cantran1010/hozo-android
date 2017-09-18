package vn.tonish.hozo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.tonish.hozo.fragment.ChatFragment;
import vn.tonish.hozo.fragment.InboxFragment;

/**
 * Created by LongBui on 8/22/17.
 */

public class NotifyFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public NotifyFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                InboxFragment tab1 = new InboxFragment();
                return tab1;
            case 1:
                ChatFragment tab2 = new ChatFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
