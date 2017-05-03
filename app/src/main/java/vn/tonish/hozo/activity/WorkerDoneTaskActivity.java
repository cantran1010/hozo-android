package vn.tonish.hozo.activity;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.model.Work;
import vn.tonish.hozo.view.CommentViewFull;
import vn.tonish.hozo.view.WorkAroundMapFragment;
import vn.tonish.hozo.view.WorkDetailView;


/**
 * Created by LongBui on 4/25/2017.
 */

public class WorkerDoneTaskActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = WorkerDoneTaskActivity.class.getSimpleName();
    private CommentViewFull commentViewFull;
    private WorkDetailView workDetailView;
    private ArrayList<Comment> comments = new ArrayList<>();
    private Work work;
    private ScrollView scv;
    private ImageView imgBack;

    @Override
    protected int getLayout() {
        return R.layout.worker_done_task_activity;
    }

    @Override
    protected void initView() {
        workDetailView = (WorkDetailView) findViewById(R.id.work_detail_view);
        commentViewFull = (CommentViewFull) findViewById(R.id.comment_view_full);

        scv = (ScrollView) findViewById(R.id.scv);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        WorkAroundMapFragment mapFragment = (WorkAroundMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapFragment.setListener(new WorkAroundMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                scv.requestDisallowInterceptTouchEvent(true);
            }
        });
    }

    @Override
    protected void initData() {
        workDetailView.updateBtnOffer(false);
        workDetailView.updateStatus(getString(R.string.done), ContextCompat.getDrawable(this, R.drawable.bg_border_done));
        workDetailView.updateBtnCallRate(true, false, getString(R.string.rate));

        //fake work detail
        work = new Work();
        work.setName("Sua Ti vi");
        work.setTimeAgo("20 phut truoc");
        work.setWorkType("Lắp đặt");
        work.setDescription("Tôi cần một người sửa ti si samsung OTX 24000,nhanh nhẹn,có năng lực,trung thực,nam giới ...");
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

        //fake  comment data
        Comment comment = new Comment();
        comment.setFullName("Bui duc long");
        comment.setBody(".....................................................................................................................................................................................................................................................................................................");
        comment.setCreatedAt("2 days ago");

        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);
        comments.add(comment);

        commentViewFull.updateData(comments);

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(work.getLat(), work.getLon());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.DEFAULT_MAP_ZOOM_LEVEL));

        // create marker
        MarkerOptions marker = new MarkerOptions().position(new LatLng(work.getLat(), work.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
        googleMap.addMarker(marker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
