package vn.tonish.hozo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.GPSTracker;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by ADMIN on 4/18/2017.
 */

public class PostATaskMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = PostATaskMapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private RelativeLayout layoutBack;
    private Button btnNext;
    private ImageView imgCurrentLocation, imgZoomIn, imgZoomOut;

    private static double lat = 21.000030;
    private static double lon = 105.837400;
    private LatLng latLng;

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

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
                }
            });

        } else {
            LogUtils.d(TAG, "onMapReady !!!!!! canGetLocation");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_next:
                Intent intent = new Intent(this, PostATaskFinishActivity.class);
                startActivity(intent);
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
}
