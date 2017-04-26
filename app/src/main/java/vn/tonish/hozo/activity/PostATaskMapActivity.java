package vn.tonish.hozo.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.GPSTracker;
import vn.tonish.hozo.utils.LocationProvider;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBD on 4/18/2017.
 */

public class PostATaskMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, LocationProvider.LocationCallback {

    private static final String TAG = PostATaskMapActivity.class.getSimpleName();
    private GoogleMap mMap;
    protected RelativeLayout layoutBack;
    protected Button btnNext;
    protected ImageView imgCurrentLocation, imgZoomIn, imgZoomOut;

    private static double lat = 21.000030;
    private static double lon = 105.837400;
    private LatLng latLng;
    private LocationProvider mLocationProvider;
    private EditText edtAddress;

    @Override
    protected int getLayout() {
        return R.layout.post_a_task_map_activity;
    }

    @Override
    protected void initView() {

        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        imgCurrentLocation = (ImageView) findViewById(R.id.img_current_location);
        imgCurrentLocation.setOnClickListener(this);

        imgZoomIn = (ImageView) findViewById(R.id.img_map_zoom_in);
        imgZoomIn.setOnClickListener(this);

        imgZoomOut = (ImageView) findViewById(R.id.img_map_zoom_out);
        imgZoomOut.setOnClickListener(this);

        edtAddress = (EditText) findViewById(R.id.edt_address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void initData() {
        mLocationProvider = new LocationProvider(this, this);

    }

    @Override
    protected void resumeData() {
//        GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
//        if (!gpsTracker.canGetLocation()) {
        mLocationProvider.connect();
//        }

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

        GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
        if (gpsTracker.canGetLocation()) {
            LogUtils.d(TAG, "onMapReady canGetLocation");

            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    latLng = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);

                    lat = cameraPosition.target.latitude;
                    lon = cameraPosition.target.longitude;
                    LogUtils.i("centerLat", "center lat : " + cameraPosition.target.latitude);
                    LogUtils.i("centerLong", "center lon : " + cameraPosition.target.longitude);
                    getAddress();
                }
            });

        } else {
            mLocationProvider.connect();
            LogUtils.d(TAG, "onMapReady !!!!!! canGetLocation");
            DialogUtils.showConfirmAlertDialog(this, getString(R.string.msg_err_gps), new DialogUtils.ConfirmDialogListener() {
                @Override
                public void onConfirmClick() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
        }

    }

    private void getAddress() {
        try {
            Geocoder geocoder;
            ArrayList<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            addresses = (ArrayList<Address>) geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            Address addRess = addresses.get(0);
            LogUtils.d(TAG, "getAddress address : " + addRess.toString());

//        String address = addRess.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addRess.getLocality();
//        String state = addRess.getAdminArea();
//        String country = addRess.getCountryName();
//        String postalCode = addRess.getPostalCode();
//        String knownName = addRess.getFeatureName(); // Only if available else return NULL

            String strReturnedAddress = "";
            for (int i = 0; i < addRess.getMaxAddressLineIndex(); i++) {
                strReturnedAddress = strReturnedAddress + addRess.getAddressLine(i) + ", ";
            }
            strReturnedAddress = strReturnedAddress.substring(0, strReturnedAddress.length() - 2);
            edtAddress.setText(strReturnedAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
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
                }
                break;

            case R.id.img_map_zoom_in:
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom + 1));
                break;

            case R.id.img_map_zoom_out:
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom - 1));
                break;
        }
    }

    private void doNext() {

        if (edtAddress.getText().toString().trim().equals("")) {
            Utils.showLongToast(PostATaskMapActivity.this, getString(R.string.msg_err_address));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_ADDRESS, edtAddress.getText().toString());
        setResult(Constants.RESPONSE_CODE_ADDRESS, intent);
        finish();
    }

    @Override
    public void handleNewLocation(Location location) {
        LogUtils.d(TAG, "handleNewLocation start");
        GPSTracker gpsTracker = new GPSTracker(PostATaskMapActivity.this);
        if (gpsTracker.canGetLocation()) {
            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
    }
}
