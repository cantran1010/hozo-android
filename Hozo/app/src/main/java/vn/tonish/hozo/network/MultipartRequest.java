package vn.tonish.hozo.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.CharsetUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

// Complete
public class MultipartRequest extends Request<String> {

    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;

    private final Response.Listener<String> mListener;

    public MultipartRequest(String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener, File file,
                            JSONObject jsonObject) {

        super(Method.POST, url, errorListener);
        this.mListener = listener;

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            entity.setCharset(CharsetUtils.get("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        buildMultipartEntity(file, user);
        httpentity = entity.build();
    }

//    private void buildMultipartEntity(File file, User user) {
//
//        Charset chars = Charset.forName("UTF-8");
//
//        String name = file.getName();
//        entity.addBinaryBody("dealer_image", file, ContentType.create("image/jpeg"), name);
//        try {
//            entity.addPart("birthday", new StringBody(DateTimeUtils.changeFormatDate2(user.getBirthday()), chars));
//            entity.addPart("bank_name", new StringBody(user.getBankName(), chars));
//            entity.addPart("phone_number", new StringBody(user.getPhoneNumber(), chars));
//            entity.addPart("address", new StringBody(user.getDetailAddress(), chars));
//            entity.addPart("province_id", new StringBody(user.getProvinceId() + "", chars));
//            entity.addPart("email", new StringBody(user.getEmail(), chars));
//            entity.addPart("name", new StringBody(user.getName(), chars));
//            entity.addPart("action", new StringBody("join", chars));
//            entity.addPart("district_id", new StringBody(user.getDistrictId() + "", chars));
//            entity.addPart("gender", new StringBody("1", chars));
//            entity.addPart("bank_account", new StringBody(user.getBankAccount(), chars));
//            entity.addPart("bank_account_number", new StringBody(user.getBankAccoutNumber(), chars));
//            entity.addPart("password", new StringBody(user.getPassword(), chars));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public String getBodyContentType() {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {

        try {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}