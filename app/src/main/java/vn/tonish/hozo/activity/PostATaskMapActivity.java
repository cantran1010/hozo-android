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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import vn.tonish.hozo.dialog.AlertDialogOk;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.GPSTracker;
import vn.tonish.hozo.utils.LocationProvider;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.drawable.location;
import static vn.tonish.hozo.R.id.map;
import static vn.tonish.hozo.common.Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE;

/**
 * Created by LongBui on 4/18/2017.
 */

public class PostATaskMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, LocationProvider.LocationCallback {

    private static final String TAG = PostATaskMapActivity.class.getSimpleName();
    private GoogleMap mMap;

    //    private static double lat = 21.000030;
//    private static double lon = 105.837400;
    private LatLng latLng;
    private LocationProvider mLocationProvider;
    private TextViewHozo tvAddress;
    private TaskResponse work;
    //    private HozoLocation location = new HozoLocation();
    private Category category;
    private final String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Marker marker;
    private boolean isOnLocation = true;

    @Override
    protected int getLayout() {
        return R.layout.post_a_task_map_activity;
    }

    @Override
    protected void initView() {

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

        tvAddress = (TextViewHozo) findViewById(R.id.tv_address);
        tvAddress.setOnClickListener(this);

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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            marker = mMap.addMarker(new MarkerOptions()
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
        mMap = googleMap;
        checkPermission();
    }

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
                tvAddress.setText(strReturnedAddress);
                tvAddress.setError(null);
            }

            if (addRess.getMaxAddressLineIndex() > 1) {
                work.setCity(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 1));
            }

            if (addRess.getMaxAddressLineIndex() > 2) {
                work.setDistrict(addRess.getAddressLine(addRess.getMaxAddressLineIndex() - 2));
            }

            work.setAddress(tvAddress.getText().toString());
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                    if (marker == null) {
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latLng.latitude, latLng.longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
                    } else
                        marker.setPosition(latLng);

                    getAddress(true);
                }
                break;

            case R.id.img_map_zoom_in:
                if (latLng != null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom + 1));
                break;

            case R.id.img_map_zoom_out:
                if (latLng != null)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom - 1));
                break;

            case R.id.tv_address:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);
                    startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    e.printStackTrace();
                }
                break;
        }
    }

    private void doNext() {

        if (tvAddress.getText().toString().trim().equals("")) {
            tvAddress.requestFocus();
            tvAddress.setError(getString(R.string.post_a_task_address_error));
            return;
        }

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
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName());

                tvAddress.setText(place.getName());
                tvAddress.setError(null);
                latLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                getAddress(false);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
                } else
                    marker.setPosition(latLng);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void handleNewLocation(Location location) {
        LogUtils.d(TAG, "handleNewLocation start");
        GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
        if (gpsTracker.canGetLocation()) {
            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            if (marker == null) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_pin)));
            } else
                marker.setPosition(latLng);
            getAddress(true);
        }
    }
}
