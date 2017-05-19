package vn.tonish.hozo.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.setting.TaskTypeActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.GenderDialog;
import vn.tonish.hozo.dialog.PriceSettingDialog;
import vn.tonish.hozo.dialog.RadiusSettingDialog;
import vn.tonish.hozo.model.AddvanceSetting;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;
import static vn.tonish.hozo.common.Constants.REQUEST_CODE_TASK_TYPE;
import static vn.tonish.hozo.database.manager.CategoryManager.insertIsSelected;
import static vn.tonish.hozo.network.DataParse.convertSettingToSettingEntiny;
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
    private long minWorkerRate;
    private long maxWorkerRate;
    private String mGender;
    private int mRadius;


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
        setDefaultAddvanceSetting();
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
                setPrice();
                break;
            case R.id.tab_location:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    LogUtils.e(TAG, e.toString());
                } catch (GooglePlayServicesNotAvailableException e) {
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
        AddvanceSetting addvanceSetting = new AddvanceSetting();
        addvanceSetting.setUserId(UserManager.getUserLogin().getId());
        addvanceSetting.setCategories(mCategory.getCategories());
        addvanceSetting.setMinWorkerRate(minWorkerRate);
        addvanceSetting.setMaxWorkerRate(maxWorkerRate);
        addvanceSetting.setLatitude(lat);
        addvanceSetting.setLongitude(lng);
        addvanceSetting.setGender(mGender);
        addvanceSetting.setRadius(mRadius);
        SettingEntiny settingEntiny = new SettingEntiny();
        settingEntiny = convertSettingToSettingEntiny(addvanceSetting);
        SettingManager.insertSetting(settingEntiny, this);
        finish();


    }

    private void reset() {
        setDefaultAddvanceSetting();

    }

    private void setRadius() {
        final RadiusSettingDialog radiusSettingDialog = new RadiusSettingDialog(this);
        radiusSettingDialog.setRadiusDialogListener(new RadiusSettingDialog.RadiusDialogListener() {
            @Override
            public void onRadiusDialogLister(String radius) {
                tvRadius.setText(radius);
                mRadius = Integer.valueOf((radius.trim().replace(".", "")).replace("km", "").replace(" ", ""));
            }
        });
        radiusSettingDialog.showView();

    }

    private void setGender() {
        final GenderDialog ageDialog = new GenderDialog(this);
        ageDialog.setAgeDialogListener(new GenderDialog.AgeDialogListener() {
            @Override
            public void onAgeDialogLister(String gender) {
                tvGender.setText(gender);
                mGender = gender;

            }
        });

        ageDialog.showView();

    }

    private void setPrice() {
        final PriceSettingDialog priceSettingDialog = new PriceSettingDialog(this);
        priceSettingDialog.setPriceDialogListener(new PriceSettingDialog.PriceDialogListener() {
            @Override
            public void onPriceDialogLister(long minPrice, long maxPrice) {
                tvPrice.setText(formatNumber(minPrice) + " - " + formatNumber(maxPrice));
                minWorkerRate = minPrice;
                maxWorkerRate = maxPrice;
            }
        });
        priceSettingDialog.showView();


    }

    private String getNameRealmCategorys(RealmList<CategoryEntity> realmList) {
        String name = "";
        for (int i = 0; i < realmList.size(); i++) {
            name = name + realmList.get(i).getName() + "-";
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

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);

                if (returnedAddress.getMaxAddressLineIndex() > 1) {
                    strAdd = returnedAddress.getAddressLine(returnedAddress.getMaxAddressLineIndex() - 1);
                }

                if (returnedAddress.getMaxAddressLineIndex() > 2) {
                    strAdd = returnedAddress.getAddressLine(returnedAddress.getMaxAddressLineIndex() - 1);
                }

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    private void setDataforView() {
        SettingEntiny settingEntiny = new SettingEntiny();
        settingEntiny = SettingManager.getSettingEntiny(this);
        tvWorkType.setText(getNameRealmCategorys(settingEntiny.getCategoryEntities()));
        tvPrice.setText(formatNumber(settingEntiny.getMinWorkerRate()) + " - " + formatNumber(settingEntiny.getMaxWorkerRate()));
        tvLocation.setText(getCompleteAddressString(settingEntiny.getLatitude(), settingEntiny.getLongitude()));
        tvRadius.setText(settingEntiny.getRadius() + " km");
        tvGender.setText(settingEntiny.getGender());

    }

    private void setDefaultAddvanceSetting() {
        if (SettingManager.getSettingEntiny(this) == null) {
            RealmList<CategoryEntity> realmList = new RealmList<>();
            SettingEntiny settingEntiny = new SettingEntiny();
            settingEntiny.setUserId(UserManager.getUserLogin().getId());
            setIsSelected(CategoryManager.getAllCategories (this));
            realmList.addAll(CategoryManager.getAllCategories(this));
            settingEntiny.setCategoryEntities(realmList);
            settingEntiny.setLatitude(21.028511);
            lat = 21.028511;
            settingEntiny.setLongitude(105.804817);
            lng = 105.804817;
            settingEntiny.setRadius(50);
            mRadius = 50;
            settingEntiny.setGender(getString(R.string.gender_any));
            mGender = getString(R.string.gender_any);
            settingEntiny.setMinWorkerRate(1000);
            minWorkerRate = 1000;
            settingEntiny.setMaxWorkerRate(100000000);
            maxWorkerRate = 1000000000;
            SettingManager.insertSetting(settingEntiny, this);
            setDataforView();
        } else {
            setDataforView();

        }
    }

    private void setIsSelected(List<CategoryEntity> categoryEntities) {

        for (CategoryEntity categoryEntity : categoryEntities) {
            insertIsSelected(this, categoryEntity, true);
        }
        CategoryManager.insertCategories(this, categoryEntities);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TASK_TYPE && data != null) {
            Category category = (Category) data.getExtras().get(Constants.EXTRA_CATEGORY_ID);
            List<Category> list = new ArrayList<>();
            mCategory = new Category();
            mCategory = category;
            for (Category cat : mCategory.getCategories()) {
                if (cat.isSelected()) {
                    list.add(cat);
                }

            }
            tvWorkType.setText(getNameCategorys((ArrayList<Category>) list));

        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                tvLocation.setText(place.getName());
                lat = place.getLatLng().latitude;
                lng = place.getLatLng().longitude;
                tvLocation.setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
