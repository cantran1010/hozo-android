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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.HozoLocation;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.model.Work;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.GPSTracker;
import vn.tonish.hozo.utils.LocationProvider;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/18/2017.
 */

public class PostATaskMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, LocationProvider.LocationCallback {

    private static final String TAG = PostATaskMapActivity.class.getSimpleName();
    private GoogleMap mMap;
    protected ImageView imgBack;
    protected Button btnNext;
    protected ImageView imgCurrentLocation, imgZoomIn, imgZoomOut;

    private static double lat = 21.000030;
    private static double lon = 105.837400;
    private LatLng latLng;
    private LocationProvider mLocationProvider;
    private EditText edtAddress;

    private Work work;
    private ArrayList<Image> images = new ArrayList<>();
    private HozoLocation location = new HozoLocation();

    @Override
    protected int getLayout() {
        return R.layout.post_a_task_map_activity;
    }

    @Override
    protected void initView() {

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

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

        work = (Work) getIntent().getSerializableExtra(Constants.EXTRA_WORK);
        images = getIntent().getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

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

            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    latLng = new LatLng(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                    lat = mMap.getCameraPosition().target.latitude;
                    lon = mMap.getCameraPosition().target.longitude;
                    LogUtils.i("centerLat", "center lat : " + mMap.getCameraPosition().target.latitude);
                    LogUtils.i("centerLong", "center lon : " + mMap.getCameraPosition().target.longitude);
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

        location.setAddress(edtAddress.getText().toString());
        location.setLat(latLng.latitude);
        location.setLon(latLng.longitude);

        Intent intent = new Intent(PostATaskMapActivity.this, PostATaskFinishActivity.class);
        intent.putExtra(Constants.EXTRA_ADDRESS, location);
        intent.putExtra(Constants.EXTRA_WORK, work);
        startActivity(intent);
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