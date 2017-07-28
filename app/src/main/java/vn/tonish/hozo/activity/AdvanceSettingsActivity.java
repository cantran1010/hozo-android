package vn.tonish.hozo.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_COST;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_RADIUS;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_TASK_TYPE;
import static vn.tonish.hozo.common.Constants.RESULT_CODE_COST;
import static vn.tonish.hozo.common.Constants.RESULT_CODE_TASK_TYPE;
import static vn.tonish.hozo.utils.Utils.formatNumber;

/**
 * Created by CanTran on 5/16/17.
 */

public class AdvanceSettingsActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = AdvanceSettingsActivity.class.getSimpleName();
    private TextViewHozo tvWorkType, tvPrice, tvLocation, tvRadius;
    private ButtonHozo btnReset, btnSave;
    private double lat;
    private double lng;
    private Category mCategory;
    private int minWorkerRate = 0;
    private int maxWorkerRate = 0;
    private int mRadius;
    private String strRadius;
    private String strLocation = "";
    private String nameTask;
    private String city = "";

    @Override
    protected int getLayout() {
        return R.layout.activity_advance_settings;
    }

    @Override
    protected void initView() {
        tvWorkType = (TextViewHozo) findViewById(R.id.tv_work_type);
        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
        tvLocation = (TextViewHozo) findViewById(R.id.tv_location);
        tvRadius = (TextViewHozo) findViewById(R.id.tv_radius);
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);
        btnSave = (ButtonHozo) findViewById(R.id.btn_save);
    }

    @Override
    protected void initData() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.layout_setting_category).setOnClickListener(this);
        findViewById(R.id.layout_setting_price).setOnClickListener(this);
        findViewById(R.id.layout_setting_location).setOnClickListener(this);
        findViewById(R.id.layout_setting_radius).setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        setDefaultvalues();
        setDataforView();
    }

    @Override
    protected void resumeData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_setting_category:
                Intent i = new Intent(this, TaskTypeActivity.class);
                i.putExtra(Constants.EXTRA_CATEGORY, mCategory);
                LogUtils.d(TAG, "categories extra" + mCategory.getCategories().toString());
                startActivityForResult(i, Constants.REQUEST_CODE_TASK_TYPE, TransitionScreen.DOWN_TO_UP);
                break;
            case R.id.layout_setting_price:
                Intent i2 = new Intent(this, CostActivity.class);
                i2.putExtra(Constants.EXTRA_MIN_PRICE, minWorkerRate);
                i2.putExtra(Constants.EXTRA_MAX_PRICE, maxWorkerRate);
                startActivityForResult(i2, REQUEST_CODE_COST, TransitionScreen.DOWN_TO_UP);

                break;
            case R.id.layout_setting_location:
                try {
                    AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)
                            .setCountry("VN")
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(autocompleteFilter)
                            .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    LogUtils.e(TAG, e.toString());
                }
                break;
            case R.id.layout_setting_radius:
                setRadius();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_reset:
                reset();
                break;
            case R.id.btn_save:
                save();
                break;
        }
    }

    private void save() {
        SettingEntiny settingEntiny = new SettingEntiny();
        settingEntiny.setUserId(UserManager.getMyUser().getId());
        settingEntiny.setMinWorkerRate(minWorkerRate);
        settingEntiny.setMaxWorkerRate(maxWorkerRate);
        settingEntiny.setLatitude(lat);
        settingEntiny.setLongitude(lng);
        settingEntiny.setRadius(mRadius);
        settingEntiny.setLocation(strLocation);
        settingEntiny.setCity(city);

        LogUtils.d(TAG, "save : settingEntiny : " + settingEntiny.toString());
        SettingManager.insertSetting(settingEntiny);
        CategoryManager.insertCategories(DataParse.convertListCategoryToListCategoryEntity(mCategory.getCategories()));
        setResult(Constants.RESULT_CODE_SETTING, new Intent());
        finish();
    }

    private void reset() {
        resetValues();
        setDataforView();
    }

    private void resetValues() {
        lat = 21.028511;
        lng = 105.804817;
        strLocation = "";
        city = "";
        mRadius = 0;
        strRadius = getString(R.string.radius_everywhere);
        minWorkerRate = 0;
        maxWorkerRate = 0;
        getNameCategorys((ArrayList<Category>) DataParse.convertListCategoryEntityToListCategory(CategoryManager.getAllCategories()));
        for (Category category1 : mCategory.getCategories()
                ) {
            category1.setSelected(true);
        }
    }


    private void setRadius() {
        Intent i = new Intent(this, ShowTaskWithin.class);
        i.putExtra(Constants.REQUEST_EXTRAS_RADIUS, mRadius);
        startActivityForResult(i, Constants.REQUEST_CODE_RADIUS, TransitionScreen.DOWN_TO_UP);
    }


    private void getNameRealmCategorys() {
        String name = "";
        for (CategoryEntity entity : CategoryManager.getAllCategories()
                ) {
            if (entity.isSelected()) {
                name = name + entity.getName() + "-";
            }

        }

        nameTask = name.substring(0, name.lastIndexOf('-'));
    }

    private void getNameCategorys(ArrayList<Category> categoryEntities) {
        String name = "";
        for (int i = 0; i < categoryEntities.size(); i++) {
            name = name + categoryEntities.get(i).getName() + "-";
        }

        nameTask = name.substring(0, name.lastIndexOf('-'));
    }


    private void setDataforView() {
        tvWorkType.setText(nameTask);
        String sPrice = "";
        if (minWorkerRate == 0 && maxWorkerRate == 0)
            sPrice = getString(R.string.more_than) + getString(R.string.all_space_type) + formatNumber(10000);
        else if (minWorkerRate == 0) {
            sPrice = getString(R.string.less_than) + getString(R.string.all_space_type) + formatNumber(maxWorkerRate);
        } else if (maxWorkerRate == 0) {
            sPrice = getString(R.string.more_than) + getString(R.string.all_space_type) + formatNumber(minWorkerRate);
        } else {
            sPrice = formatNumber(minWorkerRate) + " - " + formatNumber(maxWorkerRate);
        }
        tvPrice.setText(sPrice);
        tvLocation.setText(strLocation);
        tvRadius.setText(strRadius);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode: " + requestCode);
        if (requestCode == REQUEST_CODE_TASK_TYPE && resultCode == RESULT_CODE_TASK_TYPE && data != null) {
            Category category = (Category) data.getExtras().get(Constants.EXTRA_CATEGORY_ID);
            if ((category != null ? category.getCategories().size() : 0) > 0) {
                List<Category> list = new ArrayList<>();
                mCategory = category;
                if (mCategory != null && mCategory.getCategories().size() > 0) {
                    for (Category cat : mCategory.getCategories()) {
                        if (cat.isSelected()) {
                            list.add(cat);
                        }

                    }
                } else {
                    mCategory = new Category();
                }
                getNameCategorys((ArrayList<Category>) list);
                tvWorkType.setText(nameTask);
            }

        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getAddress());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
//                strLocation = (String) place.getAddress();
                String address = (String) place.getAddress();

                String[] arrAddress = address.split(",");

                if (arrAddress.length >= 2) {
                    city = arrAddress[arrAddress.length - 2].trim();
                    strLocation = city;
                }

                tvLocation.setText(strLocation);


                LogUtils.d(TAG, "PlaceAutocomplete latlong: " + lat + " , lng : " + lng + " , city : " + city);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            }
        } else if (requestCode == REQUEST_CODE_COST && resultCode == RESULT_CODE_COST && data != null) {
            LogUtils.d(TAG, "REQUEST_CODE_COST" + minWorkerRate + "-" + maxWorkerRate);
            minWorkerRate = (int) data.getExtras().get(Constants.EXTRA_MIN_PRICE);
            maxWorkerRate = (int) data.getExtras().get(Constants.EXTRA_MAX_PRICE);
            String sPrice = "";
            if (minWorkerRate == 0 && maxWorkerRate == 0)
                sPrice = getString(R.string.more_than) + getString(R.string.all_space_type) + formatNumber(10000);
            else if (minWorkerRate == 0) {
                sPrice = getString(R.string.less_than) + getString(R.string.all_space_type) + formatNumber(maxWorkerRate);
            } else if (maxWorkerRate == 0) {
                sPrice = getString(R.string.more_than) + getString(R.string.all_space_type) + formatNumber(minWorkerRate);
            } else {
                sPrice = formatNumber(minWorkerRate) + " - " + formatNumber(maxWorkerRate);
            }
            tvPrice.setText(sPrice);
        } else if (requestCode == REQUEST_CODE_RADIUS && resultCode == Constants.RESULT_RADIUS && data != null) {
            mRadius = (int) data.getExtras().get(Constants.EXTRA_RADIUS);
            if (mRadius == 0) strRadius = getString(R.string.radius_everywhere);
            else
                strRadius = mRadius + getString(R.string.all_space_type) + getString(R.string.km);
            tvRadius.setText(strRadius);

        }
    }

    private void setDefaultvalues() {
        SettingEntiny settingEntiny = SettingManager.getSettingEntiny();
        strLocation = settingEntiny.getLocation();
        city = settingEntiny.getCity();
        minWorkerRate = (int) settingEntiny.getMinWorkerRate();
        maxWorkerRate = (int) settingEntiny.getMaxWorkerRate();
        mCategory = new Category();
        mCategory.setCategories((ArrayList<Category>) DataParse.convertListCategoryEntityToListCategory(CategoryManager.getAllCategories()));
        lat = settingEntiny.getLatitude();
        lng = settingEntiny.getLongitude();
        mRadius = settingEntiny.getRadius();
        if (mRadius == 0) strRadius = getString(R.string.radius_everywhere);
        else
            strRadius = mRadius + getString(R.string.all_space_type) + getString(R.string.km);
        getNameRealmCategorys();
    }


}
