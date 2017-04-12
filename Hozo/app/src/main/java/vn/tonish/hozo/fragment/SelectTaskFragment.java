package vn.tonish.hozo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CategoryAdapter;
import vn.tonish.hozo.database.entity.Category;

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

        lvList = (RecyclerView) findViewById(R.id.lvList);
        categories = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        for (int i = 0; i < 10; i++) {
            categories.add(new Category());
        }
        categoryAdapter = new CategoryAdapter(getActivity(), categories);
        lvList.setLayoutManager(linearLayoutManager);
        lvList.setAdapter(categoryAdapter);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }
}
