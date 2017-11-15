package vn.tonish.hozo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.tonish.hozo.fragment.ChatFragment;
import vn.tonish.hozo.fragment.SystemNotificationFragment;
import vn.tonish.hozo.fragment.NewTaskAlertNotificationFragment;

/**
 * Created by LongBui on 8/22/17.
 */

public class NotifyFragmentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private SystemNotificationFragment tab1;
    private ChatFragment tab2;
    private NewTaskAlertNotificationFragment tab3;

    public NotifyFragmentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                if (tab1 == null)
                    tab1 = new SystemNotificationFragment();
                return tab1;
            case 1:
                if (tab2 == null)
                    tab2 = new ChatFragment();
                return tab2;
            case 2:
                if (tab3 == null)
                    tab3 = new NewTaskAlertNotificationFragment();
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
