package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.WorkAdapter;
import vn.tonish.hozo.model.Work;

/**
 * Created by Admin on 4/4/2017.
 */

public class BrowseTaskFragment extends BaseFragment {


    private RecyclerView lvList;
    private WorkAdapter workAdapter;
    private List<Work> works;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected int getLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        linearLayoutManager = new LinearLayoutManager(getActivity());

        // dummy data
        works = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            works.add(new Work());
        }
        workAdapter = new WorkAdapter(getActivity(), works);
        lvList.setLayoutManager(linearLayoutManager);
        lvList.setAdapter(workAdapter);
        createSwipeToRefresh();
        
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    // pls add you listener map picker here
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
