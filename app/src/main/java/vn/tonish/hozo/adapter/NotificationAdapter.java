package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

import static android.content.ContentValues.TAG;

/**
 * Created by LongBui on 4/12/17.
 */

public class NotificationAdapter extends BaseAdapter<Notification, NotificationAdapter.NotificationHolder, LoadingHolder> {

    private final List<Notification> notifications;
    private final Context context;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, notifications);
        this.notifications = notifications;
        this.context = context;
    }

    public interface NotificationAdapterListener {
        void onNotificationAdapterListener(int position);
    }

    private NotificationAdapterListener notificationAdapterListener;

    public NotificationAdapterListener getNotificationAdapterListener() {
        return notificationAdapterListener;
    }

    public void setNotificationAdapterListener(NotificationAdapterListener notificationAdapterListener) {
        this.notificationAdapterListener = notificationAdapterListener;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_notifications;
    }

    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public NotificationHolder returnItemHolder(View view) {
        return new NotificationHolder(view, context);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationHolder) {
            NotificationHolder notificationHolder = (NotificationHolder) holder;

            final Notification notification = notifications.get(position);
            LogUtils.d(TAG, "onBindViewHolder , notification : " + notification.toString());
            switch (notification.getEvent()) {
                case Constants.PUSH_TYPE_ADMIN_PUSH:
                case Constants.PUSH_TYPE_BLOCK_USER:
                case Constants.PUSH_TYPE_ACTIVE_USER:
                case Constants.PUSH_TYPE_ACTIVE_TASK:
                case Constants.PUSH_TYPE_ACTIVE_COMMENT:
                case Constants.PUSH_TYPE_BLOCK_TASK:
                case Constants.PUSH_TYPE_BLOCK_COMMENT:
                case Constants.PUSH_TYPE_ADMIN_NEW_TASK_ALERT:
                    notificationHolder.imgAvata.setImageResource(R.mipmap.app_icon);
                    notificationHolder.tvContent.setText(notification.getContent());
                    String matcher = context.getString(R.string.term_and_policy);
                    SpannableString spannable = new SpannableString(notificationHolder.tvContent.getText().toString());

                    Pattern patternId = Pattern.compile(matcher);
                    Matcher matcherId = patternId.matcher(notificationHolder.tvContent.getText().toString());
                    while (matcherId.find()) {
                        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#00A2E5")), matcherId.start(), matcherId.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    notificationHolder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
                    notificationHolder.tvContent.setText(spannable);
                    notificationHolder.tvContent.setContentDescription(spannable);

                    break;
                case Constants.PUSH_TYPE_NEW_TASK_ALERT:
                    String strContent = "";
                    if (notification.isUrgency()) {
                        strContent = context.getString(R.string.notification_new_task_alert_title_urgency) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_new_task_alert_day_urgency) + " " + DateTimeUtils.getOnlyDateFromIso(notification.getTaskStartTime()) + "! " + context.getString(R.string.notification_new_task_alert_footer_urgency);
                    } else {
                        Utils.displayImageAvatar(context, notificationHolder.imgAvata, notification.getAvatar());
                        strContent = context.getString(R.string.notification_new_task_alert_title) + " " + notification.getTaskName() + " " + context.getString(R.string.notification_new_task_alert_day) + " " + DateTimeUtils.getOnlyDateFromIso(notification.getTaskStartTime()) + " " + context.getString(R.string.notification_new_task_alert_footer);
                    }
                    notificationHolder.tvContent.setText(strContent);


                    SpannableStringBuilder spannable1 = new SpannableStringBuilder(notificationHolder.tvContent.getText().toString());

                    int fromMatcher = notificationHolder.tvContent.getText().toString().indexOf(notification.getTaskName());
                    int toMatcher = fromMatcher + notification.getTaskName().length();
                    if (fromMatcher >= 0) {
                        spannable1.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), fromMatcher, toMatcher, 0);
                    }
                    int fromMatcher1 = notificationHolder.tvContent.getText().toString().indexOf(DateTimeUtils.getOnlyDateFromIso(notification.getTaskStartTime()));
                    int toMatcher1 = fromMatcher1 + DateTimeUtils.getOnlyDateFromIso(notification.getTaskStartTime()).length();
                    if (fromMatcher1 >= 0) {
                        spannable1.setSpan(new android.text.style.StyleSpan(Typeface.BOLD), fromMatcher1, toMatcher1, 0);
                    }

                    notificationHolder.tvContent.setText(spannable1);
                    notificationHolder.tvContent.setContentDescription(spannable1);


                    break;
                case Constants.PUSH_TYPE_MONEY_RECEIVED:
                    notificationHolder.imgAvata.setImageResource(R.mipmap.app_icon);
                    Utils.setContentMessage(context, notificationHolder.tvContent, notification);
                    break;
                case Constants.PUSH_TYPE_BID_REFUNDED:
                    notificationHolder.imgAvata.setImageResource(R.mipmap.app_icon);
                    Utils.setContentMessage(context, notificationHolder.tvContent, notification);
                    break;
                default:
                    Utils.displayImageAvatar(context, notificationHolder.imgAvata, notification.getAvatar());
                    Utils.setContentMessage(context, notificationHolder.tvContent, notification);
                    break;
            }

            notificationHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationAdapterListener.onNotificationAdapterListener(holder.getAdapterPosition());
                }
            });

            notificationHolder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(notification.getCreatedAt(), context));

            notificationHolder.imgAvata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)) {
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra(Constants.USER_ID, notification.getUserId());
                        intent.putExtra(Constants.IS_MY_USER, notification.getUserId() == UserManager.getMyUser().getId());
                        context.startActivity(intent);
                    }
                }
            });

            if (notification.getRead()) {
                if (notification.getEvent().equalsIgnoreCase(Constants.PUSH_TYPE_NEW_TASK_ALERT)) {
                    notificationHolder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.color_create_task_lable));
                } else
                    notificationHolder.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            } else {
                if (notification.getEvent().equalsIgnoreCase(Constants.PUSH_TYPE_NEW_TASK_ALERT)) {
                    notificationHolder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.hozo_bg));
                } else
                    notificationHolder.mainLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.notify_unread));
            }

            LogUtils.d(TAG, "NotificationAdapter , notification : " + notification.toString());
        }
    }

    public class NotificationHolder extends BaseHolder implements View.OnClickListener {
        private final CircleImageView imgAvata;
        private final TextViewHozo tvContent;
        private final TextViewHozo tvTimeAgo;
        private final LinearLayout mainLayout;

        public NotificationHolder(View itemView, Context context) {
            super(itemView, context);
            imgAvata = itemView.findViewById(R.id.img_avatar);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_ago);
            mainLayout = itemView.findViewById(R.id.main_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (notificationAdapterListener != null)
                notificationAdapterListener.onNotificationAdapterListener(getAdapterPosition());
        }
    }
}
