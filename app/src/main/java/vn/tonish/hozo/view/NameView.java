package vn.tonish.hozo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.LoginActivity;
import vn.tonish.hozo.activity.MainActivity;

/**
 * Created by CanTran on 18/04/2017.
 */

public class NameView extends FrameLayout implements View.OnClickListener {
    private final static String TAG = "NameView";
    private final Context context;
    private EditText edtName;
    private TextView btnSave;


    public NameView(Context context) {
        super(context);
        this.context = context;
        initView();
    }


    @SuppressLint("InflateParams")
    private void initView() {
        View rootView = LayoutInflater.from(context).inflate(R.layout.view_name, null);
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
                    btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blue));
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
                ((LoginActivity) context).startActivityAndClearAllTask(MainActivity.class);
                break;
        }

    }

    private void saveUser() {
//        String name = edtName.getText().toString();
//        HashMap<String, String> dataRequest = new HashMap<>();
//        dataRequest.put("full_name", name);
//        NetworkUtils.postVolleyFormData(true, true, true, context, NetworkConfig.API_NAME, dataRequest, new NetworkUtils.NetworkListener() {
//            @Override
//            public void onSuccess(JSONObject jsonResponse) {
//                LogUtils.d(TAG, "dataRequest" + jsonResponse.toString());
//                try {
//                    if (jsonResponse.getInt("code") == 0) {
//                        JSONObject object = new JSONObject(getStringInJsonObj(jsonResponse, "data"));
//                        JSONObject mObject = new JSONObject(getStringInJsonObj(object, "user"));
//                        UserEntity userEntity = UserManager.getUserLogin(getContext());
//                        Realm realm = Realm.getInstance(RealmDbHelper.getRealmConfig(context));
//                        realm.beginTransaction();
//                        userEntity.setFullName(getStringInJsonObj(mObject, "full_name"));
//                        realm.commitTransaction();
//                        LogUtils.d(TAG, "token name " + UserManager.getUserToken(context));
//                        if (getStringInJsonObj(mObject, "full_name").trim().equalsIgnoreCase("")) {
//                            LogUtils.d(TAG, "name_check" + getStringInJsonObj(mObject, "full_name").trim());
//                            ((LoginActivity) context).showExtendView(NAME_VIEW);
//                        } else {
//                            ((LoginActivity) context).startActivityAndClearAllTask(MainActivity.class);
//                        }
//                    } else if (jsonResponse.getInt("code") == 1) {
//                        Toast.makeText(context, "FullName is empty", Toast.LENGTH_SHORT).show();
//
//                    } else if (jsonResponse.getInt("code") == 2) {
//                        Toast.makeText(context, "Account is not exist", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, " Not update full name", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//            @Override
//            public void onError() {
//
//            }
//        });
    }

}