package vn.tonish.hozo.activity.image;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.AlbumAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Album;
import vn.tonish.hozo.utils.TransitionScreen;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICK_IMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICK_IMAGE;

/**
 * Created by LongBui on 4/19/2017.
 */

public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    private final String[] projection = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA};

    private GridView grAlbum;
    private ArrayList<Album> albums = new ArrayList<>();
    private AlbumAdapter albumAdapter;
    private boolean isOnlyImage = false;
    private boolean isCropProfile = false;
    private int countImageAttach = 0;
    private final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected int getLayout() {
        return R.layout.activity_album;
    }

    @Override
    protected void initView() {
        grAlbum = (GridView) findViewById(R.id.gr_album);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        if (intent.hasExtra(Constants.EXTRA_ONLY_IMAGE))
            isOnlyImage = intent.getBooleanExtra(Constants.EXTRA_ONLY_IMAGE, false);

        if (intent.hasExtra(Constants.EXTRA_IS_CROP_PROFILE))
            isCropProfile = intent.getBooleanExtra(Constants.EXTRA_IS_CROP_PROFILE, false);

        if (intent.hasExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA))
            countImageAttach = intent.getIntExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA, 0);

        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != Constants.PERMISSION_REQUEST_CODE
                || grantResults.length == 0
                || grantResults[0] == PackageManager.PERMISSION_DENIED) {
            permissionDenied();
        } else {
            permissionGranted();
        }
    }

    private void permissionDenied() {
//        hideViews();
//        requestPermission();
    }

    private void permissionGranted() {
        getAlbum();
    }

    private void getAlbum() {

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> albumTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                Cursor cursor = getApplicationContext().getContentResolver()
                        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                                null, null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

                assert cursor != null;
                ArrayList<Album> temp = new ArrayList<>(cursor.getCount());
                HashSet<Long> albumSet = new HashSet<>();
                File file;
                if (cursor.moveToLast()) {
                    do {
                        if (Thread.interrupted()) {
                            return null;
                        }

                        long albumId = cursor.getLong(cursor.getColumnIndex(projection[0]));
                        String album = cursor.getString(cursor.getColumnIndex(projection[1]));
                        String image = cursor.getString(cursor.getColumnIndex(projection[2]));

                        if (!albumSet.contains(albumId)) {
                        /*
                        It may happen that some image file paths are still present in cache,
                        though image file does not exist. These last as long as media
                        scanner is not run again. To avoid get such image file paths, check
                        if image file exists.
                         */
                            file = new File(image);
                            if (file.exists()) {
                                temp.add(new Album(albumId, album, image));
                                albumSet.add(albumId);
                            }
                        }

                    } while (cursor.moveToPrevious());
                }
                cursor.close();

                if (albums == null) {
                    albums = new ArrayList<>();
                }
                albums.clear();
                albums.addAll(temp);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                albumAdapter = new AlbumAdapter(AlbumActivity.this, albums);
                grAlbum.setAdapter(albumAdapter);

                grAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), ImageSelectActivity.class);
                        intent.putExtra(Constants.INTENT_EXTRA_ALBUM, albums.get(position).getName());
                        intent.putExtra(Constants.EXTRA_ONLY_IMAGE, isOnlyImage);
                        intent.putExtra(Constants.EXTRA_IS_CROP_PROFILE, isCropProfile);
                        intent.putExtra(Constants.COUNT_IMAGE_ATTACH_EXTRA, countImageAttach);
                        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
                    }
                });
            }
        };
        albumTask.execute();

    }

    @Override
    protected void resumeData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE
                && resultCode == RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            setResult(RESPONSE_CODE_PICK_IMAGE, data);
            finish();
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
}
