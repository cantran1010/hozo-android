package vn.tonish.hozo.utils;


import android.animation.TimeInterpolator;
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
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.BuildConfig;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.SplashActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.common.DataParse;
import vn.tonish.hozo.database.entity.CategoryEntity;
import vn.tonish.hozo.database.entity.SettingEntiny;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.database.manager.SettingManager;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.common.Constants.ACCELERATE_DECELERATE_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.ACCELERATE_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.ANTICIPATE_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.ANTICIPATE_OVERSHOOT_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.BOUNCE_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.DECELERATE_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.FAST_OUT_LINEAR_IN_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.FAST_OUT_SLOW_IN_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.LINEAR_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.LINEAR_OUT_SLOW_IN_INTERPOLATOR;
import static vn.tonish.hozo.common.Constants.OVERSHOOT_INTERPOLATOR;
import static vn.tonish.hozo.database.manager.CategoryManager.checkCategoryById;

/**
 * Created by LongBui on 4/12/17.
 */
public class Utils {

    private static final String TAG = Utils.class.getName();
    private static final int MAXSIZE = 1000;
    public static final int MAXSIZE_AVATA = 300;

    public static void displayImage(Context context, ImageView img, String url) {

        if (url == null) return;
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        Glide.with(context.getApplicationContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_placeholder)
                .dontAnimate()
                .into(img);
    }

    public static void displayImageCenterCrop(Context context, ImageView img, String url) {

        if (url == null) return;
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        Glide.with(context.getApplicationContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_placeholder)
                .dontAnimate()
                .centerCrop()
                .into(img);
    }

    public static void displayImageAvatar(Context context, ImageView img, String url) {

        if (url == null) return;
        if (context == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        Glide.with(context.getApplicationContext()).load(url)
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
        Bitmap bitmap;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(fileIn.getPath(), options);
        } catch (Exception e) {
            e.printStackTrace();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeFile(fileIn.getPath(), options);
        }

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
                if (notification.getRelatedCount() == 1) {
                    content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName();
                } else {
                    content = notification.getFullName() + " " + context.getString(R.string.notification_related, notification.getRelatedCount() - 1) + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName();
                }
                matcher = context.getString(R.string.notification_comment_received_matcher);
                matcherColor = context.getString(R.string.notification_comment_received_color);
                break;
            case Constants.PUSH_TYPE_BIDDER_CANCELED:
                if (notification.getRelatedCount() == 1)
                    content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                else
                    content = notification.getFullName() + " " + context.getString(R.string.notification_related, notification.getRelatedCount() - 1) + " " + context.getString(R.string.notification_bidder_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                matcher = context.getString(R.string.notification_bidder_canceled_matcher);
                matcherColor = context.getString(R.string.notification_bidder_canceled_color);
                break;
            case Constants.PUSH_TYPE_BID_RECEIVED:
                if (notification.getRelatedCount() == 1)
                    content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                else
                    content = notification.getFullName() + " " + context.getString(R.string.notification_related, notification.getRelatedCount() - 1) + " " + context.getString(R.string.notification_bid_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                matcher = context.getString(R.string.notification_bid_received_matcher);
                matcherColor = context.getString(R.string.notification_bid_received_color);
                break;
            case Constants.PUSH_TYPE_BID_ACCEPTED:
                if (notification.getRelatedCount() == 1)
                    content = notification.getFullName() + " " + context.getString(R.string.notification_bid_accepted) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                else
                    content = notification.getFullName() + " " + context.getString(R.string.notification_bid_accepted) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task) + " " + context.getString(R.string.notification_related, notification.getRelatedCount() - 1);
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

                if (notification.getUserId() == UserManager.getMyUser().getId())
                    content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_completed);
                else
                    content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_completed_bidder);

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
            case Constants.PUSH_TYPE_BID_CANCEL:
                content = notification.getFullName() + " " + context.getString(R.string.notification_cancel_bid) + " " + context.getString(R.string.cancel_bid_task_name) + notification.getTaskName();
                matcher = context.getString(R.string.notification_cancel_bid_matcher);
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            case Constants.PUSH_TYPE_COMMENT_REPLIED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_replied) + " " + notification.getTaskName();
                matcher = context.getString(R.string.notification_comment_replied_matcher);
                matcherColor = context.getString(R.string.notification_comment_replied_color);
                break;
            case Constants.PUSH_TYPE_ENOUGH_BIDDER:
                content = context.getString(R.string.nofification_enouth_bidder_title) + " " + notification.getTaskName() + " " + context.getString(R.string.nofification_enouth_bidder_footer);
                matcher = context.getString(R.string.notification_enough_bidder_matcher);
                matcherColor = context.getString(R.string.notification_enough_bidder_color);
                break;
        }

        tvContent.setText(content);

        SpannableStringBuilder spannable = new SpannableStringBuilder(tvContent.getText().toString());

//        Pattern patternId = Pattern.compile(matcher);
//        Matcher matcherId = patternId.matcher(tvContent.getText().toString());

        int fromMatcher = tvContent.getText().toString().indexOf(matcher);
        int toMatcher = fromMatcher + matcher.length();
        if (fromMatcher >= 0) {
//            while (matcherId.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), fromMatcher, toMatcher, 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor(matcherColor)), fromMatcher, toMatcher, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
        }

//        String authorName = replacePattern(notification.getFullName());
//        Pattern patternIdName = Pattern.compile(authorName);
//        Matcher matcherIdName = patternIdName.matcher(tvContent.getText().toString());

        int fromAuthorName = tvContent.getText().toString().indexOf(notification.getFullName());
        int toAuthorName = fromAuthorName + notification.getFullName().length();

        if (fromAuthorName >= 0) {
//            while (matcherIdName.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), fromAuthorName, toAuthorName, 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), fromAuthorName, toAuthorName, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
        }

//        String matcherTaskName = replacePattern(notification.getTaskName());
//        Pattern patternIdTaskName = Pattern.compile(matcherTaskName);
//        Matcher matcherIdTaskName = patternIdTaskName.matcher(tvContent.getText().toString());

        int fromTaskName = tvContent.getText().toString().indexOf(notification.getTaskName());
        int toTaskName = fromTaskName + notification.getTaskName().length();
        if (fromTaskName >= 0) {
//        while (matcherIdTaskName.find()) {
            spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), fromTaskName, toTaskName, 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), fromTaskName, toTaskName, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        }

        tvContent.setText(spannable);
        tvContent.setContentDescription(spannable);
    }

//    public static String replacePattern(String input) {
//        String result = "";
//
//        for (int i = 0; i < input.length(); i++) {
//            if (input.charAt(i) == '$'
//                    || input.charAt(i) == '&'
//                    || input.charAt(i) == '+'
//                    || input.charAt(i) == ','
//                    || input.charAt(i) == ':'
//                    || input.charAt(i) == ';'
//                    || input.charAt(i) == '='
//                    || input.charAt(i) == '?'
//                    || input.charAt(i) == '@'
//                    || input.charAt(i) == '#'
//                    || input.charAt(i) == '\''
//                    || input.charAt(i) == '<'
//                    || input.charAt(i) == '>'
//                    || input.charAt(i) == '|'
//                    || input.charAt(i) == '.'
//                    || input.charAt(i) == '^'
//                    || input.charAt(i) == '('
//                    || input.charAt(i) == ')'
//                    || input.charAt(i) == '%'
//                    || input.charAt(i) == '!'
//                    || input.charAt(i) == '-'
//                    ) {
//                result = result + "\\";
//            }
//            result = result + input.charAt(i);
//        }
//        return result;
//    }

    public static String getContentFromNotification(Context context, Notification notification) {

        String content = "";

        if (notification.getTaskName() != null && notification.getTaskName().length() > MAX_LENGTH_TASK_NAME)
            notification.setTaskName(notification.getTaskName().substring(0, MAX_LENGTH_TASK_NAME) + "...");

        switch (notification.getEvent()) {
            case Constants.PUSH_TYPE_REVIEW_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_received);
                break;
            case Constants.PUSH_TYPE_COMMENT_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName();
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

                if (notification.getUserId() == UserManager.getMyUser().getId())
                    content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_completed);
                else
                    content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_completed_bidder);

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
            case Constants.PUSH_TYPE_BID_CANCEL:
                content = notification.getFullName() + " " + context.getString(R.string.notification_cancel_bid) + " " + context.getString(R.string.cancel_bid_task_name) + notification.getTaskName();
                break;
            case Constants.PUSH_TYPE_COMMENT_REPLIED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_replied) + " " + notification.getTaskName();
                break;
            case Constants.PUSH_TYPE_CHAT:
                content = notification.getFullName() + " " + context.getString(R.string.notification_chat) + " " + notification.getTaskName();
                break;
            case Constants.PUSH_TYPE_ENOUGH_BIDDER:
                content = context.getString(R.string.nofification_enouth_bidder_title) + " " + notification.getTaskName() + " " + context.getString(R.string.nofification_enouth_bidder_footer);
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
        LogUtils.d(TAG, "shareTask , url : " + url);
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

    public static void settingDefault(Context context) {
        if (SettingManager.getSettingEntiny() != null) return;

        LogUtils.d(TAG, "settingDefault start");
        SettingEntiny settingEntiny = new SettingEntiny();
        settingEntiny.setUserId(UserManager.getMyUser().getId());
        settingEntiny.setLatitude(21.028511);
        settingEntiny.setLongitude(105.804817);
        settingEntiny.setLocation("");
        settingEntiny.setCity("");
        settingEntiny.setRadius(0);
        settingEntiny.setGender(context.getString(R.string.gender_vn_any));
        settingEntiny.setMinWorkerRate(0);
        settingEntiny.setMaxWorkerRate(0);
        SettingManager.insertSetting(settingEntiny);
    }

    public static boolean validateInput(Context context, String input) {
//        if (input.contains(context.getString(R.string.email_input_error1))) return false;
//        if (input.toLowerCase(Locale.getDefault()).contains(context.getString(R.string.email_input_error2)))
//            return false;

//        String emailPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}";
//        if (input.matches(emailPattern)) return false;

        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(input);
        while (m.find()) {
            return false;
        }

        Iterator<PhoneNumberMatch> existsPhone = PhoneNumberUtil.getInstance().findNumbers(input, "VN").iterator();
        while (existsPhone.hasNext()) {
            return false;
        }

        return true;
    }

    public static void updateRole(TaskResponse taskResponse) {

        //fix crash on fabric -> I don't know why crash :(
        if (UserManager.getMyUser() == null) return;

        //poster
        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            taskResponse.setRole(Constants.ROLE_POSTER);
        }
        //bidder
        else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)
                || (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED))
                || (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED))
                || taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)
                || taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
            taskResponse.setRole(Constants.ROLE_TASKER);
        }
        // find task
        else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) && taskResponse.getOfferStatus().equals("")) {
            taskResponse.setRole(Constants.ROLE_FIND_TASK);
        }
    }

    public static void sendSms(Context context, String phoneNumber, String content) {
        try {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phoneNumber);
            smsIntent.putExtra("sms_body", content);
            context.startActivity(smsIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TimeInterpolator createInterpolator(@IntRange(from = 0, to = 10) final int interpolatorType) {
        switch (interpolatorType) {
            case ACCELERATE_DECELERATE_INTERPOLATOR:
                return new AccelerateDecelerateInterpolator();
            case ACCELERATE_INTERPOLATOR:
                return new AccelerateInterpolator();
            case ANTICIPATE_INTERPOLATOR:
                return new AnticipateInterpolator();
            case ANTICIPATE_OVERSHOOT_INTERPOLATOR:
                return new AnticipateOvershootInterpolator();
            case BOUNCE_INTERPOLATOR:
                return new BounceInterpolator();
            case DECELERATE_INTERPOLATOR:
                return new DecelerateInterpolator();
            case FAST_OUT_LINEAR_IN_INTERPOLATOR:
                return new FastOutLinearInInterpolator();
            case FAST_OUT_SLOW_IN_INTERPOLATOR:
                return new FastOutSlowInInterpolator();
            case LINEAR_INTERPOLATOR:
                return new LinearInterpolator();
            case LINEAR_OUT_SLOW_IN_INTERPOLATOR:
                return new LinearOutSlowInInterpolator();
            case OVERSHOOT_INTERPOLATOR:
                return new OvershootInterpolator();
            default:
                return new LinearInterpolator();
        }
    }

    public static void inserCategory(List<Category> categoryList) {
        List<CategoryEntity> list;
        for (Category category : categoryList) {
            category.setSelected(checkCategoryById(category.getId()));
        }
        list = DataParse.convertListCategoryToListCategoryEntity(categoryList);
        CategoryManager.insertCategories(list);
    }

    public static String getAddressFromLatlon(Context context, double lat, double lon) {
        Geocoder geocoder;
        List<Address> addresses;
        String strAdd = "";
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            strAdd = addresses.get(0).getAddressLine(0);  // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAdd;
    }

}
