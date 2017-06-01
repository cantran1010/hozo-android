package vn.tonish.hozo.utils;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/12/17.
 */
public class Utils {

    private static final String TAG = Utils.class.getName();
    private static final int MAXSIZE = 1000;
    public static final int MAXSIZE_AVATA = 300;

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

    public static String formatNumber(int input) {
        DecimalFormat myFormatter = new DecimalFormat("###,###.###");
        return myFormatter.format(input);
    }

    public static String formatNumber(Long input) {
        DecimalFormat myFormatter = new DecimalFormat("###,###.###");
        return myFormatter.format(input);
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
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
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

    public static File compressFile(File fileIn) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(fileIn.getPath(), options);
        LogUtils.d(TAG, "compressFile , width : " + bitmap.getWidth() + " , height : " + bitmap.getHeight());
        if (bitmap.getWidth() > MAXSIZE || bitmap.getHeight() > MAXSIZE) {
            float scale = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getWidth() / MAXSIZE : bitmap.getHeight() / MAXSIZE;
            try {
                File fileOut;
                Bitmap destinationBitmap;
                Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(fileIn), null, null);

                // fix right orientation of image after capture

                ExifInterface exif = new ExifInterface(fileIn.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                int angle = 0;

                if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                    angle = 90;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                    angle = 180;
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                    angle = 270;
                }

                if (angle != 0) {
                    Matrix mat = new Matrix();
                    mat.postRotate(angle);

                    Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
                    destinationBitmap = bitmap.createScaledBitmap(correctBmp, (int) (correctBmp.getWidth() / scale), (int) (correctBmp.getHeight() / scale), false);
                } else {
                    destinationBitmap = bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / scale), (int) (bmp.getHeight() / scale), false);
                }


                fileOut = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
                Utils.compressBitmapToFile(destinationBitmap, fileOut.getPath());
                return fileOut;
            } catch (Exception e) {
                LogUtils.e(TAG, "compressFile , error : " + e.getMessage());
                return fileIn;
            }

        } else
            return fileIn;
    }

    public static Bitmap scaleBitmap(Bitmap bmInput, int maxsize) {
        if (bmInput.getWidth() > maxsize || bmInput.getHeight() > maxsize) {
            float scale = bmInput.getWidth() > bmInput.getHeight() ? bmInput.getWidth() / maxsize : bmInput.getHeight() / maxsize;
            return bmInput.createScaledBitmap(bmInput, (int) (bmInput.getWidth() / scale), (int) (bmInput.getHeight() / scale), false);
        } else
            return bmInput;
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

        switch (notification.getEvent()) {
            case "review_received":
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_received);
                matcher = context.getString(R.string.notification_review_received_matcher);
                matcherColor = context.getString(R.string.notification_review_received_color);
                break;
            case "comment_received":
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received);
                matcher = context.getString(R.string.notification_comment_received_matcher);
                matcherColor = context.getString(R.string.notification_comment_received_color);
                break;
            case "bidder_canceled":
                content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled);
                matcher = context.getString(R.string.notification_bidder_canceled_matcher);
                matcherColor = context.getString(R.string.notification_bidder_canceled_color);
                break;
            case "bid_received":
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received);
                matcher = context.getString(R.string.notification_bid_received_matcher);
                matcherColor = context.getString(R.string.notification_bid_received_color);
                break;
            case "bid_accepted":
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_accepted);
                matcher = context.getString(R.string.notification_bid_accepted_matcher);
                matcherColor = context.getString(R.string.notification_bid_accepted_color);
                break;
            case "task_reminder":
                content = notification.getFullName() + " " + context.getString(R.string.notification_task_reminder);
                matcher = context.getString(R.string.notification_task_reminder_matcher);
                matcherColor = context.getString(R.string.notification_task_reminder_color);
                break;
            case "new_task_alert":
                content = notification.getFullName() + " " + context.getString(R.string.notification_new_task_alert);
                matcher = context.getString(R.string.notification_new_task_alert_matcher);
                matcherColor = context.getString(R.string.notification_new_task_alert_color);
                break;
            case "poster_canceled":
                content = notification.getFullName() + " " + context.getString(R.string.notification_poster_canceled);
                matcher = context.getString(R.string.notification_poster_canceled_matcher);
                matcherColor = context.getString(R.string.notification_poster_canceled_color);
                break;
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

    public static String getContentFromNotification(Context context, Notification notification) {

        String content = "";

        switch (notification.getEvent()) {
            case "review_received":
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_received);
                break;
            case "comment_received":
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received);
                break;
            case "bidder_canceled":
                content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled);
                break;
            case "bid_received":
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received);
                break;
            case "bid_accepted":
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_accepted);
                break;
            case "task_reminder":
                content = notification.getFullName() + " " + context.getString(R.string.notification_task_reminder);
                break;
            case "new_task_alert":
                content = notification.getFullName() + " " + context.getString(R.string.notification_new_task_alert);
                break;
            case "poster_canceled":
                content = notification.getFullName() + " " + context.getString(R.string.notification_poster_canceled);
                break;
        }

        return content;
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

    public static String converGenderVn(Context context, String gender) {
        String sex = "";
        if (gender.equals(context.getString(R.string.gender_male)))
            sex = context.getString(R.string.gender_vn_male);
        else if (gender.equals(context.getString(R.string.gender_mafele))) {
            sex = context.getString(R.string.gender_vn_mafele);
        } else {
            sex = context.getString(R.string.gender_vn_any);
        }
        return sex;
    }

    public static String converGenderEn(Context context, String gender) {
        String sex = "";
        if (gender.equals(context.getString(R.string.gender_vn_male)))
            sex = context.getString(R.string.gender_male);
        else if (gender.equals(context.getString(R.string.gender_vn_mafele))) {
            sex = context.getString(R.string.gender_mafele);
        } else {
            sex = context.getString(R.string.gender_any);
        }
        return sex;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
