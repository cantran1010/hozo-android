package vn.tonish.hozo.fragment;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import vn.tonish.hozo.R;

/**
 * Created by Admin on 4/4/2017.
 */

public class BrowseTaskFragment extends BaseFragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    private static double lat = 21.000030;
    private static double lon = 105.837400;

    @Override
    protected int getLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected void initView() {
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
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

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 18.0f));
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
}
