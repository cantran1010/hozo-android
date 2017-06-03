package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.other.ProfileActivity;
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
        return R.layout.adapter_notifications;
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

            Notification notification = notifications.get(position);

            if (notification.getEvent().equals(Constants.PUSH_TYPE_ADMIN_PUSH)) {
                notificationHolder.imgAvata.setImageResource(R.drawable.ic_launcher);
                notificationHolder.tvContent.setText(notification.getContent());
                notificationHolder.tvTask.setVisibility(View.GONE);
            } else {
                Utils.displayImageAvatar(context, notificationHolder.imgAvata, notifications.get(position).getAvatar());
                Utils.setContentMessage(context, notificationHolder.tvContent, notifications.get(position));
                notificationHolder.tvTask.setVisibility(View.VISIBLE);
                notificationHolder.tvTask.setText(notifications.get(position).getTaskName());
            }

            notificationHolder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(notifications.get(position).getCreatedAt(), context));

            notificationHolder.imgAvata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(Constants.USER_ID, notifications.get(holder.getAdapterPosition()).getUserId());
                    intent.putExtra(Constants.IS_MY_USER, notifications.get(holder.getAdapterPosition()).getUserId() == UserManager.getMyUser().getId());
                    context.startActivity(intent);
                }
            });

            LogUtils.d(TAG, "NotificationAdapter , notification : " + notifications.get(position).toString());
        }
    }

    public class NotificationHolder extends BaseHolder implements View.OnClickListener {

        private final CircleImageView imgAvata;
        private final TextViewHozo tvContent;
        private final TextViewHozo tvTask;
        private final TextViewHozo tvTimeAgo;

        public NotificationHolder(View itemView, Context context) {
            super(itemView, context);
            imgAvata = (CircleImageView) itemView.findViewById(R.id.img_avatar);
            tvContent = (TextViewHozo) itemView.findViewById(R.id.tv_content);
            tvTimeAgo = (TextViewHozo) itemView.findViewById(R.id.tv_time_ago);
            tvTask = (TextViewHozo) itemView.findViewById(R.id.tv_task);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (notificationAdapterListener != null)
                notificationAdapterListener.onNotificationAdapterListener(getAdapterPosition());
        }
    }
}
