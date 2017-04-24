package vn.tonish.hozo.activity.other;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.fragment.FeedBackFragment;
import vn.tonish.hozo.model.FeedBack;

/**
 * Created by huyquynh on 4/12/17.
 */

public class ProfileActivity extends BaseActivity {


    // info for user's profile
    protected ImageView img_avatar;
    protected TextView tv_name;
    protected TextView tv_birthday;
    protected TextView tv_address;
    protected TextView tv_phone;

    // pager for tab feedback
    protected ViewPager viewPager;

    // tab1 and tab 2 in viewpager
    protected TextView tab_1, tab_2;

    // adapter for feedback pager
    protected FeedBackPagerAdapter feedBackPagerAdapter;

    // data for feedback tab1 _ tab2 ;
    protected ArrayList<FeedBack> tab1Data;
    protected ArrayList<FeedBack> tab2Data;

    @Override
    protected int getLayout() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        setBackButton();
        setTitleHeader(getString(R.string.profile_tv_header));
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        viewPager = (ViewPager) findViewById(R.id.pagers);
        tab_1 = (TextView) findViewById(R.id.tab_1);
        tab_2 = (TextView) findViewById(R.id.tab_2);

        tab1Data = new ArrayList<>();
        tab2Data = new ArrayList<>();

        feedBackPagerAdapter = new FeedBackPagerAdapter(getSupportFragmentManager(), tab1Data, tab2Data);
        viewPager.setAdapter(feedBackPagerAdapter);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    public class FeedBackPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<FeedBack> tab1Data;
        private ArrayList<FeedBack> tab2Data;

        public FeedBackPagerAdapter(FragmentManager fm, ArrayList<FeedBack> tab1Data, ArrayList<FeedBack> tab2Data) {
            super(fm);
            this.tab1Data = tab1Data;
            this.tab2Data = tab2Data;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FeedBackFragment.instance(tab1Data);
                case 1:
                    return FeedBackFragment.instance(tab2Data);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
