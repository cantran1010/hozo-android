package vn.tonish.hozo.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CategoryAdapter;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;

/**
 * Created by Admin on 4/4/2017.
 */

public class SelectTaskFragment extends BaseFragment {


    private RecyclerView lvList;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private LinearLayoutManager linearLayoutManager;


    @Override
    protected int getLayout() {
        return R.layout.home_fragment;
    }


    @Override
    protected void initView() {

        createSwipeToRefresh();

        lvList = (RecyclerView) findViewById(R.id.lvList);
        categories = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        for (int i = 0; i < 30; i++) {
            categories.add(new Category());
        }
        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        lvList.setLayoutManager(linearLayoutManager);
        lvList.setAdapter(categoryAdapter);
        lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // if data is over , pls addOnSrollListener == null
                Log.e("DATA", page + "  " + totalItemsCount);
                Handler handler = new Handler();
                handler.postDelayed(new TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 30; i++) {
                            categories.add(new Category());
                        }
                        categoryAdapter.notifyDataSetChanged();
                        categoryAdapter.stopLoadMore();
                    }
                }, 2000);
            }
        });

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        // request data and stop refresh view here
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

}
