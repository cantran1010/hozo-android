package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

import static android.content.ContentValues.TAG;

/**
 * Created by CanTran on 12/04/2017.
 * Edited by HuyQuynh on 19/04/2017
 */

public class NotificationAdapter extends BaseAdapter<Notification, NotificationAdapter.NotificationHolder, LoadingHolder> {

    public List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, notifications);
        this.notifications = notifications;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationHolder) {
            NotificationHolder notificationHolder = (NotificationHolder) holder;

            Utils.displayImageAvatar(context, notificationHolder.imgAvata, notifications.get(position).getAvatar());
            Utils.setContentMessage(context,notificationHolder.tvContent,notifications.get(position));
            notificationHolder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(notifications.get(position).getCreatedAt(), context));

            LogUtils.d(TAG, "NotificationAdapter , notification : " + notifications.get(position).toString());
        }
    }

    public class NotificationHolder extends BaseHolder {

        private CircleImageView imgAvata;
        private TextViewHozo tvContent, tvTimeAgo;

        public NotificationHolder(View itemView, Context context) {
            super(itemView, context);
            imgAvata = (CircleImageView) itemView.findViewById(R.id.img_avatar);
            tvContent = (TextViewHozo) itemView.findViewById(R.id.tv_content);
            tvTimeAgo = (TextViewHozo) itemView.findViewById(R.id.tv_time_ago);
        }
    }
}
