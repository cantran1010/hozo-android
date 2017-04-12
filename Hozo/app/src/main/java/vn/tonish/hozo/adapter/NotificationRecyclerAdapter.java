package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.model.NotificationMessage;

/**
 * Created by CanTran on 12/04/2017.
 */

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private final int NOTIFICATION = 0, MESSAGE = 1;
    private ArrayList<Object> itemsNotifications;

    public NotificationRecyclerAdapter(Context context, ArrayList<Object> itemsNotifications ) {
        this.context=context;
        this.itemsNotifications = itemsNotifications;
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        private TextView tvName, tvDefault, tvTime, tvDetail;
        private ImageView imgAvata, iconMessage;

        public ViewHolder1(View v) {
            super(v);
            imgAvata = (ImageView) v.findViewById(R.id.img_notification_avata);
            iconMessage = (ImageView) v.findViewById(R.id.img_notification_icon);
            tvName = (TextView) v.findViewById(R.id.tv_notification_name);
            tvDefault = (TextView) v.findViewById(R.id.tv_notification_default);
            tvTime = (TextView) v.findViewById(R.id.tv_notification_time);
        }

        public TextView getTvName() {
            return tvName;
        }

        public void setTvName(TextView tvName) {
            this.tvName = tvName;
        }

        public TextView getTvDefault() {
            return tvDefault;
        }

        public void setTvDefault(TextView tvDefault) {
            this.tvDefault = tvDefault;
        }

        public TextView getTvTime() {
            return tvTime;
        }

        public void setTvTime(TextView tvTime) {
            this.tvTime = tvTime;
        }

        public TextView getTvDetail() {
            return tvDetail;
        }

        public void setTvDetail(TextView tvDetail) {
            this.tvDetail = tvDetail;
        }

        public ImageView getImgAvata() {
            return imgAvata;
        }

        public void setImgAvata(ImageView imgAvata) {
            this.imgAvata = imgAvata;
        }

        public ImageView getIconMessage() {
            return iconMessage;
        }

        public void setIconMessage(ImageView iconMessage) {
            this.iconMessage = iconMessage;
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView imgLogo;
        private TextView tvTitle, tvContent, tvTime;

        public ViewHolder2(View v) {
            super(v);
            imgLogo = (ImageView) v.findViewById(R.id.img_notification_logo);
            tvTitle = (TextView) v.findViewById(R.id.tv_notification_title);
            tvContent = (TextView) v.findViewById(R.id.tv_notification_content);
            tvTime = (TextView) v.findViewById(R.id.tv_notification_time1);
        }

        public TextView getTvTime() {
            return tvTime;
        }

        public void setTvTime(TextView tvTime) {
            this.tvTime = tvTime;
        }

        public ImageView getImgLogo() {
            return imgLogo;
        }

        public void setImgLogo(ImageView imgLogo) {
            this.imgLogo = imgLogo;
        }

        public TextView getTvTitle() {
            return tvTitle;
        }

        public void setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
        }

        public TextView getTvContent() {
            return tvContent;
        }

        public void setTvContent(TextView tvContent) {
            this.tvContent = tvContent;
        }
    }


    @Override
    public int getItemCount() {
        return this.itemsNotifications.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemsNotifications.get(position) instanceof Notification) {
            return NOTIFICATION;
        } else if (itemsNotifications.get(position) instanceof NotificationMessage) {
            return MESSAGE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case MESSAGE:
                View v1 = inflater.inflate(R.layout.item_notifications, viewGroup, false);
                return new ViewHolder1(v1);
            case NOTIFICATION:
                View v2 = inflater.inflate(R.layout.item_notifications1, viewGroup, false);
                return new ViewHolder2(v2);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case MESSAGE:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
            case NOTIFICATION:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;

        }
    }

    private void configureViewHolder1(ViewHolder1 vh1, int position) {
        NotificationMessage mgs = (NotificationMessage) itemsNotifications.get(position);
        if (mgs != null) {
            vh1.getImgAvata().setImageResource(R.mipmap.ic_launcher);
            vh1.getIconMessage().setImageResource(R.mipmap.ic_launcher);
            vh1.getTvName().setText(mgs.getName());
            vh1.getTvDefault().setText("text mặc định") ;
            vh1.getTvTime().setText(mgs.getCreated_date()) ;

        }
    }

    private void configureViewHolder2(ViewHolder2 vh2, int position) {
        Notification notification=(Notification) itemsNotifications.get(position);
        vh2.getImgLogo().setImageResource(R.mipmap.ic_launcher);
        vh2.getTvTitle().setText(notification.getTitle());
        vh2.getTvContent().setText(notification.getContent());
        vh2.getTvTime().setText(notification.getCreated_date());
    }

}
