package vn.tonish.hozo.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.File;
import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CandidateAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CommentViewFull;
import vn.tonish.hozo.view.WorkAroundMapFragment;
import vn.tonish.hozo.view.WorkDetailView;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;

/**
 * Created by LongBui on 4/21/2017.
 */

public class MakeAnOfferActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String TAG = MakeAnOfferActivity.class.getSimpleName();
    private CommentViewFull commentViewFull;
    private WorkDetailView workDetailView;
    private ArrayList<Comment> comments = new ArrayList<>();
//    private Work work;

    private ImageView imgAttach, imgAttached, imgDelete;
    private RelativeLayout imgLayout;
    private String imgPath;
    private ScrollView scv;
    private ImageView imgBack;

    private RecyclerView rcvCandidate;
    private ArrayList<User> usersCandidate = new ArrayList<>();
    private CandidateAdapter candidateAdapter;
    private int taskId = 0;


    @Override
    protected int getLayout() {
        return R.layout.make_an_offer_activity;
    }

    @Override
    protected void initView() {
        workDetailView = (WorkDetailView) findViewById(R.id.work_detail_view);
        commentViewFull = (CommentViewFull) findViewById(R.id.comment_view_full);

        imgAttach = (ImageView) findViewById(R.id.img_attach);
        imgAttach.setOnClickListener(this);

        imgAttached = (ImageView) findViewById(R.id.img_attached);
        imgAttached.setOnClickListener(this);

        imgDelete = (ImageView) findViewById(R.id.img_delete);
        imgDelete.setOnClickListener(this);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        imgLayout = (RelativeLayout) findViewById(R.id.img_layout);

        scv = (ScrollView) findViewById(R.id.scv);

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

//        ApiClient.getApiService().getDetailTask(UserManager.getUserToken(this), taskId).enqueue(new Callback<TaskResponse>() {
//            @Override
//            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
//                LogUtils.d(TAG, "getDetailTask , response : " + response);
//            }
//
//            @Override
//            public void onFailure(Call<TaskResponse> call, Throwable t) {
//                LogUtils.e(TAG, "getDetailTask , error : " + t.getMessage());
//            }
//        });

//        //fake work detail
//        work = new Work();
//        work.setName("Sua Ti vi");
//        work.setTimeAgo("20 phut truoc");
//        work.setWorkTypeName("Lắp đặt");
//        work.setDescription("Tôi cần một người sửa ti si samsung OTX 24000,nhanh nhẹn,có năng lực,trung thực,nam giới ...");
//        work.setPrice("350.000 Đồng");
//        work.setDate("25/04/2017");
//        work.setTime("14h:00 - 20h:00");
//        work.setAddress("Số nhà 41,ngõ 102 trường trinh,Hà Nội");
//        work.setLat(21.000030);
//        work.setLon(105.837400);
//
//        //user up work
//        User user = new User();
//        user.setFullName("TRAN MINH HAI");
//        work.setUser(user);
//
//        workDetailView.updateWork(work);
//
//        //fake  comment data
//        Comment comment = new Comment();
//        comment.setFullName("Bui duc long");
//        comment.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing, se do eiusmod tempor incididunt");
//        comment.setCreatedAt("2 days ago");
//
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//        comments.add(comment);
//
//        commentViewFull.updateData(comments);

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        LatLng latLng = new LatLng(work.getLat(), work.getLon());
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Constants.DEFAULT_MAP_ZOOM_LEVEL));
//
//        // create marker
//        MarkerOptions marker = new MarkerOptions().position(new LatLng(work.getLat(), work.getLon())).icon(BitmapDescriptorFactory.fromResource(R.drawable.maker));
//        googleMap.addMarker(marker);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_attach:

                PickImageDialog pickImageDialog = new PickImageDialog(MakeAnOfferActivity.this);
                pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
                    @Override
                    public void onCamera() {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
                    }

                    @Override
                    public void onGallery() {
                        Intent intent = new Intent(MakeAnOfferActivity.this, AlbumActivity.class);
                        intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                    }
                });
                pickImageDialog.showView();
                break;

            case R.id.img_delete:
                imgLayout.setVisibility(View.GONE);
                imgPath = null;
                break;

            case R.id.img_attached:
                Intent intent = new Intent(MakeAnOfferActivity.this, PreviewImageActivity.class);
                intent.putExtra(Constants.EXTRA_IMAGE_PATH, imgPath);
                startActivity(intent);
                break;

            case R.id.img_back:
                finish();
                break;

        }
    }

    public Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            imgPath = imagesSelected.get(0).getPath();
            Utils.displayImage(MakeAnOfferActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            Utils.displayImage(MakeAnOfferActivity.this, imgAttached, imgPath);
            imgLayout.setVisibility(View.VISIBLE);
        }

    }
}
