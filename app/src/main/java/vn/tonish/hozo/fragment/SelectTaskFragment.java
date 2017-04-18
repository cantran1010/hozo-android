package vn.tonish.hozo.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.TimerTask;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CategoryAdapter;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;

/**
 * Created by Admin on 4/4/2017.
 */

public class SelectTaskFragment extends BaseFragment {

    private RecyclerView rcvTask;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categories = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int getLayout() {
        return R.layout.select_task_fragment;
    }

    @Override
    protected void initView() {
        rcvTask = (RecyclerView) findViewById(R.id.rcv_task);

    }

    @Override
    protected void initData() {

        createSwipeToRefresh();

        //fake data
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Viec Chan Tay");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("It");

        categories.add(category1);
        categories.add(category2);

        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(categoryAdapter);

        rcvTask.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // if data is over , pls addOnSrollListener == null
                Log.e("DATA", page + "  " + totalItemsCount);
                Handler handler = new Handler();
                handler.postDelayed(new TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            Category category = new Category();
                            category.setName("Name " + i);
                            categories.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                        categoryAdapter.stopLoadMore();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onStopRefresh();
            }
        }, 2000);
    }
}
