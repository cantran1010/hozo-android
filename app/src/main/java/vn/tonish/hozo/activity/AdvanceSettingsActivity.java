package vn.tonish.hozo.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.setting.TaskTypeActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.GenderDialog;
import vn.tonish.hozo.dialog.PriceSettingDialog;
import vn.tonish.hozo.dialog.RadiusSettingDialog;
import vn.tonish.hozo.model.AddvanceSetting;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;

/**
 * Created by CanTran on 5/16/17.
 */

public class AdvanceSettingsActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = AdvanceSettingsActivity.class.getSimpleName();
    private TextViewHozo tvWorkType, tvPrice, tvLocation, tvGender, tvRadius;
    private ButtonHozo btnReset;
    private AddvanceSetting addvanceSetting;
    private LatLng latLng;

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
        addvanceSetting = new AddvanceSetting();


    }

    @Override
    protected void initData() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tab_type).setOnClickListener(this);
        findViewById(R.id.tab_price).setOnClickListener(this);
        findViewById(R.id.tab_location).setOnClickListener(this);
        findViewById(R.id.tab_gender).setOnClickListener(this);
        findViewById(R.id.tab_radius).setOnClickListener(this);
    }

    @Override
    protected void resumeData() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_type:
                startActivityForResult(new Intent(this, TaskTypeActivity.class), Constants.REQUEST_CODE_TASK_TYPE, TransitionScreen.DOWN_TO_UP);
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
        }
    }

    private void reset() {
    }

    private void setRadius() {
        final RadiusSettingDialog radiusSettingDialog = new RadiusSettingDialog(this);
        radiusSettingDialog.setRadiusDialogListener(new RadiusSettingDialog.RadiusDialogListener() {
            @Override
            public void onRadiusDialogLister(String radius) {
                tvRadius.setText(radius);
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

            }
        });

        ageDialog.showView();

    }

    private void setPrice() {
        final PriceSettingDialog priceSettingDialog = new PriceSettingDialog(this);
        priceSettingDialog.setPriceDialogListener(new PriceSettingDialog.PriceDialogListener() {
            @Override
            public void onPriceDialogLister(long minPrice, long maxPrice) {
                tvPrice.setText(minPrice + "-" + maxPrice);
            }
        });
        priceSettingDialog.showView();


    }

    private String getNameCategory(ArrayList<Integer> arrayList) {
        String name = "";
        for (int i = 0; i < arrayList.size(); i++) {
            name = name + CategoryManager.getCategoryById(this, arrayList.get(i)).getName() + " ";

        }


        return name;
    }

    private void setDefaultSetting() {
        addvanceSetting.setUserId(UserManager.getUserLogin(this).getId());


    }

    public List<Integer> getAllId(List<CategoryEntity> categoryEntities) {
        List<Integer> integers = null;
        for (CategoryEntity categoryEntity : categoryEntities) {
            integers.add(categoryEntity.getId());
        }
        return integers;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_TASK_TYPE && data != null) {
            addvanceSetting.setCategoryIds(data.getExtras().getIntegerArrayList(Constants.EXTRA_CATEGORY_ID));
            tvWorkType.setText(getNameCategory(data.getExtras().getIntegerArrayList(Constants.EXTRA_CATEGORY_ID)));
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());
                tvLocation.setText(place.getName());
                latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
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
