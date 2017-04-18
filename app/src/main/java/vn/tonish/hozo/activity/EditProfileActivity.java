package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import vn.tonish.hozo.R;

/**
 * Created by ADMIN on 4/17/2017.
 */

public class EditProfileActivity extends BaseActivity implements View.OnClickListener{

    private EditText edtName,edtAddress;
    private Button btnSave;

    @Override
    protected int getLayout() {
        return R.layout.edit_profile_activity;
    }

    @Override
    protected void initView() {
        edtName = (EditText) findViewById(R.id.edt_name);
        edtAddress = (EditText) findViewById(R.id.edt_address);

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_save:

                break;

        }
    }
}
