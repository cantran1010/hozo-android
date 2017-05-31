package vn.tonish.hozo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.MiniTask;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/18/17.
 */

public class BrowerTaskMapActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG = BrowerTaskMapActivity.class.getSimpleName();
    private ImageView imgBack;
    private GoogleMap mMap;
    private ArrayList<MiniTask> miniTasks = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.brower_task_map_activity;
    }

    @Override
    protected void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
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

        for (int i = 0; i < miniTasks.size(); i++) {

            MiniTask miniTask = miniTasks.get(i);

            LatLng latLng = new LatLng(miniTask.getLat(), miniTask.getLon());

            if (i == miniTasks.size() - 1)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            // create marker
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(miniTask.getLat(), miniTask.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
            Marker marker = mMap.addMarker(markerOption);
            marker.setTag(i);
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

                Intent intent = new Intent(BrowerTaskMapActivity.this, TaskDetailActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, miniTasks.get(position).getId());
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


        private View view;

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
