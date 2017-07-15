package vn.tonish.hozo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.TextViewHozo;

public class GiveInforActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = GiveInforActivity.class.getSimpleName();
    private TextViewHozo btnVerifyFaceBook, btnVerifyEmail;
    private CallbackManager callbackmanager;
    private ImageView imgBack;
    private String fbid;

    @Override
    protected int getLayout() {
        return R.layout.activity_give_info;
    }

    @Override
    protected void initView() {
        btnVerifyFaceBook = findViewById(R.id.btn_verify_facebook);
        btnVerifyEmail = findViewById(R.id.btn_verify_email);
        imgBack = findViewById(R.id.img_back);
        btnVerifyFaceBook.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        btnVerifyEmail.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    // Private method to handle Facebook login and callback
    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
//        LoginManager.getInstance().setReadPermissions("public_profile", "user_friends", "user_photos", "email", "user_birthday", "public_profile", "contact_email");

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("onSuccess");
                        String accessToken = loginResult.getAccessToken().getToken();
                        Log.i("accessToken", accessToken);

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                LogUtils.d(TAG, "GraphResponse" + response.toString());
                                // Get facebook data from login
                                Bundle bFacebookData = getFacebookData(object);
                                fbid = bFacebookData.getString("idFacebook");
                                LogUtils.d(TAG, "bFacebookData" + bFacebookData.toString());
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        LogUtils.d(TAG, "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        LogUtils.d(TAG, error.toString());
                    }
                });
    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();
        try {

            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("name"))
                bundle.putString("name", object.getString("name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));


        } catch (JSONException e) {
            Log.d(TAG, "Error parsing JSON");
        }
        return bundle;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_facebook:
                onFblogin();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_verify_email:
               getOpenFacebookIntent(this, "1952073665068033");
                break;
        }

    }

    public static void getOpenFacebookIntent(Context context, String id) {
        LogUtils.d(TAG, "id :" + id);

        Intent intent = null;
        try {
            // get the Facebook app if possible
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + id));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Facebook app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/PROFILENAME"));
        }
        context.startActivity(intent);
    }

}
