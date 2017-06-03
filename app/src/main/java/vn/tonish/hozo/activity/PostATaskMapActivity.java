package vn.tonish.hozo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.PlaceAutocompleteAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.GPSTracker;
import vn.tonish.hozo.utils.LocationProvider;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;

import static vn.tonish.hozo.R.drawable.location;
import static vn.tonish.hozo.R.id.map;

/**
 * Created by LongBui on 4/18/2017.
 */

public class PostATaskMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, LocationProvider.LocationCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = PostATaskMapActivity.class.getSimpleName();
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient googleApiClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private AutoCompleteTextView autocompleteView;
    private static final LatLngBounds BOUNDS_VIETNAM = new LatLngBounds(
            new LatLng(7.000030, 101.837400), new LatLng(23.000030, 108.837400));

    private GoogleMap googleMap;
    private ImageView imgBack;
    private ButtonHozo btnNext;
    private ImageView imgCurrentLocation;
    private ImageView imgZoomIn;
    private ImageView imgZoomOut;

    //    private static double lat = 21.000030;
//    private static double lon = 105.837400;
    private LatLng latLng;
    private LocationProvider mLocationProvider;
    //    private TextViewHozo tvAddress;
    private TaskResponse work;
    //    private HozoLocation location = new HozoLocation();
    private Category category;
    private final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Marker marker;
    private boolean isOnLocation = true;
    private ImageView imgClear;

    @Override
    protected int getLayout() {
        return R.layout.post_a_task_map_activity;
    }

    @Override
    protected void initView() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        autocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        autocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, googleApiClient, BOUNDS_VIETNAM,
                null);
        autocompleteView.setAdapter(placeAutocompleteAdapter);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        ButtonHozo btnNext = (ButtonHozo) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        ImageView imgCurrentLocation = (ImageView) findViewById(R.id.img_current_location);
        imgCurrentLocation.setOnClickListener(this);

        ImageView imgZoomIn = (ImageView) findViewById(R.id.img_map_zoom_in);
        imgZoomIn.setOnClickListener(this);

        ImageView imgZoomOut = (ImageView) findViewById(R.id.img_map_zoom_out);
        imgZoomOut.setOnClickListener(this);

        imgClear = (ImageView) findViewById(R.id.img_clear);
        imgClear.setOnClickListener(this);

//        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);
//        tvAddress.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void initData() {
        work = (TaskResponse) getIntent().getSerializableExtra(Constants.EXTRA_TASK);
        category = (Category) getIntent().getSerializableExtra(Constants.EXTRA_CATEGORY);

        mLocationProvider = new LocationProvider(this, this);
    }

    @Override
    protected void resumeData() {
        if (!isOnLocation) {
//            GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
//            if (!gpsTracker.canGetLocation()) {
            mLocationProvider.connect();
            isOnLocation = true;
//            }
        }
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    private void permissionGranted() {
        LogUtils.d(TAG, "onMapReady start");
        GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
        if (gpsTracker.canGetLocation()) {
            isOnLocation = true;
            LogUtils.d(TAG, "onMapReady canGetLocation");
            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latLng.latitude, latLng.longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));

            getAddress(true);

        } else {
            isOnLocation = false;
            mLocationProvider.connect();
            LogUtils.d(TAG, "onMapReady !!!!!! canGetLocation");
            DialogUtils.showOkDialog(this, getString(R.string.app_name), getString(R.string.msg_err_gps), getString(R.string.ok), new AlertDialogOk.AlertDialogListener() {
                @Override
                public void onSubmit() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != Constants.PERMISSION_REQUEST_CODE
                || grantResults.length == 0
                || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else {
            permissionGranted();
        }
    }

    private void permissionDenied() {
        LogUtils.d(TAG, "permissionDenied start");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LogUtils.d(TAG, "onMapReady start");
        this.googleMap = googleMap;
        checkPermission();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                Utils.hideKeyBoard(PostATaskMapActivity.this);
            }
        });

//        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
//            @Override
//            public void onCameraIdle() {
//                Utils.hideKeyBoard(PostATaskMapActivity.this);
//            }
//        });

        googleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                Utils.hideKeyBoard(PostATaskMapActivity.this);
            }
        });
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
    private AdapterView.OnItemClickListener mAutocompleteClickListener
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
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                LogUtils.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            getAddress(false);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            if (marker == null) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
            } else
                marker.setPosition(latLng);

            Utils.hideKeyBoard(PostATaskMapActivity.this);

//            // Format details of the place for display and show it in a TextView.
//            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                    place.getId(), place.getAddress(), place.getPhoneNumber(),
//                    place.getWebsiteUri()));
//
//            // Display the third party attributions if set.
//            final CharSequence thirdPartyAttribution = places.getAttributions();
//            if (thirdPartyAttribution == null) {
//                mPlaceDetailsAttribution.setVisibility(View.GONE);
//            } else {
//                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
//                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
//            }

            LogUtils.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

    private void getAddress(boolean isAddAddress) {
        try {
            Geocoder geocoder;
            ArrayList<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            Address addRess = addresses.get(0);
            LogUtils.d(TAG, "getAddress address : " + addRess.toString());

            if (isAddAddress) {
                String strReturnedAddress = "";
                for (int i = 0; i < addRess.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress = strReturnedAddress + addRess.getAddressLine(i) + ", ";
                }
                strReturnedAddress = strReturnedAddress.substring(0, strReturnedAddress.length() - 2);
                autocompleteView.setText(strReturnedAddress);
//                tvAddress.setText(strReturnedAddress);
//                tvAddress.setError(null);
            }

            if (addRess.getMaxAddressLineIndex() > 1) {
                work.setCity(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 1));
            }

            if (addRess.getMaxAddressLineIndex() > 2) {
                work.setDistrict(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 2));
            }

            work.setAddress(autocompleteView.getText().toString());
            work.setLatitude(latLng.latitude);
            work.setLongitude(latLng.longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.btn_next:
                doNext();
                break;

            case R.id.img_current_location:
                GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
                if (gpsTracker.canGetLocation()) {
                    latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                    if (marker == null) {
                        marker = googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latLng.latitude, latLng.longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
                    } else
                        marker.setPosition(latLng);

                    getAddress(true);
                    autocompleteView.clearFocus();
                }
                break;

            case R.id.img_map_zoom_in:
                if (latLng != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, googleMap.getCameraPosition().zoom + 1));
                break;

            case R.id.img_map_zoom_out:
                if (latLng != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, googleMap.getCameraPosition().zoom - 1));
                break;

            case R.id.img_clear:
                autocompleteView.setText("");
                break;

//            case R.id.tv_address:
//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                    .build(this);
//                    startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//                    // TODO: Handle the error.
//                    e.printStackTrace();
//                }
//                break;
        }
    }

    private void doNext() {

//        if (tvAddress.getText().toString().trim().equals("")) {
//            tvAddress.requestFocus();
//            tvAddress.setError(getString(R.string.post_a_task_address_error));
//            return;
//        }

        Intent intent = new Intent(PostATaskMapActivity.this, PostATaskFinishActivity.class);
        intent.putExtra(Constants.EXTRA_ADDRESS, location);
        intent.putExtra(Constants.EXTRA_TASK, work);
        intent.putExtra(Constants.EXTRA_CATEGORY, category);
        startActivityForResult(intent, Constants.POST_A_TASK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.POST_A_TASK_REQUEST_CODE && resultCode == Constants.POST_A_TASK_RESPONSE_CODE) {
            setResult(Constants.POST_A_TASK_RESPONSE_CODE);
            finish();
        }

//        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                LogUtils.i(TAG, "Place: " + place.getName());
//
//                tvAddress.setText(place.getName());
//                tvAddress.setError(null);
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
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                // TODO: Handle the error.
//                LogUtils.i(TAG, status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
    }

    @Override
    public void handleNewLocation(Location location) {
        LogUtils.d(TAG, "handleNewLocation start");
        GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
        if (gpsTracker.canGetLocation()) {
            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            if (marker == null) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
            } else
                marker.setPosition(latLng);
            getAddress(true);
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
    public void onConnectionFailed(ConnectionResult connectionResult) {

        LogUtils.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

//        // TODO(Developer): Check error code and notify the user of error state and resolution.
//        Toast.makeText(this,
//                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
//                Toast.LENGTH_SHORT).show();

        Utils.showLongToast(this, getString(R.string.gg_api_error), true, false);
    }
}
