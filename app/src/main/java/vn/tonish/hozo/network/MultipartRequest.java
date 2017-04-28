package vn.tonish.hozo.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.CharsetUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui.
 */
public class MultipartRequest extends Request<String> {

    private static final String TAG = MultipartRequest.class.getSimpleName();

    private MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    private HttpEntity httpentity;
    private final Response.Listener<String> mListener;
    private Context context;

//    public interface MultipartRequestListener{
//        public void onSuccess();
//        public void onError();
//    }
//
//    private MultipartRequestListener multipartRequestListener;
//
//    public MultipartRequestListener getMultipartRequestListener() {
//        return multipartRequestListener;
//    }
//
//    public void setMultipartRequestListener(MultipartRequestListener multipartRequestListener) {
//        this.multipartRequestListener = multipartRequestListener;
//    }
//
//    Response.ErrorListener err = new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError volleyError) {
//
//        }
//    };
//
//    Response.Listener<String> res =  new Response.Listener<String>() {
//        @Override
//        public void onResponse(String s) {
//
//        }
//    };

    public MultipartRequest(Context context, String url, Response.ErrorListener errorListener,
                            Response.Listener<String> listener, File file) {

        super(Method.POST, url, errorListener);

        LogUtils.d(TAG, "Volley MultipartRequest url : " + url + " , file path : " + file.getPath());

        this.mListener = listener;
        this.context = context;

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            entity.setCharset(CharsetUtils.get("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        buildMultipartEntity(file);
        httpentity = entity.build();
    }

    private void buildMultipartEntity(File file) {
        entity.addBinaryBody("image", file);
    }

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
            e.printStackTrace();
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


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + UserManager.getUserToken(context));
        LogUtils.d(TAG, "Volley  MultipartRequest getHeaders token: " + UserManager.getUserToken(context));
        return headers;
    }

}