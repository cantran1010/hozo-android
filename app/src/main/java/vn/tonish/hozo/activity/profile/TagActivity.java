package vn.tonish.hozo.activity.profile;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.TagAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.TagResponse;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 11/7/17.
 */

public class TagActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = TagActivity.class.getSimpleName();
    private EdittextHozo edtTag;
    private RecyclerView rcvTag;
    private TagAdapter tagAdapter;
    private final ArrayList<TagResponse> tagResponses = new ArrayList<>();
    private UserEntity userEntity = new UserEntity();
    private int requestCode;
    private TextViewHozo tvTitle;

    @Override
    protected int getLayout() {
        return R.layout.tag_activity;
    }

    @Override
    protected void initView() {
        edtTag = (EdittextHozo) findViewById(R.id.edt_tag);
        ButtonHozo btnSend = (ButtonHozo) findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);

        rcvTag = (RecyclerView) findViewById(R.id.rcv_tag);

        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
    }

    @Override
    protected void initData() {
        userEntity = UserManager.getMyUser();
        requestCode = getIntent().getIntExtra(Constants.REQUEST_CODE_EXTRA, 0);

        if (requestCode == Constants.REQUEST_CODE_SKILL) {
            tagResponses.addAll(userEntity.getSkills());
            tvTitle.setText(getString(R.string.skill));
        } else {
            tagResponses.addAll(userEntity.getLanguages());
            tvTitle.setText(getString(R.string.language));
        }

        setUpList();
    }

    @Override
    protected void resumeData() {

    }

    private void setUpList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setAutoMeasureEnabled(true);
        rcvTag.setNestedScrollingEnabled(false);

        rcvTag.setLayoutManager(layoutManager);
        tagAdapter = new TagAdapter(tagResponses);
        rcvTag.setAdapter(tagAdapter);
    }

    private void doSend() {

        if (TextUtils.isEmpty(edtTag.getText().toString().trim())) return;

        TagResponse tagResponse = new TagResponse();
        tagResponse.setValue(edtTag.getText().toString().trim());
        tagResponses.add(tagResponse);
        tagAdapter.notifyDataSetChanged();

        edtTag.setText(getString(R.string.empty));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_send:
                doSend();
                break;

            case R.id.img_back:

                if (requestCode == Constants.REQUEST_CODE_SKILL) {
                    RealmList<TagResponse> realmList = new RealmList<>();

                    for (TagResponse tagResponse : tagResponses) {
                        if (tagResponse.isManaged()) {
                            Realm.getDefaultInstance().beginTransaction();
                            realmList.add(tagResponse);
                            Realm.getDefaultInstance().commitTransaction();
                        } else {
                            Realm.getDefaultInstance().beginTransaction();
                            realmList.add(Realm.getDefaultInstance().copyToRealm(tagResponse));
                            Realm.getDefaultInstance().commitTransaction();
                        }
                    }

                    Realm.getDefaultInstance().beginTransaction();
                    userEntity.setSkills(realmList);
                    Realm.getDefaultInstance().commitTransaction();

                    UserManager.insertUser(userEntity, true);
                } else {
                    RealmList<TagResponse> realmList = new RealmList<>();

                    for (TagResponse tagResponse : tagResponses) {
                        if (tagResponse.isManaged()) {
                            Realm.getDefaultInstance().beginTransaction();
                            realmList.add(tagResponse);
                            Realm.getDefaultInstance().commitTransaction();
                        } else {
                            Realm.getDefaultInstance().beginTransaction();
                            realmList.add(Realm.getDefaultInstance().copyToRealm(tagResponse));
                            Realm.getDefaultInstance().commitTransaction();
                        }
                    }

                    Realm.getDefaultInstance().beginTransaction();
                    userEntity.setLanguages(realmList);
                    Realm.getDefaultInstance().commitTransaction();

                    UserManager.insertUser(userEntity, true);
                }

                Intent intent = new Intent();
                setResult(Constants.RESULT_CODE_TAG, intent);
                finish();
                break;

        }
    }

}
