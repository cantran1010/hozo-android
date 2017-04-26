package vn.tonish.hozo.customview;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.realm.Realm;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.LoginActivity;
import vn.tonish.hozo.activity.MainActivity;
import vn.tonish.hozo.database.entity.UserEntity;
import vn.tonish.hozo.database.manager.RealmDbHelper;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.network.NetworkConfig;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.common.Constants.NAME_VIEW;
import static vn.tonish.hozo.utils.Utils.getStringInJsonObj;

/**
 * Created by CanTran on 18/04/2017.
 */

public class NameView extends FrameLayout implements View.OnClickListener {
    private final static String TAG = "NameView";
    private Context context;
    private View rootView;
    private EditText edtName;
    private TextView btnSave, btnBack;


    public NameView(Context context) {
        super(context);
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
    }


    private void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.view_name, null);
        addView(rootView);
        edtName = (EditText) rootView.findViewById(R.id.edt_name);
        btnSave = (TextView) rootView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtName.getText().toString().trim().length() > 5 && (edtName.getText().toString().trim().length() < 50)) {
                    btnSave.setBackgroundColor(getResources().getColor(R.color.white));
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setBackgroundColor(getResources().getColor(R.color.blue));
                    btnSave.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                ((LoginActivity) context).closeExtendView();
                break;

            case R.id.btn_save:
                saveUser();
                Toast.makeText(context, "wellcome " + edtName.getText().toString() + " to hozo", Toast.LENGTH_SHORT).show();
                ((LoginActivity) context).startActivityAndClearAllTask(MainActivity.class);
                break;
        }

    }

    private void saveUser() {
        String name = edtName.getText().toString();
        HashMap<String, String> dataRequest = new HashMap<>();
        dataRequest.put("full_name", name);
        NetworkUtils.postVolleyFormData(true, true, true, context, NetworkConfig.API_NAME, dataRequest, new NetworkUtils.NetworkListener() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                LogUtils.d(TAG, "dataRequest" + jsonResponse.toString());
                try {
                    if (jsonResponse.getInt("code") == 0) {
                        JSONObject object = new JSONObject(getStringInJsonObj(jsonResponse, "data"));
                        JSONObject mObject = new JSONObject(getStringInJsonObj(object, "user"));

                        UserEntity userEntity = UserManager.getUserLogin(getContext());

                        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
                        realm.beginTransaction();
                        userEntity.setFullName(getStringInJsonObj(mObject, "full_name"));
                        realm.commitTransaction();

                        UserManager.insertUserLogin(userEntity, getContext());

                        if ((getStringInJsonObj(mObject, "full_name").trim()).equalsIgnoreCase("") || getStringInJsonObj(mObject, "full_name").trim() == null) {
                            LogUtils.d(TAG, "name_check" + getStringInJsonObj(mObject, "full_name").trim());
                            ((LoginActivity) context).showExtendView(NAME_VIEW);
                        } else {
                            ((LoginActivity) context).startActivityAndClearAllTask(MainActivity.class);
                        }
                    } else if (jsonResponse.getInt("code") == 1) {
                        Toast.makeText(context, "FullName is empty", Toast.LENGTH_SHORT).show();

                    } else if (jsonResponse.getInt("code") == 2) {
                        Toast.makeText(context, "Account is not exist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, " Not update full name", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError() {

            }
        });
    }

}