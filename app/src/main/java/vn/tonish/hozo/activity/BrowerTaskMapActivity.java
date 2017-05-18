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
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/18/17.
 */

public class BrowerTaskMapActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG = BrowerTaskMapActivity.class.getSimpleName();
    private ImageView imgBack;
    private GoogleMap mMap;
    private ArrayList<TaskResponse> taskResponses = new ArrayList<>();


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

        //fake data
        TaskResponse taskResponse1 = new TaskResponse();
        taskResponse1.setTitle("Test 1");
        taskResponse1.setLatitude(21.000030);
        taskResponse1.setLongitude(105.837400);
        taskResponse1.setAddress("Truong trinh ha noi 1111");

        TaskResponse taskResponse2 = new TaskResponse();
        taskResponse2.setTitle("Test 222222222222");
        taskResponse2.setLatitude(21.010030);
        taskResponse2.setLongitude(105.847400);
        taskResponse2.setAddress("Truong trinh ha noi 2222");

        TaskResponse taskResponse3 = new TaskResponse();
        taskResponse3.setTitle("Test 3");
        taskResponse3.setLatitude(21.020030);
        taskResponse3.setLongitude(105.857400);
        taskResponse3.setAddress("Truong trinh ha noi 3333");

        taskResponses.add(taskResponse1);
        taskResponses.add(taskResponse2);
        taskResponses.add(taskResponse3);
    }

    @Override
    protected void resumeData() {

    }

    private void updateMap() {

        for (int i = 0; i < taskResponses.size(); i++) {

            TaskResponse taskResponse = taskResponses.get(i);

            LatLng latLng = new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude());

            if (i == taskResponses.size() - 1)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            // create marker
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(taskResponse.getLatitude(), taskResponse.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
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

                Utils.showLongToast(BrowerTaskMapActivity.this, position + "");

                Intent intent = new Intent(BrowerTaskMapActivity.this, MainActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, 123);
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
            tvName.setText(taskResponses.get(positon).getTitle());
            tvAddress.setText(taskResponses.get(positon).getAddress());
            return view;
        }

    }
}
