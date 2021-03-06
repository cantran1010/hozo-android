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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.media.ExifInterface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.BuildConfig;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.SplashActivity;
import vn.tonish.hozo.adapter.CustomArrayAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.rest.responseRes.TransactionResponse;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 4/12/17.
 */
public class Utils {

    private static final String TAG = Utils.class.getName();
    private static final int MAXSIZE = 1000;
    public static final int MAXSIZE_AVATA = 300;
    private static final int MAX_BUGDET = 1000000;
    private static final int MIN_BUGDET = 10000;

    public static void displayImage(Context context, ImageView img, String url) {
        LogUtils.d(TAG, "onBindViewHolder , url : " + url);
//        if (url == null) return;
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

//        if (url == null) return;
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

    public static void displayImageRounded(Context context, ImageView img, String url, int radius, int margin) {

//        if (url == null) return;
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
                .error(R.drawable.image_placeholder)
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, radius, margin))
                .into(img);
    }

    public static void displayImageAvatar(Context context, ImageView img, String url) {

//        if (url == null) return;
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

    public static void hideSoftKeyboard(Context context, View EdittextHozo) {
        if (EdittextHozo == null)
            return;

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EdittextHozo.getWindowToken(), 0);
    }


    public static void showSoftKeyboard(Context context, View EdittextHozo) {
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

    public static String formatNumber(long input) {
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

        if (phoneNumber == null) {
            showLongToast(context, context.getString(R.string.call_err_null_number), true, false);
            return;
        }

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
        String content;
        String matcher = "";
        String matcherColor = "#00A2E5";

        if (notification.getTaskName() != null && notification.getTaskName().length() > MAX_LENGTH_TASK_NAME)
            notification.setTaskName(notification.getTaskName().substring(0, MAX_LENGTH_TASK_NAME) + "...");

        switch (notification.getEvent()) {
            case Constants.PUSH_TYPE_COMMENT_RECEIVED:
                if (notification.getRelatedCount() == 1) {
                    content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_comment_received_footer);
                } else {
                    content = notification.getFullName() + " " + context.getString(R.string.notification_related, notification.getRelatedCount() - 1) + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_comment_received_footer);
                }
                matcher = context.getString(R.string.notification_comment_received_matcher);
                matcherColor = context.getString(R.string.notification_comment_received_color);
                break;
            case Constants.PUSH_TYPE_BIDDER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                matcher = context.getString(R.string.notification_bidder_canceled_matcher);
                matcherColor = context.getString(R.string.notification_bidder_canceled_color);
                break;
            case Constants.PUSH_TYPE_ASSIGNER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_assign_canceled_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_assign_canceled_footer);
                matcher = context.getString(R.string.notification_assigner_canceled_matcher);
                matcherColor = context.getString(R.string.notification_bidder_canceled_color);
                break;
            case Constants.PUSH_TYPE_BID_RECEIVED:
                if (notification.getRelatedCount() == 1)
                    content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task) + context.getString(R.string.notification_bid_received_footer);
                else
                    content = notification.getFullName() + " " + context.getString(R.string.notification_related, notification.getRelatedCount() - 1) + " " + context.getString(R.string.notification_bid_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task) + context.getString(R.string.notification_bid_received_footer);
                matcher = context.getString(R.string.notification_bid_received_matcher);
                matcherColor = context.getString(R.string.notification_bid_received_color);
                break;
            case Constants.PUSH_TYPE_BID_ACCEPTED:
                content = context.getString(R.string.notification_bid_accepted_title) + " " + notification.getTaskName() + context.getString(R.string.notification_bid_accepted_footer);
                matcher = context.getString(R.string.notification_bid_accepted_matcher);
                matcherColor = context.getString(R.string.notification_bid_accepted_color);
                break;
            case Constants.PUSH_TYPE_TASK_REMINDER:
                content = context.getString(R.string.work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_reminder);
                matcher = context.getString(R.string.notification_task_reminder_matcher);
                matcherColor = context.getString(R.string.notification_task_reminder_color);
                break;
            case Constants.PUSH_TYPE_POSTER_CANCELED:
                content = context.getString(R.string.notification_poster_canceled_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_poster_canceled_footer);
                matcher = context.getString(R.string.notification_poster_canceled_matcher);
                matcherColor = context.getString(R.string.notification_poster_canceled_color);
                break;
            case Constants.PUSH_TYPE_TASK_COMPLETE:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_completed_bidder);
                matcher = context.getString(R.string.notification_task_completed_matcher);
                matcherColor = context.getString(R.string.notification_task_completed_color);
                break;
            case Constants.PUSH_TYPE_TASK_OVERDUE:
                if (notification.getUserId() == UserManager.getMyUser().getId()) {
                    if (notification.getAmount() > 0)
                        content = context.getString(R.string.push_overdue_title) + " \"" + notification.getTaskName() + "\" " + context.getString(R.string.your_task) + " " + context.getString(R.string.push_overdue_footer1) + " " + context.getString(R.string.push_overdue_footer2) + " " + Utils.formatNumber(notification.getAmount()) + "đ " + context.getString(R.string.push_overdue_footer3);
                    else
                        content = context.getString(R.string.push_overdue_title) + " \"" + notification.getTaskName() + "\" " + context.getString(R.string.your_task) + " " + context.getString(R.string.push_overdue_footer1);
                    matcher = context.getString(R.string.notification_task_overdue_matcher);
                    matcherColor = context.getString(R.string.notification_task_overdue_color);
                } else {
                    content = context.getString(R.string.push_overdue_title) + " " + notification.getTaskName() + " " + context.getString(R.string.push_overdue_footer_2);
                    matcher = context.getString(R.string.notification_task_overdue_matcher);
                    matcherColor = context.getString(R.string.notification_task_overdue_color);
                }
                break;
            case Constants.PUSH_TYPE_BID_MISSED:
                content = context.getString(R.string.bid_missed_title) + " \"" + notification.getTaskName() + "\" " + context.getString(R.string.bid_missed_footer);
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

            case Constants.PUSH_TYPE_REVIEW_WORKER:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_content) + " " + notification.getTaskName();
                matcher = context.getString(R.string.notification_review_matcher);
                matcherColor = context.getString(R.string.notification_review_poster_color);
                break;
            case Constants.PUSH_TYPE_REVIEW_POSTER:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_worker_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_review_worker_footer);
                matcher = context.getString(R.string.notification_review_poster_matcher);
                matcherColor = context.getString(R.string.notification_review_poster_color);
                break;
            case Constants.PUSH_TYPE_REVIEW_WORKER_NA:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_received_title) + " " + notification.getTaskName() + context.getString(R.string.notification_review_received_footer);
                matcher = context.getString(R.string.notification_review_received_matcher);
                matcherColor = context.getString(R.string.notification_review_received_color);
                break;
            case Constants.PUSH_TYPE_REVIEW_WORKER_DONE:
                if (notification.getAmount() > 0)
                    content = context.getString(R.string.notification_review_worker_done_title_bonus, notification.getFullName(), notification.getTaskName(), Utils.formatNumber(notification.getAmount()));
                else
                    content = context.getString(R.string.notification_review_worker_done_title, notification.getFullName(), notification.getTaskName());
                matcher = context.getString(R.string.notification_review_worker_matcher);
                matcherColor = context.getString(R.string.notification_review_worker_color);
                break;
            case Constants.PUSH_TYPE_AWAIT_APPROVAL:
                content = context.getString(R.string.nofification_enouth_bidder_title) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task)
                        + " " + context.getString(R.string.notification_await_approval);
                matcher = context.getString(R.string.notification_await_approval_matcher);
                matcherColor = context.getString(R.string.notification_await_approval_color);
                break;
            case Constants.PUSH_TYPE_MONEY_RECEIVED:
                content = context.getString(R.string.nofification_money_received) + " " + notification.getContent();
                matcher = context.getString(R.string.nofification_money_received_matcher);
                matcherColor = context.getString(R.string.notification_await_approval_color);
                break;
            case Constants.PUSH_TYPE_BID_REFUNDED:
                content = context.getString(R.string.notification_bid_refunded_title) + " " + notification.getTaskName();
                matcher = context.getString(R.string.notification_bid_refunded_matcher);
                matcherColor = context.getString(R.string.notification_review_worker_color);
                break;
            case Constants.PUSH_TYPE_WAGE_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.nofification_wage_received, Utils.formatNumber(notification.getAmount()));
                matcher = context.getString(R.string.nofification_wage_received_matcher);
                matcherColor = context.getString(R.string.notification_await_approval_color);
                break;
            case Constants.PUSH_TYPE_POSTING_BONUS_RECEIVED:
                content = context.getString(R.string.nofification_posting_bonus_received, Utils.formatNumber(notification.getAmount()), notification.getTaskName());
                matcher = context.getString(R.string.nofification_posting_bonus_received_matcher);
                matcherColor = context.getString(R.string.notification_await_approval_color);
                break;
            case Constants.PUSH_TYPE_REF_BONUS_RECEIVED:
                content = context.getString(R.string.nofification_ref_bonus_received, Utils.formatNumber(notification.getAmount()));
                matcher = context.getString(R.string.nofification_ref_bonus_received_matcher);
                matcherColor = context.getString(R.string.notification_await_approval_color);
                break;

            case Constants.PUSH_TYPE_TASK_REAPPLIED:
                content = context.getString(R.string.nofification_task_type_reapplied, notification.getTaskName());
                matcher = context.getString(R.string.nofification_task_type_reapplied_matcher);
                matcherColor = context.getString(R.string.notification_await_approval_color);
                break;
            case Constants.NOT_ENOUGH_PREPAID:
                content = context.getString(R.string.not_enough_prepaid_start) + "\"" + notification.getTaskName() + " " + context.getString(R.string.not_enough_prepaid_end);
                matcher = notification.getTaskName();
                matcherColor = context.getString(R.string.notification_await_approval_color);
                break;
            case Constants.TASK_REFUND_PREPAID:
                content = context.getString(R.string.task_refund_prepaid_start) + " " + Utils.formatNumber(notification.getAmount()) + "đ " + context.getString(R.string.task_refund_prepaid_end) + " \"" + notification.getTaskName() + "\" ";
                matcher = Utils.formatNumber(notification.getAmount()) + "đ ";
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            case Constants.BID_PAYMENT_RECEIVED:
                content = context.getString(R.string.bid_payment_received_start) + " " + Utils.formatNumber(notification.getAmount()) + "đ " + context.getString(R.string.bid_payment_received_end) + " " + notification.getTaskName();
                matcher = Utils.formatNumber(notification.getAmount()) + "đ ";
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            case Constants.BID_COMMISSION_RECEIVED:
                content = context.getString(R.string.bid_commission_received, Utils.formatNumber(notification.getAmount()), notification.getTaskName());
                matcher = Utils.formatNumber(notification.getAmount()) + "đ ";
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            case Constants.WORK_DEDUCTED:
                content = context.getString(R.string.work_deducted, Utils.formatNumber(notification.getAmount()), notification.getTaskName());
                matcher = Utils.formatNumber(notification.getAmount()) + "đ ";
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            case Constants.TASK_CONFIRMATION_PENALIZED:
                String wayllet;
                if (notification.getWallet().equals("cash"))
                    wayllet = "ví tiền";
                else wayllet = "ví tài khoản";
                content = context.getString(R.string.task_confirmation_penalized_event, Utils.formatNumber(notification.getAmount()), wayllet, notification.getTaskName());
                matcher = Utils.formatNumber(notification.getAmount()) + "đ ";
                matcherColor = context.getString(R.string.notification_task_overdue_color);
                break;
            default:
                content = notification.getEvent();
                break;
        }

        tvContent.setText(content);

        SpannableStringBuilder spannable = new SpannableStringBuilder(tvContent.getText().toString());

        int fromMatcher = tvContent.getText().toString().indexOf(matcher);
        int toMatcher = fromMatcher + matcher.length();
        if (fromMatcher >= 0) {
            spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), fromMatcher, toMatcher, 0);
            spannable.setSpan(new ForegroundColorSpan(Color.parseColor(matcherColor)), fromMatcher, toMatcher, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (notification.getFullName() != null) {
            int fromAuthorName = tvContent.getText().toString().indexOf(notification.getFullName());
            int toAuthorName = fromAuthorName + notification.getFullName().length();

            if (fromAuthorName >= 0) {
                spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), fromAuthorName, toAuthorName, 0);
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), fromAuthorName, toAuthorName, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        if (notification.getTaskName() != null) {
            int fromTaskName = tvContent.getText().toString().indexOf(notification.getTaskName());
            int toTaskName = fromTaskName + notification.getTaskName().length();
            if (fromTaskName >= 0) {
                spannable.setSpan(new android.text.style.StyleSpan(Typeface.NORMAL), fromTaskName, toTaskName, 0);
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), fromTaskName, toTaskName, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        tvContent.setText(spannable);
        tvContent.setContentDescription(spannable);
    }

    public static String getContentFromNotification(Context context, Notification notification) {
        String content = "";

        if (notification.getTaskName() != null && notification.getTaskName().length() > MAX_LENGTH_TASK_NAME)
            notification.setTaskName(notification.getTaskName().substring(0, MAX_LENGTH_TASK_NAME) + "...");

        switch (notification.getEvent()) {
            case Constants.PUSH_TYPE_COMMENT_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_comment_received) + " " + notification.getTaskName();
                break;
            case Constants.PUSH_TYPE_BIDDER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bidder_canceled) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                break;
            case Constants.PUSH_TYPE_ASSIGNER_CANCELED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_assign_canceled_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_assign_canceled_footer);
                break;
            case Constants.PUSH_TYPE_BID_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.notification_bid_received) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task);
                break;
            case Constants.PUSH_TYPE_BID_ACCEPTED:
                content = context.getString(R.string.notification_bid_accepted_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_bid_accepted_footer);
                break;
            case Constants.PUSH_TYPE_TASK_REMINDER:
                content = context.getString(R.string.work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_reminder);
                break;
            case Constants.PUSH_TYPE_POSTER_CANCELED:
                content = context.getString(R.string.notification_poster_canceled_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_poster_canceled_footer);
                break;
            case Constants.PUSH_TYPE_TASK_COMPLETE:
                content = context.getString(R.string.push_complete_work) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_task_completed);
                break;
            case Constants.PUSH_TYPE_TASK_OVERDUE:
                if (notification.getUserId() == UserManager.getMyUser().getId()) {
                    if (notification.getAmount() > 0)
                        content = context.getString(R.string.push_overdue_title) + " \"" + notification.getTaskName() + "\" " + context.getString(R.string.your_task) + " " + context.getString(R.string.push_overdue_footer1) + " " + context.getString(R.string.push_overdue_footer2) + " " + Utils.formatNumber(notification.getAmount()) + "đ " + context.getString(R.string.push_overdue_footer3);
                    else
                        content = context.getString(R.string.push_overdue_title) + " \"" + notification.getTaskName() + "\" " + context.getString(R.string.your_task) + " " + context.getString(R.string.push_overdue_footer1);

                } else {
                    content = context.getString(R.string.push_overdue_title) + " " + notification.getTaskName() + " " + context.getString(R.string.push_overdue_footer_2);
                }
                break;
            case Constants.PUSH_TYPE_BID_MISSED:
                content = context.getString(R.string.bid_missed_title) + " " + notification.getTaskName() + " " + context.getString(R.string.bid_missed_footer);
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
            case Constants.PUSH_TYPE_ENOUGH_BIDDER:
                content = context.getString(R.string.nofification_enouth_bidder_title) + " " + notification.getTaskName() + " " + context.getString(R.string.nofification_enouth_bidder_footer);
                break;
            case Constants.PUSH_TYPE_REVIEW_WORKER:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_content) + " " + notification.getTaskName();
                break;
            case Constants.PUSH_TYPE_REVIEW_POSTER:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_worker_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_review_worker_footer);
                break;
            case Constants.PUSH_TYPE_REVIEW_WORKER_NA:
                content = notification.getFullName() + " " + context.getString(R.string.notification_review_received_title) + " " + notification.getTaskName() + context.getString(R.string.notification_review_received_footer);
                break;
            case Constants.PUSH_TYPE_REVIEW_WORKER_DONE:
                if (notification.getAmount() > 0)
                    content = context.getString(R.string.notification_review_worker_done_title_bonus, notification.getFullName(), notification.getTaskName(), Utils.formatNumber(notification.getAmount()));
                else
                    content = context.getString(R.string.notification_review_worker_done_title, notification.getFullName(), notification.getTaskName());
                break;
            case Constants.PUSH_TYPE_AWAIT_APPROVAL:
                content = context.getString(R.string.nofification_enouth_bidder_title) + " " + notification.getTaskName() + " " + context.getString(R.string.your_task)
                        + " " + context.getString(R.string.notification_await_approval);
                break;
            case Constants.PUSH_TYPE_MONEY_RECEIVED:
                content = context.getString(R.string.nofification_money_received) + " " + notification.getContent();
                break;
            case Constants.PUSH_TYPE_NEW_TASK_ALERT:
//                content = context.getString(R.string.notification_new_task_alert_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_new_task_alert_day) + " " + DateTimeUtils.getOnlyDateFromIso(notification.getTaskStartTime()) + " " + context.getString(R.string.notification_new_task_alert_footer);
                content = context.getString(R.string.notification_new_task_alert_title_2) + " \"" + notification.getTaskName() + "\" " + context.getString(R.string.notification_new_task_alert_footer);
                break;
            case Constants.PUSH_TYPE_BID_REFUNDED:
                content = context.getString(R.string.notification_bid_refunded_title) + " " + notification.getTaskName();
                break;
            case Constants.PUSH_TYPE_CHAT:
                content = notification.getFullName() + " " + context.getString(R.string.notification_group_chat);
                break;
            case Constants.PUSH_TYPE_PRIVATE_CHAT:
                content = notification.getFullName() + " " + context.getString(R.string.notification_private_chat);
                break;
            case Constants.PUSH_TYPE_WAGE_RECEIVED:
                content = notification.getFullName() + " " + context.getString(R.string.nofification_wage_received, Utils.formatNumber(notification.getAmount()));
                break;
            case Constants.PUSH_TYPE_POSTING_BONUS_RECEIVED:
                content = context.getString(R.string.nofification_posting_bonus_received, Utils.formatNumber(notification.getAmount()), notification.getTaskName());
                break;
            case Constants.PUSH_TYPE_REF_BONUS_RECEIVED:
                content = context.getString(R.string.nofification_ref_bonus_received, Utils.formatNumber(notification.getAmount()));
                break;
            case Constants.PUSH_TYPE_TASK_REAPPLIED:
                content = context.getString(R.string.nofification_task_type_reapplied, notification.getTaskName());
                break;
            case Constants.NOT_ENOUGH_PREPAID:
                content = context.getString(R.string.not_enough_prepaid_start) + "\"" + notification.getTaskName() + " " + context.getString(R.string.not_enough_prepaid_end);
                break;
            case Constants.TASK_REFUND_PREPAID:
                content = context.getString(R.string.task_refund_prepaid_start) + " " + Utils.formatNumber(notification.getAmount()) + "đ " + context.getString(R.string.task_refund_prepaid_end) + " \"" + notification.getTaskName() + "\" ";
                break;
            case Constants.BID_PAYMENT_RECEIVED:
                content = context.getString(R.string.bid_payment_received_start) + " " + Utils.formatNumber(notification.getAmount()) + "đ " + context.getString(R.string.bid_payment_received_end) + " " + notification.getTaskName();
                break;
            case Constants.BID_COMMISSION_RECEIVED:
                content = context.getString(R.string.bid_commission_received, Utils.formatNumber(notification.getAmount()), notification.getTaskName());
                break;
            case Constants.WORK_DEDUCTED:
                content = context.getString(R.string.work_deducted, Utils.formatNumber(notification.getAmount()), notification.getTaskName());
                break;

        }

        return content;
    }

    public static String converGenderVn(Context context, String gender) {
        String sex;
        if (gender != null && gender.equals(context.getString(R.string.gender_male)))
            sex = context.getString(R.string.gender_vn_male);
        else if (gender != null && gender.equals(context.getString(R.string.gender_female))) {
            sex = context.getString(R.string.gender_vn_mafele);
        } else {
            sex = context.getString(R.string.gender_vn_any);
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
        PackageInfo pInfo;

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
        String outPut;
        byte[] data = new byte[0];
        try {
            data = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outPut = Base64.encodeToString(data, Base64.DEFAULT);
        return outPut;
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

    public static void openPlayStore(Context context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public static void showSearch(final Context context, final View view, boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right));
        } else {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_right));
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            });
        }
    }

    public static String getContentTransaction(Context context, TransactionResponse transactionResponse) {
        switch (transactionResponse.getMethod()) {
            case "promotion":
                return context.getString(R.string.transaction_promotion);
            case "bid_deposit":
                if (transactionResponse.getType().equals("in"))
                    return context.getString(R.string.transaction_bid_deposit_in, transactionResponse.getTaskName());
                else
                    return context.getString(R.string.transaction_bid_deposit_out, transactionResponse.getTaskName());
            case "reg_awarded":
                return context.getString(R.string.transaction_reg_awarded);
            case "sys_donated":
                return context.getString(R.string.transaction_sys_donated, transactionResponse.getMessage());
            case "ref_bonus":
                return context.getString(R.string.transaction_ref_bonus);
            case "promo_posting":
                return context.getString(R.string.transaction_promo_posting);
            case "wage_transfer":
                return context.getString(R.string.wage_transfer, transactionResponse.getTaskName(), transactionResponse.getFullName());
            case "wage_received":
                return context.getString(R.string.wage_received, transactionResponse.getTaskName());
            case "withdraw_bank":
                return context.getString(R.string.withdraw_bank);
            case "refund_withdraw_bank":
                return context.getString(R.string.refund_withdraw_bank);
            case "transfer_cash_account":
                return context.getString(R.string.transfer_cash);
            case "received_cash_account":
                return context.getString(R.string.received_cash);
            case "ins_ref_bonus":
                return context.getString(R.string.transaction_ref_bonus);
            case "task_ref_bonus":
                return context.getString(R.string.transaction_ref_task_bonus);
            case "bid_fee":
                if (transactionResponse.getType().equalsIgnoreCase("in"))
                    return context.getString(R.string.transaction_bid_fee_in, transactionResponse.getTaskName());
                else
                    return context.getString(R.string.transaction_bid_fee_out, transactionResponse.getTaskName());
            case "task_prepay":
                return context.getString(R.string.task_prepay, transactionResponse.getTaskName());
            case "task_refund_prepaid":
                return context.getString(R.string.task_refund_prepaid_transaction, transactionResponse.getTaskName());
            case "bid_payment_received":
                return context.getString(R.string.bid_payment_received_transaction, transactionResponse.getTaskName());
            case "bid_commission_received":
                return context.getString(R.string.bid_commission_received_transaction, transactionResponse.getTaskName());
            case "work_deduction":
                return context.getString(R.string.work_deduction_transaction, transactionResponse.getTaskName());
            case "rating_bonus":
                return context.getString(R.string.rating_bonus, transactionResponse.getTaskName());
            case "task_confirmation_penalized":
                return context.getString(R.string.task_confirmation_penalized, transactionResponse.getTaskName());
            case "visa":
                return context.getString(R.string.transaction_1pay);
            default:
                if (transactionResponse.getProvider().equals("1pay"))
                    return context.getString(R.string.transaction_1pay);
                else
                    return transactionResponse.getMethod();
        }
    }

    public static String getMemberChat(Context context, TaskResponse taskResponse) {
        String result;
        if (taskResponse.getPoster().getId() == UserManager.getMyUser().getId()) {
            if (taskResponse.getAssigneeCount() == 1) {
                result = context.getString(R.string.you) + " " + taskResponse.getAssignees().get(0).getFullName();
            } else {
                result = context.getString(R.string.you2) + " " + taskResponse.getAssignees().get(0).getFullName()
                        + " " + context.getString(R.string.count_assigner, taskResponse.getAssigneeCount() - 1);
            }
        } else {
            if (taskResponse.getAssigneeCount() == 1) {
                result = context.getString(R.string.you) + " " + taskResponse.getPoster().getFullName();
            } else {
                result = context.getString(R.string.you2) + " " + taskResponse.getPoster().getFullName()
                        + " " + context.getString(R.string.count_assigner, taskResponse.getAssigneeCount() - 1);
            }
        }

//        result = result + taskResponse.getPoster().getFullName() + " ";
//        result = result + context.getString(R.string.count_assigner, taskResponse.getAssigneeCount());

        return result;
    }

    public static void formatMoney(Context context, ArrayList<String> vnds, long mn, AutoCompleteTextView autoCompleteTextView) {
        if (mn * 10 >= MIN_BUGDET && mn * 10 <= MAX_BUGDET && mn * 10 % 1000 == 0)
            vnds.add(formatNumber(mn * 10));
        if (mn * 100 >= MIN_BUGDET && mn * 100 <= MAX_BUGDET && mn * 100 % 1000 == 0)
            vnds.add(formatNumber(mn * 100));
        if (mn * 1000 >= MIN_BUGDET && mn * 1000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 1000));
        if (mn * 10000 >= MIN_BUGDET && mn * 10000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 10000));
        if (mn * 100000 >= MIN_BUGDET && mn * 100000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 100000));
        if (mn * 1000000 >= MIN_BUGDET && mn * 1000000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 1000000));
        if (mn * 10000000 >= MIN_BUGDET && mn * 10000000 <= MAX_BUGDET)
            vnds.add(formatNumber(mn * 10000000));
        CustomArrayAdapter adapter = new CustomArrayAdapter(context, vnds);
        autoCompleteTextView.setAdapter(adapter);
    }


    public static long getLongEdittext(EdittextHozo edittextHozo) {
        String s = edittextHozo.getText().toString().replace(",", "").replace(".", "");
        return Long.valueOf(s);
    }

    public static String sortID(int posterID, int taskerID) {
        if (posterID < taskerID)
            return posterID + "-" + taskerID;
        else return taskerID + "-" + posterID;
    }

    public static Map<String, String> getHashMapFromQuery(String query)
            throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

}
