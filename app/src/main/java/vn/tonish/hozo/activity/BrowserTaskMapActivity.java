package vn.tonish.hozo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdate;
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

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.MiniTask;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.R.id.map;

/**
 * Created by LongBui on 5/18/17.
 */

public class BrowserTaskMapActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<MiniTask> miniTasks = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_brower_task_map;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void initData() {
        miniTasks = getIntent().getParcelableArrayListExtra(Constants.LIST_TASK_EXTRA);
    }

    @Override
    protected void resumeData() {

    }

    private void updateMap() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < miniTasks.size(); i++) {

            MiniTask miniTask = miniTasks.get(i);

            // create marker
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(miniTask.getLat(), miniTask.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
            Marker marker = mMap.addMarker(markerOption);
            marker.setTag(i);

            builder.include(marker.getPosition());
        }

        if(miniTasks.size() > 0){
            LatLngBounds bounds = builder.build();

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.30); // offset from edges of the map 30% of screen

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

            mMap.moveCamera(cameraUpdate);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_back:
                finish();
                break;

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        updateMap();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int position = (int) marker.getTag();

                Intent intent = new Intent(BrowserTaskMapActivity.this, TaskDetailActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, miniTasks.get(position).getId());
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


        private final View view;

        @SuppressLint("InflateParams")
        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.pop_up_marker, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (marker != null
                    && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            TextViewHozo tvName = (TextViewHozo) view.findViewById(R.id.tv_name);
            TextViewHozo tvAddress = (TextViewHozo) view.findViewById(R.id.tv_address);
            int positon = (int) marker.getTag();
            tvName.setText(miniTasks.get(positon).getTitle());
            tvAddress.setText(miniTasks.get(positon).getAddress());
            return view;
        }

    }
}
