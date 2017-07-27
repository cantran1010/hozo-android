package vn.tonish.hozo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.AlertDialogOkFullScreen;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.GPSTracker;
import vn.tonish.hozo.utils.LocationProvider;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.map;
import static vn.tonish.hozo.R.string.post_task_map_get_location_error_next;

/**
 * Created by LongBui on 4/18/2017.
 */

public class PostATaskMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, LocationProvider.LocationCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = PostATaskMapActivity.class.getSimpleName();
    private GoogleMap googleMap;
    private LatLng latLng;
    private LocationProvider mLocationProvider;
    private TaskResponse work;
    private Category category;
    private final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Marker marker;
    private boolean isOnLocation = true;
    private TextViewHozo tvAddress;
    private ImageView imgPickList, imgPickMap;
    private int pickType = 1;
    private RelativeLayout locationLayout;

    @Override
    protected int getLayout() {
        return R.layout.activity_post_a_task_map;
    }

    @Override
    protected void initView() {
        tvAddress = findViewById(R.id.tv_address);
        tvAddress.setOnClickListener(this);

        locationLayout = findViewById(R.id.location_layout);

        imgPickList = findViewById(R.id.img_pick_list);
        imgPickList.setOnClickListener(this);

        imgPickMap = findViewById(R.id.img_pick_map);
        imgPickMap.setOnClickListener(this);

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

            // must dealy 1 second if user setting gps on and back very fast
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLocationProvider.connect();
                    isOnLocation = true;
                }
            }, 1000);
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
            DialogUtils.showOkDialogFullScreen(this, getString(R.string.app_name), getString(R.string.msg_err_gps), getString(R.string.ok), new AlertDialogOkFullScreen.AlertDialogListener() {
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

    private void getAddress(boolean isAddAddress) {

        LogUtils.d(TAG, "getAddress address , latLng.latitude: " + latLng.latitude + " , latLng.longitude : " + latLng.longitude);

        try {
            Geocoder geocoder;
            ArrayList<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            Address addRess = addresses.get(0); // may be crash,can't get address from geocoder
            LogUtils.d(TAG, "getAddress address : " + addRess.toString());
            LogUtils.d(TAG, "getAddress addRess.getMaxAddressLineIndex() : " + addRess.getMaxAddressLineIndex());

            String strReturnedAddress = "";

            if (addRess.getMaxAddressLineIndex() == 0) {
                strReturnedAddress = addRess.getAddressLine(0);
            } else {
                for (int i = 0; i < addRess.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress = strReturnedAddress + addRess.getAddressLine(i) + ", ";
                }
                LogUtils.d(TAG, "getAddress strReturnedAddress before : " + strReturnedAddress);
                strReturnedAddress = strReturnedAddress.substring(0, strReturnedAddress.length() - 2);
                LogUtils.d(TAG, "getAddress strReturnedAddress : " + strReturnedAddress);
            }

            if (pickType == 1) {
                if (isAddAddress) {
                    tvAddress.setText(strReturnedAddress);
                }

                if (addRess.getMaxAddressLineIndex() == 0) {
                    String[] arrAddress = strReturnedAddress.split(",");

                    if (arrAddress.length >= 2) {
                        work.setCity(arrAddress[arrAddress.length - 2].trim());
                    } else {
                        work.setCity(arrAddress[arrAddress.length - 1]);
                    }

                } else {
                    if (addRess.getMaxAddressLineIndex() >= 1) {
                        work.setCity(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 1));
                    } else {
                        work.setCity(addRess.getAddressLine(0));
                    }
                }

                if (addRess.getMaxAddressLineIndex() == 0) {
                    String[] arrAddress = strReturnedAddress.split(",");
                    if (arrAddress.length >= 3) {
                        work.setDistrict(arrAddress[arrAddress.length - 3].trim());
                    } else {
                        work.setDistrict(arrAddress[arrAddress.length - 1]);
                    }
                } else {
                    if (addRess.getMaxAddressLineIndex() >= 2) {
                        work.setDistrict(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 2));
                    } else {
                        work.setDistrict(addRess.getAddressLine(0));
                    }
                }

                work.setAddress(tvAddress.getText().toString());
                work.setLatitude(latLng.latitude);
                work.setLongitude(latLng.longitude);
            } else {
                tvAddress.setText(strReturnedAddress);

                if (addRess.getMaxAddressLineIndex() == 0) {
                    String[] arrAddress = strReturnedAddress.split(",");

                    if (arrAddress.length >= 2) {
                        work.setCity(arrAddress[arrAddress.length - 2].trim());
                    } else {
                        work.setCity(arrAddress[arrAddress.length - 1]);
                    }

                } else {
                    if (addRess.getMaxAddressLineIndex() >= 1) {
                        work.setCity(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 1));
                    } else {
                        work.setCity(addRess.getAddressLine(0));
                    }
                }

                if (addRess.getMaxAddressLineIndex() == 0) {
                    String[] arrAddress = strReturnedAddress.split(",");
                    if (arrAddress.length >= 3) {
                        work.setDistrict(arrAddress[arrAddress.length - 3].trim());
                    } else {
                        work.setDistrict(arrAddress[arrAddress.length - 1]);
                    }
                } else {
                    if (addRess.getMaxAddressLineIndex() >= 2) {
                        work.setDistrict(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 2));
                    } else {
                        work.setDistrict(addRess.getAddressLine(0));
                    }
                }

                work.setAddress(tvAddress.getText().toString());
                work.setLatitude(latLng.latitude);
                work.setLongitude(latLng.longitude);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Utils.showLongToast(this, getString(R.string.post_a_task_map_get_location_error), true, false);
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
                doCurrentLocation();
                break;

            case R.id.img_map_zoom_in:
                if (latLng != null && googleMap != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, googleMap.getCameraPosition().zoom + 1));
                break;

            case R.id.img_map_zoom_out:
                if (latLng != null && googleMap != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, googleMap.getCameraPosition().zoom - 1));
                break;

            case R.id.img_pick_list:
                doInputMap();
                break;

            case R.id.img_pick_map:
                doMoveMap();
                break;

            case R.id.tv_address:
                Intent intent = new Intent(this, PlaceActivity.class);
//                intent.putExtra(Constants.EXTRA_ADDRESS, tvAddress.getText().toString());
                startActivityForResult(intent, Constants.REQUEST_CODE_ADDRESS, TransitionScreen.FADE_IN);
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

    private void doCurrentLocation() {

        if (googleMap == null) return;
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

    private void doMoveMap() {
        if (googleMap == null) return;
        if (pickType == 2) return;
        if (marker == null) return;

        // move map
        tvAddress.setClickable(false);
        pickType = 2;
        tvAddress.setVisibility(View.VISIBLE);
        locationLayout.setVisibility(View.VISIBLE);
        imgPickList.setImageResource(R.drawable.ic_menu_list_off);
        imgPickMap.setImageResource(R.drawable.ic_menu_map_on);
        marker.setVisible(false);

        tvAddress.setSelected(true);

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                latLng = new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                getAddress(false);
            }
        });
        doCurrentLocation();
    }

    private void doInputMap() {
        if (googleMap == null) return;
        if (pickType == 1) return;

        // input map
        tvAddress.setClickable(true);
        pickType = 1;
        locationLayout.setVisibility(View.GONE);
        imgPickList.setImageResource(R.drawable.ic_menu_list_on);
        imgPickMap.setImageResource(R.drawable.ic_menu_map_off);
        marker.setVisible(true);

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

            }
        });

        doCurrentLocation();
    }

    private void doNext() {

//        if (tvAddress.getText().toString().trim().equals("")) {
//            tvAddress.requestFocus();
//            tvAddress.setError(getString(R.string.post_a_task_address_error));
//            return;
//        }

        if (work.getLatitude() == 0 || work.getLongitude() == 0
                || work.getAddress() == null || work.getAddress().equals("")
                || work.getCity() == null || work.getCity().equals("")
                || work.getAddress() == null || work.getAddress().equals("")) {
            Utils.showLongToast(this, getString(post_task_map_get_location_error_next), true, false);
            return;
        }

        LogUtils.d(TAG, "doNext , work : " + work.toString());

        Intent intent = new Intent(PostATaskMapActivity.this, PostATaskFinishActivity.class);
//        intent.putExtra(Constants.EXTRA_ADDRESS, location);
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
        } else if (requestCode == Constants.REQUEST_CODE_ADDRESS && resultCode == Constants.RESULT_CODE_ADDRESS) {
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

            latLng = new LatLng(data.getDoubleExtra(Constants.LAT_EXTRA, 0), data.getDoubleExtra(Constants.LON_EXTRA, 0));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            tvAddress.setText(data.getStringExtra(Constants.EXTRA_ADDRESS));
            getAddress(false);
            if (marker == null) {
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
            } else
                marker.setPosition(latLng);

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
    public void handleNewLocation() {
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
