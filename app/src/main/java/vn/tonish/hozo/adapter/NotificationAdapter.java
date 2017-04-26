package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Notification;

/**
 * Created by CanTran on 12/04/2017.
 * Edited by HuyQuynh on 19/04/2017
 */

public class NotificationAdapter extends BaseAdapter<Notification, NotificationAdapter.NotificationHolder, LoadingHolder> {

    public Context context;
    public List<Notification> list;

    public NotificationAdapter(Context context, List<Notification> list) {
        super(context, list);
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
            //NotificationHolder holder_ = (NotificationHolder) holder;
        }
    }

    public class NotificationHolder extends BaseHolder {

        public NotificationHolder(View itemView, Context context) {
            super(itemView, context);
        }
    }
}
