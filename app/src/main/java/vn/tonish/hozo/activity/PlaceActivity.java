package vn.tonish.hozo.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.string.post_task_map_get_location_error_next;

/**
 * Created by LongBui on 7/24/17.
 */

public class PlaceActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = PlaceActivity.class.getSimpleName();
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    private GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;
    private TextViewHozo tvNoAddress;

    @Override
    protected int getLayout() {
        return R.layout.place_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        RelativeLayout layoutClear = (RelativeLayout) findViewById(R.id.layout_clear);
        layoutClear.setOnClickListener(this);

        tvNoAddress = (TextViewHozo) findViewById(R.id.tv_no_address);

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        autocompleteView.setThreshold(1);

        // Register a listener that receives callbacks when a suggestion has been selected
        autocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        final AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("VN")
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
        String address = getIntent().getStringExtra(Constants.EXTRA_ADDRESS);
        autocompleteView.setText(address);
    }

    @Override
    protected void resumeData() {

    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
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
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

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
                LogUtils.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            try {
                Utils.hideKeyBoard(PlaceActivity.this);
                // Get the Place object from the buffer.
                final Place place = places.get(0);

                Intent intent = new Intent();
                intent.putExtra(Constants.LAT_EXTRA, place.getLatLng().latitude);
                intent.putExtra(Constants.LON_EXTRA, place.getLatLng().longitude);
                intent.putExtra(Constants.EXTRA_ADDRESS, autocompleteView.getText().toString());
                setResult(Constants.RESULT_CODE_ADDRESS, intent);
                finish();
                places.release();

//                // Get the Place object from the buffer.
//                final Place place = places.get(0);
//
//                latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
//                getAddress(false);
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
//
//                if (marker == null) {
//                    marker = googleMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(latLng.latitude, latLng.longitude))
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
//                } else
//                    marker.setPosition(latLng);
//
//                Utils.hideKeyBoard(PlaceActivity.this);
//                places.release();
            } catch (Exception e) {
                Utils.showLongToast(PlaceActivity.this, getString(post_task_map_get_location_error_next), true, false);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.layout_clear:
                autocompleteView.setText(getString(R.string.all_space_type));
                autocompleteView.requestFocus();
                break;
        }
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

        LogUtils.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

//        // TODO(Developer): Check error code and notify the user of error state and resolution.
//        Toast.makeText(this,
//                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
//                Toast.LENGTH_SHORT).show();

        Utils.showLongToast(this, getString(R.string.gg_api_error), true, false);
    }
}
