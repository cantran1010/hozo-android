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
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by LongBui on 4/4/2017.
 */

public class SelectTaskFragment extends BaseFragment {
    private static final String TAG = SelectTaskFragment.class.getSimpleName();
    private RecyclerView rcvTask;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories = new ArrayList<>();
    private List<CategoryEntity> categoryEntities = new ArrayList<>();
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
        getCacheData();
        getCategory();
    }

    private void getCacheData() {
        categoryEntities = CategoryManager.getAllCategories(getContext());
        for (int i = 0; i < categoryEntities.size(); i++) {
            categories.add(DataParse.convertCatogoryEntityToCategory(categoryEntities.get(i)));
        }
        refreshCategory();
    }

    private void getCategory() {

//        ProgressDialogUtils.showProgressDialog(getActivity());

        ApiClient.getApiService().getCategories(UserManager.getUserToken()).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                categories = response.body();
                LogUtils.d(TAG, "getCategories onResponse body : " + response.body());
                LogUtils.d(TAG, "getCategories onResponse status code : " + response.code());
                LogUtils.d(TAG, "getCategories onResponse isSuccessful : " + response.isSuccessful());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    refreshCategory();
                    CategoryManager.deleteAll(getContext());
                    CategoryManager.insertCategories(getContext(),DataParse.convertListCategoryToListCategoryEntity(categories));
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.RefreshToken(getContext(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getCategory();
                        }
                    });
                } else {
                    DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getCategory();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
//                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                LogUtils.e(TAG, "getCategories onFailure status code : " + t.getMessage());
                // show network error dialog

                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getCategory();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
//                ProgressDialogUtils.dismissProgressDialog();
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
                startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.DOWN_TO_UP);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            updateMenuUi(3);
            openFragment(R.id.layout_container, MyTaskFragment.class, false, TransitionScreen.RIGHT_TO_LEFT);
        }
    }
}
