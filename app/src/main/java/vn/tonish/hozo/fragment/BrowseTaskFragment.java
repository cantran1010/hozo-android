package vn.tonish.hozo.fragment;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;

/**
 * Created by Admin on 4/4/2017.
 * Edited by huyquynh on 19/4/2017
 */

public class BrowseTaskFragment extends BaseFragment implements View.OnClickListener {
    private ImageView imgSearch, imgLocation, imgControls;
    private boolean checkView = true;

    @Override
    protected int getLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected void initView() {
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgLocation = (ImageView) findViewById(R.id.img_location);
        imgControls = (ImageView) findViewById(R.id.img_controls);
    }

    @Override
    protected void initData() {
        openFragment(R.id.find_task_container, ListTaskFragment.class, false);
        imgControls.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgSearch.setOnClickListener(this);

    }

    @Override
    protected void resumeData() {

    }

    public void getData() {
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_location:
                if (checkView) {
                    openFragment(R.id.find_task_container, MapTaskFragment.class, false);
                    checkView = false;
                } else {
                    openFragment(R.id.find_task_container, ListTaskFragment.class, false);
                    checkView = true;
                }

                break;
        }

    }
}
