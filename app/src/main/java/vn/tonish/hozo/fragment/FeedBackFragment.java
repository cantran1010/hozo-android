package vn.tonish.hozo.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.FeedBackAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.FeedBack;

/**
 * Created by huyquynh on 4/20/17.
 */

public class FeedBackFragment extends BaseFragment {

    private RecyclerView lvList;
    private FeedBackAdapter feedBackAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<FeedBack> feedBacks;


    public static FeedBackFragment instance(ArrayList<FeedBack> feedBacks) {
        FeedBackFragment feedBackFragment = new FeedBackFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constants.DATA, feedBacks);
        feedBackFragment.setArguments(bundle);
        return feedBackFragment;
    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_feed_back;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        feedBacks = new ArrayList<>();
        feedBackAdapter = new FeedBackAdapter(getActivity(), feedBacks);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        lvList.setLayoutManager(linearLayoutManager);
        lvList.setAdapter(feedBackAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }
}
