package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.ProfileActivity;
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

            if (notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_USER)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_USER)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_TASK)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ACTIVE_COMMENT)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_TASK)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_BLOCK_COMMENT)
                    || notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_NEW_TASK_ALERT)) {
                notificationHolder.imgAvata.setImageResource(R.mipmap.app_icon);
                notificationHolder.tvContent.setText(notification.getContent());


                String matcher = context.getString(R.string.term_and_policy);

                SpannableString spannable = new SpannableString(notificationHolder.tvContent.getText().toString());

                Pattern patternId = Pattern.compile(matcher);
                Matcher matcherId = patternId.matcher(notificationHolder.tvContent.getText().toString());
//                while (matcherId.find()) {
//                    spannable.setSpan(new ClickableSpan() {
//                        @Override
//                        public void onClick(View widget) {
////                            Utils.openGeneralInfoActivity(context, context.getString(R.string.other_condition), "http://hozo.vn/dieu-khoan-su-dung/?ref=app");
//                            notificationAdapterListener.onNotificationAdapterListener(position);
//                        }
//                    }, matcherId.start(), matcherId.end(), 0);
////            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#00A2E5")), matcherId.start(), matcherId.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }

                while (matcherId.find()) {
                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#00A2E5")), matcherId.start(), matcherId.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                notificationHolder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
                notificationHolder.tvContent.setText(spannable);
                notificationHolder.tvContent.setContentDescription(spannable);


            } else {
                Utils.displayImageAvatar(context, notificationHolder.imgAvata, notifications.get(position).getAvatar());
                Utils.setContentMessage(context, notificationHolder.tvContent, notifications.get(position));
            }

            notificationHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationAdapterListener.onNotificationAdapterListener(holder.getAdapterPosition());
                }
            });

            notificationHolder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(notifications.get(position).getCreatedAt(), context));

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

            LogUtils.d(TAG, "NotificationAdapter , notification : " + notification.toString());
        }
    }

    public class NotificationHolder extends BaseHolder implements View.OnClickListener {

        private final CircleImageView imgAvata;
        private final TextViewHozo tvContent;
        private final TextViewHozo tvTimeAgo;

        public NotificationHolder(View itemView, Context context) {
            super(itemView, context);
            imgAvata = (CircleImageView) itemView.findViewById(R.id.img_avatar);
            tvContent = (TextViewHozo) itemView.findViewById(R.id.tv_content);
            tvTimeAgo = (TextViewHozo) itemView.findViewById(R.id.tv_time_ago);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (notificationAdapterListener != null)
                notificationAdapterListener.onNotificationAdapterListener(getAdapterPosition());
        }
    }
}
