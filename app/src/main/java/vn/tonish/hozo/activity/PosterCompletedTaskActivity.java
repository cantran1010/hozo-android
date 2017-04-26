package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.PosterCompletedAdapter;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.model.Work;
import vn.tonish.hozo.view.WorkDetailView;

/**
 * Created by LongBD on 4/25/2017.
 */

public class PosterCompletedTaskActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = PosterCompletedTaskActivity.class.getSimpleName();
    private WorkDetailView workDetailView;
    private Work work;
    private RecyclerView rcvUser;
    private PosterCompletedAdapter posterCompletedAdapter;
    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.poster_completed_task_activity;
    }

    @Override
    protected void initView() {
        workDetailView = (WorkDetailView) findViewById(R.id.work_detail_view);

        rcvUser = (RecyclerView) findViewById(R.id.rcv_user);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        workDetailView.updateBtnOffer(false);
    }

    @Override
    protected void initData() {

        //fake work detail
        work = new Work();
        work.setName("Sua Ti vi");
        work.setTimeAgo("20 phut truoc");
        work.setWorkType("Lắp đặt");
        work.setDes("Tôi cần một người sửa ti si samsung OTX 24000,nhanh nhẹn,có năng lực,trung thực,nam giới ...");
        work.setPrice("350.000 Đồng");
        work.setDate("25/04/2017");
        work.setTime("14h:00 - 20h:00");
        work.setAddress("Số nhà 41,ngõ 102 trường trinh,Hà Nội");
        work.setLat(21.000030);
        work.setLon(105.837400);

        //user up work
        User user = new User();
        user.setFull_name("TRAN MINH HAI");
        work.setUser(user);

        workDetailView.updateWork(work);

        //fake list user assigned
        User user1 = new User();
        user1.setFull_name("Tristan");
        users.add(user1);
        users.add(user1);
        users.add(user1);
        users.add(user1);
        users.add(user1);
        users.add(user1);
        users.add(user1);

        posterCompletedAdapter = new PosterCompletedAdapter(users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(layoutManager);
        rcvUser.setAdapter(posterCompletedAdapter);

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(work.getLat(), work.getLon());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(work.getLat(), work.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
        googleMap.addMarker(marker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
