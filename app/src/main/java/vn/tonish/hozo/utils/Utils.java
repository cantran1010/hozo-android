package vn.tonish.hozo.utils;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

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
                    hexString.append("0").append(Integer.toHexString((0xFF & aHash)));
                } else {
                    hexString.append(Integer.toHexString(0xFF & aHash));
                }
            }
            return hexString.toString();
        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }
        return null;

    }

    public static void displayImage(Context context, ImageView img, String url) {
        Glide.with(context).load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
    }

    public static void displayImageAvatar(Context context, ImageView img, String url) {
        Glide.with(context).load(url)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.avatar_default)
                .dontAnimate()
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

    public static void hideSoftKeyboard(Context context, EdittextHozo EdittextHozo) {
        if (EdittextHozo == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EdittextHozo.getWindowToken(), 0);
    }


    public static void showSoftKeyboard(Context context, EdittextHozo EdittextHozo) {
        if (EdittextHozo == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(EdittextHozo, 0);
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

    public static String formatNumber(Long input) {
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
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getNameCategoryById(int id) {
        return CategoryManager.getCategoryById(id).getName();
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

    public static void call(Context context, String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
            showLongToast(context, context.getString(R.string.call_err));
        }
    }

    public static void setContentMessage(Context context, TextViewHozo tvContent, Notification notification) {

        String content = "";
        String matcher = "";
        String matcherColor = "#00A2E5";

        if (notification.getEvent().equals("review_received")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_review_received);
            matcher = context.getString(R.string.notification_review_received_matcher);
            matcherColor = context.getString(R.string.notification_review_received_color);
        } else if (notification.getEvent().equals("comment_received")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received);
            matcher = context.getString(R.string.notification_comment_received_matcher);
            matcherColor = context.getString(R.string.notification_comment_received_color);
        } else if (notification.getEvent().equals("bidder_canceled")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled);
            matcher = context.getString(R.string.notification_bidder_canceled_matcher);
            matcherColor = context.getString(R.string.notification_bidder_canceled_color);
        } else if (notification.getEvent().equals("bid_received")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received);
            matcher = context.getString(R.string.notification_bid_received_matcher);
            matcherColor = context.getString(R.string.notification_bid_received_color);
        } else if (notification.getEvent().equals("bid_accepted")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_bid_accepted);
            matcher = context.getString(R.string.notification_bid_accepted_matcher);
            matcherColor = context.getString(R.string.notification_bid_accepted_color);
        } else if (notification.getEvent().equals("task_reminder")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_task_reminder);
            matcher = context.getString(R.string.notification_task_reminder_matcher);
            matcherColor = context.getString(R.string.notification_task_reminder_color);
        } else if (notification.getEvent().equals("new_task_alert")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_new_task_alert);
            matcher = context.getString(R.string.notification_new_task_alert_matcher);
            matcherColor = context.getString(R.string.notification_new_task_alert_color);
        } else if (notification.getEvent().equals("poster_canceled")) {
            content = notification.getFullName() + " " + context.getString(R.string.notification_poster_canceled);
            matcher = context.getString(R.string.notification_poster_canceled_matcher);
            matcherColor = context.getString(R.string.notification_poster_canceled_color);
        }

        tvContent.setText(content);
        SpannableString spannable = new SpannableString(tvContent.getText().toString());
        Pattern patternId = Pattern.compile(matcher);
        Matcher matcherId = patternId.matcher(tvContent.getText().toString());
        while (matcherId.find()) {
            spannable.setSpan(new RelativeSizeSpan(1f), matcherId.start(), matcherId.end(), 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor(matcherColor)), matcherId.start(), matcherId.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        String matcherName = notification.getFullName();
        Pattern patternIdName = Pattern.compile(matcherName);
        Matcher matcherIdName = patternIdName.matcher(tvContent.getText().toString());
        while (matcherIdName.find()) {
            spannable.setSpan(new RelativeSizeSpan(1.1f), matcherIdName.start(), matcherIdName.end(), 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), matcherIdName.start(), matcherIdName.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvContent.setText(spannable);
        tvContent.setContentDescription(spannable);
    }

    public static void checkContainsTaskResponse(List<TaskResponse> taskResponses, TaskResponse taskResponse) {
        boolean isUpdate = false;
        int index = 0;

        for (int i = 0; i < taskResponses.size(); i++) {
            if (taskResponses.get(i).getId() == taskResponse.getId()) {
                isUpdate = true;
                index = i;
                break;
            }
        }

        if (isUpdate) taskResponses.set(index, taskResponse);
        else taskResponses.add(0, taskResponse);
    }

}
