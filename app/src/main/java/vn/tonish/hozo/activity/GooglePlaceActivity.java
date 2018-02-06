package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.PlaceAdapter;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.model.HozoPlace;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.PlaceReponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by CanTran on 2/1/18.
 */

public class GooglePlaceActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GooglePlaceActivity.class.getSimpleName();
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private PlaceAdapter placedapter;
    private AutoCompleteTextView autocompleteView;
    private List<HozoPlace> hozoPlaces = new ArrayList<>();
    private RecyclerView rcvAdress;
    private Call<PlaceReponse> call;
    private ImageView imgClear;
    private TextViewHozo tvNoAddress;

    @Override
    protected int getLayout() {
        return R.layout.place_activity;
    }

    @Override
    protected void initView() {
        LogUtils.d(TAG, "start GooglePlaceActivity");
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        rcvAdress = (RecyclerView) findViewById(R.id.rcv_address);
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        // Retrieve the AutoCompleteTextView that will display HozoPlace suggestions.
        autocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);
        autocompleteView.setThreshold(1);
        tvNoAddress = (TextViewHozo) findViewById(R.id.tv_no_address);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        imgClear.setOnClickListener(this);
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

        placeAutocompleteAdapter.setOnCountListener(new PlaceAutocompleteAdapter.OnCountListener() {
            @Override
            public void onCount(int count) {
                if (count == 0 && autocompleteView.getText().toString().trim().length() > 0) {
                    tvNoAddress.setVisibility(View.VISIBLE);
                } else {
                    tvNoAddress.setVisibility(View.GONE);
                }
            }
        });
        autocompleteView.setAdapter(placeAutocompleteAdapter);

    }

    @Override
    protected void initData() {
        autocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (autocompleteView.getText().toString().length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setPlaceDefault() {
        placedapter = new PlaceAdapter(GooglePlaceActivity.this, hozoPlaces);
        LinearLayoutManager lvManager = new LinearLayoutManager(this);
        rcvAdress.setLayoutManager(lvManager);
        rcvAdress.setAdapter(placedapter);
        placedapter.setPlaceAdapterLister(new PlaceAdapter.PlaceAdapterLister() {
            @Override
            public void onItemClick(int position) {
                hideKeyBoard(GooglePlaceActivity.this);
                Intent intent = new Intent();
                intent.putExtra(Constants.LAT_EXTRA, hozoPlaces.get(position).getGeometry().getLocation().getLat());
                intent.putExtra(Constants.LON_EXTRA, hozoPlaces.get(position).getGeometry().getLocation().getLng());
                intent.putExtra(Constants.EXTRA_ADDRESS, hozoPlaces.get(position).getAddress());
                setResult(Constants.RESULT_CODE_ADDRESS, intent);
                finish();
            }
        });
    }

    private void getSearchAddress(final String query) {
        LogUtils.d(TAG, "start getSearchAddress" + query);
        Map<String, String> option = new HashMap<>();
        if (query != null) option.put("query", query);
        call = ApiClient.getApiService().getPlaces(UserManager.getUserToken(), option);
        call.enqueue(new Callback<PlaceReponse>() {
            @Override
            public void onResponse(Call<PlaceReponse> call, Response<PlaceReponse> response) {
                LogUtils.d(TAG, "getPlaces code : " + response.code());
                LogUtils.d(TAG, "getPlaces body : " + response.body());
                if (response.code() == Constants.HTTP_CODE_OK) {
                    if (response.body() != null)
                        hozoPlaces.clear();
                    hozoPlaces.addAll(response.body().getHozoPlaces());
                    setPlaceDefault();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(GooglePlaceActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            getSearchAddress(query);
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(GooglePlaceActivity.this);
                }
            }

            @Override
            public void onFailure(Call<PlaceReponse> call, Throwable t) {
                LogUtils.d(TAG, "getSearchAddress error : " + t.getMessage());
                DialogUtils.showRetryDialog(GooglePlaceActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        getSearchAddress(query);
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

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays HozoPlace suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private final AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each HozoPlace suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            LogUtils.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a HozoPlace object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            LogUtils.i(TAG, "Called getPlaceById to get HozoPlace details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private final ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                LogUtils.e(TAG, "HozoPlace query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            try {
                hideKeyBoard(GooglePlaceActivity.this);
                final Place place = places.get(0);
                Intent intent = new Intent();
                intent.putExtra(Constants.LAT_EXTRA, place.getLatLng().latitude);
                intent.putExtra(Constants.LON_EXTRA, place.getLatLng().longitude);
                intent.putExtra(Constants.EXTRA_ADDRESS, place.getAddress());
                setResult(Constants.RESULT_CODE_ADDRESS, intent);
                finish();
                places.release();
            } catch (Exception e) {

            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideKeyBoard(this);
                finish();
                break;

            case R.id.img_clear:
                autocompleteView.setText("");
                autocompleteView.requestFocus();
                break;

        }
    }


    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(GooglePlaceActivity.this);
        if (googleApiClient.isConnected()) googleApiClient.disconnect();
    }


    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        autocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getSearchAddress(autocompleteView.getText().toString().trim());
                if (autocompleteView.getText().toString().trim().isEmpty()) hozoPlaces.clear();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
