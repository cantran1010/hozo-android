package vn.tonish.hozo.fragment;

import android.support.v4.content.ContextCompat;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.setViewBackground;

/**
 * Created by LongBui on 4/4/2017.
 */

public class MyTaskFragment extends BaseFragment implements View.OnClickListener {
    private TextViewHozo layoutWorker, layoutPoster;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_my_work;
    }

    @Override
    protected void initView() {
        layoutWorker = (TextViewHozo) findViewById(R.id.layout_worker);
        layoutPoster = (TextViewHozo) findViewById(R.id.layout_poster);
        selectedTab(1);

    }

    @Override
    protected void initData() {
        layoutWorker.setOnClickListener(this);
        layoutPoster.setOnClickListener(this);
    }

    @Override
    protected void resumeData() {

    }

    private void selectedTab(int position) {
        if (position == 1) {
            layoutWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.green));
            setViewBackground(layoutWorker,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_green));
            layoutPoster.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray));
            setViewBackground(layoutPoster,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_gray));
        }else {
            layoutWorker.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            setViewBackground(layoutWorker,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_gray));
            layoutPoster.setTextColor(ContextCompat.getColor(getActivity(),R.color.green));
            setViewBackground(layoutPoster,ContextCompat.getDrawable(getContext(),R.drawable.bg_border_green));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layout_worker:
                selectedTab(1);
                break;

            case R.id.layout_poster:
                selectedTab(2);
                break;

        }

    }
}
