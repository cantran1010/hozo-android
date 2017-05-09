package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PostATaskActivity;
import vn.tonish.hozo.adapter.CategoryAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 4/4/2017.
 */

public class SelectTaskFragment extends BaseFragment {
    private static final String TAG = SelectTaskFragment.class.getSimpleName();
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

        ApiClient.getApiService().getCategories(UserManager.getUserToken(getActivity())).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories = (ArrayList<Category>) response.body();
                LogUtils.d(TAG, "getCategories onResponse body : " + response.body());
                LogUtils.d(TAG, "getCategories onResponse status code : " + response.code());
                LogUtils.d(TAG, "getCategories onResponse isSuccessful : " + response.isSuccessful());

                if (response.code() == 401) {
                    NetworkUtils.RefreshToken(getContext(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getCategory();
                        }
                    });
                } else {
                    refreshCategory();
                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                LogUtils.e(TAG, "getCategories onFailure status code : " + t.getMessage());
                // show network error dialog

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
            openFragment(R.id.layout_container, MyTaskFragment.class, false, true);
        }
    }
}
