package vn.tonish.hozo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.AlbumAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Album;

import static vn.tonish.hozo.common.Constants.REQUEST_CODE_PICKIMAGE;
import static vn.tonish.hozo.common.Constants.RESPONSE_CODE_PICKIMAGE;

/**
 * Created by ADMIN on 4/19/2017.
 */

public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    private final String[] projection = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA};

    private GridView grAlbum;
    private ArrayList<Album> albums = new ArrayList<>();
    private AlbumAdapter albumAdapter;
    private RelativeLayout layoutBack;
    private boolean isOnlyImage = false;


    @Override
    protected int getLayout() {
        return R.layout.album_activity;
    }

    @Override
    protected void initView() {
        grAlbum = (GridView) findViewById(R.id.gr_album);
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();

        if (intent.hasExtra(Constants.EXTRA_ONLY_IMAGE))
            isOnlyImage = intent.getBooleanExtra(Constants.EXTRA_ONLY_IMAGE, false);
        getAlbum();
    }

    private void getAlbum() {

        AsyncTask<Void, Void, Void> albumTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {

                Cursor cursor = getApplicationContext().getContentResolver()
                        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                                null, null, MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

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
                        startActivityForResult(intent, REQUEST_CODE_PICKIMAGE);
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

        if (requestCode == REQUEST_CODE_PICKIMAGE
                && resultCode == RESPONSE_CODE_PICKIMAGE
                && data != null) {
            setResult(RESPONSE_CODE_PICKIMAGE, data);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_back:
                finish();
                break;

        }
    }
}
