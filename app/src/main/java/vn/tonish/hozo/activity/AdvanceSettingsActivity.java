package vn.tonish.hozo.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.setting.CostActivity;
import vn.tonish.hozo.activity.setting.TaskTypeActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.GenderDialog;
import vn.tonish.hozo.dialog.RadiusSettingDialog;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.network.DataParse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_COST;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_TASK_TYPE;
import static vn.tonish.hozo.common.Constants.RESULT_CODE_COST;
import static vn.tonish.hozo.common.Constants.RESULT_CODE_TASK_TYPE;
import static vn.tonish.hozo.utils.Utils.formatNumber;

/**
 * Created by CanTran on 5/16/17.
 */

public class AdvanceSettingsActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = AdvanceSettingsActivity.class.getSimpleName();
    private TextViewHozo tvWorkType, tvPrice, tvLocation, tvGender, tvRadius;
    private ButtonHozo btnReset, btnSave;
    private double lat;
    private double lng;
    private Category mCategory;
    private int minWorkerRate = 0;
    private int maxWorkerRate = 0;
    private int mRadius;
    private int idRadius;
    private String strRadius;
    private String strGender;
    private String strLocation;


    @Override
    protected int getLayout() {
        return R.layout.activity_advance_settings;
    }

    @Override
    protected void initView() {
        tvWorkType = (TextViewHozo) findViewById(R.id.tv_work_type);
        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
        tvLocation = (TextViewHozo) findViewById(R.id.tv_location);
        tvGender = (TextViewHozo) findViewById(R.id.tv_gender);
        tvRadius = (TextViewHozo) findViewById(R.id.tv_radius);
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);
        btnReset = (ButtonHozo) findViewById(R.id.btn_reset);
        btnSave = (ButtonHozo) findViewById(R.id.btn_save);


    }

    @Override
    protected void initData() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tab_type).setOnClickListener(this);
        findViewById(R.id.tab_price).setOnClickListener(this);
        findViewById(R.id.tab_location).setOnClickListener(this);
        findViewById(R.id.tab_gender).setOnClickListener(this);
        findViewById(R.id.tab_radius).setOnClickListener(this);
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
            case R.id.tab_type:
                Intent i = new Intent(this, TaskTypeActivity.class);
                i.putExtra(Constants.EXTRA_CATEGORY, mCategory);
                startActivityForResult(i, Constants.REQUEST_CODE_TASK_TYPE, TransitionScreen.DOWN_TO_UP);
                break;
            case R.id.tab_price:
                Intent i2 = new Intent(this, CostActivity.class);
                i2.putExtra(Constants.EXTRA_MIN_PRICE, minWorkerRate);
                i2.putExtra(Constants.EXTRA_MAX_PRICE, maxWorkerRate);
                startActivityForResult(i2, REQUEST_CODE_COST, TransitionScreen.DOWN_TO_UP);

                break;
            case R.id.tab_location:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    LogUtils.e(TAG, e.toString());
                }
                break;
            case R.id.tab_gender:
                setGender();
                break;
            case R.id.tab_radius:
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
        LogUtils.d(TAG, "latlong: " + lat + "va: " + lng);
        SettingEntiny settingEntiny = new SettingEntiny();
        settingEntiny.setUserId(UserManager.getMyUser().getId());
        settingEntiny.setMinWorkerRate(minWorkerRate);
        settingEntiny.setMaxWorkerRate(maxWorkerRate);
        settingEntiny.setLatitude(lat);
        settingEntiny.setLongitude(lng);
        settingEntiny.setGender(strGender);
        settingEntiny.setRadius(mRadius);
        settingEntiny.setLocation(strLocation);
        SettingManager.insertSetting(settingEntiny);
        CategoryManager.insertCategories(DataParse.convertListCategoryToListCategoryEntity(mCategory.getCategories()));
        finish();


    }

    private void reset() {
        setDefaultvalues();
        setDataforView();

    }


    private void setRadius() {
        final RadiusSettingDialog radiusSettingDialog = new RadiusSettingDialog(this, idRadius);
        radiusSettingDialog.setRadiusDialogListener(new RadiusSettingDialog.RadiusDialogListener() {
            @Override
            public void onRadiusDialogLister(String radius, int index) {
                tvRadius.setText(radius);
                if (!radius.equals(getString(R.string.radius_everywhere)))
                    mRadius = Integer.valueOf((radius.trim().replace(".", "")).replace("km", "").replace(" ", ""));
                else
                    mRadius = 0;
                idRadius = index;
            }
        });
        radiusSettingDialog.showView();

    }

    private void setGender() {
        final GenderDialog ageDialog = new GenderDialog(this, strGender);
        ageDialog.setAgeDialogListener(new GenderDialog.AgeDialogListener() {
            @Override
            public void onAgeDialogLister(String gender) {
                tvGender.setText(gender);
                strGender = gender;
            }
        });

        ageDialog.showView();

    }


    private String getNameRealmCategorys() {
        String name = "";
        for (CategoryEntity entity : CategoryManager.getAllCategories()
                ) {
            if (entity.isSelected()) {
                name = name + entity.getName() + "-";
            }

        }
        return name;
    }

    private String getNameCategorys(ArrayList<Category> categoryEntities) {
        String name = "";
        for (int i = 0; i < categoryEntities.size(); i++) {
            name = name + categoryEntities.get(i).getName() + "-";
        }
        return name;
    }


    private void setDataforView() {
        SettingEntiny settingEntiny = SettingManager.getSettingEntiny();
        tvWorkType.setText(getNameRealmCategorys());
        tvPrice.setText(formatNumber(settingEntiny.getMinWorkerRate()) + " - " + formatNumber(settingEntiny.getMaxWorkerRate()));
        tvLocation.setText(strLocation);
        tvRadius.setText(strRadius);
        tvGender.setText(settingEntiny.getGender());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode: " + requestCode);
        if (requestCode == REQUEST_CODE_TASK_TYPE && resultCode == RESULT_CODE_TASK_TYPE && data != null) {
            Category category = (Category) data.getExtras().get(Constants.EXTRA_CATEGORY_ID);
            if ((category != null ? category.getCategories().size() : 0) > 0) {
                List<Category> list = new ArrayList<>();
                mCategory = new Category();
                mCategory = category;
                for (Category cat : mCategory != null ? mCategory.getCategories() : null) {
                    if (cat.isSelected()) {
                        list.add(cat);
                    }

                }
                tvWorkType.setText(getNameCategorys((ArrayList<Category>) list));
            }

        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
                strLocation = (String) place.getName();
                tvLocation.setText(strLocation);
                LogUtils.d(TAG, "latlong: " + lat + "va: " + lng);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            }
        }

        if (requestCode == REQUEST_CODE_COST && resultCode == RESULT_CODE_COST && data != null) {
            LogUtils.d(TAG, "REQUEST_CODE_COST" + minWorkerRate + "-" + maxWorkerRate);
            minWorkerRate = (int) data.getExtras().get(Constants.EXTRA_MIN_PRICE);
            maxWorkerRate = (int) data.getExtras().get(Constants.EXTRA_MAX_PRICE);
            tvPrice.setText(formatNumber(minWorkerRate) + " - " + formatNumber(maxWorkerRate));

        }
    }

    private void setDefaultvalues() {
        SettingEntiny settingEntiny=SettingManager.getSettingEntiny();
        strLocation = settingEntiny.getLocation();
        minWorkerRate = (int) settingEntiny.getMinWorkerRate();
        maxWorkerRate = (int) settingEntiny.getMaxWorkerRate();
        mCategory = new Category();
        mCategory.setCategories((ArrayList<Category>) DataParse.convertListCategoryEntityToListCategory(CategoryManager.getAllCategories()));
        lat = settingEntiny.getLatitude();
        lng = settingEntiny.getLongitude();
        strGender = settingEntiny.getGender();
        if (settingEntiny.getRadius() == 0) {
            idRadius = R.id.rb_everywhere;
            strRadius = getString(R.string.radius_everywhere);
        } else {
            int rad = settingEntiny.getRadius();
            switch (rad) {
                case 5:
                    idRadius = R.id.rb_5;
                    strRadius = getString(R.string.radius_5km);
                    break;
                case 10:
                    idRadius = R.id.rb_10;
                    strRadius = getString(R.string.radius_10km);
                    break;
                case 15:
                    idRadius = R.id.rb_15;
                    strRadius = getString(R.string.radius_15km);
                    break;
                case 20:
                    idRadius = R.id.rb_20;
                    strRadius = getString(R.string.radius_20km);
                    break;
                case 25:
                    idRadius = R.id.rb_25;
                    strRadius = getString(R.string.radius_25km);
                    break;
                case 50:
                    idRadius = R.id.rb_50;
                    strRadius = getString(R.string.radius_50km);
                    break;
            }

        }

    }


}
