package vn.tonish.hozo.utils;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.BuildConfig;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.SplashActivity;
import vn.tonish.hozo.common.Constants;
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

        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_placeholder)
                .dontAnimate()
                .into(img);
    }

    public static void displayImageCenterCrop(Context context, ImageView img, String url) {

        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_placeholder)
                .dontAnimate()
                .centerCrop()
                .into(img);
    }

    public static void displayImageAvatar(Context context, ImageView img, String url) {

        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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


    public static void showLongToast(Context context, String content, boolean isError, boolean isShort) {
        if (context == null) return;

        if (context instanceof Activity)
            if (((Activity) context).isFinishing()) {
                return;
            }

        showToastCustom(context, content, isError, isShort);
    }

    private static void showToastCustom(Context context, String content,
                                        boolean isError, boolean isShort) {
        Toast toastCustom = new Toast(context);
        ViewGroup viewGroup = (ViewGroup) toastCustom.getView();
        View viewToastCustom;
        if (isError) {
            viewToastCustom = LayoutInflater.from(context).inflate(
                    R.layout.toast_custom_warning, viewGroup);
        } else {
            viewToastCustom = LayoutInflater.from(context).inflate(
                    R.layout.toast_custom_info, viewGroup);
        }
        toastCustom.setDuration(isShort ? Toast.LENGTH_SHORT
                : Toast.LENGTH_LONG);
        toastCustom.setGravity(Gravity.BOTTOM, 0,
                (int) PxUtils.pxFromDp(context, context.getResources().getDimension(R.dimen.toast_offset)));
        toastCustom.setMargin(0, 0);
        toastCustom.setView(viewToastCustom);
        ((TextViewHozo) viewToastCustom.findViewById(R.id.toastDescription))
                .setText(content);
        toastCustom.show();
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
            if (bmp != null) bmp.recycle();
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
                    try {
                        Matrix mat = new Matrix();
                        mat.postRotate(angle);

                        //maybe out of memory when create bitmap so need scale bitmap before create and rotate bitmap
                        Bitmap bmpScale = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale), false);
                        bitmap = Bitmap.createBitmap(bmpScale, 0, 0, bmpScale.getWidth(), bmpScale.getHeight(), mat, true);

//                    Bitmap correctBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
//                    bitmap = Bitmap.createScaledBitmap(correctBmp, (int) (correctBmp.getWidth() / scale), (int) (correctBmp.getHeight() / scale), false);

                        bmpScale.recycle();
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                } else {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scale), (int) (bitmap.getHeight() / scale), false);
                }

                fileOut = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
                Utils.compressBitmapToFile(bitmap, fileOut.getPath());

//                //recycle bitmap
//                bitmap.recycle();

                return fileOut;
            } catch (Exception e) {
                LogUtils.e(TAG, "compressFile , error : " + e.getMessage());
                return fileIn;
            }

        } else {
            if (!fileIn.getName().endsWith("jpg")) {
                try {
                    File fileOut;

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
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
                    }

                    fileOut = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
                    Utils.compressBitmapToFile(bitmap, fileOut.getPath());

//                    bitmap.recycle();
                    return fileOut;

                } catch (IOException e) {
                    e.printStackTrace();
                    return fileIn;
                }
            } else
                return fileIn;

        }
    }

    public static Bitmap scaleBitmap(Bitmap bmInput, int maxsize) {
        if (bmInput.getWidth() > maxsize || bmInput.getHeight() > maxsize) {
            float scale = bmInput.getWidth() > bmInput.getHeight() ? bmInput.getWidth() / maxsize : bmInput.getHeight() / maxsize;
            return Bitmap.createScaledBitmap(bmInput, (int) (bmInput.getWidth() / scale), (int) (bmInput.getHeight() / scale), false);
        } else
            return bmInput;
    }

    public static void call(Context context, String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            context.startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
            showLongToast(context, context.getString(R.string.call_err), true, false);
        }
    }


    private static final int MAX_LENGTH_TASK_NAME = 80;

    public static void setContentMessage(Context context, TextViewHozo tvContent, Notification notification) {

        String content = "";
        String matcher = "";
        String matcherColor = "#00A2E5";

        if (notification.getTaskName().length() > MAX_LENGTH_TASK_NAME)
            notification.setTaskName(notification.getTaskName().substring(0, MAX_LENGTH_TASK_NAME) + "...");

        switch (notification.getEvent()) {
            case Constants.PUSH_TYPE_REVIEW_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_received);
                matcher = context.getString(R.string.notification_review_received_matcher);
                matcherColor = context.getString(R.string.notification_review_received_color);
                break;
            case Constants.PUSH_TYPE_COMMENT_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                matcher = context.getString(R.string.notification_comment_received_matcher);
                matcherColor = context.getString(R.string.notification_comment_received_color);
                break;
            case Constants.PUSH_TYPE_BIDDER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                matcher = context.getString(R.string.notification_bidder_canceled_matcher);
                matcherColor = context.getString(R.string.notification_bidder_canceled_color);
                break;
            case Constants.PUSH_TYPE_BID_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                matcher = context.getString(R.string.notification_bid_received_matcher);
                matcherColor = context.getString(R.string.notification_bid_received_color);
                break;
            case Constants.PUSH_TYPE_BID_ACCEPTED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_accepted) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                matcher = context.getString(R.string.notification_bid_accepted_matcher);
                matcherColor = context.getString(R.string.notification_bid_accepted_color);
                break;
            case Constants.PUSH_TYPE_TASK_REMINDER:
                content = context.getString(R.string.work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_reminder);
                matcher = context.getString(R.string.notification_task_reminder_matcher);
                matcherColor = context.getString(R.string.notification_task_reminder_color);
                break;
            case Constants.PUSH_TYPE_NEW_TASK_ALERT:
                content = notification.getFullName() + " " + context.getString(R.string.notification_new_task_alert) + " " + notification.getTaskName() + " " + context.getString(R.string.push_alert);
                matcher = context.getString(R.string.notification_new_task_alert_matcher);
                matcherColor = context.getString(R.string.notification_new_task_alert_color);
                break;
            case Constants.PUSH_TYPE_POSTER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_poster_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.push_you_bidded);
                matcher = context.getString(R.string.notification_poster_canceled_matcher);
                matcherColor = context.getString(R.string.notification_poster_canceled_color);
                break;
            case Constants.PUSH_TYPE_TASK_COMPLETE:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task) + " " + context.getString(R.string.notification_task_completed);
                matcher = context.getString(R.string.notification_task_completed_matcher);
                matcherColor = context.getString(R.string.notification_task_completed_color);
                break;
            case Constants.PUSH_TYPE_TASK_OVERDUE:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task) + " " + context.getString(R.string.notification_task_overdue);
                matcher = context.getString(R.string.notification_task_overdue_matcher);
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            case Constants.PUSH_TYPE_BID_MISSED:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_bid_missed);
                matcher = context.getString(R.string.notification_bid_missed_matcher);
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            case Constants.PUSH_TYPE_TASK_REOPEN:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_reopen);
                matcher = context.getString(R.string.notification_task_reopen_matcher);
                matcherColor = context.getString(R.string.notification_task_completed_color);
                break;
            case Constants.PUSH_TYPE_ADMIN_PUSH:
                content = notification.getContent();
                matcher = content;
                matcherColor = context.getString(R.string.notification_task_completed_color);
                break;
        }

        tvContent.setText(content);

        SpannableString spannable = new SpannableString(tvContent.getText().toString());

        Pattern patternId = Pattern.compile(matcher);
        Matcher matcherId = patternId.matcher(tvContent.getText().toString());
        while (matcherId.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), matcherId.start(), matcherId.end(), 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor(matcherColor)), matcherId.start(), matcherId.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        String authorName = replacePattern(notification.getFullName());
        Pattern patternIdName = Pattern.compile(authorName);
        Matcher matcherIdName = patternIdName.matcher(tvContent.getText().toString());
        while (matcherIdName.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), matcherIdName.start(), matcherIdName.end(), 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), matcherIdName.start(), matcherIdName.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        String matcherTaskName = replacePattern(notification.getTaskName());
        Pattern patternIdTaskName = Pattern.compile(matcherTaskName);
        Matcher matcherIdTaskName = patternIdTaskName.matcher(tvContent.getText().toString());
        while (matcherIdTaskName.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), matcherIdTaskName.start(), matcherIdTaskName.end(), 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), matcherIdTaskName.start(), matcherIdTaskName.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvContent.setText(spannable);
        tvContent.setContentDescription(spannable);
    }

    public static String replacePattern(String input) {
        String result = "";

        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '$'
                    || input.charAt(i) == '&'
                    || input.charAt(i) == '+'
                    || input.charAt(i) == ','
                    || input.charAt(i) == ':'
                    || input.charAt(i) == ';'
                    || input.charAt(i) == '='
                    || input.charAt(i) == '?'
                    || input.charAt(i) == '@'
                    || input.charAt(i) == '#'
                    || input.charAt(i) == '\''
                    || input.charAt(i) == '<'
                    || input.charAt(i) == '>'
                    || input.charAt(i) == '|'
                    || input.charAt(i) == '.'
                    || input.charAt(i) == '^'
                    || input.charAt(i) == '('
                    || input.charAt(i) == ')'
                    || input.charAt(i) == '%'
                    || input.charAt(i) == '!'
                    || input.charAt(i) == '-'
                    ) {
                result = result + "\\";
            }
            result = result + input.charAt(i);
        }
        return result;
    }

    public static String getContentFromNotification(Context context, Notification notification) {

        String content = "";

        if (notification.getTaskName() != null && notification.getTaskName().length() > MAX_LENGTH_TASK_NAME)
            notification.setTaskName(notification.getTaskName().substring(0, MAX_LENGTH_TASK_NAME) + "...");

        switch (notification.getEvent()) {
            case Constants.PUSH_TYPE_REVIEW_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_received);
                break;
            case Constants.PUSH_TYPE_COMMENT_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                break;
            case Constants.PUSH_TYPE_BIDDER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                break;
            case Constants.PUSH_TYPE_BID_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                break;
            case Constants.PUSH_TYPE_BID_ACCEPTED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_accepted) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                break;
            case Constants.PUSH_TYPE_TASK_REMINDER:
                content = context.getString(R.string.work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_reminder);
                break;
            case Constants.PUSH_TYPE_NEW_TASK_ALERT:
                content = notification.getFullName() + " " + context.getString(R.string.notification_new_task_alert) + " " + notification.getTaskName() + " " + context.getString(R.string.push_alert);
                break;
            case Constants.PUSH_TYPE_POSTER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_poster_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.push_you_bidded);
                break;
            case Constants.PUSH_TYPE_TASK_COMPLETE:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task) + " " + context.getString(R.string.notification_task_completed);
                break;
            case Constants.PUSH_TYPE_TASK_OVERDUE:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task) + " " + context.getString(R.string.notification_task_overdue);
                break;
            case Constants.PUSH_TYPE_BID_MISSED:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_bid_missed);
                break;
            case Constants.PUSH_TYPE_TASK_REOPEN:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_reopen);
                break;
            case Constants.PUSH_TYPE_ADMIN_PUSH:
                content = notification.getContent();
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
        else taskResponses.add(taskResponse);
    }

    public static String converGenderVn(Context context, String gender) {
        String sex;
        if (gender.equals(context.getString(R.string.gender_male)))
            sex = context.getString(R.string.gender_vn_male);
        else if (gender.equals(context.getString(R.string.gender_female))) {
            sex = context.getString(R.string.gender_vn_mafele);
        } else {
            sex = context.getString(R.string.gender_vn_any);
        }
        return sex;
    }

    public static String converGenderEn(Context context, String gender) {
        String sex;
        if (gender.equals(context.getString(R.string.gender_vn_male)))
            sex = context.getString(R.string.gender_male);
        else if (gender.equals(context.getString(R.string.gender_vn_mafele))) {
            sex = context.getString(R.string.gender_female);
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

    public static void cancelNotification(Context context, int notifyId) {
        String notificationService = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(notificationService);
        notificationManager.cancel(notifyId);
    }

    // must call in oncreate of activity or fragment
    public static void cancelAllNotification(Context context) {
        NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    public static void sendMail(Context context, String mailTo) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", mailTo, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.support_mail_subject));
            emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.support_mail_body));
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (Exception e) {
            e.printStackTrace();
            showLongToast(context, context.getString(R.string.send_mail_error), true, false);
        }
    }

    public static void openBrowser(Context context, String url) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            showLongToast(context, context.getString(R.string.open_brower_error), true, false);
        }
    }

    public static String getCurrentVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return "";

    }

    public static void blockUser(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void shareTask(Context context, int taskId) {
        String url = BuildConfig.SHARE_URL + base64(String.valueOf(taskId));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        context.startActivity(Intent.createChooser(intent, "Share"));
    }

    public static String base64(String input) {
        String outPut = "";
        byte[] data = new byte[0];
        try {
            data = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outPut = Base64.encodeToString(data, Base64.DEFAULT);
        return outPut;
    }

}
