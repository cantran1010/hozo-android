package vn.tonish.hozo.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TouchImageView;

public class PreviewPagerAdapter extends PagerAdapter {
    private ArrayList<String> pagerItems;
    private LayoutInflater inflater;
    private Activity context;
    private static final String TAG = PreviewPagerAdapter.class.getName();

    public interface ItemClickListListener {
        void onClick(int position);
    }

    public PreviewPagerAdapter(Activity context, ArrayList<String> pagerItems) {
        super();
        this.pagerItems = pagerItems;
        this.context = context;

    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LogUtils.d(TAG, "PreviewPagerAdapter instantiateItem position : " + position);
        inflater = LayoutInflater.from(context);
        final RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.item_viewpager_view, container, false);
        final TouchImageView imageView = (TouchImageView) layout.findViewById(R.id.imgNewsFeed);
        Utils.displayImage(context, imageView, pagerItems.get(position));
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount() {
        return pagerItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view.equals(obj);
    }

}