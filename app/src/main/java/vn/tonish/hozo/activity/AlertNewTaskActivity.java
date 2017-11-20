package vn.tonish.hozo.activity;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.KeyWordAdapter;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.adapter.TaskTypeAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.entity.RealmInt;
import vn.tonish.hozo.database.entity.RealmString;
import vn.tonish.hozo.database.entity.SettingAdvanceEntity;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingAdvanceManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.SettingAdvance;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.APIError;
import vn.tonish.hozo.rest.responseRes.ErrorUtils;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.ExpandableLayout;
import vn.tonish.hozo.view.RadioButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.database.manager.SettingAdvanceManager.getSettingAdvace;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;
import static vn.tonish.hozo.utils.Utils.hideSoftKeyboard;

/**
 * Created by CanTran on 11/15/17.
 */

public class AlertNewTaskActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private TextViewHozo tvDistanceValue;
    private RelativeLayout layoutOptionDistance;
    private ImageView imgFollowArrow, imgCategoryArrow, imgTimeArrow, imgDistance, imgKeyword, imgPrice;
    private TextViewHozo tvFolowed, tvCategory, tvDateTime, tvDistance, tvPrice;
    private RadioButtonHozo radAllTime, radDate, radAllDistance, radDistanceOption, radAllPrice, rad10, rad100, rad500, radNotiYes, radNotiNo, rdFolowedYes, rdFollowedNo;
    private RecyclerView rcvCategory, rcvKeyword;
    private SeekBar seebarDistance;
    private Animation anim_down, anim_up;
    private ExpandableLayout followExpandableLayout, categoryExpandableLayout, timeExpandableLayout, distanceExpandableLayout, priceExpandableLayout, keywordExpandableLayout;
    private TaskTypeAdapter mAdapter;
    private ArrayList<Category> categories;
    private SettingAdvanceEntity newTaskAlertEntity;
    private TextViewHozo tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday, tvKeyword;
    private int count = 0;
    private String address = "";
    private int distance = 50;
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;
    private String categoryName = "";
    private String strTime = "";
    private KeyWordAdapter keyWordAdapter;
    private ArrayList<String> keywords;
    private EdittextHozo edtKeyword;
    private String mKeyWord = "";
    private int minPrice = 0;
    private int maxPrice = 0;
    private ArrayList<Integer> catIds = new ArrayList<>();
    private ArrayList<Integer> dayOfWeek = new ArrayList<>();
    private ArrayList<Double> locations = new ArrayList<>();
    private boolean isFollowed = true;
    private boolean isNotification = true;
    private LinearLayout layoutContext;

    @Override
    protected int getLayout() {
        return R.layout.activity_alert_new_task;
    }

    @Override
    protected void initView() {
        ImageView btnBack = (ImageView) findViewById(R.id.img_back);
        seebarDistance = (SeekBar) findViewById(R.id.seebar_distance);
        rcvCategory = (RecyclerView) findViewById(R.id.rcv_category);

        layoutContext = (LinearLayout) findViewById(R.id.layout_content);
        followExpandableLayout = (ExpandableLayout) findViewById(R.id.follow_expandable_layout);
        categoryExpandableLayout = (ExpandableLayout) findViewById(R.id.category_expandable_layout);
        timeExpandableLayout = (ExpandableLayout) findViewById(R.id.layout_detail_time);
        distanceExpandableLayout = (ExpandableLayout) findViewById(R.id.layout_detail_distance);
        priceExpandableLayout = (ExpandableLayout) findViewById(R.id.layout_detail_price);
        keywordExpandableLayout = (ExpandableLayout) findViewById(R.id.layout_detail_keyword);


        RelativeLayout layoutFollow = (RelativeLayout) findViewById(R.id.layout_follow);
        RelativeLayout layoutCategory = (RelativeLayout) findViewById(R.id.layout_category);
        RelativeLayout layoutDateTime = (RelativeLayout) findViewById(R.id.layout_date_time);
        RelativeLayout layoutDistance = (RelativeLayout) findViewById(R.id.layout_distance);
        RelativeLayout layoutPrice = (RelativeLayout) findViewById(R.id.layout_price);
        RelativeLayout layoutKeyword = (RelativeLayout) findViewById(R.id.layout_keyword);
        layoutOptionDistance = (RelativeLayout) findViewById(R.id.layout_option_distance);

        tvFolowed = (TextViewHozo) findViewById(R.id.tv_follow);
        tvCategory = (TextViewHozo) findViewById(R.id.tv_category);
        tvDateTime = (TextViewHozo) findViewById(R.id.tv_time);
        tvDistance = (TextViewHozo) findViewById(R.id.tv_distance);
        tvDistanceValue = (TextViewHozo) findViewById(R.id.tv_distance_value);
        tvPrice = (TextViewHozo) findViewById(R.id.tv_price);
        ImageView btnSend = (ImageView) findViewById(R.id.btn_send);
        tvKeyword = (TextViewHozo) findViewById(R.id.tv_keyword);

        edtKeyword = (EdittextHozo) findViewById(R.id.edt_keyword);

        imgFollowArrow = (ImageView) findViewById(R.id.img_follow_arrow);
        imgCategoryArrow = (ImageView) findViewById(R.id.img_category_arrow);
        imgTimeArrow = (ImageView) findViewById(R.id.img_time_arrow);
        imgDistance = (ImageView) findViewById(R.id.img_distance);
        imgPrice = (ImageView) findViewById(R.id.img_price);
        imgKeyword = (ImageView) findViewById(R.id.img_keyword);


        RadioGroup radioFollowed = (RadioGroup) findViewById(R.id.radio_follow);
        RadioGroup radioTime = (RadioGroup) findViewById(R.id.radio_time);
        RadioGroup radioDistance = (RadioGroup) findViewById(R.id.radio_distance);
        RadioGroup radioPrice = (RadioGroup) findViewById(R.id.radio_price);
        RadioGroup radioNotification = (RadioGroup) findViewById(R.id.radio_notification);
        radAllTime = (RadioButtonHozo) findViewById(R.id.radio_all_time);
        radDate = (RadioButtonHozo) findViewById(R.id.radio_date);
        radAllDistance = (RadioButtonHozo) findViewById(R.id.rad_all_distance);
        radDistanceOption = (RadioButtonHozo) findViewById(R.id.rad_distance_option);
        radAllPrice = (RadioButtonHozo) findViewById(R.id.rad_all_price);
        rad10 = (RadioButtonHozo) findViewById(R.id.rad_10_100);
        rad100 = (RadioButtonHozo) findViewById(R.id.rad_100_500);
        rad500 = (RadioButtonHozo) findViewById(R.id.rad_500);
        radNotiYes = (RadioButtonHozo) findViewById(R.id.rd_notification_yes);
        radNotiNo = (RadioButtonHozo) findViewById(R.id.rd_notification_no);
        rdFolowedYes = (RadioButtonHozo) findViewById(R.id.rd_folowed_yes);
        rdFollowedNo = (RadioButtonHozo) findViewById(R.id.rd_followed_no);


        rcvCategory = (RecyclerView) findViewById(R.id.rcv_category);
        rcvKeyword = (RecyclerView) findViewById(R.id.rcv_keyword);


        tvMonday = (TextViewHozo) findViewById(R.id.tv_monday);
        tvTuesday = (TextViewHozo) findViewById(R.id.tv_tuesday);
        tvWednesday = (TextViewHozo) findViewById(R.id.tv_wednesday);
        tvThursday = (TextViewHozo) findViewById(R.id.tv_thursday);
        tvFriday = (TextViewHozo) findViewById(R.id.tv_friday);
        tvSaturday = (TextViewHozo) findViewById(R.id.tv_saturday);
        tvSunday = (TextViewHozo) findViewById(R.id.tv_sunday);

        tvMonday.setOnClickListener(this);
        tvTuesday.setOnClickListener(this);
        tvWednesday.setOnClickListener(this);
        tvThursday.setOnClickListener(this);
        tvFriday.setOnClickListener(this);
        tvSaturday.setOnClickListener(this);
        tvSunday.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        layoutFollow.setOnClickListener(this);
        layoutCategory.setOnClickListener(this);
        layoutDateTime.setOnClickListener(this);
        layoutDistance.setOnClickListener(this);
        layoutPrice.setOnClickListener(this);
        layoutKeyword.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        radioFollowed.setOnCheckedChangeListener(this);
        radioTime.setOnCheckedChangeListener(this);
        radioDistance.setOnCheckedChangeListener(this);
        radioPrice.setOnCheckedChangeListener(this);
        radioNotification.setOnCheckedChangeListener(this);

    }

    @Override
    protected void initData() {
        LogUtils.d(TAG, "get setting from realm" + getSettingAdvace());
        anim_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_down);
        anim_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate_up);
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autocompleteView = (AutoCompleteTextView)
                findViewById(R.id.edt_address);

        autocompleteView.setThreshold(1);

        // Register a listener that receives callbacks when a suggestion has been selected
        autocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        final AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
//                .setCountry("VN")
                .build();

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, null,
                autocompleteFilter);
        autocompleteView.setAdapter(placeAutocompleteAdapter);
        createListCategory();
        getDataforView();
        seebarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                radDistanceOption.setChecked(true);
                if (i < 1)
                    seekBar.setProgress(1);
                tvDistanceValue.setText(getString(R.string.distance, seekBar.getProgress()));
                tvDistance.setText(getString(R.string.distance, seekBar.getProgress()));
                distance = seekBar.getProgress();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private final AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item != null ? item.getPlaceId() : null;
            final CharSequence primaryText = item != null ? item.getPrimaryText(null) : null;

            LogUtils.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            LogUtils.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    private final ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                LogUtils.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            try {
                // Get the Place object from the buffer.
                final Place place = places.get(0);
                double lat = place.getLatLng().latitude;
                double lon = place.getLatLng().longitude;
                LogUtils.e(TAG, "Place address : " + place.getAddress() + "-" + lat + "-" + lon);
                if (lat != 0 && lon != 0) {
                    locations = new ArrayList<>();
                    locations.add(0, lat);
                    locations.add(1, lon);
                }
                address = autocompleteView.getText().toString();
                autocompleteView.setError(null);
                places.release();
                hideKeyBoard(AlertNewTaskActivity.this);

            } catch (Exception e) {
                Utils.showLongToast(AlertNewTaskActivity.this, getString(R.string.post_task_map_get_location_error_next), true, false);
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(AlertNewTaskActivity.this);
        googleApiClient.disconnect();
    }


    private void getDataforView() {
        if (getSettingAdvace() == null) {
            getSettingAdvanceFromeServer();
        } else {
            newTaskAlertEntity = getSettingAdvace();
            setDataForView();
        }
    }

    private void createListCategory() {
        rcvCategory.setLayoutManager(new LinearLayoutManager(this));
        categories = new ArrayList<>();
        Category cat = new Category();
        cat.setId(0);
        cat.setName(getString(R.string.hozo_all));
        cat.setSelected(false);
        categories.add(0, cat);
        if (CategoryManager.getAllCategories() != null)
            categories.addAll(DataParse.convertListCategoryEntityToListCategory(CategoryManager.getAllCategories()));
        for (Category category : categories
                ) {
            category.setSelected(false);
        }
        mAdapter = new TaskTypeAdapter(categories);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcvCategory.setLayoutManager(layoutManager);
        rcvCategory.setAdapter(mAdapter);
        mAdapter.setListener(new TaskTypeAdapter.CategoryListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                catIds = new ArrayList<>();
                if (buttonView.getText().equals(getString(R.string.hozo_all))) {
                    categoryName = getString(R.string.hozo_all);
                } else categoryName = getCategoryName();
                tvCategory.setText(categoryName);
                if (categories.size() > 0) {
                    for (Category cat : categories
                            ) {

                        if (cat.getId() != 0 && cat.isSelected()) {
                            catIds.add(cat.getId());
                            if (catIds.size() == categories.size() - 1) catIds.clear();
                        }
                    }
                }


            }
        });
        LogUtils.d(TAG, "categorys get 1 :" + categories.toString());

    }

    private void setDataForView() {
        if (newTaskAlertEntity != null) {
            isNotification = newTaskAlertEntity.isNtaNotification();
            if (isNotification) {
                radNotiYes.setChecked(true);
                layoutContext.setVisibility(View.VISIBLE);
                setNotification();
            } else {
                layoutContext.setVisibility(View.GONE);
                radNotiNo.setChecked(true);
            }

        }
    }

    private void setNotification() {
        // set follow
        setFollowForView();
        // set category
        setCategoryForView();
        // set data for  date time
        setDateTime();
        // set distance
        setDistanceForView();
        // set price
        setPriceForView();
        // set key word
        setKeyWordForView();
    }


    private void setKeyWordForView() {
        keywords = new ArrayList<>();
        if (newTaskAlertEntity.getKeywords() != null && newTaskAlertEntity.getKeywords().size() > 0) {
            LogUtils.d(TAG, "keyword" + newTaskAlertEntity.getKeywords().size());
            for (RealmString realmString : newTaskAlertEntity.getKeywords()
                    ) {
                keywords.add(realmString.getValue());
            }
        }
        keyWordAdapter = new KeyWordAdapter(this, keywords);
        keyWordAdapter.stopLoadMore();
        RecyclerView.LayoutManager keyWordLayoutManager = new LinearLayoutManager(this);
        rcvKeyword.setLayoutManager(keyWordLayoutManager);
        rcvKeyword.setAdapter(keyWordAdapter);
        keyWordAdapter.setKeyWordListener(new KeyWordAdapter.KeyWordListener() {
            @Override
            public void OnClickListener() {
                setTextForKeyWord();
            }
        });
        setTextForKeyWord();

    }


    private void setPriceForView() {
        if (newTaskAlertEntity.getNtaMinWorkerRate() == 10000 && newTaskAlertEntity.getNtaMaxWorkerRate() == 100000) {
            minPrice = 10000;
            maxPrice = 100000;
            rad10.setChecked(true);
            tvPrice.setText(rad10.getText());
        } else if (newTaskAlertEntity.getNtaMinWorkerRate() == 100000 && newTaskAlertEntity.getNtaMaxWorkerRate() == 500000) {
            minPrice = 100000;
            maxPrice = 500000;
            rad100.setChecked(true);
            tvPrice.setText(rad100.getText());
        } else if (newTaskAlertEntity.getNtaMinWorkerRate() == 500000) {
            maxPrice = 0;
            minPrice = 500000;
            rad500.setChecked(true);
            tvPrice.setText(rad500.getText());
        } else {
            minPrice = 0;
            maxPrice = 0;
            tvPrice.setText(radAllPrice.getText());
            radAllPrice.setChecked(true);
        }

    }

    private void setDistanceForView() {
        hideKeyBoard(this);
        if (newTaskAlertEntity.getNtaLatlon() != null && newTaskAlertEntity.getNtaLatlon().size() > 0) {
            layoutOptionDistance.setVisibility(View.VISIBLE);
            radDistanceOption.setChecked(true);
            distance = newTaskAlertEntity.getNtaDistance();
            locations = new ArrayList<>();
            locations.add(0, newTaskAlertEntity.getNtaLatlon().get(0).getVal());
            locations.add(1, newTaskAlertEntity.getNtaLatlon().get(1).getVal());
            address = newTaskAlertEntity.getNtaAddress();
            seebarDistance.setProgress(newTaskAlertEntity.getNtaDistance());
            tvDistanceValue.setText(getString(R.string.distance, newTaskAlertEntity.getNtaDistance()));
            tvDistance.setText(getString(R.string.distance, newTaskAlertEntity.getNtaDistance()));
        } else {
            layoutOptionDistance.setVisibility(View.GONE);
            locations = new ArrayList<>();
            radAllDistance.setChecked(true);
            distance = seebarDistance.getProgress();
            if (UserManager.getMyUser().getLatitude() != 0 && UserManager.getMyUser().getLongitude() != 0) {
                locations.add(0, UserManager.getMyUser().getLatitude());
                locations.add(1, UserManager.getMyUser().getLongitude());
                address = UserManager.getMyUser().getAddress();
            }
            tvDistance.setText(getString(R.string.hozo_all));
            tvDistanceValue.setText(getString(R.string.distance, seebarDistance.getProgress()));
        }
        autocompleteView.setText(address);

    }


    private void setFollowForView() {
        if (newTaskAlertEntity.isNtaFollowed()) {
            rdFolowedYes.setChecked(true);
            tvFolowed.setText(getString(R.string.yes_all));
            isFollowed = true;
        } else {
            rdFollowedNo.setChecked(true);
            tvFolowed.setText(getString(R.string.no_all));
            isFollowed = false;
        }

    }

    private void setDateTime() {
        strTime = "";
        if (newTaskAlertEntity.getNtaDays() != null && newTaskAlertEntity.getNtaDays().size() > 0) {
            radDate.setChecked(true);
            for (RealmInt anInt : newTaskAlertEntity.getNtaDays()
                    ) {
                switch (anInt.getVal()) {

                    case 1:
                        dayOfWeek.add(1);
                        tvSunday.setBackgroundResource(R.drawable.bg_circle_press);
                        tvSunday.setTextColor(ContextCompat.getColor(this, R.color.white));
                        strTime = strTime + tvSunday.getText() + ", ";
                        break;
                    case 2:
                        dayOfWeek.add(2);
                        tvMonday.setBackgroundResource(R.drawable.bg_circle_press);
                        tvMonday.setTextColor(ContextCompat.getColor(this, R.color.white));
                        strTime = strTime + tvMonday.getText() + ", ";
                        break;
                    case 3:
                        dayOfWeek.add(3);
                        tvTuesday.setBackgroundResource(R.drawable.bg_circle_press);
                        tvTuesday.setTextColor(ContextCompat.getColor(this, R.color.white));
                        strTime = strTime + tvTuesday.getText() + ", ";
                        break;

                    case 4:
                        dayOfWeek.add(4);
                        tvWednesday.setBackgroundResource(R.drawable.bg_circle_press);
                        tvWednesday.setTextColor(ContextCompat.getColor(this, R.color.white));
                        strTime = strTime + tvWednesday.getText() + ", ";
                        break;
                    case 5:
                        dayOfWeek.add(5);
                        tvThursday.setBackgroundResource(R.drawable.bg_circle_press);
                        tvThursday.setTextColor(ContextCompat.getColor(this, R.color.white));
                        strTime = strTime + tvThursday.getText() + ", ";
                        break;
                    case 6:
                        dayOfWeek.add(6);
                        tvFriday.setBackgroundResource(R.drawable.bg_circle_press);
                        tvFriday.setTextColor(ContextCompat.getColor(this, R.color.white));
                        strTime = strTime + tvFriday.getText() + ", ";
                        break;
                    case 7:
                        dayOfWeek.add(7);
                        tvSaturday.setBackgroundResource(R.drawable.bg_circle_press);
                        tvSaturday.setTextColor(ContextCompat.getColor(this, R.color.white));
                        strTime = strTime + tvSaturday.getText() + ", ";
                        break;
                    default:
                        dayOfWeek = new ArrayList<>();
                        clearChooseDay();
                        break;


                }
            }
        } else {
            dayOfWeek = new ArrayList<>();
            strTime = radAllTime.getText().toString();
        }
        if (strTime.endsWith(", ") && strTime.length() > 2) {
            strTime = strTime.substring(0, strTime.length() - 2);
        }
        count = dayOfWeek.size();
        tvDateTime.setText(strTime);
    }


    private void clearChooseDay() {
        dayOfWeek = new ArrayList<>();
        radAllTime.setChecked(true);
        count = 0;
        tvSunday.setBackgroundResource(R.drawable.bg_circle_default);
        tvMonday.setBackgroundResource(R.drawable.bg_circle_default);
        tvTuesday.setBackgroundResource(R.drawable.bg_circle_default);
        tvWednesday.setBackgroundResource(R.drawable.bg_circle_default);
        tvThursday.setBackgroundResource(R.drawable.bg_circle_default);
        tvFriday.setBackgroundResource(R.drawable.bg_circle_default);
        tvSaturday.setBackgroundResource(R.drawable.bg_circle_default);
        tvSunday.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
        tvMonday.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
        tvTuesday.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
        tvWednesday.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
        tvThursday.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
        tvFriday.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
        tvSaturday.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
        strTime = getString(R.string.hozo_all);
    }

    private void setCategoryForView() {
        if (newTaskAlertEntity.getNtaCategory() != null && newTaskAlertEntity.getNtaCategory().size() > 0) {
            LogUtils.d(TAG, "categories size" + newTaskAlertEntity.getNtaCategory().size());
            LogUtils.d(TAG, "categorys get 2:" + categories.toString());
            for (RealmInt realmInt : newTaskAlertEntity.getNtaCategory()
                    ) {
                for (Category category : categories
                        ) {
                    LogUtils.d(TAG, "categories id" + realmInt.getVal());
                    if (category.getId() == realmInt.getVal()) {
                        catIds.add(category.getId());
                        category.setSelected(true);
                    }

                }
            }


        } else {
            catIds = new ArrayList<>();
            categories.get(0).setSelected(true);

        }
        LogUtils.d(TAG, "categorys get 3:" + categories.toString());
        tvCategory.setText(getCategoryName());
    }

    private String getCategoryName() {
        int n = 0;
        StringBuilder name = new StringBuilder();
        for (Category c : categories
                ) {
            if (c.isSelected()) {
                n++;
                name.append(", ").append(c.getName());
            }
        }
        if (n == categories.size() - 1 || name.length() < 2)
            name = new StringBuilder(getString(R.string.hozo_all));
        else name = new StringBuilder(name.substring(2, name.length()));

        return name.toString();
    }

    private void expandableLayout(ExpandableLayout expan, ImageView img) {
        closeExpandale(expan);
        if (expan.isExpanded()) {
            img.startAnimation(anim_down);
        } else {
            img.startAnimation(anim_up);
        }
        expan.toggle();

    }

    private void closeExpandale(ExpandableLayout expan) {
        hideKeyBoard(this);
        if (followExpandableLayout.isExpanded() && expan != followExpandableLayout) {
            followExpandableLayout.toggle();
            imgFollowArrow.startAnimation(anim_down);
        }
        if (categoryExpandableLayout.isExpanded() && expan != categoryExpandableLayout) {
            categoryExpandableLayout.toggle();
            imgCategoryArrow.startAnimation(anim_down);
        }
        if (timeExpandableLayout.isExpanded() && expan != timeExpandableLayout) {
            timeExpandableLayout.toggle();
            imgTimeArrow.startAnimation(anim_down);
        }
        if (distanceExpandableLayout.isExpanded() && expan != distanceExpandableLayout) {
            distanceExpandableLayout.toggle();
            imgDistance.startAnimation(anim_down);
        }
        if (priceExpandableLayout.isExpanded() && expan != priceExpandableLayout) {
            priceExpandableLayout.toggle();
            imgPrice.startAnimation(anim_down);
        }
        if (keywordExpandableLayout.isExpanded() && expan != keywordExpandableLayout) {
            keywordExpandableLayout.toggle();
            imgKeyword.startAnimation(anim_down);
        }
    }

    private void getSettingAdvanceFromeServer() {
        ApiClient.getApiService().getSettingAdvance(UserManager.getUserToken()).enqueue(new Callback<SettingAdvance>() {
            @Override
            public void onResponse(Call<SettingAdvance> call, Response<SettingAdvance> response) {
                LogUtils.d(TAG, "getSettingAdvaceFromeServer code : " + response.code());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    //noinspection ConstantConditions
                    LogUtils.d(TAG, "newTaskAlert activity onResponse : " + response.body().toString());
                    SettingAdvanceManager.insertSettingAdvance(response.body());
                    newTaskAlertEntity = getSettingAdvace();
                    setDataForView();
                    LogUtils.d(TAG, "data setting: " + newTaskAlertEntity.toString());
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(AlertNewTaskActivity.this);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    if (error.status().equalsIgnoreCase(getString(R.string.system_error)))
                        Utils.showLongToast(AlertNewTaskActivity.this, getString(R.string.invalid_error), true, false);
                    else
                        Utils.showLongToast(AlertNewTaskActivity.this, error.message(), true, false);
                }
            }

            @Override
            public void onFailure(Call<SettingAdvance> call, Throwable t) {
                LogUtils.e(TAG, "getSettingAdvaceFromeServer onFailure status code : " + t.getMessage());
                DialogUtils.showRetryDialog(AlertNewTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getSettingAdvanceFromeServer();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
    }

    private void postSettingAdvance() {

        final JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("notification_nta", isNotification);
            if (isNotification) {
                jsonRequest.put("nta_followed", isFollowed);
                if (catIds != null && catIds.size() > 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < catIds.size(); i++)
                        jsonArray.put(catIds.get(i));
                    jsonRequest.put("nta_categories", jsonArray);
                } else jsonRequest.put("nta_categories", new JSONArray());
                jsonRequest.put("nta_worker_rate_min", minPrice);
                jsonRequest.put("nta_worker_rate_max", maxPrice);
                if (keywords != null && keywords.size() > 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < keywords.size(); i++)
                        jsonArray.put(keywords.get(i));
                    jsonRequest.put("nta_keywords", jsonArray);
                } else jsonRequest.put("nta_keywords", new JSONArray());

                if (dayOfWeek != null && dayOfWeek.size() > 0) {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < dayOfWeek.size(); i++)
                        jsonArray.put(dayOfWeek.get(i));
                    jsonRequest.put("nta_worker_days", jsonArray);
                } else jsonRequest.put("nta_worker_days", new JSONArray());

                JSONArray jsonArray = new JSONArray();
                if (locations.size() > 0) {
                    for (int i = 0; i < locations.size(); i++)
                        jsonArray.put(locations.get(i));
                }
                LogUtils.d(TAG, "kt distance" + radDistanceOption.isChecked() + "--" + locations.size() + "--" + distance);
                if (radDistanceOption.isChecked() && locations != null && locations.size() > 0 && distance > 0) {
                    jsonRequest.put("nta_distance", distance);
                    jsonRequest.put("nta_origin_location", jsonArray);

                } else {
                    jsonRequest.put("nta_distance", 0);
                    jsonRequest.put("nta_origin_location", new JSONArray());
                }
                jsonRequest.put("nta_origin_address", address);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "SettingAdvance activity json: " + jsonRequest.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonRequest.toString());
        ApiClient.getApiService().postSettingAdvance(UserManager.getUserToken(), body).enqueue(new Callback<SettingAdvance>() {
            @Override
            public void onResponse(Call<SettingAdvance> call, Response<SettingAdvance> response) {
                if (response.code() == Constants.HTTP_CODE_OK) {
                    //noinspection ConstantConditions
                    LogUtils.d(TAG, "SettingAdvance activity data response : " + response.body().toString());
                    SettingAdvanceManager.insertSettingAdvance(response.body());
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(AlertNewTaskActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            postSettingAdvance();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(AlertNewTaskActivity.this);
                } else {
                    DialogUtils.showRetryDialog(AlertNewTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            postSettingAdvance();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<SettingAdvance> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "postSettingAdvance onFailure status code : " + t.getMessage());
                DialogUtils.showRetryDialog(AlertNewTaskActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        postSettingAdvance();
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
                postSettingAdvance();
                hideKeyBoard(this);
                finish();
                break;
            case R.id.layout_follow:
                expandableLayout(followExpandableLayout, imgFollowArrow);
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
                clickDay(tvMonday);
                break;
            case R.id.tv_tuesday:
                clickDay(tvTuesday);
                break;
            case R.id.tv_wednesday:
                clickDay(tvWednesday);
                break;
            case R.id.tv_thursday:
                clickDay(tvThursday);
                break;
            case R.id.tv_friday:
                clickDay(tvFriday);
                break;
            case R.id.tv_saturday:
                clickDay(tvSaturday);
                break;
            case R.id.tv_sunday:
                clickDay(tvSunday);
                break;
            case R.id.btn_send:
                sendKeyWord();
                break;
            case R.id.tv_default:
                defaultSetting();
                break;
        }

    }

    private void defaultSetting() {
        closeExpandale(null);
        rdFolowedYes.setChecked(true);
        tvFolowed.setText(getString(R.string.hozo_all));
        isFollowed = true;

        catIds = new ArrayList<>();
        categories.get(0).setSelected(true);
        if (categories.size() > 0)
            for (int i = 1; i < categories.size(); i++) {
                categories.get(i).setSelected(false);
            }
        mAdapter.notifyDataSetChanged();
        tvCategory.setText(getString(R.string.hozo_all));

        minPrice = 0;
        maxPrice = 0;
        radAllPrice.setChecked(true);
        tvPrice.setText(getString(R.string.hozo_all));

        clearChooseDay();
        tvDateTime.setText(getString(R.string.hozo_all));

        locations = new ArrayList<>();
        radAllDistance.setChecked(true);
        distance = 50;
        if (UserManager.getMyUser().getLatitude() != 0 && UserManager.getMyUser().getLongitude() != 0) {
            locations.add(0, UserManager.getMyUser().getLatitude());
            locations.add(1, UserManager.getMyUser().getLongitude());
            address = UserManager.getMyUser().getAddress();
            tvDistance.setText(getString(R.string.hozo_all));
        }
//        autocompleteView.setText(address);
        keywords.clear();
        keyWordAdapter.notifyDataSetChanged();
        setTextForKeyWord();
        edtKeyword.setText("");

    }

    private void sendKeyWord() {
        String mKeyWord = edtKeyword.getText().toString().trim();
        if (mKeyWord.isEmpty()) {
            edtKeyword.setError(getString(R.string.keyword_empty));
        } else if (isDuplicate(mKeyWord)) {
            Toast.makeText(this, getString(R.string.keyword_duplicate), Toast.LENGTH_SHORT).show();
        } else {
            keywords.add(mKeyWord);
            keyWordAdapter.notifyItemInserted(keywords.size() - 1);
        }
        edtKeyword.setText("");
        hideSoftKeyboard(this, edtKeyword);
        setTextForKeyWord();

    }

    private boolean isDuplicate(String key) {
        boolean ck = false;
        for (String stKey : keywords
                ) {
            if (key.equalsIgnoreCase(stKey))
                ck = true;
        }
        return ck;
    }

    private void clickDay(TextViewHozo tv) {

        radDate.setChecked(true);
        if (tv.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_default).getConstantState())) {
            LogUtils.d(TAG, "count 1 :" + count);
            if (count == 6) {
                count = 0;
                clearChooseDay();
            } else {
                count++;
                tv.setBackgroundResource(R.drawable.bg_circle_press);
                tv.setTextColor(ContextCompat.getColor(this, R.color.white));
                strTime = strTime.replace(getString(R.string.hozo_all), "") + ", " + tv.getText();
            }
        } else {
            if (count == 1) {
                count = 0;
                clearChooseDay();
            } else {
                count--;
                tv.setBackgroundResource(R.drawable.bg_circle_default);
                tv.setTextColor(ContextCompat.getColor(this, R.color.hozo_bg));
                strTime = strTime.replace(", " + tv.getText(), "").replace(getString(R.string.hozo_all), "");

            }

        }
        if (strTime.startsWith(", ")) strTime = strTime.replace(", ", "");
        tvDateTime.setText(strTime);
        getDayOfWeek();
    }

    private void getDayOfWeek() {
        dayOfWeek.clear();
        if (tvSunday.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_press).getConstantState()))
            dayOfWeek.add(1);
        if (tvMonday.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_press).getConstantState()))
            dayOfWeek.add(2);
        if (tvTuesday.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_press).getConstantState()))
            dayOfWeek.add(3);
        if (tvWednesday.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_press).getConstantState()))
            dayOfWeek.add(4);
        if (tvThursday.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_press).getConstantState()))
            dayOfWeek.add(5);
        if (tvFriday.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_press).getConstantState()))
            dayOfWeek.add(6);
        if (tvSaturday.getBackground().getConstantState().equals(ContextCompat.getDrawable(this, R.drawable.bg_circle_press).getConstantState()))
            dayOfWeek.add(7);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.rd_folowed_yes:
                tvFolowed.setText(getString(R.string.yes_all));
                isFollowed = true;
                break;
            case R.id.rd_followed_no:
                isFollowed = false;
                tvFolowed.setText(getString(R.string.no_all));
                break;
            case R.id.radio_all_time:
                clearChooseDay();
                tvDateTime.setText(radAllTime.getText());
                break;
            case R.id.rad_all_distance:
                distance = 0;
                layoutOptionDistance.setVisibility(View.GONE);
                tvDistance.setText(getString(R.string.hozo_all));
                break;
            case R.id.rad_distance_option:
                distance = seebarDistance.getProgress();
                layoutOptionDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(getString(R.string.distance, seebarDistance.getProgress()));
                break;
            case R.id.rad_all_price:
                minPrice = 0;
                maxPrice = 0;
                tvPrice.setText(radAllPrice.getText());
                break;
            case R.id.rad_10_100:
                minPrice = 10000;
                maxPrice = 100000;
                tvPrice.setText(rad10.getText());
                break;
            case R.id.rad_100_500:
                minPrice = 100000;
                maxPrice = 500000;
                tvPrice.setText(rad100.getText());
                break;
            case R.id.rad_500:
                minPrice = 500000;
                maxPrice = 0;
                tvPrice.setText(rad500.getText());
                break;
            case R.id.rd_notification_yes:
                isNotification = true;
                setNotification();
                layoutContext.setVisibility(View.VISIBLE);

                break;
            case R.id.rd_notification_no:
                layoutContext.setVisibility(View.GONE);
                isNotification = false;
                break;

        }

    }

    private void setTextForKeyWord() {
        StringBuilder strKeyWord = new StringBuilder();
        if (keywords != null)
            for (String realmString : keywords
                    ) {
                strKeyWord.append(", ").append(realmString);
            }
        if (strKeyWord.toString().startsWith(", "))
            mKeyWord = strKeyWord.substring(2, strKeyWord.length());
        if (keywords == null || keywords.size() == 0) mKeyWord = "";
        tvKeyword.setText(mKeyWord);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

