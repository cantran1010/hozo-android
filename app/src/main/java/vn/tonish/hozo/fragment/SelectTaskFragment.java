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
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;

import static vn.tonish.hozo.database.manager.CategoryManager.checkCategoryById;
import static vn.tonish.hozo.database.manager.CategoryManager.insertIsSelected;

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
        if (SettingManager.getSettingEntiny() == null)
            settingDefault();
    }

    private void getCacheData() {
        categoryEntities = CategoryManager.getAllCategories();
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
                    inserCategory(categories);
//                    CategoryManager.deleteAll();
//                    CategoryManager.insertCategories(DataParse.convertListCategoryToListCategoryEntity(categories));

                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(getActivity(), new NetworkUtils.RefreshListener() {
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

//                DialogUtils.showRetryDialog(getActivity(), new AlertDialogOkAndCancel.AlertDialogListener() {
//                    @Override
//                    public void onSubmit() {
//                        getCategory();
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//                ProgressDialogUtils.dismissProgressDialog();
            }
        });
    }

    private void inserCategory(List<Category> categoryList) {
        List<CategoryEntity> list = new ArrayList<>();
        if (CategoryManager.getAllCategories().size() == 0) {
            list = DataParse.convertListCategoryToListCategoryEntity(categoryList);
            setIsSelected(list);
            CategoryManager.insertCategories(list);
        } else {
            for (Category category : categoryList
                    ) {
                category.setSelected(checkCategoryById(category.getId()));
            }
            list = DataParse.convertListCategoryToListCategoryEntity(categoryList);
            CategoryManager.insertCategories(list);
        }
        LogUtils.d(TAG, "setIsSelected : " + CategoryManager.getAllCategories().toString());

    }

    private void setIsSelected(List<CategoryEntity> categoryEntities) {
        for (CategoryEntity categoryEntity : categoryEntities) {
            insertIsSelected(categoryEntity, true);
        }
        CategoryManager.insertCategories(categoryEntities);
        LogUtils.d(TAG, "setIsSelected null " + CategoryManager.getAllCategories().toString());
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

    private void settingDefault() {
        LogUtils.d(TAG, "settingDefault start");
        SettingEntiny settingEntiny = new SettingEntiny();
        settingEntiny.setUserId(UserManager.getMyUser().getId());
        settingEntiny.setLatitude(21.028511);
        settingEntiny.setLongitude(105.804817);
        settingEntiny.setLocation("Hà Nội");
        settingEntiny.setRadius(0);
        settingEntiny.setGender(getString(R.string.gender_vn_any));
        settingEntiny.setMinWorkerRate(10000);
        settingEntiny.setMaxWorkerRate(100000000);
        SettingManager.insertSetting(settingEntiny);
    }


}
