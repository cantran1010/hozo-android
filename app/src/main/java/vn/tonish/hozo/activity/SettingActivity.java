package vn.tonish.hozo.activity;

import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.TaskTypeAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.RealmInt;
import vn.tonish.hozo.database.entity.SettingAdvanceEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingAdvanceManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.HozoExpandableRelativeLayout;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.database.manager.SettingAdvanceManager.getSettingAdvace;
import static vn.tonish.hozo.utils.Utils.inserCategory;

/**
 * Created by CanTran on 9/15/17.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private ImageView btnBack;
    private TextViewHozo tvDefault;
    private RelativeLayout layoutStatus, layoutCategory, layoutDateTime, layoutDistance, layoutPrice, layoutKeyword;
    private ImageView imgStatusArrow, imgCategoryArrow, imgTimeArrow, imgDistance, imgKeyword, imgPrice;
    private TextViewHozo tvStatus, tvCategory, tvDateTime, tvCity, tvDistance;
    private RadioGroup radioStatus, radioTime, radioDistance, radioPrice;
    private RadioButton raStatusAll, radStausOpen, radStatusAssign, radAllTime, radDate, radAllDistance, radDistanceOption, radAllPrice, rad10, rad100, rad500;
    private RecyclerView rcvCategory, rcvKeyword;
    private SeekBar seebarDistance;
    private Animation anim_down, anim_up;
    private HozoExpandableRelativeLayout statusExpandableLayout, categoryExpandableLayout, timeExpandableLayout, distanceExpandableLayout, priceExpandableLayout, keywordExpandableLayout;
    private TaskTypeAdapter mAdapter;
    private ArrayList<CategoryEntity> categoryEntities;
    private ArrayList<Category> categories;
    private SettingAdvanceEntity advanceEntity, advanceEntityNew;
    private Category cat;
    private TextViewHozo tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;
    private boolean isMon, isTues, isWed, isThurs, isFri, isSat, isSun;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        btnBack = findViewById(R.id.img_back);
        tvDefault = findViewById(R.id.tv_default);
        seebarDistance = findViewById(R.id.seebar_distance);
        rcvCategory = findViewById(R.id.rcv_category);

        statusExpandableLayout = findViewById(R.id.status_expandable_layout);
        categoryExpandableLayout = findViewById(R.id.category_expandable_layout);
        timeExpandableLayout = findViewById(R.id.layout_detail_time);
        distanceExpandableLayout = findViewById(R.id.layout_detail_distance);
        priceExpandableLayout = findViewById(R.id.layout_detail_price);
        keywordExpandableLayout = findViewById(R.id.layout_detail_keyword);


        layoutStatus = findViewById(R.id.layout_status);
        layoutCategory = findViewById(R.id.layout_category);
        layoutDateTime = findViewById(R.id.layout_date_time);
        layoutDistance = findViewById(R.id.layout_distance);
        layoutPrice = findViewById(R.id.layout_price);
        layoutKeyword = findViewById(R.id.layout_keyword);

        tvStatus = findViewById(R.id.tv_status);
        tvCategory = findViewById(R.id.tv_category);
        tvDateTime = findViewById(R.id.tv_time);
        tvCity = findViewById(R.id.tv_city);
        tvDistance = findViewById(R.id.tv_distance);


        imgStatusArrow = findViewById(R.id.img_status_arrow);
        imgCategoryArrow = findViewById(R.id.img_category_arrow);
        imgTimeArrow = findViewById(R.id.img_time_arrow);
        imgDistance = findViewById(R.id.img_distance);
        imgPrice = findViewById(R.id.img_price);
        imgKeyword = findViewById(R.id.img_keyword);


        radioStatus = findViewById(R.id.radio_status);
        radioTime = findViewById(R.id.radio_time);
        radioDistance = findViewById(R.id.radio_distance);
        radioPrice = findViewById(R.id.radio_price);


        raStatusAll = findViewById(R.id.rd_status_all);
        radAllTime = findViewById(R.id.radio_all_time);
        radDate = findViewById(R.id.radio_date);
        radStausOpen = findViewById(R.id.rd_status_open);
        radStatusAssign = findViewById(R.id.rd_status_assign);
        radAllDistance = findViewById(R.id.rad_all_distance);
        radDistanceOption = findViewById(R.id.rad_distance_option);
        radAllPrice = findViewById(R.id.rad_all_price);
        rad10 = findViewById(R.id.rad_10_100);
        rad100 = findViewById(R.id.rad_100_500);
        rad500 = findViewById(R.id.rad_500);

        rcvCategory = findViewById(R.id.rcv_category);
        rcvKeyword = findViewById(R.id.rcv_keyword);


        tvMonday = findViewById(R.id.tv_monday);
        tvTuesday = findViewById(R.id.tv_tuesday);
        tvWednesday = findViewById(R.id.tv_wednesday);
        tvThursday = findViewById(R.id.tv_thursday);
        tvFriday = findViewById(R.id.tv_friday);
        tvSaturday = findViewById(R.id.tv_saturday);
        tvSunday = findViewById(R.id.tv_sunday);

        tvMonday.setOnClickListener(this);
        tvTuesday.setOnClickListener(this);
        tvWednesday.setOnClickListener(this);
        tvThursday.setOnClickListener(this);
        tvFriday.setOnClickListener(this);
        tvSaturday.setOnClickListener(this);
        tvSunday.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvDefault.setOnClickListener(this);
        layoutStatus.setOnClickListener(this);
        layoutCategory.setOnClickListener(this);
        layoutDateTime.setOnClickListener(this);
        layoutDistance.setOnClickListener(this);
        layoutPrice.setOnClickListener(this);
        layoutKeyword.setOnClickListener(this);
        radioStatus.setOnCheckedChangeListener(this);
    }


    @Override
    protected void initData() {
        advanceEntityNew = new SettingAdvanceEntity();
        LogUtils.d(TAG, "get setting from realm" + getSettingAdvace());
        anim_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_down);
        anim_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_up);
        categoryEntities = new ArrayList<>();
        rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        categories = new ArrayList<>();
        cat = new Category();
        cat.setId(0);
        cat.setName(getString(R.string.hozo_all));
        cat.setSelected(false);
        categories.add(0, cat);
        createListCategory();
        mAdapter = new TaskTypeAdapter(categories);
        rcvCategory.setAdapter(mAdapter);


    }

    private void getDataforView() {

        if (SettingAdvanceManager.getSettingAdvace() == null) {
            getSettingAdvaceFromeServer();
        } else {
            getSettingAdvaceFromeServer();
            advanceEntity = SettingAdvanceManager.getSettingAdvace();
            LogUtils.d(TAG, "data setting: " + advanceEntity.toString());
            setDataForView();
        }


    }

    private void createListCategory() {
        mAdapter = new TaskTypeAdapter(categories);
        do {
            if (CategoryManager.getAllCategories() == null) {
                getCategoryFromeServer();
            } else {
                categories.addAll(DataParse.convertListCategoryEntityToListCategory(CategoryManager.getAllCategories()));
                getDataforView();
            }


        } while (CategoryManager.getAllCategories() == null);


    }

    private void setDataForView() {
        if (advanceEntity != null) {
            setStatus(advanceEntity.getStatus());
            // set category
            setCategoryForView();
            // set data for  date time
            setDateTime();


        }
    }


    private void setStatus(String status) {
        if (status.equalsIgnoreCase(Constants.STATUS_SETTING_OPEN)) {
            radioStatus.check(R.id.rd_status_open);
        } else if (status.equalsIgnoreCase(Constants.STATUS_SETTING_ASSIGED)) {
            radioStatus.check(R.id.rd_status_assign);
        } else {
            radioStatus.check(R.id.rd_status_all);
        }

    }

    private void setDateTime() {
        clearChooseDay();
        if (advanceEntity.getDays().size() > 0) {
            radioTime.check(R.id.radio_date);
            for (RealmInt anInt : advanceEntity.getDays()
                    ) {
                switch (anInt.getVal()) {
                    case 1:
                        isSun = true;
                        tvSunday.setBackgroundResource(R.drawable.bg_circle_press);
                        break;
                    case 2:
                        isMon = true;
                        tvMonday.setBackgroundResource(R.drawable.bg_circle_press);
                        break;
                    case 3:
                        isTues = true;
                        tvTuesday.setBackgroundResource(R.drawable.bg_circle_press);
                        break;
                    case 4:
                        isWed = true;
                        tvWednesday.setBackgroundResource(R.drawable.bg_circle_press);
                        break;
                    case 5:
                        isThurs = true;
                        tvThursday.setBackgroundResource(R.drawable.bg_circle_press);
                        break;
                    case 6:
                        isFri = true;
                        tvFriday.setBackgroundResource(R.drawable.bg_circle_press);
                        break;
                    case 7:
                        isSat = true;
                        tvSaturday.setBackgroundResource(R.drawable.bg_circle_press);
                        break;
                }
            }

        } else {
            radioTime.check(R.id.radio_all_time);
            isSun = false;
            isMon = false;
            isTues = false;
            isWed = false;
            isThurs = false;
            isFri = false;
            isSat = false;
        }
    }


    private void clearChooseDay() {
        tvSunday.setBackgroundResource(R.drawable.bg_circle_default);
        tvMonday.setBackgroundResource(R.drawable.bg_circle_default);
        tvTuesday.setBackgroundResource(R.drawable.bg_circle_default);
        tvWednesday.setBackgroundResource(R.drawable.bg_circle_default);
        tvThursday.setBackgroundResource(R.drawable.bg_circle_default);
        tvFriday.setBackgroundResource(R.drawable.bg_circle_default);
        tvSaturday.setBackgroundResource(R.drawable.bg_circle_default);
    }

//    private void chooseDateTime(String status) {
//        if (status.equalsIgnoreCase(Constants.STATUS_SETTING_OPEN)) {
//            radioStatus.check(R.id.rd_status_open);
//        } else if (status.equalsIgnoreCase(Constants.STATUS_SETTING_ASSIGED)) {
//            radioStatus.check(R.id.rd_status_assign);
//        } else {
//            radioStatus.check(R.id.rd_status_all);
//        }

//    }

    private void setCategoryForView() {
        if (advanceEntity.getCategories().size() > 0) {
            for (RealmInt realmInt : advanceEntity.getCategories()
                    ) {
                for (Category category : categories
                        ) {
                    if (category.getId() == realmInt.getVal()) {
                        category.setSelected(true);


                    }
                }

            }

        } else {
            categories.get(0).setSelected(true);
        }
        mAdapter.notifyDataSetChanged();


    }


    private void expandableLayout(HozoExpandableRelativeLayout expan, ImageView img) {
        if (expan.getCurrentPosition() == expan.getClosePosition()) {
            img.startAnimation(anim_up);
        } else {
            img.startAnimation(anim_down);
        }
        expan.toggle();

    }

    private void getSettingAdvaceFromeServer() {

        ApiClient.getApiService().getSettingAdvance(UserManager.getUserToken()).enqueue(new Callback<SettingAdvanceEntity>() {
            @Override
            public void onResponse(Call<SettingAdvanceEntity> call, Response<SettingAdvanceEntity> response) {
                if (response.code() == Constants.HTTP_CODE_OK) {
                    LogUtils.d(TAG, "getSettingAdvance toString : " + response.body().toString());
                    LogUtils.d(TAG, "getSettingAdvance onResponse status code : " + response.code());
                    advanceEntity = response.body();
                    SettingAdvanceManager.insertSettingAdvace(advanceEntity);
                    setDataForView();
                    LogUtils.d(TAG, "data setting: " + advanceEntity.toString());
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(SettingActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    LogUtils.d(TAG, "getSettingAdvance errorBody" + error.toString());
                    if (error.status().equalsIgnoreCase(getString(R.string.login_status_block)))
                        Utils.showLongToast(SettingActivity.this, getString(R.string.login_block_phone), true, false);
                    else
                        Utils.showLongToast(SettingActivity.this, error.message(), true, false);

                }

            }

            @Override
            public void onFailure(Call<SettingAdvanceEntity> call, Throwable t) {
                LogUtils.e(TAG, "getSettingAdvaceFromeServer onFailure status code : " + t.getMessage());
                DialogUtils.showRetryDialog(SettingActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getSettingAdvaceFromeServer();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void getCategoryFromeServer() {

        if (CategoryManager.getAllCategories() == null || CategoryManager.getAllCategories().size() == 0)

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

                        inserCategory(response.body());

                    } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                        NetworkUtils.refreshToken(SettingActivity.this, new NetworkUtils.RefreshListener() {
                            @Override
                            public void onRefreshFinish() {
                                getCategoryFromeServer();
                            }
                        });
                    } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                        Utils.blockUser(SettingActivity.this);
                    } else {
                        DialogUtils.showRetryDialog(SettingActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                            @Override
                            public void onSubmit() {
                                getCategoryFromeServer();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    LogUtils.e(TAG, "getCategoryFromeServer onFailure status code : " + t.getMessage());
                    DialogUtils.showRetryDialog(SettingActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            getCategoryFromeServer();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            });
    }


    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_status:
                expandableLayout(statusExpandableLayout, imgStatusArrow);
                break;
            case R.id.layout_category:
                expandableLayout(categoryExpandableLayout, imgCategoryArrow);
                break;
            case R.id.layout_date_time:
                expandableLayout(timeExpandableLayout, imgTimeArrow);
                break;
            case R.id.layout_price:
                expandableLayout(priceExpandableLayout, imgPrice);
                break;
            case R.id.layout_distance:
                expandableLayout(distanceExpandableLayout, imgDistance);
                break;
            case R.id.layout_keyword:
                expandableLayout(keywordExpandableLayout, imgKeyword);
                break;
            case R.id.tv_monday:
                clickDay(tvMonday, isMon);
                break;
            case R.id.tv_tuesday:
                clickDay(tvTuesday, isTues);
                break;
            case R.id.tv_wednesday:
                clickDay(tvWednesday, isWed);
                break;
            case R.id.tv_thursday:
                clickDay(tvThursday, isThurs);
                break;
            case R.id.tv_friday:
                clickDay(tvFriday, isFri);
                break;
            case R.id.tv_saturday:
                clickDay(tvSaturday, isSat);
                break;
            case R.id.tv_sunday:
                clickDay(tvSunday, isSun);
                break;
        }

    }

    private void clickDay(TextViewHozo tv, boolean isClick) {
        radioTime.check(R.id.radio_date);
        LogUtils.d(TAG, "click day");
        if (isClick) {
            tv.setBackgroundResource(R.drawable.circle_selector);
            isClick = false;
        } else {
            tv.setBackgroundResource(R.drawable.bg_circle_press);
            isClick = true;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.rd_status_all:
                advanceEntityNew.setStatus("");
                break;
            case R.id.rd_status_open:
                advanceEntityNew.setStatus(Constants.STATUS_SETTING_OPEN);
                break;
            case R.id.rd_status_assign:
                advanceEntityNew.setStatus(Constants.STATUS_SETTING_ASSIGED);

        }

    }
}

