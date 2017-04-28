package vn.tonish.hozo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CategoryAdapter;
import vn.tonish.hozo.model.Category;

/**
 * Created by LongBui on 4/4/2017.
 */

public class SelectTaskFragment extends BaseFragment {

    private RecyclerView rcvTask;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categories = new ArrayList<>();
    protected LinearLayoutManager linearLayoutManager;

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

        //fake data
        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Việc công trình");
        category1.setDescription("Các công việc như : Bốc dỡ,lắp đặt,thợ phụ");
        category1.setPresentPath("http://palmcoastcomputerrepair.net/wp-content/uploads/2014/08/on-line-computer-repair-options.jpg");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Việc công nghệ");
        category2.setDescription("Các công việc như : Cài đặt máy tính,lắp máy in");
        category2.setPresentPath("https://cdn.pixabay.com/photo/2015/12/22/04/00/edit-1103598_960_720.png");

        categories.add(category1);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);
        categories.add(category2);

        categoryAdapter = new CategoryAdapter(categories);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(categoryAdapter);
    }

    @Override
    protected void resumeData() {

    }

}
