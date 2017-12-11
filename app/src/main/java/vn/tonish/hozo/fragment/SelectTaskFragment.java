package vn.tonish.hozo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.task.PostTaskActivity;
import vn.tonish.hozo.adapter.CategoryAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.fragment.mytask.MyTaskFragment;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;

import static vn.tonish.hozo.database.manager.CategoryManager.checkCategoryById;
import static vn.tonish.hozo.database.manager.CategoryManager.insertIsSelected;

/**
 * Created by LongBui on 4/4/2017.
 */

public class SelectTaskFragment extends BaseFragment {
    private static final String TAG = SelectTaskFragment.class.getSimpleName();
    private RecyclerView rcvTask;
    private final List<Category> categories = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_select_task;
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
        List<CategoryEntity> categoryEntities = CategoryManager.getAllCategories();
        for (int i = 0; i < categoryEntities.size(); i++) {
            categories.add(DataParse.convertCatogoryEntityToCategory(categoryEntities.get(i)));
        }
        refreshCategory();
    }

    private void getCategory() {

        if (CategoryManager.getAllCategories() == null || CategoryManager.getAllCategories().size() == 0)
            ProgressDialogUtils.showHozoProgressDialog(getContext());

        ApiClient.getApiService().getCategories(UserManager.getUserToken()).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                LogUtils.d(TAG, "getCategories onResponse body : " + response.body());
                LogUtils.d(TAG, "getCategories onResponse status code : " + response.code());
                LogUtils.d(TAG, "getCategories onResponse isSuccessful : " + response.isSuccessful());

                if (response.code() == Constants.HTTP_CODE_OK) {
                    categories.clear();
                    for (Category category : response.body())
                        if (category.getStatus().equals(Constants.CATEGORY_ACTIVE))
                            categories.add(category);

                    refreshCategory();
                    inserCategory(response.body());

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getCategory();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(getActivity());
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
                ProgressDialogUtils.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                LogUtils.e(TAG, "getCategories onFailure status code : " + t.getMessage());
                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void inserCategory(List<Category> categoryList) {
        List<CategoryEntity> list;
        if (CategoryManager.getAllCategories().size() == 0) {
            list = DataParse.convertListCategoryToListCategoryEntity(categoryList);
            setIsSelected(list);
            CategoryManager.insertCategories(list);
        } else {
            for (Category category : categoryList) {
                category.setSelected(checkCategoryById(category.getId()));
            }
            list = DataParse.convertListCategoryToListCategoryEntity(categoryList);
            CategoryManager.insertCategories(list);
        }
    }

    private void setIsSelected(List<CategoryEntity> categoryEntities) {
        for (CategoryEntity categoryEntity : categoryEntities) {
            insertIsSelected(categoryEntity);
        }
        CategoryManager.insertCategories(categoryEntities);
    }


    @Override
    protected void resumeData() {
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Constants.BROAD_CAST_REFRESH_CATEGORY));
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshCategory() {
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvTask.setLayoutManager(linearLayoutManager);
        rcvTask.setAdapter(categoryAdapter);
        categoryAdapter.setCategoryAdapterLister(new CategoryAdapter.CategoryAdapterLister() {
            @Override
            public void onCallBack(int position) {
                Intent intent = new Intent(getActivity(), PostTaskActivity.class);
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
            Bundle bundle = new Bundle();
            bundle.putString(Constants.ROLE_EXTRA, Constants.ROLE_POSTER);
            openFragment(R.id.layout_container, MyTaskFragment.class, bundle, false, TransitionScreen.RIGHT_TO_LEFT);
            updateMenuUi(3);
        }
    }


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getCategory();
        }
    };

}
