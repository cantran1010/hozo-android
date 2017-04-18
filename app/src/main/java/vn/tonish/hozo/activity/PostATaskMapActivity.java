package vn.tonish.hozo.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import vn.tonish.hozo.R;

/**
 * Created by ADMIN on 4/18/2017.
 */

public class PostATaskMapActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private RelativeLayout layoutBack;
    private Button btnNext;
    private static double lat = 21.000030;
    private static double lon = 105.837400;

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
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15.0f));
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                lat = cameraPosition.target.latitude;
                lon = cameraPosition.target.longitude;

                Log.i("centerLat", "center lat : " + cameraPosition.target.latitude);

                Log.i("centerLong", "center lon : " + cameraPosition.target.longitude);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_next:
                Intent intent = new Intent(this,PostATaskFinishActivity.class);
                startActivity(intent);
                break;
        }
    }
}
