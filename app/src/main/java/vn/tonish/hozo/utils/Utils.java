package vn.tonish.hozo.utils;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.SmsManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.tonish.hozo.R;

/**
 * Created by LongBui.
 */
public class Utils {

    private static final String TAG = Utils.class.getName();

    public static String md5(String data) {
        StringBuilder hexString = new StringBuilder();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash;
        try {
            hash = md != null ? md.digest() : new byte[0];
            for (byte aHash : hash) {
                if ((0xff & aHash) < 0x10) {
                    hexString.append("0"
                            + Integer.toHexString((0xFF & aHash)));
                } else {
                    hexString.append(Integer.toHexString(0xFF & aHash));
                }
            }
            return hexString.toString();
        } catch (NullPointerException e) {

        }
        return null;

    }

    public static void displayImage(Context context, ImageView img, String url) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
    }


    public static void displayImageAvatar(Context context, ImageView img, String url) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.avatar_default)
                .into(img);
    }

    public static void displayImageHolder(Context context, ImageView img, String url) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(vn.tonish.hozo.R.drawable.loading)
                .into(img);
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void hideKeyBoard(Activity context) {

        if (context == null) return;
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftKeyboard(Context context, EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }


    public static void showSoftKeyboard(Context context, EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }


    public static void showLongToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void sendSMS(Context context, String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);
        sms.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
    }

    public static String formatNumber(int input) {
        DecimalFormat myFormatter = new DecimalFormat("###,###.###");
        return myFormatter.format(input);
    }

    public static boolean isNullOrEmpty(Object obj) {
        String inputString = String.valueOf(obj);
        return obj == null || (inputString.isEmpty() || inputString.equals("null"));
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static String getStringInJsonObj(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            try {
                String e = String.valueOf(jsonObject.get(key));
                return e.equalsIgnoreCase("null") ? "" : e;
            } catch (JSONException var3) {
                return "";
            }
        } else {
            return "";
        }
    }

    @SuppressWarnings("deprecation")
    public static void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    public static void compressBitmapToFile(Bitmap bmp, String path) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
