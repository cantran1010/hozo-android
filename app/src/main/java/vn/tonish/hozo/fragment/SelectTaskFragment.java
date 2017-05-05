package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PostATaskActivity;
import vn.tonish.hozo.adapter.CategoryAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;

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
        getCategory();
    }

    private void getCategory() {
        NetworkUtils.getRequestVolleyFormData(true, true, true, getActivity(), NetworkConfig.API_CATEGORY, new HashMap<String, String>(), new NetworkUtils.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                try {
                    JSONArray jsonArr = jsonResponse.getJSONObject("data").getJSONArray("categories");
                    for (int i = 0; i < jsonArr.length(); i++) {

                        JSONObject jsonCategory = jsonArr.getJSONObject(i);

                        Category category = new Category();
                        category.setId(jsonCategory.getInt("id"));
                        category.setName(jsonCategory.getString("name"));
                        category.setDescription(jsonCategory.getString("description"));
                        category.setSuggestTitle(jsonCategory.getString("suggest_title"));
                        category.setSuggestDescription(jsonCategory.getString("suggest_description"));
                        category.setPresentPath(jsonCategory.getString("avatar"));

                        categories.add(category);
                    }
                    refreshCategory();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    protected void resumeData() {

    }

    private void refreshCategory() {
        categoryAdapter = new CategoryAdapter(categories);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(categoryAdapter);

        categoryAdapter.setCategoryAdapterLister(new CategoryAdapter.CategoryAdapterLister() {
            @Override
            public void onCallBack(int position) {
                Intent intent = new Intent(getActivity(), PostATaskActivity.class);
                intent.putExtra(Constants.EXTRA_CATEGORY, categories.get(position));
                startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            updateMenuUi(3);
            openFragment(R.id.layout_container, MyTaskFragment.class, false);
        }
    }
}
